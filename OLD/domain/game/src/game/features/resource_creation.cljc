(ns game.features.resource-creation)

;; (defn is-pos-within-block? [pos block]
;;   (let [x (get-in block [:pos :x])
;;         y (get-in block [:pos :y])
;;         half-w (/ (get-in block [:size :w]) 2)
;;         half-h (/ (get-in block [:size :h]) 2)]
;;     (and
;;      (>= (:x pos) (- x half-w))
;;      (<= (:x pos) (+ x half-w))
;;      (>= (:y pos) (- y half-h))
;;      (<= (:y pos) (+ y half-h)))))

;; (defn is-pos-within-any-block? [pos blocks]
;;   (pos?
;;    (count
;;     (filter #(is-pos-within-block? pos %)
;;             (vals blocks)))))

;; (defn random-pos-not-in-block [blocks]
;;   (let [!x (atom (- (rand 1200.0) 600.0))
;;         !y (atom (- (rand 800.0) 400.0))]
;;     (while (is-pos-within-any-block? {:x @!x :y @!y} blocks)
;;       (reset! !x (- (rand 1200.0) 600.0))
;;       (reset! !y (- (rand 800.0) 400.0)))
;;     {:x @!x :y @!y}))

;; DUPLICATION
(defn distance-from-to [pos-1 pos-2]
  (let [dx (- (:x pos-2) (:x pos-1))
        dy (- (:y pos-2) (:y pos-1))]
    (Math/sqrt (+ (* dx dx) (* dy dy)))))



(defn is-pos-within-circle? [pos circle]
  (let [distance-to-ship (distance-from-to (:pos circle) pos)
        radius (:radius circle)]
    (<= distance-to-ship radius)))

(defn is-pos-within-box? [pos box]
  (let [x (get-in box [:pos :x] 0)
        y (get-in box [:pos :y] 0)
        half-w (/ (get-in box [:size :w]) 2)
        half-h (/ (get-in box [:size :h]) 2)]
    (and
     (>= (:x pos) (- x half-w))
     (<= (:x pos) (+ x half-w))
     (>= (:y pos) (- y half-h))
     (<= (:y pos) (+ y half-h)))))

(defn is-pos-within-group? [pos group-props group-elements]
  (let [!within-group? (atom false)]
    (doseq [[type props & inner-elements] group-elements]
      (when
          (case type
            :circle (is-pos-within-circle? pos props)
            :box (is-pos-within-box? pos props)
            :group (is-pos-within-group? pos props inner-elements))
        (reset! !within-group? true)))
    @!within-group?))

;; need to redevelop this USING TDD
(defn is-pos-within-terrain? [pos terrain]
  (let [!within-terrain? (atom false)
        [_ props & elements] terrain]
    (when (is-pos-within-group? pos props elements)
      (reset! !within-terrain? true))
    @!within-terrain?)

  #_(pos?
   (+
    (count
     (filter #(is-pos-within-circle? pos %)
             (vals (:circles terrain))))
    (count
     (filter #(is-pos-within-box? pos %)
             (vals (:boxes terrain)))))))

(defn random-pos-not-in-terrain [terrain]
  (let [!x (atom (- (rand 2400.0) 1200.0))
        !y (atom (- (rand 1600.0) 800.0))]
    (while (is-pos-within-terrain? {:x @!x :y @!y} terrain)
      (reset! !x (- (rand 2400.0) 1200.0))
      (reset! !y (- (rand 1600.0) 800.0)))
    {:x @!x :y @!y}))

(defn resource-creation [world t]
  (update
   world
   :grades
   (fn [grades]
     (into {}
           (map
            (fn [[grade-id grade]]
              (let [amount-of-grade (apply + (map :amount (vals (:resources grade))))
                    min-amount (get grade :min 0)]
                ;; create single resources up to the amount needed
                (let [amount-needed (max 0 (- min-amount amount-of-grade))
                      new-resources
                      (into {}
                            (map (fn [n]
                                   (let [
                                         ;; resource-id (keyword (str (name grade-id) "-" (rand-int 1000000)))
                                         resource-id  (str grade-id "-" (rand-int 1000000))
                                         resource-pos (random-pos-not-in-terrain (:terrain world))]
                                     [resource-id {:pos resource-pos :amount 1}]))
                                 (range amount-needed)))]
                  [grade-id (update grade :resources merge new-resources)])))
            grades)))))

