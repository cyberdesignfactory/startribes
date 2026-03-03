(ns components.ui.header)

(defn source-code-link []
  [:a.btn.btn-primary.mb-2
   {:href "https://github.com/cyberdesignfactory/startribes"
    :style {:text-decoration :none}
    :target :blank} "Source Code"])

(defn round2dps [number]
  (/
   (int (* number 100.0))
   100.0))

(defn header [{:keys [ style time-left ship-fbs show-menu-button? on-menu-clicked]}]
  [:div.text-center.bg-primary.pt-4.pb-1
   {:style style}
    (when show-menu-button?
      [:div
       {:style
        {:position :absolute
         :left "16px"
         :top "16px"}}

       [:button.btn
        {:class "bg-primary"
         :on-click #(on-menu-clicked)}
        [:i.bi.bi-list.fs-1.fs-large {:class "text-secondary"}]]

       ]

      )

   ;; [:div.row
   ;;  [:div.col-1
   ;;   (when show-menu-button?
   ;;     [:button.btn
   ;;      {:class "bg-primary"
   ;;       :on-click #(on-menu-clicked)}
   ;;      [:i.bi.bi-list.fs-1.fs-large {:class "text-secondary"}]])]

   ;;  [:div.col-10

     [:h2.mb-0.text-secondary "Star Tribes"]
     [source-code-link]
     (if time-left
       [:h4.text-secondary (str (int time-left))])

     (if ship-fbs
       [:h4.text-secondary (str ship-fbs)])

   ;; ]

   ;;  [:div.col-1

   ;;   ]
   ;;  ]

   ])

