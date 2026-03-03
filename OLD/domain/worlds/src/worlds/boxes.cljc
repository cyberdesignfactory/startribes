(ns worlds.boxes
  (:require [worlds.shared :as shared]))

(defn boxes-terrain [{:keys [t]}]
  (into [:group {:pos {:x (* -3 240)
                       :y (* -3 240)}}]
        (for [ix (range 8)
              iy (range 8)]
          [:box {:pos {:x (* 240 ix)
                       :y (* 240 iy)}
                 :size {:w 120 :h 120}}])))

(defn simple-ship []
  {:pos {:x 600 :y -900}
   :rot 0.25
   :controls {:thrust 0.0
              :rudder 0.0}
   ;; ship type?
   :type :destroyer
   :class :heavy
   :weapons {:neutron-cannon {:megatons 16}}})

(defn simple-boxes-world [{:keys [t]}]
  {
   :t t
   :grades (shared/simple-grades)
   :tribes (-> (shared/simple-tribes)
               #_(assoc-in [:y :bases :y-1 :energy] 0.7)
               #_(assoc-in [:y :ships :y-1] (simple-ship)))
   :terrain (boxes-terrain {:t t})
   }
  )

