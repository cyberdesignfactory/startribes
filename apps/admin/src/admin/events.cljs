(ns admin.events
  (:require
   [cljs.core.async :as a :refer [<! >! go]]
   [re-frame.core :as re-frame]
   [ajax.core :as ajax]
   [haslett.client :as ws]
   [haslett.format :as fmt]
   [editscript.core :as e]
   [admin.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-fx
 ::fetch-world-data
 (fn [{:keys [db]} _]
   {:db (-> db
            (assoc-in [:ui :loading?] true))
    :http-xhrio {:method :get
                 :uri "http://localhost:8080/"
                 :timeout 8000
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::fetch-success]
                 :on-failure [::fetch-failure]}}))

(re-frame/reg-event-db
 ::fetch-success
 (fn [db [_ world]]
   #_(.log js/console result)
   (-> db
       (assoc :world world)
       (assoc-in [:ui :loading?] false))))

(re-frame/reg-event-db
 ::fetch-failure
 (fn [db [_ result]]
   (.log js/console result)
   (-> db
       )))

(def !stream (atom nil))

;; could be an fx perhaps - to dispatch event
(re-frame/reg-event-db
 ::connect
 (fn [db _]
   (go
     (let [url  "ws://localhost:8080/updates"
           stream (<! (ws/connect url))]
       (reset! !stream stream)
       #_(>! (:out stream) "Hello Rob")
       (loop []
         (let [item (<! (:in stream))]
           #_(ws/close stream)
           (when item
             (re-frame/dispatch [::update-received item])
             (recur))))))
   db))

(re-frame/reg-event-db
 ::update-received
 (fn [db [_ edits]]
   ;; (println edits)
   (-> db
       (update :world #(e/patch % (e/edits->script (cljs.reader/read-string edits)))))))

(re-frame/reg-event-db
 ::damage-clicked
 (fn [db [_ ship-id]]
   #_(println "Damage clicked:" ship-id)
   (go
     (>! (:out @!stream) [:damage ship-id]))
   db))

(re-frame/reg-event-db
 ::create-ship-clicked
 (fn [db _]
   #_(println "Create Ship clicked:")
   (go
     (>! (:out @!stream) "Create Ship"))
   db))

