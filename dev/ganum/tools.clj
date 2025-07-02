(ns ganum.tools)

(comment

  (require '[clojure.repl.deps :refer [add-libs]])

  (add-libs '{bidi/bidi {:mvn/version "2.1.6"}})

  (def route ["/index.html" :index])
  (b/match-route route "/index.html")

  (add-libs '{io.replikativ/konserve {:mvn/version "0.8.321"}})
  ;
  )

