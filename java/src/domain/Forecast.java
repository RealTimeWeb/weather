package domain;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import exceptions.WeatherException;

public class Forecast {
	/**
	 * @return the location
	 */
	public Coordinate getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Coordinate location) {
		this.location = location;
	}

	/**
	 * @return the elevation
	 */
	public int getElevation() {
		return elevation;
	}

	/**
	 * @param elevation the elevation to set
	 */
	public void setElevation(int elevation) {
		this.elevation = elevation;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the indices
	 */
	public ForecastIndex[] getIndices() {
		return indices;
	}

	/**
	 * @param indices the indices to set
	 */
	public void setIndices(ForecastIndex[] indices) {
		this.indices = indices;
	}

	/**
	 * @return the currentTemperature
	 */
	public int getCurrentTemperature() {
		return currentTemperature;
	}

	/**
	 * @param currentTemperature the currentTemperature to set
	 */
	public void setCurrentTemperature(int currentTemperature) {
		this.currentTemperature = currentTemperature;
	}

	/**
	 * @return the currentDewPoint
	 */
	public int getCurrentDewPoint() {
		return currentDewPoint;
	}

	/**
	 * @param currentDewPoint the currentDewPoint to set
	 */
	public void setCurrentDewPoint(int currentDewPoint) {
		this.currentDewPoint = currentDewPoint;
	}

	/**
	 * @return the currentRelativeHumidity
	 */
	public int getCurrentRelativeHumidity() {
		return currentRelativeHumidity;
	}

	/**
	 * @param currentRelativeHumidity the currentRelativeHumidity to set
	 */
	public void setCurrentRelativeHumidity(int currentRelativeHumidity) {
		this.currentRelativeHumidity = currentRelativeHumidity;
	}

	/**
	 * @return the windSpeed
	 */
	public int getWindSpeed() {
		return windSpeed;
	}

	/**
	 * @param windSpeed the windSpeed to set
	 */
	public void setWindSpeed(int windSpeed) {
		this.windSpeed = windSpeed;
	}

	/**
	 * @return the windDirection
	 */
	public int getWindDirection() {
		return windDirection;
	}

	/**
	 * @param windDirection the windDirection to set
	 */
	public void setWindDirection(int windDirection) {
		this.windDirection = windDirection;
	}

	/**
	 * @return the gust
	 */
	public int getGust() {
		return gust;
	}

	/**
	 * @param gust the gust to set
	 */
	public void setGust(int gust) {
		this.gust = gust;
	}

	/**
	 * @return the weather
	 */
	public String getWeather() {
		return weather;
	}

	/**
	 * @param weather the weather to set
	 */
	public void setWeather(String weather) {
		this.weather = weather;
	}

	/**
	 * @return the visibility
	 */
	public double getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility the visibility to set
	 */
	public void setVisibility(double visibility) {
		this.visibility = visibility;
	}

	/**
	 * @return the windChill
	 */
	public int getWindChill() {
		return windChill;
	}

	/**
	 * @param windChill the windChill to set
	 */
	public void setWindChill(int windChill) {
		this.windChill = windChill;
	}


	private Coordinate location;
	private int elevation;
	private String name;
	private ForecastIndex[] indices;
	private int currentTemperature;
	private int currentDewPoint;
	private int currentRelativeHumidity;
	private int windSpeed;
	private int windDirection;
	private int gust;
	private String weather;
	private double visibility;
	private int windChill;

	public String toString() {
		return "Weather["+location+": "+name+"]";
	}


	public Forecast(String json) throws WeatherException {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
		JsonParser parser = new JsonParser();
		JsonObject top = parser.parse(json).getAsJsonObject();
		
		JsonObject rawLocation = top.get("location").getAsJsonObject();
		double latitude = rawLocation.get("latitude").getAsDouble();
		double longitude = rawLocation.get("longitude").getAsDouble();
		this.location = new Coordinate(latitude, longitude);
		this.elevation = rawLocation.get("elevation").getAsInt();
		this.name = rawLocation.get("areaDescription").getAsString();
		
		JsonObject rawTime = top.get("time").getAsJsonObject();
		JsonObject rawData = top.get("data").getAsJsonObject();
		
		String[] periodNames = gson.fromJson(rawTime.get("startPeriodName").getAsJsonArray(), String[].class);
		Date[] periodTimes = gson.fromJson(rawTime.get("startValidTime").getAsJsonArray(), Date[].class);
		String[] tempLabels = gson.fromJson(rawTime.get("tempLabel").getAsJsonArray(), String[].class);
		int[] temperatures = gson.fromJson(rawData.get("temperature").getAsJsonArray(), int[].class);
		// POP is sometimes reported as "null", which means 0% chance. For your inconvenience :)
		JsonArray pops_array = rawData.get("pop").getAsJsonArray();
		int[] pops = new int[pops_array.size()];
		for (int i = 0; i < pops.length; i+= 1) {
			if (pops_array.get(i).isJsonNull()) {
				pops[i] = 0;
			} else {
				pops[i] = pops_array.get(i).getAsInt();
			}
		}
		String[] weather = gson.fromJson(rawData.get("weather").getAsJsonArray(), String[].class);
		URL[] icons = gson.fromJson(rawData.get("iconLink").getAsJsonArray(), URL[].class);
		String[] text = gson.fromJson(rawData.get("text").getAsJsonArray(), String[].class);

		Vector<ForecastIndex> forecasts = new Vector<ForecastIndex>(15);
		for (int i = 0; i < periodNames.length; i+= 1) {
			forecasts.add(new ForecastIndex(periodNames[i], periodTimes[i], tempLabels[i], 
					temperatures[i], pops[i], weather[i], icons[i], text[i]));
		}
		this.indices = new ForecastIndex[forecasts.size()];
		this.indices = forecasts.toArray(this.indices);

		JsonObject rawCurrent = top.get("currentobservation").getAsJsonObject();
		this.currentTemperature = rawCurrent.get("Temp").getAsInt();
		this.currentDewPoint = rawCurrent.get("Dewp").getAsInt();
		this.currentRelativeHumidity= rawCurrent.get("Relh").getAsInt();
		this.windSpeed= rawCurrent.get("Winds").getAsInt();
		this.windDirection= rawCurrent.get("Windd").getAsInt();
		this.gust= rawCurrent.get("Gust").getAsInt();
		this.weather = rawCurrent.get("Weather").getAsString();
		this.visibility = rawCurrent.get("Visibility").getAsDouble();
		this.windChill = rawCurrent.get("WindChill").getAsInt();
	}
}
