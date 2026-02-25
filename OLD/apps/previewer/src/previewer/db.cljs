(ns previewer.db
  (:require [terrains.experimental :as experimental]
            [tribes.simple :as simple]))

(def default-db
  {:name "re-frame"

   ;; :ui-path [:worlds :w-1]
   ;; :ui-path [:tribe :ships :destroyer]

   :ui {

        :sidebar-open? false
        :selected-path [:worlds :circles]




        ;; :type-id :terrain
        ;; :terrain-id :experimental
        ;; :tribe-id :y
        }


   ;; :structure
   ;; {
   ;;  :worlds {:title "Simple"
   ;;           :world (simple/simple-world 0)}
   ;;  :terrain {:title "Terrain"
   ;;            :items
   ;;            {:doors {:title "Doors"
   ;;                     :items
   ;;                     {:flat {:title "Flat"                ;; v== default values
   ;;                             :world {:terrain {:doors {:flat {}}}}}}}}}}

   :world {:terrain (experimental/experimental-terrain 0)
           :tribes (simple/simple-tribes)}

   })


