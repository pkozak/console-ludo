(ns console-ludo.ui.congratulations
  (:require
    [clojure.string :as str]
    [console-ludo.core.game :as GAME]
    [console-ludo.ui.board :as UI-BOARD]
    [console-ludo.commons :refer [enumerate]]))

(defn render-player-list [colors]
  (str/join "\n"
    (for [[i color] (enumerate colors)]
      (str (inc i) ". " (name color)))))

(defn rendered [game]
  (str/join "\n"
    [(UI-BOARD/rendered game)
     (str "CONGRATULATIONS!")
     (str (render-player-list (GAME/rating game)))]))

(defn display [game]
  (println (rendered game)))
