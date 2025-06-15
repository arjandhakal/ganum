(ns guis.core
  (:require [guis.analyze :as analyze]
            [replicant.dom :as r]
            [clojure.walk :as walk]
            [cljs.core.async :refer [>! <! go chan go-loop ]]))

(def views
  [{:id :analyze
    :text "Analyze"}])


(defn get-current-view [state]
  (:current-view state :analyze))

(defn render-ui [state]
  (let [current-view (get-current-view state)]
    (case current-view
      :analyze
      (analyze/render-ui state))))

(defn read-file
  "Takes a Javascript file and reads it asynchronously, returning a channel."
  [file]
  (let [out-ch (chan)]
    (let [reader (js/FileReader.)]
      (set! (.-onload reader)
            (fn [event]
              (go (>! out-ch {:name (.-name file)
                              :type (.-type file)
                              :content (-> event .-target .-result)}))))
      (.readAsText reader file))
    out-ch)) 


(defn read-user-files
  [store [update-path file-list]]
  (go
    (let [channels (mapv read-file file-list)
          read-files (loop [chs channels
                            acc []]
                       (if (empty? chs)
                         acc
                         (let [result (<! (first chs))]
                           (recur (rest chs) (conj acc result)))))] 
      (swap! store assoc-in update-path read-files)
      (swap! store assoc-in [:uploads :reading-content?] false)
      (prn @store))))

(defn perform-actions [state event-data]
  (mapcat
   (fn [action]
     (prn (first action) (rest action))
     (or (analyze/perform-action state action)
         (prn "Unknown action")))
   event-data))

(defn process-effect [store [effect & args]]
  (prn effect args)
  (case effect
    :effect/read-file-content
    (do (read-user-files store args)
        (prn @store))
    :effect/assoc-in
    (apply swap! store assoc-in args)))


(defn interpolate [event data]
  (walk/postwalk
   (fn [x]
     (case x
       :event.target/files
       (some-> event .-target .-files)
       x))
   data))


(defn init [store]
  (add-watch store ::render (fn [_ _ _ new-state]
                              (r/render
                               js/document.body
                               (render-ui new-state))))

  ;; Listening to all the events
  (r/set-dispatch!
   (fn [{:replicant/keys [dom-event]} event-data]
     (->> (interpolate dom-event event-data)
          (perform-actions @store)
          (run! #(process-effect store %)))))

  (swap! store assoc ::loaded-at (.getTime (js/Date.))))

