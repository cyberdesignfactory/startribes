(ns viewer.panels.failed
  (:require
   [re-frame.core :as re-frame]
   [viewer.events :as events]
   [viewer.subs :as subs]
   ;; [viewer.panels.shared :as panels.shared]
   [components.ui.header :as header]
   ))

(defn failed-panel []
  (let [!mission (re-frame/subscribe [::subs/mission])]
    [:div
     [header/header]
     [:div.container
      [:div.card.bg-primary-subtle.p-2.m-3.opacity-75
       [:div.card-header.pb-1.text-center
        [:h4 (str (:title @!mission) " Failed.")]
        ]
       [:div.card-body.pb-1.text-center
        [:div.text-center.p-4
         [:h5.mb-4 "The mission has been failed."]
         [:button.btn.btn-secondary {:on-click #(re-frame/dispatch [::events/game-continued])} "Continue"]
         ]]]]]))


