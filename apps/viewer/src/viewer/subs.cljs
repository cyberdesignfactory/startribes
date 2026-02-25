(ns viewer.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::orientation
 (fn [db]
   (:orientation db)))

(re-frame/reg-sub
 ::ui
 (fn [db]
   (:ui db)))

(re-frame/reg-sub
 ::missions
 (fn [db]
   (:missions db)))

(re-frame/reg-sub
 ::time-left
 (fn [db]
   (:time-left db)))

(re-frame/reg-sub
 ::mission-id
 (fn [db]
   (:mission-id db)))

(re-frame/reg-sub
 ::mission
 (fn [db]
   (get-in db [:missions (:mission-id db)])))

(re-frame/reg-sub
 ::world
 (fn [db]
   (:world db)))

(re-frame/reg-sub
 ::ship-thrust
 (fn [db _]
   (:ship-thrust db)))

(re-frame/reg-sub
 ::ship-rudder
 (fn [db _]
   (:ship-rudder db)))

(re-frame/reg-sub
 ::ship-strafe
 (fn [db _]
   (:ship-strafe db)))

(re-frame/reg-sub
 ::ship-fbs
 (fn [db _]
   (:ship-fbs db)))

(re-frame/reg-sub
 ::player-action-title
 (fn [db _]
   (get-in db [:world :tribes :y :ships :y-1 :action-title])))

(re-frame/reg-sub
 ::player-action-description
 (fn [db _]
   (get-in db [:world :tribes :y :ships :y-1 :action-description])))
