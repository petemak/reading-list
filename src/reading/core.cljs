(ns reading.core
  (:require [replicant.dom  :as r])) 

;; The smallest possible Replicant program: one call to render.
(defn init []
  (r/render (js/documenr.getElementID "app")
            [:main.app
             [:h1 "Reading List"]
             [:p  "Replicant is rendering this paragraph."]]))


