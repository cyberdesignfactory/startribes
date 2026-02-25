(ns game.features.resource-collection-test
  (:require [clojure.test :refer :all]
            [game.features.resource-collection :refer :all]))

(defn ship [tribe-id x y]
  {:tribe-id tribe-id
   :controls {:thrust 0.1
              :rudder 0.1}

   ;; :fs {:t 4000
   ;;      :pos {:x x :y y}
   ;;      :rot 0.0
   ;;      :vel 0.0
   ;;      :ang-vel 0.0}

   :pos {:x x :y y}
   :rot 0.0

   :target {:type :resource
            :id :y-1}})

(deftest resource-collection-test

  (testing "Resource collected"

    (let [initial-universe
          {:grades {:y {:resources {:y-1 {:pos {:x 80.0 :y 80.0}
                                          :amount 4}}}}
           :tribes {:y {:ships {:y-1 (assoc-in (ship :y 80.0 80.0) [:inv :y] 40)}}}}
          t 4200
          universe (resource-collection initial-universe t)]

      (is (= 0 (count (get-in universe [:grades :y :resources]))))
      (is (nil? (get-in universe [:grades :y :resources :y-1])))
      (is (= 44 (get-in universe [:tribes :y :ships :y-1 :inv :y])))

      )))


