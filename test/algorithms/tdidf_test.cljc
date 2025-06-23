(ns algorithms.tdidf-test
  (:require [clojure.test :refer [deftest testing is]]
            [algorithms.tfidf :as tfidf]))


(deftest tfidf-tokenize-document-test
  (testing "splits the words correctly"
    (is (= (into #{} (-> "Jon Bon Jovi, performs first full Bon Jovi concert in
over three years!!"
                         tfidf/tokenize))
           #{"jon" 
            "bon" 
            "jovi" 
            "performs" 
            "first" 
            "full" 
            "concert" 
            "three" 
            "years" }))))
