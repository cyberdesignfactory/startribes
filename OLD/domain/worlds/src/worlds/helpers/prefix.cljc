(ns worlds.helpers.prefix
  (:require [clojure.pprint :refer [pprint]]))

(defn prefix-world-keys [world prefix]
  (-> world
      #_(update :blocks
                (fn [blocks]
                  (update-keys blocks
                               #(keyword (str (name prefix) "-" (name %))))))

      (update :nav-points
              (fn [nav-points]
                (update-keys nav-points
                             #(keyword (str (name prefix) "-" (name %))))))
      (update :grades
              (fn [grades]
                (into {}
                      (for [[id grade] grades]
                        [id (-> grade
                                (assoc :resources
                                       (update-keys (:resources grade)
                                                    #(keyword (str (name prefix) "-" (name %))))))]))))
      (update :tribes
              (fn [tribes]
                (into {}
                      (for [[id tribe] tribes]
                        [id (-> tribe
                                (assoc :bases
                                       (update-keys (:bases tribe)
                                                    #(keyword (str (name prefix) "-" (name %)))))
                                (assoc :ships
                                       (update-keys (:ships tribe)
                                                    #(keyword (str (name prefix) "-" (name %))))))]))))

      (update-in [:terrain :circles]
                 (fn [circles]
                   (update-keys circles
                                #(keyword (str (name prefix) "-" (name %))))))

      ))

