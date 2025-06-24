(ns algorithms.tfidf 
  (:require
    [clojure.string :as str]
    [clojure.math :as math]))

;; TODO : Move the list to an EDN file
(def stop-words #{"i" "me" "my" "myself" "we" "our" "ours" "ourselves" "you" "your" "yours" "yourself" "yourselves" "he" "him" "his" "himself" "she" "her" "hers" "herself" "it" "its" "itself" "they" "them" "their" "theirs" "themselves" "what" "which" "who" "whom" "this" "that" "these" "those" "am" "is" "are" "was" "were" "be" "been" "being" "have" "has" "had" "having" "do" "does" "did" "doing" "a" "an" "the" "and" "but" "if" "or" "because" "as" "until" "while" "of" "at" "by" "for" "with" "about" "against" "between" "into" "through" "during" "before" "after" "above" "below" "to" "from" "up" "down" "in" "out" "on" "off" "over" "under" "again" "further" "then" "once" "here" "there" "when" "where" "why" "how" "all" "any" "both" "each" "few" "more" "most" "other" "some" "such" "no" "nor" "not" "only" "own" "same" "so" "than" "too" "very" "s" "t" "can" "will" "just" "don" "should" "now"})


(defn- split-into-tokens [content]
  (re-seq #"[a-zA-Z0-9]+" content))

(defn- lowercase-words [tokens]
  (map str/lower-case tokens))

(defn- filter-stop-words [tokens]
  (remove stop-words tokens))

(defn tokenize
  "Changes the document content into a sequence of words"
  [content]
  (-> content
        split-into-tokens
        lowercase-words
        filter-stop-words
        ;; TODO (Stemming and Lemmatization)
        ))

(defn term-frequency
  "Calculate the logarithmic scale term frequency [tf]"
  [term word-frequencies]
  (math/log (+ 1
               (get word-frequencies term 0))))


(defn inverse-document-frequency
  "Calculates the inverse document frequency of a term [idf].
   - term: term calculating idf for 
   - doc-frequencies: map of {term -> number of docs containing term}
   - total-docs : total number of documents in the corpus"
  [term doc-frequencies total-docs]
  (let [df (get doc-frequencies term 0)]
    (math/log (/ total-docs
                 (+ 1 df)))))

(defn tf-idf
  "Calculates the tf-idf of a term"
  [term word-frequencies doc-frequencies total-docs]
  (let [tf (term-frequency term word-frequencies)
        idf (inverse-document-frequency term doc-frequencies total-docs)]
    (* tf idf)))


(defn tfidf-analyze [files]
  (prn files))



