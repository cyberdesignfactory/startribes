(ns hello-test
  (:require [clojure.test :refer [deftest is]]
            [hello]))

(deftest greet-test

  (is (= "Hello, Rob" (hello/greet "Rob"))))


