package br.com.henriquewilhelm.orbit;
/**
 * The Event class gathers the calculations of one element of the solar system  
 * (Sun, Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto)
 * The calculations are:
 * <p>Rise and set Time
 * HorizonToHorizonCrossing type
 * Time meridianCrossing (noon)
 * Time, antimeridianCrossing (Midnigth)
 * position (rightAscention, declination, longitudeEcliptic and distance)</p>
 * 
 * @author zoglmannk v1.0.0
 * @version v1.0.0
 */
public class Event {
	/**
	 * Name of Element of system solar
	 */
	public String name;
	/**
	 * Element of system solar Rise
	 */
	public Time rise;
	/**
	 * Element of system solar Set
	 */
	public Time set;
	
	public double riseAzimuth, setAzimuth;
	/**
	 * Type eNum ( RISEN_AND_SET, NO_CHANGE_PREVIOUSLY_RISEN, NO_CHANGE_PREVIOUSLY_SET, ONLY_SET, ONLY_RISEN)
	 */
	public HorizonToHorizonCrossing type;
	/**
	 * Element of system solar meridianCrossing (Noon)
	 */
	public Time meridianCrossing;
	/**
	 * Element of system solar meridianCrossing (Midnigth)
	 */
	public Time antimeridianCrossing;
	public Time risenAmount, setAmount;
	/**
	 * currentDate Position
	 * Position (rightAscention, declination, longitudeEcliptic and distance)
	 */
	public Position position;
	/**
	 * tomorrow Position (currentDate +1)
	 * Position (rightAscention, declination, longitudeEcliptic and distance)
	 */
	public Position positionTomorrow;
	/**
	 * Type of Horizon Crossing (NO_CHANGE_PREVIOUSLY_RISEN, NO_CHANGE_PREVIOUSLY_SET, ONLY_RISEN 
	 * ONLY_SET, RISEN_AND_SET) 
 	 * @author zoglmannk v1.0.0
	 */
	public enum HorizonToHorizonCrossing {
		RISEN_AND_SET,
		NO_CHANGE_PREVIOUSLY_RISEN,
		NO_CHANGE_PREVIOUSLY_SET,
		ONLY_SET,
		ONLY_RISEN
	}
}
