(ns reading.state
  (:require [clojure.string :as str]))

(def initial-state
  {:draft ""
   :show :all
   :books []})


(defn add-book [state]
  (let [title (str/trim (:draft state))]
    (if (str/blank? title)
      state
      (-> state
          (update :books conj {:id (random-uuid)
                               :title title
                               :read? false})
          (assoc :draft "")))))
