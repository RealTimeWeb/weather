WeatherService
==============

Get the weather for a region, or get long-term forecasts.

Usage is very simple:

    import weatherservice.regular as weatherservice

    # Consumes latitude, longitudes
    report = weatherservice.get_report(92, 40)
    print "Today's temperature", report.weather.temp
    
    
Online documentation is available: http://mickey.cs.vt.edu/realtimeweb/static/resources/weather/python/index.html
