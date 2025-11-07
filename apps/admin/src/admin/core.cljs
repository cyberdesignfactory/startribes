(ns admin.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [cljs.core.async :as a :refer [<! >! go]]
   [haslett.client :as ws]
   [haslett.format :as fmt]
   [admin.events :as events]
   [admin.views :as views]
   [admin.config :as config]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch-sync [::events/fetch-world-data])
  (re-frame/dispatch-sync [::events/connect])
  (dev-setup)
  (mount-root))

