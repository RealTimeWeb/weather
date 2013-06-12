package json;

import exceptions.WeatherException;

/**
 * A listener for handling data received about the weather. On success,
 * a string of JSON containing all the information about the forecast will be
 * passed to the getWeatherCompleted method, which must be overridden in any
 * implementing classes. On failure, an exception is passed to the
 * getWeatherFailed method, which must also be overridden in any implementing
 * class.<br>
 * <br>
 * 
 * @author acbart
 * 
 */
public interface JsonWeatherListener {
	/**
	 * The method that should be overridden to handle the JSON-formatted
	 * weather forecast.
	 * 
	 * @param businessData
	 */
	public abstract void getWeatherCompleted(String forecast);

	/**
	 * The method that should be overridden to handle the Exception that occurs
	 * while getting the weather forecast.
	 * 
	 * @param exception
	 */
	public abstract void getWeatherFailed(WeatherException exception);
}
