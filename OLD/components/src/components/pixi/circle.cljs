(ns components.pixi.circle
  (:require
   ["@pixi/react" :refer [Stage Container Sprite Text Graphics]]
   ))

(defn pixi-circle [circle]

  ;; not sure if useCallback needed here, doesn't seem to make any difference

  (let [circle-color 0x777777
        circle-inner-color 0x888888
        circle-x (get-in circle [:pos :x])
        circle-y (get-in circle [:pos :y])
        circle-radius (get-in circle [:radius])
        margin 16
        draw (fn [^js/PIXI.Graphics g]
               (.clear g)
               (.beginFill g circle-color)

               (.drawCircle g 0 0 circle-radius)

               (.endFill g)
               (.beginFill g circle-inner-color)
               (.drawCircle g 0 0 (- circle-radius margin))

               (.endFill g))]

    [:> Container {:x circle-x :y circle-y}
     [:> Graphics {:draw draw}]]))

