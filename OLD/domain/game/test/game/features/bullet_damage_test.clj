(ns game.features.bullet-damage-test
  (:require [clojure.test :refer :all]
            [game.features.bullet-damage :refer :all]))

(deftest bullet-damages-enemy-base
  (let [projected-world
        {:tribes {:r {:strategy :aggressive
                      :bases {:r-1 {:tribe-id :r
                                    :t 4000
                                    :pos {:x 0 :y 0}
                                    :rot 0.0
                                    :vel 100.0
                                    :energy 0.8}}}
                  :y {:strategy :retaliatory
                      :bullets {:y-1 {:tribe-id :y
                                      :t 4000
                                      :pos {:x 10 :y 0}
                                      :rot 0.0
                                      :vel 360.0
                                      :base-id :y-1}}}}}
    t 6000
    world (bullet-damage projected-world t)]

  (is (= (get-in world [:tribes :r :bases :r-1 :energy]) 0.7000000000000001))
  (is (nil? (get-in world [:tribes :y :bullets :y-1])))
  #_(is (= (get-in world [:tribes :y :standings :r]) 0.02))))

(deftest bullet-damages-retaliatory-tribe
  (let [projected-world
        {:tribes {:r {:strategy :aggressive
                      :bullets {:r-1 {:t 4000
                                      :pos {:x 0 :y 0}
                                      :rot 0.0
                                      :vel 360.0
                                      :ship-id :r-1}}}
                  :y {:strategy :retaliatory
                      :ships {:y-1 {:t 4000
                                    :pos {:x 10 :y 0}
                                    :rot 0.0
                                    :vel 100.0
                                    :energy 0.8}}}}}
        t 6000
        world (bullet-damage projected-world t)]

    (is (= (get-in world [:tribes :y :ships :y-1 :energy]) 0.6000000000000001))
    (is (nil? (get-in world [:tribes :r :bullets :r-1])))
    #_(is (= (get-in world [:tribes :y :standings :r]) -0.04))))

(deftest bullet-from-retaliatory-tribe-damages
  (let [projected-world
        {:tribes {:r {:strategy :aggressive
                      :ships {:r-1 {:tribe-id :r
                                    :t 4000
                                    :pos {:x 0 :y 0}
                                    :rot 0.0
                                    :vel 100.0
                                    :energy 0.8}}}
                  :y {:strategy :retaliatory
                      :bullets {:y-1 {:tribe-id :y
                                      :t 4000
                                      :pos {:x 10 :y 0}
                                      :rot 0.0
                                      :vel 360.0
                                      :ship-id :y-1}}}}}
    t 6000
    world (bullet-damage projected-world t)]

  (is (= (get-in world [:tribes :r :ships :r-1 :energy]) 0.6000000000000001))
  (is (nil? (get-in world [:tribes :y :bullets :y-1])))
  #_(is (= (get-in world [:tribes :y :standings :r]) 0.02))))

