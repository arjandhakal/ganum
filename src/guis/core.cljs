(ns guis.core
  (:require [guis.analyze :as analyze]
            [guis.files :as files]
            [replicant.dom :as r]
            [clojure.walk :as walk]
            [cljs.core.async :refer [>! <! go chan go-loop]]
            [bidi.bidi :as router]
            [guis.components.navbar :as navbar]))

(def routes
  ["/" {"" :home
        "files" :files
        "analyze" :analyze}])

(defmulti get-view-data identity)

(defmethod get-view-data :home [_] {:id :home
                            :text "Home"})
(defmethod get-view-data :files [_] {:id :files
                                 :text "Files"})
(defmethod get-view-data :analyze [_] {:id :analyze
                               :text "Analyze"})

(defn get-current-view [state]
  (:current-view state :home))


(defn update-browser-history [location]
  (.pushState (.-history js/window) {} "" (if (= location "")
                                            "/"
                                            location)))

(defn nav-perform-action [_state [action & args]]
  (let [location (:location (first args))]
    (cond (= :gui/navigate action)
          (do
            (update-browser-history (or location "/"))
            [[:effect/assoc-in [:current-view] (:handler (router/match-route routes (:location (first args))))]]))))

(defn render-ui [state]
  (let [current-view (get-current-view state)]
    [:div.flex.flex-col.gap-2.m-auto.max-w-7xl.p-2
     (navbar/responsive-navbar)
     (case current-view
       :analyze
       (analyze/render-ui state)
       :home
       [:h1 "Home"]
       :files
       (files/render-ui state)
       [:h1 "Default"])]))


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
         (nav-perform-action state action)
         (prn (str "Unknown action" action))))
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
       :event.navigate/location
       (some-> js/window .-location .-pathname)
       x))
   data))


(defn- on-pop-state [_event]
  (prn _event)) 

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

  ;; Dispatching the initial route)
  (swap! store assoc :current-view (:handler (router/match-route routes (-> js/window .-location .-pathname))))
  
  (swap! store assoc :loaded-at (.getTime (js/Date.))))
(prn (-> js/window .-history))


