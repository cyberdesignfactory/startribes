(ns viewer.panels.home
  (:require
   [re-frame.core :as re-frame]
   [viewer.events :as events]
   [viewer.subs :as subs]
   [components.ui.header :as header]))

(defn mission-disabled? [id missions]
  (let [mission (get missions id)
        !disabled (atom false)]
    (doseq [rm-id (:requires mission)]
      (when (not (:accomplished? (get missions rm-id)))
        (reset! !disabled true)))
    @!disabled))

(defn home-panel []
  (let [campaign-details @(re-frame/subscribe [::subs/campaign-details])
        missions @(re-frame/subscribe [::subs/missions])
        ;; probably a better way to do this...
        latest-enabled-mission-id
        (let [!latest-id (atom nil)]
          (doseq [[id mission] missions]
            (if (not (mission-disabled? id missions))
              (reset! !latest-id id)))
          @!latest-id)
        fbs @(re-frame/subscribe [::subs/ship-fbs])]
    [:div
     [header/header]
     [:div.container
      [:div.card.bg-primary-subtle.p-2.m-3.opacity-75.pt-1
       [:div.card-header.pb-1.text-center
        [:div.row
         [:div.col-2
          #_[:i.mt-0.pt-0.bi.bi-arrow-left-square-fill.fs-1.fs-large.text-primary
           {:on-click
            #(when (:previous-campaign-id campaign-details)
               (re-frame/dispatch
                [::events/campaign-clicked
                 (:previous-campaign-id campaign-details)]))}]]
         [:div.col-8.pt-1
          [:h4 (:title campaign-details)]]
         [:div.col-2
          #_[:i.mt-0.bi.bi-arrow-right-square-fill.fs-1.fs-large.text-primary
           {:on-click
            #(when (:next-campaign-id campaign-details)
               (re-frame/dispatch
                [::events/campaign-clicked
                 (:next-campaign-id campaign-details)]))}]]]]
       [:div.card-body.pb-1.text-center
        [:div.text-center

         (when (.-matches (.matchMedia js/window "(any-pointer: coarse)"))
           [:div.mt-0
            [:button.btn.btn-link.text-primary
             {:on-click #(re-frame/dispatch [::events/calibrate-clicked])}
             "Calibrate controls"]])

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
                (if disabled?
                  [:div
                   [:p "(Mission not yet available)"]]

                  [:button.btn.btn-secondary
                   {:on-click #(re-frame/dispatch [::events/mission-selected id])}
                   "Launch"])]]]))]]]]]))


