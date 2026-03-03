(ns ui.geometry-test
  (:require [clojure.test :refer [deftest is testing]]
            [ui.geometry :as geometry]))

(deftest rotate-pos-around-pos-test
  (let [pos {:x 100 :y 0}
        pivot-pos {:x 0 :y 0}
        rot 0.25

        pos'
        (geometry/rotate-pos-around-pos pos pivot-pos rot)

        ]

    (is (< (Math/abs (- 0.0 (:x pos'))) 0.01))
    (is (< (Math/abs (- 100.0 (:y pos'))) 0.01))))

(deftest clip-pos-to-box-test
  (let [pos {:x 200.0 :y 0.0}
        box {:pos {:x 0.0 :y 0.0}
             :size {:w 200.0 :h 200.0}}
        pos' (geometry/clip-pos-to-box pos box)]

    (is (= {:x 100.0 :y 0.0}
           pos')))


  )
