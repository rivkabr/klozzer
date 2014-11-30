(defproject viebel/klozzer "0.3.0"
  :description "File caching and File System Browser API with core.async"
  :url "https://github.com/viebel/klozzer"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}

  :min-lein-version "2.3.4"

  ;; We need to add src/cljs too, because cljsbuild does not add its
  ;; source-paths to the project source-paths
  :source-paths ["src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.andrewmcveigh/cljs-time "0.1.6"]
                 [org.clojure/clojurescript "0.0-2202"]
                 [im.chit/purnam "0.4.3"]
                 [org.clojure/core.async "0.1.278.0-76b25b-alpha"]
                 [org.clojure/clojurescript "0.0-2371"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :hooks [leiningen.cljsbuild]

  :cljsbuild
  {:builds {;; This build is only used for including any cljs source
            ;; in the packaged jar when you issue lein jar command and
            ;; any other command that depends on it
            :klozzer
            {:source-paths ["src/cljs" "src/clj"]
             ;; The :jar true option is not needed to include the CLJS
             ;; sources in the packaged jar. This is because we added
             ;; the CLJS source codebase to the Leiningen
             ;; :source-paths
             ;:jar true
             ;; Compilation Options
             :compiler
             {:output-to "dev-resources/public/js/klozzer.js"
              :optimizations :simple
              :pretty-print true}}}})
