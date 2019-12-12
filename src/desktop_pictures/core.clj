(ns desktop-pictures.core
  (:require [clj-http.client   :as client]
            [clojure.data.json :as json]
            [clojure.java.io   :as io])
  (:gen-class))

(defn get-json [url, headers]
  (let [res (client/get url {:headers headers})]
    (and (= (:status res) 200)
         (get-in (json/read-str (:body res)) ["urls" "full"]))))

(defn img-url [query]
  (get-json (format "https://api.unsplash.com/photos/random?query=%s&orientation=landscape" query)
            {"Authorization" "Client-ID f68d033e10a3261de3de11fddd37b39e64268b263809578a1c318db003bac32c"}))

(defn download-img [url]
  (io/copy (:body (client/get url {:as :stream}))
           (java.io.File. (str (.getTime (java.util.Date.)) ".jpg"))))

(defn delete-old []
  (doseq [x (filter #(clojure.string/ends-with? % ".jpg") (.list (io/file ".")))]
    (io/delete-file x)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (when-let [url (img-url (first args))]
    (do (delete-old) (download-img url))))
