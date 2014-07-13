Welcome to Weather Libraries documentation!
===========================================

The Weather library offers access to the National Weather Service for the
United States (and only for the United States). You can either get information via latitude and longitude, or
by passing in an address (which will be geocoded through Google).

>>> from weatherservice import weatherservice



>>> report = weatherservice.get_report("New York, NY")
>>> report
<weatherservice.weatherservice.Report object>
>>> report.weather.temp
41
>>> weatherservice.get_report("Tokoyo, China")
weatherservice.weatherservice.WeatherException: This city was outside of the continental United States.
>>> weatherservice.disconnect()
>>> weatherservice.get_report("New York, NY").weather.temp
88
>>> weatherservice.get_report("Newark, DE")
weatherservice.weatherservice.GeocodeException: The given city was not in the cache.

>>> weatherservice.connect() #Starts offline by default


To run the unit tests from the command line:

>>> python -m tests.test


Further documentation is available in the `docs/_builds/index.html` file.
