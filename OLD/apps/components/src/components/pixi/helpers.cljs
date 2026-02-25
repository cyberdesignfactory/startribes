(ns components.pixi.helpers)

(defn tribe-color [tribe-id]
  (case tribe-id
    :r 0xff3333
    :y 0xffaa33
    :g 0x33cc33
    :b 0x3333ff
    0x777777))

(defn resource-color [rt]
  (case rt
    :r 0xff7777
    :y 0xffff77
    :g 0xaaffaa
    :b 0x7777ff
    ;; :ry 0xffaa33
    :ry 0xffcc77
    :yg 0xccee33
    :gb 0x33ffcc
    :br 0xff33ff
    0x777777)

  #_(case rt
    :r 0xff7777
    :y 0xffff77
    :g 0xaaffaa
    :b 0x7777ff
    :ry 0xffcc77
    :yg 0xaaff77
    :gb 0x77ffaa
    :br 0xff77ff
    0x777777))

(defn tribe-resource-types [tribe-id]
  (case tribe-id
    :r [:b :br :r :ry :y]
    :y [:r :ry :y :yg :g]
    :g [:y :yg :g :gb :b]
    :b [:g :gb :b :br :r]
    []))


