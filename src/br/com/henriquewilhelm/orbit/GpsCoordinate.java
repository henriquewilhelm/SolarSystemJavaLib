package br.com.henriquewilhelm.orbit;

/**
 * GPS Coordinate (Latitude x Longitude)
 * 
 * @author zoglmannk v1.0.0
 * @version v1.0.0
 */

public class GpsCoordinate {
	/**
	 * LATITUDE YOUR LOCATION (North latitudes positive)
	 */
	public final double latitude; 
	/**
	 * LONGITUDE YOUR LOCATION (West longitudes negative)
	 */
	public final double longitude;

	/**
	 * @param latitude   north latitudes positive
	 * @param longitude  west longitudes negative
	 */
	public GpsCoordinate(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
