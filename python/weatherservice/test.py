import weatherservice
import unittest

class TestWeatherServiceOffline(unittest.TestCase):
    def setUp(self):
        weatherservice.disconnect("cache.json")
    
    def test_china(self):
        with self.assertRaises(weatherservice.GeocodeException):
            weatherservice.get_report("tokoyo, china")

class TestWeatherServiceOnline(unittest.TestCase):
    def setUp(self):
        weatherservice.connect()
    
    def test_newark(self):
        report = weatherservice.get_report("newark, de")
        self.assertIsInstance(report, weatherservice.Report)
    
    def test_china(self):
        with self.assertRaises(weatherservice.WeatherException):
            weatherservice.get_report("tokoyo, china")
        
if __name__ == '__main__':
    unittest.main()