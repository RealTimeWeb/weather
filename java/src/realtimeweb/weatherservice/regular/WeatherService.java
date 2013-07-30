package realtimeweb.weatherservice.regular;

import realtimeweb.weatherservice.main.AbstractWeatherService;
import realtimeweb.weatherservice.json.JsonWeatherService;
import realtimeweb.weatherservice.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import realtimeweb.weatherservice.domain.Report;
import realtimeweb.weatherservice.json.JsonGetReportListener;

/**
 * Used to get data as classes.
 */
public class WeatherService implements AbstractWeatherService {
	private static WeatherService instance;
	private JsonWeatherService jsonInstance;
	private Gson gson;
	/**
	 * **For internal use only!** Protected Constructor guards against instantiation.
	
	 * @return 
	 */
	protected  WeatherService() {
		this.jsonInstance = JsonWeatherService.getInstance();
		this.gson = new Gson();
	}
	
	/**
	 * Retrieves the singleton instance.
	
	 * @return WeatherService
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
	
	/**
	 * Establishes a connection to the online service. Requires an internet connection.
	
	 */
	@Override
	public void connect() {
		jsonInstance.connect();
	}
	
	/**
	 * Establishes that Business Search data should be retrieved locally. This does not require an internet connection.<br><br>If data is being retrieved locally, you must be sure that your parameters match locally stored data. Otherwise, you will get nothing in return.
	
	 */
	@Override
	public void disconnect() {
		jsonInstance.disconnect();
	}
	
	/**
	 * Gets a report on the current weather, forecast, and more detailed information about the location.
	 * @param latitude The latitude (up-down) of the location to get information about.
	 * @param longitude The longitude (left-right) of the location to get information about.
	 * @return Report
	 */
	public Report getReport(double latitude, double longitude) throws Exception {
		String response = jsonInstance.getReport(latitude,longitude);
		JsonParser parser = new JsonParser();
		JsonObject top = parser.parse(response).getAsJsonObject();
		return new Report(top, gson);
	}
	
	/**
	 * Gets a report on the current weather, forecast, and more detailed information about the location.
	 * @param latitude The latitude (up-down) of the location to get information about.
	 * @param longitude The longitude (left-right) of the location to get information about.
	 * @param callback The listener that will receive the data (or error).
	 */
	public void getReport(double latitude, double longitude, final GetReportListener callback) {
		
		jsonInstance.getReport(latitude, longitude, new JsonGetReportListener() {
		    @Override
		    public void getReportFailed(Exception exception) {
		        callback.getReportFailed(exception);
		    }
		    
		    @Override
		    public void getReportCompleted(String response) {
		        JsonParser parser = new JsonParser();
		JsonObject top = parser.parse(response).getAsJsonObject();
		        Report result = new Report(top, gson);
		        callback.getReportCompleted(result);
		    }
		});
		
	}
	
}
