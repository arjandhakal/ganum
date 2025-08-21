(ns ganum.tools)

(comment
  ;; Trying out repl 
  (require '[clojure.repl.deps :refer [add-libs]])

  (add-libs '{bidi/bidi {:mvn/version "2.1.6"}})
  (add-libs '{io.replikativ/konserve {:mvn/version "0.8.321"}})

  (add-libs '{metosin/reitit-core {:mvn/version  "0.9.1"}})
  ;
  )


(require '[bidi.bidi :as b] )


