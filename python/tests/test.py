import weatherservice.weatherservice as weatherservice
import unittest

class TestWeatherServiceOnline(unittest.TestCase):
    def setUp(self):
        weatherservice.connect()
    
    def test_newark(self):
        report = weatherservice.get_report("newark, de")
        self.assertIsInstance(report, weatherservice.Report)
    
    def test_china(self):
        with self.assertRaises(weatherservice.WeatherException):
            weatherservice.get_report("tokoyo, china")
            
class TestWeatherServiceEditing(unittest.TestCase):
    def setUp(self):
        weatherservice.connect()
    
    def test_cache(self):
        weatherservice._start_editing()
        weatherservice.get_report("newark, de")
        weatherservice.get_report("blacksburg, va")
        weatherservice.get_report("new york, NY")
        weatherservice.get_report("santa barbara, CA")
        weatherservice.get_report("san francisco, CA")
        self.assertEqual(len(weatherservice._CACHE), 10)
        weatherservice.get_report("san francisco, CA")
        weatherservice.get_report("san francisco, CA")
        weatherservice.get_report("san francisco, CA")
        self.assertEqual(sum([len(element) for key, element in weatherservice._CACHE.items()]), 26)
        
    def tearDown(self):
        weatherservice._save_cache("cache2.json")
        

class TestWeatherServiceOffline(unittest.TestCase):
    def setUp(self):
        weatherservice.disconnect("cache2.json")
    
    def test_china(self):
        with self.assertRaises(weatherservice.GeocodeException):
            weatherservice.get_report("tokoyo, china")
    def test_newark(self):
        report = weatherservice.get_report("newark, de")
        self.assertIsInstance(report, weatherservice.Report)
        
if __name__ == '__main__':
    unittest.main()