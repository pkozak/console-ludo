(ns console-ludo.core.cube
  (:refer-clojure :exclude [rand]))

(def max-cube 6)
(def max? #{max-cube})

(defn rand []
  (inc (rand-int max-cube)))
