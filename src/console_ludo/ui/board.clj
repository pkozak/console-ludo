(ns console-ludo.ui.board
  (:require [clojure.string :as str]
            [console-ludo.ui.cell :as CELL]))

(def cells [[:yg0, :yg1, :emp, :emp, :emp, :emp, :p11, :p12, :p13, :emp, :emp, :emp, :emp, :yy0, :yy1],
            [:yg2, :yg3, :emp, :emp, :emp, :emp, :p10, :my0, :p14, :emp, :emp, :emp, :emp, :yy2, :yy3],
            [:emp, :emp, :emp, :emp, :emp, :emp, :p09, :my1, :p15, :emp, :emp, :emp, :emp, :emp, :emp],
            [:emp, :emp, :emp, :emp, :emp, :emp, :p08, :my2, :p16, :emp, :emp, :emp, :emp, :emp, :emp],
            [:emp, :emp, :emp, :emp, :emp, :emp, :p07, :my3, :p17, :emp, :emp, :emp, :emp, :emp, :emp],
            [:emp, :emp, :emp, :emp, :emp, :emp, :p06, :emp, :p18, :emp, :emp, :emp, :emp, :emp, :emp],
            [:p00, :p01, :p02, :p03, :p04, :p05, :emp, :emp, :emp, :p19, :p20, :p21, :p22, :p23, :p24],
            [:p51, :mg0, :mg1, :mg2, :mg3, :emp, :emp, :emp, :emp, :emp, :mb3, :mb2, :mb1, :mb0, :p25],
            [:p50, :p49, :p48, :p47, :p46, :p45, :emp, :emp, :emp, :p31, :p30, :p29, :p28, :p27, :p26],
            [:emp, :emp, :emp, :emp, :emp, :emp, :p44, :emp, :p32, :emp, :emp, :emp, :emp, :emp, :emp],
            [:emp, :emp, :emp, :emp, :emp, :emp, :p43, :mr3, :p33, :emp, :emp, :emp, :emp, :emp, :emp],
            [:emp, :emp, :emp, :emp, :emp, :emp, :p42, :mr2, :p34, :emp, :emp, :emp, :emp, :emp, :emp],
            [:emp, :emp, :emp, :emp, :emp, :emp, :p41, :mr1, :p35, :emp, :emp, :emp, :emp, :emp, :emp],
            [:yr0, :yr1, :emp, :emp, :emp, :emp, :p40, :mr0, :p36, :emp, :emp, :emp, :emp, :yb0, :yb1]
            [:yr2, :yr3, :emp, :emp, :emp, :emp, :p39, :p38, :p37, :emp, :emp, :emp, :emp, :yb2, :yb3]])

(defn rendered [game]
  (str/join "\n"
    (for [cell-row cells]
      (str/join ""
        (for [cell-symbol cell-row]
          (CELL/rendered game cell-symbol))))))
