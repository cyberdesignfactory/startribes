(ns viewer.views
  (:require
   [re-frame.core :as re-frame]
   [viewer.events :as events]
   [viewer.subs :as subs]
   [viewer.panels.intro :as panels.intro]
   [viewer.panels.calibration :as panels.calibration]
   [viewer.panels.home :as panels.home]
   [viewer.panels.mission :as panels.mission]
   [viewer.panels.accomplished :as panels.accomplished]
   [viewer.panels.failed :as panels.failed]))

(defn main-panel []
  (let [!ui (re-frame/subscribe [::subs/ui])]
    (fn []
      ;; need to dereference ratoms INSIDE the inner fn
      (case (:panel-id @!ui)
        :intro [panels.intro/intro-panel]
        :calibration [panels.calibration/calibration-panel]
        :home [panels.home/home-panel]
        :mission [panels.mission/mission-panel]
        :accomplished [panels.accomplished/accomplished-panel]
        :failed [panels.failed/failed-panel]))))

