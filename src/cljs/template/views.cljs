(ns template.views
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.core :refer [get-mui-theme color]]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [secretary.core :as secretary]
    [re-frame.core :as re]
    [reagent.core :as r]
    [template.page :as page]))

(defn records-panel [title]
  (let [records (page/page-item "records")]
  (fn []
    [:div {:style {:padding-top "80px"
                   :margin-left "20px"
                   :margin-right "20px"}}
    [:h1.title title]
    (for [[record id] (map-indexed (fn [i r] [r (str "record-" i)]) records)]
      [ui/paper {:key id
                 :style {:padding "10px" :margin-bottom "3px"}}
        [:div.container
          [:img.cover {:src (str "https://api.finna.fi" (get-in record ["images" 0]))}]
          [:h3.author
            (for [[author id] (map-indexed (fn [i a] [a (str "record-" i)]) (get record "nonPresenterAuthors"))]
              [:span {:key id} (str (get author "name") "  ")]
            )]
            ; [:a {:href (get-in record ["onlineUrls" 0 "url"]) :target "_blank"}
            [:a.title {:href (str "https://vaski.finna.fi/Record/" (get record "id")) :target "_blank"}
              [:h2 (get record "title")]]
      ]])
      ])))

(defn empty-panel []
  [:div {:style {:padding-top "80px"
                 :margin-left "20px"
                 :margin-right "20px"}}
    [:h1 "Haetaan tietoja ..."]])

(defn home-panel []
  (records-panel "Kaikki uutuudet"))

(defn ekirjat-panel []
 (records-panel "E-kirjat"))

(defn sarjakuvat-panel []
 (records-panel "Sarjakuvat"))

(defn jannitys-panel []
  (records-panel "Jännitys"))

(defn spefi-panel []
 (records-panel "Spefi"))

(defmulti panels identity)
(defmethod panels :empty-panel [] [empty-panel])
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :ekirjat-panel [] [ekirjat-panel])
(defmethod panels :sarjakuvat-panel [] [sarjakuvat-panel])
(defmethod panels :jannitys-panel [] [jannitys-panel])
(defmethod panels :spefi-panel [] [spefi-panel])
(defmethod panels :default [] [home-panel])


(defn main-panel []
  (let [name (re/subscribe [:name])
        drawer-open (re/subscribe [:drawer-open])
        active-panel (re/subscribe [:active-panel])]
    (fn []
      [ui/mui-theme-provider
        {:mui-theme (get-mui-theme
          {:palette {:text-color "#7A7478"}})}
        [:div
          [ui/app-bar { :title "Title"
                        :style {:position "fixed" :text-transform "uppercase"}
                        :icon-element-right
                          (r/as-element [ui/icon-button
                            (ic/action-account-balance-wallet)])
                        :on-left-icon-button-touch-tap
                          #(re/dispatch [:open-drawer])}]
          [ui/drawer
            {:docked            false
             :open              (if @drawer-open true false)
             :on-request-change #(re/dispatch [:close-drawer])}
            [ui/menu-item {:primary-text "Home"
                           :href "#/"}]
            [ui/menu-item {:primary-text "E-kirjat"
                           :href "#/ekirjat"}]
            [ui/menu-item {:primary-text "Sarjakuvat"
                           :href "#/sarjakuvat"}]
            [ui/menu-item {:primary-text "Jännitys"
                           :href "#/jannitys"}]
            [ui/menu-item {:primary-text "Spefi"
                           :href "#/spefi"}]]
          [ui/paper
            (panels @active-panel)]]])))
