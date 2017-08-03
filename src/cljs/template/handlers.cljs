(ns template.handlers
  (:require [re-frame.core :as re]))

(defn drawer-state [state {:keys [db]} _]
  {:db (assoc db :drawer-open state)})

(defn set-active-panel [{:keys [db]} [_ active-panel]]
  {:db (assoc db :active-panel active-panel)})

(defn register []
  (re/reg-event-fx :set-active-panel set-active-panel)
  (re/reg-event-fx :close-drawer (partial drawer-state false))
  (re/reg-event-fx :open-drawer (partial drawer-state true)))
