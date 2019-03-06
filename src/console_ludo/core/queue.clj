(ns console-ludo.core.queue
  (:require [console-ludo.commons :refer [queue]]))

(def color-order [:green :yellow :blue :red])

(defn color-cmp [x y]
  (Integer/compare (.indexOf color-order x) (.indexOf color-order y)))

(defn from-seq [colors]
  (->> colors
    (sort color-cmp)
    (queue)))

(defn rotate [q]
  (conj (pop q) (peek q)))
