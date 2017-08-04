(ns vaski.routes
  (:import goog.History)
  (:require [secretary.core :as secretary :refer-macros [defroute]]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [re-frame.core :as re]
            [vaski.db :as db]
            [vaski.page :as page]))

  (def navigation-state
    (atom [{:name "Home" :path "/"}
           {:name "E-kirjat" :path "/ekirjat"}
           {:name "Sarjakuvat" :path "/sarjakuvat"}
           {:name "Jännitys" :path "/jannitys"}
           {:name "Spefi" :path "/spefi"}
           ]))

  (defn hook-browser-navigation! []
    (doto (History.)
      (events/listen EventType/NAVIGATE
                    (fn [event] (secretary/dispatch! (.-token event))))
      (.setEnabled true)))

  (def baseUrl "https://api.finna.fi/v1/search?type=AllFields&sort=main_date_str+desc&limit=50")
  (def turku "&filter[]=~building:1/Vaski/1/")
  (def suomi "&filter[]=language:fin")
  (defn app-routes []
    (secretary/set-config! :prefix "#")

    ;; --------------------
    (defroute home "/" []
      (re/dispatch [:close-drawer])
      (re/dispatch [:set-active-panel :empty-panel])
      (page/load-content
        (str baseUrl turku suomi "&filter[]=first_indexed:[NOW-14DAYS+TO+NOW]&filter[]=~format:1/Book/Book/&filter[]=~major_genre_str_mv:fiction")
        :home-panel))

    (defroute records "/records" []
      (re/dispatch [:close-drawer]))

    (defroute ekirjat "/ekirjat" []
      (re/dispatch [:close-drawer])
      (re/dispatch [:set-active-panel :empty-panel])
      (page/load-content
        (str baseUrl "&filter[]=first_indexed:[NOW-1MONTHS+TO+NOW]&filter[]=~building:0/Vaski/&filter[]=~format:1/Book/eBook/&filter[]=~major_genre_str_mv:fiction")
        :ekirjat-panel))

    (defroute sarjakuvat "/sarjakuvat" []
      (re/dispatch [:close-drawer])
      (re/dispatch [:set-active-panel :empty-panel])
      (page/load-content
        (str baseUrl turku "&filter[]=first_indexed:[NOW-2MONTHS+TO+NOW]&lookfor=ykl+85.32+OR+ykl+85.331+OR+ykl+85.35&type=Classification")
        :sarjakuvat-panel))

    (defroute jannitys "/jannitys" []
      (re/dispatch [:close-drawer])
      (re/dispatch [:set-active-panel :empty-panel])
      (page/load-content
        (str baseUrl turku suomi "&filter[]=first_indexed:[NOW-1MONTHS+TO+NOW]&filter[]=~format:1/Book/Book/&lookfor0[]=jännityskirjallisuus+OR+rikoskirjallisuus&type=Genre")
        :jannitys-panel))

    (defroute spefi "/spefi" []
      (re/dispatch [:close-drawer])
      (re/dispatch [:set-active-panel :empty-panel])
      (page/load-content
        (str baseUrl turku suomi "&filter[]=first_indexed:[NOW-2MONTHS+TO+NOW]&filter[]=~format:1/Book/Book/&lookfor0[]=fantasiakirjallisuus+OR+tieteiskirjallisuus&type=Genre")
        :spefi-panel))
    ;; --------------------

    (hook-browser-navigation!))
