(ns components.pixi.resource
  (:require
   ["@pixi/react" :refer [Stage Container Sprite Text Graphics]]
   [components.pixi.helpers :refer [resource-color]]))

(defn pixi-resource [resource]

  ;; not sure if useCallback needed here, doesn't seem to make any difference

  (let [;; resource-color (resource-color (:grade-id resource))
        resource-color (:colour resource)
        resource-x (get-in resource [:pos :x])
        resource-y (get-in resource [:pos :y])

        size (* 2 (min 16 (max 4 (get resource :amount 1))))

        draw (fn [^js/PIXI.Graphics g]
               (.clear g)
               (.beginFill g resource-color)
               (.drawCircle g 0 0 size)
               #_(.endFill g))]

    [:> Container {:x resource-x :y resource-y}
     [:> Graphics {:draw draw}]]))


