(ns worlds.rob
  (:require
   [worlds.shared :as shared]
   [worlds.helpers.translate :as translate]
   [worlds.helpers.merge :as merge]
   [worlds.helpers.prefix :as prefix]
   [worlds.helpers.repeat :as repeat]))

(defn simple-ship []
  {:pos {:x 900 :y -900}
   :rot 0.25
   :controls {:thrust 0.0
              :rudder 0.0}
   ;; ship type?
   :type :destroyer
   :class :heavy
   :weapons {
             :neutron-cannon {:megatons 16}
             }
   ;;:inv {:yg 200}
   })

(defn simple-terrain [{:keys [t]}]

  #_(into
   [:group {:pos {:x 100 :y 0}}

    ]

   [
    ;; problem occurs (can go through boxes) as soon as we add this:
    [:group {}


     [:box {:pos {:x (+ 180 (* 360 0))
                  :y (+ 180 (* 360 0))}
            :size {:w 120 :h 120}}]]

    ]

   )

  [:group {:pos {:x 100 :y 0}}

   ;; problem occurs (can go through boxes) as soon as we add this:
   [:group {}

    [:box {:pos {:x (+ 180 (* 360 0))
                 :y (+ 180 (* 360 0))}
           :size {:w 120 :h 120}}]]
   ]

  #_(into [:group {:pos {:x (+ -180 (* -3 360))
                       :y (* -3 360)}}]

        [
         [:group {}

          [:box {:pos {:x (+ 180 (* 360 0))
                       :y (+ 180 (* 360 0))}
                 :size {:w 120 :h 120}}]

          ]]


        #_(for [ix (range 7)
              iy (range 7)]

          #_[:group {}]
          [:box {:pos {:x (+ 180 (* 360 ix))
                       :y (+ 180 (* 360 iy))}
                 :size {:w 120 :h 120}}]

          #_[:group {:pos {:x 0 :y 0}}
           [:circle {:pos {:x (* 360 ix)
                           :y (* 360 iy)}
                     :radius 60}]
           [:box {:pos {:x (+ 180 (* 360 ix))
                        :y (+ 180 (* 360 iy))}
                  :size {:w 120 :h 120}}]])))

(defn boxes-terrain [{:keys [t]}]
  (into [:group {:pos {:x (* -3 240)
                       :y (* -3 240)}}]
        (for [ix (range 7)
              iy (range 7)]
          [:box {:pos {:x (* 240 ix)
                       :y (* 240 iy)}
                 :size {:w 120 :h 120}}])))
(defn simple-rob-world [{:keys [t]}]
  (let [instances {:nw {:pos {:x -600 :y -600}
                        :scale {:x  1 :y  1}}
                   :ne {:pos {:x  600 :y -600}
                        :scale {:x  1 :y  1}}
                   :sw {:pos {:x -600 :y  600}
                        :scale {:x  1 :y  1}}
                   :se {:pos {:x  600 :y  600}
                        :scale {:x  1 :y  1}}}]
    (merge/merge-worlds
     [{:t t
       :grades (shared/simple-grades)
       :tribes (-> (shared/simple-tribes)
                   #_(assoc-in [:y :ships :y-1] (simple-ship)))
       :terrain (simple-terrain {:t t})
       ;; :terrain (boxes-terrain {:t t})

       }])))

