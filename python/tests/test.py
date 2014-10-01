import weather.weather as weatherservice
import unittest

class TestWeatherServiceOnlineDict(unittest.TestCase):
    def setUp(self):
        weatherservice.connect()
    
    def test_newark(self):
        weatherservice._USE_CLASSES = False
        report = weatherservice.get_report("newark, de")
        self.assertIsInstance(report, dict)
    
    def test_china(self):
        with self.assertRaises(weatherservice.WeatherException):
            weatherservice.get_report("tokoyo, china")

class TestWeatherServiceOnline(unittest.TestCase):
    def setUp(self):
        weatherservice.connect()
    
    def test_newark(self):
        weatherservice._USE_CLASSES = True
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
        self.assertEqual(len(weatherservice._CACHE), 27)
        weatherservice.get_report("san francisco, CA")
        weatherservice.get_report("san francisco, CA")
        weatherservice.get_report("san francisco, CA")
        self.assertEqual(sum([len(element) for key, element in weatherservice._CACHE.items()]), 67)
        
    def tearDown(self):
        weatherservice._save_cache("cache2.json")
        

class TestWeatherServiceOffline(unittest.TestCase):
    def setUp(self):
        weatherservice.disconnect()
    
    def test_china(self):
        with self.assertRaises(weatherservice.GeocodeException):
            weatherservice.get_report("tokoyo, china")
    def test_newark(self):
        weatherservice._USE_CLASSES = True
        report = weatherservice.get_report("newark, de")
        self.assertIsInstance(report, weatherservice.Report)


class TestWeatherServiceForecast(unittest.TestCase):
    def setUp(self):
        weatherservice.connect()

    def test_temperature(self):
        self.assertIsInstance(weatherservice.get_temperature("2202 Kraft "
                                                             "Drive, Blacksburg, "
                                                             "VA"), int)

    def test_forecasts(self):
        self.assertIsInstance(weatherservice.get_forecasts("2202 Kraft Drive, "
                                                           "Blacksburg, "
                                                           "VA"), list)
if __name__ == '__main__':
    unittest.main()