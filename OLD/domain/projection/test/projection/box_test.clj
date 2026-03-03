(ns projection.box-test
  (:require [clojure.test :refer [deftest is testing]]
            [projection.box :as box]))

(deftest pos-after-box-test

  ;; box with 0 :rot
  (let [box {:pos {:x 0 :y 0}
             :rot 0.0
             :size {:w 80 :h 80}}]

    ;; No adjustment needed
    (is (= {:x 240.0 :y 0.0}
           (box/pos-after-box {:x 240.0 :y 0.0} box)))
    (is (= {:x -20.0 :y 200.0}
           (box/pos-after-box {:x -20.0 :y 200.0} box)))
    (is (= {:x 20.0 :y 200.0}
           (box/pos-after-box {:x 20.0 :y 200.0} box)))
    (is (= {:x 200.0 :y -20.0}
           (box/pos-after-box {:x 200.0 :y -20.0} box)))
    (is (= {:x 200.0 :y 20.0}
           (box/pos-after-box {:x 200.0 :y 20.0} box)))



    #_(is (= {:x -20 :y 200}
           (box/pos-after-box {:x -20 :y 190} {:pos {:x -60 :y 120}
                                               :size {:w 120 :h 120}})))

    #_(is (= {:x -20 :y 0}
           (box/pos-after-box {:x 0 :y 0} {:pos {:x 40 :y 0}
                                           :size {:w 120 :h 120}})))





    ;; Horizontally
    (is (= {:x -40.0 :y 0.0}
           (box/pos-after-box {:x -20.0 :y 0.0} box)))

    (is (= {:x 40.0 :y 0.0}
           (box/pos-after-box {:x 20.0 :y 0.0} box)))

    ;; Vertically
    (is (= {:x 0.0 :y -40.0}
           (box/pos-after-box {:x 0.0 :y -20.0} box)))

    (is (= {:x 0.0 :y 40.0}
           (box/pos-after-box {:x 0.0 :y 20.0} box)))














    ;; ;; Diagonally
    ;; (is (= {:x  11.0 :y  11.0}
    ;;        (box/pos-after-box {:x 8 :y 8} box)))

    )

  #_(testing "Box rotated 45 degrees"
    (let [box {:pos {:x 0 :y 0}
               :rot 0.125
               :size {:w 80 :h 80}}]

      ;; Diagonally top-right
      (is (= {:x 0.0 :y 40.0}
             (box/pos-after-box {:x 10.0 :y -10.0} box)))



      ))

  )



