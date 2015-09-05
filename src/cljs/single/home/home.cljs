(ns single.home.home
  (:require [single.ROUTES :as R]
            [goog.net.XhrIo :as xhr]
            [cljs.core.async :as async :refer [chan close!]]
            [dommy.core :as dom]
            [hiccups.runtime :as hiccupsrt]
            [clojure.walk :refer [keywordize-keys]]

            [cljs.reader :as reader]
            [mytemplates.templates :as mytemplates]
            )
  (:require-macros
    [cljs.core.async.macros :refer [go alt!]]
    [dommy.core :refer [sel sel1]]
    [hiccups.core :as hiccups :refer [html]]
    )
  )

(def FILES_URL "http://localhost:8080/files")

(defn log [s]
  (.log js/console (str s)))

(defn GET [url]
  (let [ch (chan 1)]
    (xhr/send url
              (fn [event]
                (let [res (-> event .-target .getResponseText)]
                  (go (>! ch res)
                      (close! ch)))))
    ch))

(def GET_FILES (GET FILES_URL))

(defn home-page []
  [:div#content
   [:div#show_files "Loading..."]
   [:div#file-form-div "Upload form is loading..."]
   ]
  )

(go
  (dom/set-html!
    (sel1 :#show_files)
    (html (let [files (<! GET_FILES)]
            (mytemplates/display-files files)
            )))

  (dom/set-html!
    (sel1 :#file-form-div)
    (html mytemplates/upload-form)
    )
  )



