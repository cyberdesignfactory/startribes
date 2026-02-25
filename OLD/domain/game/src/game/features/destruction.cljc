(ns game.features.destruction)

(defn destruction [universe t]
  (let [!universe (atom universe)]

    (doseq [[tribe-id tribe] (:tribes universe)]
      (doseq [[base-id base] (:bases tribe)]
        (when (<= (get base :energy 1.0) 0.0)
          (doseq [grade-id (keys (:inv base))]
            (let [
                  ;; resource-id (keyword (str grade-id "-" (rand-int 1000000)))
                  resource-id (str grade-id "-" (rand-int 1000000))
                  resource {:pos (:pos base)
                            :amount (get-in base [:inv grade-id])}]
              (swap! !universe assoc-in [:grades grade-id :resources resource-id] resource)

              ))
          (swap! !universe update-in [:tribes tribe-id :bases] dissoc base-id))

        ;; adjust standings?

        )
      (doseq [[ship-id ship] (:ships tribe)]
        (when (<= (get ship :energy 1.0) 0.0)
          (doseq [grade-id (keys (:inv ship))]
            (let [
                  ;; resource-id (keyword (str grade-id "-" (rand-int 1000000)))
                  resource-id (str grade-id "-" (rand-int 1000000))
                  resource {:pos (:pos ship)
                            :amount (get-in ship [:inv grade-id])}]
              (swap! !universe assoc-in [:grades grade-id :resources resource-id] resource)

              ))
          (swap! !universe update-in [:tribes tribe-id :ships] dissoc ship-id))

        ;; adjust standings?

        ))

    @!universe))
