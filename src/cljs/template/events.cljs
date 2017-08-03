(ns template.events
  (:require [re-frame.core :as re]
            [template.db :as db]))

(re/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))
