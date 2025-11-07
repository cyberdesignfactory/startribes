(ns components.viewport
  (:require
   ;; ["@react-spring/web" :refer [useSpring animated]]
   ["@use-gesture/react" :refer [useDrag usePinch useGesture]]))

(defn viewport [{:keys [x y s on-scroll on-zoom]} content]
  (let [bind (useGesture
              (clj->js
               {:onDrag
                (fn [data]
                  (let [{:keys [down movement offset]}
                        (js->clj data
                                 {:keywordize-keys true})
                        [mx my] movement
                        [ox oy] offset
                        ]
                    (on-scroll ox oy mx my data)))
                :onPinch
                (fn [data]
                  (let [{:keys [da origin offset]}
                        (js->clj data
                                 {:keywordize-keys true})
                        [d a] da
                        [os oa] offset
                        ]
                    (on-zoom os)))}))]
    [:div (js->clj (bind))
     [:div
      {:style {:touch-action "none"}} content]]))

