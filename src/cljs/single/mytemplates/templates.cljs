(ns single.mytemplates.templates
  (:require [cognitect.transit :as t]))

(def PICTURE_URL "http://localhost:8080/picture/")
(defn read-json [json]
  (t/read (t/reader :json) json))

(defn display-one-file [picture]
  "destructuring didn't work"
  (let [picture-name (get picture 0)
        picture-url (get picture 1)
        picture-ratio-class (get picture 2)
        picture-pop-up-link (get picture 3)]
    [:div {:class picture-ratio-class}
     [:a.fancybox {:title picture-name
                   :href  picture-pop-up-link}
      [:img {:src picture-url}]
      ]
     [:div.file-fullscreen {:id picture-pop-up-link
                            :style "display: none;"}
      [:img {:src picture-url}]
      ]

     [:form
      [:button.close {:type  "submit"
                      :title "Delete file"}
       "&times;"]
      ]
     ]
    ))


(defn map-to-list-of-picture-name-url-ratioclass [x]
  (conj []
        (get x "name")
        (str PICTURE_URL (get x "id"))
        (if (> (get x "ratio") 1.45)
          "file bigfile"
          "file")
        (str "show_popup_link_" (get x "id"))
        ))

(defn make-inner-htmls [files]
  (map display-one-file (doall (map map-to-list-of-picture-name-url-ratioclass (read-json files)))))

(defn display-files [files]
  [:div
   [:div.heading [:h3 "List of Files"]]
   [:div.file-container
    (make-inner-htmls files)
    ]
   ])


(def upload-form
  [:form#fileForm
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
