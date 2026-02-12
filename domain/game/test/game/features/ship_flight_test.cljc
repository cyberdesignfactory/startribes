(ns game.features.ship-flight-test
  (:require [clojure.test :refer :all]
            [game.features.ship-flight :refer [flight-projection] :as ship-flight]))

(deftest flight-projection-test
  (let [pos {:x 0.0 :y 0.0}
        rot 0.0
        vel 100.0
        ang-vel 0.0
        side-vel 0.0
        terrain {}
        dt-max-250ms 200]
    (is (= {:pos {:x 0.0 :y -72.0} :rot 0.0}
           (flight-projection pos rot vel ang-vel side-vel terrain dt-max-250ms))))
  #_(let [pos {:x 0.0 :y 0.0}
        rot 0.25
        vel 100.0
        ang-vel 0.0
        side-vel 0.0
        terrain {}
        dt-max-250ms 200]
    (is (= {:pos {:x 72.0 :y 0.0} :rot 0.25}
           (flight-projection pos rot vel ang-vel side-vel terrain dt-max-250ms)))))

(deftest ship-flight-test
  (testing "No blocks, straight flight"
    (let [initial-universe
          {:tribes
           {:y
            {:ships {:y-1 {:controls {:thrust 1.0
                                      :rudder -1.0}
                           :pos {:x 20.0 :y 0.0}
                           :rot 0.0}}
             :blocks {}}}}
          t 4200
          universe (ship-flight/ship-flight-feature initial-universe t)]

      (is (= {:controls {:thrust 1.0
                         :rudder -1.0}
              :pos {:x 20.0 :y 0.0}
              :rot 0.0}
             (get-in universe [:tribes :y :ships :y-1]))))))

