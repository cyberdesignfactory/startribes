(ns game.features.auto-targetting
  (:require [clojure.pprint :as pp]
            [game.features.shared :as shared]))

(defn score-resource [grade-id resource tribe]
  (cond
    (= grade-id (get-in tribe [:production :primary]))     (* (:amount resource) 3.0)
    (= grade-id (get-in tribe [:production :secondary-1])) (* (:amount resource) 1.0)
    (= grade-id (get-in tribe [:production :secondary-2])) (* (:amount resource) 1.0)
    :else 0.0))

(defn score-ship [other-tribe-id other-ship {:keys [production strategy standings]}]
  (case strategy
    :aggressive
    (+ (* (get-in other-ship [:inv (:primary production)] 0) 3.0)
       (* (get-in other-ship [:inv (:secondary-1 production)] 0) 1.0)
       (* (get-in other-ship [:inv (:secondary-2 production)] 0) 1.0))

    :retaliatory
    (let [standing (get-in standings [other-tribe-id] 0.0)]
      (* -20.0 standing))

    :passive
    0.0

    :bully
    (let [standing (get-in standings [other-tribe-id] 0.0)]
      (* 20.0 (- standing 0.2)))

    0.0))

(defn ship-full-of? [ship rt]
  (>= (get-in ship [:inv rt] 0) 16))

(defn nearest-non-primary-base-full-of-grade [world grade-id pos]
  (let [!bases-with-grade (atom [])]
    (doseq [[tribe-id tribe] (:tribes world)
            [base-id base] (:bases tribe)]
      (when (and (not= :primary (:role base))
                 (>= (get-in base [:inv grade-id] 0) 16))
        (let [distance (shared/distance-from-to pos (:pos base))]
          (swap! !bases-with-grade conj [tribe-id base-id distance]))))
    (first
     (sort-by (fn [[_ _ distance]] distance)
              @!bases-with-grade))))

(defn nearest-base-of-role [world tribe-id role pos]
  (let [!bases-of-role (atom [])
        tribe (get-in world [:tribes tribe-id])]
    (doseq [[base-id base] (:bases tribe)]
      (when (= role (:role base))
        (let [distance (shared/distance-from-to pos (:pos base))]
          (swap! !bases-of-role conj [tribe-id base-id distance]))))
    (first
     (sort-by (fn [[_ _ distance]] distance)
              @!bases-of-role))))

(defn auto-targetting [world t]
  (let [!world (atom world)]

    (doseq [[tribe-id tribe] (:tribes world)
            [ship-id ship] (:ships tribe)]

      (when (:auto-targetting? ship)

        (let [
              ship-pos (get-in ship [:fs :pos])

              primary-rt (get-in tribe [:production :primary])
              secondary-1-rt (get-in tribe [:production :secondary-1])
              secondary-2-rt (get-in tribe [:production :secondary-2])

              nearest-base-id-with-primary-resource
              (nearest-non-primary-base-full-of-grade world primary-rt ship-pos)

              nearest-base-of-role-primary
              (nearest-base-of-role world tribe-id :primary ship-pos)

              nearest-base-of-role-secondary-1
              (nearest-base-of-role world tribe-id :secondary-1 ship-pos)

              nearest-base-of-role-secondary-2
              (nearest-base-of-role world tribe-id :secondary-2 ship-pos)

              target
              (cond
                ;; if ship full of primary resource, deliver to primary base
                (ship-full-of? ship primary-rt)
                {:type :base
                 :tribe-id (first nearest-base-of-role-primary)
                 :id (second nearest-base-of-role-primary)}

                ;; if a base of any tribe is full of primary resource, target to collect
                nearest-base-id-with-primary-resource
                {:type :base
                 :tribe-id (first nearest-base-id-with-primary-resource)
                 :id (second nearest-base-id-with-primary-resource)}

                ;; if ship full of secondary-1 resource, deliver to secondary base
                (ship-full-of? ship secondary-1-rt)
                {:type :base
                 :tribe-id (first nearest-base-of-role-secondary-1)
                 :id (second nearest-base-of-role-secondary-1)}

                ;; if ship full of secondary-2 resource, deliver to secondary base
                (ship-full-of? ship secondary-2-rt)
                {:type :base
                 :tribe-id (first nearest-base-of-role-secondary-2)
                 :id (second nearest-base-of-role-secondary-2)}

                :else
                (let [resources-with-grades
                      (for [[grade-id grade] (:grades world)
                            [res-id res] (:resources grade)]
                        [res-id res grade-id])
                      ;; _ (println resources-with-grades)

                      resource-scores-and-distances
                      (map
                       (fn [[res-id res grade-id]]
                         (let [score (score-resource grade-id res tribe)
                               ;; distance (distance-to-resource ship res)
                               distance (max 10.0
                                             (shared/distance-from-to (get-in ship [:fs :pos])
                                                                      (:pos res)))]
                           [res-id grade-id score distance]))
                       resources-with-grades)
                      ;; _ (println resource-scores-and-distances)

                      [res-id-to-target grade-id-to-target res-score res-distance]
                      (first
                       (reverse
                        (sort-by
                         (fn [[_ _ score distance]]
                           (/ score distance))
                         resource-scores-and-distances)))

                      ship-scores-and-distances
                      (for [[other-tribe-id other-tribe] (filter
                                                          (fn [[tr-id tr]]
                                                            (not= tribe-id tr-id))
                                                          (:tribes world))
                            [other-ship-id other-ship] (:ships other-tribe)]
                        (let [score (score-ship other-tribe-id other-ship tribe)
                              ;; distance (distance-to-ship ship other-ship)
                              distance (max 10.0
                                            (shared/distance-from-to (get-in ship [:fs :pos])
                                                                     (get-in other-ship [:fs :pos])))]
                          [other-ship-id other-tribe-id score distance]))
                      ;; _ (println ship-scores-and-distances)

                      [ship-id-to-target tribe-id-to-target ship-score ship-distance]
                      (first
                       (reverse
                        (sort-by
                         (fn [[_ _ score distance]]
                           ;; (/ score (or distance 10.0))
                           (/ score (if (zero? distance)
                                      10.0
                                      distance)))  ;; <---- TEMPORARY HACK
                         ship-scores-and-distances)))

                      ]

                  ;; (println ship-score ship-distance)

                  (if (and (> (count ship-scores-and-distances) 0)
                           (> (/ ship-score ship-distance)
                              (/ res-score res-distance)))
                    {:type :ship
                     :tribe-id tribe-id-to-target
                     :id ship-id-to-target}
                    {:type :resource
                     :grade-id grade-id-to-target
                     :id res-id-to-target})
                  ))]

          (swap! !world assoc-in [:tribes tribe-id :ships ship-id :target] target))))

    @!world))
