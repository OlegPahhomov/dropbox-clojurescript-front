(ns single.presentation.utils
  (:require [single.presentation.pages :as p]))


(defn limit_top [page-nr]
  (if (>= page-nr (- (count p/pages) 1))
    page-nr
    (inc page-nr)))

(defn limit_bottom [page-nr]
  (if (<= page-nr 0)
    0
    (dec page-nr)))

