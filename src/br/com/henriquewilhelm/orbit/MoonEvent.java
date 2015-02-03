package br.com.henriquewilhelm.orbit;

import java.util.Date;

/**
 * The class extends of Event class, gathers the calculations of Moon
 * The calculations are:
 * <p>Rise and set Time,
 * HorizonToHorizonCrossing type,
 * Time meridianCrossing (noon),
 * Time antimeridianCrossing (Midnigth),
 * Position (rightAscention, declination, longitudeEcliptic and distance)</p>
 * 
 * @author zoglmannk v1.0.0
 * @version v1.0.0
 */
public class MoonEvent extends Event {
	/**
	 * Age in Days (1 to 28)
	 */
	public double ageInDays;
	/**
	 * Percent Illumination of Moon
	 */
	public double illuminationPercent;
	/**
	 * String Phase
	 */
	public String phase;
	/**
	 * String Perigee Or Apogee
	 */
	public String perigeeOrApogee;
	
	/**
	 * Construtor without Event
	 */
	public MoonEvent() { }
	/**
	 * Construtor with MoonEvent
	 * @param event Event class
	 */
	public MoonEvent(Event event) {
		this.rise = event.rise;
		this.set = event.set;
		this.riseAzimuth = event.riseAzimuth;
		this.setAzimuth  = event.setAzimuth;
		this.type = event.type;
		this.meridianCrossing = event.meridianCrossing;
		this.antimeridianCrossing = event.antimeridianCrossing;
		this.risenAmount = event.risenAmount;
		this.setAmount = event.setAmount;
		this.position = event.position;
		this.zodiac = event.zodiac;
	}
}
