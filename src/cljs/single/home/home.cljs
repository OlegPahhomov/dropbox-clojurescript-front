(ns single.home.home
  (:use [jayq.core :only [$ ajax prevent parent document-ready siblings attr prop]])
  (:require [single.ROUTES :as R]
            [goog.net.XhrIo :as xhr]
            [cljs.core.async :as async :refer [chan close! <!]]
            [cljs-http.client :as http]
            [dommy.core :as dom]
            [hiccups.runtime :as hiccupsrt]
            [clojure.walk :refer [keywordize-keys]]
            [single.mytemplates.templates :as mytemplates]
            [jayq.core :as jq]
            [ajax.core :as ajax :refer [GET POST]]
            )
  (:require-macros
    [cljs.core.async.macros :refer [go alt!]]
    [dommy.core :refer [sel sel1]]
    [hiccups.core :as hiccups :refer [html]]
    )
  )

(def FILES_URL "http://localhost:8080/files")

(defn log [s]
  (.log js/console s))

(defn GETs [url]
  (let [channel (chan 1)]
    (xhr/send url
              (fn [event]
                (let [response (-> event .-target .getResponseText)]
                  (go (>! channel response)
                      (close! channel)))))
    channel))

(def GET_FILES (GETs FILES_URL))

(defn home-page []
  [:div#content
   [:div#show_files "Loading..."]
   [:div#file-form-div "Upload form is loading..."]
   [:div#fancybox "blabla"]
   [:button#btn "button"]
   ]
  )


(defn generate-form-data [params]
  (let [form-data (js/FormData.)]
    (doseq [[k v] params]
      (.append form-data (name k) v))
    form-data))

(defn upload [file]
  (go (let [response (<! (http/post "http://localhost:8080/add"
                                    {:body (generate-form-data {:file file})}))]
        (prn (:status response))
        (prn (:body response)))))

;; some-dom-element is a single file upload input
;; <input type="file">



(go
  (dom/set-html!
    (sel1 :#show_files)
    (html (let [files (<! GET_FILES)]
            (mytemplates/display-files files)
            )
          ))

  (dom/set-html!
    (sel1 :#file-form-div)
    (html mytemplates/upload-form))

  (dom/set-text!
    (sel1 :#fancybox)
    (do
      ;(log (ajax/POST "http://localhost:8080/remove/1"))
      "text")
    )
  (jq/bind ($ "#btn") :click (fn [] (js/alert (str "Hi! "))))
  (jq/bind ($ "button.close")
           :click
           (fn [] (js/alert "delete doesnt work")
             #_(http/post
                 "http://localhost:8080/remove/")

             ))
  #_(let [files (first ($ ($ "#fileForm"))) ]
    (jq/bind
      ($ "#fileForm") :submit
      (fn [e]
        (do
          (prevent e)
          (js/alert "uploading doesn't work")
          #_(log files)
          #_(http/request {
                         :url             "http://localhost:8080/add"
                         :data            (new js/FormData files)
                         :method          "POST"
                         :cache           false
                         :contentType     "multipart/form-data"
                         :processData     false
                         :response-format {:content-type "application/json"}
                         })
          ;(upload (-> "file" .-files first))
          #_(ajax "http://localhost:8080/add"
                  {
                   :data            (new js/FormData files)
                   :method          "POST"
                   :cache           false
                   :contentType     false
                   :processData     false
                   :response-format {:content-type "application/json"}
                   }
                  )
          )
        ;(http/post
        ;  "http://localhost:8080/add")
        ;  {:multipart-params
        ;   ["file" (new js/FormData files)]}
        )
      )
    )
  )


