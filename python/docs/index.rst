.. weatherservice documentation master file, created by
   sphinx-quickstart on Tue Jul 30 18:33:43 2013.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Welcome to weatherservice's documentation!
==========================================

The WeatherService library offers access to the National Weather Service for the
United States. You can either get information via latitude and longitude, or
by passing in an address (which will be geocoded through Google). This library
can work online (connected) or offline (disconnected). When offline, only certain
addresses are available.

>>> from weatherservice import weatherservice
>>> weatherservice.connect()
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
   
Methods
----------------

.. autofunction:: weatherservice.connect

.. autofunction:: weatherservice.disconnect
    
.. autofunction:: weatherservice.get_report

.. autofunction:: weatherservice.get_report_by_latlng

Data Classes
------------

.. autoclass:: weatherservice.Report
    :members:
    :special-members: __init__
    
.. autoclass:: weatherservice.Weather
    :members:
    :special-members: __init__
    
.. autoclass:: weatherservice.Location
    :members:
    :special-members: __init__
    
.. autoclass:: weatherservice.Forecast
    :members:
    :special-members: __init__

Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`

