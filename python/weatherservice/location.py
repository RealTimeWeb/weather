class Location(object):
    """
    A detailed description of a location
    """
    def __init__(self, latitude, longitude, elavation, name):
        """
        Creates a new Location
        
        :param self: This object
        :type self: Location
        :param latitude: The latitude (up-down) of this location.
        :type latitude: float
        :param longitude: The longitude (left-right) of this location.
        :type longitude: float
        :param elavation: The height above sea-level (in feet).
        :type elavation: int
        :param name: The city and state that this location is in.
        :type name: string
        :returns: Location
        """
        self.latitude = latitude
        self.longitude = longitude
        self.elavation = elavation
        self.name = name
    
    @staticmethod
    def _from_json(json_data):
        """
        Creates a Location from json data.
        
        :param json_data: The raw json data to parse
        :type json_data: dict
        :returns: Location
        """
        return Location(float(json_data['latitude']),
                    float(json_data['longitude']),
                    int(json_data['elevation']),
                    json_data['areaDescription'])