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
   [worlds.pieces.door :as door]
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
      [:p "Pieces"
       [:div.list-group
        [preview-item "Door" [:pieces :door]]]]]

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

        ;; -----------------------------

        [:pieces :door]
        [pixi.universe/pixi-universe
         {:universe {
                     :t 0
                     ;; :terrain (boxes/boxes-terrain {:t 0})
                     :terrain (door/door-piece)

                     }
          :width width
          :height height
          :camera {:pos {:x 0 :y 240.0}
                   :scale 0.24}}]

        ;; -----------------------------

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

        )]]))

