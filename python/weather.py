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
        return Weather(json_data['Temp'],
                    json_data['Dewp'],
                    json_data['Relh'],
                    json_data['Winds'],
                    json_data['Windd'],
                    json_data['Weather'],
                    json_data['Weatherimage'],
                    json_data['Visibility'],
                    json_data['WindChill'],
                    json_data['SLP'])