(ns tribes.simple)

(defn simple-tribes []
  {:y
   {
    :colour 0xffaa33
    :bases
    {:y-1
     {:pos {:x 900 :y -900}}
     }
    :ships
    {:y-1
     {
      ;; :pos {:x -160 :y 0}
      :pos {:x -900 :y 900}
      :rot 0.75
      :energy 0.7
      :controls {:thrust 0.0
                 :rudder 0.0
                 :fire-pressed? true}}}}})

