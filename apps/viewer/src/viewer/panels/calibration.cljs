(ns viewer.panels.calibration
  (:require
   [re-frame.core :as re-frame]
   [breaking-point.core :as bp]
   [viewer.events :as events]
   [viewer.subs :as subs]
   [components.pixi.universe :refer [pixi-universe]]
   [components.ui.header :as header]))

(defn calibration-intro []
  (let [
        world @(re-frame/subscribe [::subs/world])
        ship-thrust @(re-frame/subscribe [::subs/ship-thrust])
        ship-rudder @(re-frame/subscribe [::subs/ship-rudder])
        ship-strafe @(re-frame/subscribe [::subs/ship-strafe])
        ship-fbs @(re-frame/subscribe [::subs/ship-fbs])
        calibration @(re-frame/subscribe [::subs/calibration])
        orientation @(re-frame/subscribe [::subs/orientation])
        scale 0.36
        ]
    [:div.card.bg-dark.mb-3
     [:div.card-header.bg-dark
      [:h4.text-light.pt-2 "Calibrate Controls"]]
     [:div.card-body.bg-dark
      [:p.text-light "Tilt device:"]
      [:p.text-light "Forwards and backwards for max / min thrust."]
      [:p.text-light "Left and right for max / min rudder."]

      [pixi-universe
       {:width 360
        :height 360

        :player-thrust ship-thrust
        :player-rudder ship-rudder
        :player-strafe ship-strafe

        ;; :camera {:pos {:x 0 :y 0}
        ;;          :scale 0.36
        ;;          :rot 0.0}
        :camera
        {:pos
         {
          :x (get-in world [:tribes :y :ships :y-1 :pos :x] 0.0)
          :y (get-in world [:tribes :y :ships :y-1 :pos :y] 0.0)
          }
         :rot (get-in world [:tribes :y :ships :y-1 :rot] 0.0)
         :scale scale}

        :universe world

        }]

      #_[:div
       [:pre.text-light (str (Math/round (:min-alpha calibration))
                             " "
                             (Math/round (:max-alpha calibration)))]]
      #_[:div
       [:pre.text-light (str (Math/round (:min-beta calibration))
                             " "
                             (Math/round (:max-beta calibration)))]]
      #_[:div
       [:pre.text-light (str (Math/round (:min-gamma calibration))
                             " "
                             (Math/round (:max-gamma calibration)))]]
      #_[:div
       [:pre.text-light (str (Math/round (:alpha orientation)))]]
      #_[:div
       [:pre.text-light (str (Math/round (:beta orientation)))]]
      #_[:div
       [:pre.text-light (str (Math/round (:gamma orientation)))]]


      [:div.mt-2
       [:button.btn.btn-link.text-light
        {:on-click #(re-frame/dispatch [::events/calibrate-clicked])}
        "Restart Calibration"]]
      ]]))



(defn calibration-panel []
  #_(let [!portrait? (re-frame/subscribe [::bp/portrait?])])
  [:div
   [header/header]
   [:div.container.mt-4.text-center
    [calibration-intro]


    
    [:div
     [:button.btn.btn-secondary
      {:on-click #(re-frame/dispatch [::events/begin-campaign-clicked])}
      #_"Begin Campaign"
      "Done"
      ]]]])
