#lang racket

;; this module provides functions and structures for extracting reports from forecast.weather.gov 

;; ---------------------------------------------------------------------------------------------------
;; interface

(provide
 ;; type Longitude 
 ;; type Latitude 
 
 (struct-out report)
 ;; Report = ... 
 
 (struct-out weather)
 ;; Weather = ... 
 
 (struct-out forecast)
 ;; Forecast = ... 
 
 (struct-out location)
 ;; Location = ...
 
 (struct-out wind)
 ;; Wind = ...
 
 )

;; ---------------------------------------------------------------------------------------------------
;; implementation: public 

; Provide the external functions
;(provide get-report)
;(provide get-report/city)
;(provide get-report/string)
;(provide get-report/json)
;(provide disconnect-weather-service)
;(provide connect-weather-service)
(provide get-weather)

; Load the internal libraries
(require net/url)
(require srfi/19)
(require srfi/6)
(require racket/port)
(require json)
(require net/uri-codec)
(require htdp/error)

; Define the structs
(define-struct weather (temperature dewpoint humidity visibility pressure wind description image-url) #:transparent)

(define-struct wind (speed direction chill) #:transparent)

(define-struct forecast (temperature-label temperature probability-of-precipitation image-url description long-description) #:transparent)

(define-struct location (latitude longitude elevation name) #:transparent)

(define-struct report (current tonight tomorrow day-after location) #:transparent)

(define (json->weather jdata)
  (make-weather (string->number (hash-ref jdata 'Temp))
                (string->number (hash-ref jdata 'Dewp))
                (string->number (hash-ref jdata 'Relh))
                (string->number (hash-ref jdata 'Visibility))
                (string->number (hash-ref jdata 'SLP))
                (json->wind jdata)
                (hash-ref jdata 'Weather)
                (string-append "http://forecast.weather.gov/images/wtf/medium/" (hash-ref jdata 'Weatherimage))))

(define (json->wind jdata)
  (make-wind (string->number (hash-ref jdata 'Winds))
             (string->number (hash-ref jdata 'Windd))
             (string->number (hash-ref jdata 'WindChill))))

(define (json->forecast jdata indexer)
  (make-forecast (indexer (hash-ref (hash-ref jdata 'time) 'tempLabel))
                 (string->number (indexer (hash-ref (hash-ref jdata 'data) 'temperature)))
                 ; ABUSE THE LAMBDA!
                 ((lambda (x) (if (equal? (indexer x) 'null) 0 (string->number (indexer x)))) (hash-ref (hash-ref jdata 'data) 'pop))
                 (indexer (hash-ref (hash-ref jdata 'data) 'iconLink))
                 (indexer (hash-ref (hash-ref jdata 'data) 'weather))
                 (indexer (hash-ref (hash-ref jdata 'data) 'text))))

(define (json->location jdata)
  (make-location (string->number (hash-ref jdata 'latitude))
                 (string->number (hash-ref jdata 'longitude))
                 (string->number (hash-ref jdata 'elevation))
                 (hash-ref jdata 'areaDescription)))

(define (json->report jdata)
  (local [(define day? (string=? "High" (first (hash-ref (hash-ref jdata 'time) 'tempLabel))))]
    (make-report (json->weather (hash-ref jdata 'currentobservation))
                 (json->forecast jdata (if day? second first))
                 (json->forecast jdata (if day? third second))
                 (json->forecast jdata (if day? fifth fourth))
                 (json->location (hash-ref jdata 'location)))))

(define (json->geocoded-location jdata)
  (begin
    (define location (hash-ref (hash-ref (first (hash-ref jdata 'results)) 'geometry) 'location))
    (make-location (hash-ref location 'lat)
                   (hash-ref location 'lng)
                   0
                   (hash-ref (first (hash-ref jdata 'results)) 'formatted_address)
                   )))


(define (make-null-report)
  (make-report (make-weather 0 0 0 0 0 (make-wind 0 0 0) "" "")
               (make-forecast "" 0 0 "" "" "")
               (make-forecast "" 0 0 "" "" "")
               (make-forecast "" 0 0 "" "" "")
               (make-location 0 0 0 "")))

; Handle connections
(define CONNECTION true)
(define (disconnect-weather-service)
  (set! CONNECTION false))
(define (connect-weather-service)
  (set! CONNECTION true))

; Build Client Store
(define CLIENT_STORE #F) ;(read-json (open-input-file "cache.json")))

(define (boolean->string a-boolean)
  (if a-boolean
      "true"
      "false"))
(define (string->boolean a-string)
  (string=? a-string "true"))
(define (key-value pair)
  (string-append (symbol->string (car pair)) "=" (cdr pair)))
(define (convert-post-args data)
  (string->bytes/utf-8 (alist->form-urlencoded data)))
(define (convert-get-args url data)
  (string-append url "?" (string-join (map key-value data) "&")))
(define (hash-request url data)
  (string-append url "%{" (string-join (map key-value data) "}%{") "}"))
(define (post->json url full-data index-data)
  (if CONNECTION
      (port->string (post-pure-port (string->url url) (convert-post-args full-data)))
      (hash-ref CLIENT_STORE (hash-request url index-data) "")))
(define (get->json url full-data index-data)
  (if CONNECTION
      (port->string (get-pure-port (string->url (convert-get-args url full-data))))
      (hash-ref CLIENT_STORE (string->symbol (hash-request url index-data)) "")))

; Define the services, and their helpers
(define (get-weather address)
  (check-arg 'get-weather (string? address) 'string 1 address)
  (get-report/city address))

(define (geocode address)
  (define data (geocode/json address))
  (define status (if (eof-object? data) "UNAVAILABLE" (hash-ref data 'status )))
  (cond [(string=? "OK" status) (json->geocoded-location data)]
        [(string=? "REQUEST_DENIED" status) "get-weather: the given address was denied. Please copy the address you used and send it to acbart@vt.edu."]
        [(string=? "ZERO_RESULTS" status) "get-weather: the given address could not be found."]
        [(string=? "OVER_QUERY_LIMIT" status) "get-weather: the service has been used too many times today."]
        [(string=? "INVALID_REQUEST" status) "get-weather: the given address was invalid."]
        [(string=? "UNKNOWN_ERROR" status) "get-weather: a temporary error occurred; please try again."]
        [(string=? "UNAVAILABLE" status) "get-weather: the given address is not available offline."]
        [else status]))


(define (geocode/json address)
  (local [(define data (geocode/string address))]
    (string->jsexpr data)))

(define (geocode/string address)
  (get->json (string-append "http://maps.googleapis.com/maps/api/geocode/json")
             (list (cons 'address address) (cons 'sensor "true"))
             (list (cons 'address address))))

(define (get-report latitude longitude)
  (local [(define data (get-report/json latitude longitude))]
    (cond [(equal? data 'null)
           (error "get-weather: the given address was not inside the United States.")]
          [(eof-object? data) (make-null-report)]
          [else (json->report data)])))

(define (get-report/city address)
  (local [(define coordinate-data (geocode address))]
    (if (string? coordinate-data) coordinate-data
        (get-report (location-latitude coordinate-data) (location-longitude coordinate-data)))))

(define (get-report/json latitude longitude)
  (local [(define data (get-report/string latitude longitude))]
    (if (or (and (>= (string-length data) 9) 
                 (string=? (substring data 0 9) "<!DOCTYPE"))
            (and (>= (string-length data) 7)
                 (string=? (substring data 0 7) "<script")))
        'null
        (string->jsexpr data))))

(define (get-report/string latitude longitude)
  (get->json (string-append "http://forecast.weather.gov/MapClick.php") 
             (list (cons 'lat (real->decimal-string latitude)) (cons 'lon (real->decimal-string longitude)) (cons 'FcstType "json")) 
             (list (cons 'lat (real->decimal-string latitude)) (cons 'lon (real->decimal-string longitude)))))



(define (geocode->key address)
  (hash-request "http://maps.googleapis.com/maps/api/geocode/json"
                (list (cons 'address (string-downcase address)))))
(define (report->key latitude longitude)
  (hash-request "http://forecast.weather.gov/MapClick.php"
                (list (cons 'lat (real->decimal-string latitude)) (cons 'lon (real->decimal-string longitude)))))
(define (generate-cache addresses)
  (local
    [(define new-cache (make-hash))]
    (map (lambda (address)
           (local [(define loc (geocode address))
                   (define lat (location-latitude loc))
                   (define lng (location-longitude loc))]
             (begin
               (hash-set! new-cache 
                          (string->symbol (report->key lat lng))
                          (get-report/string lat lng))
               (hash-set! new-cache 
                          (string->symbol (geocode->key address))
                          (geocode/string address)))
             ))
         addresses)
    new-cache))

(define (store-cache filename inputs)
  (begin 
    (define out (open-output-file filename #:mode 'binary #:exists 'replace))
    (write-json (generate-cache inputs)
                out)
    (close-output-port out)))