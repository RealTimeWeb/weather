
#lang scribble/manual

@(require (for-label racket "weather-service-online.rkt"))
 
@title[#:version "2.0"]{Weather Service}
@author{Austin Cory Bart}
@author+email["Austin Cory Bart" "acbart@vt.edu"]

@section{Introduction}

This teachpack let's you get the weather and forecast for a given address. You can use it online, with live data:

@defmodule["weather-service-online.rkt"]

@margin-note{Why would you use it offline? First, you don't need an internet connection. Second, you don't have to worry about your tests changing, since local data never changes (even though weather should change daily!).}
or offline with local data:

@defmodule["weather-service-offline.rkt"]

@section{Functions}
@defmodule["weather-service-online.rkt"]
@defproc[(get-weather [address string?]) (or/c string? report?)]{

Gets a report on the current weather, the forecast, and more detailed information about the given address, UNLESS there was an error, in which case the error is returned as a string.
@itemlist[

			@item{@racket[address] --- An address in the United States, such as @racket["Seattle, WA"] or @racket["101 Smith Hall, Newark, DE 19716"].}]

When using the service offline, you should only use the following addresses:
@itemlist[@item{@racket["Newark, DE"]}
           @item{@racket["Seattle, WA"]}
           @item{@racket["Orlando, FL"]}
           @item{@racket["Washington DC"]}
           @item{@racket["Los Angeles, CA"]}
           @item{@racket["Chicago, IL"]}
           @item{@racket["Alaska"]}
          ]

There are several reasons why @racket[get-weather] might fail:
@itemlist[@item{The address might be outside of the United States.}
           @item{There might have been no results found for the address (try it in Google Maps to make sure it's a real address)}
           @item{If you're using the offline version, the address may not be available offline. You can try it with the online version.}
           ]

}

@section{Structs}
@defmodule["weather-service-online.rkt"]

@defstruct[report ([current weather?]
			[tonight forecast?]
                        [tomorrow forecast?]
                        [day-after forecast?]
			[location location?])]{
A container for the weather, forecasts, and location information.
@itemlist[
			@item{@racket[current] --- The current weather for this address.}
			@item{@racket[tonight] --- The forecast for tonight.}
                        @item{@racket[tomorrow] --- The forecast for tomorrow.}
                        @item{@racket[day-after] --- The forecast for the day after tomorrow.}
			@item{@racket[location] --- More detailed location information on this address.}
                        ]}

@defstruct[weather ([temperature number?]
			[dewpoint number?]
			[humidity number?]
                        [visibility number?]
                        [pressure number?]
                        [wind wind?]
			[description string?]
			[image-url string?])]{
A structured representation the current weather.
@itemlist[
			@item{@racket[temp] --- The current temperature (in Fahrenheit).}
			@item{@racket[dewpoint] --- The current dewpoint temperature (in Fahrenheit).}
			@item{@racket[humidity] --- The current relative humidity (as a percentage).}
                        @item{@racket[visibility] --- How far you can see (in miles).}
			@item{@racket[pressure] --- The barometric pressure (in inches).}
                        @item{@racket[wind] --- The current wind pattern.}
			@item{@racket[description] --- A human-readable description of the current weather.}
			@item{@racket[image-url] --- A url pointing to a picture that describes the weather.  Use @(hyperlink "http://docs.racket-lang.org/teachpack/2htdpimage.html#(def._((lib._2htdp/image..rkt)._bitmap/url))" @racket["bitmap/url"]) to convert it to an image.}
			]}
                 
                 
@defstruct[wind ([speed number?]
			[direction number?]
			[chill number?])]{
A structured representation of wind.
@itemlist[
                        @item{@racket[speed] --- The current wind speed (in miles-per-hour).}
			@item{@racket[direction] --- The current wind direction (in degrees).}
			@item{@racket[chill] --- The perceived temperature (in Fahrenheit) caused by the wind.}
                        ]}


@defstruct[forecast ([temperature-label string?]
			[temperature number?]
			[probability-of-precipitation number?]
			[image-url string?]
                        [description string?]
			[long-description string?])]{
A prediction for future weather.
@itemlist[
			@item{@racket[temperature-label] --- Either @racket["High"] or @racket["Low"], depending on whether or not the predicted temperature is a daily high or a daily low.}
			@item{@racket[temperature] --- The predicted temperature for this period (in Fahrenheit).}
			@item{@racket[probability-of-precipitation] --- The probability of precipitation for this period (as a percentage).}
			@item{@racket[image-url] --- A url pointing to a picture that describes the predicted weather for this period. Use @(hyperlink "http://docs.racket-lang.org/teachpack/2htdpimage.html#(def._((lib._2htdp/image..rkt)._bitmap/url))" @racket["bitmap/url"]) to convert it to an image.}
                        @item{@racket[description] --- A human-readable description of the predicted weather for this period.}
			@item{@racket[long-description] --- A more-detailed, human-readable description of the predicted weather.}]}

@defstruct[location ([latitude number?]
			[longitude number?]
			[elavation number?]
			[name string?]) ]{

A detailed description of a location
@itemlist[

			@item{@racket[latitude] --- The latitude (up-down) of this location.}

			@item{@racket[longitude] --- The longitude (left-right) of this location.}

			@item{@racket[elavation] --- The height above sea-level (in feet).}

			@item{@racket[name] --- The city and state that this location is in.}]}



@; ----------------------------------------------------------------------------
@section{License}

Copyright (c) 2013, Virginia Tech and Austin Cory Bart.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

@itemize[

@item{ Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer. }

@item{ Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution. }

] @;itemize

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

@; ----------------------------------------------------------------------------