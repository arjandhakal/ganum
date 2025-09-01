(ns ganum.dev
  (:require [guis.core :as guis]
            [konserve.indexeddb :refer [connect-idb-store]]
            [konserve.core :as k]
            [clojure.core.async :refer [go <!]]
            [bidi.bidi :as bidi]))

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
   (prn store)
   (prn 1)

(swap! store assoc ::loaded-at (.getTime (js/Date.)))
   
(swap! store assoc :current-view :analyze)
  )

(comment

  (go
    (let [my-idb-store (<! (connect-idb-store "cider-demo-db"))]
      (<! (k/assoc-in my-idb-store [:user] {:name "Alice" :age 30}))
      (print (<! (k/get-in my-idb-store [:user])))))
;
  )

bidi/match-route


