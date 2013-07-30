import regular as weather_service

print map(lambda x : x.probability_of_precipitation, weather_service.get_report(37.2327, -80.4284).forecasts)