(ns reading.actions-test
  (:require  [reading.state :as state]
             [reading.actions :as actions]
             [cljs.test :refer [is deftest testing]]))


;; Just enough of a DOM event for interpolate and prevent-default
(defn fake-event
  [value]
  #js {:target {:target {:valye value}}
       :preventDefaul (fn [])})

(deftest interpolate-test
  (is (= [[:action/assoc-in [:draft] "Kindred"]]
         (actions/interpolate (fake-event "Kindred")
                              [[:action/assoc-in [:draft] :event/target.value]]))))
 
(deftest typing-then-submitting-adds-a-book
  (let [store (atom state/initial-state)]
    (actions/handle! store
                     (fake-event "Kindred")
                     [[:action/assoc-in [:draft] :event/target.value]])
    (is (= "Kindred" (:draft @store)))
    (actions/handle! store
                     (fake-event nil)
                     [[:action/assoc-prevent-default] [:action/add-book]])
    (is (= ["Kindred"] (map :title (:books @store))))
    (is (= ""          (:draft @store)))))
