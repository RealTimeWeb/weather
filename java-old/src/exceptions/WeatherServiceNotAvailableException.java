package exceptions;

/**
 * For whatever reason, the weather service could not be connected to.
 * @author acbart
 *
 */
public class WeatherServiceNotAvailableException extends WeatherException {

	public WeatherServiceNotAvailableException(String error) {
		super(error);
		NAME = "Weather Service Not Available Exception";
	}

}
