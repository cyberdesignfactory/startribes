(ns game.features.auto-navigation-test
  (:require [clojure.test :refer :all]
            [game.features.auto-navigation :refer :all]))

(defn ship [pos]
  {:controls {:thrust 0.1
              :rudder 0.1
              :fire-pressed? false}
   :fs {:t 4000
        :pos pos
        :rot 0.0
        :vel 0.0
        :ang-vel 0.0}
   :target {:type :resource
            :grade-id :y
            :id :y-1}})

(deftest auto-navigation-test
  (let [test-universe
        {
         ;; :blocks {:bl-1 {:pos {:x 50 :y 100} :size {:w 20 :h 220}}}
         ;; :nav-points {:np-1 {:x 40 :y -40}
         ;;              :np-2 {:x 60 :y -40}}
         ;; :grades {:y {:resources {:y-1 {:grade :y
         ;;                                :pos {:x 100.0 :y 0.0}
         ;;                                :amount 4}}}}
         ;; :tribes {:y {:ships {:y-1 (ship {:x -180.0 :y -60.0})
         ;;                      :y-2 (ship {:x 0.0 :y   0.0})
         ;;                      :y-3 (ship {:x -80.0 :y -40.0})}
         ;;              :bases {:y-1 {:tribe-id :y
         ;;                            :pos {:x 100.0 :y 0.0}}}}}
         }]

    (testing "Targets a ship which can be reached directly"
      (let [initial-universe
            (-> test-universe
                (assoc-in [:tribes :r :ships :r-1]
                          {:t 4000
                           ;; :pos {:x 400.0 :y 0.0}
                           :pos {:x 0.0 :y -400.0}
                           :rot 0.0
                           :target {:type :ship
                                    :tribe-id :y
                                    :id :y-1}
                           })
                (assoc-in [:tribes :y :ships :y-1]
                          {:t 4000
                           :pos {:x 0.0 :y 0.0}
                           :rot 0.0
                           :vel 0.0
                           :target {:type :ship
                                    :tribe-id :r
                                    :id :r-1}}))
            t 4200
            universe (auto-navigation initial-universe t)]
        (is (= 1.0 (get-in universe [:tribes :y :ships :y-1 :thrust])))
        (is (= 0.0 (get-in universe [:tribes :y :ships :y-1 :rudder])))
        (is (= true (get-in universe [:tribes :y :ships :y-1 :fbs :primary])))
        ))


    #_(testing "The target is the nearest resource, which is straight ahead, or base target if inv > 16"
        (let [initial-universe
              {:blocks {}
               :grades {:y {:resources {:y-1 {:pos {:x 100.0 :y 0.0}
                                            :amount 4}}}}
             :tribes {:y {:ships {:y-1 (ship {:x 0.0 :y -60.0})
                                  :y-2 (ship {:x 0.0 :y  0.0})
                                  :y-3 (-> (ship {:x 0.0 :y 60.0})
                                           (assoc :target {:type :base
                                                           :tribe-id :y
                                                           :id :y-1}))}
                          :bases {:y-1 {:pos {:x 100.0 :y 0.0}}}}}}
            t 4200
            universe (auto-navigation initial-universe t)]

        (is (= {:thrust 1.0
                :rudder 1.0
                :fire-pressed? false}
               (get-in universe [:tribes :y :ships :y-1 :controls])))
        (is (= {:thrust 1.0
                :rudder 0.0
                :fire-pressed? false}
               (get-in universe [:tribes :y :ships :y-2 :controls])))
        (is (= {:thrust 1.0
                :rudder -1.0
                :fire-pressed? false}
               (get-in universe [:tribes :y :ships :y-3 :controls])))))

    #_(testing "The target is the nearest resource, which can only be reached via a nav path"
      (let [initial-universe
            {:blocks {:bl-1 {:pos {:x 50 :y 100} :size {:w 20 :h 220}}}
             :nav-points {:np-1 {:x 40 :y -40}
                          :np-2 {:x 60 :y -40}}
             :grades
             {:y
              {:resources {:y-1 {:grade :y
                                 :pos {:x 100.0 :y 0.0}
                                 :amount 4}}}}
             :tribes
             {:y
              {:ships {:y-1 (ship {:x -180.0 :y -60.0})
                       :y-2 (ship {:x 0.0 :y   0.0})
                       :y-3 (ship {:x -80.0 :y -40.0})}
               :bases {:y-1 {:tribe-id :y
                             :pos {:x 100.0 :y 0.0}}}}}}
            t 4200
            universe (auto-navigation initial-universe t)]

        (is (= {:thrust 1.0
                :rudder 0.0
                :fire-pressed? false}
               (get-in universe [:tribes :y :ships :y-1 :controls])))
        (is (= {:thrust 1.0
                :rudder -1.0
                :fire-pressed? false}
               (get-in universe [:tribes :y :ships :y-2 :controls])))
        ;; THIS IS DIFFERENT TO RESULT IN ORIGINAL TEST, SO CODE MIGHT BE WRONG
        (is (= {:thrust 1.0
                :rudder 0.0
                :fire-pressed? false}
               (get-in universe [:tribes :y :ships :y-3 :controls])))

        ))

    #_(testing "Should navigate to 'latest' point in nav path with line of sight to target"
      (let [initial-universe
            {:blocks {:bl-1 {:pos {:x 50 :y 100} :size {:w 20 :h 220}}}
             :nav-points {:np-1 {:x -400 :y -40}
                          :np-2 {:x 60 :y -40}}
             :grades {:y {:resources {:y-1 {:pos {:x 100.0 :y 0.0}
                                            :amount 4}}}}
             :tribes {:y {:ships {:y-1 (ship {:x -200.0 :y -40.0})}}}}
            t 4200
            universe (auto-navigation initial-universe t)]
        ;; should navigate to second point in nav path
        (is (= {:thrust 1.0
                :rudder 0.0
                :fire-pressed? false}
               (get-in universe [:tribes :y :ships :y-1 :controls])))))))
