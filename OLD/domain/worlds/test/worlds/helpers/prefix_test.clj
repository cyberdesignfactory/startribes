(ns worlds.helpers.prefix-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :refer [pprint]]
            [worlds.helpers.prefix :refer :all]))

(deftest prefix-world-keys-test
  (is (= {
          ;; :blocks {:left-b-1  {:pos {:x -760 :y -160}}
          ;;          :left-b-2 {:pos {:x  760 :y -160}}}

          :nav-points {}
          :tribes {:r {:colour 0xff7777
                       :bases {:left-r-1  {:pos {:x -760 :y -160}}
                               :left-y-1 {:pos {:x  760 :y -160}}}
                       :ships {:left-r-1  {:pos {:x -760 :y -160}}
                               :left-y-1 {:pos {:x  760 :y -160}}}}}
          :grades {:r {:colour 0xff7777
                       :resources {:left-r-1  {:pos {:x -760 :y -160}}
                                   :left-y-1 {:pos {:x  760 :y -160}}}}}

          :terrain {:circles {}}}

         (prefix-world-keys {
                                ;; :blocks {:b-1 {:pos {:x -760 :y -160}}
                                ;;          :b-2 {:pos {:x  760 :y -160}}}
                                :grades {:r {:colour 0xff7777
                                             :resources {:r-1 {:pos {:x -760 :y -160}}
                                                         :y-1 {:pos {:x  760 :y -160}}}}}
                                :tribes {:r {:colour 0xff7777
                                             :bases {:r-1 {:pos {:x -760 :y -160}}
                                                     :y-1 {:pos {:x  760 :y -160}}}
                                             :ships {:r-1 {:pos {:x -760 :y -160}}
                                                     :y-1 {:pos {:x  760 :y -160}}}}}}
                               :left))))

