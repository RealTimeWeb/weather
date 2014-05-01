package realtimeweb.weatherservice.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




/**
 * A prediction for future weather.
 */
public class Forecast {
	
    private String longDescription;
    private String description;
    private String imageUrl;
    private String temperatureLabel;
    private String periodName;
    // This value can sometimes be null, so you should set that to 0.
    private Integer probabilityOfPrecipitation;
    private String periodTime;
    private Integer temperature;
    
    
    /**
     * @return A more-detailed, human-readable description of the predicted weather for this period.
     */
    public String getLongDescription() {
        return this.longDescription;
    }
    
    /**
     * @param A more-detailed, human-readable description of the predicted weather for this period.
     * @return String
     */
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }
    
    /**
     * @return A human-readable description of the predicted weather for this period.
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * @param A human-readable description of the predicted weather for this period.
     * @return String
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * @return A url pointing to a picture that describes the predicted weather for this period.
     */
    public String getImageUrl() {
        return this.imageUrl;
    }
    
    /**
     * @param A url pointing to a picture that describes the predicted weather for this period.
     * @return String
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    /**
     * @return Either 'High' or 'Low', depending on whether or not the predicted temperature is a daily high or a daily low.
     */
    public String getTemperatureLabel() {
        return this.temperatureLabel;
    }
    
    /**
     * @param Either 'High' or 'Low', depending on whether or not the predicted temperature is a daily high or a daily low.
     * @return String
     */
    public void setTemperatureLabel(String temperatureLabel) {
        this.temperatureLabel = temperatureLabel;
    }
    
    /**
     * @return A human-readable name for this time period (e.g. Tonight or Saturday).
     */
    public String getPeriodName() {
        return this.periodName;
    }
    
    /**
     * @param A human-readable name for this time period (e.g. Tonight or Saturday).
     * @return String
     */
    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }
    
    /**
     * @return The probability of precipitation for this period (as a percentage).
     */
    public Integer getProbabilityOfPrecipitation() {
        return this.probabilityOfPrecipitation;
    }
    
    /**
     * @param The probability of precipitation for this period (as a percentage).
     * @return Integer
     */
    public void setProbabilityOfPrecipitation(Integer probabilityOfPrecipitation) {
        this.probabilityOfPrecipitation = probabilityOfPrecipitation;
    }
    
    /**
     * @return A string representing the time that this period starts. Encoded as YYYY-MM-DDTHH:MM:SS, where the T is not a number, but a always present character (e.g. 2013-07-30T18:00:00).
     */
    public String getPeriodTime() {
        return this.periodTime;
    }
    
    /**
     * @param A string representing the time that this period starts. Encoded as YYYY-MM-DDTHH:MM:SS, where the T is not a number, but a always present character (e.g. 2013-07-30T18:00:00).
     * @return String
     */
    public void setPeriodTime(String periodTime) {
        this.periodTime = periodTime;
    }
    
    /**
     * @return The predicted temperature for this period (in Fahrenheit).
     */
    public Integer getTemperature() {
        return this.temperature;
    }
    
    /**
     * @param The predicted temperature for this period (in Fahrenheit).
     * @return Integer
     */
    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }
    
	
	/**
	 * Creates a string based representation of this Forecast.
	
	 * @return String
	 */
	public String toString() {
		return "Forecast[" +longDescription+", "+description+", "+imageUrl+", "+temperatureLabel+", "+periodName+", "+probabilityOfPrecipitation+", "+periodTime+", "+temperature+"]";
	}
	
	/**
	 * Internal constructor to create a Forecast from a json representation.
	 * @param map The raw json data that will be parsed.
	 * @return 
	 */
    @SuppressWarnings("unchecked")
	public Forecast(Map<String, Object> raw) {
        // TODO: Check that the data has the correct schema.
        // NOTE: It's much safer to check the Map for fields than to catch a runtime exception.
        try {
            this.longDescription = ((Map<String, Object>) raw.get("data")).get("text").toString();
            this.description = ((Map<String, Object>) raw.get("data")).get("weather").toString();
            this.imageUrl = ((Map<String, Object>) raw.get("data")).get("iconLink").toString();
            this.temperatureLabel = ((Map<String, Object>) raw.get("time")).get("tempLabel").toString();
            this.periodName = ((Map<String, Object>) raw.get("time")).get("startPeriodName").toString();
            this.probabilityOfPrecipitation = Integer.parseInt(((Map<String, Object>) raw.get("data")).get("pop").toString());
            this.periodTime = ((Map<String, Object>) raw.get("time")).get("startValidTime").toString();
            this.temperature = Integer.parseInt(((Map<String, Object>) raw.get("data")).get("temperature").toString());
        } catch (NullPointerException e) {
    		System.err.println("Could not convert the response to a Forecast; a field was missing.");
    		e.printStackTrace();
    	} catch (ClassCastException e) {
    		System.err.println("Could not convert the response to a Forecast; a field had the wrong structure.");
    		e.printStackTrace();
        }
    
	}
    
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

	@SuppressWarnings("unchecked")
	public static ArrayList<Forecast> parse(Map<String, Object> raw) {
		HashMap<String, Object> rawTime = (HashMap<String, Object>) raw.get("time");
		HashMap<String, Object> rawData = (HashMap<String, Object>) raw.get("data");
		
		ArrayList<String> periodNames = ((ArrayList<String>)rawTime.get("startPeriodName"));
		ArrayList<String> periodTimes = ((ArrayList<String>)rawTime.get("startValidTime"));
		ArrayList<String> tempLabels = ((ArrayList<String>)rawTime.get("tempLabel"));
		ArrayList<String> temperatures = ((ArrayList<String>)rawData.get("temperature"));
		ArrayList<String> pops = ((ArrayList<String>)rawData.get("pop"));
		for (int i = 0; i < pops.size(); i+= 1) {
			if (pops.get(i) == null) {
				pops.set(i, "0");
			}
		}
		ArrayList<String> weather = ((ArrayList<String>)rawData.get("weather"));
		ArrayList<String> icons = ((ArrayList<String>)rawData.get("iconLink"));
		ArrayList<String> text = ((ArrayList<String>)rawData.get("text"));

		ArrayList<Forecast> forecasts = new ArrayList<Forecast>();
		for (int i = 0; i < periodNames.size(); i+= 1) {
			forecasts.add(new Forecast(periodNames.get(i), periodTimes.get(i), tempLabels.get(i), 
					Integer.parseInt(temperatures.get(i)), Integer.parseInt(pops.get(i)), weather.get(i), icons.get(i), text.get(i)));
		}
		return forecasts;
	}	
}