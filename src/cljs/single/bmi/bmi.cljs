(ns single.bmi.bmi
  (:require [reagent.core :as reagent :refer [atom]]
            [single.ROUTES :as R]))

(def bmi-data (atom {:height 180 :weight 80}))

(defn calc-bmi []
  (let [{:keys [height weight bmi] :as data} @bmi-data
        h (/ height 100)]
    (if (nil? bmi)
      (assoc data :bmi (/ weight (* h h)))
      (assoc data :weight (* bmi h h)))))

(defn slider [param value min max]
  (.log js/console param)
  [:input {:type      "range"
           :value     value
           :min       min
           :max       max
           :style     {:width "100%"}
           :on-change (fn [event]
                        (swap! bmi-data assoc param (.-target.value event))
                        (when (not= param :bmi)
                          (swap! bmi-data assoc :bmi nil)))}])

(defn bmi-component []
  (let [{:keys [weight height bmi]} (calc-bmi)
        [color diagnose]
        (cond
          (< bmi 18.5) ["orange" "underweight"]
          (< bmi 25) ["inherit" "normal"]
          (< bmi 30) ["orange" "overweight"]
          :else ["red" "obese"])]
    [:div
     [:h3 "BMI calculator"]
     [:div [:a {:href R/P_HOME} "go to the home page"]]
     [:div
      "Height: " (int height) "cm"
      [slider :height height 100 220]]
     [:div
      "Weight: " (int weight) "kg"
      [slider :weight weight 30 150]]
     [:div
      "BMI: " (int bmi) " "
      [:span {:style {:color color}} diagnose]
      [slider :bmi bmi 10 50]]]))

