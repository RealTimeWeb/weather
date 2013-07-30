package json;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import util.Util;

import main.AbstractWeatherService;

import domain.Coordinate;

import exceptions.WeatherException;
import exceptions.WeatherLocationUnavailableException;
import exceptions.WeatherServiceNotAvailableException;

public class JsonWeatherService implements AbstractWeatherService {

	private static JsonWeatherService instance;
	protected boolean local;
	private ClientStore clientStore;
	private boolean storing = false;
	
	/**
	 * <b>This method is for internal use only.</b><br>
	 * <br>
	 * 
	 * When the system is storing, any requests made to the online Weather service
	 * will be stored for future use. However, those requests will not be saved
	 * between runs of your program.
	 */
	public void setStoring(boolean storing) {
		this.storing = storing;
	}

	/**
	 * A protected constructor to defeat instantation of this singleton.
	 */
	protected JsonWeatherService(boolean recording) {
		disconnect();
		setStoring(recording);
		this.clientStore = new ClientStore(recording);
	}

	/**
	 * Get the global instance of the Weather connection.
	 * 
	 * @return
	 */
	public static JsonWeatherService getInstance() {
		if (instance == null) {
			synchronized (JsonWeatherService.class) {
				if (instance == null) {
					instance = new JsonWeatherService(false);
				}
			}
		}
		return instance;
	}
	
	/**
	 * <b>This method is for internal use only.</b><br>
	 * <br>
	 * 
	 * Retrieves the singleton instance of the JsonWeatherService, optionally
	 * turning recording on or off.
	 * 
	 * @return
	 */
	static JsonWeatherService getRecordingInstance() {
		if (instance == null) {
			synchronized (JsonWeatherService.class) {
				if (instance == null) {
					instance = new JsonWeatherService(true);
				}
			}
		}
		return instance;
	}
	
	public String getWeather(final Coordinate location) throws WeatherException {
		String url = "http://forecast.weather.gov/MapClick.php";
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("lat", String.valueOf(location.getLatitude()));
		parameters.put("lon", String.valueOf(location.getLongitude()));
		parameters.put("FcstType", "json");
		if (this.local){
			return this.clientStore.getData(Util.hashRequest(url, parameters));
		}
		String jsonResponse = "";
		try {
			jsonResponse = Util.get(url, parameters);
			if (jsonResponse.startsWith("<")) {
				throw new WeatherLocationUnavailableException(jsonResponse);
			}
			return jsonResponse;
		} catch (IOException e) {
			throw new WeatherServiceNotAvailableException(e.toString());
		} catch (URISyntaxException e) {
			throw new WeatherServiceNotAvailableException(e.toString());
		}
	}

	public void getWeather(final Coordinate location,
			final JsonWeatherListener callback) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					callback.getWeatherCompleted(JsonWeatherService.getInstance().getWeather(location));
				} catch (WeatherException e) {
					callback.getWeatherFailed(e);
				}
			}
		};
		thread.start();
	}

	/**
	 * Establishes a connection to the online Business Search service.
	 * dataservice. This requires an internet connection.<br>
	 * <br>
	 **/
	@Override
	public void connect() {
		this.local = false;
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
		this.local = true;
	}
	
	/**
	 * <b>This is an internal method. Do not use it!</b><br>
	 * <br>
	 * 
	 * Internal method to access this instance's local data.
	 * 
	 * @return
	 */
	public ClientStore getClientStore() {
		return clientStore;
	}
	

	public static void main(String[] args) {
		JsonWeatherService jws = JsonWeatherService.getInstance();
		jws.connect();
		jws.getWeather(new Coordinate(40, -77), new JsonWeatherListener() {

			@Override
			public void getWeatherCompleted(String forecast) {
				System.out.println(forecast);
			}
			
			@Override
			public void getWeatherFailed(WeatherException error) {
				System.err.println(error.toString());
			}
			
		});
	}
}
