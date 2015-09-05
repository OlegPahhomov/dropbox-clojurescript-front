(ns single.mytemplates.templates
  (:use [jayq.core :only [$ prevent document-ready]])
  (:require [cognitect.transit :as t]
            [ajax.core :as ajax :refer [GET POST]]
            [cljs-http.client :as http]))

(def MAIN_URL "http://localhost:8080/")
(def DELETE_URL (str MAIN_URL "remove/"))
(def PICTURE_URL (str MAIN_URL "picture/"))
(defn read-json [json]
  (t/read (t/reader :json) json))

(defn log [s]
  (.log js/console (str s)))

(def abc
  (fn [] (js/alert "hello")))

(defn display-one-file [picture]
  "destructuring didn't work"
  (let [picture-id (get picture 0)
        picture-name (get picture 1)
        picture-url (get picture 2)
        picture-ratio-class (get picture 3)
        picture-pop-up-link (get picture 4)]
    [:div {:class picture-ratio-class}
     [:a.fancybox {:title picture-name
                   :href  picture-pop-up-link}
      [:img {:src picture-url}]
      ]
     [:div.file-fullscreen {:id    picture-pop-up-link
                            :style "display: none;"}
      [:img {:src picture-url}]
      ]

     [:div {:id (str "delete_" picture-id)}
      [:button.close {:type  "submit"
                      :title "Delete file"}
       "&times;"]
      ]
     ]
    ))


(defn map-to-list-of-picture-id-name-url-ratioclass [filelist]
  (conj []
        (get filelist "id")
        (get filelist "name")
        (str PICTURE_URL (get filelist "id"))
        (if (> (get filelist "ratio") 1.45)
          "file bigfile"
          "file")
        (str "show_popup_link_" (get filelist "id"))
        ))

(defn make-inner-htmls [files]
  (map display-one-file (doall (map map-to-list-of-picture-id-name-url-ratioclass (read-json files)))))

(defn display-files [files]
  [:div
   [:div.heading [:h3 "List of Files"]]
   [:div.file-container
    (make-inner-htmls files)
    ]
   ])


(def upload-form
  [:form#fileForm {:method :post
                   :enctype "multipart/form-data"}
   [:div.heading [:h3 "Upload new files"]]
   [:div.upload-block
    [:div.upload-block__item.upload-block__item--big
     [:input#file.inherit-width.upload-item-input {:type     "file"
                                                   :multiple true
                                                   :accept   "image/*"
                                                   :require  true}]
     ]
    [:div.upload-block__item
     [:input.inherit-width {:type  "submit"
                            :value "Add file"}]
     ]
    [:div.upload-block__item.upload-block__item--big.upload-block__note
     "Big images will be resized proportionally to 1600px width or 900px height"
     ]
    [:div.upload-block__item]
    ]
   ]
  )
