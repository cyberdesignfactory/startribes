(ns viewer.missions.m-4
  (:require ;; [worlds.circles :as circles]
            [worlds.boxes :as boxes]
            ;; [worlds.rob :as rob]
            ;; [viewer.worlds.simple :as simple]
            ))

(defn long-str [& strings] (clojure.string/join "\n" strings))

(defn mission-4 []
  (let [y-action-fn
        (fn [world tribe-id id]

          (if (pos? (count (get-in world [:tribes :b :ships])))
            ;; if blue ship exists, attack it
            {:title "Attack"
             :description "Attack the blue ship"
             :type :attack-ship
             :tribe-id :b
             :id :b-1}
            ;; otherwise attack the base
            {:title "Attack"
             :description "Attack the blue base"
             :type :attack-base
             :tribe-id :b
             :id :b-1}))

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

            (assoc-in [:tribes :b :bases :b-1] {:role :primary
                                                :pos {:x -900 :y 900}})

            (assoc-in [:tribes :b :ships :b-1] {:pos {:x -600 :y 900}
                                                :weapons {:primary {:type :bullet}
                                                          :secondary {:type :mine}}
                                                :target {:type :ship
                                                         :tribe-id :y
                                                         :id :y-1}}))
        success-fn
        (fn [world]
          (not (get-in world [:tribes :b :bases :b-1])))]

    {
     :title "Destroy an Enemy Base"
     :description
     (long-str "We're going to need you to take a base out this time."
               "Blue have been disrupting our operations."
               "Now is the time to strike as they've only left a few of their fighters to defend the base."
               )

     ;; :requires [:m-3]
     :requires []

     :time-limit 20

     :initial-world initial-world

     :success-fn success-fn

      }

    ))

