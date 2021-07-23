(ns password-philosophy-test
  (:require [clojure.test :refer [deftest is]]
            [password-philosophy])
  (:import [java.io BufferedReader StringReader]))

(deftest parse-line-valid-input
  (is (=
       [2 3 \x "abcdef"]
       (password-philosophy/parse-line "2-3 x: abcdef"))))

(deftest parse-line-invalid-lower-bound
  (is (thrown?
       java.lang.IllegalArgumentException
       (password-philosophy/parse-line "z-3 x: abcdef"))))

(deftest parse-line-invalid-upper-bound
  (is (thrown?
       java.lang.IllegalArgumentException
       (password-philosophy/parse-line "1-f x: abcdef"))))

(deftest parse-line-invalid-character
  (is (thrown?
       java.lang.IllegalArgumentException
       (password-philosophy/parse-line "1-9 *: abcdef"))))

(deftest valid?-valid-password
  (is (= true
         (password-philosophy/valid? [1 3 \a "abcdef"]))))

(deftest valid?-character-repeated-side-by-side
  (is (= true
         (password-philosophy/valid? [1 3 \a "aacdef"]))))

(deftest valid?-character-spread-through-password
  (is (= true
         (password-philosophy/valid? [1 3 \a "abadaf"]))))

(deftest valid?-character-not-found-in-password
  (is (= false
         (password-philosophy/valid? [1 3 \x "abcdef"]))))

(deftest valid?-character-appearing-more-than-upper-bound
  (is (= false
         (password-philosophy/valid? [1 3 \a "abaaea"]))))

(deftest read-policies-stream
  (let [file-content "1-2 a: abcdef\n2-3 b: cdefgh"
        rdr (BufferedReader. (StringReader. file-content))]
    (is (=
         [[1 2 \a "abcdef"]
          [2 3 \b "cdefgh"]]
         (password-philosophy/read-policies rdr)))))

