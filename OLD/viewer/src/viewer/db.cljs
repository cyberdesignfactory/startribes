(ns viewer.db
  (:require [worlds.circles :as circles]
            [worlds.boxes :as boxes]
            [worlds.rob :as rob]
            ;; [viewer.worlds.simple :as simple]
            [viewer.missions.m-1 :as m-1]
            [viewer.missions.m-2 :as m-2]
            [viewer.missions.m-3 :as m-3]
            [viewer.missions.m-4 :as m-4]
            [viewer.missions.m-5 :as m-5]
            [viewer.missions.m-6 :as m-6]
            [viewer.missions.m-7 :as m-7]))

(defn long-str [& strings] (clojure.string/join "\n" strings))

(def default-db
  {:name "re-frame"

   :ui {
        :panel-id :intro
        }

   :orientation {:alpha 0.7}

   :ship-thrust 0.0
   :ship-rudder 0.0
   :ship-strafe 0.0

   :ship-fbs {:primary false
              :secondary false}

   ;; "Campaign One"
   :missions
   {:m-1
    (-> (m-1/mission-1)
        (assoc :requires [])
        (assoc :accomplished? false)
        (assoc :best-time nil))

    :m-2
    (-> (m-2/mission-2)
        (assoc :requires [:m-1])
        (assoc :accomplished? false)
        (assoc :best-time nil))

    :m-3
    (-> (m-3/mission-3)
        (assoc :requires [:m-2])
        (assoc :accomplished? false)
        (assoc :best-time nil))

    :m-4
    (-> (m-4/mission-4)
        (assoc :requires [:m-3])
        (assoc :accomplished? false)
        (assoc :best-time nil))

    :m-5
    (-> (m-5/mission-5)
        (assoc :requires [:m-4])
        (assoc :accomplished? false)
        (assoc :best-time nil))

    :m-6
    (-> (m-6/mission-6)
        (assoc :requires [:m-5])
        (assoc :accomplished? false)
        (assoc :best-time nil))

    :m-7
    (-> (m-7/mission-7)
        (assoc :requires [:m-6])
        (assoc :accomplished? false)
        (assoc :best-time nil))
    }
   })

