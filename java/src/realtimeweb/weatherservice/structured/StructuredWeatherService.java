package realtimeweb.weatherservice.structured;

import realtimeweb.weatherservice.main.AbstractWeatherService;
import realtimeweb.weatherservice.util.Util;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.google.gson.Gson;
import realtimeweb.weatherservice.json.JsonWeatherService;
import realtimeweb.weatherservice.json.JsonGetReportListener;

/**
 * Used to get data as built-in Java objects (HashMap, ArrayList, etc.).
 */
public class StructuredWeatherService implements AbstractWeatherService {
	private static StructuredWeatherService instance;
	private JsonWeatherService jsonInstance;
	private Gson gson;
	/**
	 * **For internal use only!** Protected Constructor guards against instantiation.
	
	 * @return 
	 */
	protected  StructuredWeatherService() {
		this.jsonInstance = JsonWeatherService.getInstance();
		this.gson = new Gson();
	}
	
	/**
	 * Retrieves the singleton instance.
	
	 * @return StructuredWeatherService
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
	 * @return HashMap<String, Object>
	 */
	public HashMap<String, Object> getReport(double latitude, double longitude) throws Exception {
		return gson.fromJson(jsonInstance.getReport(latitude, longitude), LinkedHashMap.class);
	}
	
	/**
	 * Gets a report on the current weather, forecast, and more detailed information about the location.
	 * @param latitude The latitude (up-down) of the location to get information about.
	 * @param longitude The longitude (left-right) of the location to get information about.
	 * @param callback The listener that will be given the data (or error)
	 */
	public void getReport(double latitude, double longitude, final StructuredGetReportListener callback) {
		
		jsonInstance.getReport(latitude, longitude, new JsonGetReportListener() {
		    @Override
		    public void getReportFailed(Exception exception) {
		        callback.getReportFailed(exception);
		    }
		    
		    @Override
		    public void getReportCompleted(String data) {
		        callback.getReportCompleted(gson.fromJson(data, LinkedHashMap.class));
		    }
		});
		
	}
	
}
