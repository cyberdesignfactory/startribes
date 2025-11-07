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
    (is (= {:pos {:x 72.0 :y 0.0} :rot 0.0}
           (flight-projection pos rot vel ang-vel side-vel terrain dt-max-250ms)))))

(deftest ship-flight-test
  (testing "No blocks, straight flight"
    (let [initial-universe
          {:tribes
           {:y
            {
             :ships {:y-1 {:controls {:thrust 1.0
                                      :rudder -1.0}
                           :fs {:t 4200
                                :pos {:x 20.0 :y 0.0}
                                :rot 0.0
                               ;;  :vel 100.0
                               ;;  :ang-vel 0.0
                                }}}
             :blocks {}

             }}}

          t 4200
          universe (ship-flight/ship-flight-feature initial-universe t)]

      (is (= {:t 4200
              :pos {:x 20.0 :y 0.0}
              :rot 0.0
             ;; :vel 100.0
             ;; :ang-vel -0.1
              }

             (get-in universe [:tribes :y :ships :y-1 :fs])

             ))

      #_(is (= [[[:ships :y-1 :fs] {:t 4200
                                  :pos {:x 20.0 :y 0.0}
                                  :rot 0.0
                                  :vel 100.0
                                  :ang-vel -0.1}]]
             events))))

  #_(testing "No blocks, circular flight"
    (let [universe {:ships {:y-1 {:controls {:thrust 1.0
                                          :rudder 0.0}
                               :fs {:t 4000
                                    :pos {:x 0.0 :y 0.0}
                                    :rot 0.0
                                    :vel 100.0
                                    :ang-vel 0.1}}}
                 :blocks {}}
          t 4200
          events (ship-flight universe t)]

      (is (= [[[:ships :y-1 :fs] {:t 4200
                                  :pos {:x 19.955026456134178, :y 1.1296345150181601}
                                  :rot 0.020000000000000004,
                                  :vel 100.0
                                  :ang-vel 0.0}]]
             events)))))
