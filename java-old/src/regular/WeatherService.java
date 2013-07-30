package regular;

import main.AbstractWeatherService;

import json.JsonWeatherService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import domain.Coordinate;
import domain.Forecast;
import exceptions.WeatherException;

public class WeatherService implements AbstractWeatherService {

	private static WeatherService instance;
	private JsonWeatherService jsonWeatherService;
	private Gson gson;

	/**
	 * A protected constructor to defeat instantation of this singleton.
	 */
	protected WeatherService() {
		this.jsonWeatherService = JsonWeatherService.getInstance();
		this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
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
	public static WeatherService getInstance() {
		if (instance == null) {
			synchronized (WeatherService.class) {
				if (instance == null) {
					instance = new WeatherService();
				}
			}
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public Forecast getWeather(final Coordinate location)
			throws WeatherException {
		String jsonResponse = this.jsonWeatherService.getWeather(location);
		JsonParser parser = new JsonParser();
		JsonObject top = parser.parse(jsonResponse).getAsJsonObject();
		return new Forecast(top, gson);
	}

	public static void main(String[] args) {
		WeatherService sws = WeatherService.getInstance();
		sws.connect();
		try {
			System.out.println(sws.getWeather(new Coordinate(40, -77)));
		} catch (WeatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
