(ns single.home.home
  (:require [single.ROUTES :as R]
            [goog.net.XhrIo :as xhr]
            [cljs.core.async :as async :refer [chan close!]]
            [dommy.core :as dom]
            [hiccups.runtime :as hiccupsrt]
            [clojure.walk :refer [keywordize-keys]]
            [cognitect.transit :as t]
            [cljs.reader :as reader]
            [mytemplates]
            )
  (:require-macros
    [cljs.core.async.macros :refer [go alt!]]
    [dommy.core :refer [sel sel1]]
    [hiccups.core :as hiccups :refer [html]]
    )
  )

(def SERVER_URL "http://localhost:8080/files")

(defn log [s]
  (.log js/console (str s)))

(log "Started")

(defn GETU [url]
  (let [ch (chan 1)]
    (xhr/send url
              (fn [event]
                (let [res (-> event .-target .getResponseText)]
                  (go (>! ch res)
                      (close! ch)))))
    ch))

(def GET (GETU SERVER_URL))



(defn home-page []
  [:div#content
   [:div#show_files "Loading..."]
   [:div#file-form-div "Upload form is loading..."]




   ;;;experimental
   [:div [:h2 "Welcome to single Yo"]
    [:div [:a {:href R/P_ABOUT} "go to about page"]]
    [:div [:a {:href R/P_PRESENTATION} "go to presentation page"]]
    [:div [:a {:href R/P_BMI} "go to bmi calc"]]
    [:div [:a {:href R/P_TIME} "go to time page"]]
    [:div [:a {:href R/P_TODO} "go to todo page"]]
    [:div {:id "log"}]
    ]
   ]
  )

(defn read-json [json]
  (t/read (t/reader :json) json))

(defn add-div [x]
  [:div {:class "item"} (get x "name")])

(go
  (dom/set-html!
    (sel1 :#log)
    (html [:div {:class "bigitem "}
           (let [files (<! GET)]
             (do
               (map add-div (read-json files))
               ;(doseq file files (log file))
               ))
           ]))
  (dom/set-html!
    (sel1 :#file-form-div)
    (html mytemplates/upload-form)
    )
  )



