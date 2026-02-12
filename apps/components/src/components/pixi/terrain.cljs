(ns components.pixi.terrain
  (:require
   ["@pixi/react" :refer [Stage Container Sprite Text Graphics]]
   [components.pixi.circle :as circle]
   [components.pixi.box :as box]))

(defn pixi-group [group-props elements]
  [:> Container
   {:x (get-in group-props [:pos :x] 0.0)
    :y (get-in group-props [:pos :y] 0.0)}

   ;; use group-props here
   (for [[type props & inner-elements] elements]
     (case type
       :group [pixi-group props inner-elements]
       :circle [circle/pixi-circle props]
       :box [box/pixi-box props]))])

(defn pixi-terrain [terrain]
  (let [[_ props & elements] terrain]
    ;; assume top level of terrain is a group
    [pixi-group props elements]))

