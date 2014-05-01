package realtimeweb.weatherservice.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




/**
 * A detailed description of a location
 */
public class Location {
	
    private Double latitude;
    private Integer elevation;
    private String name;
    private Double longitude;
    
    
    /**
     * @return The latitude (up-down) of this location.
     */
    public Double getLatitude() {
        return this.latitude;
    }
    
    /**
     * @param The latitude (up-down) of this location.
     * @return Double
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    /**
     * @return The height above sea-level (in feet).
     */
    public Integer getElevation() {
        return this.elevation;
    }
    
    /**
     * @param The height above sea-level (in feet).
     * @return Integer
     */
    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }
    
    /**
     * @return The city and state that this location is in.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * @param The city and state that this location is in.
     * @return String
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return The longitude (left-right) of this location.
     */
    public Double getLongitude() {
        return this.longitude;
    }
    
    /**
     * @param The longitude (left-right) of this location.
     * @return Double
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
	
	/**
	 * Creates a string based representation of this Location.
	
	 * @return String
	 */
	public String toString() {
		return "Location[" +latitude+", "+elevation+", "+name+", "+longitude+"]";
	}
	
	/**
	 * Internal constructor to create a Location from a json representation.
	 * @param map The raw json data that will be parsed.
	 * @return 
	 */
    public Location(Map<String, Object> raw) {
        // TODO: Check that the data has the correct schema.
        // NOTE: It's much safer to check the Map for fields than to catch a runtime exception.
        try {
            this.latitude = Double.parseDouble(raw.get("latitude").toString());
            this.elevation = Integer.parseInt(raw.get("elevation").toString());
            this.name = raw.get("areaDescription").toString();
            this.longitude = Double.parseDouble(raw.get("longitude").toString());
        } catch (NullPointerException e) {
    		System.err.println("Could not convert the response to a Location; a field was missing.");
    		e.printStackTrace();
    	} catch (ClassCastException e) {
    		System.err.println("Could not convert the response to a Location; a field had the wrong structure.");
    		e.printStackTrace();
        }
    
	}
    
    public static Location fromGeocode(Map<String, Object> raw) {
    	try {
    		Map<String, Object> firstResult = (Map<String, Object>) ((List<Object>)raw.get("results")).get(0);
    		Map<String, Object> location = (Map<String, Object>) ((Map<String, Object>)firstResult.get("geometry")).get("location");
            Double latitude = Double.parseDouble(location.get("lat").toString());
            Double longitude = Double.parseDouble(location.get("lng").toString());
            String name = (String) firstResult.get("formatted_address");
            return new Location(latitude, longitude, name, 0);
        } catch (NullPointerException e) {
    		System.err.println("Could not convert the geocode response to a Location; a field was missing.");
    		e.printStackTrace();
    	} catch (ClassCastException e) {
    		System.err.println("Could not convert the geocode response to a Location; a field had the wrong structure.");
    		e.printStackTrace();
        }
		return null;
    }
    
    public Location(double latitude, double longitude, String name, int elevation) {
    	this.latitude = latitude;
    	this.longitude = longitude;
    	this.name = name;
    	this.elevation = elevation;
    }
}