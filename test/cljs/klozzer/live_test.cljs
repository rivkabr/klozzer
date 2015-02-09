(ns test.live4
  (:use-macros [cljs.core.async.macros :only [go go-loop]])
  (:require [klozzer.net :as net]))

(js/alert 3)


(enable-console-print!)


(net/init-fs 10000)
(go
   (println (<! (net/get-cached-url "http://localhost:4000" "google" "text"))))



