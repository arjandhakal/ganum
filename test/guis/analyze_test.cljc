(ns guis.analyze-test
  (:require [clojure.test :refer [deftest testing is]]
            [guis.analyze :as analyze]))


(deftest get-document-term-counts-test
  (testing "splits the word correctly based on spaces"
    (is (= (-> "Jon Bon Jovi performs first full Bon Jovi concert in over three years"
               analyze/get-document-term-counts)
           {"jon" 1
            "bon" 2
            "jovi" 2
            "performs" 1
            "first" 1
            "full" 1
            "concert" 1
            "in" 1
            "over" 1
            "three" 1
            "years" 1}))))

