(ns console-ludo.ui.input
  (:require
    [console-ludo.commons :refer [str->int str->int! read-line!]]
    [console-ludo.ui.game :as UI-GAME]))

(def digit? #(Character/isDigit %1))

(defn parse-digits [text]
  (apply str (filter digit? text)))

(defn read-valid [validator]
  (->>
    (repeatedly #(read-line!))
    (map validator)
    (remove nil?)
    (first)))

(defn make-pawn-index-validator [movable-pawns]
  (let [pawn-index? (apply hash-set (map :index movable-pawns))
        text->index (comp pawn-index? str->int parse-digits)]
    (fn [text]
      (if-let [index (text->index text)]
        index
        (println "Incorrect pawn. Try again")))))

(defn skip []
  (println "Press enter to skip")
  (read-line!)
  nil)

(defn user-pawn-index [game {movable-pawns :movable-pawns :as template}]
  (UI-GAME/display game template)
  (if (seq movable-pawns)
    (read-valid (make-pawn-index-validator movable-pawns))
    (skip)))

(defn auto-pawn-index [game {movable-pawns :movable-pawns}]
  (-> movable-pawns
    (first)
    (get :index)))

(def pawn-index-callback (atom user-pawn-index))

(defn debug-mode-on! []
  (reset! pawn-index-callback auto-pawn-index))

(defn pawn-index [game movement]
  (@pawn-index-callback game movement))