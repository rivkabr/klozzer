(ns klozzer.net
  (:use-macros [cljs.core.async.macros :only [go go-loop]]
               [purnam.core :only [? ! !>]])
  (:require [cljs.core.async :refer [<! close! chan put! >!]]
            [klozzer.core :refer [new-storage]]
            [cljs-time.core :as time-core]
            [cljs-time.coerce :as time-coerce]
            [clojure.string :as string]
            [klozzer.protocols :refer [IFileSystem -write -read -file]]))

(def fs (atom nil))

(defn init-fs "must be called before anything" [size-in-mb] 
  (go 
    (let [[status data] (<! (new-storage size-in-mb))]
      (if (= :error status)
        [status data]
        [:ok (reset! fs data)]))))

(defn get-headers [url]
  (let [c (chan)]
    (let [xhr (js/XMLHttpRequest.)]
      (!> xhr.open "HEAD" url true)
      (! xhr.onreadystatechange (fn [e]
                                (put! c 
                                      {"Last-Modified" (!> xhr.getResponseHeader "Last-Modified")})))
      (!> xhr.send))
    c))

(defn get-url [url response-type]
  (let [c (chan)
        xhr (js/XMLHttpRequest.)]
    (!> xhr.open "GET" url true)
    (! xhr.responseType response-type)
    (! xhr.onload (fn [e]
                    (if (<= 200 (? xhr.status) 300)
                      (put! c [:ok (? xhr.response)])
                      (put! c [:error (? xhr.status)]))))
    (!> xhr.send)
    c))

(def last-modified-dates (atom {}))

(defn get-last-modified-date [url url-for-timestamp]
  (go
    (if-let [last-modified-date (@last-modified-dates (or url-for-timestamp url))]
      last-modified-date
      (if-let [headers (<! (get-headers url))]
        (let [date (js/Date. (headers "Last-Modified"))]
          (swap! last-modified-dates assoc url-for-timestamp date)
          date)
        ""))))

(defn after-js-date? [& args]
  (apply time-core/after? (map time-coerce/from-date args)))

(defn local-file-up-to-date? [key url url-for-timestamp]
  (go
    (if-let [my-file (<! (-file @fs key))]
      (after-js-date? (? my-file.lastModifiedDate) (<! (get-last-modified-date url url-for-timestamp)))
      false)))
   
(defn local-file-when-up-to-date [key response-type url url-for-timestamp]
  (go
    (if (nil? @fs) nil
      (if (<! (local-file-up-to-date? key url url-for-timestamp))
        (<! (-read @fs key response-type))
        nil))))

(defn get-cached-url [url key response-type]
  (let [[url-for-get url-for-head url-for-timestamp] (if (seqable? url) url [url url])
        key (string/replace (str key) "/" ".")]
    (go
      (if-let [my-data (<! (local-file-when-up-to-date key response-type url-for-head url-for-timestamp))]
          [:ok my-data]
          (let [[status data] (<! (get-url url-for-get response-type))]
            (if (= :error status)
                [:error data]
                (do (when @fs
                      (-write @fs key data))
                    [:ok data])))))))

(defn get-several-files [get-single-file-func coll]
  (go-loop [res {} channels (map get-single-file-func coll)]
           (if (empty? channels)
             res
             (let [[[file data] c] (alts! channels)]
               (recur (assoc res file data) (remove #{c} channels))))))
