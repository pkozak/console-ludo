(ns console-ludo.ui.game
  (:require [clojure.string :as str]
            [console-ludo.ui.board :as BOARD]
            [console-ludo.ui.pawn :as UI-PAWN]))

(defn rendered [game, {player :player, cube :cube, movable-pawns :movable-pawns}]
  (str/join "\n"
    (remove nil? [(BOARD/rendered game)
                  (str "PLAYER: " (get player :color))
                  (str "CUBE: " cube)
                  (when (seq movable-pawns)
                    (str "PAWNS: " (UI-PAWN/render-list movable-pawns)))])))

(defn display [game movement]
  (println (rendered game movement)))
