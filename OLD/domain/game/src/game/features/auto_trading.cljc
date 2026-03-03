(ns game.features.auto-trading
  (:require [game.features.shared :as shared]))

(defn ship-base-collisions [universe]
  (let [!collisions (atom [])]
    (doseq [[ship-tribe-id ship-tribe] (:tribes universe)
            [ship-id ship] (:ships ship-tribe)
            [base-tribe-id base-tribe] (:tribes universe)
            [base-id base] (:bases base-tribe)]
      (let [ship-pos (get-in ship [:pos])
            base-pos (:pos base)]
        (when (< (shared/distance-from-to ship-pos base-pos) 42.0)  ;; was 28.0
          (swap! !collisions conj
                 [ship-tribe-id ship-id
                  base-tribe-id base-id]))))
    @!collisions))

(defn ship-base-trades [collisions universe]
  (let [!trades (atom [])]
    (doseq [[ship-tribe-id ship-id
             base-tribe-id base-id] collisions]
      (let [ship-tribe (get-in universe [:tribes ship-tribe-id])
            base-tribe (get-in universe [:tribes base-tribe-id])
            ship (get-in universe [:tribes ship-tribe-id :ships ship-id])
            base (get-in universe [:tribes base-tribe-id :bases base-id])
            same-tribe? (= ship-tribe-id base-tribe-id)  ;; needed?
            grades-ship-wants [(get-in ship-tribe [:production :primary])
                               (get-in ship-tribe [:production :secondary-1])
                               (get-in ship-tribe [:production :secondary-2])]
            grades-base-wants [(get-in base-tribe
                                       [:production (:role base)])]
            grades-base-offers
            (case (:role base)
              :primary []
              :secondary-1 [(get-in base-tribe [:production :primary])
                            (get-in base-tribe [:production :surplus-1])]
              :secondary-2 [(get-in base-tribe [:production :primary])
                            (get-in base-tribe [:production :surplus-2])])]
        (doseq [grade-id grades-base-wants]
          (let [amount (get-in ship [:inv grade-id] 0)]
            (when (> amount 0)  ;; needed?
              (swap! !trades conj
                     [ship-tribe-id ship-id
                      base-tribe-id base-id
                      grade-id amount]))))
        (doseq [grade-id grades-base-offers]
          (let [amount (min 16 ;; <- ship capacity
                            (get-in base [:inv grade-id] 0))]
            (when (and (> amount 0)
                       (some #{grade-id} grades-ship-wants))
              (swap! !trades conj
                     [ship-tribe-id ship-id
                      base-tribe-id base-id
                      grade-id (- amount)]))))))
    @!trades))

(defn auto-trading [universe t]
  (let [!universe (atom universe)
        collisions (ship-base-collisions universe)
        trades (ship-base-trades collisions universe)]
    (doseq [[ship-tribe-id ship-id
             base-tribe-id base-id
             grade-id amount] trades]
      (swap! !universe
             update-in [:tribes ship-tribe-id :ships ship-id :inv grade-id]
             #(- (or % 0) amount))
      (swap! !universe
             update-in [:tribes base-tribe-id :bases base-id :inv grade-id]
             #(+ (or % 0) amount)))
    @!universe))
