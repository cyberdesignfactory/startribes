(ns components.pixi.terrain
  (:require
   ["@pixi/react" :refer [Stage Container Sprite Text Graphics]]
   [components.pixi.circle :as circle]
   [components.pixi.box :as box]
   ))

;; pixi-circle

;; pixi-box
#_(defn pixi-box [box]

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
               (.endFill g))]

    [:> Container {:x box-x :y box-y}
     [:> Graphics {:draw draw}]]
    )
  )

(defn pixi-group [group-props elements]

  [:> Container
   {:x (get-in group-props [:pos :x] 0.0)
    :y (get-in group-props [:pos :y] 0.0)}

   ;; use group-props here

   (for [[type props & inner-elements] elements]
     (case type
       :group [pixi-group props inner-elements]
       :circle [circle/pixi-circle props]
       :box [box/pixi-box props]
       ))

   #_[pixi-box {:pos {:x 0 :y 0}
                :size {:w 120 :h 120}}   ]]

  )

;; (defn pixi-terrain [[_ props & inner-elements]]
(defn pixi-terrain [terrain]

  (let [[_ props & elements] terrain]

    ;; assume top level of terrain is a group
    #_[pixi-group props inner-elements]
    [pixi-group props elements]
    #_(concat
       [pixi-group props]
       inner-elements
       ))


  )

