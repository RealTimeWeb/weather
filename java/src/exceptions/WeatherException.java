package exceptions;

public class WeatherException extends Exception {
	private String error;
	protected String NAME = "Generic Weather Service Exception";

	public WeatherException(String error) {
		this.error = error;
	}
	
	public String toString() {
		return NAME + ": "+this.error;
	}
}
