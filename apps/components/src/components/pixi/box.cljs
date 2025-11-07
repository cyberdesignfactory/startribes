(ns components.pixi.box
  (:require
   ["@pixi/react" :refer [Stage Container Sprite Text Graphics]]
   ))

(defn pixi-box [box]

  ;; not sure if useCallback needed here, doesn't seem to make any difference

  (let [box-color 0x777777
        box-inner-color 0x888888
        box-x (get-in box [:pos :x])
        box-y (get-in box [:pos :y])
        box-w (get-in box [:size :w])
        box-h (get-in box [:size :h])
        margin 16
        draw (fn [^js/PIXI.Graphics g]
               (.clear g)
               (.beginFill g box-color)
               (.drawRoundedRect g
                                 (- (/ box-w 2))
                                 (- (/ box-h 2))
                                 box-w
                                 box-h
                                 8
                                 )
               (.endFill g)
               (.beginFill g box-inner-color)
               (.drawRoundedRect g
                                 (- (/ (- box-w margin) 2))
                                 (- (/ (- box-h margin) 2))
                                 (- box-w margin)
                                 (- box-h margin)
                                 8
                                 )
               (.endFill g)

               )

        ]

    [:> Container {:x box-x :y box-y}
     [:> Graphics {:draw draw}]]

    )
  )

