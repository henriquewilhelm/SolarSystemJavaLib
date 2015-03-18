package br.com.henriquewilhelm.orbit;

import java.io.Serializable;

/**
 * GPS Coordinate (Latitude x Longitude)
 * 
 * @author zoglmannk v1.0.0
 * @version v1.0.0
 */

public class GpsCoordinate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1518328647115667495L;
	/**
	 * LATITUDE YOUR LOCATION (North latitudes positive)
	 */
	private final double latitude; 
	/**
	 * LONGITUDE YOUR LOCATION (West longitudes negative)
	 */
	private final double longitude;
	
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param latitude   north latitudes positive
	 * @param longitude  west longitudes negative
	 */
	public GpsCoordinate(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
