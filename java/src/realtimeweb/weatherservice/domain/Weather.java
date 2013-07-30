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
 * A structured representation the current weather.
 */
public class Weather {
	
	
	private int temp;
	private int dewpoint;
	private int humidity;
	private int windSpeed;
	private int windDirection;
	private String description;
	private String imageUrl;
	private double visibility;
	private int windchill;
	private double pressure;
	
	
	/**
	 * The current temperature (in Fahrenheit).
	
	 * @return int
	 */
	public int getTemp() {
		return this.temp;
	}
	
	/**
	 * 
	 * @param temp The current temperature (in Fahrenheit).
	 */
	public void setTemp(int temp) {
		this.temp = temp;
	}
	
	/**
	 * The current dewpoint temperature (in Fahrenheit).
	
	 * @return int
	 */
	public int getDewpoint() {
		return this.dewpoint;
	}
	
	/**
	 * 
	 * @param dewpoint The current dewpoint temperature (in Fahrenheit).
	 */
	public void setDewpoint(int dewpoint) {
		this.dewpoint = dewpoint;
	}
	
	/**
	 * The current relative humidity (as a percentage).
	
	 * @return int
	 */
	public int getHumidity() {
		return this.humidity;
	}
	
	/**
	 * 
	 * @param humidity The current relative humidity (as a percentage).
	 */
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	
	/**
	 * The current wind speed (in miles-per-hour).
	
	 * @return int
	 */
	public int getWindSpeed() {
		return this.windSpeed;
	}
	
	/**
	 * 
	 * @param windSpeed The current wind speed (in miles-per-hour).
	 */
	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
	}
	
	/**
	 * The current wind direction (in degrees).
	
	 * @return int
	 */
	public int getWindDirection() {
		return this.windDirection;
	}
	
	/**
	 * 
	 * @param windDirection The current wind direction (in degrees).
	 */
	public void setWindDirection(int windDirection) {
		this.windDirection = windDirection;
	}
	
	/**
	 * A human-readable description of the current weather.
	
	 * @return String
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * 
	 * @param description A human-readable description of the current weather.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * A url pointing to a picture that describes the weather.
	
	 * @return String
	 */
	public String getImageUrl() {
		return this.imageUrl;
	}
	
	/**
	 * 
	 * @param imageUrl A url pointing to a picture that describes the weather.
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	/**
	 * How far you can see (in miles).
	
	 * @return double
	 */
	public double getVisibility() {
		return this.visibility;
	}
	
	/**
	 * 
	 * @param visibility How far you can see (in miles).
	 */
	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}
	
	/**
	 * The perceived temperature (in Fahrenheit).
	
	 * @return int
	 */
	public int getWindchill() {
		return this.windchill;
	}
	
	/**
	 * 
	 * @param windchill The perceived temperature (in Fahrenheit).
	 */
	public void setWindchill(int windchill) {
		this.windchill = windchill;
	}
	
	/**
	 * The barometric pressure (in inches).
	
	 * @return double
	 */
	public double getPressure() {
		return this.pressure;
	}
	
	/**
	 * 
	 * @param pressure The barometric pressure (in inches).
	 */
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
	
	
	
	/**
	 * A structured representation the current weather.
	
	 * @return String
	 */
	public String toString() {
		return "Weather[" + temp + ", " + dewpoint + ", " + humidity + ", " + windSpeed + ", " + windDirection + ", " + description + ", " + imageUrl + ", " + visibility + ", " + windchill + ", " + pressure + "]";
	}
	
	/**
	 * Internal constructor to create a Weather from a Json representation.
	 * @param json The raw json data that will be parsed.
	 * @param gson The Gson parser. See <a href='https://code.google.com/p/google-gson/'>https://code.google.com/p/google-gson/</a> for more information.
	 * @return 
	 */
	public  Weather(JsonObject json, Gson gson) {
		this.temp = json.get("Temp").getAsInt();
		this.dewpoint = json.get("Dewp").getAsInt();
		this.humidity = json.get("Relh").getAsInt();
		this.windSpeed = json.get("Winds").getAsInt();
		this.windDirection = json.get("Windd").getAsInt();
		this.description = json.get("Weather").getAsString();
		this.imageUrl = json.get("Weatherimage").getAsString();
		this.visibility = json.get("Visibility").getAsDouble();
		this.windchill = json.get("WindChill").getAsInt();
		this.pressure = json.get("SLP").getAsDouble();
	}
	
	/**
	 * Regular constructor to create a Weather.
	 * @param temp The current temperature (in Fahrenheit).
	 * @param dewpoint The current dewpoint temperature (in Fahrenheit).
	 * @param humidity The current relative humidity (as a percentage).
	 * @param windSpeed The current wind speed (in miles-per-hour).
	 * @param windDirection The current wind direction (in degrees).
	 * @param description A human-readable description of the current weather.
	 * @param imageUrl A url pointing to a picture that describes the weather.
	 * @param visibility How far you can see (in miles).
	 * @param windchill The perceived temperature (in Fahrenheit).
	 * @param pressure The barometric pressure (in inches).
	 * @return 
	 */
	public  Weather(int temp, int dewpoint, int humidity, int windSpeed, int windDirection, String description, String imageUrl, double visibility, int windchill, double pressure) {
		this.temp = temp;
		this.dewpoint = dewpoint;
		this.humidity = humidity;
		this.windSpeed = windSpeed;
		this.windDirection = windDirection;
		this.description = description;
		this.imageUrl = imageUrl;
		this.visibility = visibility;
		this.windchill = windchill;
		this.pressure = pressure;
	}
	
}
