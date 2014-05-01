package realtimeweb.weatherservice.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




/**
 * A structured representation the current weather.
 */
public class Weather {
	
    private Integer windSpeed;
    private Integer windchill;
    private Integer dewpoint;
    private String imageUrl;
    private Integer windDirection;
    private Double visibility;
    private Integer humidity;
    private Double pressure;
    private Integer temp;
    private String description;
    
    
    /**
     * @return The current wind speed (in miles-per-hour).
     */
    public Integer getWindSpeed() {
        return this.windSpeed;
    }
    
    /**
     * @param The current wind speed (in miles-per-hour).
     * @return Integer
     */
    public void setWindSpeed(Integer windSpeed) {
        this.windSpeed = windSpeed;
    }
    
    /**
     * @return The perceived temperature (in Fahrenheit).
     */
    public Integer getWindchill() {
        return this.windchill;
    }
    
    /**
     * @param The perceived temperature (in Fahrenheit).
     * @return Integer
     */
    public void setWindchill(Integer windchill) {
        this.windchill = windchill;
    }
    
    /**
     * @return The current dewpoint temperature (in Fahrenheit).
     */
    public Integer getDewpoint() {
        return this.dewpoint;
    }
    
    /**
     * @param The current dewpoint temperature (in Fahrenheit).
     * @return Integer
     */
    public void setDewpoint(Integer dewpoint) {
        this.dewpoint = dewpoint;
    }
    
    /**
     * @return A url pointing to a picture that describes the weather.
     */
    public String getImageUrl() {
        return this.imageUrl;
    }
    
    /**
     * @param A url pointing to a picture that describes the weather.
     * @return String
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    /**
     * @return The current wind direction (in degrees).
     */
    public Integer getWindDirection() {
        return this.windDirection;
    }
    
    /**
     * @param The current wind direction (in degrees).
     * @return Integer
     */
    public void setWindDirection(Integer windDirection) {
        this.windDirection = windDirection;
    }
    
    /**
     * @return How far you can see (in miles).
     */
    public Double getVisibility() {
        return this.visibility;
    }
    
    /**
     * @param How far you can see (in miles).
     * @return Double
     */
    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }
    
    /**
     * @return The current relative humidity (as a percentage).
     */
    public Integer getHumidity() {
        return this.humidity;
    }
    
    /**
     * @param The current relative humidity (as a percentage).
     * @return Integer
     */
    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }
    
    /**
     * @return The barometric pressure (in inches).
     */
    public Double getPressure() {
        return this.pressure;
    }
    
    /**
     * @param The barometric pressure (in inches).
     * @return Double
     */
    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }
    
    /**
     * @return The current temperature (in Fahrenheit).
     */
    public Integer getTemp() {
        return this.temp;
    }
    
    /**
     * @param The current temperature (in Fahrenheit).
     * @return Integer
     */
    public void setTemp(Integer temp) {
        this.temp = temp;
    }
    
    /**
     * @return A human-readable description of the current weather.
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * @param A human-readable description of the current weather.
     * @return String
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
	
	/**
	 * Creates a string based representation of this Weather.
	
	 * @return String
	 */
	public String toString() {
		return "Weather[" +windSpeed+", "+windchill+", "+dewpoint+", "+imageUrl+", "+windDirection+", "+visibility+", "+humidity+", "+pressure+", "+temp+", "+description+"]";
	}
	
	/**
	 * Internal constructor to create a Weather from a json representation.
	 * @param map The raw json data that will be parsed.
	 * @return 
	 */
    public Weather(Map<String, Object> raw) {
        // TODO: Check that the data has the correct schema.
        // NOTE: It's much safer to check the Map for fields than to catch a runtime exception.
        try {
            this.windSpeed = Integer.parseInt(raw.get("Winds").toString());
            this.windchill = Integer.parseInt(raw.get("WindChill").toString());
            this.dewpoint = Integer.parseInt(raw.get("Dewp").toString());
            this.imageUrl = raw.get("Weatherimage").toString();
            this.windDirection = Integer.parseInt(raw.get("Windd").toString());
            this.visibility = Double.parseDouble(raw.get("Visibility").toString());
            this.humidity = Integer.parseInt(raw.get("Relh").toString());
            this.pressure = Double.parseDouble(raw.get("SLP").toString());
            this.temp = Integer.parseInt(raw.get("Temp").toString());
            this.description = raw.get("Weather").toString();
        } catch (NullPointerException e) {
    		System.err.println("Could not convert the response to a Weather; a field was missing.");
    		e.printStackTrace();
    	} catch (ClassCastException e) {
    		System.err.println("Could not convert the response to a Weather; a field had the wrong structure.");
    		e.printStackTrace();
        }
    
	}	
}