(ns guis.analyze
  (:require [clojure.string :as str]
            [algorithms.tfidf :as tfidf]))


(defn tokenize
  "Changes the document content into a sequence of words"
  [content]
  (let [terms (str/split content #" ")
        lc-terms (map str/lower-case terms)]
    (frequencies lc-terms)))

(defn tfidf-analyze [files]
  "")

(defn perform-action [state [action & args]]
  (cond
    (= ::upload-files action) [[:effect/assoc-in [:uploads :reading-content?] true]
                               [:effect/read-file-content [:uploads :files] (:files (first args))]]
    (= :tfidf-analyze action)
     (tfidf-analyze (-> state
                        :uploads
                        :files))))

(def supported-files ".doc,.docx,.xml,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,.txt,.md,.org,.csv,.rtf,.log,text/plain,text/markdown,text/org,text/csv,application/rtf,text/x-log")


(defn render-ui [state]
  [:div.flex.flex-col.gap-2.w-3xs.m-auto
   [:h1 "Analyze Your Text Files"]
   [:input.file-input.file-input-primary {:type "file"
                                          :name "files"
                                          :disabled (some-> state
                                                            :uploads
                                                            :reading-content?)
                                          :multiple true
                                          :on {:change [[::upload-files
                                                         {:files :event.target/files}]]}
                                          :accept supported-files
                                          :id "analyze-file-upload"}]
   [:button {:class "btn btn-info"
             :on {:click [[:tfidf-analyze]]}} "Analyze"]])


(defn render-button [state]
  [:button (if (= (:color state) "red")
             "red button"
             "other button")])
