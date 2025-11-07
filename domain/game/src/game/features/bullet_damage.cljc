(ns game.features.bullet-damage)

(defn bullet-damage [world t]
  (let [!world (atom world)]
    (doseq [[bullet-tribe-id bullet-tribe] (:tribes world)
            [bullet-id bullet] (:bullets bullet-tribe)]

      (doseq [[base-tribe-id base-tribe] (:tribes world)
              [base-id base] (:bases base-tribe)]
        (when (not= base-tribe-id bullet-tribe-id)
          (let [dx (abs (- (get-in bullet [:pos :x])
                           (get-in base [:pos :x])))
                dy (abs (- (get-in bullet [:pos :y])
                           (get-in base [:pos :y])))]
            (when (and (< dx 60) (< dy 60)) ;; was 36
              (swap! !world
                     update-in [:tribes base-tribe-id :bases base-id :energy]
                     #(- (or % 1.0) 0.1))
              (swap! !world
                     update-in [:tribes bullet-tribe-id :bullets]
                     dissoc bullet-id)

              ))))

      ;; This is here ready for ship to ship combat...
      (doseq [[ship-tribe-id ship-tribe] (:tribes world)
              [ship-id ship] (:ships ship-tribe)]
        (when (not= ship-tribe-id bullet-tribe-id)
          (let [dx (abs (- (get-in bullet [:pos :x])
                           (get-in ship [:pos :x])))
                dy (abs (- (get-in bullet [:pos :y])
                           (get-in ship [:pos :y])))]
            (when (and (< dx 60) (< dy 60)) ;; was 36
              (swap! !world
                     update-in [:tribes ship-tribe-id :ships ship-id :energy]
                     #(- (or % 1.0) 0.2))
              (swap! !world
                     update-in [:tribes bullet-tribe-id :bullets]
                     dissoc bullet-id)

              ;; ** PREVIOUS VERSION HAS ADJUST STANDINGS HERE **

                ;; adjust standings? for both attacker and victim (if necessary)
                (case (get-in world [:tribes ship-tribe-id :strategy])
                  :retaliatory
                  (swap! !world
                         update-in
                         [:tribes ship-tribe-id :standings bullet-tribe-id]
                         #(- (or % 0.0) 0.04))

                  :bully
                  (swap! !world
                         update-in
                         [:tribes ship-tribe-id :standings bullet-tribe-id]
                         #(- (or % 0.0) 0.04))

                  nil
                  )

                (case (get-in world [:tribes bullet-tribe-id :strategy])
                  :retaliatory
                  (swap! !world
                         update-in
                         [:tribes bullet-tribe-id :standings ship-tribe-id]
                         #(+ (or % 0.0) 0.02))


                  :bully
                  (swap! !world
                         update-in
                         [:tribes bullet-tribe-id :standings ship-tribe-id]
                         #(+ (or % 0.0) 0.02))

                  nil)

              )))))

    @!world))

