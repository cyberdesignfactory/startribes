(ns viewer.db
  (:require [worlds.circles :as circles]
            [worlds.boxes :as boxes]
            [worlds.rob :as rob]
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

   :calibration {:min-alpha 1000
                 :max-alpha -1000
                 :min-beta 34
                 :max-beta 66
                 :min-gamma -25
                 :max-gamma 25
                 }
   :orientation {:alpha 0.7}

   :ship-thrust 0.0
   :ship-rudder 0.0
   :ship-strafe 0.0

   :ship-fbs {:primary false
              :secondary false}

   :campaign-id :c-1
   :campaigns
   {
    :c-1
    {
     :title "Campaign One"
     :previous-campaign-id nil
     :next-campaign-id :c-2
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

     }
    :c-2
    {
     :title "Campaign Two"
     :previous-campaign-id :c-1
     :next-campaign-id nil
     :missions
     {:m-1
      (-> (m-1/mission-1)
          (assoc :requires [])
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

     }
    }

   })

