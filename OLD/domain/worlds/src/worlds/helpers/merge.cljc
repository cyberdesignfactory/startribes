(ns worlds.helpers.merge
  (:require [clojure.pprint :refer [pprint]]))

;; from https://clojuredocs.org/clojure.core/merge#example-5b80849ee4b00ac801ed9e75
(defn deep-merge [v & vs]
  (letfn [(rec-merge [v1 v2]
            (if (and (map? v1) (map? v2))
              (merge-with deep-merge v1 v2)
              v2))]
    (if (some identity vs)
      (reduce #(rec-merge %1 %2) v vs)
      (last vs))))

#_(defn merge-worlds [worlds]
  {
   :title (apply str (map :title worlds))
   :blocks (apply merge (map :blocks worlds))
   :nav-points (apply merge (map :nav-points worlds))
   :grades (apply deep-merge (map :grades worlds))
   :tribes (apply deep-merge (map :tribes worlds))})


(defn merge-worlds [worlds]
  (let [!world (atom (first worlds))]
    (doseq [u (rest worlds)]

      #_(doseq [[block-id block] (:blocks u)]
        (swap! !world assoc-in [:blocks block-id] block))

      (doseq [[nav-point-id nav-point] (:nav-points u)]
        (swap! !world assoc-in [:nav-points nav-point-id] nav-point))

      (doseq [[grade-id grade] (:grades u)]
        (swap! !world update-in [:grades grade-id]
               #(deep-merge % grade)))

      (doseq [[tribe-id tribe] (:tribes u)]
        (swap! !world update-in [:tribes tribe-id]
               #(deep-merge % tribe)))

      (doseq [[circle-id circle] (get-in u [:terrain :circles])]
        (swap! !world assoc-in [:terrain :circles circle-id] circle))

      )

    @!world))


