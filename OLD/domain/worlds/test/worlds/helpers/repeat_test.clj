(ns worlds.helpers.repeat-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :refer [pprint]]
            [worlds.helpers.repeat :refer :all]))

(deftest repeat-world-test
  (testing "Mirrored horizontally and vertically"
    (let [initial-world {:title ""
                         :nav-points {:np-1 {:x -100 :y 0}
                                      :np-2 {:x  100 :y 0}}
                         :tribes {:r {:bases {:r-1 {:pos {:x  300 :y  300}}}}}

                         :terrain {:circles {:c-1 {:pos {:x 200 :y 200}}}}
                         }
          instances {:nw {:pos {:x -600 :y -600}
                          :scale {:x  1 :y  1}}
                     :ne {:pos {:x  600 :y -600}
                          :scale {:x  1 :y  1}}
                     :sw {:pos {:x -600 :y  600}
                          :scale {:x  1 :y  1}}
                     :se {:pos {:x  600 :y  600}
                          :scale {:x  1 :y  1}}}
          world (repeat-world initial-world instances)]
      (is (= {
              :title ""
              ;; :blocks {}
              :nav-points {
                           :nw-np-1 {:x -700 :y -600}
                           :nw-np-2  {:x -500 :y -600}
                           :ne-np-1 {:x  500 :y -600}
                           :ne-np-2  {:x  700 :y -600}
                           :sw-np-1 {:x -700 :y  600}
                           :sw-np-2  {:x -500 :y  600}
                           :se-np-1 {:x  500 :y  600}
                           :se-np-2  {:x  700 :y  600}
                           }
              :grades {}
              :tribes {:r {
                           :bases {
                                   :nw-r-1 {:pos {:x -300 :y -300}}
                                   :ne-r-1 {:pos {:x  900 :y -300}}
                                   :sw-r-1 {:pos {:x -300 :y  900}}
                                   :se-r-1 {:pos {:x  900 :y  900}}
                                   }
                           :ships {}}}
              :terrain {:circles {:nw-c-1 {:pos {:x -400 :y -400}}
                                  :ne-c-1 {:pos {:x  800 :y -400}}
                                  :sw-c-1 {:pos {:x -400 :y  800}}
                                  :se-c-1 {:pos {:x  800 :y  800}}}}
              }
             world))
      )))

