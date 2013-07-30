package exceptions;


public class WeatherServiceReturnedErrorException extends WeatherException {

	public WeatherServiceReturnedErrorException(String error) {
		super(error);
		NAME = "Weather Service Return Could Not Be Parsed";
	}

}
