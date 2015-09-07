(ns single.home.home
  (:use [jayq.core :only [$ ajax prevent parent document-ready siblings attr prop]])
  (:require [goog.net.XhrIo :as xhr]
            [cljs.core.async :as async :refer [chan close! <!]]
            [cljs-http.client :as http]
            [dommy.core :as dom]
            [hiccups.runtime :as hiccupsrt]
            [single.mytemplates.templates :as mytemplates]
            [single.mytemplates.upload-form :as upload-form]
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
(def REMOVE_URL "http://localhost:8080/remove/")

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
   ]
  )

(defn fill-files [files]
  (dom/set-html!
    (sel1 :#show_files)
    (html (mytemplates/display-files files)))
  )

(defn add-delete-button [element element-id]
  (jq/bind element :click
           (fn [e]
             (do
               (prevent e)
               (http/post (str REMOVE_URL element-id))
               (.reload js/location)
               ))))

(defn add-delete-buttons []
  (let [elements ($ "button.close")]
    (loop [i 0]
      (when (< i (.-length elements))
        (let [element (get elements i)
              element-id (subs (attr element :id) 7)]
          (add-delete-button element element-id)
          (recur (inc i)))))))

(defn replace-upload-form []
  (dom/set-html!
    (sel1 :#file-form-div)
    (html upload-form/upload-form)))

(defn add-files []
  (jq/bind
    ($ "#fileForm") :submit
    (fn [e]
      (do
        (prevent e)
        (js/alert "uploading doesn't work")))))

(go
  (fill-files (<! GET_FILES))
  (add-delete-buttons)
  (replace-upload-form)
  (add-files))



;;; adding files in vain :)
#_(defn generate-form-data [params]
    (let [form-data (js/FormData.)]
      (doseq [[k v] params]
        (.append form-data (name k) v))
      form-data))

#_(defn upload [file]
    (go (let [response (<! (http/post "http://localhost:8080/add"
                                      {:body (generate-form-data {:file file})}))]
          (prn (:status response))
          (prn (:body response)))))

#_(let [files (first ($ ($ "#fileForm")))]
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


