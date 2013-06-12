package domain;
import java.net.URL;
import java.util.Date;


public class ForecastIndex {
	private String name;
	private Date date;
	private String temperatureLabel;
	private int temperature;
f	private int probabilityOfPrecipitation;
	private String weather;
	private URL icon;
	private String text;
	
	ForecastIndex(String name) {
		this.name = name;
	}
	ForecastIndex(String name, Date date, String temperatureLabel,
			int temperature, int probabilityOfPrecipitation, String weather,
			URL icon, String text) {
		super();
		this.name = name;
		this.date = date;
		this.temperatureLabel = temperatureLabel;
		this.temperature = temperature;
		this.probabilityOfPrecipitation = probabilityOfPrecipitation;
		this.weather = weather;
		this.icon = icon;
		this.text = text;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @return the temperatureLabel
	 */
	public String getTemperatureLabel() {
		return temperatureLabel;
	}
	/**
	 * @return the temperature
	 */
	public int getTemperature() {
		return temperature;
	}
	/**
	 * @return the probabilityOfPrecipitation
	 */
	public int getProbabilityOfPrecipitation() {
		return probabilityOfPrecipitation;
	}
	/**
	 * @return the weather
	 */
	public String getWeather() {
		return weather;
	}
	/**
	 * @return the icon
	 */
	public URL getIcon() {
		return icon;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param name the name to set
	 */
	void setName(String name) {
		this.name = name;
	}
	/**
	 * @param date the date to set
	 */
	void setDate(Date date) {
		this.date = date;
	}
	/**
	 * @param temperatureLabel the temperatureLabel to set
	 */
	void setTemperatureLabel(String temperatureLabel) {
		this.temperatureLabel = temperatureLabel;
	}
	/**
	 * @param temperature the temperature to set
	 */
	void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	/**
	 * @param probabilityOfPrecipitation the probabilityOfPrecipitation to set
	 */
	void setProbabilityOfPrecipitation(int probabilityOfPrecipitation) {
		this.probabilityOfPrecipitation = probabilityOfPrecipitation;
	}
	/**
	 * @param weather the weather to set
	 */
	void setWeather(String weather) {
		this.weather = weather;
	}
	/**
	 * @param icon the icon to set
	 */
	void setIcon(URL icon) {
		this.icon = icon;
	}
	/**
	 * @param text the text to set
	 */
	void setText(String text) {
		this.text = text;
	}
}
