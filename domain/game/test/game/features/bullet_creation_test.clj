(ns game.features.bullet-creation-test
  (:require [clojure.test :refer :all]
            [game.features.bullet-creation :refer :all]))

(deftest bullet-creation-test
  (let [initial-universe
        {:tribes {:y {:ships {:y-1 {:fbs {:primary true
                                          :secondary true}
                                    :pos {:x 200 :y 200}
                                    :rot 0.75
                                    :weapons {:primary {:type :bullet}
                                              :secondary {:type :mine}}}}
                      :bullets {:bullet-2 {:t 4200
                                           :pos {:x 200 :y 200}
                                           :rot 0.75
                                           :vel 360.0
                                           :ship-id :y-1
                                           ;; needed for 'time last bullet fired' per weapon
                                           :weapon-id :primary}
                                :bullet-1 {:t 3600
                                           :pos {:x 200 :y 200}
                                           :rot 0.75
                                           :vel 360.0
                                           :ship-id :y-1
                                           :weapon-id :primary}
                                :mine-1 {:t 4600
                                         :pos {:x 200 :y 200}
                                         :rot 0.75
                                         :vel 0.0
                                         :ship-id :y-1
                                         :weapon-id :secondary}}}}}
        t 5200
        universe (bullet-creation initial-universe t)]
    (is (= 6 (count (get-in universe [:tribes :y :bullets]))))
    (is (some #{{:t 4600
                 :pos {:x 200 :y 200}
                 :rot 0.75
                 :vel 360.0
                 :ship-id :y-1
                 :weapon-id :primary}}
              (vals (get-in universe [:tribes :y :bullets]))))
    (is (some #{{:t 5000
                 :pos {:x 200 :y 200}
                 :rot 0.75
                 :vel 360.0
                 :ship-id :y-1
                 :weapon-id :primary}}
              (vals (get-in universe [:tribes :y :bullets]))))
    (is (some #{{:t 5000
                 :pos {:x 200 :y 200}
                 :rot 0.75
                 :vel 0.0
                 :ship-id :y-1
                 :weapon-id :secondary}}
              (vals (get-in universe [:tribes :y :bullets]))))))

