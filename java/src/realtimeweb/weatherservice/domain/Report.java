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
 * A container for the weather, forecasts, and location information.
 */
public class Report {
	
	
	private Weather weather;
	private ArrayList<Forecast> forecasts;
	private Location location;
	
	
	/**
	 * The current weather for this location.
	
	 * @return Weather
	 */
	public Weather getWeather() {
		return this.weather;
	}
	
	/**
	 * 
	 * @param weather The current weather for this location.
	 */
	public void setWeather(Weather weather) {
		this.weather = weather;
	}
	
	/**
	 * The forecast for the next 7 days and 7 nights.
	
	 * @return ArrayList<Forecast>
	 */
	public ArrayList<Forecast> getForecasts() {
		return this.forecasts;
	}
	
	/**
	 * 
	 * @param forecasts The forecast for the next 7 days and 7 nights.
	 */
	public void setForecasts(ArrayList<Forecast> forecasts) {
		this.forecasts = forecasts;
	}
	
	/**
	 * More detailed information on this location.
	
	 * @return Location
	 */
	public Location getLocation() {
		return this.location;
	}
	
	/**
	 * 
	 * @param location More detailed information on this location.
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	
	
	/**
	 * A container for the weather, forecasts, and location information.
	
	 * @return String
	 */
	public String toString() {
		return "Report[" + weather + ", " + forecasts + ", " + location + "]";
	}
	
	/**
	 * Internal constructor to create a Report from a Json representation.
	 * @param json The raw json data that will be parsed.
	 * @param gson The Gson parser. See <a href='https://code.google.com/p/google-gson/'>https://code.google.com/p/google-gson/</a> for more information.
	 * @return 
	 */
	public  Report(JsonObject json, Gson gson) {
		this.weather = new Weather(json.get("data").getAsJsonObject().get("currentObservation").getAsJsonObject(), gson);
		this.forecasts = gson.fromJson(json.get("data").getAsJsonArray(), ArrayList<Forecast>.class);
		this.location = new Location(json.get("location").getAsJsonObject(), gson);
	}
	
	/**
	 * Regular constructor to create a Report.
	 * @param weather The current weather for this location.
	 * @param forecasts The forecast for the next 7 days and 7 nights.
	 * @param location More detailed information on this location.
	 * @return 
	 */
	public  Report(Weather weather, ArrayList<Forecast> forecasts, Location location) {
		this.weather = weather;
		this.forecasts = forecasts;
		this.location = location;
	}
	
}
