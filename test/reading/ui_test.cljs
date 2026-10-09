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
