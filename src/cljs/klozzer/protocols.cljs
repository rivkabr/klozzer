(ns ^:shared klozzer.protocols
   (:refer-clojure :exclude [-write]))

(defprotocol IFileSystem
  (-write [this filename data] "Writes a file with data")
  (-file-entry [this filename create?] "Retrieves the fileEntry object")
  (-file [this filename] "Retrieves the file data")
  (-url [this filename] "Retrieves the url of the file")
  (-read [this filename format] "Reads a file. format can be text or arraybuffer"))
