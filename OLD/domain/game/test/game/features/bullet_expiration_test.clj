(ns game.features.bullet-expiration-test
  (:require [clojure.test :refer :all]
            [game.features.bullet-expiration :refer :all]))

(deftest bullet-expiration-test
  (let [projected-universe
        {:tribes {:r {:bullets {:r-1 {:t 3800
                                      :pos {:x (- 360 720) :y -200}
                                      :rot 0.0
                                      :vel 360.0
                                      :ship-id :r-1}
                                :r-2 {:t 4200
                                      :pos {:x (- 360 720) :y -200}
                                      :rot 0.0
                                      :vel 360.0
                                      :ship-id :r-1}}}}}
        t 6000
        universe (bullet-expiration projected-universe t)]
    (is (nil? (get-in universe [:tribes :r :bullets :r-1])))
    (is (not (nil? (get-in universe [:tribes :r :bullets :r-2]))))))

