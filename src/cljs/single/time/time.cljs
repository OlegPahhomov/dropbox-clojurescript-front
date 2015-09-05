(ns single.time.time
  (:require [reagent.core :as reagent :refer [atom]]
            [single.ROUTES :as R]))

(defonce timer (atom (js/Date.)))

(defonce time-color (atom "#f34"))

(defonce time-updater (js/setInterval
                        #(reset! timer (js/Date.)) 1000))


(defn clock []
  (let [time-str (-> @timer .toTimeString (clojure.string/split " ") first)]
    [:div.example-clock
     {:style {:color     @time-color
              :font-size "100px"}}
     time-str]))

(defn color-input []
  [:div.color-input
   "Time color: "
   [:input {:type      "text"
            :value     @time-color
            :on-change #(reset! time-color (-> % .-target .-value))}]])

(defn simple-example []
  [:div
   [:h2 "Hello world, it is now"]
   [:div [:a {:href R/P_HOME} "go to the home page"]]
   [:div {:style {:paddingTop "40px"
                  :paddingBottom "40px"}} [clock]]
   [:div [color-input]]
   ])

(defn ^:export run []
  (reagent/render [simple-example]
                  (js/document.getElementById "app")))