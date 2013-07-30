package structured;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gson.Gson;

import domain.Coordinate;
import exceptions.WeatherException;
import main.AbstractWeatherService;
import json.JsonWeatherListener;
import json.JsonWeatherService;

public class StructuredWeatherService implements AbstractWeatherService {
	private static StructuredWeatherService instance;
	private JsonWeatherService jsonWeatherService;
	private Gson gson;

	/**
	 * A protected constructor to defeat instantation of this singleton.
	 */
	protected StructuredWeatherService() {
		this.jsonWeatherService = JsonWeatherService.getInstance();
		this.gson =  new Gson();
	}
	
	/**
	 * Establishes a connection to the online Business Search service.
	 * dataservice. This requires an internet connection.<br>
	 * <br>
	 **/
	@Override
	public void connect() {
		this.jsonWeatherService.connect();
	}

	/**
	 * Establishes that data should be retrieved locally instead of from the
	 * online service. This is the default behavior for the Weather Service.<br>
	 * <br>
	 * 
	 * If data is being retrieved locally, you must be sure that your parameters
	 * match locally stored data. Otherwise, you will get nothing in return.
	 * 
	 * @param local
	 */
	@Override
	public void disconnect() {
		this.jsonWeatherService.disconnect();
	}

	/**
	 * Get the global instance of the Weather connection.
	 * 
	 * @return
	 */
	public static StructuredWeatherService getInstance() {
		if (instance == null) {
			synchronized (StructuredWeatherService.class) {
				if (instance == null) {
					instance = new StructuredWeatherService();
				}
			}
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getWeather(final Coordinate location) throws WeatherException {
		String jsonResponse = this.jsonWeatherService.getWeather(location);
		return gson.fromJson(jsonResponse, LinkedHashMap.class);
	}
	
	public void getWeather(final Coordinate location, final StructuredWeatherListener callback) {
		jsonWeatherService.getWeather(location, new JsonWeatherListener() {
			
			@Override
			public void getWeatherFailed(WeatherException exception) {
				callback.getWeatherFailed(exception);
			}
			
			@Override
			public void getWeatherCompleted(String forecast) {
				callback.getWeatherCompleted(gson.fromJson(forecast, LinkedHashMap.class));
			}
		});
	}
	
	public static void main(String[] args) {
		StructuredWeatherService sws = StructuredWeatherService.getInstance();
		sws.connect();
		try {
			System.out.println(sws.getWeather(new Coordinate(40, -77)));
		} catch (WeatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
