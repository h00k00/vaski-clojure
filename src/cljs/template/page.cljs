(ns template.page
  (:require
    [ajax.core :as a]
    [ajax.edn :as edn]
    [re-frame.core :as re]))

(defonce page-content (atom {}))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "Virhe sisällön latauksessa: " status " " status-text)))

(defn load-content [link panel]
  (a/GET link
            {:response-format (a/json-response-format)
            :error-handler error-handler
            :handler    (fn [response]
                          (js/console.log response)
                          (reset! page-content response)
                          (re/dispatch [:set-active-panel panel]))}))

(defn page-item [key]
  (get @page-content key))
