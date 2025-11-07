(ns worlds.helpers.ascii
  #_(:require [clojure.pprint :refer [pprint]]))

(defn from-ascii [layout transforms]
  (let [!world (atom {})]
    (doseq [yi (range (count layout))]
      (let [line (get layout yi)
            yi-offset (/ (dec (count layout)) 2)]
        (doseq [xi (range (count line))]
          (let [c (get line xi)
                xi-offset (/ (dec (count line)) 2)]
            (doseq [[chr transform-fn] transforms]
              ;;(println c chr)
              (when (= c chr)
                (swap! !world
                       #(transform-fn % (- xi xi-offset) (- yi yi-offset)))))))))
    @!world))


