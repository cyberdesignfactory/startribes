(ns game.features.auto-trading-test
  (:require [clojure.test :refer :all]
            [game.features.auto-trading :refer :all]))

(defn ship [x y]
  {:controls {:thrust 0.1
              :rudder 0.1}
   :pos {:x x :y y}
        :rot 0.0

   ;; :fs {:t 4000
   ;;      :pos {:x x :y y}
   ;;      :rot 0.0
   ;;      :vel 0.0
   ;;      :ang-vel 0.0}

   })

(deftest auto-trading-test
  (testing "Auto Trading - Ship delivers primary resource type to primary base"
    (let [initial-universe
          {:tribes {:y {:colour 0xffaa33
                        :production {:primary :y
                                     :secondary-1 :ry
                                     :secondary-2 :yg
                                     :surplus-1 :r
                                     :surplus-2 :g}
                        :bases {:y-1 {:role :primary
                                      :pos {:x 80.0 :y 80.0}
                                      :inv {:y 36}}}
                        :ships {:y-1 (assoc-in (ship 80.0 80.0) [:inv :y] 16)}
                        }}}
          t 4200
          universe (auto-trading initial-universe t)]

      (is (= 0 (get-in universe [:tribes :y :ships :y-1 :inv :y])))
      (is (= 52 (get-in universe [:tribes :y :bases :y-1 :inv :y])))))

  (testing "Auto Trading - Ship collects primary resource from secondary base"
    (let [initial-universe
          {:tribes {:y {:colour 0xffaa33
                        :production {:primary :y
                                     :secondary-1 :ry
                                     :secondary-2 :yg
                                     :surplus-1 :r
                                     :surplus-2 :g}

                        :bases {:y-1 {:tribe-id :y
                                      :role :secondary-1
                                      :pos {:x 80.0 :y 80.0}
                                      :inv {
                                            :y 36
                                            }}}

                        :ships {:y-1 (assoc-in (ship 80.0 80.0) [:inv :ry] 0)}

                        }}}
          t 4200
          universe (auto-trading initial-universe t)]

      (is (= 16 (get-in universe [:tribes :y :ships :y-1 :inv :y])))
      (is (= 20 (get-in universe [:tribes :y :bases :y-1 :inv :y])))))

  (testing "Auto Trading - Ship collects primary resource from other tribe's secondary base"
    (let [initial-universe
          {:tribes {:r {:colour 0xff3333
                        :production {:primary :r
                                     :secondary-1 :br
                                     :secondary-2 :ry
                                     :surplus-1 :b
                                     :surplus-2 :y}
                        :bases {:r-1 {:role :secondary-2
                                      :pos {:x 80.0 :y 80.0}
                                      :inv {
                                            :y 36
                                            }}}}
                    :y {:colour 0xffaa33
                        :production {:primary :y
                                     :secondary-1 :ry
                                     :secondary-2 :yg
                                     :surplus-1 :r
                                     :surplus-2 :g}
                        :ships {:y-1 (assoc-in (ship 80.0 80.0) [:inv :ry] 0)}
                        }}}
          t 4200
          universe (auto-trading initial-universe t)]

      (is (= 16 (get-in universe [:tribes :y :ships :y-1 :inv :y])))
      (is (= 20 (get-in universe [:tribes :r :bases :r-1 :inv :y])))))

  (testing "Auto Trading - Ship delivers secondary-1 resource to secondary base"
    (let [initial-universe
          {:tribes {:y {:colour 0xffaa33
                        :production {:primary :y
                                     :secondary-1 :ry
                                     :secondary-2 :yg
                                     :surplus-1 :r
                                     :surplus-2 :g}
                        :bases {:y-1 {:role :secondary-1
                                      :pos {:x 80.0 :y 80.0}
                                      :inv {:ry 36}}}
                        :ships {:y-1 (assoc-in (ship 80.0 80.0) [:inv :ry] 16)}}}}
          t 4200
          universe (auto-trading initial-universe t)]

      (is (= 0 (get-in universe [:tribes :y :ships :y-1 :inv :ry])))
      (is (= 52 (get-in universe [:tribes :y :bases :y-1 :inv :ry])))))

  (testing "Auto Trading - Ship delivers secondary-2 resource to secondary base"
    (let [initial-universe
          {:tribes {:y {:colour 0xffaa33
                        :production {:primary :y
                                     :secondary-1 :ry
                                     :secondary-2 :yg
                                     :surplus-1 :r
                                     :surplus-2 :g}
                        :bases {:y-1 {:role :secondary-2
                                      :pos {:x 80.0 :y 80.0}
                                      :inv {:yg 36}}}
                        :ships {:y-1 (assoc-in (ship 80.0 80.0) [:inv :yg] 16)}}}}
          t 4200
          universe (auto-trading initial-universe t)]

      (is (= 0 (get-in universe [:tribes :y :ships :y-1 :inv :yg])))
      (is (= 52 (get-in universe [:tribes :y :bases :y-1 :inv :yg]))))))
