package realtimeweb.weatherservice.domain;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A detailed description of a location
 */
public class Location {
	
	
	private double latitude;
	private double longitude;
	private int elavation;
	private String name;
	
	
	/**
	 * The latitude (up-down) of this location.
	
	 * @return double
	 */
	public double getLatitude() {
		return this.latitude;
	}
	
	/**
	 * 
	 * @param latitude The latitude (up-down) of this location.
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * The longitude (left-right) of this location.
	
	 * @return double
	 */
	public double getLongitude() {
		return this.longitude;
	}
	
	/**
	 * 
	 * @param longitude The longitude (left-right) of this location.
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * The height above sea-level (in feet).
	
	 * @return int
	 */
	public int getElavation() {
		return this.elavation;
	}
	
	/**
	 * 
	 * @param elavation The height above sea-level (in feet).
	 */
	public void setElavation(int elavation) {
		this.elavation = elavation;
	}
	
	/**
	 * The city and state that this location is in.
	
	 * @return String
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 
	 * @param name The city and state that this location is in.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	/**
	 * A detailed description of a location
	
	 * @return String
	 */
	public String toString() {
		return "Location[" + latitude + ", " + longitude + ", " + elavation + ", " + name + "]";
	}
	
	/**
	 * Internal constructor to create a Location from a Json representation.
	 * @param json The raw json data that will be parsed.
	 * @param gson The Gson parser. See <a href='https://code.google.com/p/google-gson/'>https://code.google.com/p/google-gson/</a> for more information.
	 * @return 
	 */
	public  Location(JsonObject json, Gson gson) {
		this.latitude = json.get("latitude").getAsDouble();
		this.longitude = json.get("longitude").getAsDouble();
		this.elavation = json.get("elevation").getAsInt();
		this.name = json.get("areaDescription").getAsString();
	}
	
	/**
	 * Regular constructor to create a Location.
	 * @param latitude The latitude (up-down) of this location.
	 * @param longitude The longitude (left-right) of this location.
	 * @param elavation The height above sea-level (in feet).
	 * @param name The city and state that this location is in.
	 * @return 
	 */
	public  Location(double latitude, double longitude, int elavation, String name) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.elavation = elavation;
		this.name = name;
	}
	
}
