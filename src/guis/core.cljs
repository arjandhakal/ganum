(ns guis.core
  (:require [guis.analyze :as analyze]
            [replicant.dom :as r]))

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

(defn init [store]
  (add-watch store ::render (fn [_ _ _ new-state]
                              (r/render
                               js/document.body
                               (render-ui new-state))))
  (swap! store assoc ::loaded-at (.getTime (js/Date.))))

