(ns single.mytemplates.upload-form)

(def upload-form
  [:form#fileForm {:method  :post
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
