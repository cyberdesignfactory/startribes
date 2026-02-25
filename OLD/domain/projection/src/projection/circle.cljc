(ns projection.circle)

(defn distance-from-to [pos-1 pos-2]
  (let [dx (- (:x pos-2) (:x pos-1))
        dy (- (:y pos-2) (:y pos-1))]
    (Math/sqrt (+ (* dx dx) (* dy dy)))))

(defn angle-to-pos [pos-1 pos-2]
  (let [dx (- (:x pos-2) (:x pos-1))
        dy (- (:y pos-2) (:y pos-1))]
    (Math/atan2 dy dx)))

(defn pos-after-circle [pos circle]
  ;; (println pos circle)
  ;; from centre of circle...
  (let [angle-to-ship (angle-to-pos (:pos circle) pos)
        distance-to-ship (distance-from-to (:pos circle) pos)
        radius (:radius circle)]
    (if (<= distance-to-ship radius)
      (let [dx (* (- radius distance-to-ship) (Math/cos angle-to-ship))
            dy (* (- radius distance-to-ship) (Math/sin angle-to-ship))]
        ;; Rounds to nearest whole number (mainly for testing purposes)

        ;; {:x (Math/rint (+ (:x pos) dx))
        ;;  :y (Math/rint (+ (:y pos) dy))}

        ;; {:x (+ (:x pos) dx)
        ;;  :y (+ (:y pos) dy)}

        {

         :x #?(:clj (Math/rint (+ (:x pos) dx))
               :cljs (+ (:x pos) dx))

         :y #?(:clj (Math/rint (+ (:y pos) dy))
               :cljs (+ (:y pos) dy))
         }

        )
      pos)))

