package realtimeweb.weatherservice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import realtimeweb.weatherservice.domain.*;
import realtimeweb.stickyweb.EditableCache;
import realtimeweb.stickyweb.StickyWeb;
import realtimeweb.stickyweb.StickyWebRequest;
import realtimeweb.stickyweb.StickyWebResponse;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceNotFoundException;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceParseException;
import realtimeweb.stickyweb.exceptions.StickyWebInternetException;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidPostArguments;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidQueryString;
import realtimeweb.stickyweb.exceptions.StickyWebJsonResponseParseException;
import realtimeweb.stickyweb.exceptions.StickyWebLoadDataSourceException;
import realtimeweb.stickyweb.exceptions.StickyWebNotInCacheException;

/**
 * Get a report of present weather and forecast data.
 */
public class WeatherService {
    private StickyWeb connection;
	private boolean online;
    
    public static void main(String[] args) {
        WeatherService weatherService = new WeatherService();
        Report myReport = weatherService.getReport("newark, de");
        List<Forecast> myForecasts = myReport.getForecasts();
        System.out.println(myForecasts.size());
        Forecast firstForecast = myForecasts.get(0);
        System.out.println(firstForecast.getProbabilityOfPrecipitation());
        
        // The following pre-generated code demonstrates how you can
		// use StickyWeb's EditableCache to create data files.
		try {
            // First, you create a new EditableCache, possibly passing in an FileInputStream to an existing cache
			EditableCache recording = new EditableCache();
            // You can add a Request object directly to the cache.
			String[] locations= {"newark, de", "newark,de",
					"blacksburg,va", "blacksburg, va",
					"minneapolis,mn", "minneapolis, mn",
					"seattle, wa", "seattle, wa",
					"nome, al", "nome,al",
					"miami,fl", "miami, fl",}; 
			for (String location : locations) {
				System.out.println(location);
				recording.addData(weatherService.geocodeRequest(location));
				Location l = weatherService.geocode(location);
				recording.addData(weatherService.getReportRequest(l.getLatitude(), l.getLongitude()));				
			}
            // Then you can save the expanded cache, possibly over the original
			recording.saveToStream(new FileOutputStream("cache.json"));
		} catch (StickyWebDataSourceNotFoundException e) {
			System.err.println("The given FileStream was not able to be found.");
		} catch (StickyWebDataSourceParseException e) {
			System.err.println("The given FileStream could not be parsed; possibly the structure is incorrect.");
		} catch (StickyWebLoadDataSourceException e) {
			System.err.println("The given data source could not be loaded.");
		} catch (FileNotFoundException e) {
			System.err.println("The given cache.json file was not found, or could not be opened.");
		}
        // ** End of how to use the EditableCache
		catch (StickyWebNotInCacheException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StickyWebInternetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StickyWebInvalidQueryString e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StickyWebInvalidPostArguments e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
    /**
     * Create a new, online connection to the service
     */
	public WeatherService() {
        this.online = true;
		try {
			this.connection = new StickyWeb(null);
		} catch (StickyWebDataSourceNotFoundException e) {
			System.err.println("The given datastream could not be loaded.");
		} catch (StickyWebDataSourceParseException e) {
			System.err.println("The given datastream could not be parsed");
		} catch (StickyWebLoadDataSourceException e) {
			System.err.println("The given data source could not be loaded");
		}
	}
	
    /**
     * Create a new, offline connection to the service.
     * @param cache The filename of the cache to be used.
     */
	public WeatherService(String cache) {
        // TODO: You might consider putting the cache directly into the jar file,
        // and not even exposing filenames!
        try {
            this.online = false;
            this.connection = new StickyWeb(new FileInputStream(cache));
        } catch (StickyWebDataSourceNotFoundException e) {
			System.err.println("The given data source could not be found.");
            System.exit(1);
		} catch (StickyWebDataSourceParseException e) {
			System.err.println("Could not read the data source. Perhaps its format is incorrect?");
            System.exit(1);
		} catch (StickyWebLoadDataSourceException e) {
			System.err.println("The given data source could not be read.");
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.err.println("The given cache file could not be found. Make sure it is in the right folder.");
			System.exit(1);
		}
	}
    
    
    private StickyWebRequest getReportRequest(Double latitude, Double longitude) {
        try {
            final String url = String.format("http://forecast.weather.gov/MapClick.php");
            HashMap<String, String> parameters = new HashMap<String, String>();
            // TODO: Validate the inputs here
            parameters.put("lat", String.valueOf(latitude));parameters.put("FcstType", "json");
            parameters.put("lon", String.valueOf(longitude));
            ArrayList<String> indexList = new ArrayList<String>();
            indexList.add("lat");
            indexList.add("FcstType");
            indexList.add("lon");
            
            return connection.get(url, parameters)
                            .setOnline(online)
                            .setIndexes(indexList);
        } catch (StickyWebDataSourceNotFoundException e) {
			System.err.println("Could not find the data source.");
		}
        return null;
    }
    
    /**
     * Given an address within the United States, returns the weather report. Notice that the address must be within the United States!
     * @param address A string like "Newark, de" or even something more specific like "112 Burruss Hall, Blacksburg, VA, 24061".
     * @return a report
     */
    public Report getReport(String address) {
    	return getReport(geocode(address));
    }
    
    private Report getReport(Location location) {
		return getReport(location.getLatitude(), location.getLongitude()); 
	}

	/**
     * Gets a report on the current weather, forecast, and more detailed information about the location.
    
     * @param cache The latitude (up-down) of the location to get information about.
     * @param cache The longitude (left-right) of the location to get information about.
     * @return a report
     */
	public Report getReport(Double latitude, Double longitude) {
        try {
			StickyWebRequest request =  getReportRequest(latitude, longitude);
            return new Report((Map<String, Object>)request.execute().asJSON());
		} catch (StickyWebNotInCacheException e) {
			System.err.println("There is no query in the cache for the given inputs. Perhaps something was mispelled?");
		} catch (StickyWebInternetException e) {
			System.err.println("Could not connect to the weather service. It might be your internet connection, or a problem with the web service.");
		} catch (StickyWebInvalidQueryString e) {
			System.err.println("The given arguments were invalid, and could not be turned into a query.");
		} catch (StickyWebInvalidPostArguments e) {
			System.err.println("The given arguments were invalid, and could not be turned into a query.");
        
        } catch (StickyWebJsonResponseParseException e) {
            System.err.println("The response from the weather server couldn't be understood. Most likely, this address was outside of the US.");
        
		}
		return null;
	}
	
	private StickyWebRequest geocodeRequest(String address) {
		try {
			address = address.toLowerCase();
            final String url = String.format("http://maps.googleapis.com/maps/api/geocode/json");
            HashMap<String, String> parameters = new HashMap<String, String>();
            // TODO: Validate the inputs here
            parameters.put("address", address);
            parameters.put("sensor", "true");
            ArrayList<String> indexList = new ArrayList<String>();
            indexList.add("address");
            
            return connection.get(url, parameters)
                            .setOnline(online)
                            .setIndexes(indexList);
        } catch (StickyWebDataSourceNotFoundException e) {
			System.err.println("Could not find the data source.");
		}
        return null;
	}
	
	public Location geocode(String address) {
		try {
			StickyWebRequest request =  geocodeRequest(address);
			Map<String, Object> result = (Map<String, Object>)request.execute().asJSON();
			switch (GeocodeError.valueOf((String) result.get("status"))) {
				case OK: return Location.fromGeocode(result);
				case REQUEST_DENIED: System.err.println("The given address was denied."); break;
				case ZERO_RESULTS: System.err.println("The given address could not be found.");break;
				case OVER_QUERY_LIMIT: System.err.println("the service has been used too many times today.");break;
				case INVALID_REQUEST: System.err.println("the given address was invalid.");break;
				case UNKNOWN_ERROR: System.err.println("a temporary error occurred; please try again.");break;
				case UNAVAILABLE: System.err.println("the given address is not available offline.");break;
			}
		} catch (StickyWebNotInCacheException e) {
			System.err.println("There is no query in the cache for the given inputs. Perhaps something was mispelled?");
		} catch (StickyWebInternetException e) {
			System.err.println("Could not connect to the geocoding service. It might be your internet connection, or a problem with the web service.");
		} catch (StickyWebInvalidQueryString e) {
			System.err.println("The given arguments were invalid, and could not be turned into a query.");
		} catch (StickyWebInvalidPostArguments e) {
			System.err.println("The given arguments were invalid, and could not be turned into a query.");        
        } catch (StickyWebJsonResponseParseException e) {
            System.err.println("The response from the geocoding server couldn't be understood.");
        
		}
		return null;
	}
    
}