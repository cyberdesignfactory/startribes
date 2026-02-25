(ns game.features.energy-recharge-test
  (:require [clojure.test :refer :all]
            [game.features.energy-recharge :refer :all]))

(deftest base-energy-recharge-test
  (let [projected-universe
        {
         :tribes {:y {:bases {:y-1 {:energy 1.0}
                              :y-2 {:energy 0.7}}}}}
        t 4200
        universe (energy-recharge projected-universe t)]

    (is (= 1.0  (get-in universe [:tribes :y :bases :y-1 :energy])))
    ;; (is (= 0.72 (get-in universe [:tribes :y :bases :y-2 :energy])))
    (is (= 0.702 (get-in universe [:tribes :y :bases :y-2 :energy])))

    ))

(deftest ship-energy-recharge-test
  (let [projected-universe
        {
         :tribes {:y {:ships {:y-1 {:energy 1.0}
                              :y-2 {:energy 0.7}}}}}
        t 4200
        universe (energy-recharge projected-universe t)]

    (is (= 1.0  (get-in universe [:tribes :y :ships :y-1 :energy])))
    ;; (is (= 0.72 (get-in universe [:tribes :y :ships :y-2 :energy])))
    (is (= 0.702 (get-in universe [:tribes :y :ships :y-2 :energy])))

    ))
