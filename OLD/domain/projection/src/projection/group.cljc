(ns projection.group
  (:require [projection.circle :as circle]
            [projection.box :as box]))

(defn pos-after-group [pos group-props group-elements]

  (let [!pos (atom pos)]

    (doseq [[type props & inner-elements] group-elements]

      ;; I think we need to adjust the position of the circle/box/group
      ;; before 'applying' it...

      (let [obstacle-pos-with-group {:x (+ (get-in props [:pos :x] 0)
                                           (get-in group-props [:pos :x] 0))
                                     :y (+ (get-in props [:pos :y] 0)
                                           (get-in group-props [:pos :y] 0))}

            new-obstacle-props (-> props
                                   (assoc :pos obstacle-pos-with-group))


            ]
        ;; (println group-props)
        ;; (println group-elements)
        ;; (println obstacle-pos-with-group)
        ;; (println "----------")
        ;; (println @!pos)
        ;; (println new-obstacle-props)
        ;; (println (box/pos-after-box @!pos new-obstacle-props))

        ;; (reset! !pos pos-with-group)

        (case type
          ;; :group (swap! !pos pos-after-group props inner-elements)
          :group (swap! !pos pos-after-group props inner-elements)
          ;; :circle (swap! !pos circle/pos-after-circle props)
          :circle (swap! !pos circle/pos-after-circle new-obstacle-props)
          ;; :box (swap! !pos box/pos-after-box props)
          :box (swap! !pos box/pos-after-box new-obstacle-props)
          )))
    @!pos

    )
  )
