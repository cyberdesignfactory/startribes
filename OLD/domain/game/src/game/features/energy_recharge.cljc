(ns game.features.energy-recharge)

;; this charges at a rate based on game cycle interval, not a constant rate
;; (which would require something like :energy-last-charged-at
;; or getting a dt (t - (:t world)) and multiplying by a constant)

(defn energy-recharge [universe t]
  (let [!universe (atom universe)]
    (doseq [[tribe-id tribe] (:tribes universe)]
      (doseq [[base-id base] (:bases tribe)]
        (when (< (get base :energy 1.0) 1.0)
          (swap! !universe
                 update-in [:tribes tribe-id :bases base-id :energy]
                 ;; #(+ % 0.02)
                 #(+ % 0.002)
                 )))
      (doseq [[ship-id ship] (:ships tribe)]
        (when (< (get ship :energy 1.0) 1.0)
          (swap! !universe
                 update-in [:tribes tribe-id :ships ship-id :energy]
                 ;; #(+ % 0.02)
                 #(+ % 0.002)
                 ))))
      @!universe))
