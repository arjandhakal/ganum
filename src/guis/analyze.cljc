(ns guis.analyze)

(defn render-ui [state]
  [:div.flex.flex-col.gap-2
   "Analyzing some files"
   [:input {:type "file"
            :name "files"
            :multiple true}]])


