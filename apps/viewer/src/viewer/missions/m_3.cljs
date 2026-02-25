(ns viewer.missions.m-3
  (:require ;; [worlds.circles :as circles]
            ;; [worlds.boxes :as boxes]
            [worlds.rob :as rob]
            ;; [viewer.worlds.simple :as simple]
            ))

(defn long-str [& strings] (clojure.string/join "\n" strings))

(defn mission-3 []
  (let [y-action-fn
        (fn [world tribe-id id]
          (let [ship (get-in world [:tribes tribe-id :ships id])]
            (if (>= (get-in ship [:inv :y] 0) 20)
              {:title "Deliver"
               :description "Deliver to primary base"
               :type :visit-base
               :tribe-id :y
               :role :primary}
              {:title "Collect"
               :description "Collect from secondary base"
               :type :visit-base
               :tribe-id :y
               :role :secondary-1})))

        y-ship {:pos {:x 600 :y -900}
                :rot 0.5
                :weapons {:primary {:type :bullet}
                          :secondary {:type :mine}}
                :action-fn y-action-fn}

        initial-world
        (-> (rob/simple-rob-world {:t 0})

            (assoc-in [:tribes :y :bases :y-1]
                      {:role :primary
                       :pos {:x 900 :y -900}})

            (assoc-in [:tribes :y :bases :y-2]
                      {:role :secondary-1
                       :pos {:x 900 :y 900}
                       :inv {:y 20}})

            (assoc-in [:tribes :y :ships :y-1] y-ship)
            ;; two red fighters here
            )

        success-fn
        (fn [world]
          (>= (get-in world [:tribes :y :bases :y-1 :inv :y] 0)
              20))]

    {:title "Transport from Base to Base"
     :description
     (long-str
      "Please transport 20 yellow from a secondary base to a primary base."
      "Reconnaissance have reported a light enemy presence.")
               ;; two red fighters
     :time-limit 20
     :initial-world initial-world
     :success-fn success-fn}

    ))

