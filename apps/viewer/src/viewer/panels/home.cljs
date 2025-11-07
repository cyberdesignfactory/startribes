(ns viewer.panels.home
  (:require
   [re-frame.core :as re-frame]
   [viewer.events :as events]
   [viewer.subs :as subs]
   ;; [viewer.panels.shared :as panels.shared]
   [components.ui.header :as header]
   ))



(defn mission-disabled? [id missions]
  (let [mission (get missions id)
        !disabled (atom false)]
    (doseq [rm-id (:requires mission)]
      (when (not (:accomplished? (get missions rm-id)))
        (reset! !disabled true)))
    @!disabled))


(defn home-panel []
  (let [missions @(re-frame/subscribe [::subs/missions])
        ;; probably a better way to do this...
        latest-enabled-mission-id
        (let [!latest-id (atom nil)]
          (doseq [[id mission] missions]
            (if (not (mission-disabled? id missions))
              (reset! !latest-id id)))
          @!latest-id)
        fbs @(re-frame/subscribe [::subs/ship-fbs])]
    [:div
     ;; [panels.shared/header]
     [header/header]
     [:div.container
      [:div.card.bg-primary-subtle.p-2.m-3.opacity-75
       [:div.card-header.pb-1.text-center
        [:h4 "Missions" #_"Campaign One"]]
       [:div.card-body.pb-1.text-center
        [:div.text-center
         (for [[id mission] missions]

           (let [disabled? (mission-disabled? id missions)]

             [:div.mb-3
              {:key id}

              [:div.card.p-2.m-3
               {:id (if (= id latest-enabled-mission-id) "latest" nil)
                :class (if disabled? "bg-primary-subtle" "bg-light")
                :style {:scroll-margin-top "36px"}}
               [:div.card-header.pb-1.text-center
                [:h5 (:title mission)]]
               [:div.card-body.pb-1.text-center
                [:p (:description mission)]
                [:p (str "Time Limit: " (:time-limit mission))]
                (if (:best-time mission)
                  [:p (str "Best time: " (int (:best-time mission)))])
                #_[:p (str "Requires: " (:required mission))]

                #_(let [el (.getElementById js/document "latest")]
                  (when el
                    (.scrollIntoView el)))

                (if disabled?
                  [:div
                   [:p "(Mission not yet available)"]]

                  [:button.btn.btn-secondary
                   {
                    :on-click #(re-frame/dispatch [::events/mission-selected id])
                    ;; :on-click #(re-frame/dispatch [::events/mission-accomplished id])
                    ;; :on-click #(re-frame/dispatch [::events/mission-failed id])

                    ;; COMMENT OUT THIS BLOCK TO ALLOW ALL MISSIONS FOR NOW
                    ;; :disabled disabled?

                    }
                   "Launch"])

                ]]]))]]]]

     ])

  )


