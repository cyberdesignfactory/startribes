(ns components.pixi.base
  (:require ["@pixi/react" :refer [Stage Container Sprite Graphics]]
            [components.pixi.helpers :refer [resource-color]]))

(defn pixi-base [base on-select]
  (let [tribe-id (:tribe-id base)
        base-color (:colour base)
        base-x (get-in base [:pos :x])
        base-y (get-in base [:pos :y])
        draw (fn [^js/PIXI.Graphics g]
               (.clear g)
               (.beginFill g base-color)
               (.drawCircle g 0 0 60)
               (.endFill g)

               (let [base-rts (:grades base)]
                 (doseq [n (range 5)]
                   (let [rt (nth base-rts n)
                         amount (get-in base [:inv rt])]
                     (let [width 16
                           height (* 3.6 amount)
                           x-pos (- (* n width) (/ (* 5 width) 2))
                           y-pos (- 36 height)
                           ]
                       (.beginFill g (resource-color rt))
                       (.drawRoundedRect g x-pos y-pos width height 8)
                       (.endFill g))))))

        draw-energy (fn [^js/PIXI.Graphics g]
                      (.clear g)
                      (when (< (get base :energy 1.0) 1.0)
                        (.lineStyle g 2 0x244824)
                        (.drawRect g -32 72 64 8)
                        (.beginFill g 0x448844)
                        (.drawRect g -32 72 (* (:energy base) 64) 8)
                        (.endFill g)))]

    [:> Container {:x base-x :y base-y}
     [:> Graphics {
                   :draw draw
                   :pointerdown #(on-select)
                   :interactive true
                   }]
     [:> Graphics {:draw draw-energy
                   :alpha 0.6}]]))


