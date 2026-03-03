(ns projection.group-test
  (:require [clojure.test :refer [deftest is testing]]
            [projection.group :as group]))

(deftest pos-after-group-test

  (let [group-element
        [:group {}
         [:group {}
          [:group {}
           [:group {:pos {:x 0 :y 200}}
            [:box {:pos {:x 40 :y 0}
                   :size {:w 120 :h 120}}]
            #_[:box {:pos {:x  200 :y 0}
                     :size {:h 180 :w 120}}]]]]]

         ;; group ???
        group-props (get group-element 1)
        group-elements (rest (rest group-element))
        ]

    (is (= {:x 0 :y 0}
           (group/pos-after-group {:x 0 :y 0} group-props group-elements)))

    (is (= {:x -20 :y 200}
           (group/pos-after-group {:x 0 :y 200} group-props group-elements)))

    )

  ;; box with 0 :rot
  #_(let [box {:pos {:x 0 :y 0}
             :rot 0.0
             :size {:w 80 :h 80}}]


    ))



