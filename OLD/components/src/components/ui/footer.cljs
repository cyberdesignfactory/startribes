(ns components.ui.footer)

(defn footer [{:keys [style title description]}]
  [:div.text-center.bg-primary.pt-2.pb-2
   {:style style}
   [:h5.my-2.text-secondary title]
   [:h6.text-light description]])

