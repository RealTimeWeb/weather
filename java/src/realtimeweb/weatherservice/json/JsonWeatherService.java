package realtimeweb.weatherservice.json;

import realtimeweb.weatherservice.main.AbstractWeatherService;
import java.util.HashMap;
import realtimeweb.weatherservice.util.Util;

/**
 * Used to get data as a raw string.
 */
public class JsonWeatherService implements AbstractWeatherService {
	private static JsonWeatherService instance;
	protected boolean local;
	private ClientStore clientStore;
	/**
	 * **For internal use only!** Protected Constructor guards against instantiation.
	
	 * @return 
	 */
	protected  JsonWeatherService() {
		disconnect();
		this.clientStore = new ClientStore();
	}
	
	/**
	 * Retrieves the singleton instance.
	
	 * @return JsonWeatherService
	 */
	public static JsonWeatherService getInstance() {
		if (instance == null) {
			synchronized (JsonWeatherService.class) {
				if (instance == null) {
					instance = new JsonWeatherService();
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
		this.local = false;
	}
	
	/**
	 * Establishes that Business Search data should be retrieved locally. This does not require an internet connection.<br><br>If data is being retrieved locally, you must be sure that your parameters match locally stored data. Otherwise, you will get nothing in return.
	
	 */
	@Override
	public void disconnect() {
		this.local = true;
	}
	
	/**
	 * **For internal use only!** The ClientStore is the internal cache where offline data is stored.
	
	 * @return ClientStore
	 */
	public ClientStore getClientStore() {
		return this.clientStore;
	}
	
	/**
	 * Gets a report on the current weather, forecast, and more detailed information about the location.
	 * @param latitude The latitude (up-down) of the location to get information about.
	 * @param longitude The longitude (left-right) of the location to get information about.
	 * @return String
	 */
	public String getReport(double latitude, double longitude) throws Exception {
		String url = "http://forecast.weather.gov/MapClick.php";
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("lat", String.valueOf(latitude));
		parameters.put("lon", String.valueOf(longitude));
		if (this.local) {
			return clientStore.getData(Util.hashRequest(url, parameters));
		}
		parameters.put("FcstType", String.valueOf("json"));
		String jsonResponse = "";
		try {
		    jsonResponse = Util.get(url, parameters);
		    if (jsonResponse.startsWith("<")) {
		        throw new Exception(jsonResponse);
		    }
		    return jsonResponse;
		} catch (Exception e) {
		    throw new Exception(e.toString());
		}
	}
	
	/**
	 * Gets a report on the current weather, forecast, and more detailed information about the location.
	 * @param latitude The latitude (up-down) of the location to get information about.
	 * @param longitude The longitude (left-right) of the location to get information about.
	 * @param callback The listener that will be given the data (or error).
	 */
	public void getReport(final double latitude, final double longitude, final JsonGetReportListener callback) {
		
		Thread thread = new Thread() {
		    @Override
		    public void run() {
		        try {
		            callback.getReportCompleted(JsonWeatherService.getInstance().getReport(latitude, longitude));
		        } catch (Exception e) {
		            callback.getReportFailed(e);
		        }
		    }
		};
		thread.start();
		
	}
	
}
