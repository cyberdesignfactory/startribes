(ns admin.views
  (:require
   [re-frame.core :as re-frame]
   [admin.events :as events]
   [admin.subs :as subs]))

(defn header []
  [:div.text-center.bg-primary.pt-4.pb-1
   {:style
    {:position :absolute
     :top 0
     :opacity 0.8
     :width "100%"}}
   [:div
    [:h2.mb-0.text-secondary "Star Tribes"]
    [:h6.mt-2.mb-3.text-light "Admin Console"]]])

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        !world (re-frame/subscribe [::subs/world])]
    [:div
     [header]
     [:div.container.text-center {:style {:margin-top 120}}
      [:h4.text-light.pt-2 "Energy"]
      [:hr.text-light]
      [:div
       ;; assume all ships are in yellow tribe for now
       (for [[ship-id ship] (get-in @!world [:tribes :y :ships])]
         [:div.row.my-2
          {:key ship-id}
          [:div.col.text-center
           [:p.text-light (str ship-id)]]
          [:div.col.text-center
           [:p.text-light (:energy ship)]]
          [:div.col.text-center
           [:button.btn.btn-primary
            {:on-click #(re-frame/dispatch [::events/damage-clicked ship-id])}
            "Damage"]]])]
      [:hr.text-light]
      [:button.btn.btn-primary
       {:on-click #(re-frame/dispatch [::events/create-ship-clicked])}
       "Create Ship"]]]))

