(ns components.pixi.resource
  (:require
   ["@pixi/react" :refer [Stage Container Sprite Text Graphics]]
   [components.pixi.helpers :refer [tribe-color resource-color tribe-resource-types]]))


#_(defn tribe-color [tribe-id]
  (case tribe-id
    :r 0xff3333
    :y 0xffaa33
    :g 0x33cc33
    :b 0x3333ff
    0x777777))

(defn pixi-resource [resource]

  ;; not sure if useCallback needed here, doesn't seem to make any difference

  (let [;; resource-color (resource-color (:grade-id resource))
        resource-color (:colour resource)
        resource-x (get-in resource [:pos :x])
        resource-y (get-in resource [:pos :y])

        size (* 2 (min 16 (max 4 (get resource :amount 1))))
        ;; size (min 4 (get resource :amount 1))
        ;; size (get resource :amount 1)

        draw (fn [^js/PIXI.Graphics g]
               (.clear g)
               (.beginFill g resource-color)

               #_(.drawCircle g 0 0 8)
               (.drawCircle g 0 0 size)

               #_(.endFill g))

               ]

    [:> Container {:x resource-x :y resource-y}
     [:> Graphics {:draw draw}]]

        )

  )


