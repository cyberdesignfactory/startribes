(ns game.features.bullet-creation)

(defn bullet-creation [world t]
  (let [!world (atom world)
        bullet-interval 400]
    (doseq [[tribe-id tribe] (:tribes world)
            [ship-id ship] (:ships tribe)
            [weapon-id weapon] (:weapons ship)]
      (when (get-in ship [:fbs weapon-id])
        (let [time-last-bullet-fired
              (last
               (sort
                (map #(get-in % [:t])
                     (filter #(= (:weapon-id %) weapon-id)
                             (vals (:bullets tribe))))))

              first-bullet-t
              (if (nil? time-last-bullet-fired)
                t
                (+ time-last-bullet-fired bullet-interval))

              !t-fire (atom first-bullet-t)]

          #_(println time-last-bullet-fired first-bullet-t)

          (while (<= @!t-fire t)
            (let [bullet-id (keyword (str "bullet-" (rand-int 1000000)))
                  vel (if (= :primary weapon-id) 360.0 0.0)
                  bullet  {:t @!t-fire
                           :pos (:pos ship)
                           :rot (:rot ship)
                           :vel vel
                           :ship-id ship-id
                           :weapon-id weapon-id}]
              (swap! !world assoc-in [:tribes tribe-id :bullets bullet-id] bullet))
            (swap! !t-fire #(+ % bullet-interval))))))
    @!world))

