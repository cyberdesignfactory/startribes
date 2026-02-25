(ns viewer.db
  (:require [worlds.circles :as circles]
            [worlds.boxes :as boxes]
            [worlds.rob :as rob]
            [viewer.worlds.simple :as simple]))


;; (defn campaign-one []
;;   {
;;    :missions {:m-1 {
;;                     }}
;;    }
;;   )

(defn long-str [& strings] (clojure.string/join "\n" strings))

(defn simple-ship []
  {:pos {:x 600 :y -900}
   ;; :rot 0.25
   :rot 0.0

   :weapons {:primary {:type :bullet}
             :secondary {:type :mine}}

   :auto-targetting? true
   ;; :thrust 0.0
   ;; :rudder 0.0
   ;; :strafe 0.0

   ;; :controls {:thrust 0.0
   ;;            :rudder 0.0}

   ;; ;; ship type?
   ;; :type :destroyer
   ;; :class :heavy
   ;; :weapons {:neutron-cannon {:megatons 16}}

   })

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
    {:title "Collect and Deliver"
     :description
     (long-str "Collect and deliver 7 yellow resources to the yellow base."
               "Reconnaissance have reported three peaceful green ships in the area.")
     :requires []
     :time-limit 60
     :accomplished? false
     :best-time nil
     :initial-world
     (-> (circles/simple-circles-world {:t 0})

         (assoc-in [:tribes :y :bases :y-1]
                   {:role :primary
                    :pos {:x 900 :y -900}})

         (assoc-in [:tribes :y :ships :y-1] (simple-ship)))

     :success-fn (fn [world]
                   (>= (get-in world [:tribes :y :bases :y-1 :inv :y] 0)
                       7))

     :failure-fn (fn [world]
                   false ;; FOR NOW
                   )}

    :m-2
    {
     :title "Destroy a Red Fighter"
     :description
     (long-str "An enemy fighter is incoming."
               "Please take it out before it can hit the base")
     ;; we want the red ship to target the yellow base

     :requires [:m-1]
     :time-limit 40
     :accomplished? false
     :best-time nil

     :initial-world
     (-> (boxes/simple-boxes-world {:t 0})

         (assoc-in [:tribes :y :bases :y-1]
                   {:role :primary
                    :pos {:x 900 :y -900}})

         (assoc-in [:tribes :y :ships :y-1] (simple-ship))

         (assoc-in [:tribes :r :ships :r-1]
                   {:pos {:x -600 :y 900}
                    :weapons {:primary {:type :bullet}
                              :secondary {:type :mine}}
                    :target {:type :base
                             :tribe-id :y
                             :id :y-1}}))

     :success-fn (fn [world]
                   (not (get-in world [:tribes :r :ships :r-1])))

     :failure-fn (fn [world]
                   ;; false ;; FOR NOW
                   ;; should be that yellow base is destroyed (ie non-existent)
                   (not (get-in world [:tribes :y :bases :y-1]))
                   )
     }


    :m-3
    {
     :title "Transport from Base to Base"
     :description
     (long-str "Please transport 20 yellow from a secondary base to a primary base."
               "Reconnaissance have reported a light enemy presence.")
               ;; two red fighters
     :requires [:m-2]
     :time-limit 20
     :accomplished? false
     :best-time nil
     :initial-world
     (-> (rob/simple-rob-world {:t 0})

         (assoc-in [:tribes :y :bases :y-1]
                   {:role :primary
                    :pos {:x 900 :y -900}})

         (assoc-in [:tribes :y :bases :y-2]
                   {:role :secondary-1
                    :pos {:x 900 :y 900}
                    :inv {:y 20}})

         (assoc-in [:tribes :y :ships :y-1] (simple-ship))
         ;; two red fighters here
         )

     :success-fn (fn [world]
                   (>= (get-in world [:tribes :y :bases :y-1 :inv :y] 0)
                       20))

     :failure-fn (fn [world]
                   false ;; FOR NOW
                   )
     }


    :m-4
    {
     :title "Destroy an Enemy Base"
     :description
     (long-str "We're going to need you to take a base out this time."
               "Blue have been disrupting our operations."
               "Now is the time to strike as they've only left a few of their fighters to defend the base."
               )

     :requires [:m-3]
     :time-limit 20
     :accomplished? false
     :best-time nil

     :initial-world
     (-> (boxes/simple-boxes-world {:t 0})

         (assoc-in [:tribes :y :bases :y-1] {:role :primary
                                             :pos {:x 900 :y -900}})

         (assoc-in [:tribes :y :ships :y-1] (simple-ship))

         (assoc-in [:tribes :b :bases :b-1] {:role :primary
                                             :pos {:x -900 :y 900}})

         (assoc-in [:tribes :b :ships :b-1] {:pos {:x -600 :y 900}
                                             :weapons {:primary {:type :bullet}
                                                       :secondary {:type :mine}}
                                             :target {:type :ship
                                                      :tribe-id :y
                                                      :id :y-1}})
         )

     :success-fn (fn [world]
                   (not (get-in world [:tribes :b :bases :b-1])))

     :failure-fn (fn [world]
                   false ;; FOR NOW
                   )
      }

    :m-5
    {
     :title "Gather Resources Under Fire"
     :description
     (long-str
      "We desperately need RY for our operations."
      "However there are hostile Red ships around where it can be collected."
      "The Green and Blue tribes are peaceful, so try not to engage with them unless they attack."
      "We need ten units or production stops, and time is running short."
      )
     :requires [:m-4]
     :time-limit 60
     :accomplished? false
     :best-time nil

     :initial-world
     (-> (boxes/simple-boxes-world {:t 0})

         (assoc-in [:tribes :y :bases :y-1] {:role :primary
                                             :pos {:x 900 :y -900}})

         (assoc-in [:tribes :y :ships :y-1] (simple-ship))

         (assoc-in [:tribes :r :ships :r-1] {:pos {:x -2400 :y 900}
                                             :weapons {:primary {:type :bullet}
                                                       :secondary {:type :mine}}
                                             :target {:type :ship
                                                      :tribe-id :y
                                                      :id :y-1}})
         (assoc-in [:tribes :r :ships :r-2] {:pos {:x -1600 :y 900}
                                             :weapons {:primary {:type :bullet}
                                                       :secondary {:type :mine}}
                                             :target {:type :ship
                                                      :tribe-id :y
                                                      :id :y-1}})
         )

     :success-fn (fn [world]
                   (>= (get-in world [:tribes :y :bases :y-1 :inv :y] 0)
                       10))

     :failure-fn (fn [world]
                   false ;; FOR NOW
                   )
      }

    :m-6
    {
     :title "Loot Resources (from Green)"
     :description
     (long-str "Green are carrying our vitally needed YG resource."
               "We'll need to 'borrow' it from them, before they deliver it to their base."
               "You know what to do...  Please deliver it to the yellow base.")

     :requires [:m-5]
     :time-limit 20
     :accomplished? false
     :best-time nil

     :initial-world
     (-> (boxes/simple-boxes-world {:t 0})

         (assoc-in [:tribes :y :bases :y-2] {:role :secondary-2
                                             :pos {:x 900 :y -900}})

         (assoc-in [:tribes :y :ships :y-1] (simple-ship))

         (assoc-in [:tribes :g :bases :g-2] {:role :secondary-1
                                             :pos {:x 900 :y 900}})

         (assoc-in [:tribes :g :ships :g-1] {:pos {:x -600 :y -900}
                                             :weapons {:primary {:type :bullet}
                                                       :secondary {:type :mine}}
                                             :inv {:yg 20}
                                             :target {:type :base
                                                      :tribe-id :g
                                                      :id :g-2}})
         )

     :success-fn (fn [world]
                   (>= (get-in world [:tribes :y :bases :y-2 :inv :yg] 0)
                       20))

     :failure-fn (fn [world]
                   (>= (get-in world [:tribes :g :bases :g-2 :inv :yg] 0)
                       20))
     }

    :m-7
    {
     :title "Destroy (Red) Primary Base"
     :description
     (long-str "Okay, this is the big one."
               "We are going to take out Red's primary base, removing this aggressive tribe from the game."
               "Lunatic and Maelstrom will deal with the fighters - you take out the base.")
     :requires [:m-6]
     :time-limit 36
     :accomplished? false
     :best-time nil

     :initial-world
     (-> (boxes/simple-boxes-world {:t 0})

         (assoc-in [:tribes :y :bases :y-1] {:role :primary
                                             :pos {:x 900 :y -900}
                                             :weapons {:primary {:type :bullet}
                                                       :secondary {:type :mine}}
                                             })

         (assoc-in [:tribes :y :ships :y-1] {:pos {:x 600 :y -900}
                                             :weapons {:primary {:type :bullet}
                                                       :secondary {:type :mine}}
                                             :rot 0.25})
         (assoc-in [:tribes :y :ships :y-2] {:pos {:x 800 :y -900}
                                             :rot 0.25
                                             :weapons {:primary {:type :bullet}
                                                       :secondary {:type :mine}}
                                             :target {:type :ship
                                                      :tribe-id :r
                                                      :id :r-1}})
         (assoc-in [:tribes :y :ships :y-3] {:pos {:x 1000 :y -900}
                                             :rot 0.25
                                             :weapons {:primary {:type :bullet}
                                                       :secondary {:type :mine}}
                                             :target {:type :ship
                                                      :tribe-id :r
                                                      :id :r-2}})

         (assoc-in [:tribes :r :bases :r-1] {:role :primary
                                             :pos {:x -900 :y 900}})

         (assoc-in [:tribes :r :ships :r-1] {:pos {:x -600 :y 900}
                                             :weapons {:primary {:type :bullet}
                                                       :secondary {:type :mine}}
                                             :target {:type :ship
                                                      :tribe-id :y
                                                      :id :y-1}})
         (assoc-in [:tribes :r :ships :r-2] {:pos {:x -1600 :y 900}
                                             :weapons {:primary {:type :bullet}
                                                       :secondary {:type :mine}}
                                             :target {:type :ship
                                                      :tribe-id :y
                                                      :id :y-1}})
         (assoc-in [:tribes :r :ships :r-3] {:pos {:x -2400 :y 900}
                                             :weapons {:primary {:type :bullet}
                                                       :secondary {:type :mine}}
                                             :target {:type :ship
                                                      :tribe-id :y
                                                      :id :y-1}})
         )

     :success-fn (fn [world]
                   (not (get-in world [:tribes :r :bases :r-1])))

     :failure-fn (fn [world]
                   false ;; FOR NOW
                   )

     }
    }
   })


#_"
We desperately need Iron Ore for our operations.

However there are hostile ships around where it can be collected.

We need twenty units or production stops, and time is running short.

"

#_"
Please escort the Titanium Falcon from Zeta Base to Alpha Base.

She is carrying new recruits ready to be trained.

We are expecting hostiles - please get our soldiers here safely.

Longshot and Maelstrom will accompany you.

"

#_"
Take Out A Transporter
--
We cannot let them bring fresh troops in.

We need you to take out their fighter escort - that should deter their transporter ship and make it turn back.

You'll have Lunatic with you.

If the transporter doesn't turn back, you might have to take further action.
"

