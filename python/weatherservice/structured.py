import json
import threading
from weatherservice._cache import _recursively_convert_unicode_to_str, lookup
from weatherservice.report import Report
import weatherservice.raw_json as raw_json
def connect():
    """
    Connect to the online data source in order to get up-to-date information.
    
    :returns: void
    """
    raw_json.connect()
def disconnect():
    """
    Connect to the local cache, so no internet connection is required.
    
    :returns: void
    """
    raw_json.disconnect()
def get_report(latitude, longitude):
    """
    Gets a report on the current weather, forecast, and more detailed information about the location.
    
    :param latitude: The latitude (up-down) of the location to get information about.
    :type latitude: float
    :param longitude: The longitude (left-right) of the location to get information about.
    :type longitude: float
    :returns: dict
    """
    try:
        report = raw_json.get_report(latitude, longitude)
        json_report = json.loads(report.decode("utf-8"))
        return _recursively_convert_unicode_to_str(json_report)
    except Exception as e:
        print(e)
        print ("Bad response from the server. Check to make sure your arguments are right; especially that the location is within the United States.")

def get_report_async(callback, error_callback, latitude, longitude):
    """
    Asynchronous version of get_report
    
    :param callback: Function that consumes the data (a dict) returned on success.
    :type callback: function
    :param error_callback: Function that consumes the exception returned on failure.
    :type error_callback: function
    :param latitude: The latitude (up-down) of the location to get information about.
    :type latitude: float
    :param longitude: The longitude (left-right) of the location to get information about.
    :type longitude: float
    :returns: void
    """
    def server_call(callback, error_callback, latitude, longitude):
        """
        Internal closure to thread this call.
        
        :param callback: Function that consumes the data (a dict) returned on success.
        :type callback: function
        :param error_callback: Function that consumes the exception returned on failure.
        :type error_callback: function
        :param latitude: The latitude (up-down) of the location to get information about.
        :type latitude: float
        :param longitude: The longitude (left-right) of the location to get information about.
        :type longitude: float
        :returns: void
        """
        try:
            callback(get_report(latitude, longitude))
        except Exception as e:
            error_callback(e)
    threading.Thread(target=server_call, args = (latitude, longitude)).start()

