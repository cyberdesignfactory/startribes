(ns terrains.boxes)

(defn boxes-terrain [{:keys [t]}]
  (let [nx 7
        ny 7
        ;; d-radius (* 240.0 (* (abs (- delta 0.5)) 0.4))
        delta (mod (* t 0.001) 1.0)
        ]
    {:boxes

     (into {}
           (for [ix (range nx)
                 iy (range ny)]

             (let [

                   base-radius 160.0

                   dilation-period-ms 250


                   ;; d-radius (* 240.0 (* (abs (- delta 0.5 (* ix 0.2) (* iy 0.2))) 0.4))

                   d-radius 0.0  ;; FOR NOW
                   ;; d-radius (* 240.0 (* (abs (- delta 0.5)) 0.4))

                   ]

               [(keyword
                 (str ix "-" iy))
                {:pos {:x (- (* ix 360) (* (dec nx) 180))
                       :y (- (* iy 360) (* (dec ny) 180))}
                 :size {:w 160
                        :h 160}

                 }])
             ))


     ;; {:c-1 {:pos {:x -360 :y 0}
     ;;                 :radius (+ 80 d-radius)}
     ;;           :c-2 {:pos {:x    0 :y 0}
     ;;                 :radius (+ 160 (- d-radius))}
     ;;           :c-3 {:pos {:x  360 :y 0}
     ;;                 :radius (+ 80 d-radius)}}


     }))

