(ns worlds.helpers.translate
  (:require [clojure.pprint :refer [pprint]]))

(defn translate-world [world {:keys [x y]}]
  (-> world


      #_(update :blocks
              (fn [blocks]
                (into {}
                      (for [[id block] blocks]
                        [id (-> block
                                (update-in [:pos :x] #(+ x %))
                                (update-in [:pos :y] #(+ y %)))]))))
      (update :nav-points
              (fn [nav-points]
                (into {}
                      (for [[id nav-point] nav-points]
                        [id (-> nav-point
                                (update-in [:x] #(+ x %))
                                (update-in [:y] #(+ y %))
                                )]))))
      (update :grades
                (fn [grades]
                  (into {}
                        (for [[id grade] grades]
                          [id (-> grade
                                  (assoc :resources
                                         (into {}
                                               (for [[id resource] (:resources grade)]
                                                 [id (-> resource
                                                         (update-in [:pos :x] #(+ x %))
                                                         (update-in [:pos :y] #(+ y %)))]))))]))))

      (update-in [:terrain :circles]
              (fn [circles]
                (into {}
                      (for [[id circle] circles]
                        [id (-> circle

                                (update-in [:pos :x] #(+ x %))
                                (update-in [:pos :y] #(+ y %))

                                )]))))
      (update :tribes
              (fn [tribes]
                (into {}
                      (for [[id tribe] tribes]
                        [id (-> tribe
                                (assoc :bases
                                       (into {}
                                             (for [[id base] (:bases tribe)]
                                               [id (-> base
                                                       (update-in [:pos :x] #(+ x %))
                                                       (update-in [:pos :y] #(+ y %)))])))
                                (assoc :ships
                                       (into {}
                                             (for [[id ship] (:ships tribe)]
                                               [id (-> ship
                                                       (update-in [:pos :x] #(+ x %))
                                                       (update-in [:pos :y] #(+ y %)))]))))]))))))

