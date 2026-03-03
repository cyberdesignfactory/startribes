(ns worlds.helpers.scale-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :refer [pprint]]
            [worlds.helpers.scale :refer :all]))

(deftest scale-world-test
  (is (= {
          ;; :blocks {}

          :nav-points {}
          :grades {}

          :tribes {:y {:colour 0xffaa33
                       :bases {:y-1 {:pos {:x 600 :y 200}}}
                       :ships {}}}

          ;; :terrain {:circles {}}


          }
         (scale-world {
                       :tribes {:y {:colour 0xffaa33
                                    :bases {:y-1 {:pos {:x -600 :y -200}}}}}}
                      {:x -1 :y -1}))))

