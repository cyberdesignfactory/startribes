(ns components.pixi.ship
  (:require
   ["@pixi/events"]
   ["@pixi/react" :refer [Stage Container Sprite Text Graphics]]
   [components.pixi.helpers :refer [resource-color]]))

(defn pixi-ship [ship on-select]
  (let [ship-color (:colour ship)
        ship-x (get-in ship [:pos :x])
        ship-y (get-in ship [:pos :y])
        draw (fn [^js/PIXI.Graphics g]
               (.clear g)
               (.beginFill g ship-color)
               (.drawCircle g 0 0 30)
               (.drawRoundedRect g -60 -22 36 12 2)
               (.drawRoundedRect g -60  10 36 12 2)
               (.endFill g)

               (let [
                     ;; ship-rts (tribe-resource-types tribe-id)
                     ship-rts (:grades ship)
                     ]
                 (doseq [n (range 5)]
                   (let [
                         rt (nth ship-rts n)
                         amount (get-in ship [:inv rt])
                         ]
                     (let [height 10
                           width (* 2 amount)
                           x-pos -16
                           y-pos (- (* n height) (/ (* 5 height) 2))
                           ]
                       (.beginFill g (resource-color rt))
                       (.drawRoundedRect g x-pos y-pos width height 8)
                       (.endFill g))))))

        draw-thrusters (fn [^js/PIXI.Graphics g]
                         (.clear g)

                         ;; depending on thrust setting...

                         (.lineStyle g 2 0xff3333)
                         (.drawRect g -62 8 24 16)
                         (.beginFill g 0xaa3333)
                         (.drawRect g -62 8 (* (:thrust ship) 24) 16)
                         (.endFill g)

                         (.lineStyle g 2 0xff3333)
                         (.drawRect g -62 -24 24 16)
                         (.beginFill g 0xaa3333)
                         (.drawRect g -62 -24 (* (:thrust ship) 24) 16)
                         (.endFill g))

        draw-energy (fn [^js/PIXI.Graphics g]
                      (.clear g)
                      (when (< (get ship :energy 1.0) 1.0)
                        (.lineStyle g 2 0x244824)
                        (.drawRect g -32 72 64 8)
                        (.beginFill g 0x448844)
                        (.drawRect g -32 72 (* (:energy ship) 64) 8)
                        (.endFill g)))]

    [:> Container {:x ship-x
                   :y ship-y
                   :rotation (- (/ Math/PI 2))}
     [:> Graphics {:draw draw
                   :rotation (* (get-in ship [:rot]) 2 Math/PI)
                   :pointerdown #(on-select)
                   :interactive true
                   ;; :hit-area (js/PIXI.Rectangle. 0 0 50 50)
                   }]
     [:> Graphics {:draw draw-energy
                   :rotation (+ (/ Math/PI 2)  (* (get-in ship [:rot]) 2 Math/PI))
                   :alpha 0.6}]

     #_[:> Graphics {:draw draw-thrusters
                   :rotation (+ #_(/ Math/PI 2)  (* (get-in ship [:rot]) 2 Math/PI))
                   ;; :alpha 0.6
                   }]]))


