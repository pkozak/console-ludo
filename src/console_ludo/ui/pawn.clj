(ns console-ludo.ui.pawn
  (:require [clojure.string :as str]))

(def color-letter {:green "G", :blue "B", :yellow "Y", :red "R"})

(defn rendered [{color :color index :index}]
  (str (color-letter color) index " "))

(defn render-list [pawns]
  (if pawns
    (->>
      (map rendered pawns)
      (str/join ","))
    "NONE"))
