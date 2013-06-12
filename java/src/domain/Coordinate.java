package domain;

public class Coordinate {
	public Coordinate(double latitude, double longitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public String toString() {
		return longitude + ", " + latitude;
	}
	
	private double longitude;
	private double latitude;
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
