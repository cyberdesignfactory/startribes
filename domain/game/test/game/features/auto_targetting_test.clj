(ns game.features.auto-targetting-test
  (:require [clojure.test :refer :all]
            [game.features.auto-targetting :refer :all]))

(deftest nearest-id-test
  (let [candidates {:y-1 {:pos {:x 300 :y 0}}
                    :y-2 {:pos {:x 200 :y 0}}
                    :y-3 {:pos {:x 400 :y 0}}}]
    (is (= :y-2 (nearest-id {:x 0 :y 0} candidates)))
    (is (= :y-3 (nearest-id {:x 500 :y 0} candidates)))))

(deftest auto-targetting-test

  (testing "Collect 7 resources and deliver to base"

    (let [action-fn
          (fn [world tribe-id id]
            (let [ship (get-in world [:tribes tribe-id :ships id])]
              (if (>= (get-in ship [:inv :y] 0) 7)
                ;; target nearest primary base
                {:type :visit-base
                 :tribe-id tribe-id
                 :role :primary}
                ;; target nearest 'y' resource
                {:type :collect-resource
                 :grade-id :y})))

          world
          {:grades
           {:y {:resources {:y-1 {:pos {:x 20.0 :y 100.0}}}}}
           :tribes
           {:r {:bases {:r-1 {:pos {:x -200.0 :y 0.0}}}
                :ships {:r-1 {:pos {:x 0.0 :y 200.0}}}}
            :y {:bases {:y-1 {:pos {:x 200.0 :y 0.0}
                              :role :primary}}
                :ships {:y-1 {:pos {:x 0.0 :y 0.0}
                              :action-fn action-fn}}}}}
          world-with-inv
          (-> world
              (assoc-in [:tribes :y :ships :y-1 :inv] {:y 7}))]

      (is (= {:type :resource
              :grade-id :y
              :id :y-1}
             (-> (auto-targetting world 4200)
                 (get-in [:tribes :y :ships :y-1 :target]))))

      (is (= {:type :base
              :tribe-id :y
              :id :y-1}
             (-> (auto-targetting world-with-inv 4200)
                 (get-in [:tribes :y :ships :y-1 :target]))))))



  #_(let [initial-world
        {:grades
         {:y {:resources {:y-1 {:pos {:x 20.0 :y 100.0}}}}}
         :tribes
         {:r {:bases {:r-1 {:pos {:x -200.0 :y 0.0}}}
              :ships {:r-1 {:pos {:x 0.0 :y 200.0}}}}
          :y {:bases {:y-1 {:pos {:x 200.0 :y 0.0}}}
              :ships {:y-1 {:pos {:x 0.0 :y 0.0}}}}}}]

    (testing "NEW Collect 7 resources and deliver to base"
      (let [action-fn
            (fn [world tribe-id id]
              (let [ship (get-in world [:tribes tribe-id :ships id])]
                (if (>= (get-in ship [:inv :y]) 7)
                  ;; target nearest primary base
                  {:type :base
                   :tribe-id tribe-id
                   :id (nearest-base-of-role world tribe-id :primary)}
                  ;; target nearest 'y' resource
                  {:type :resource
                   :grade-id :y
                   :id (nearest-resource-of-grade pos world :y)})))


            world-1
            (assoc-in initial-world
                      [:grades :y :resources :y-1]
                      {:y-1 {:pos {:x 20.0 :y 100.0}}})

            world-2
            (assoc-in initial-world
                      [:tribes :y :ships :y-1 :inv :y]
                      7)

            target-1
            (get-in (auto-targetting world-1 4200)
                    [:tribes :y :ships :y-1 :target])

            target-2
            (get-in (auto-targetting world-2 4200)
                    [:tribes :y :ships :y-1 :target])
            ]

        (is (= {:type :resource
                :grade-id :y
                :id :r-1}
               (-> (auto-targetting
                    {:grades
                     {:y {:resources {:y-1 {:pos {:x 20 :y 100}}}}}
                     :tribes
                     {:y {:ships {:y-1 {:pos {:x 0.0 :y 0.0}
                                        :action-fn action-fn}}}}}
                    4200)
                   (get-in [:tribes :y :ships :y-1 :target]))))

        (is (={:type :base
               :tribe-id :y
               :id :y-1}
               (-> (auto-targetting
                    {
                     ;; :grades
                     ;; {:y {:resources {:y-1 {:pos {:x 20 :y 100}}}}}
                     :tribes
                     {:y {
                          :bases {:y-1 {:pos {:x 200.0 :y 0.0}}}
                          :ships {:y-1 {:pos {:x 0.0 :y 0.0}
                                        :inv {:y 7}
                                        :action-fn action-fn}}}}}
                    4200)
                   (get-in [:tribes :y :ships :y-1 :target]))))

        ;; = OR =




        




        ;; = OR =

        (is (= {:type :resource
                :grade-id :y
                :id :r-1}
               (-> (auto-targetting initial-world 4200)
                   (get-in [:tribes :y :ships :y-1 :target]))))

        (is (= {:type :base
                :tribe-id :y
                :id :y-1}
               (-> (auto-targetting
                    (-> initial-world
                        (assoc-in
                         [:tribes :y :ships :y-1 :inv :y]
                         7))
                    4200)
                   (get-in [:tribes :y :ships :y-1 :target]))))

        ;; = OR =

        (is (= {:type :resource
                :grade-id :y
                :id :r-1} target-1))

        (is (= {:type :base
                :tribe-id :y
                :id :y-1} target-2))

        )
      )

    )

  #_(testing "Collect 7 resources and deliver to base"
    (let [action-fn
          (fn [world tribe-id id]
            (let [ship (get-in world [:tribes tribe-id :ships id])]
              (if (>= (get-in ship [:inv :y]) 7)
                ;; target nearest primary base
                {:type :base
                 :tribe-id tribe-id
                 :id (nearest-base-of-role world tribe-id :primary)}
                ;; target nearest 'y' resource
                {:type :resource
                 :grade-id :y
                 :id (nearest-resource-of-grade pos world :y)})))


          ship-without-resources {:action-fn action-fn}

          ship-with-resources {:inv {:y 7}
                               :action-fn action-fn}

          world-without-resources-collected
          {:grades
           {:y {:resources {:y-1 {:pos {:x 20.0 :y 100.0}}}}}
           :tribes
           {:y {:ships {:y-1 {:action-fn action-fn}}}}}

          world-with-resources-collected
          {:grades
           {:y {:resources {:y-1 {:pos {:x 20.0 :y 100.0}}}}}
           :tribes
           {:y {:ships {:y-1 {:inv {:y 7}
                              :action-fn action-fn}}}}}

          world-without-resources-collected' (auto-targetting world 4200)
          world-with-resources-collected' (auto-targetting world 4200)
          ]

      (is (= {:type :ship
              :tribe-id :r
              :id :r-1}
             (get-in (auto-targetting world 4200)
                     [:tribes :y :ships :y-1 :target])))
      )
    )

  #_(testing "Destroy nearest enemy ship"
    (let [action-fn
          (fn [world tribe-id id]
            {:type :attack-ship
             :tribe-id :r})

          world
          {:tribes
           {:r {:ships {:r-1 {}}}
            :y {:ships {:y-1 {:action-fn action-fn}}}}}

          ;; world
          ;; {:tribes
          ;;  {:r {:ships {:r-1 {}}}
          ;;   :y {:ships
          ;;       {:y-1
          ;;        {:action-fn
          ;;         (fn [world tribe-id id]
          ;;           {:type :attack-ship
          ;;            :tribe-id :r})}}}}}

          world' (auto-targetting world 4200)]
      (is (= {:type :ship
              :tribe-id :r
              :id :r-1}
             (get-in world' [:tribes :y :ships :y-1 :target])))))

  (testing "Collect from base and deliver to another base")

  (testing "Destroy ship, collect its resources, deliver to base")

  (testing "Destroy ships, collect resources and deliver to base")

  ;; M4, M7
  (testing "Destroy ships, then destroy base")

  )
