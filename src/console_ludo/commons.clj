(ns console-ludo.commons)

(defn read-line! []
  (let [x (read-line)]
    (if x x (throw (new InterruptedException)))))

(def str->int! #(Integer/parseInt %1))

(defn str->int [s]
  (try (str->int! s)
       (catch NumberFormatException e nil)))

(defn between? [[from to] x]
  (and
    (>= x from)
    (< x to)))

(defn enumerate [coll]
  (map-indexed vector coll))

(defn queue
  ([] (clojure.lang.PersistentQueue/EMPTY))
  ([coll]
   (reduce conj clojure.lang.PersistentQueue/EMPTY coll)))

(defmethod print-method clojure.lang.PersistentQueue [v ^java.io.Writer w]
  (.write w (str "#Q "(seq v))))
