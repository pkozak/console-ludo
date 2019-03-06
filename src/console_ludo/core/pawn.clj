(ns console-ludo.core.pawn
  (:require [console-ludo.core.cube :as CUBE]
            [console-ludo.commons :refer [enumerate between?]]))

(def path-size 52)
(def max-distance 56)
(def start-path-nrs {:green 0, :yellow 13, :blue 26, :red 39})
(def pawns-per-player 4)
(def yard -1)

(defn pawn [index color distance]
  {:index index
   :color color
   :distance distance})

(defn distances
  ([] (distances []))
  ([custom]
   (->>
     (cycle [yard])
     (concat custom)
     (take pawns-per-player))))

(defn pawns
  ([color] (pawns color nil))
  ([color custom-distances]
   (vec
     (for [[index distance] (enumerate (distances custom-distances))]
       (pawn index color distance)))))

(defn on-yard? [{distance :distance}]
  (when distance
    (= yard distance)))

(defn on-path? [{distance :distance}]
  (when distance
    (between? [0 path-size] distance)))

(defn on-meta? [{distance :distance}]
  (when distance
    (between? [path-size max-distance] distance)))

(defn path-nr [color distance]
  (-> (get start-path-nrs color)
      (+ distance)
      (mod path-size)))

(defn pos [{color :color, index :index, distance :distance :as pawn}]
  (cond
    (nil? pawn) nil
    (on-yard? pawn) {:type :yard, :color color, :nr index}
    (on-path? pawn) {:type :path, :color nil, :nr (path-nr color distance)}
    (on-meta? pawn) {:type :meta, :color color, :nr (- distance path-size)}
    :else nil))

(defn distance+ [distance cube]
  (if (on-yard? {:distance distance})
    (if (CUBE/max? cube) 0 nil)
    (let [next-distance (+ distance cube)]
      (if (< next-distance max-distance) next-distance nil))))

(defn next-pos [pawn cube]
  (when-let [distance (distance+ (:distance pawn) cube)]
    (pos (assoc pawn :distance distance))))
