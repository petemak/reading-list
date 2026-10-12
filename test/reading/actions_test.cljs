(ns reading.actions-test
  (:require [reading.ui :as ui]
            [reading.state :as state]
            [reading.actions :as actions]
            [reading.test-util :as tu]
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


(deftest toggle-and-remove-test
  (let [store (atom (assoc state/initial-state
                           :books [{:id 1 :title "Kindred" :read? false}]))]
    (actions/handle! store (fake-event nil) [[:action/toggle-read 1]])
    (is (true? (:read? (first (:books @store)))))

    (actions/handle! store (fake-event nil) [[:action/remove-book 1]])
    (is (empty? (:books @store)))))

(deftest book-item-test
  (let [li (ui/book-item {:id 42 :title "Kindred" :read? false})
        [checkbox] (tu/find-tag li "input")
        [button] (tu/find-tag li "button")]

    (testing "each item is keyed by the book's id"
      (is (= 42 (:replicant/key (tu/attrs li)))))

    (testing "the controls send actions for this book"
      (is (= [[:action/toggle-read 42]] (get-in (tu/attrs checkbox) [:on :change])))
      (is (= [[:action/remove-book 42]] (get-in (tu/attrs button) [:on :click]))))

    (testing "read books get the read class"
      (is (nil? (:class (tu/attrs li))))
      (is (= "read" (:class (tu/attrs (ui/book-item {:id 1 :title "x" :read? true}))))))))
