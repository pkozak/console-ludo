(ns console-ludo.ui.cell
  (:require [console-ludo.core.game :as GAME]
            [console-ludo.core.pawn :as PAWN]
            [console-ludo.ui.cell-parser :as PARSER]
            [console-ludo.ui.pawn :as UI-PAWN]))

(def start? (set (vals PAWN/start-path-nrs)))

(defn rendered-empty [game cell]
  "   ")

(defn rendered-yard [game {index :index color :color}]
  (let [pawn (GAME/find-player-pawn game color index)]
    (if (PAWN/on-yard? pawn)
      (UI-PAWN/rendered pawn)
      "   ")))

(defn rendered-meta [game {index :index color :color}]
  (let [pos {:type :meta, :color color, :nr index}
        pawn (GAME/find-pawn-by-pos game pos)]
    (if pawn
      (UI-PAWN/rendered pawn)
      " ^ ")))

(defn rendered-path [game {index :index}]
  (let [pos {:nr index, :type :path, :color nil}
        pawn (GAME/find-pawn-by-pos game pos)]
    (cond
      (not (nil? pawn)) (UI-PAWN/rendered pawn)
      (start? index) " @ "
      :else " . ")))

(def renderers {:yard rendered-yard,
                :empty rendered-empty,
                :meta rendered-meta
                :path rendered-path})

(defn rendered-cell [game info]
  (let [type (get info :type)
        renderer (get renderers type)]
    (renderer game info)))

(defn rendered [game cell-symbol]
  (->>
    (PARSER/parsed cell-symbol)
    (rendered-cell game)))
