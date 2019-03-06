(ns console-ludo.core.player
  (:require [console-ludo.core.pawn :as PAWN]))

(defn player
  ([color] (player color (PAWN/distances)))
  ([color distances]
   {:color color
    :pawns (PAWN/pawns color distances)}))

(defn players [colors]
  (mapv player colors))

(defn indexed [players]
  (->> players
    (map-indexed (fn [i player] (assoc player :index i)))
    (vec)))