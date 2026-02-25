(ns game.features.destruction-test
  (:require [clojure.test :refer :all]
            [game.features.destruction :refer :all]))

(deftest base-destruction-test
  (let [initial-universe
        {:tribes {:y {:bases {:y-1 {:tribe-id :y
                                    :t 4000
                                    :pos {:x 360.0 :y -200.0}
                                    :rot 0.0
                                    :vel 100.0
                                    :energy 0.0
                                    :inv {:ry 3
                                          :y 6
                                          :yg 3}}

                              :y-2 {:tribe-id :y
                                    :t 4000
                                    :pos {:x 360.0 :y -200.0}
                                    :rot 0.0
                                    :vel 100.0
                                    :energy 0.1
                                    :inv {:ry 2
                                          :y 4
                                          :yg 2}}}}}}
        t 4200
        universe (destruction initial-universe t)]

    (is (some #{{:pos {:x 360.0 :y -200.0}
                 :amount 3}}
              (vals (get-in universe [:grades :ry :resources]))))

    (is (some #{{:pos {:x 360.0 :y -200.0}
                 :amount 6}}
              (vals (get-in universe [:grades :y :resources]))))

    (is (some #{{:pos {:x 360.0 :y -200.0}
                 :amount 3}}
              (vals (get-in universe [:grades :yg :resources]))))

    ))

(deftest ship-destruction-test
  (let [initial-universe
        {:tribes {:y {:ships {:y-1 {:tribe-id :y
                                    :t 4000
                                    :pos {:x 360.0 :y -200.0}
                                    :rot 0.0
                                    :vel 100.0
                                    :energy 0.0
                                    :inv {:ry 3
                                          :y 6
                                          :yg 3}}

                              :y-2 {:tribe-id :y
                                    :t 4000
                                    :pos {:x 360.0 :y -200.0}
                                    :rot 0.0
                                    :vel 100.0
                                    :energy 0.1
                                    :inv {:ry 2
                                          :y 4
                                          :yg 2}}}}}}
        t 4200
        universe (destruction initial-universe t)]

    (is (some #{{:pos {:x 360.0 :y -200.0}
                 :amount 3}}
              (vals (get-in universe [:grades :ry :resources]))))

    (is (some #{{:pos {:x 360.0 :y -200.0}
                 :amount 6}}
              (vals (get-in universe [:grades :y :resources]))))

    (is (some #{{:pos {:x 360.0 :y -200.0}
                 :amount 3}}
              (vals (get-in universe [:grades :yg :resources]))))

    ))
