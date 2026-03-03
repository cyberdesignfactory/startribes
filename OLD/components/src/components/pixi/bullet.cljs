(ns components.pixi.bullet
  (:require
   ["@pixi/react" :refer [Stage Container Sprite Text Graphics]]
   [components.pixi.helpers :refer [resource-color]]))

(defn pixi-bullet [bullet]

  ;; not sure if useCallback needed here, doesn't seem to make any difference

  (let [bullet-color (:colour bullet)
        bullet-x (get-in bullet [:pos :x])
        bullet-y (get-in bullet [:pos :y])
        draw (fn [^js/PIXI.Graphics g]
               (.clear g)
               (.beginFill g bullet-color)
               (.drawRoundedRect g 0 0 36 16 8)
               #_(.endFill g))]

    [:> Container {:x bullet-x :y bullet-y}
     [:> Graphics {:draw draw
                   :rotation (- (* (get-in bullet [:rot]) 2 Math/PI)
                                (/ Math/PI 2))}]]))


