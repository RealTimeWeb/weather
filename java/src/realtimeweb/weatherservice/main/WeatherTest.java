package realtimeweb.weatherservice.main;

import realtimeweb.weatherservice.json.JsonWeatherService;
import realtimeweb.weatherservice.regular.WeatherService;
import realtimeweb.weatherservice.structured.StructuredWeatherService;

public class WeatherTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JsonWeatherService jws = JsonWeatherService.getInstance();
		jws.connect();
		try {
			System.out.println(jws.getReport(37.2327, -80.4284));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StructuredWeatherService sws = StructuredWeatherService.getInstance();
		sws.connect();
		try {
			System.out.println(sws.getReport(37.2327, -80.4284));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WeatherService ws = WeatherService.getInstance();
		ws.connect();
		try {
			System.out.println(ws.getReport(37.2327, -80.4284));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
