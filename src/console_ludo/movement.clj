(ns console-ludo.movement
  (:require
     [console-ludo.core.game :as GAME]
     [console-ludo.core.pawn :as PAWN]
     [console-ludo.core.cube :as CUBE]
     [console-ludo.ui.input :as INPUT]))

(defn template [game]
  (let [{color :color :as player} (GAME/find-curr-player game)
        cube (CUBE/rand)]
    {:cube cube
     :movable-pawns (GAME/movable-pawns game color cube)
     :player player
     :pawn nil}))

(defn fill [game template]
  (let [pawn-index (INPUT/pawn-index game template)
        pawn (get-in template [:player :pawns pawn-index])]
    (assoc template :pawn pawn)))

(defn bonus? [pawn cube]
  (and
    pawn
    (CUBE/max? cube)
    (not (PAWN/on-yard? pawn))))

(defn continue? [game {pawn :pawn cube :cube}]
  (if-let [color (get pawn :color)]
    (and
      (not (GAME/player-finished? game color))
      (bonus? pawn cube))))
