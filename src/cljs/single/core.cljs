(ns single.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]

            [single.home.home :as home]
            [single.about.about :as about]
            [single.presentation.presentation :as presentation]
            [single.bmi.bmi :as bmi]
            [single.time.time :as time]
            [single.todo.todo :as todo]
            [single.ROUTES :as R]
            )
  (:import goog.History))

;; -------------------------
;; Views


(defn current-page [] [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix (str R/PREFIX))


(defn secretary-defroutes [route-map]
  (doseq [[route page] route-map]
    (secretary/defroute (str route) [] (session/put! :current-page page))))


(secretary-defroutes {R/HOME         #'home/home-page
                      R/ABOUT        #'about/about-page
                      R/BMI          #'bmi/bmi-component
                      R/PRESENTATION #'presentation/presentation
                      R/TIME         #'time/simple-example
                      R/TODO         #'todo/todo-app
                      })



;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      EventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-root))


#_(do
    (secretary/defroute (str R/HOME) [] (session/put! :current-page #'home/home-page))
    (secretary/defroute (str R/ABOUT) [] (session/put! :current-page #'about/about-page))
    (secretary/defroute (str R/BMI) [] (session/put! :current-page #'bmi/bmi-component))
    (secretary/defroute (str R/PRESENTATION) [] (session/put! :current-page #'presentation/presentation)))