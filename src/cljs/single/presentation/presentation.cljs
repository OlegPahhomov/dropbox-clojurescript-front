(ns single.presentation.presentation
  (:require [reagent.core :as reagent :refer [atom]]
            [single.presentation.pages :as pages]
            [single.presentation.utils :as utils]
            [single.ROUTES :as R])

  (:import goog.History))

(enable-console-print!)


(defonce current-page (atom 0))

(defn next-page []
  (swap! current-page utils/limit_top))

(defn previous-page []
  (swap! current-page utils/limit_bottom))

(def arrow-key-listener
  (do
    (.addEventListener js/window "keydown"
                       (fn [event]
                         (let [key-code (.-keyCode event)
                               left-arrow 37
                               right-arrow 39]
                           (cond
                             (= left-arrow key-code) (previous-page)
                             (= right-arrow key-code) (next-page)))))))

(defonce setup arrow-key-listener)

(defn presentation []
  [:div
   [:h2 "This is presentation"]
   [:div [:a {:href R/P_HOME} "go to the home page"]]
   [:div
    [:button {:on-click previous-page} "previous page"]
    (str " current page: " @current-page " ")
    [:button {:on-click next-page} "next page"]
    (get pages/pages @current-page)]
   ])
