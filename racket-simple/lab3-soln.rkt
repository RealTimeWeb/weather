;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-advanced-reader.ss" "lang")((modname lab3-soln) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #t #t none #f ())))
; Austin Cory Bart (acbart@vt.edu)
(require 2htdp/image)
(require 2htdp/universe)

; 1.1
; 4 minutes
;; [Data Definition]
(define-struct steb (pre post size))
;; A STEB is (make-steb String String Number)
;; interp of fields: 
;; pre [String]: the text before the cursor
;; name [String]: the text after the cursor
;; price [Number]: the width of the box in pixels

;; [CONSTRUCTOR] make-steb: String String Number --> Steb
;; [SELECTORS] steb-pre: Steb --> String
;; steb-post : Steb --> String
;; steb-size : Steb --> Number
;; [PREDICATE] steb?: ANY --> Boolean
;;[Examples: you can use these in your unit tests]
(define STEB-SIZE 16)
(define STEB1 (make-steb "" "" STEB-SIZE))
(define STEB2 (make-steb "Newark," " DE" STEB-SIZE))
(define STEB3 (make-steb "Newark, DE" "" STEB-SIZE))
(define STEB4 (make-steb "" "Newark, DE" STEB-SIZE))

;;[Function Template: functions that consume a STEB can use this]
#| 
(define (steb-funf a-steb)
 ... (steb-pre a-steb) ;String
 ... (steb-post a-steb) ;String
 ... (steb-size a-steb) ;Number
 )
|#

;1.2

; 20 minutes
; update-steb-cloc : Steb Number -> Steb
; consumes any STEB and a new cursor location, and produces a
; new STEB where every field is the same EXCEPT the cursor location.
(define (update-steb-cloc a-steb new-location)
  (make-steb (substring (string-append (steb-pre a-steb) (steb-post a-steb)) ;String
                        0 new-location)
             (substring (string-append (steb-pre a-steb) (steb-post a-steb)) ;String
                        new-location)
             (steb-size a-steb) ;Number
             )
  )

(check-expect (update-steb-cloc STEB1 0) STEB1)
(check-expect (update-steb-cloc STEB2 1) (make-steb "N" "ewark, DE" STEB-SIZE))
(check-expect (update-steb-cloc STEB2 9) (make-steb "Newark, D" "E" STEB-SIZE))

;1.3
; 20 minutes
(define (reverse-substring a-string start finish)
  (substring a-string 
             (- (string-length a-string) finish)
             (- (string-length a-string) start)))

; move-cursor-left: Steb -> Steb
; consumes a STEB and produces a new STEB
; with the cursor 1 character to the left, unless the cursor is already at the start, in which case do 
; nothing. Be sure, as always, to develop appropriate unit tests.
(define (move-cursor-left a-steb)
  (cond [(string=? "" (steb-pre a-steb)) a-steb]
        [else (make-steb (reverse-substring (steb-pre a-steb) 1 (string-length (steb-pre a-steb)))
                         (string-append (reverse-substring (steb-pre a-steb)  0 1)
                                        (steb-post a-steb))
                         (steb-size a-steb))]))
(check-expect (move-cursor-left STEB1) STEB1)
(check-expect (move-cursor-left STEB2) (make-steb "Newark" ", DE" STEB-SIZE))
(check-expect (move-cursor-left STEB4) (make-steb "" "Newark, DE" STEB-SIZE))
;1.4
; 1 minute
; move-cursor-right: Steb -> Steb
; consumes a STEB and produces a new STEB
; with the cursor 1 character to the right, unless the cursor is already at the start, in which case do 
; nothing. Be sure, as always, to develop appropriate unit tests.
(define (move-cursor-right a-steb)
  (cond [(string=? "" (steb-post a-steb)) a-steb]
        [else (make-steb (string-append (steb-pre a-steb) 
                                        (substring (steb-post a-steb) 0 1))
                         (substring (steb-post a-steb) 1)
                         (steb-size a-steb))]))

(check-expect (move-cursor-right STEB1) STEB1)
(check-expect (move-cursor-right STEB2) (make-steb "Newark, " "DE" STEB-SIZE))
(check-expect (move-cursor-right STEB3) (make-steb "Newark, DE" "" STEB-SIZE))

;1.5
; 3 minutes
(define (delete-char a-steb)
  (cond [(string=? "" (steb-pre a-steb)) a-steb]
        [else (make-steb (substring (steb-pre a-steb)
                                    0
                                    (sub1 (string-length (steb-pre a-steb))))
                         (steb-post a-steb)
                         (steb-size a-steb))]))

(check-expect (delete-char STEB1) STEB1)
(check-expect (delete-char STEB2) (make-steb "Newark" " DE" STEB-SIZE))
(check-expect (delete-char STEB4) STEB4)

;1.6
; 2 minutes
(define (insert-char a-steb a-char)
  (make-steb (string-append (steb-pre a-steb) a-char)
             (steb-post a-steb)
             (steb-size a-steb)))

(check-expect (insert-char STEB1 "*") (make-steb "*" "" STEB-SIZE))
(check-expect (insert-char STEB2 "*") (make-steb "Newark,*" " DE" STEB-SIZE))
(check-expect (insert-char STEB3 "*") (make-steb "Newark, DE*" "" STEB-SIZE))

; 1.7
; 1 minute

(define (render-steb-with-cursor a-steb)
  (string-append (steb-pre a-steb)
                 "|"
                 (steb-post a-steb)))

(define (render-steb a-steb)
  (overlay/align "left" "middle"
                 (text (render-steb-with-cursor a-steb)
                       (steb-size a-steb)
                       "black")
                 (rectangle (* 50 (steb-size a-steb))
                            (steb-size a-steb)
                            'outline
                            "black")
                 ))

(render-steb STEB1)
(render-steb STEB2)
(render-steb STEB3)
(render-steb STEB4)

; 1.8
; 3 minutes
;; an STEBKeyEvent is either
;; -- "left": move cursor 1 left, unless already at start of string
;; -- "right": move cursor 1 right, unless already at end of string 
;; -- "\b" [backspace]: delete the character to the left of the cursor
;; -- any string of length 1: insert string at the current cursor location.
;; -- otherwise ignore the key
(define (update-steb a-steb a-key-event)
  (cond [(string=? a-key-event "left") (move-cursor-left a-steb)]
        [(string=? a-key-event "right") (move-cursor-right a-steb)]
        [(string=? a-key-event "\b") (delete-char a-steb)]
        [(= 1 (string-length a-key-event)) (insert-char a-steb a-key-event)]
        [else a-steb]))

(define (test-steb a-steb)
  (big-bang 
   a-steb
   (on-draw render-steb)
   (on-key update-steb)
   (check-with steb?)))


(require "weather-service-offline.rkt") 
(define NEWARK-REPORT (get-weather "Newark, DE"))
(define NEWARK-CURRENT (report-current NEWARK-REPORT))
(define NEWARK-TOMORROW (report-tomorrow NEWARK-REPORT))
; 2.0
; 5 minutes
(define (render-forecast a-forecast title)
  (above (text title STEB-SIZE "black")
         (bitmap/url (forecast-image-url a-forecast))
         (text (string-append (forecast-temperature-label a-forecast)
                              " "
                              (number->string (forecast-temperature a-forecast)))
               STEB-SIZE "black")
         (text (forecast-description a-forecast)
               STEB-SIZE "black")))

(render-forecast NEWARK-TOMORROW "Tomorrow")

; 2.2
; 10 minutes
(define (render-current a-weather)
  (beside (above (text (number->string (weather-temperature a-weather))
                       STEB-SIZE "black")
                 (text (string-append "Feels like " 
                                      (number->string (wind-chill (weather-wind a-weather))))
                       STEB-SIZE "black")
                 (text (string-append "Hum. " 
                                      (number->string (weather-humidity a-weather)))
                       STEB-SIZE "black"))
          (bitmap/url (weather-image-url a-weather)))
  )

(define (render-weather a-report)
  (above (render-current (report-current a-report))
         (beside (render-forecast (report-tonight a-report) "Tonight")
                 (render-forecast (report-tomorrow a-report) "Tomorrow")
                 (render-forecast (report-day-after a-report) "Next Day")
                 )))

(render-weather NEWARK-REPORT)


;2.3
; 1 minute
;; a WeatherWorld is a struct
(define-struct ww (steb report))
(define WW1 (make-ww STEB2 NEWARK-REPORT))
;; [Interpretation]
;; steb [STEB]: A SimpleTextEditorBox holding the current location
;; report [Report]: the current report for our location
(define (render-ww a-ww)
  (above (render-weather (ww-report a-ww))
         (render-steb (ww-steb a-ww))))

; 2.4
; 7 min
(define (steb-value a-report)
  (string-append (steb-pre a-report)
                 (steb-post a-report)))

(define (update-report-on-enter a-ww a-wwkey)
  (get-weather (steb-value (ww-steb a-ww))))
          
(define (handle-ww-key a-ww a-wwkey)
  (cond [(key=? a-wwkey "\r") (make-ww (ww-steb a-ww)
                                       (update-report-on-enter a-ww a-wwkey))]
        [else (make-ww (update-steb (ww-steb a-ww) a-wwkey)
                       (ww-report a-ww))]))
  
; 2.5
; 2 min
(define (main a-ww)
  (big-bang 
   a-ww
   (on-draw render-ww)
   (on-key handle-ww-key)
   (check-with ww?)))

(main WW1)