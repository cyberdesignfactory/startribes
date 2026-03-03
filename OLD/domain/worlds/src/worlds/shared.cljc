(ns worlds.shared)

(defn simple-grades []
  {
   :r {:role :primary
       :colour 0xff7777
       :min 8
       }
   :y {:role :primary
       :colour 0xffff77
       ;; :resources {:y-1 {:pos {:x 20.0 :y 20.0} :amount 4}}
       :min 8
       }
   :g {:role :primary
       :colour 0xaaffaa
       :min 8
       }
   :b {:role :primary
       :colour 0x7777ff
       :min 8
       }
   :ry {:role :secondary
        :colour 0xffcc77}
   :yg {:role :secondary
        :colour 0xaaff77}
   :gb {:role :secondary
        :colour 0x77ffaa}
   :br {:role :secondary
        :colour 0xff77ff}
   }
  )


(defn simple-tribes []
  {
   :r
   {:colour 0xff3333

    :production {:primary :r
                 :secondary-1 :br
                 :secondary-2 :ry
                 :surplus-1 :b
                 :surplus-2 :y}

    ;; :strategy :passive
    :strategy :aggressive

    ;; :bases {:r-1 {:role :primary
    ;;               :pos {:x -900 :y -900}}}
    ;; :ships {:r-1 {:pos {:x -900 :y -900}
    ;;               :rot 0.75
    ;;               :target {:type :ship
    ;;                        :tribe-id :y
    ;;                        :id :y-1}
    ;;               }
    ;;         :r-2 {:pos {:x -600 :y -900}
    ;;               :rot 0.75
    ;;               :target {:type :ship
    ;;                        :tribe-id :y
    ;;                        :id :y-1}
    ;;               }}

    }

   :y
   {:colour 0xffaa33

    :strategy :passive  ;; FOR NOW

    :production {:primary :y
                 :secondary-1 :ry
                 :secondary-2 :yg
                 :surplus-1 :r
                 :surplus-2 :g}

    ;; :bases {:y-1 {:role :primary
    ;;               :pos {:x 900 :y -900}}}

    ;; :ships {:y-1 (simple-ship)}
    }


   :g
   {:colour 0x33ff33

    :production {:primary :g
                 :secondary-1 :yg
                 :secondary-2 :gb
                 :surplus-1 :y
                 :surplus-2 :b}

    :strategy :passive  ;; FOR NOW

    ;; :bases {:g-1 {:role :primary
    ;;               :pos {:x 900 :y 900}}}
    ;; :ships {:g-1 {:pos {:x 900 :y 900}
    ;;               :rot 0.75
    ;;               :target {:type :ship
    ;;                        :tribe-id :b
    ;;                        :id :b-1}
    ;;               }
    ;;         :g-2 {:pos {:x 600 :y 900}
    ;;               :rot 0.75
    ;;               :target {:type :ship
    ;;                        :tribe-id :b
    ;;                        :id :b-2}}}

    }

   :b
   {:colour 0x3333ff

    :production {:primary :b
                 :secondary-1 :gb
                 :secondary-2 :br
                 :surplus-1 :g
                 :surplus-2 :r}

    :strategy :retaliatory

    ;; :bases {:b-1 {:role :primary
    ;;               :pos {:x -900 :y 900}}}
    ;; :ships {:b-1 {:pos {:x -900 :y 900}
    ;;               :rot 0.75
    ;;               :target {:type :ship
    ;;                        :tribe-id :r
    ;;                        :id :r-1}
    ;;               }
    ;;         :b-2 {:pos {:x -600 :y 900}
    ;;               :rot 0.75
    ;;               :target {:type :ship
    ;;                        :tribe-id :r
    ;;                        :id :r-2}
    ;;               }}

    }

   })


