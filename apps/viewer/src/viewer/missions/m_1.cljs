(ns viewer.missions.m-1
  (:require [worlds.circles :as circles]
            ;; [worlds.boxes :as boxes]
            ;; [worlds.rob :as rob]
            ;; [viewer.worlds.simple :as simple]
            ))

(defn long-str [& strings] (clojure.string/join "\n" strings))

(defn mission-1 []
  (let [y-action-fn
        (fn [world tribe-id id]
          (let [ship (get-in world [:tribes tribe-id :ships id])
                y-inv (get-in ship [:inv :y] 0)]
            (if (>= y-inv 7)
              ;; target nearest primary base
              {:title "Deliver"
               :description "Deliver resource to base"
               :type :visit-base
               :tribe-id tribe-id
               :role :primary}
              ;; target nearest 'y' resource
              {:title "Collect"
               :description
               (str "Collect " (- 7 y-inv) " Y resource")
               :type :collect-resource
               :grade-id :y})))

        y-ship {:pos {:x 600 :y -900}
                :rot 0.5
                :weapons {:primary {:type :bullet}
                          :secondary {:type :mine}}
                :action-fn y-action-fn}

        initial-world (-> (circles/simple-circles-world {:t 0})
                          (assoc-in [:tribes :y :bases :y-1]
                                    {:role :primary
                                     :pos {:x 900 :y -900}})
                          (assoc-in [:tribes :y :ships :y-1] y-ship))

        success-fn (fn [world]
                     (>=
                      (get-in world
                              [:tribes :y :bases :y-1 :inv :y]
                              0)
                      7))]

    {:title "Collect and Deliver"
     :description
     (long-str "Collect and deliver 7 yellow resources to the yellow base."
               "Reconnaissance have reported three peaceful green ships in the area.")
     :requires []
     :time-limit 60
     :initial-world initial-world
     :success-fn success-fn}))


