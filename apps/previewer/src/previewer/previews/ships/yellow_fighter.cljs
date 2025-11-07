(ns previewer.previews.ships.yellow-fighter)

(defn yellow-fighter-preview []
  (let [ship {

              :rot -0.125
              ;; :thrust 1.0
              :thrust 0.7
              :energy 0.7

              }]
    {:tribes {:y {:colour 0xffaa33
                  :ships {:y-1 ship}}}}))

