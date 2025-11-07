(ns projection.circle-test
  (:require [clojure.test :refer [deftest is]]
            [projection.circle :as circle]))

(deftest pos-after-circle-test

  (let [cs {:pos {:x 0 :y 0}
            :radius 16}]

    ;; No adjustment needed
    (is (= {:x -24.0 :y   0.0}
           (circle/pos-after-circle {:x -24.0 :y 0.0} cs)))

    ;; Horizontally
    (is (= {:x -16.0 :y   0.0}
           (circle/pos-after-circle {:x -8 :y 0} cs)))

    (is (= {:x  16.0 :y   0.0}
           (circle/pos-after-circle {:x 8 :y 0} cs)))

    ;; Vertically
    (is (= {:x   0.0 :y -16.0}
           (circle/pos-after-circle {:x 0 :y -8} cs)))

    (is (= {:x   0.0 :y  16.0}
           (circle/pos-after-circle {:x 0 :y 8} cs)))

    ;; Diagonally
    (is (= {:x  11.0 :y  11.0}
           (circle/pos-after-circle {:x 8 :y 8} cs)))

    ))


