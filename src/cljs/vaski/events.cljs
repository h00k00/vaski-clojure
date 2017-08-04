(ns vaski.events
  (:require [re-frame.core :as re]
            [vaski.db :as db]))

(re/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))
