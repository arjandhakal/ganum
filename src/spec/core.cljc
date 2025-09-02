(ns spec.core
  (:require [clojure.spec.alpha :as s])) 

(s/def :ganum.guis/current-view #{:analyze :home :files})

(s/def :ganum.guis.core/loaded-at int?)

(s/def :ganum.guis.uploads/reading-content? boolean?)
(s/def :ganum.guis.uploads.file/content string?)
(s/def :ganum.guis.uploads.file/type string?)
(s/def :ganum.guis.uploads/file  (s/keys :req-un [:ganum.guis.uploads.file/content :ganum.guis.uploads.file/type]))
(s/def :ganum.guis.uploads/files (s/coll-of :ganum.guis.uploads/file))
(s/def :ganum.guis/uploads (s/keys :req-un [:ganum.guis.uploads/reading-content? :ganum.guis.uploads/files]))

(s/def :ganum.guis/state (s/keys :req-un [:ganum.guis/current-view :ganum.guis.core/loaded-at]
                                 :opt-un [:ganum.guis/uploads]))









