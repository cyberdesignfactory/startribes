(ns game.features.resource-collection
  (:require [game.features.shared :as shared]))

(defn tribe-grades [tribe-id]
  (case tribe-id
    :r [:b :br :r :ry :y]
    :y [:r :ry :y :yg :g]
    :g [:y :yg :g :gb :b]
    :b [:g :gb :b :br :r]
    []))

(defn resource-ship-collisions [world]
  (filter
   #(< (nth % 4) 28.0)
   (for [[grade-id grade] (:grades world)
         [res-id res] (:resources grade)
         [tribe-id tribe] (:tribes world)
         [ship-id ship] (:ships tribe)]

     (let [res-pos (:pos res)
           ship-pos (get-in ship [:pos])]
       [grade-id res-id tribe-id ship-id (shared/distance-from-to res-pos ship-pos)])

     )))

(defn update-resource [[resource-id resource] tribe world collisions]
  [resource-id resource]
  )

(defn update-ship [[ship-id ship] tribe world collisions]
  [ship-id ship]
  )

(defn resource-collection [world t]
  (let [collisions (resource-ship-collisions world)
        !world (atom world)]
    (doseq [[grade-id res-id tribe-id ship-id] collisions]
      (let [amount (get-in world [:grades grade-id :resources res-id :amount])]
        (swap! !world
               update-in [:grades grade-id :resources] #(dissoc % res-id))
        (swap! !world
               update-in [:tribes tribe-id :ships ship-id :inv grade-id]
                         #(+ (if (nil? %) 0 %) amount))))
    @!world))

