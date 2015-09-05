(ns single.about.about
  (:require [single.ROUTES :as R]))


(defn about-page []
  [:div
   [:h2 "About single"]
   [:div [:a {:href R/P_HOME} "go to the home page"]]
   ])