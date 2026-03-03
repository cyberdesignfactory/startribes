(ns viewer.missions.m-2
  (:require ;; [worlds.circles :as circles]
            [worlds.boxes :as boxes]
            ;; [worlds.rob :as rob]
            ;; [viewer.worlds.simple :as simple]
            ))

(defn long-str [& strings] (clojure.string/join "\n" strings))

(defn mission-2 []
  (let [y-action-fn
        (fn [world tribe-id id]
          (let [ship (get-in world [:tribes tribe-id :ships id])]
            {:title "Attack"
             :description "Attack the red ship"
             :type :attack-ship
             :tribe-id :r
             :id :r-1}))

        y-ship {:pos {:x 600 :y -900}
                :rot 0.5
                :weapons {:primary {:type :bullet}
                          :secondary {:type :mine}}
                :action-fn y-action-fn}

        initial-world
        (-> (boxes/simple-boxes-world {:t 0})

            (assoc-in [:tribes :y :bases :y-1]
                      {:role :primary
                       :pos {:x 900 :y -900}})

            (assoc-in [:tribes :y :ships :y-1] y-ship)

            ;; we want the red ship to target the yellow base
            (assoc-in [:tribes :r :ships :r-1]
                      {:pos {:x -600 :y 900}
                       :weapons {:primary {:type :bullet}
                                 :secondary {:type :mine}}
                       :target {:type :base
                                :tribe-id :y
                                :id :y-1}}))

        success-fn (fn [world]
                      (not (get-in world [:tribes :r :ships :r-1])))

        failure-fn (fn [world]
                      ;; yellow base is destroyed (ie non-existent)
                      (not (get-in world [:tribes :y :bases :y-1])))]
    {:title "Destroy a Red Fighter"
     :description
     (long-str "An enemy fighter is incoming."
               "Please take it out before it can hit the base")
     :time-limit 40
     :initial-world initial-world
     :success-fn success-fn
     :failure-fn failure-fn}))

