(ns reading.state-test
  (:require [reading.state :as state]
            [cljs.test :refer [deftest is testing]]))

(deftest initial-state-test
  (is (= []   (:books state/initial-state)))
  (is (= ""   (:draft state/initial-state)))
  (is (= :all (:show  state/initial-state)))) 
