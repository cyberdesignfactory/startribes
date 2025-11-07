(ns components.ui.menu
  (:require [reagent.core :as r]))

(defn menu [{:keys [items selected-id on-selected]}]
  (fn [{:keys [items selected-id on-selected]}]
    [:div.container.pt-4.text-center
     [:ul.nav.nav-tabs.text-center
      (for [[id item] items]
        [:li.nav-item {:key id}
         #_[:pre (str item)]
         [:a.nav-link {:style {:cursor :pointer}
                       :class
                       (if (= id selected-id)
                         "active"
                         "text-secondary")
                       :on-click (fn [e]
                                   (on-selected id)
                                   (.preventDefault e))
                       }
          (:title item)]])]

     [:div
      (for [[id item] items]
        (if (= id selected-id)
          [:div
           {:key id}
           (:view item)]))]]))

