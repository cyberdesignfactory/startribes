(ns worlds.helpers.translate-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :refer [pprint]]
            [worlds.helpers.translate :refer :all]))

(deftest translate-world-test
  (is (= {
          ;; :blocks {}
          :nav-points {}
          :grades {
                   :r
                   {:colour 0xff3333
                    :resources
                    {:r-1 {
                           :pos {:x 600 :y 200}
                           :amount 4
                           }}}}

          :tribes {
                   :y {
                       :colour 0xffaa33
                       :bases {:y-1 {:pos {:x 600 :y 200}}}
                       :ships {}
                       }
                   }

          :terrain {:circles {

                              }}
          }
         (translate-world
          {:grades {:r {:colour 0xff3333
                        :resources {:r-1 {
                                          :pos {:x 200 :y 400}
                                          :amount 4
                                          }}}}
           :tribes {:y {:colour 0xffaa33
                        :bases {:y-1 {:pos {:x 200 :y 400}}}}}

           :terrain {:circles {}}

           }
          {:x 400 :y -200}))))

