(ns viewer.panels.mission
  (:require
   [re-frame.core :as re-frame]
   [components.pixi.universe :refer [pixi-universe]]
   [viewer.events :as events]
   [viewer.subs :as subs]
   ;; [viewer.panels.shared :as panels.shared]
   [components.ui.header :as header]
   [components.ui.footer :as footer]
   ))

(defn mission-panel []
  (let [
        ;; world-id (re-frame/subscribe [::subs/world-id])
        world @(re-frame/subscribe [::subs/world])
        time-left @(re-frame/subscribe [::subs/time-left])
        ship-thrust @(re-frame/subscribe [::subs/ship-thrust])
        ship-rudder @(re-frame/subscribe [::subs/ship-rudder])
        ship-strafe @(re-frame/subscribe [::subs/ship-strafe])
        ship-fbs @(re-frame/subscribe [::subs/ship-fbs])
        player-action-title @(re-frame/subscribe [::subs/player-action-title])
        player-action-description @(re-frame/subscribe [::subs/player-action-description])
        width (- (.-innerWidth js/window) 0)  ;; was 16
        height (- (.-innerHeight js/window) 8)  ;; was 16 or 24
        scale 0.36]
    [:div
     [header/header {:style
                     {:position :absolute
                      :top 0
                      :opacity 0.7
                      :width "100%"}
                     :time-left time-left}]

     [footer/footer {:style
                     {:position :absolute
                      :bottom 0
                      :opacity 0.7
                      :width "100%"}
                     :title player-action-title
                     :description player-action-description}]

     [pixi-universe
      {:universe world
       :player-thrust ship-thrust
       :player-rudder ship-rudder
       :player-strafe ship-strafe
       ;; :width 600
       ;; :height 600
       :width width
       :height height
       :header-margin 140
       ;; :camera {:pos {:x 0 :y 0}
       ;;          :scale 0.36}
       :camera {:pos {
                      :x (get-in world [:tribes :y :ships :y-1 :pos :x] 0.0)
                      :y (get-in world [:tribes :y :ships :y-1 :pos :y] 0.0)
                      }
                :rot (get-in world [:tribes :y :ships :y-1 :rot] 0.0)
                :scale scale}

       }]
     ;; [:div
     ;;  [:h3 (str "screen-width: " @(re-frame/subscribe [::bp/screen-width]))]
     ;;  [:h3 (str "screen: " @(re-frame/subscribe [::bp/screen]))]]
     ]))
