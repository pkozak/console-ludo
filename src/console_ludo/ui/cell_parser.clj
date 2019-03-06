(ns console-ludo.ui.cell-parser
  (:require [console-ludo.commons :refer [str->int!]]))

(def letter->color {"g" :green, "b" :blue, "r" :red, "y" :yellow})

(defn parsed-empty [cell-symbol]
  (when (= cell-symbol :emp)
    {:type :empty}))

(defn parsed-yard [cell-symbol]
  (when-let [[_ letter digit] (re-find #"y(\w)(\d)" (name cell-symbol))]
    {:type :yard
     :index (str->int! digit)
     :color (letter->color letter)}))

(defn parsed-meta [cell-symbol]
  (when-let [[_ letter digit] (re-find #"m(\w)(\d)" (name cell-symbol))]
    {:type :meta
     :index (str->int! digit)
     :color (letter->color letter)}))

(defn parsed-path [cell-symbol]
  (when-let [[_ digits] (re-find #"p(\d\d)" (name cell-symbol))]
    {:type :path
     :index (str->int! digits)}))

(def parsers [parsed-empty parsed-meta parsed-path parsed-yard])

(defn parsed-slowly [cell]
  (->>
    (for [parser parsers]
      (parser cell))
    (remove nil?)
    (first)))

(def parsed (memoize parsed-slowly))
