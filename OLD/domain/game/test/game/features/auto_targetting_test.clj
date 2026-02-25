(ns game.features.auto-targetting-test
  (:require [clojure.test :refer :all]
            [game.features.auto-targetting :refer :all]))

(defn ship [tribe-id x y]
  {:tribe-id tribe-id
   :controls {:thrust 0.1
              :rudder 0.1}
   :auto-targetting? true

   :pos {:x x :y y}
   :rot 0.0

   ;; :fs {:t 4000
   ;;      :pos {:x x :y y}
   ;;      :rot 0.0
   ;;      :vel 0.0
   ;;      :ang-vel 0.0}

   })

(deftest auto-targetting-test
  (let [test-world
        {:tribes {:r {:strategy :aggressive
                      :production {:primary :r
                                   :secondary-1 :br
                                   :secondary-2 :ry
                                   :surplus-1 :b
                                   :surplus-2 :y}}
                  :y {:colour 0xffaa33
                      :strategy :retaliatory
                      :production {:primary :y
                                   :secondary-1 :ry
                                   :secondary-2 :yg
                                   :surplus-1 :r
                                   :surplus-2 :g}
                      :bases {:y-1 {:role :secondary-1
                                    :pos {:x 200 :y 200}}
                              :y-2 {:role :primary
                                    :pos {:x 200 :y 100}}
                              :y-3 {:role :primary
                                    :pos {:x 200 :y 160}}}}}}
        t 4200]

    (testing "Targets nearest relevant base when ship inventory full"
      (let [initial-world (-> test-world
                              (assoc-in [:tribes :y :ships :y-1]
                                        (-> (ship :y 0.0 0.0)
                                            (assoc :inv {:y 16}))))
            world (auto-targetting initial-world t)]
        (is (= {:type :base
                :tribe-id :y
                :id :y-2}
               (get-in world [:tribes :y :ships :y-1 :target])))))

    (testing "Targets a secondary base to collect (as base has >= ship's capacity of primary resource)"
      (let [initial-world (-> test-world
                              (assoc-in [:tribes :y :bases :y-secondary-1]
                                        {:role :secondary-1
                                         :pos {:x 80.0 :y 80.0}
                                         :inv {:y 42}})
                              (assoc-in [:tribes :y :ships :y-1]
                                        (-> (ship :y 0.0 0.0)
                                            (assoc :inv {:y 0}))))
            world (auto-targetting initial-world t)]
        (is (= {:type :base
                :tribe-id :y
                :id :y-secondary-1}
               (get-in world [:tribes :y :ships :y-1 :target])))))

    (testing "Targets another tribe's secondary base to collect (as base has >= ship's capacity of primary resource)"
      (let [initial-world (-> test-world
                              (assoc-in [:tribes :r :bases :r-secondary-2]
                                        {:role :secondary-2
                                         :pos {:x 80.0 :y 80.0}
                                         :inv {:y 42}})
                              (assoc-in [:tribes :y :ships :y-1]
                                        (-> (ship :y 0.0 0.0)
                                            (assoc :inv {:y 0}))))
            world (auto-targetting initial-world t)]
        (is (= {:type :base
                :tribe-id :r
                :id :r-secondary-2}
               (get-in world [:tribes :y :ships :y-1 :target])))))

    (testing "Targets a secondary-1 base to deliver (as ship at full capacity of secondary-1 resource)"
      (let [initial-world (-> test-world
                              (assoc-in [:tribes :y :bases :y-secondary-1]
                                        {:role :secondary-1
                                         :pos {:x 80.0 :y 80.0}})
                              (assoc-in [:tribes :y :ships :y-1]
                                        (-> (ship :y 0.0 0.0)
                                            (assoc :inv {:ry 16}))))
            world (auto-targetting initial-world t)]
        (is (= {:type :base
                :tribe-id :y
                :id :y-secondary-1}
               (get-in world [:tribes :y :ships :y-1 :target])))))

    (testing "Targets a secondary-2 base to deliver (as ship at full capacity of secondary-2 resource)"
      (let [initial-world (-> test-world
                              (assoc-in [:tribes :y :bases :y-secondary-1]
                                        {:role :secondary-1
                                         :pos {:x 40.0 :y 80.0}})
                              (assoc-in [:tribes :y :bases :y-secondary-2]
                                        {:role :secondary-2
                                         :pos {:x 80.0 :y 80.0}})
                              (assoc-in [:tribes :y :ships :y-1]
                                        (-> (ship :y 0.0 0.0)
                                            (assoc :inv {:ry 0
                                                         :yg 16}))))
            world (auto-targetting initial-world t)]
        (is (= {:type :base
                :tribe-id :y
                :id :y-secondary-2}
               (get-in world [:tribes :y :ships :y-1 :target])))))

    (testing "Targets nearest resource"
      (let [initial-world (-> test-world
                              (assoc-in [:grades :y :resources]
                                        {:y-1 {:grade-id :y
                                               :pos {:x -40.0 :y 40.0}
                                               :amount 4}
                                         :y-2 {:grade-id :y
                                               :pos {:x 0.0 :y 40.0}
                                               :amount 4}
                                         :y-3 {:grade-id :y
                                               :pos {:x 40.0 :y 40.0}
                                               :amount 4}})
                              (assoc-in [:tribes :y :ships :y-1] (ship :y 0.0 0.0)))
            world (auto-targetting initial-world t)]
        (is (= {:type :resource
                :grade-id :y
                :id :y-2}
               (get-in world [:tribes :y :ships :y-1 :target])))))

    (testing "Targets an enemy ship (as aggressive strategy and ship has resources)"
      (let [initial-world (-> test-world
                              (assoc-in [:grades :y :resources :y-1]
                                        {:pos {:x -40.0 :y 40.0}
                                         :amount 4})
                              (assoc-in [:tribes :r :ships :r-1]
                                        (ship :r 0.0 0.0))
                              (assoc-in [:tribes :y :ships :y-1]
                                        (-> (ship :y 100.0 0.0)
                                            (assoc :inv {:ry 14}))))
            t 4200
            world (auto-targetting initial-world t)]
        (is (= {:type :ship
                :tribe-id :y
                :id :y-1}
               (get-in world [:tribes :r :ships :r-1 :target])))
        (is (= {:type :resource
                :grade-id :y
                :id :y-1}
               (get-in world [:tribes :y :ships :y-1 :target])))))))


#_(deftest aggressive-targets-resource
    (testing "Targets a resource (as aggressive strategy but no ship has wanted resources)"
      (let [world {
                   :tribes {:r {:colour 0xffff33
                                :strategy :aggressive
                                :production {:primary :r
                                             :secondary-1 :br
                                             :secondary-2 :ry
                                             :surplus-1 :b
                                             :surplus-2 :y}}
                            :y {:colour 0xffaa33
                                :strategy :aggressive
                                :production {:primary :y
                                             :secondary-1 :ry
                                             :secondary-2 :yg
                                             :surplus-1 :r
                                             :surplus-2 :g}}}
                   :resources {:r-1 {:grade-id :r
                                     :pos {:x -40.0 :y 40.0}
                                     :amount 4}
                               :y-1 {:grade-id :y
                                     :pos {:x -40.0 :y 40.0}
                                     :amount 4}}
                   :ships {:r-1 (-> (ship :r 0.0 0.0))
                           :y-1 (-> (ship :y 100.0 0.0)
                                    (assoc :inv {:yg 12}))}}
            t 4200
            events (auto-targetting world t)]
        (is (= [[[:ships :r-1 :target] {:type :resource
                                        :id :r-1}]
                [[:ships :y-1 :target] {:type :resource
                                        :id :y-1}]]
               events))))

    )

#_(deftest retaliatory-targets-resource
    (testing "Yellow targets a red ship (as retaliatory strategy and red has low standing)"
      (let [world {:tribes {:r {:colour 0xffff33
                                :strategy :aggressive
                                :production {:primary :r
                                             :secondary-1 :br
                                             :secondary-2 :ry
                                             :surplus-1 :b
                                             :surplus-2 :y}}
                            :y {:colour 0xffaa33
                                :strategy :retaliatory
                                :standings {:r -0.6}
                                :production {:primary :y
                                             :secondary-1 :ry
                                             :secondary-2 :yg
                                             :surplus-1 :r
                                             :surplus-2 :g}}}
                   :resources {:r-1 {:grade-id :r
                                     :pos {:x -40.0 :y 40.0}
                                     :amount 4}
                               :y-1 {:grade-id :y
                                     :pos {:x -40.0 :y 40.0}
                                     :amount 4}}
                   :ships {:r-1 (-> (ship :r 0.0 0.0))
                           :y-1 (-> (ship :y 100.0 0.0)
                                    (assoc :inv {:yg 12}))}}
            t 4200
            events (auto-targetting world t)]
        (is (= [[[:ships :r-1 :target] {:type :resource
                                        :id :r-1}]
                [[:ships :y-1 :target] {:type :ship
                                        :id :r-1}]]
               events)))))

