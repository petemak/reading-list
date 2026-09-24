(ns reading.ui)

(defn book-item
  [book]
  [:li {:replicant/key (:id book)}
   [:span (:title book)]])


(defn render-app
  [state]
  [:main.app
   [:h1 "Reading List"]
   (if (empty? (:books state))
     [:p.empty "No books yet."]
     [:ul.books (map book-item (:books state))])
   [:hr]])
