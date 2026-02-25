(ns viewer.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [breaking-point.core :as bp]
   [viewer.events :as events]
   [viewer.views :as views]
   [viewer.config :as config]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

;; key codes: https://github.com/google/closure-library/blob/master/closure/goog/events/keycodes.js#L27

(defn dispatch-keyboard-rules []
  (let [w {:keyCode 87}
        s {:keyCode 83}
        left {:keyCode 37}
        right {:keyCode 39}
        up {:keyCode 38}
        down {:keyCode 40}
        a {:keyCode 65}
        d {:keyCode 68}
        esc {:keyCode 27}
        ]
    (re-frame/dispatch-sync
     [::rp/set-keydown-rules
      {:event-keys [
                    [[::events/set-ship-thrust 1.0] [ w ]]
                    [[::events/set-ship-thrust -1.0] [ s ]]
                    [[::events/set-ship-rudder -1.0] [ left ]]
                    [[::events/set-ship-rudder 1.0] [ right ]]

                    [[::events/update-fbs :primary true] [ up ]]
                    [[::events/update-fbs :secondary true] [ down ]]

                    [[::events/set-ship-strafe -1.0] [ a ]]
                    [[::events/set-ship-strafe 1.0] [ d ]]
                    ]
       ;; :clear-keys [[esc]]
       }])
    (re-frame/dispatch-sync
     [::rp/set-keyup-rules
      {:event-keys [
                    [[::events/set-ship-thrust 0.0] [ w ]]
                    [[::events/set-ship-thrust 0.0] [ s ]]
                    [[::events/set-ship-rudder 0.0] [ left ]]
                    [[::events/set-ship-rudder 0.0] [ right ]]

                    [[::events/update-fbs :primary false] [ up ]]
                    [[::events/update-fbs :secondary false] [ down ]]

                    [[::events/set-ship-strafe 0.0] [ a ]]
                    [[::events/set-ship-strafe 0.0] [ d ]]
                    ]
       ;; :clear-keys [[esc]]
       }])))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch-sync [::events/initialize-touch-listener])
  (re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keydown"])
  (re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keyup"])
  (re-frame/dispatch-sync [::bp/set-breakpoints
                           {:breakpoints [:mobile
                                          768
                                          :tablet
                                          992
                                          :small-monitor
                                          1200
                                          :large-monitor]
                            :debounce-ms 166}])
  (dispatch-keyboard-rules)
  (dev-setup)
  (mount-root)

  (js/setInterval
   (fn []
     (let [t (.getTime (js/Date.))]
       (re-frame/dispatch [::events/game-cycle t])))
   10))

