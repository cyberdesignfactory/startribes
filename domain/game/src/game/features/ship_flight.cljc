(ns game.features.ship-flight
  (:require
   [projection.circle :as circle]
   [projection.box :as box]
   [projection.group :as group]))

(defn flight-projection [pos rot vel ang-vel side-vel terrain dt-max-250ms]

  #_{:pos {:x 20.0 :y 0.0} :rot 0.0}

  (let [
        ship-angle (* 2 Math/PI rot)
        ;; projected-x (+ (:x pos)
        ;;                (* (/ vel 100.0) ;; 3.6  ;; thrust
        ;;                   0.36 ;; 0.001 ;; 0.36
        ;;                   dt-max-250ms
        ;;                   (Math/cos ship-angle))
        ;;                (* side-vel ;; strafe
        ;;                   -0.24
        ;;                   dt-max-250ms
        ;;                   (Math/sin ship-angle)))
        ;; projected-y (+ (:y pos)
        ;;                (* (/ vel 100.0) ;; 3.6  ;; thrust
        ;;                   0.36 ;; 0.001 ;; 0.36
        ;;                   dt-max-250ms
        ;;                   (Math/sin ship-angle))
        ;;                (* side-vel ;; strafe
        ;;                   0.24
        ;;                   dt-max-250ms
        ;;                   (Math/cos ship-angle)))


        ;; projected-x (+ (:x pos)
        ;;                (* (/ vel 100.0) ;; 3.6  ;; thrust
        ;;                   0.36 ;; 0.001 ;; 0.36
        ;;                   dt-max-250ms
        ;;                   (Math/sin ship-angle))
        ;;                (* side-vel ;; strafe
        ;;                   -0.24
        ;;                   dt-max-250ms
        ;;                   (Math/cos ship-angle)))
        ;; projected-y (- (:y pos)
        ;;                (* (/ vel 100.0) ;; 3.6  ;; thrust
        ;;                   0.36 ;; 0.001 ;; 0.36
        ;;                   dt-max-250ms
        ;;                   (Math/cos ship-angle))
        ;;                (* side-vel ;; strafe
        ;;                   0.24
        ;;                   dt-max-250ms
        ;;                   (Math/sin ship-angle)))


        projected-x (+ (:x pos)
                       (* (/ vel 100.0) ;; 3.6  ;; thrust
                          0.36 ;; 0.001 ;; 0.36
                          dt-max-250ms
                          (Math/sin ship-angle))
                       (* side-vel ;; strafe
                          0.24
                          dt-max-250ms
                          (Math/cos ship-angle)))
        projected-y (- (:y pos)
                       (* (/ vel 100.0) ;; 3.6  ;; thrust
                          0.36 ;; 0.001 ;; 0.36
                          dt-max-250ms
                          (Math/cos ship-angle))
                       (* side-vel ;; strafe
                          -0.24
                          dt-max-250ms
                          (Math/sin ship-angle)))



        projected-rot (mod (+ rot
                              (* (* ang-vel 10.0) ;; rudder
                                 0.00024
                                 dt-max-250ms)) 1.0)

        projected-pos {:x projected-x
                       :y projected-y}
        !adjusted-pos (atom projected-pos)

        ]

    #_(doseq [[ci c] (:circles terrain)]
      (swap! !adjusted-pos
             #(circle/pos-after-circle % c)))

    #_(doseq [[bi b] (:boxes terrain)]
      (swap! !adjusted-pos
             #(box/pos-after-box % b)))

    (let [[_ props & elements] terrain]
      (swap! !adjusted-pos
             #(group/pos-after-group % props elements)))

    {
     :pos @!adjusted-pos
     :rot projected-rot
     }

))

;; (defn update-ship [ship terrain dt-max-250ms]
(defn update-ship [[ship-id ship] terrain dt-max-250ms]
  (let [
        pos (get ship :pos {:x 0.0 :y 0.0})
        rot (get ship :rot 0.0)
        vel (* (get ship :thrust 0.0) 100.0)
        ang-vel (* (get ship :rudder 0.0) 0.1)
        ;; side-vel (* (get ship :strafe 0.0) 0.24)  ;; not sure what constant should be
        side-vel (* (get ship :strafe 0.0) 1.0)  ;; not sure what constant should be

        result (flight-projection pos rot vel ang-vel side-vel terrain dt-max-250ms)
        ]

    [ship-id (-> ship
                 (assoc :pos (:pos result))
                 (assoc :rot (:rot result)))]))

(defn update-bullet [[bullet-id bullet] terrain dt-max-250ms]
  (let [
        pos (get bullet :pos {:x 0.0 :y 0.0})
        rot (get bullet :rot 0.0)
        ;; vel 360.0
        vel (:vel bullet)
        ang-vel 0.0
        ;; side-vel (* (get ship :strafe 0.0) 0.24)  ;; not sure what constant should be
        side-vel 0.0

        result (flight-projection pos rot vel ang-vel side-vel terrain dt-max-250ms)
        ]

    [bullet-id (-> bullet
                   (assoc :pos (:pos result))
                   (assoc :rot (:rot result)))]))

(defn ship-flight-feature [world t]
  ;; just updating player's ship for now...
  #_(-> world
        (update-in [:tribes :y :ships :y-1] #(update-ship % (:terrain world) 10))
        )

  (update world :tribes
          (fn [tribes]
            (into {}
                  (map
                   (fn [[tribe-id tribe]]
                     [tribe-id
                      (-> tribe
                          (update :ships
                                  (fn [ships]
                                    (into {}
                                          (map #(update-ship % (:terrain world) 10) ships)
                                          )))
                          (update :bullets
                                  (fn [bullets]
                                    (into {}
                                          (map #(update-bullet % (:terrain world) 10) bullets)
                                          ))))])
                   tribes)))))


