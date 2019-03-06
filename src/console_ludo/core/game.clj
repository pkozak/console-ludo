(ns console-ludo.core.game
  (:require [console-ludo.commons :refer [enumerate]]
            [console-ludo.core.pawn :as PAWN]
            [console-ludo.core.player :as PLAYER]
            [console-ludo.core.queue :as QUEUE]))

(defn from-players [players]
  {:players (PLAYER/indexed players)
   :queue (QUEUE/from-seq (map :color players))
   :rating []})

(defn running? [{queue :queue}]
  (->
    (seq queue)
    (count)
    (> 1)))

(defn by-color [color]
  #(= color (get %1 :color)))

(defn find-player [game color]
  (->> game
       (:players)
       (filter (by-color color))
       (first)))

(defn find-curr-player [{queue :queue :as game}]
  (find-player game (peek queue)))

(defn find-pawns [game]
  (mapcat :pawns (get game :players)))

(defn find-player-pawn [game color index]
  (get-in (find-player game color) [:pawns index]))

(defn find-player-pawns [game color]
  (get (find-player game color) :pawns))

(defn update-pawn [game pawn data]
  (let [game-atom (atom game)]
    (when-let [{index :index color :color} pawn]
      (when-let [{player-index :index} (find-player game color)]
        (swap! game-atom assoc-in [:players player-index :pawns index] (merge pawn data))))
    @game-atom))

(defn normalize-pawn [game pawn]
  (cond
    (nil? pawn) nil
    (vector? pawn) (normalize-pawn game {:color (get pawn 0) :index (get pawn 1)})
    (contains? pawn :distance) pawn
    :else (let [{color :color index :index} pawn]
            (find-player-pawn game color index))))

(defn by-pos [pos]
  #(= pos (PAWN/pos %1)))

(defn find-pawn-by-pos [game pos]
  (->>
    (find-pawns game)
    (filter (by-pos pos))
    (first)))

(defn movable-pawn? [game pawn cube]
  (when-let [pawn (normalize-pawn game pawn)]
    (when-let [next-pos (PAWN/next-pos pawn cube)]
      (let [found (find-pawn-by-pos game next-pos)]
        (or
          (not found)
          (not= (get pawn :color) (get found :color)))))))

(defn player-finished? [game color]
  (->>
    (find-player-pawns game color)
    (every? PAWN/on-meta?)))

(defn curr-player-done [{queue :queue :as game}]
  (-> game
      (update :rating conj (peek queue))
      (assoc :queue (pop queue))))

(defn switch-player [{queue :queue :as game}]
  (assoc game :queue (QUEUE/rotate queue)))

(defn rating [{rating :rating queue :queue}]
  (concat rating queue))

(defn move-pawn [game pawn cube]
  (when-let [pawn (normalize-pawn game pawn)]
    (when-let [distance (PAWN/distance+ (:distance pawn) cube)]
      (when-let [pos (PAWN/pos (assoc pawn :distance distance))]
        (-> game
            (update-pawn (find-pawn-by-pos game pos) {:distance PAWN/yard})
            (update-pawn pawn {:distance distance}))))))

(defn movable-pawns [game color cube]
  (->>
    (find-player-pawns game color)
    (filterv #(movable-pawn? game %1 cube))))
