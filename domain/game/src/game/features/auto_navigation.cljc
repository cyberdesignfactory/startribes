(ns game.features.auto-navigation
  (:require [clojure.pprint :as pp]
            ;; [domain.geometry.line-of-sight :refer [line-of-sight?]]
            ;; [domain.nav.position :refer [nav-position]]
            ;; [domain.maths :refer [distance-from-to rotation-delta-from-to]]
            [game.features.shared :as shared]
            ))

#_(defn print-ts [caption]
  (let [t (- (.getTime (java.util.Date.)) 1733100000000)]
    (println caption ": " t)))

(defn auto-navigation [universe t]
  ;; (println "auto-navigation:")
  ;; (time)
  (let [!universe (atom universe)]
    (doseq [[tribe-id tribe] (:tribes universe)
            [ship-id ship] (:ships tribe)]
      (let [ship-pos (get-in ship [:pos])
            target (:target ship)
            ;; _ (print-ts "1")

            target-pos
            (case (:type target)
              :resource
              (get-in universe
                      [:grades (:grade-id target) :resources (:id target) :pos])
              :base
              (get-in universe
                      [:tribes (:tribe-id target) :bases (:id target) :pos])
              :ship
              (get-in universe
                      [:tribes (:tribe-id target) :ships (:id target) :pos])
              nil nil
              )
            ;; _ (when (nil? target-pos) (pirintf "Target pos nil for %s\n" ship-id))

            ship-pos (get-in ship [:pos])
            ;; _ (when (nil? ship-pos) (printf "Ship pos nil for %s" ship-id))
            ship-rot (get-in ship [:rot])
            blocks (:blocks universe)
            nav-points (:nav-points universe)

            ;; _ (print-ts "2")

            ;; los-to-target? (and (not (nil? target-pos))
            ;;                     (line-of-sight? ship-pos target-pos blocks))
            ;; possibly-nil-nav-pos (if (nil? target-pos)
            ;;                        {:x 0.0 :y 0.0}
            ;;                        (if los-to-target?
            ;;                          target-pos
            ;;                          (if-let [point (nav-position ship-pos target-pos nav-points blocks)]
            ;;                            point
            ;;                            nil)))

            ;; _ (print-ts "3")

            ;; nav-pos (or possibly-nil-nav-pos {:x 0 :y 0})

            ;; nav-pos {:x 0 :y 0}  ;; target-pos
            nav-pos (if (nil? target-pos)
                      {:x 0 :y 0}
                      target-pos)

            _ (when (nil? nav-pos)
                (do
                  (pp/pprint universe)
                  ;; (printf "Nav pos nil for %s\n" ship-id)
                  ))

            ;; _ (println ship-pos nav-pos)

            ;; _ (println ship-id)
            ;; _ (pp/pprint ship)

            distance (shared/distance-from-to ship-pos nav-pos)
            rotation-delta (shared/rotation-delta-from-to ship-pos ship-rot nav-pos)
            thrust
            (case (:type target)
              :ship (cond

                      ;; (and los-to-target?
                      ;;      ;; to avoid 'orbiting' the target
                      ;;      (< distance 120)) 0.0  ;; -1.0
                      ;; (and los-to-target?
                      ;;      (< distance 240)) 0.0

                      :else 1.0)
              (if (and (> (Math/abs rotation-delta) 0.125)
                       (< distance 160)) 0.0 1.0))  ;; first was -0.2

            rudder (cond
                     (< rotation-delta -0.02) -1.0
                     (> rotation-delta  0.02)  1.0
                     :else 0.0)

            ;; fire-pressed? (and (= :ship (:type target))
            ;;                    (< distance 800))

            fire-pressed? (and (or (= :ship (:type target))
                                   (and (= :base (:type target))
                                        (not= tribe-id (:tribe-id target))))
                               (< distance 800))

            ]

        ;; (print-ts "4")

        #_(swap! !universe assoc-in [:tribes tribe-id :ships ship-id :controls]
               {:thrust thrust
                :rudder rudder
                :fire-pressed? fire-pressed?})
        (swap! !universe assoc-in [:tribes tribe-id :ships ship-id :thrust] thrust)
        (swap! !universe assoc-in [:tribes tribe-id :ships ship-id :rudder] rudder)
        (swap! !universe assoc-in [:tribes tribe-id :ships ship-id :fbs :primary] fire-pressed?)

        ))

    @!universe))
