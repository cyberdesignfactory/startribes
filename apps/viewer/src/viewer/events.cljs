(ns viewer.events
  (:require
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [viewer.db :as db]
   [game.cycle :as cycle]
   [worlds.rob :as rob]
   [worlds.circles :as circles]
   [worlds.boxes :as boxes]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

;; ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn limit [amount]
  (max -1.0 (min 1.0 amount)))

(defn orientation-changed [alpha beta gamma]
  (re-frame/dispatch [::set-orientation
                      {:beta beta
                       :alpha alpha
                       :gamma gamma}]))

(re-frame/reg-event-db
 ::initialize-touch-listener
 (fn [_ _]
   (.addEventListener js/window "touchstart"
                      (fn [e]
                        (.preventDefault e)
                        (doseq [ct (.-changedTouches e)]
                          (if (> (.-clientX ct) (/ (.-innerWidth js/window) 2))
                            (re-frame/dispatch [::update-fbs :primary true])
                            (re-frame/dispatch [::update-fbs :secondary true])))))
   (.addEventListener js/window "touchend"
                      (fn [e]
                        ;; (.preventDefault e)
                        (doseq [ct (.-changedTouches e)]
                          (if (> (.-clientX ct) (/ (.-innerWidth js/window) 2))
                            (re-frame/dispatch [::update-fbs :primary false])
                            (re-frame/dispatch [::update-fbs :secondary false])))))))

(re-frame/reg-event-db
 ::update-fbs
 (fn [db [_ fb down?]]
   (-> db
       (assoc-in [:ship-fbs fb] down?))))

(re-frame/reg-event-db
 ::set-orientation
 (fn [db [_ orientation]]
   (let [calibrating? (= :calibration (get-in db [:ui :panel-id]))
         min-alpha (get-in db [:calibration :min-alpha])
         max-alpha (get-in db [:calibration :max-alpha])
         min-beta (get-in db [:calibration :min-beta])
         max-beta (get-in db [:calibration :max-beta])
         min-gamma (get-in db [:calibration :min-gamma])
         max-gamma (get-in db [:calibration :max-gamma])

         controls
         {:thrust (limit
                   (- 1.0
                      (* 2.0 (/ (- (:beta orientation) min-beta)
                                (- max-beta min-beta)))))
          :rudder (limit
                   (+ -1.0
                      (* 2.0 (/ (- (:gamma orientation) min-gamma)
                                (- max-gamma min-gamma)))))}]

     (if calibrating?
       (let [min-alpha (min (get-in db [:calibration :min-alpha])
                            (:alpha orientation))
             max-alpha (max (get-in db [:calibration :max-alpha])
                            (:alpha orientation))
             min-beta (min (get-in db [:calibration :min-beta])
                           (:beta orientation))
             max-beta (max (get-in db [:calibration :max-beta])
                           (:beta orientation))
             min-gamma (min (get-in db [:calibration :min-gamma])
                            (:gamma orientation))
             max-gamma (max (get-in db [:calibration :max-gamma])
                            (:gamma orientation))]

         (-> db
             (assoc-in [:calibration :min-alpha] min-alpha)
             (assoc-in [:calibration :max-alpha] max-alpha)
             (assoc-in [:calibration :min-beta] min-beta)
             (assoc-in [:calibration :max-beta] max-beta)
             (assoc-in [:calibration :min-gamma] min-gamma)
             (assoc-in [:calibration :max-gamma] max-gamma)
             (assoc :orientation orientation)
             (assoc :ship-thrust (:thrust controls))
             (assoc :ship-rudder (:rudder controls))
             (assoc :ship-strafe (:strafe controls))
             ))

       (-> db
           (assoc :orientation orientation)
           (assoc :ship-thrust (:thrust controls))
           (assoc :ship-rudder (:rudder controls))
           (assoc :ship-strafe (:strafe controls))
           )))))

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
 ::calibrate-clicked
 (fn [db _]
   (-> db
       (assoc :world
              {:tribes
               {:y
                {
                 :colour 0xffaa33
                 :ships
                 {:y-1
                  {:pos {:x 0 :y 0}
                   :rot 0.0
                   }}}}
               :terrain
               (into
                [:group {:pos {:x 0 :y 0}}]
                (concat
                 (for [y (range -680 800 160)]
                   [:box {:pos {:x -240
                                :y y}
                          :size {:w 80
                                 :h 80}}])
                 (for [y (range -600 800 160)]
                   [:box {:pos {:x 240
                                :y y}
                          :size {:w 80
                                 :h 80}}])))})

       (assoc-in [:ui :panel-id] :calibration)
       (assoc :calibration
              {:min-alpha (- (get-in db [:orientation :alpha]) 16)
               :max-alpha (+ (get-in db [:orientation :alpha]) 16)
               :min-beta (- (get-in db [:orientation :beta]) 16)
               :max-beta (+ (get-in db [:orientation :beta]) 16)
               :min-gamma (- (get-in db [:orientation :gamma]) 16)
               :max-gamma (+ (get-in db [:orientation :gamma]) 16)}))))

(re-frame/reg-event-fx
 ::request-orientation-permission
 (fn [_ _]
   ;; Feature detect the permission API (iOS 13+)
   ;; Non-iOS 13+ devices (like Android) don't need explicit permission
   (when (and (exists? js/DeviceOrientationEvent)
              (fn? js/DeviceOrientationEvent.requestPermission))
     (.requestPermission js/DeviceOrientationEvent))
     ;; maybe should do this in the 'then' of the promise...
     ;;(if (= response "granted")
     ;; (re-frame/dispatch [::start-orientation-listener])
     ;; (re-frame/dispatch [::permission-denied])
   {:dispatch [::start-orientation-listener]}))

(re-frame/reg-event-fx
 ::start-orientation-listener
 (fn [_ _]
   (.addEventListener js/window "deviceorientation"
                      (fn [event]
                        (orientation-changed (.-alpha event)
                                             (.-beta event)
                                             (.-gamma event))))
   {:dispatch [::begin-campaign-clicked]}))

(re-frame/reg-event-db
 ::permission-denied
 (fn [db [_ _]]
   (js/alert "Permission denied")
   db))

(re-frame/reg-event-db
 ::begin-campaign-clicked
 (fn [db _]
   (-> db
       (assoc-in [:ui :panel-id] :home))))

(re-frame/reg-event-db
 ::campaign-clicked
 (fn [db [_ id]]
   (-> db
       (assoc :campaign-id id))))

(re-frame/reg-event-db
 ::mission-selected
 (fn [db [_ id]]
   (let [t (.getTime (js/Date.))]
     (-> db
         (assoc-in [:ui :panel-id] :mission)
         ;; set mission up here
         (assoc :mission-id id)
         (assoc :time-left (get-in db [:campaigns (:campaign-id db)
                                       :missions id :time-limit]))
         ;; reset player ship fire buttons
         (assoc-in [:ship-fbs :primary] false)
         (assoc-in [:ship-fbs :secondary] false)

         (assoc :world (get-in db [:campaigns (:campaign-id db)
                                   :missions id :initial-world]))))))

(re-frame/reg-event-db
 ::mission-accomplished
 (fn [db [_ id]]
   (let [time-limit (get-in db [:campaigns (:campaign-id db)
                                :missions id :time-limit])
         time-taken (- time-limit (:time-left db))]
     (-> db
         (assoc-in [:ui :panel-id] :accomplished)
         ;; tear world down
         (dissoc :world)
         ;; update progress
         (assoc-in [:campaigns (:campaign-id db)
                    :missions id :accomplished?] true)
         (assoc-in [:campaigns (:campaign-id db)
                    :missions id :most-recent-time] time-taken)
         (update-in [:campaigns (:campaign-id db)
                     :missions id :best-time]
                    (fn [current-best-time]
                      (if (or
                           (not current-best-time)
                           (< time-taken current-best-time))
                        time-taken
                        current-best-time)))
         (assoc-in [:time-left] nil)))))

(re-frame/reg-event-db
 ::mission-failed
 (fn [db [_ id]]
   (-> db
       (assoc-in [:ui :panel-id] :failed)
       (dissoc :world))))

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
       (when mission-id
         (when ((get-in db [:campaigns (:campaign-id db)
                            :missions mission-id :success-fn]) (:world db))
           (re-frame/dispatch [::mission-accomplished mission-id]))
         (when (and (fn? (get-in db [:campaigns (:campaign-id db)
                                     :missions mission-id :failure-fn]))
                    ((get-in db [:campaigns (:campaign-id db)
                                 :missions mission-id :failure-fn]) (:world db)))
           (re-frame/dispatch [::mission-failed mission-id]))
         (when-not (get-in db [:world :tribes :y :ships :y-1])
           (re-frame/dispatch [::mission-failed mission-id]))
         (when (neg? (:time-left db))
           (re-frame/dispatch [::mission-failed mission-id])))

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

