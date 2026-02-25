(ns worlds.helpers.merge-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :refer [pprint]]
            [worlds.helpers.merge :refer :all]))


(deftest merge-worlds-test
  (testing "Merge two worlds (left, right)"
    (is (= {
            :title "Test World"
            ;; :blocks nil
            ;; :nav-points nil
            :grades {
                     :ry {:role :secondary
                          :colour 0xffcc77
                          :min 4}
                     :yg {:role :secondary
                          :colour 0xaaff77
                          :min 4
                          :resources {:yg-1 {:pos {:x 200 :y 200}
                                             :amount 4}}}
                     }
            :tribes {:r {:colour 0xff7777
                         :bases {:r-1  {:pos {:x -760 :y -160}}
                                 :r-2  {:pos {:x -760 :y -160}}}}
                     :y {:colour 0xffaa33
                         :bases {:y-1 {:pos {:x  760 :y -160}}
                                 :y-2 {:pos {:x  760 :y -160}}}}}

            ;; :terrain {:circles {}}

            }
           (merge-worlds [
                          {
                           :title "Test World"
                           :grades {:ry {:role :secondary
                                         :colour 0xffcc77
                                         :min 4}}
                           :tribes {:r {:colour 0xff7777
                                        :bases {:r-1 {:pos {:x -760 :y -160}}}}
                                    :y {:colour 0xffaa33
                                        :bases {:y-1 {:pos {:x  760 :y -160}}}}}}
                          {:grades {:yg {:role :secondary
                                         :colour 0xaaff77
                                         :min 4
                                         :resources {:yg-1 {:pos {:x 200 :y 200}
                                                            :amount 4}}}}}
                          {:tribes {:r {:colour 0xff7777
                                        :bases {:r-2 {:pos {:x -760 :y -160}}}}
                                    :y {:colour 0xffaa33
                                        :bases {:y-2 {:pos {:x  760 :y -160}}}}}}

                          ;; {:terrain {:circles {}}}


                             ])))))

