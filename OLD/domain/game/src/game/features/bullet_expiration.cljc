(ns game.features.bullet-expiration)

(defn bullet-expiration [world t]
  (let [!world (atom world)
        bullet-duration 2000]
    (doseq [[tribe-id tribe] (:tribes world)
            [bullet-id bullet] (:bullets tribe)]

      (when (>= t (+ (get-in bullet [ :t]) bullet-duration))
        (swap! !world update-in [:tribes tribe-id :bullets] dissoc bullet-id))

      )
    @!world))
