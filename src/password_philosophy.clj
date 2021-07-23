(ns password-philosophy
  (:require [clojure.java.io :as io])
  (:import java.lang.IllegalArgumentException))

(defn- count-filtered [coll f]
  (->> coll (filter f) count))

(defn- count-char [ch passwd]
  (count-filtered passwd (partial = ch)))

(defn valid? [[lower upper ch passwd]]
  (<= lower (count-char ch passwd) upper))

(defn- count-valid-passwords [policies]
  (count-filtered policies valid?))

(def ^:private line-regex #"^(\d+)-(\d+) ([a-z]): ([a-z]+)$")

(defn parse-line [line]
  (let [[_ lower upper ch passwd :as matches] (re-matches line-regex line)]
    (if (nil? matches)
      (throw (IllegalArgumentException. (str "Invalid policy line " line)))
      [(Integer/parseInt lower)
       (Integer/parseInt upper)
       (first ch)
       passwd])))

(defn read-policies [rdr]
  (->> rdr
       line-seq
       (map parse-line)))

(defn run [{input '--input :or {input "sample.txt"}}]
  (->> input
       str
       io/reader
       read-policies
       count-valid-passwords
       println))
