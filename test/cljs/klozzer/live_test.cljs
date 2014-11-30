(ns test.live2
  (:use-macros [cljs.core.async.macros :only [go go-loop]])
  (:require [klozzer.net :as net]))

(enable-console-print!)


(net/init-fs 10000)
(js/alert 3)
(go
   (println (<! (net/get-cached-url ["http://localhost:3000" :google "text"]))))



