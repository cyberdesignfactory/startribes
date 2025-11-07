(ns terrains.experimental)

(defn deep-merge [v & vs]
  (letfn [(rec-merge [v1 v2]
            (if (and (map? v1) (map? v2))
              (merge-with deep-merge v1 v2)
              v2))]
    (if (some identity vs)
      (reduce #(rec-merge %1 %2) v vs)
      (last vs))))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn spinny-thing [{:keys [pos
                            radius
                            delta]}]
  {:circles {:c-1
             {:pos {:x 200.0 :y 0.0}
              :radius 60.0
              }}
   :boxes {}})

(defn experimental-terrain [{:keys [t]}]
  (deep-merge
   {:circles
    (into {}
          (for [n (range 8)]
            [(keyword (str n))
             {:pos {:x -200.0 :y (* n 100)}
              :radius 80.0}]))}
   {:circles
    {:c-2
     {:pos {:x 400.0 :y 0.0}
      :radius 60.0
      }
     :c-3
     {:pos {:x 600.0 :y 0.0}
      :radius 60.0
      }}}
   (spinny-thing {:t t})))

