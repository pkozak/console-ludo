(ns console-ludo.core-test
  (:require [clojure.test :refer :all]
            [console-ludo.core.game :refer :all]
            [console-ludo.core.pawn :as PAWN]
            [console-ludo.core.player :refer [player players]]))

(defn distances [& custom]
  (PAWN/distances custom))

(defn movable-pawns-indexes [game color cube]
  (->>
    (movable-pawns game color cube)
    (mapv :index)))

(deftest test-move-pawn
  (testing "Pawn can leave yard when cube is 6"
    (is (->
          (from-players [(player :yellow)])
          (move-pawn [:yellow 0] 6)
          (find-player-pawn :yellow 0)
          (PAWN/pos)
          (= {:type :path, :nr 13, :color nil}))))
  (testing "Pawn can't leave yard when cube is not 6"
    (is (->
          (from-players [(player :yellow)])
          (move-pawn [:yellow 0] 4)
          (nil?))))
  (testing "Pawn on path can move"
    (is (->
          (from-players [(player :yellow (distances 0))])
          (move-pawn [:yellow 0] 4)
          (move-pawn [:yellow 0] 5)
          (find-player-pawn :yellow 0)
          (:distance)
          (= 9))))
  (testing "Pawn moving to meta nr 0"
    (is (->
          (from-players [(player :yellow (distances 51))])
          (move-pawn [:yellow 0] 1)
          (find-player-pawn :yellow 0)
          (PAWN/pos)
          (= {:type :meta, :nr 0, :color :yellow}))))
  (testing "Pawn moving to meta nr 3"
    (is (->
          (from-players [(player :yellow (distances 52))])
          (move-pawn [:yellow 0] 3)
          (find-player-pawn :yellow 0)
          (PAWN/pos)
          (= {:type :meta, :nr 3, :color :yellow}))))
  (testing "Pawn moving to over meta"
    (is (->
          (from-players [(player :yellow (distances 52))])
          (move-pawn [:yellow 0] 4)
          (nil?))))
  (testing "Pawn can't move to field where is pawn with same color (case: from yard)"
    (is (->
          (from-players (players [:yellow :red]))
          (move-pawn [:yellow 0] 6)
          (movable-pawns-indexes :yellow 6)
          (= [0]))))
  (testing "Pawn can't move to field where is pawn with same color (case: path)"
    (is (->
          (from-players [(player :yellow (distances 3 0))])
          (movable-pawns-indexes :yellow 3)
          (= [0]))))
  (testing "Pawn can't move to field where is pawn with same color (case: meta)"
    (is (->
          (from-players [(player :yellow (distances 55 50))])
          (movable-pawns-indexes :yellow 5)
          (= []))))
  (testing "Pawn can kill enemy pawn"
    (is (= (PAWN/path-nr :yellow 15) (PAWN/path-nr :red 41)))
    (is (->
          (from-players [(player :yellow (distances 15))
                         (player :red (distances 40))])
          (move-pawn [:red 0] 1)
          (find-player-pawn :yellow 0)
          (PAWN/pos)
          (:type)
          (= :yard)))))

(deftest test-player-finished?
  (testing "one pawn is on meta"
   (is (->
         (from-players [(player :yellow (distances 52))])
         (player-finished? :yellow)
         (false?))))
  (testing "one pawn is on meta"
    (is (->
          (from-players [(player :yellow (distances 52 53 54 55))])
          (player-finished? :yellow)))))
