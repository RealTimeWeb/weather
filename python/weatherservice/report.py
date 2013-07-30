from weather import Weather
from location import Location
from forecast import Forecast
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