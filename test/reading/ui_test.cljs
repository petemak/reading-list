(ns reading.ui-test
  (:require [reading.ui :as ui]
            [reading.state :as state]
            [reading.test-util :as tu]
            [cljs.test :refer [deftest is testing]]))


(deftest form-test
  (let [app (ui/render-app (assoc state/initial-state :draft "Kin"))
        [form] (tu/find-tag app "form")
        [input] (tu/find-tag app "input")]
    (testing "submitting the form adds a book"
      (is (= [[:action/prevent-default] [:action/add-book]]
             (get-in (tu/attrs form) [:on :submit]))))
    (testing "the input shows the draft and stores every keystroke"
      (is (= "Kin" (:value (tu/attrs input))))
      (is (= [[:action/assoc-in [:draft] :event/target.value]]
             (get-in (tu/attrs input) [:on :input])))))) 

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



(def two-books
  (assoc state/initial-state
         :books [{:id 1 :title "Kindred" :read? false}
                 {:id 2 :title "Beloved" :read? true}]))


(deftest filter-button-test
  (let [active   (ui/filter-button :read :read "Read")
        inactive (ui/filter-button :all :read "Read")]
    (is (= "active" (:class (tu/attrs active))))
    (is (nil? (:class (tu/attrs inactive))))
    (is (= [[:action/assoc-in [:show] :read]] (get-in (tu/attrs active) [:on :click])))))

(deftest only-visible-books-are-listed
  (let [app (ui/render-app (assoc two-books :show :unread))]
    (is (= ["Kindred"] (map tu/text (tu/find-tag app "span"))))))

(deftest empty-message-when-nothing-matches
  (let [app (ui/render-app (assoc two-books :show :read
                                  :books [{:id 1 :title "Kindred" :read? false}]))]
    (is (some #(= "Nothing to show. Add a title above, or pick another filter." (tu/text %))
              (tu/find-tag app "p")))))


(defn progress-nodes [app]
  (filter #(= ui/progress (first %)) (tu/nodes app)))


(deftest progress-test
  (testing "hidden when there are no books"
    (is (empty? (progress-nodes (ui/render-app state/initial-state)))))
  (testing "counts read books out of all books"
    (let [[p] (progress-nodes (ui/render-app two-books))]
      (is (= {::ui/done 1 ::ui/total 2} (tu/attrs p))))))
