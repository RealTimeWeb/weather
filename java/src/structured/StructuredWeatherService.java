package structured;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LinkedTreeMap;

import domain.Coordinate;
import domain.Forecast;
import exceptions.WeatherException;
import main.AbstractWeatherService;
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
