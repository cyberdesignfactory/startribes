(ns previewer.events
  (:require
   [re-frame.core :as re-frame]
   [game.cycle :as cycle]
   [terrains.circles :as circles]
   [terrains.boxes :as boxes]
   [terrains.experimental :as experimental]
   [previewer.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

#_(re-frame/reg-event-db
   ::set-t
   (fn [db [_ t]]
     (-> db
         (assoc :t t))))

(re-frame/reg-event-db
 ::menu-clicked
 (fn [db [_ t]]
   (-> db
       (update-in [:ui :sidebar-open?] not))))

(re-frame/reg-event-db
 ::preview-selected
 (fn [db [_ path]]
   (-> db
       (assoc-in [:ui :selected-path] path)
       (assoc-in [:ui :sidebar-open?] false))))

(re-frame/reg-event-db
 ::update-ui
 (fn [db [_ korks value]]

   (let [
         new-db (assoc-in db (cons :ui korks) value)

         world
         (case (get-in new-db [:ui :type-id])
           :terrains
           {:terrain
            (case (get-in new-db [:ui :terrain-id])
              :circles (circles/circles-terrain 0)
              :boxes (boxes/boxes-terrain 0)
              :experimental (experimental/experimental-terrain 0)
              )}

           :tribes
           {} ;; FOR NOW
           )]
     (-> new-db
         (assoc :world world)
         #_(assoc-in (cons :ui korks) value)))))

(re-frame/reg-event-db
 ::world-cycle
 (fn [db [_ t]]
   (-> db
       (update-in [:world :terrain]
                  #(cycle/terrain-cycle % t)))))

