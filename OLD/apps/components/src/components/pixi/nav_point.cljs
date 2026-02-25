(ns components.pixi.nav-point
  (:require
   ["@pixi/react" :refer [Stage Container Sprite Text Graphics]]
   ))


(defn pixi-nav-point [nav-point]

  ;; not sure if useCallback needed here, doesn't seem to make any difference

  [:> Container
   (let [nav-point-color 0x000033 ;; 0x444444 ;; 0x000044
         nav-point-x (get-in nav-point [:x])
         nav-point-y (get-in nav-point [:y])
         draw (fn [^js/PIXI.Graphics g]
                (.clear g)
                (.beginFill g nav-point-color)

                (.drawCircle g 0 0 8)

                #_(.endFill g))

         ]

     [:> Container {:x nav-point-x :y nav-point-y}
      [:> Graphics {:draw draw}]]

     )]

  )

