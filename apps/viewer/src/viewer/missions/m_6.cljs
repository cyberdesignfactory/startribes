(ns viewer.missions.m-6
  (:require ;; [worlds.circles :as circles]
            [worlds.boxes :as boxes]
            ;; [worlds.rob :as rob]
            ;; [viewer.worlds.simple :as simple]
            ))

(defn long-str [& strings] (clojure.string/join "\n" strings))

(defn mission-6 []
  (let [y-action-fn
        (fn [world tribe-id id]
          (let [ship (get-in world [:tribes tribe-id :ships id])]

            (cond
              ;; if green ship exists, attack it
              (get-in world [:tribes :g :ships :g-1])
              {:title "Attack"
               :description "Destroy the green ship"
               :type :attack-ship
               :tribe-id :g
               :id :g-1}

              ;; otherwise collect yg resources
              (< (get-in ship [:inv :yg] 0) 20)
              {:title "Collect"
               :description "Collect the YG resource"
               :type :collect-resource
               :grade-id :yg}

              ;; then go to the base
              :else
              {:title "Deliver"
               :description "Deliver to the secondary base"
               :type :visit-base
               :tribe-id tribe-id
               :role :secondary-2})))

        y-ship {:pos {:x 600 :y -900}
                :rot 0.5
                :weapons {:primary {:type :bullet}
                          :secondary {:type :mine}}
                :action-fn y-action-fn}

        initial-world
        (-> (boxes/simple-boxes-world {:t 0})

            (assoc-in [:tribes :y :bases :y-2]
                      {:role :secondary-2
                       :pos {:x 900 :y -900}})

            (assoc-in [:tribes :y :ships :y-1] y-ship)

            (assoc-in [:tribes :g :bases :g-2]
                      {:role :secondary-1
                       :pos {:x 900 :y 900}})

            (assoc-in [:tribes :g :ships :g-1]
                      {:pos {:x -600 :y -900}
                       :weapons {:primary {:type :bullet}
                                 :secondary {:type :mine}}
                       :inv {:yg 20}
                       :target {:type :base
                                :tribe-id :g
                                :id :g-2}}))

        success-fn
        (fn [world]
          (>= (get-in world [:tribes :y :bases :y-2 :inv :yg] 0)
              20))

        failure-fn (fn [world]
                      (>= (get-in world [:tribes :g :bases :g-2 :inv :yg] 0)
                          20))]

    {
     :title "Loot Resources (from Green)"
     :description
     (long-str "Green are carrying our vitally needed YG resource."
               "We'll need to 'borrow' it from them, before they deliver it to their base."
               "You know what to do...  Please deliver it to the yellow base.")

     ;; :requires [:m-5]
     :requires []
     :time-limit 20
     :accomplished? false
     :best-time nil

     :initial-world initial-world

     :success-fn success-fn
     :failure-fn failure-fn

     }

    ))

