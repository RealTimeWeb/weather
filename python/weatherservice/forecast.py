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
                    map(int, json_data['data']['temperature']),
                    map(int, json_data['data']['pop']),
                    json_data['data']['weather'],
                    json_data['data']['iconLink'],
                    json_data['data']['text'])