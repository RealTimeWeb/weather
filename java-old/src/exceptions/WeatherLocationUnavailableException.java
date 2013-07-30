package exceptions;

public class WeatherLocationUnavailableException extends WeatherException {

	public WeatherLocationUnavailableException(String error) {
		super(error);
		NAME = "There was no weather available for this location at this time.";
	}

}
