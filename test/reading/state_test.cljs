(ns reading.state-test
  (:require [reading.state :as state]
            [cljs.test :refer [deftest is testing]]))

(deftest initial-state-test
  (is (= ""   (:draft state/initial-state)))
  (is (= :all (:show  state/initial-state)))
  (is (= []   (:books state/initial-state))))

(deftest add-book-test
  (testing "adds the draft as an anread book and clears the draft"
    (let [s (state/add-book (assoc state/initial-state :draft "Kindred"))]
      (is (= "" (:draft s)))
      (is (= [{:title "Kindred" :read? false}]
             (map #(dissoc % :id) (:books s))))
      (is (= (uuid? (:id (first (:books s))))))))
  (testing "trims surrounding whitespace"
    (is (= "Kindred"
           (-> (assoc state/initial-state :draft " Kindred ")
               state/add-book :books first :title ))))
  (testing "ignores a blank draft: no book added, state unchaged"
    (let [s (assoc state/initial-state :draft "  ")] 
      (is (= s (state/add-book s))))))
 
