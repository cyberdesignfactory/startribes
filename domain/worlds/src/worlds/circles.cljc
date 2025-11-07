(ns worlds.circles
  (:require [worlds.shared :as shared]))

(defn circles-terrain [{:keys [t]}]
  (into [:group {:pos {:x (* -3 240)
                       :y (* -3 240)}}]
        (for [ix (range 8)
              iy (range 8)]
          [:circle {:pos {:x (* 240 ix)
                          :y (* 240 iy)}
                    :radius 60}])))

#_(defn simple-ship []
  {:pos {:x 900 :y -900}
   :rot 0.25
   :controls {:thrust 0.0
              :rudder 0.0}
   ;; ship type?
   :type :destroyer
   :class :heavy
   :weapons {
             :neutron-cannon {:megatons 16}
             }
   })

(defn simple-circles-world [{:keys [t]}]
  {
   :t t
   :grades (shared/simple-grades)
   :tribes (-> (shared/simple-tribes)
               #_(assoc-in [:y :ships :y-1] (simple-ship))

               )
   :terrain (circles-terrain {:t t})
   }
  )


