(ns viewer.missions.m-7
  (:require ;; [worlds.circles :as circles]
            [worlds.boxes :as boxes]
            ;; [worlds.rob :as rob]
            ;; [viewer.worlds.simple :as simple]
            ))

(defn long-str [& strings] (clojure.string/join "\n" strings))

(defn mission-7 []
  (let [y-action-fn
        (fn [world tribe-id id]
          (let [ship (get-in world [:tribes tribe-id :ships id])]

            (cond
              ;; if reds ship exists, attack them
              (pos? (count (get-in world [:tribes :r :ships])))
              {:title "Attack"
               :description "Attack red ship"
               :type :attack-ship
               :tribe-id :r}

              ;; otherwise attack the red base
              :else
              {:title "Attack"
               :description "Attack the red base"
               :type :attack-base
               :tribe-id :r
               :id :r-1})))

        y-ship {:pos {:x 600 :y -900}
                :rot 0.5
                :weapons {:primary {:type :bullet}
                          :secondary {:type :mine}}
                :action-fn y-action-fn}

        initial-world
        (-> (boxes/simple-boxes-world {:t 0})
            (assoc-in [:tribes :y :bases :y-1]
                      {:role :primary
                       :pos {:x 900 :y -900}
                       :weapons {:primary {:type :bullet}
                                 :secondary {:type :mine}}
                       })

            (assoc-in [:tribes :y :ships :y-1] y-ship)

            (assoc-in [:tribes :y :ships :y-2]
                      {:pos {:x 800 :y -900}
                       :rot 0.25
                       :weapons {:primary {:type :bullet}
                                 :secondary {:type :mine}}
                       :action-fn y-action-fn

                       ;; :target {:type :ship
                       ;;          :tribe-id :r
                       ;;          :id :r-1}

                       })
            (assoc-in [:tribes :y :ships :y-3]
                      {:pos {:x 1000 :y -900}
                       :rot 0.25
                       :weapons {:primary {:type :bullet}
                                 :secondary {:type :mine}}
                       :action-fn y-action-fn

                       ;; :target {:type :ship
                       ;;          :tribe-id :r
                       ;;          :id :r-2}

                       })

            (assoc-in [:tribes :r :bases :r-1]
                      {:role :primary
                       :pos {:x -900 :y 900}})

            (assoc-in [:tribes :r :ships :r-1]
                      {:pos {:x -600 :y 900}
                       :weapons {:primary {:type :bullet}
                                 :secondary {:type :mine}}
                       :action-fn
                       (fn [world tribe-id id]
                         {:title "Attack"
                          :description "Attack yellow ship"
                          :type :attack-ship
                          :tribe-id :y})
                       ;; :target {:type :ship
                       ;;          :tribe-id :y
                       ;;          :id :y-1}

                       })
            (assoc-in [:tribes :r :ships :r-2]
                      {:pos {:x -1600 :y 900}
                       :weapons {:primary {:type :bullet}
                                 :secondary {:type :mine}}
                       :action-fn
                       (fn [world tribe-id id]
                         {:title "Attack"
                          :description "Attack yellow ship"
                          :type :attack-ship
                          :tribe-id :y})
                       ;; :target {:type :ship
                       ;;          :tribe-id :y
                       ;;          :id :y-1}

                       })
            (assoc-in [:tribes :r :ships :r-3]
                      {:pos {:x -2400 :y 900}
                       :weapons {:primary {:type :bullet}
                                 :secondary {:type :mine}}
                       :action-fn
                       (fn [world tribe-id id]
                         {:title "Attack"
                          :description "Attack yellow ship"
                          :type :attack-ship
                          :tribe-id :y})
                       ;; :target {:type :ship
                       ;;          :tribe-id :y
                       ;;          :id :y-1}

                       })
            )

        success-fn
        (fn [world]
          (not (get-in world [:tribes :r :bases :r-1])))

        failure-fn (fn [world]
                      (>= (get-in world [:tribes :g :bases :g-2 :inv :yg] 0)
                          20))]

    {
     :title "Destroy (Red) Primary Base"
     :description
     (long-str "Okay, this is the big one."
               "We are going to take out Red's primary base, removing this aggressive tribe from the game."
               "Lunatic and Maelstrom will deal with the fighters - you take out the base.")
     :time-limit 60
     :initial-world initial-world

     :success-fn success-fn
     :failure-fn failure-fn

     }
    ))
