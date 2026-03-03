(ns components.pixi.geometry)

(defn intersect-segment-box
  "Finds the intersection of a segment (p1 to p2) and a 2D box.
   p1, p2: [x y]
   box-min, box-max: [x y]"
  [[x1 y1 :as p1] [x2 y2 :as p2] [xmin ymin] [xmax ymax]]
  (let [dx (- x2 x1)
        dy (- y2 y1)
        ;; p and q are vectors for the four boundary conditions
        ps [(- dx) dx (- dy) dy]
        qs [(- x1 xmin) (- xmax x1) (- y1 ymin) (- ymax y1)]]
    (loop [u1 0.0
           u2 1.0
           i 0]
      (if (< i 4)
        (let [p (nth ps i)
              q (nth qs i)]
          (cond
            ;; Line is parallel to boundary
            (zero? p) (if (neg? q)
                        nil ;; Parallel and outside
                        (recur u1 u2 (inc i)))

            ;; Calculate parameter r for this boundary
            :else (let [r (/ q p)]
                    (if (neg? p)
                      ;; Potentially updates entry point (u1)
                      (let [next-u1 (max u1 r)]
                        (if (> next-u1 u2) nil (recur next-u1 u2 (inc i))))
                      ;; Potentially updates exit point (u2)
                      (let [next-u2 (min u2 r)]
                        (if (< next-u2 u1) nil (recur u1 next-u2 (inc i))))))))

        ;; If we finish loop, calculate actual intersection points
        (let [get-pt (fn [u] [(+ x1 (* u dx)) (+ y1 (* u dy))])]
          [(get-pt u1) (get-pt u2)])))))

(defn clip-pos-to-box [pos box]
  (let [box-min [(- (get-in box [:pos :x])
                    (/ (get-in box [:size :w]) 2))
                 (- (get-in box [:pos :y])
                    (/ (get-in box [:size :h]) 2))]
        box-max [(+ (get-in box [:pos :x])
                    (/ (get-in box [:size :w]) 2))
                 (+ (get-in box [:pos :y])
                    (/ (get-in box [:size :h]) 2))]
        clipped-pos-vector
        (second
         (intersect-segment-box
          [(get-in box [:pos :x]) (get-in box [:pos :y])]
          [(:x pos) (:y pos)]
          box-min
          box-max))]
    {:x (first clipped-pos-vector)
     :y (second clipped-pos-vector)}))

(defn rotate-pos-around-pos [pos pivot-pos rot]
  (let [angle (* rot 2 Math/PI)
        c (Math/cos angle)
        s (Math/sin angle)
        dx (- (:x pos) (:x pivot-pos))
        dy (- (:y pos) (:y pivot-pos))
        nx (+ (:x pivot-pos) (- (* dx c) (* dy s)))
        ny (+ (:y pivot-pos) (+ (* dx s) (* dy c)))]
    {:x nx :y ny}))
