(ns desktop-pictures.core
  (:require [clj-http.client   :as client]
            [clojure.data.json :as json]
            [clojure.java.io   :as io])
  (:gen-class))

(def resolution
  (let [size (.getScreenSize (java.awt.Toolkit/getDefaultToolkit))]
    {:width (.width size) :height (.height size)}))

(defn get-json [url, headers]
  (let [res (client/get url {:headers headers})]
    (and (= (:status res) 200)
         (get-in (json/read-str (:body res)) ["urls" "raw"]))))

(defn img-url [query]
  (get-json (format "https://api.unsplash.com/photos/random?query=%s&orientation=landscape" query)
            {"Authorization" "Client-ID f68d033e10a3261de3de11fddd37b39e64268b263809578a1c318db003bac32c"}))

(defn cut-img [url]
  (format "%s&fit=crop&w=%s&h=%s&crop=edges" url (:width resolution) (:height resolution)))

(defn download-img [url]
  (io/copy (:body (client/get (cut-img url) {:as :stream}))
           (java.io.File. (str (.getTime (java.util.Date.)) ".jpg"))))

(defn delete-old []
  (doseq [x (filter #(clojure.string/ends-with? % ".jpg") (.list (io/file ".")))]
    (io/delete-file x)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (when-let [url (img-url (first args))]
    (do (delete-old) (download-img url))))
