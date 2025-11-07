(ns components.pixi.universe
  (:require
   ["@pixi/react" :refer [Stage Container Sprite Text Graphics]]
   [components.pixi.base :refer [pixi-base]]
   [components.pixi.ship :refer [pixi-ship]]
   [components.pixi.bullet :refer [pixi-bullet]]
   [components.pixi.nav-point :refer [pixi-nav-point]]
   [components.pixi.resource :refer [pixi-resource]]
   [components.pixi.terrain :refer [pixi-terrain]]
   [components.pixi.circle :refer [pixi-circle]]
   [components.pixi.box :refer [pixi-box]]
   ))

(defn pixi-universe [{:keys [universe
                             camera
                             width
                             height
                             player-thrust
                             player-rudder
                             player-strafe
                             on-base-select
                             on-ship-select]
                      :or {
                           ;; camera {:pos {:x 0 :y 0}
                           ;;         :scale 0.36}
                           width 395
                           height 600
                           }}]

  (let [camera-scale (get camera :scale 0.16)

        ;; player-thrust
        ;; (get-in universe [:tribes :y :ships :y-1 :thrust]
        ;;         0.0)

        ;; player-rudder
        ;; (get-in universe [:tribes :y :ships :y-1 :rudder]
        ;;         0.0)

        ;; player-strafe
        ;; (get-in universe [:tribes :y :ships :y-1 :strafe]
        ;;         0.0)

        ;; draw-thrust
        ;; (fn [^js/PIXI.Graphics g]
        ;;   (.clear g)
        ;;   (.beginFill g 0xffaa33)
        ;;   #_(.drawRect g 20 240 20 (* (- 1.0 player-thrust) 240.0))
        ;;   (.drawRect g 20 240 20 200)
        ;;   )

        draw-thrust
        (fn [^js/PIXI.Graphics g]
          (.clear g)
          (.beginFill g 0xffaa33)
          (doseq [x [20 400]]
            (when (< player-thrust 0.0)
              (.drawRect g
                         x
                         480
                         20
                         (* player-thrust -240.0)))
            (when (> player-thrust 0.0)
              (.drawRect g x
                         (- 480 (* (abs player-thrust) 240.0))
                         20
                         (* (abs player-thrust) 240.0)))))

        ;; draw-rudder (fn [^js/PIXI.Graphics g]
        ;;               (.clear g)
        ;;               (.beginFill g 0xffaa33)
        ;;               (.drawRect g 20 240 (- 480.0
        ;;                                      (* player-rudder 240.0))
        ;;                          20)
        ;;               )

        draw-rudder (fn [^js/PIXI.Graphics g]
                      (.clear g)
                      (.beginFill g 0xffaa33)
                      (when (< player-rudder 0.0)
                        (.drawRect g
                                   (- 220
                                      (* player-rudder -200.0))
                                   160
                                   (* player-rudder -200.0)
                                   20))
                      (when (> player-rudder 0.0)
                        (.drawRect g
                                   220
                                   160
                                   (* player-rudder 200.0)
                                   20)))

         ;; draw-strafe (fn [^js/PIXI.Graphics g]
         ;;               (.clear g)
         ;;               (.beginFill g 0xffaa33)
         ;;               #_(.drawRect g 20 240 240 20)
         ;;               (.drawRect g 20 480 (- 480.0
         ;;                                      (* player-strafe 240.0))
         ;;                          20
         ;;                          )
         ;;               )

        draw-strafe (fn [^js/PIXI.Graphics g]
                      (.clear g)
                      (.beginFill g 0xffaa33)
                      (when (< player-strafe 0.0)
                        (.drawRect g
                                   (- 220
                                      (* player-strafe -200.0))
                                   720
                                   (* player-strafe -200.0)
                                   20))
                      (when (> player-strafe 0.0)
                        (.drawRect g
                                   220
                                   720
                                   (* player-strafe 200.0)
                                   20)))

        ]

    [:> Stage {:width width :height height
               :options {:background-color 0x000033}}


     ;; we only want this for mobile
     ;; (probably a better way of doing this)
     (when (< width 800)
       [:> Graphics
        {:draw draw-thrust
         :alpha 0.24
         :rotation 0.0}])

     (when (< width 800)
       [:> Graphics
        {:draw draw-rudder
         :alpha 0.24
         :rotation 0.0}])

     (when (< width 800)
       [:> Graphics
        {:draw draw-strafe
         :alpha 0.24
         :rotation 0.0}])


     [:> Container {:x (/ width 2)
                    :y (/ height 2)
                    :rotation (- (* 2 (.-PI js/Math) (get-in camera [:rot] 0.0))
                                 (/ (.-PI js/Math) 2))
                    :scale camera-scale}
      [:> Container {:x (- (* (get-in camera [:pos :x] 0)))
                     :y (- (* (get-in camera [:pos :y] 0)))}

       [pixi-terrain (:terrain universe)]


       #_(for [[id circle] (:circles (:terrain universe))]
         [pixi-circle (assoc circle :key id)])

       #_(for [[id box] (:boxes (:terrain universe))]
         [pixi-box (assoc box :key id)])

       (for [[id nav-point] (:nav-points universe)]
         [pixi-nav-point (-> nav-point
                             (assoc :key id))])

       (for [[tribe-id tribe] (:tribes universe)
             [id base] (:bases tribe)]
         [pixi-base (-> base
                        (assoc :key id)
                        (assoc :grades
                               (vals
                                (get-in universe [:tribes tribe-id :production])))
                        (assoc :colour
                               (get-in universe [:tribes tribe-id :colour])))
          (fn []
            (on-base-select id))])

       (for [[tribe-id tribe] (:tribes universe)
             [id ship] (:ships tribe)]
         [pixi-ship (-> ship
                        (assoc :key id)
                        (assoc :grades
                               (vals
                                (get-in universe [:tribes tribe-id :production])))
                        (assoc :colour
                               (get-in universe [:tribes tribe-id :colour])))
          (fn []
            (on-ship-select id))])

       (for [[tribe-id tribe] (:tribes universe)
             [id bullet] (:bullets tribe)]
         [pixi-bullet (-> bullet
                          (assoc :key id)
                          (assoc :colour
                                 (get-in universe [:tribes tribe-id :colour])))])

       (for [[grade-id grade] (:grades universe)
             [id resource] (:resources grade)]
         [pixi-resource (-> resource
                            (assoc :key id)
                            (assoc :colour
                                   (get-in universe [:grades grade-id :colour])))])]

      #_[:> Text {:text (str camera)
                  :style {:fill 0xff7777
                          :stroke 0xffffff}}]]
     ]))

