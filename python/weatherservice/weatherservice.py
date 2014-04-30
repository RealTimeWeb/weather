import sys

PYTHON_3 = sys.version_info >= (3, 0)

import urllib
if PYTHON_3:
    import urllib2
    from urllib.parse import quote_plus
else:
    import urllib.request as request
    from urllib import quote_plus
    
import json
import threading

################################################################################
# Auxilary
################################################################################

def urlencode(query, params):
    """
    Correctly convert the given query and parameters into a full query+query
    string, ensuring the order of the params.
    """
    return query + '?' + "&".join(key+'='+urllib.quote_plus(value) 
                                  for key, value in params)

def _parse_int(value, default=0):
    """
    Attempt to cast *value* into an integer, returning *default* if it fails.
    """
    if value is None:
        return default
    try:
        return int(value)
    except ValueError:
        return default

def _parse_float(value, default=0):
    """
    Attempt to cast *value* into a float, returning *default* if it fails.
    """
    if value is None:
        return default
    try:
        return float(value)
    except ValueError:
        return default

def _get(url):
    """
    Convert a URL into it's response (a *str*).
    """
    if PYTHON_3:
        response = request.urlopen(url)
        return response.read()
    else:
        req = urllib2.Request(url)
        response = urllib2.urlopen(req)
        return response.read()
        
def _recursively_convert_unicode_to_str(input):
    """
    Force the given input to only use `str` instead of `bytes` or `unicode`.
    This works even if the input is a dict, list, 
    """
    if isinstance(input, dict):
        return {_recursively_convert_unicode_to_str(key): _recursively_convert_unicode_to_str(value) for key, value in input.items()}
    elif isinstance(input, list):
        return [_recursively_convert_unicode_to_str(element) for element in input]
    elif not PYTHON_3 and isinstance(input, unicode):
        return input.encode('utf-8')
    else:
        return input

def _from_json(data):
    """
    Convert the given string data into a JSON dict/list/primitive, ensuring that
    `str` are used instead of bytes.
    """
    return _recursively_convert_unicode_to_str(json.loads(data))

        
################################################################################
# Cache
################################################################################

_CACHE = {}
_CACHE_COUNTER = {}
_CONNECTED = False
def connect():
    """
    Connect to the online data source in order to get up-to-date information.
    :returns: void
    """
    _CONNECTED = True
def disconnect(filename="cache.json"):
    """
    Connect to the local cache, so no internet connection is required.
    :returns: void
    """
    _CACHE = _recursively_convert_unicode_to_str(json.load(open(filename, r)))
    for key in CACHE.keys():
        _CACHE_COUNTER[key] = 0
        _CACHE_PATTERN[key] = _CACHE[key][0]
        _CACHE_DATA[key] = _CACHE[key][1:]
    _CONNECTED = False
def _lookup(key):
    """
    Internal method that looks up a key in the local cache.
    :param key: Get the value based on the key from the cache.
    :type key: string
    :returns: void
    """
    if _CACHE_COUNTER[key] >= len(_CACHE[key][1:]):
        if _CACHE[key][0] == "empty":
            return ""
        elif _CACHE[key][0] == "repeat" and _CACHE[key][1:]:
            return _CACHE[key][-1]
        elif _CACHE[key][0] == "repeat":
            return ""
        else:
            _CACHE_COUNTER[key] = 0
    else:
        _CACHE_COUNTER[key] += 1
    if _CACHE[key]:
        return _CACHE[key][1+_CACHE_COUNTER]
    else:
        return ""
    
def _save_cache(filename="cache.json"):
    json.dump(_CACHE, filename)
        
################################################################################
# Domain Objects
################################################################################
        
class Location(object):
    """
    A detailed description of a location
    """
    def __init__(self, latitude, longitude, elevation, name):
        """
        Creates a new Location
        
        :param self: This object
        :type self: Location
        :param latitude: The latitude (up-down) of this location.
        :type latitude: float
        :param longitude: The longitude (left-right) of this location.
        :type longitude: float
        :param elevation: The height above sea-level (in feet).
        :type elevation: int
        :param name: The city and state that this location is in.
        :type name: string
        :returns: Location
        """
        self.latitude = latitude
        self.longitude = longitude
        self.elevation = elevation
        self.name = name
    
    @staticmethod
    def _from_json(json_data):
        """
        Creates a Location from json data.
        
        :param json_data: The raw json data to parse
        :type json_data: dict
        :returns: Location
        """
        return Location(_parse_float(json_data.get('latitude', 0.0),
                        _parse_float(json_data.get('longitude', 0.0),
                        _parse_int(json_data.get('elevation', 0)),
                        json_data.get('areaDescription', ''))

class Weather(object):
    """
    A structured representation the current weather.
    """
    def __init__(self, temp, dewpoint, humidity, wind_speed, wind_direction, description, image_url, visibility, windchill, pressure):
        """
        Creates a new Weather
        
        :param self: This object
        :type self: Weather
        :param temp: The current temperature (in Fahrenheit).
        :type temp: int
        :param dewpoint: The current dewpoint temperature (in Fahrenheit).
        :type dewpoint: int
        :param humidity: The current relative humidity (as a percentage).
        :type humidity: int
        :param wind_speed: The current wind speed (in miles-per-hour).
        :type wind_speed: int
        :param wind_direction: The current wind direction (in degrees).
        :type wind_direction: int
        :param description: A human-readable description of the current weather.
        :type description: string
        :param image_url: A url pointing to a picture that describes the weather.
        :type image_url: string
        :param visibility: How far you can see (in miles).
        :type visibility: float
        :param windchill: The perceived temperature (in Fahrenheit).
        :type windchill: int
        :param pressure: The barometric pressure (in inches).
        :type pressure: float
        :returns: Weather
        """
        self.temp = temp
        self.dewpoint = dewpoint
        self.humidity = humidity
        self.wind_speed = wind_speed
        self.wind_direction = wind_direction
        self.description = description
        self.image_url = image_url
        self.visibility = visibility
        self.windchill = windchill
        self.pressure = pressure
    
    @staticmethod
    def _from_json(json_data):
        """
        Creates a Weather from json data.
        
        :param json_data: The raw json data to parse
        :type json_data: dict
        :returns: Weather
        """
        return Weather(_parse_int(json_data.get('Temp', 0)),
                       _parse_int(json_data.get('Dewp', 0)),
                       _parse_int(json_data.get('Relh', 0)),
                       _parse_int(json_data.get('Winds', 0)),
                       _parse_int(json_data.get('Windd', 0)),
                       json_data.get('Weather', ''),
                       json_data.get('Weatherimage', ''),
                       _parse_float(json_data.get('Visibility', 0.0)),
                       _parse_int(json_data.get('WindChill', 0)),
                       _parse_float(json_data.get('SLP', 0.0)))

class Forecast(object):
    """
    A prediction for future weather.
    """
    def __init__(self, period_name, period_time, temperature_label, temperature, probability_of_precipitation, description, image_url, long_description):
        """
        Creates a new Forecast
        
        :param self: This object
        :type self: Forecast
        :param period_name: A human-readable name for this time period (e.g. Tonight or Saturday).
        :type period_name: string
        :param period_time: A string representing the time that this period starts. Encoded as YYYY-MM-DDTHH:MM:SS, where the T is not a number, but a always present character (e.g. 2013-07-30T18:00:00).
        :type period_time: string
        :param temperature_label: Either 'High' or 'Low', depending on whether or not the predicted temperature is a daily high or a daily low.
        :type temperature_label: string
        :param temperature: The predicted temperature for this period (in Fahrenheit).
        :type temperature: int
        :param probability_of_precipitation: The probability of precipitation for this period (as a percentage).
        :type probability_of_precipitation: int
        :param description: A human-readable description of the predicted weather for this period.
        :type description: string
        :param image_url: A url pointing to a picture that describes the predicted weather for this period.
        :type image_url: string
        :param long_description: A more-detailed, human-readable description of the predicted weather for this period.
        :type long_description: string
        :returns: Forecast
        """
        self.period_name = period_name
        self.period_time = period_time
        self.temperature_label = temperature_label
        self.temperature = temperature
        if probability_of_precipitation is None:
            self.probability_of_precipitation = 0
        else:
            self.probability_of_precipitation = probability_of_precipitation
        self.description = description
        self.image_url = image_url
        self.long_description = long_description
    
    @staticmethod
    def _from_json(json_data):
        """
        Creates a Forecast from json data.
        
        :param json_data: The raw json data to parse
        :type json_data: dict
        :returns: Forecast
        """
        return map(Forecast, json_data['time']['startPeriodName'],
                    json_data['time']['startValidTime'],
                    json_data['time']['tempLabel'],
                    map(_parse_int, json_data['data']['temperature']),
                    map(_parse_int, json_data['data']['pop']),
                    json_data['data']['weather'],
                    json_data['data']['iconLink'],
                    json_data['data']['text'])

class Report(object):
    """
    A container for the weather, forecasts, and location information.
    """
    def __init__(self, weather, forecasts, location):
        """
        Creates a new Report
        
        :param self: This object
        :type self: Report
        :param weather: The current weather for this location.
        :type weather: Weather
        :param forecasts: The forecast for the next 7 days and 7 nights.
        :type forecasts: listof Forecast
        :param location: More detailed information on this location.
        :type location: Location
        :returns: Report
        """
        self.weather = weather
        self.forecasts = forecasts
        self.location = location
    
    @staticmethod
    def _from_json(json_data):
        """
        Creates a Report from json data.
        
        :param json_data: The raw json data to parse
        :type json_data: dict
        :returns: Report
        """
        return Report(Weather._from_json(json_data['currentobservation']),
                    Forecast._from_json(json_data),
                    Location._from_json(json_data['location']))
                    
################################################################################
# Service call
################################################################################

def _get_report_request(latitude,longitude):
    """
    Used to build the request string used by :func:`get_report`.
    
    
    :param latitude: The latitude (up-down) of the location to get information about.
    :type latitude: float
    
    :param longitude: The longitude (left-right) of the location to get information about.
    :type longitude: float
    :returns: str
    """
    arguments = [("lat", latitude), ("FcstType", "json"), ("lon", longitude)]
    return urlencode("http://forecast.weather.gov/MapClick.php", arguments)

def _get_report_string(latitude,longitude):
    """
    Like :func:`get_report` except returns the raw data instead.
    
    
    :param latitude: The latitude (up-down) of the location to get information about.
    :type latitude: float
    
    :param longitude: The longitude (left-right) of the location to get information about.
    :type longitude: float
    :returns: str
    """
    key = _get_report_request(latitude, longitude)
    result = _get(key) if _CONNECTED else _lookup(key)
    return result
    
def get_report_as_dict(latitude,longitude):
    """
    Gets a report on the current weather, forecast, and more detailed information about the location.
    
    
    :param latitude: The latitude (up-down) of the location to get information about.
    :type latitude: float
    
    :param longitude: The longitude (left-right) of the location to get information about.
    :type longitude: float
    :returns: dict
    """
    result = _get_report_string(latitude, longitude)
    return _from_json(result)

def get_report(latitude,longitude):
    """
    Gets a report on the current weather, forecast, and more detailed information about the location.
    
    
    :param latitude: The latitude (up-down) of the location to get information about.
    :type latitude: float
    
    :param longitude: The longitude (left-right) of the location to get information about.
    :type longitude: float
    :returns: Report
    """
    result = _get_report_string(latitude,longitude)
    return Report._from_json(_from_json(result))
    