(ns game.cycle
  (:require
   [projection.circle :as circle]
   [projection.box :as box]
   [game.features.ship-flight :as ship-flight]
   [game.features.resource-creation :as resource-creation]
   [game.features.resource-collection :as resource-collection]
   [game.features.auto-targetting :as auto-targetting]
   [game.features.auto-navigation :as auto-navigation]
   [game.features.auto-trading :as auto-trading]
   [game.features.bullet-creation :as bullet-creation]
   [game.features.bullet-damage :as bullet-damage]
   [game.features.bullet-expiration :as bullet-expiration]
   [game.features.energy-recharge :as energy-recharge]
   [game.features.destruction :as destruction]
   ))


(defn terrain-cycle [terrain t]
  (let [delta (mod (* t 0.001) 1.0)
        d-radius (* 240.0 (* (abs (- delta 0.5)) 0.4))]
    (-> terrain
        (update :circles
                (fn [circles]
                  (into {}
                        (for [[id circle] circles]
                          [id (-> circle
                                  (assoc :radius (+ 80.0 d-radius)))])))))))


(defn game-cycle [world t ship-thrust ship-rudder ship-strafe]
  (let [thrust (get-in world [:tribes :y :ships :y-1 :thrust])
        rudder (get-in world [:tribes :y :ships :y-1 :rudder])
        ;; delta (mod (* t 0.001) 1.0)
        ;; terrain (:terrain world)
        ]
    (-> world
        (assoc-in [:tribes :y :ships :y-1 :thrust] ship-thrust)
        (assoc-in [:tribes :y :ships :y-1 :rudder] ship-rudder)
        (assoc-in [:tribes :y :ships :y-1 :strafe] ship-strafe)

        ;; perhaps this is basically a 'feature'?
        ;; (update-in [:terrain] #(terrain-cycle % t))

        ;; (update-in [:tribes :y :ships :y-1] #(update-ship % terrain 10))
        (ship-flight/ship-flight-feature t)
        ;; do we need bullet-flight-feature ??
        (bullet-creation/bullet-creation t)
        (bullet-damage/bullet-damage t)
        (bullet-expiration/bullet-expiration t)
        (energy-recharge/energy-recharge t)
        (destruction/destruction t)
        #_(auto-targetting/auto-targetting t)
        ;; need to make an exception for player ship
        ;; before we put this back in...
        (auto-navigation/auto-navigation t)
        (auto-trading/auto-trading t)
        (resource-creation/resource-creation t)
        (resource-collection/resource-collection t)

        (assoc :t t)

        )))

