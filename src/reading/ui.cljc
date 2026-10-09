(ns reading.ui)

(defn book-item
  [book]
  [:li {:replicant/key (:id book)}
   [:span (:title book)]])


(defn render-app
  [state]
  [:main.app
   [:h1 "Reading List"]
   [:hr]
   [:form {:on {:submit [[:action/prevent-default] [:action/add-book]]}}
    [:input {:type "text"
             :placeholder "Add a book title"
             :value (:draft state)
             :on {:input [[:action/assoc-in [:draft] :event/target.value]]}}]
    [:button {:type "submit"} "Add"]]
   
   
   (if (empty? (:books state))
     [:p.empty "Opps.. No books on the list yet!"]
     [:ul.books (map book-item (:books state))])
   
   [:hr]
   [:button {:on {:click (fn [e] (js/alert "Hi"))}} "Say hi"]])
   
