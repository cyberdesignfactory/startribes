(ns components.pixi.universe
  (:require
   ["@pixi/react" :refer [Stage Container Sprite Text Graphics]]
   ;; copied from domain/ui for now...
   [components.pixi.geometry :as geometry]
   [components.pixi.base :refer [pixi-base]]
   [components.pixi.ship :refer [pixi-ship]]
   [components.pixi.bullet :refer [pixi-bullet]]
   [components.pixi.nav-point :refer [pixi-nav-point]]
   [components.pixi.resource :refer [pixi-resource]]
   [components.pixi.terrain :refer [pixi-terrain]]
   [components.pixi.nav-hint :refer [pixi-nav-hint]]
   [components.pixi.circle :refer [pixi-circle]]
   [components.pixi.box :refer [pixi-box]]))

(defn pixi-universe [{:keys [universe
                             camera
                             width
                             height
                             header-margin
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

        nav-pos (get-in universe [:tribes :y :ships :y-1 :nav-pos])

        ship-pos (get-in universe [:tribes :y :ships :y-1 :pos])
        ship-rot (get-in universe [:tribes :y :ships :y-1 :rot])

        rotated-nav-pos
        (geometry/rotate-pos-around-pos nav-pos ship-pos (- ship-rot))
        clipped-rotated-nav-pos
        (geometry/clip-pos-to-box rotated-nav-pos
                                  {:pos (-> ship-pos
                                            (update :y #(+ % 2)))
                                   :size {:w (/ width (:scale camera))
                                          :h (/ height (:scale camera))}})

        nav-hint-pos
        (geometry/rotate-pos-around-pos clipped-rotated-nav-pos
                                        ship-pos
                                        ship-rot)

        thrust-y (/ height 1.8)
        half-y (/ thrust-y 1.5)
        draw-thrust
        (fn [^js/PIXI.Graphics g]
          (.clear g)
          (.beginFill g 0xffaa33)
          (doseq [x [20 (- width 40)]]  ;; 400
            (when (< player-thrust 0.0)
              (.drawRect g
                         x
                         thrust-y
                         20
                         (* player-thrust (- half-y))))
            (when (> player-thrust 0.0)
              (.drawRect g x
                         (- thrust-y (* (abs player-thrust) half-y))
                         20
                         (* (abs player-thrust) half-y)))))


        ;; draw-thrust
        ;; (fn [^js/PIXI.Graphics g]
        ;;   (.clear g)
        ;;   (.beginFill g 0xffaa33)
        ;;   (doseq [x [20 400]]
        ;;     (when (< player-thrust 0.0)
        ;;       (.drawRect g
        ;;                  x
        ;;                  480
        ;;                  20
        ;;                  (* player-thrust -240.0)))
        ;;     (when (> player-thrust 0.0)
        ;;       (.drawRect g x
        ;;                  (- 480 (* (abs player-thrust) 240.0))
        ;;                  20
        ;;                  (* (abs player-thrust) 240.0)))))

        ;; x-middle 220
        ;; rudder-y 160
        ;; half-w 200.0
        x-middle (/ width 2)
        rudder-y (+ header-margin 20)
        half-w (- (/ width 2) 20)
        draw-rudder (fn [^js/PIXI.Graphics g]
                      (.clear g)
                      (.beginFill g 0xffaa33)
                      (when (< player-rudder 0.0)
                        (.drawRect g
                                   (- x-middle
                                      (* player-rudder (- half-w)))
                                   rudder-y
                                   (* player-rudder (- half-w))
                                   20))
                      (when (> player-rudder 0.0)
                        (.drawRect g
                                   x-middle
                                   rudder-y
                                   (* player-rudder half-w)
                                   20)))

        ;; draw-rudder (fn [^js/PIXI.Graphics g]
        ;;               (.clear g)
        ;;               (.beginFill g 0xffaa33)
        ;;               (when (< player-rudder 0.0)
        ;;                 (.drawRect g
        ;;                            (- 220
        ;;                               (* player-rudder -200.0))
        ;;                            160
        ;;                            (* player-rudder -200.0)
        ;;                            20))
        ;;               (when (> player-rudder 0.0)
        ;;                 (.drawRect g
        ;;                            220
        ;;                            160
        ;;                            (* player-rudder 200.0)
        ;;                            20)))

        strafe-y (- height 40)
        draw-strafe (fn [^js/PIXI.Graphics g]
                      (.clear g)
                      (.beginFill g 0xffaa33)
                      (when (< player-strafe 0.0)
                        (.drawRect g
                                   (- x-middle
                                      (* player-strafe (- half-w)))
                                   strafe-y
                                   (* player-strafe (- half-w))
                                   20))
                      (when (> player-strafe 0.0)
                        (.drawRect g
                                   x-middle
                                   strafe-y
                                   (* player-strafe half-w)
                                   20)))

        ;; draw-strafe (fn [^js/PIXI.Graphics g]
        ;;               (.clear g)
        ;;               (.beginFill g 0xffaa33)
        ;;               (when (< player-strafe 0.0)
        ;;                 (.drawRect g
        ;;                            (- 220
        ;;                               (* player-strafe -200.0))
        ;;                            720
        ;;                            (* player-strafe -200.0)
        ;;                            20))
        ;;               (when (> player-strafe 0.0)
        ;;                 (.drawRect g
        ;;                            220
        ;;                            720
        ;;                            (* player-strafe 200.0)
        ;;                            20)))

        ]

    [:> Stage {:width width :height height

               :style {
                       "-webkit-user-select" "none"
                       "user-select" "none"
                       "-webkit-touch-callout" "none"
                       }
               
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

                    :rotation
                    (- (* 2
                          (.-PI js/Math)
                          (get-in camera [:rot] 0.0))
                       #_(/ (.-PI js/Math) 2))

                    :scale camera-scale}
      [:> Container {:x (- (* (get-in camera [:pos :x] 0)))
                     :y (- (* (get-in camera [:pos :y] 0)))}

       [pixi-terrain (:terrain universe)]

       #_(when nav-pos
         [pixi-nav-hint {:pos nav-hint-pos
                         :rot ship-rot
                         :colour 0xffaa33
                         :inner-colour 0xcc7700}])

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
                                   (get-in universe [:grades grade-id :colour])))])



       ]

      #_[:> Text {:text (str (get-in universe [:tribes :y :ships :y-1 :target]))
                :style {:fill 0xff7777
                        :stroke 0xffffff}}]

      #_[:> Text {:text (str nav-hint-pos-vector)
                  :style {:fill 0xff7777
                          :stroke 0xffffff}}]]
     ]))

