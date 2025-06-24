(ns algorithms.tdidf-test
  (:require [clojure.test :refer [deftest testing is]]
            [algorithms.tfidf :as tfidf]
            [clojure.math :as math]))


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
             "years"}))))

(deftest tfidf-tf-test
  (testing "the term frequence tf(t,d)"
    (is (= (tfidf/term-frequency "jon" (frequencies ["jon" "john" "bon" "son"]))
           (math/log (+ 1 1))))
    (is (= (tfidf/term-frequency "jon" (frequencies ["jon" "jon" "bon" "son"]))
           (math/log (+ 2 1))))))


(deftest tfidf-inverse-document-frequency-test
  (let [fault-tolerance 0.001
        total-docs 1000
        doc-frequency {"clojure" 200 ; A moderately common term
                       "powerful" 5 ; A rare term
                       "man" 900 ; A super common term
                       }]
    (testing "for a moderately common term"
      (let [expected-idf (math/log (/ 1000 (inc 200)))]
        (is (< (abs (- expected-idf
                       (tfidf/inverse-document-frequency "clojure" doc-frequency total-docs)))
               fault-tolerance))))

    (testing "for a rare term"
      (let [expected-idf (math/log (/ 1000 (inc 5)))]
        (is (< (abs (- expected-idf
                       (tfidf/inverse-document-frequency "powerful" doc-frequency total-docs)))
               fault-tolerance))))

    (testing "for a super common term"
      (let [expected-idf (math/log (/ 1000 (inc 5)))]
        (is (< (abs (- expected-idf
                       (tfidf/inverse-document-frequency "powerful" doc-frequency total-docs)))
               fault-tolerance))))))
