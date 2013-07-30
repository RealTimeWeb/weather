#lang racket

; Provide the external structs
(provide (struct-out weather))
(provide (struct-out forecast))
(provide (struct-out location))
(provide (struct-out report))

; Provide the external functions
(provide get-report)
(provide get-report/string)
(provide get-report/json)
(provide disconnect-weather-service)
(provide connect-weather-service)

; Load the internal libraries
(require net/url)
(require srfi/19)
(require srfi/6)
(require racket/port)
(require json)
(require net/uri-codec)

; Define the structs
(define-struct weather (temp dewpoint humidity wind-speed wind-direction description image-url visibility windchill pressure))

(define-struct forecast (period-name period-time temperature-label temperature probability-of-precipitation description image-url long-description))

(define-struct location (latitude longitude elavation name))

(define-struct report (weather forecasts location))

(define (json->weather jdata)
	(make-weather (string->number (hash-ref jdata 'Temp))
			(string->number (hash-ref jdata 'Dewp))
			(string->number (hash-ref jdata 'Relh))
			(string->number (hash-ref jdata 'Winds))
			(string->number (hash-ref jdata 'Windd))
			(hash-ref jdata 'Weather)
			(hash-ref jdata 'Weatherimage)
			(string->number (hash-ref jdata 'Visibility))
			(string->number (hash-ref jdata 'WindChill))
			(string->number (hash-ref jdata 'SLP))))

(define (json->forecast jdata)
	(map make-forecast (hash-ref (hash-ref jdata 'time) 'startPeriodName)
			(hash-ref (hash-ref jdata 'time) 'startValidTime)
			(hash-ref (hash-ref jdata 'time) 'tempLabel)
			(map string->number (hash-ref (hash-ref jdata 'data) 'temperature))
			(map (lambda (x) (if (equal? x 'null) 0 (string->number x))) (hash-ref (hash-ref jdata 'data) 'pop))
			(hash-ref (hash-ref jdata 'data) 'weather)
			(hash-ref (hash-ref jdata 'data) 'iconLink)
			(hash-ref (hash-ref jdata 'data) 'text)))

(define (json->location jdata)
	(make-location (string->number (hash-ref jdata 'latitude))
			(string->number (hash-ref jdata 'longitude))
			(string->number (hash-ref jdata 'elevation))
			(hash-ref jdata 'areaDescription)))

(define (json->report jdata)
	(make-report (json->weather (hash-ref jdata 'currentobservation))
			(json->forecast jdata)
			(json->location (hash-ref jdata 'location))))


; Handle connections
(define CONNECTION true)
(define (disconnect-weather-service)
	(set! CONNECTION false))
(define (connect-weather-service)
	(set! CONNECTION true))

; Build Client Store
(define CLIENT_STORE (read-json (open-input-file "cache.json")))

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
		(hash-ref CLIENT_STORE (hash-request url index-data) "")))

; Define the services, and their helpers
(define (get-report latitude longitude)
  (local [(define data (get-report/json latitude longitude))]
    (if (equal? data 'null)
        #f
	(json->report data))))

(define (get-report/json latitude longitude)
  (local [(define data (get-report/string latitude longitude))]
    (if (string=? (substring data 0 9) "<!DOCTYPE")
        'null
	(string->jsexpr data))))

(define (get-report/string latitude longitude)
	(get->json (string-append "http://forecast.weather.gov/MapClick.php") 
	 	(list (cons 'lat (real->decimal-string latitude)) (cons 'lon (real->decimal-string longitude)) (cons 'FcstType "json")) 
	 	(list (cons 'lat (real->decimal-string latitude)) (cons 'lon (real->decimal-string longitude)))))

