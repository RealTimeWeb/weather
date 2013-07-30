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
 * A prediction for future weather.
 */
public class Forecast {
	
	
	private String periodName;
	private String periodTime;
	private String temperatureLabel;
	private int temperature;
	private int probabilityOfPrecipitation;    //This value can sometimes be null, so you should set that to 0.
	private String description;
	private String imageUrl;
	private String longDescription;
	
	
	/**
	 * A human-readable name for this time period (e.g. Tonight or Saturday).
	
	 * @return String
	 */
	public String getPeriodName() {
		return this.periodName;
	}
	
	/**
	 * 
	 * @param periodName A human-readable name for this time period (e.g. Tonight or Saturday).
	 */
	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
	
	/**
	 * A string representing the time that this period starts. Encoded as YYYY-MM-DDTHH:MM:SS, where the T is not a number, but a always present character (e.g. 2013-07-30T18:00:00).
	
	 * @return String
	 */
	public String getPeriodTime() {
		return this.periodTime;
	}
	
	/**
	 * 
	 * @param periodTime A string representing the time that this period starts. Encoded as YYYY-MM-DDTHH:MM:SS, where the T is not a number, but a always present character (e.g. 2013-07-30T18:00:00).
	 */
	public void setPeriodTime(String periodTime) {
		this.periodTime = periodTime;
	}
	
	/**
	 * Either 'High' or 'Low', depending on whether or not the predicted temperature is a daily high or a daily low.
	
	 * @return String
	 */
	public String getTemperatureLabel() {
		return this.temperatureLabel;
	}
	
	/**
	 * 
	 * @param temperatureLabel Either 'High' or 'Low', depending on whether or not the predicted temperature is a daily high or a daily low.
	 */
	public void setTemperatureLabel(String temperatureLabel) {
		this.temperatureLabel = temperatureLabel;
	}
	
	/**
	 * The predicted temperature for this period (in Fahrenheit).
	
	 * @return int
	 */
	public int getTemperature() {
		return this.temperature;
	}
	
	/**
	 * 
	 * @param temperature The predicted temperature for this period (in Fahrenheit).
	 */
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	
	/**
	 * The probability of precipitation for this period (as a percentage).
	
	 * @return int
	 */
	public int getProbabilityOfPrecipitation() {
		return this.probabilityOfPrecipitation;
	}
	
	/**
	 * 
	 * @param probabilityOfPrecipitation The probability of precipitation for this period (as a percentage).
	 */
	public void setProbabilityOfPrecipitation(int probabilityOfPrecipitation) {
		this.probabilityOfPrecipitation = probabilityOfPrecipitation;
	}
	
	/**
	 * A human-readable description of the predicted weather for this period.
	
	 * @return String
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * 
	 * @param description A human-readable description of the predicted weather for this period.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * A url pointing to a picture that describes the predicted weather for this period.
	
	 * @return String
	 */
	public String getImageUrl() {
		return this.imageUrl;
	}
	
	/**
	 * 
	 * @param imageUrl A url pointing to a picture that describes the predicted weather for this period.
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	/**
	 * A more-detailed, human-readable description of the predicted weather for this period.
	
	 * @return String
	 */
	public String getLongDescription() {
		return this.longDescription;
	}
	
	/**
	 * 
	 * @param longDescription A more-detailed, human-readable description of the predicted weather for this period.
	 */
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	
	
	
	/**
	 * A prediction for future weather.
	
	 * @return String
	 */
	public String toString() {
		return "Forecast[" + periodName + ", " + periodTime + ", " + temperatureLabel + ", " + temperature + ", " + probabilityOfPrecipitation + ", " + description + ", " + imageUrl + ", " + longDescription + "]";
	}
	
	/**
	 * Internal constructor to create a Forecast from a Json representation.
	 * @param json The raw json data that will be parsed.
	 * @param gson The Gson parser. See <a href='https://code.google.com/p/google-gson/'>https://code.google.com/p/google-gson/</a> for more information.
	 * @return 
	 */
	public  Forecast(JsonObject json, Gson gson) {
		this.periodName = json.get("time").getAsJsonObject().get("startPeriodName").getAsString();
		this.periodTime = json.get("time").getAsJsonObject().get("startValidTime").getAsString();
		this.temperatureLabel = json.get("time").getAsJsonObject().get("tempLabel").getAsString();
		this.temperature = json.get("data").getAsJsonObject().get("temperature").getAsInt();
		this.probabilityOfPrecipitation = json.get("data").getAsJsonObject().get("pop").getAsInt();
		this.description = json.get("data").getAsJsonObject().get("weather").getAsString();
		this.imageUrl = json.get("data").getAsJsonObject().get("iconLink").getAsString();
		this.longDescription = json.get("data").getAsJsonObject().get("text").getAsString();
	}
	
	/**
	 * Regular constructor to create a Forecast.
	 * @param periodName A human-readable name for this time period (e.g. Tonight or Saturday).
	 * @param periodTime A string representing the time that this period starts. Encoded as YYYY-MM-DDTHH:MM:SS, where the T is not a number, but a always present character (e.g. 2013-07-30T18:00:00).
	 * @param temperatureLabel Either 'High' or 'Low', depending on whether or not the predicted temperature is a daily high or a daily low.
	 * @param temperature The predicted temperature for this period (in Fahrenheit).
	 * @param probabilityOfPrecipitation The probability of precipitation for this period (as a percentage).
	 * @param description A human-readable description of the predicted weather for this period.
	 * @param imageUrl A url pointing to a picture that describes the predicted weather for this period.
	 * @param longDescription A more-detailed, human-readable description of the predicted weather for this period.
	 * @return 
	 */
	public  Forecast(String periodName, String periodTime, String temperatureLabel, int temperature, int probabilityOfPrecipitation, String description, String imageUrl, String longDescription) {
		this.periodName = periodName;
		this.periodTime = periodTime;
		this.temperatureLabel = temperatureLabel;
		this.temperature = temperature;
		this.probabilityOfPrecipitation = probabilityOfPrecipitation;
		this.description = description;
		this.imageUrl = imageUrl;
		this.longDescription = longDescription;
	}

	public static ArrayList<Forecast> parse(JsonObject top, Gson gson) {
		JsonObject rawTime = top.get("time").getAsJsonObject();
		JsonObject rawData = top.get("data").getAsJsonObject();
		
		String[] periodNames = gson.fromJson(rawTime.get("startPeriodName").getAsJsonArray(), String[].class);
		String[] periodTimes = gson.fromJson(rawTime.get("startValidTime").getAsJsonArray(), String[].class);
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
		String[] icons = gson.fromJson(rawData.get("iconLink").getAsJsonArray(), String[].class);
		String[] text = gson.fromJson(rawData.get("text").getAsJsonArray(), String[].class);

		ArrayList<Forecast> forecasts = new ArrayList<Forecast>();
		for (int i = 0; i < periodNames.length; i+= 1) {
			forecasts.add(new Forecast(periodNames[i], periodTimes[i], tempLabels[i], 
					temperatures[i], pops[i], weather[i], icons[i], text[i]));
		}
		return forecasts;
	}
	
}
