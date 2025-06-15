(ns ganum.dev
  (:require [guis.core :as guis]))

(defonce store (atom {:current-view :analyze}))

(defn main []
  (guis/init store)
  (println "loaded"))

(defn ^:dev/after-load reload []
  (guis/init store)
  (println "また"))
(comment

  ;;start the guis
  (main)

  (reload)
  )


