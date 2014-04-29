def parse_int(value, default=0):
    try:
        return int(value)
    except ValueError:
        return default
def parse_float(value, default=0):
    try:
        return float(value)
    except ValueError:
        return default


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
        return Weather(parse_int(json_data.get('Temp', 0)),
                    parse_int(json_data.get('Dewp', 0)),
                    parse_int(json_data.get('Relh', 0)),
                    parse_int(json_data.get('Winds', 0)),
                    parse_int(json_data.get('Windd', 0)),
                    json_data.get('Weather', ''),
                    json_data.get('Weatherimage', ''),
                    parse_float(json_data.get('Visibility', 0.0)),
                    parse_int(json_data.get('WindChill', 0)),
                    parse_float(json_data.get('SLP', 0.0)))