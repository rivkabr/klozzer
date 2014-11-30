# klozzer

`klozzer` is a `clojurescript` library that provides:
* caching for files retrieved from the net.
* `core.async` wrapping of the [File System Api](https://developer.mozilla.org/en-US/docs/Web/API/LocalFileSystem)

It relies on the [File System Api](https://developer.mozilla.org/en-US/docs/Web/API/LocalFileSystem) therefore it will work only on Chrome. It might be interesting to combine it with [idb.filesystem.js](https://github.com/ebidel/idb.filesystem.js) in order to make it available on other browsers.

## Usage

The low-level layer is basically a wrapper of the `File System API`:

```
(defprotocol IFileSystem
  (-write [this filename data] "Writes a file with data")
  (-file-entry [this filename create?] "Retrieves the fileEntry object")
  (-file [this filename] "Retrieves the file data")
  (-url [this filename] "Retrieves the url of the file")
  (-read [this filename format] "Reads a file. format can be text or arraybuffer"))
```

There is a higher level layer that provides caching:

* get-cached-url

## Develompment
The project was built using [cljs-start](https://github.com/magomimmo/cljs-start). 


### Installing PhantomJS

[Phantomjs][6] is a headless-browser based on [WebKit][10] used mainly
for JS testing.

    [Download the version][11] for your operating system and follow the
    corresponding instruction.

### Run the dummy tests

```bash
lein test
Compiling ClojureScript.

lein test user

Ran 0 tests containing 0 assertions.
0 failures, 0 errors.
Running ClojureScript test: phantomjs

Testing klozzer.core-test

Ran 1 tests containing 2 assertions.
Testing complete: 0 failures, 0 errors.

Ran 1 tests containing 2 assertions.
Testing complete: 0 failures, 0 errors.
```

### Run the CLJ nREPL

```clj
lein repl
Compiling ClojureScript.
nREPL server started on port 49893 on host 127.0.0.1 - nrepl://127.0.0.1:49893
REPL-y 0.3.5, nREPL 0.2.6
Clojure 1.6.0
Java HotSpot(TM) 64-Bit Server VM 1.7.0_45-b18
    Docs: (doc function-name-here)
              (find-doc "part-of-name-here")
                Source: (source function-name-here)
                 Javadoc: (javadoc java-object-or-class-here)
                     Exit: Control+D or (exit) or (quit)
                      Results: Stored in vars *1, *2, *3, an exception in *e

                      user=>
                      ```

### Run the HTTP server

```clj
user=> (run)
2014-11-07 17:28:19.917:INFO:oejs.Server:jetty-7.6.13.v20130916
2014-11-07 17:28:20.150:INFO:oejs.AbstractConnector:Started SelectChannelConnector@0.0.0.0:3000
#<Server org.eclipse.jetty.server.Server@8bcddb0>
user=>
```

### Run the bREPL from the nREPL

From the active REPL evaluate the following expressions:

```clj
user=> (browser-repl)
Browser-REPL ready @ http://localhost:53659/6909/repl/start
Type `:cljs/quit` to stop the ClojureScript REPL
nil
cljs.user=>
```

### Connect the bREPL with the browser

After having run the HTTP server and the bREPL, Just open
[localhost:3000][12] in a browser, wait a moment and go back to the bREPL to
interact with the browser frow the REPL.

```clj
cljs.user=> (js/alert "Hello, ClojureScript!")
nil
cljs.user=> (ns cljs.user (:use [klozzer.core :only [foo]]))
nil
cljs.user=> (foo "Welcome to ")
"Welcome to ClojureScript!"
cljs.user=>
```

### Run the included dummy unit tests from the bREPL and exit the bREPL

```clj
cljs.user=> (cemerick.cljs.test/run-all-tests)

Testing klozzer.core-test

Ran 1 tests containing 2 assertions.
Testing complete: 0 failures, 0 errors.
{:test 1, :pass 2, :fail 0, :error 0}
Ran 1 tests containing 2 assertions.
Testing complete: 0 failures, 0 errors.

cljs.user=>
```

### Stop and restart the HTTP server

```clj
cljs.user=> :cljs/quit
:cljs/quit
user=> (.stop http/server)
nil
user=> (.start http/server)
2014-11-07 17:33:16.199:INFO:oejs.Server:jetty-7.6.13.v20130916
2014-11-07 17:33:16.200:INFO:oejs.AbstractConnector:Started SelectChannelConnector@0.0.0.0:3000
nil
user=>
```

### Exit

```clj
user=> exit
Bye for now!
```

### Package the jar and see its content

```bash
lein jar
Compiling ClojureScript.
Created /Users/mimmo/tmp/klozzer/target/klozzer-0.0.1-SNAPSHOT.jar

jar tvf target/klozzer-0.0.1-SNAPSHOT.jar
    92 Thu Oct 31 22:24:36 CET 2013 META-INF/MANIFEST.MF
      4461 Thu Oct 31 22:24:36 CET 2013 META-INF/maven/klozzer/klozzer/pom.xml
         111 Thu Oct 31 22:24:36 CET 2013 META-INF/maven/klozzer/klozzer/pom.properties
           2131 Thu Oct 31 22:24:36 CET 2013 META-INF/leiningen/klozzer/klozzer/project.clj
             2131 Thu Oct 31 22:24:36 CET 2013 project.clj
                241 Thu Oct 31 22:24:36 CET 2013 META-INF/leiningen/klozzer/klozzer/README.md
                 11220 Thu Oct 31 22:24:36 CET 2013 META-INF/leiningen/klozzer/klozzer/LICENSE
                      0 Thu Oct 31 21:24:32 CET 2013 wonderful_lib/
                         174 Thu Oct 31 21:24:32 CET 2013 wonderful_lib/core.cljs
                         ```

### Debugging CLJS code in the browser

> DISCLAIMER: I'm still working to understand what is the best
> development workflow with Chrome Development Tools. At the moment the
> following is a minimal workflow for debugging CLJS code by using the
> recently added *source map* feature to the CLJS compiler.

#### Enable JS source map in Chrome

* Open the Chrome Developer Tools
* Click the Setting icon at the very bottom right of the Development
  Tools Window
  * Flag the `Enable JS source map` option in the `Source` section
  * Close the Setting Window

  Now compile the whitespace build in auto mode:

  ```bash
  lein do clean, cljsbuild auto
  Deleting files generated by lein-cljsbuild.
  Compiling ClojureScript.
  Compiling "dev-resources/public/js/klozzer.js" from ("src/cljs" "test/cljs" "dev-resources/tools/repl")...
  Successfully compiled "dev-resources/public/js/klozzer.js" in 17.986431 seconds.
  ```

  Then:

  * open the `~/Developer/klozzer/dev-resources/public/index.html`
    page in Chrome
    * click the `Sources` tab from the Developer Tools Window
    * type `Cmd+O` (or the corresponding keys chord of your operating
      system)
    * type `js/core` in the search field
    * select the first match from the result set
      (i.e. `...js/klozzer/core.cljs`). The source code of the
        `core.cljs` will be displayed in the a new tab.

        Now, in the `core.cljs` click the line number for which you want to
        set a breakpoint and just reload the `index.html` page.

        Unfortunately, the very first time you reload the page, the debugger
        closes the `core.cljs` file and opens the `klozzer.js` file
        emitted by the CLJS compiler instead.

        Just open the `core.cljs` again.

        Now you can use the standard debugging commands from the Chrome
        debugger pane on the right of the Developer Tools Window to debug your
        CLJS code.

        You can even modify the source code inside the Developer Tools and
        then save the changes by right clicking the `core.cljs` pane. The
        `cljsbuild auto whitespace` running task will recompile it and the
        source map will be updated as well.

        Not too bad!

### Simple and advanced compilation modes and tests

The `cljs-start` lein-template generates the `:simple` and `:advanced`
CLJS compilation modes as well by defining the corresponding `:simple`
and `:advanced` lein profiles in the `profiles.clj` file.

It also defines the corresponding tests commands which exercise the
CLJS unit tests against the PhantomJS engine.

### Activate and test the simple compilation modes

To activate and test the `:simple` compilation mode:

```bash
lein with-profiles simple do clean, compile, test
Deleting files generated by lein-cljsbuild.
Compiling ClojureScript.
Compiling "dev-resources/public/js/klozzer.js" from ("src/cljs" "test/cljs")...
Successfully compiled "dev-resources/public/js/klozzer.js" in 16.526333 seconds.
Compiling ClojureScript.

lein test user

Ran 0 tests containing 0 assertions.
0 failures, 0 errors.
Running ClojureScript test: phantomjs

Testing klozzer.core-test

Ran 1 tests containing 2 assertions.
0 failures, 0 errors.
{:test 1, :pass 2, :fail 0, :error 0, :type :summary}
```

To activate and test the `:advanced` compilation mode:

```bash
lein with-profiles advanced do clean, compile, test
Deleting files generated by lein-cljsbuild.
Compiling ClojureScript.
Compiling "dev-resources/public/js/klozzer.js" from ("src/cljs" "test/cljs" "test/cljs")...
Successfully compiled "dev-resources/public/js/klozzer.js" in 14.142855 seconds.
Compiling ClojureScript.

lein test user

Ran 0 tests containing 0 assertions.
0 failures, 0 errors.
Running ClojureScript test: phantomjs

Testing klozzer.core-test

Ran 1 tests containing 2 assertions.
0 failures, 0 errors.
{:test 1, :pass 2, :fail 0, :error 0, :type :summary}
```


## TODO
* Combine with [idb.filesystem.js](https://github.com/ebidel/idb.filesystem.js) in order to make it available on other browsers
