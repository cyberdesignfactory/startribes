(ns projection.box)

(defn distance-from-to [pos-1 pos-2]
  (let [dx (- (:x pos-2) (:x pos-1))
        dy (- (:y pos-2) (:y pos-1))]
    (Math/sqrt (+ (* dx dx) (* dy dy)))))

(defn angle-to-pos [pos-1 pos-2]
  (let [dx (- (:x pos-2) (:x pos-1))
        dy (- (:y pos-2) (:y pos-1))]
    (Math/atan2 dy dx)))

(defn pos-after-box [pos box]
  ;; (println pos circle)
  ;; from centre of circle...
  (let [
        x (:x pos)
        y (:y pos)
        dx (- x (get-in box [:pos :x]))
        dy (- y (get-in box [:pos :y]))
        box-half-w (/ (get-in box [:size :w]) 2)
        box-half-h (/ (get-in box [:size :h]) 2)
        ]

    (cond

      ;; i think it is sometimes going into these when
      ;; ship is on a *vertical* wall...

      ;; left quadrant
      (and (< (- box-half-w) dx 0)
           ;; (< (abs dy) box-half-h)
           (< (abs dy) (abs dx))

           )
      (-> pos
          (update :x - (+ box-half-w dx)))

      ;; right quadrant
      (and (< 0 dx box-half-w)
           ;; (< (abs dy) box-half-h)
           (< (abs dy) (abs dx))

           )
      (-> pos
          (update :x + (- box-half-w dx)))

      ;; up quadrant
      (and (< (- box-half-h) dy 0)
           ;; (< (abs dx) box-half-w)
           (< (abs dx) (abs dy))

           )
      (-> pos
          (update :y - (+ box-half-h dy)))

      ;; down quadrant
      (and (< 0 dy box-half-h)
           ;; (< (abs dx) box-half-w)
           (< (abs dx) (abs dy))

           )
      (-> pos
          (update :y + (- box-half-h dy)))

      :else
      pos

      )

    ))

