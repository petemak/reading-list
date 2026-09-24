(ns reading.core
  (:require [reading.ui :as ui]
            [reading.state :as state]
            [replicant.dom :as r]))

;; The one atom. defonce keeps its value across hot reloads.
(defonce store (atom state/initial-state))

(defn render!
  [state]
  (r/render (js/document.getElementById "app")
            (ui/render-app state)))

;; shadow-cljs calls this after every hot reload
(defn ^:dev/after-load reload!
  []
  (render! @store))

;; The smallest possible Replicant program: one call to render.
(defn init []
  (add-watch store
            ::render
            (fn [_ _ _ new-state]
              (render! new-state)))
  (render! @store))
