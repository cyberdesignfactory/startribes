(ns viewer.worlds.simple)


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

      ;; ship type?
      :type :destroyer
      :class :heavy
      :weapons {
                :neutron-cannon {:megatons 16}
                }

      :pos {:x -900 :y 900}
      :rot 0.75
      :controls {:thrust 0.0
                 :rudder 0.0}}}}}

  )

(defn simple-world []
  {:tribes (simple-tribes)})

