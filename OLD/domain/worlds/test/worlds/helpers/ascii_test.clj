(ns worlds.helpers.ascii-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :refer [pprint]]
            [worlds.helpers.ascii :refer :all]))

(deftest from-ascii-test
  (testing "A single block in the centre"
    (let [layout ["---"
                  "-X-"
                  "---"]
          transforms {\X (fn [world xi yi]
                           (let [block-id (str (rand-int 1000000))
                                 block {:pos {:x  (* xi 120) :y (* yi 120)}
                                        :size {:w 120 :h 120}}]
                             (-> world
                                 (assoc-in [:blocks block-id] block))))}
          world (from-ascii layout transforms)]

      (is (= 1 (count (:blocks world))))
      (let [[block-id block] (first (:blocks world))]
        (is (= {:x 0 :y 0} (:pos block)))
        (is (= {:w 120 :h 120} (:size block))))))

  (testing "Blocks top left and centre"
    (let [layout ["X----"
                  "-----"
                  "--X--"
                  "-----"
                  "-----"]
          transforms {\X (fn [world xi yi]
                           (let [block-id (str (rand-int 1000000))
                                 block {:pos {:x  (* xi 120) :y (* yi 120)}
                                        :size {:w 120 :h 120}}]
                             (-> world
                                 (assoc-in [:blocks block-id] block))))}
          world (from-ascii layout transforms)]

      (is (= 2 (count (:blocks world))))
      (let [[block-id block] (first (:blocks world))]
        (is (= {:x -240 :y -240} (:pos block)))
        (is (= {:w 120 :h 120} (:size block))))
      (let [[block-id block] (second (:blocks world))]
        (is (= {:x 0 :y 0} (:pos block)))
        (is (= {:w 120 :h 120} (:size block)))))))


