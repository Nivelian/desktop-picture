(ns desktop-pictures.core
  (:require [clj-http.client :as client])
  (:gen-class))

(defn random-photo [query]
 (:body
  (client/get (format "https://api.unsplash.com/photos/random?query=%s&orientation=landscape" query)
              {:headers {"Authorization" "Client-ID f68d033e10a3261de3de11fddd37b39e64268b263809578a1c318db003bac32c"}})))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
