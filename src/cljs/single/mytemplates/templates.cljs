(ns single.mytemplates.templates
  (:use [jayq.core :only [$ prevent document-ready]])
  (:require [cognitect.transit :as t]
            [ajax.core :as ajax :refer [GET POST]]
            [cljs-http.client :as http]))

(def MAIN_URL "http://localhost:8080/")
(def PICTURE_URL (str MAIN_URL "picture/"))

(defn read-json [json]
  (t/read (t/reader :json) json))

(defn log [s]
  (.log js/console s))

(defn display-one-file [picture]
  "destructuring didn't work"
  (let [picture-id (get picture 0)
        picture-name (get picture 1)
        picture-url (get picture 2)
        picture-ratio-class (get picture 3)
        picture-pop-up-link (get picture 4)
        picture-delete-class (get picture 5)
        ]
    [:div {:class picture-ratio-class}
     [:a.fancybox {:title picture-name
                   :href  picture-url}                      ;picture-pop-up-link}
      [:img {:src picture-url}]
      ]
     ;;;;fancybox... trying in vain
     ;[:div.file-fullscreen {:id    picture-pop-up-link
     ;                       :style "display: none;"}
     ; [:img {:src picture-url}]
     ; ]

     [:div
      [:button.close {:id    picture-delete-class
                      :type  "submit"
                      :title "Delete file"}
       "&times;"]
      ]
     ]
    ))


(defn map-to-dto-list [filelist]
  (conj []
        (get filelist "id")
        (get filelist "name")
        (str PICTURE_URL (get filelist "id"))
        (if (> (get filelist "ratio") 1.45)
          "file bigfile"
          "file")
        (str "show_popup_link_" (get filelist "id"))
        (str "delete_" (get filelist "id"))
        ))

(defn make-inner-htmls [files]
  (map display-one-file (doall (map map-to-dto-list (read-json files)))))

(defn display-files [files]
  [:div
   [:div.heading [:h3 "List of Files"]]
   [:div.file-container
    (make-inner-htmls files)
    ]
   ])



