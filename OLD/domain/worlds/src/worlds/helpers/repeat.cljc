(ns worlds.helpers.repeat
  (:require [clojure.pprint :refer [pprint]]
            [worlds.helpers.prefix :refer [prefix-world-keys]]
            [worlds.helpers.merge :refer [merge-worlds]]
            [worlds.helpers.translate :refer [translate-world]]
            [worlds.helpers.scale :refer [scale-world]]
            ))

(defn repeat-world [world instances]
  (merge-worlds
   (map (fn [[id instance]]
          (-> world
              ;; should perhaps allow 'nil' for the scale value and return identity
              (scale-world (get instance :scale {:x 1.0 :y 1.0}))

              ;; should perhaps allow 'nil' for the pos value and return identity
              (translate-world (get instance :pos {:x 0.0 :y 0.0}))

              (prefix-world-keys id)

              ))
        instances)))
