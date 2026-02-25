(ns viewer.missions.m-5
  (:require ;; [worlds.circles :as circles]
            [worlds.boxes :as boxes]
            ;; [worlds.rob :as rob]
            ;; [viewer.worlds.simple :as simple]
            ))

(defn long-str [& strings] (clojure.string/join "\n" strings))

(defn mission-5 []
  (let [y-action-fn
        (fn [world tribe-id id]
          (let [ship (get-in world [:tribes tribe-id :ships id])
                y-inv (get-in ship [:inv :y] 0)]

            (cond
              ;; if enemy ships, attack them
              (pos? (count (get-in world [:tribes :r :ships])))
              {:title "Attack"
               :description "Attack red ship"
               :type :attack-ship
               :tribe-id :r}

              ;; if < 10 resources, collect them
              (< y-inv 10)
              {:title "Collect"
               :description
               (str "Collect " (- 10 y-inv) " Y resource")
               :type :collect-resource
               :grade-id :y}

              ;; otherwise go to the base
              :else
              {:title "Deliver"
               :description "Deliver to primary base"
               :type :visit-base
               :tribe-id tribe-id
               :role :primary})))

        y-ship {:pos {:x 600 :y -900}
                :rot 0.5
                :weapons {:primary {:type :bullet}
                          :secondary {:type :mine}}
                :action-fn y-action-fn}

        initial-world
        (-> (boxes/simple-boxes-world {:t 0})

         (assoc-in [:tribes :y :bases :y-1] {:role :primary
                                             :pos {:x 900 :y -900}})

         (assoc-in [:tribes :y :ships :y-1] y-ship)

         (assoc-in [:tribes :r :ships :r-1]
                   {:pos {:x -2400 :y 900}
                    :weapons {:primary {:type :bullet}
                              :secondary {:type :mine}}
                    :target {:type :ship
                             :tribe-id :y
                             :id :y-1}})
         (assoc-in [:tribes :r :ships :r-2]
                   {:pos {:x -1600 :y 900}
                    :weapons {:primary {:type :bullet}
                              :secondary {:type :mine}}
                    :target {:type :ship
                             :tribe-id :y
                             :id :y-1}})
         )

        success-fn
        (fn [world]
          (>= (get-in world [:tribes :y :bases :y-1 :inv :y] 0)
              10))]

    {:title "Gather Resources Under Fire"
     :description
     (long-str
      "We desperately need RY for our operations."
      "However there are hostile Red ships around where it can be collected."
      "The Green and Blue tribes are peaceful, so try not to engage with them unless they attack."
      "We need ten units or production stops, and time is running short."
      )
     ;; :requires [:m-4]
     :requires []

     :time-limit 60
     :initial-world initial-world

     :success-fn success-fn

      }

    ))

