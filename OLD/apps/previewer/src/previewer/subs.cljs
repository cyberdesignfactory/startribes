(ns previewer.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

#_(re-frame/reg-sub
 ::t
 (fn [db]
   (:t db)))

(re-frame/reg-sub
 ::ui
 (fn [db]
   (:ui db)))

(re-frame/reg-sub
 ::world
 (fn [db]
   (get-in db [:world])))

