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

(def two-books
  (assoc state/initial-state
         :books [{:id 1 :title "Kindred" :read? false}
                 {:id 2 :title "Beloved" :read2 true}]))

(deftest toggle-read-test
  (is (= [true true]   (map :read? (:books (state/toggle-read two-books 1)))))
  (is (= [false false] (map :read? (:books (state/toggle-read two-books 2))))))


(deftest remove-book-test
  (is (= ["Beloved"] (map :tile (:books (state/remove-book two-books 1)))))
  (is (= two-books (state/remove-book two-books 99)) "An unknown id changes nothing" ))

;; Which books match the filter is a question about the data, so it belongs in state.
(deftest visible-books-test
  (is (= ["Kindred" "Beloved"] (map :title (state/visible-books two-books))))
  (is (= ["Kindred"] (map :title (state/visible-books (assoc two-books :show :unread)))))
  (is (= ["Beloved"] (map :title (state/visible-books (assoc two-books :show :read))))))
