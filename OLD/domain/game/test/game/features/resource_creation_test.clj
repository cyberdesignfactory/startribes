(ns game.features.resource-creation-test
  (:require [clojure.test :refer :all]
            [game.features.resource-creation :refer :all]))

(deftest is-pos-within-terrain-test
  (let [terrain [:group {}
                 [:box {:size {:w 100 :h 100}}]]
        pos {:x 40 :y 0}]
    (is (is-pos-within-terrain? pos terrain))))

(deftest resource-creation-test

  (testing "Resources need to be created"

    (let [initial-world
          {
           :grades
           {
            :r {:role :primary
                :colour 0xff7777}
            :y {:role :primary
                :colour 0xffff77
                :resources {:y-1 {:pos {:x 20.0 :y 20.0} :amount 4}}
                :min 8
                }
            :g {:role :primary
                :colour 0xaaffaa}
            :b {:role :primary
                :colour 0x7777ff}
            :ry {:role :secondary
                 :colour 0xffcc77}
            :yg {:role :secondary
                 :colour 0xaaff77
                 :min 4}
            :gb {:role :secondary
                 :colour 0x77ffaa}
            :br {:role :secondary
                 :colour 0xff77ff}
            }

           }
          t 4200
          world (resource-creation initial-world t)
          ]

      (is (= 0 (apply + (map :amount (vals (get-in world [:grades :r  :resources]))))))
      (is (= 8 (apply + (map :amount (vals (get-in world [:grades :y  :resources]))))))
      (is (= 0 (apply + (map :amount (vals (get-in world [:grades :g  :resources]))))))
      (is (= 0 (apply + (map :amount (vals (get-in world [:grades :b  :resources]))))))
      (is (= 0 (apply + (map :amount (vals (get-in world [:grades :ry :resources]))))))
      (is (= 4 (apply + (map :amount (vals (get-in world [:grades :yg :resources]))))))
      (is (= 0 (apply + (map :amount (vals (get-in world [:grades :gb :resources]))))))
      (is (= 0 (apply + (map :amount (vals (get-in world [:grades :br :resources]))))))

      )
    )

  )
