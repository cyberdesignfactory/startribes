(ns game.features.auto-targetting
  (:require [clojure.pprint :as pp]
            [game.features.shared :as shared]))

(defn nearest-id [pos candidates]
  ;; candidates is assumed to be a map where each value has a :pos
  (let [!nearest-id (atom nil)
        !shortest-distance (atom 1000000)]
    (doseq [[id value] candidates]
      (let [distance (shared/distance-from-to pos (:pos value))]
        (when (< distance @!shortest-distance)
          (reset! !shortest-distance distance)
          (reset! !nearest-id id))))
    @!nearest-id))

(defn auto-targetting [world t]
  (let [!world (atom world)]
    (doseq [[tribe-id tribe] (:tribes world)
            [ship-id ship] (:ships tribe)]
      (when (fn? (:action-fn ship))
        (let [action ((:action-fn ship) world tribe-id ship-id)

              target
              (case (:type action)

                :collect-resource
                {:type :resource
                 :grade-id (:grade-id action)
                 :id
                 (nearest-id
                  (:pos ship)
                  (get-in world
                          [:grades (:grade-id action) :resources]))}

                :visit-base
                {:type :base
                 :tribe-id (:tribe-id action)
                 :id
                 (nearest-id
                  (:pos ship)
                  (into {}
                        (filter #(= (:role action)
                                    (:role (second %)))
                                (get-in world
                                        [:tribes (:tribe-id action) :bases]))))}

                :attack-ship
                {:type :ship
                 :tribe-id (:tribe-id action)
                 :id (if (:id action)
                       (:id action)
                       (nearest-id
                        (:pos ship)
                        (get-in world
                                [:tribes (:tribe-id action) :ships])))
                 }

                :attack-base
                {:type :base
                 :tribe-id (:tribe-id action)
                 :id (:id action)})]

          (swap! !world
                 assoc-in
                 [:tribes tribe-id :ships ship-id :target]
                 target)

          (swap! !world
                 assoc-in
                 [:tribes tribe-id :ships ship-id :action-title]
                 (:title action))
          (swap! !world
                 assoc-in
                 [:tribes tribe-id :ships ship-id :action-description]
                 (:description action)))))
    @!world))

