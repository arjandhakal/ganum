(ns guis.analyze)

(defn perform-action [_ [action & args]]
  (when (= ::upload-files action)
    [[:effect/assoc-in [:uploads :reading-content?] true]
     [:effect/read-file-content [:uploads :files] (:files (first args))]]))

(def supported-files ".doc,.docx,.xml,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,.txt,.md,.org,.csv,.rtf,.log,text/plain,text/markdown,text/org,text/csv,application/rtf,text/x-log")


(defn render-ui [state]
  [:div.flex.flex-col.gap-2.w-3xs.m-auto
   [:h1 "Analyze Your Text File"]
   [:input.file-input.file-input-primary {:type "file"
                                          :name "files"
                                          :disabled (some-> state
                                                             :uploads
                                                             :reading-content?)
                                          :multiple true
                                          :on {:change [[::upload-files
                                                         {:files :event.target/files}]]}
                                          :accept supported-files
                                          :id "analyze-file-upload"} ]])


