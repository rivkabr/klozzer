klozzer
=======

`klozzer` is a `clojurescript` library that provides:
* caching for files retrieved from the net.
* `core.async` wrapping of the [File System Api](https://developer.mozilla.org/en-US/docs/Web/API/LocalFileSystem)

It relies on the [File System Api](https://developer.mozilla.org/en-US/docs/Web/API/LocalFileSystem) therefore it will work only on Chrome. It might be interesting to combine it with [idb.filesystem.js](https://github.com/ebidel/idb.filesystem.js) in order to make it available on other browsers.

Usage
=====

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


TODO
====
* Combine with [idb.filesystem.js](https://github.com/ebidel/idb.filesystem.js) in order to make it available on other browsers
