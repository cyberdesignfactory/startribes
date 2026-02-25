(ns game.features.shared)

(defn distance-from-to [pos-1 pos-2]
  (let [dx (- (:x pos-2) (:x pos-1))
        dy (- (:y pos-2) (:y pos-1))]
    (Math/sqrt (+ (* dx dx) (* dy dy)))))

;; Ensure rotation is between -0.5 and 0.5
(defn normalise-rotation [rot]
  (- (mod (+ rot 1000.5) 1.0) 0.5))

(defn rotation-delta-from-to [pos rot target-pos]
  ;; 0.0
  (let [dx (- (:x target-pos) (:x pos))
        dy (- (:y target-pos) (:y pos))
        ;; angle (Math/atan2 dy dx)
        angle (Math/atan2 dx (- dy))
        rotation-to-position (/ angle (* 2 Math/PI))
        rotation-delta (- rotation-to-position rot)]
    (normalise-rotation rotation-delta)))

