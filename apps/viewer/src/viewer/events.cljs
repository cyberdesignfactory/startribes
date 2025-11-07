(ns viewer.events
  (:require
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [viewer.db :as db]
   [tribes.simple :as simple]
   [game.cycle :as cycle]
   ;; [terrains.circles :as circles]
   ;; [terrains.boxes :as boxes]
   ;; [terrains.experimental :as experimental]
   [worlds.rob :as rob]
   [worlds.circles :as circles]
   [worlds.boxes :as boxes]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn limit [amount]
  (max -1.0 (min 1.0 amount)))

(defn orientation-changed [alpha beta gamma]

  ;; thrust = (40 - beta) / 20
  ;; rudder = (90 - alpha) / 30
  ;; strafe = (5 + gamma) / 15

  (let [controls {:thrust (limit (/ (- 50.0 beta) 16.0))
                  :strafe (limit (/ (- 90.0 alpha) 10.0))
                  :rudder (limit (/ (+ 5.0 gamma) 30.0))}]
    (re-frame/dispatch [::set-controls controls])
    (re-frame/dispatch [::set-orientation {:beta beta
                                           :alpha alpha
                                           :gamma gamma}])))
(re-frame/reg-event-db
 ::initialize-orientation-listener
 (fn [db _]
   (.addEventListener js/window "deviceorientation"
                      (fn [e]
                        (when (.-alpha e)
                          #_(js/alert (str (.-alpha e)
                                           (.-beta e)
                                           (.-gamma e)))
                          (orientation-changed (.-alpha e)
                                               (.-beta e)
                                               (.-gamma e)))))

   (-> db
       (assoc :ship-thrust false)
       (assoc :ship-rudder false)
       (assoc :ship-strafe false)

       )))


(re-frame/reg-event-db
 ::initialize-touch-listener
 (fn [_ _]
   (.addEventListener js/window "touchstart"
                      (fn [e]
                        ;; (.preventDefault e)
                        ;; (.log js/console e)
                        (doseq [ct (.-changedTouches e)]
                          ;; (.log js/console ct)
                          ;; (.log js/console (.-clientX ct))

                          #_(if (> (.-clientX ct) (/ (.-innerWidth js/window) 2))
                              (.log js/console "fb1 down")
                              (.log js/console "fb2 down")
                              )

                          (if (> (.-clientX ct) (/ (.-innerWidth js/window) 2))
                            (re-frame/dispatch [::update-fbs :primary true])
                            (re-frame/dispatch [::update-fbs :secondary true])
                            )
                          ;; (.log js/console (.-innerWidth js/window))

                          )

                        ))
   (.addEventListener js/window "touchend"
                      (fn [e]
                        ;; (.preventDefault e)
                        ;; (.log js/console e)
                        (doseq [ct (.-changedTouches e)]
                          ;; (.log js/console ct)
                          ;; (.log js/console (.-clientX ct))

                          #_(if (> (.-clientX ct) (/ (.-innerWidth js/window) 2))
                            (.log js/console "fb1 up")
                            (.log js/console "fb2 up")
                            )
                          (if (> (.-clientX ct) (/ (.-innerWidth js/window) 2))
                            (re-frame/dispatch [::update-fbs :primary false])
                            (re-frame/dispatch [::update-fbs :secondary false])
                            )
                          )

                        ))))

(re-frame/reg-event-db
 ::set-controls
 (fn [db [_ controls]]
   (-> db
       ;; (assoc :controls controls)
       (assoc :ship-thrust (:thrust controls))
       (assoc :ship-rudder (:rudder controls))
       (assoc :ship-strafe (:strafe controls))
       )))

(re-frame/reg-event-db
 ::update-fbs
 (fn [db [_ fb down?]]
   (-> db
       (assoc-in [:ship-fbs fb] down?))))

(re-frame/reg-event-db
 ::set-orientation
 (fn [db [_ orientation]]
   (-> db
       (assoc :orientation orientation)
       )))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(re-frame/reg-event-db
 ::set-ship-thrust
 (fn [db [_ value]]
   (assoc db :ship-thrust value)))

(re-frame/reg-event-db
 ::set-ship-rudder
 (fn [db [_ value]]
   (assoc db :ship-rudder value)))

(re-frame/reg-event-db
 ::set-ship-strafe
 (fn [db [_ value]]
   (assoc db :ship-strafe value)))

(re-frame/reg-event-db
 ::begin-campaign-clicked
 (fn [db _]
   (-> db
       (assoc-in [:ui :panel-id] :home)
       )))

(re-frame/reg-event-db
 ::mission-selected
 (fn [db [_ id]]
   (let [t (.getTime (js/Date.))]
     (-> db
         (assoc-in [:ui :panel-id] :mission)
         ;; set mission up here
         (assoc :mission-id id)

         (assoc :time-left (get-in db [:missions id :time-limit]))

         #_(assoc :world (case (get-in db [:missions id :world-id])

                         :simple-circles-world
                         (circles/simple-circles-world {:t t})

                         :simple-boxes-world
                         (boxes/simple-boxes-world {:t t})

                         :simple-rob-world
                         (rob/simple-rob-world {:t t})

                         )
                )

         ;; reset player ship fire buttons
         (assoc-in [:ship-fbs :primary] false)
         (assoc-in [:ship-fbs :secondary] false)
         ;; these two lines probably aren't needed
         (assoc-in [:world :tribes :y :ships :y-1 :fbs :primary] false)
         (assoc-in [:world :tribes :y :ships :y-1 :fbs :secondary] false)

         (assoc :world (get-in db [:missions id :initial-world]))

         ;; and start game cycle presumably
         ))))

(re-frame/reg-event-db
 ::mission-accomplished
 (fn [db [_ id]]
   (let [time-limit (get-in db [:missions id :time-limit])
         time-taken (- time-limit (:time-left db))]
     (-> db
         (assoc-in [:ui :panel-id] :accomplished)
         ;; tear world down
         (dissoc :world)
         ;; and stop game cycle presumably
         ;; and update progress
         (assoc-in [:missions id :accomplished?] true)
         (assoc-in [:missions id :most-recent-time] time-taken)
         (update-in [:missions id :best-time]
                    (fn [current-best-time]
                      (if (or
                           (not current-best-time)
                           (< time-taken current-best-time))
                        time-taken
                        current-best-time)))

         (assoc-in [:time-left] nil))
     )))

(re-frame/reg-event-db
 ::mission-failed
 (fn [db [_ id]]
   (-> db
       (assoc-in [:ui :panel-id] :failed)
       ;; tear mission down here
       ;; and world
       (dissoc :world)
       ;; and stop game cycle presumably
       )))

#_(re-frame/reg-event-db
 ::game-continued
 (fn [db [_ id]]
   (-> db
       ;; tear mission down here
       (dissoc :mission-id)
       (dissoc :mission)
       (assoc-in [:ui :panel-id] :home)
       )))

(re-frame/reg-event-fx
 ::game-continued
 (fn [{:keys [db]} [_ id]]
   {:db
    (-> db
        ;; tear mission down here
        (dissoc :mission-id)
        (dissoc :mission)
        (assoc-in [:ui :panel-id] :home))
    :fx [[:dispatch [::scroll-to-latest-mission]]]}))

(re-frame/reg-event-db
 ::scroll-to-latest-mission
 (fn [db _]
   (js/setTimeout
    (fn []
      (let [el (.getElementById js/document "latest")]
        (when el
          (.scrollIntoView el))))
    200)
   db))

(re-frame/reg-event-db
 ::game-cycle
 (fn [db [_ t]]
   (if (:world db)
     (let [mission-id (:mission-id db)]
       (when ((get-in db [:missions mission-id :success-fn]) (:world db))
         (re-frame/dispatch [::mission-accomplished mission-id]))
       (when ((get-in db [:missions mission-id :failure-fn]) (:world db))
         (re-frame/dispatch [::mission-failed mission-id]))
       (when-not (get-in db [:world :tribes :y :ships :y-1])
         (re-frame/dispatch [::mission-failed mission-id]))
       (when (neg? (:time-left db))
         (re-frame/dispatch [::mission-failed mission-id]))

       (-> db
           (update :time-left #(- % 0.01))

           (assoc-in [:world :tribes :y :ships :y-1 :fbs :primary]
                     (get-in db [:ship-fbs :primary]))
           (assoc-in [:world :tribes :y :ships :y-1 :fbs :secondary]
                     (get-in db [:ship-fbs :secondary]))

           (update :world
                   #(cycle/game-cycle %
                                      t
                                      (:ship-thrust db)
                                      (:ship-rudder db)
                                      (:ship-strafe db)))))
     db)))

