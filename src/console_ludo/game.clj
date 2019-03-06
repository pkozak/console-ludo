(ns console-ludo.game
  (:require [console-ludo.core.game :as GAME]
            [console-ludo.core.player :refer [players]]
            [console-ludo.movement :as MOVEMENT]
            [console-ludo.ui.input :as UI-INPUT]
            [console-ludo.ui.congratulations :as UI-CONGRATULATIONS]))

(def ga (atom nil))

(defn process-pawn-moving [game {pawn :pawn, cube :cube}]
  (if-let [{color :color index :index} pawn]
    (GAME/move-pawn game [color index] cube)
    game))

(defn process-player-finishing [game {pawn :pawn}]
  (if-let [{color :color} pawn]
    (if (GAME/player-finished? game color)
      (GAME/curr-player-done game)
      game)
    game))

(defn process-player-switching [game movement]
  (if (MOVEMENT/continue? game movement)
    game
    (GAME/switch-player game)))

(defn process-movement [game movement]
  (-> game
    (process-pawn-moving movement)
    (process-player-finishing movement)
    (process-player-switching movement)))

(defn updated-game [game]
  (let [template (MOVEMENT/template game)
        movement (MOVEMENT/fill game template)]
    (process-movement game movement)))

(defn game-loop [game]
  (reset! ga game)
  (if (GAME/running? game)
    (recur (updated-game game))
    game))

(defn run []
  (->>
    (players [:red :yellow :green :blue])
    (GAME/from-players)
    (game-loop)
    (UI-CONGRATULATIONS/display)))

(defn -main []
  ;(UI-INPUT/debug-mode-on!)
  (try
    (run)
    (catch InterruptedException _ (println "Game was terminated."))))
