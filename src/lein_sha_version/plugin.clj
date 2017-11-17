(ns lein-sha-version.plugin
  (:use
   [clojure.java.io :only [file]]
   [leiningen.core.main :only [debug]])
  (:import
   org.eclipse.jgit.storage.file.FileRepositoryBuilder
   [org.eclipse.jgit.lib ObjectId Repository]))

(defn git-sha [{:keys [root version sha]}]
  (debug "Finding SHA for" root)
  (let [^Repository repository (.. (FileRepositoryBuilder.)
                                   (readEnvironment)
                                   (findGitDir (file root))
                                   (build))
        ^ObjectId head (.resolve repository "HEAD")]
    (if head
      (.name head)
      version)))

(defn middleware
  [project]
  (let [sha (git-sha project)]
    (assoc project :manifest { "Implementation-Version" sha })))
