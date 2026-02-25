(ns previewer.views
  (:require
   [re-frame.core :as re-frame]
   [components.pixi.universe :as pixi.universe]
   [components.ui.header :as header]
   [components.ui.menu :as menu]
   [worlds.rob :as rob]
   [worlds.circles :as circles]
   [worlds.boxes :as boxes]
   [worlds.shared :as shared]
   [previewer.subs :as subs]
   [previewer.events :as events]
   [previewer.previews.ships.yellow-fighter :as yellow-fighter]
   ))

(defn preview-item [title path]
  [:div.list-group-item.bg-primary-subtle
   [:button.btn
    {:on-click
     #(re-frame/dispatch
       [::events/preview-selected path])}
    title]])

(defn sidebar []
  [:div.z-1.bg-primary.subtle
   {:style {:position :fixed
            :left "0px"
            :top "96px"}}
   [:div.text-start.mx-2.p-2
    [:div.list-group
     [:div.list-group-item.bg-primary-subtle
      [:p "Worlds"
       [:div.list-group
        [preview-item "Circles" [:worlds :circles]]
        [preview-item "Boxes" [:worlds :boxes]]
        [preview-item "Rob" [:worlds :rob]]]]]

     [:div.list-group-item.bg-primary-subtle
      [:p "Ships"
       [:div.list-group
        [preview-item "Fighter" [:ships :fighter]]
        [preview-item "Destroyer" [:ships :destroyer]]
        [preview-item "Transporter" [:ships :transporter]]]]]]]])

(defn main-panel []
  (let [!ui (re-frame/subscribe [::subs/ui])
        !world (re-frame/subscribe [::subs/world])
        width (- (.-innerWidth js/window) 0)  ;; was 16
        height (- (.-innerHeight js/window) 8)  ;; was 16 or 24
        scale 0.36]
    [:div
     (when (get-in @!ui [:sidebar-open?])
       [sidebar])
     [header/header {:style
                     {:position :absolute
                      :top 0
                      :opacity 0.8
                      :width "100%"}
                     :show-menu-button? true
                     :on-menu-clicked #(re-frame/dispatch [::events/menu-clicked])}]
     [:div {:style {:margin-top 96}}

      (case (get-in @!ui [:selected-path])

        [:worlds :circles]
        [pixi.universe/pixi-universe
         {:universe (circles/simple-circles-world {:t 0})
          :width width
          :height height
          :camera {:pos {:x 0 :y 240.0}
                   :scale 0.24}}]

        [:worlds :boxes]
        [pixi.universe/pixi-universe
         {:universe (boxes/simple-boxes-world {:t 0})
          :width width
          :height height
          :camera {:pos {:x 0 :y 240.0}
                   :scale 0.24}}]

        [:worlds :rob]
        [pixi.universe/pixi-universe
         {:universe (rob/simple-rob-world {:t 0})
          :width width
          :height height
          :camera {:pos {:x 0 :y 240.0}
                   :scale 0.24}}]

        [:ships :fighter]
        [pixi.universe/pixi-universe
         {:universe {:t 0
                     :tribes (-> (shared/simple-tribes)
                                 (assoc-in [:y :ships :y-1]
                                           {

                                            }))

                     }
          :player-thrust -1.0
          :player-rudder 0.0
          :player-strafe 0.0
          :width width
          :height height
          :camera {:pos {:x 0 :y 240.0}
                   :scale 0.24}}]


        [:div]

        )





      #_[pixi.universe/pixi-universe
       {
        ;; :universe (yellow-fighter/yellow-fighter-preview)
        :universe (rob/simple-rob-world {:t 0})
        ;; :universe (circles/simple-circles-world {:t 0})
        ;; :universe (boxes/simple-boxes-world {:t 0})
        ;; universe
        #_{:terrain

         [:group {}
          (into [:group {}]
                (for [n (range 6)]
                  [:group {:pos {:x 0 :y (* n 200)}}
                   [:box {:pos {:x -200 :y 0}
                          :size {:w 120 :h 120}}]
                   [:box {:pos {:x  200 :y 0}
                          :size {:h 180 :w 120}}]]))]
         }

        ;; :width 600
        ;; :height 600
        :width width
        :height height
        ;; :camera {:scale 1.0}
        :camera {:pos {:x 0.0
                       :y 240.0}
                 :rot 0.25
                 :scale 0.24}}]]]

    #_[:div
     [header/header {:position :absolute
                     :top 0
                     :opacity 0.8
                     :width "100%"}]
     [:div {:style {:margin-top 96}}
      [menu/menu {:items {:worlds {:title "Worlds"}
                          :terrain {:title "Terrain"}
                          :tribe {:title "Tribe"}
                          :grade {:title "Grade"}
                          ;; :doors {:title "Doors"}
                          ;; :tribes {:title "Tribes"}
                          }
                  :selected-id (:type-id @!ui)
                  :on-selected (fn [id] (re-frame/dispatch [::events/update-ui [:type-id] id]))}]]

     (case (:type-id @!ui)

       :worlds
       [:div

        ]

       :terrain
       [:div
        [menu/menu {:items {:doors {:title "Doors"}
                            :bridges {:title "Bridges"}
                            :roads {:title "Roads"}}

                    }]


        #_[menu/menu {:items {:circles {:title "Circles"}
                            :boxes {:title "Boxes"}
                            :experimental {:title "Experimental"}}
                    :selected-id (:terrain-id @!ui)
                    :on-selected (fn [id] (re-frame/dispatch [::events/update-ui [:terrain-id] id]))}]]

       :tribes
       [:div
        [:h2 "TRIBES"]]

       )

     [pixi.universe/pixi-universe
      {:universe @!world #_{
                  ;; :terrain {
                  ;;           :boxes {:bx-1 {:pos {:x 0.0, :y 0.0}
                  ;;                          :size {:w 80.0 :h 80.0}}}}

                  ;; :terrain (circles/circles-terrain {:t t})
                  ;; :terrain (boxes/boxes-terrain {:t t})
                  ;; :terrain (experimental/experimental-terrain {:t t})
                  ;; :terrain @!terrain
                  :terrain (:terrain @!world)
                  :tribes (:tribes @!world)
                  }
       ;; :width 600
       :height 600
       :width width
       ;; :height height

       }
      ]

     #_[:div
      [:div.d-lg-none
       #_[:h4.text-light "Mobile"]
       {:style {:margin-top 96}}

       [menu/menu
        {:items {
                 :t-1 {:title "Terrain One"}
                 :t-2 {:title "Terrain Two"}
                 :t-3 {:title "Terrain Three"}
                 }
         :selected-id :t-2
         :on-selected (fn [id] (js/alert (str id)))}]

       [pixi.universe/pixi-universe
        {:universe {
                    ;; :terrain {
                    ;;           :boxes {:bx-1 {:pos {:x 0.0, :y 0.0}
                    ;;                          :size {:w 80.0 :h 80.0}}}}

                    ;; :terrain (circles/circles-terrain {:t t})
                    ;; :terrain (boxes/boxes-terrain {:t t})
                    ;; :terrain (experimental/experimental-terrain {:t t})
                    ;; :terrain @!terrain
                    :terrain (:terrain @!world)
                    :tribes (:tribes @!world)
                    }
         ;; :width 600
         ;; :height 600
         :width width
         :height height

         }
        ]

       ]
      [:div.d-none.d-lg-block
       #_[:h4.text-light "Desktop"]

       {:style {:margin-top 96}}

       [menu/menu
        {:items {
                 :t-1 {:title "Terrain One"}
                 :t-2 {:title "Terrain Two"}
                 :t-3 {:title "Terrain Three"}
                 }
         :selected-id :t-2
         :on-selected (fn [id] #_(js/alert (str id)))}]

       ]]


     #_[pixi.universe/pixi-universe
      {:universe {
                  ;; :terrain {
                  ;;           :boxes {:bx-1 {:pos {:x 0.0, :y 0.0}
                  ;;                          :size {:w 80.0 :h 80.0}}}}

                  ;; :terrain (circles/circles-terrain {:t t})
                  ;; :terrain (boxes/boxes-terrain {:t t})
                  ;; :terrain (experimental/experimental-terrain {:t t})
                  ;; :terrain @!terrain
                  :terrain (:terrain @!world)
                  :tribes (:tribes @!world)
                  }
       ;; :width 600
       ;; :height 600
       :width width
       :height height

       }
      ]
     ]
    ))

