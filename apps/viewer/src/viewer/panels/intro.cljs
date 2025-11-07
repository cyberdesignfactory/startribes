(ns viewer.panels.intro
  (:require
   [re-frame.core :as re-frame]
   [breaking-point.core :as bp]
   [viewer.events :as events]
   [viewer.subs :as subs]
   [components.ui.header :as header]))

(defn touch-intro []
  [:div.card.bg-dark.mb-3
   [:div.card-header.bg-dark
    [:h4.text-light.pt-2 "Controls"]]
   [:div.card-body.bg-dark
    [:p.text-light "Tilt device to control thrust / rudder / strafe."]
    [:p.text-light "Press right half of screen to fire primary weapon."]
    [:p.text-light "Press left half of screen to fire secondary weapon."]]])

(defn keyboard-intro []
  (defn left-control-row [key action]
    [:div.row.justify-content-end
     [:div.col.text-end key]
     [:div.col.text-start action]])
  (defn right-control-row [key action]
    [:div.row.justify-content-start
     [:div.col-1.xtext-end key]
     [:div.col.xtext-start action]])
  [:div
   [:div.card.bg-dark.mb-3
    [:div.card-header.bg-dark
     [:h4.text-light.pt-2 "Controls"]]
    [:div.card-body.bg-dark
     [:div.row.bg-dark
      [:div.col.text-end
       [left-control-row [:h5.text-secondary "W"] [:p.text-light "Forwards"]]
       [left-control-row [:h5.text-secondary "S"] [:p.text-light "Backwards"]]
       [left-control-row [:h5.text-secondary "A"] [:p.text-light "Strafe Left"]]
       [left-control-row [:h5.text-secondary "D"] [:p.text-light "Strafe Right"]]
       ]
      [:div.col.text-start
       [right-control-row
        [:i.bi.bi-arrow-up-square.text-secondary]
        [:p.text-light "Fire Primary Weapon"]]
       [right-control-row
        [:i.bi.bi-arrow-down-square.text-secondary]
        [:p.text-light "Fire Secondary Weapon"]]
       [right-control-row
        [:i.bi.bi-arrow-left-square.text-secondary]
        [:p.text-light "Rotate Left"]]
       [right-control-row
        [:i.bi.bi-arrow-right-square.text-secondary]
        [:p.text-light "Rotate Right"]]]]]]])


(defn intro-panel []
  #_(let [!portrait? (re-frame/subscribe [::bp/portrait?])])
  [:div
   [header/header]
   [:div.container.mt-4.text-center
    (if (.-matches (.matchMedia js/window "(any-pointer: coarse)"))
      [touch-intro]
      [keyboard-intro])
    [:button.btn.btn-secondary
     {:on-click #(re-frame/dispatch [::events/begin-campaign-clicked])}
     "Begin Campaign"]]])

