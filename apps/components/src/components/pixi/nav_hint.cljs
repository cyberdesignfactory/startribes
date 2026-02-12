(ns components.pixi.nav-hint
  (:require
   ["@pixi/react" :refer [Stage Container Sprite Text Graphics]]
   ))

(defn pixi-nav-hint [nav-hint]

  ;; not sure if useCallback needed here, doesn't seem to make any difference

  (let [nav-hint-colour (get-in nav-hint [:colour])
        nav-hint-inner-colour (get-in nav-hint [:inner-colour])
        nav-hint-x (get-in nav-hint [:pos :x])
        nav-hint-y (get-in nav-hint [:pos :y])
        nav-hint-radius 48
        nav-hint-rot (get nav-hint :rot 0.0)
        margin 16
        draw (fn [^js/PIXI.Graphics g]
               (.clear g)
               (.beginFill g nav-hint-colour)

               #_(.drawCircle g 0 0 nav-hint-radius)
               ;; (.drawCircle g 0 0 (- nav-hint-radius margin))

               ;; (.beginFill g nav-hint-inner-colour)
               (.beginFill g nav-hint-colour)
               ;; (.drawRect g -24 -4 48 8)
               ;; (.drawRect g -4 -24 8 48)
               (.drawRect g -48 -4 32 8)
               (.drawRect g  16 -4 32 8)

               (.drawRect g -4 -48 8 32)
               (.drawRect g -4  16 8 32)

               (.endFill g))]

    [:> Container {:x nav-hint-x :y nav-hint-y}
     [:> Graphics {:draw draw
                   :rotation (* nav-hint-rot 2 Math/PI)

                   }]]))

