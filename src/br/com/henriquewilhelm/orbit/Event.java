package br.com.henriquewilhelm.orbit;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

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
 * @author Henrique Wilhelm v1.0.0
 * @author zoglmannk v1.0.0
 * @version v2.0.0
 */
public class Event {
	/**
	 * Name of Element of system solar
	 */
	private String name;
	/**
	 * Julian Date Number used in calculations
	 */
	private double julianDate;
	/**
	 * Date used in calculations
	 */
	private Date date;
	/**
	 * Element of system solar Rise
	 */
	private Time rise;
	/**
	 * Element of system solar Set
	 */
	private Time set;
	
	private double riseAzimuth, setAzimuth;
	/**
	 * Type eNum ( RISEN_AND_SET, NO_CHANGE_PREVIOUSLY_RISEN, NO_CHANGE_PREVIOUSLY_SET, ONLY_SET, ONLY_RISEN)
	 */
	private HorizonToHorizonCrossing type;
	/**
	 * Element of system solar meridianCrossing (Noon)
	 */
	private Time meridianCrossing;
	/**
	 * Element of system solar meridianCrossing (Midnigth)
	 */
	private Time antimeridianCrossing;
	private Time risenAmount, setAmount;
	/**
	 * currentDate Position
	 * Position (rightAscention, declination, longitudeEcliptic and distance)
	 */
	private Position position;
	/**
	 * tomorrow Position (currentDate +1)
	 * Position (rightAscention, declination, longitudeEcliptic and distance)
	 */
	private Position positionTomorrow;
	
	private String zodiac;
		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getJulianDate() {
		return julianDate;
	}

	public void setJulianDate(double julianDate) {
		this.julianDate = julianDate;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getRise() {
		return rise;
	}

	public void setRise(Time rise) {
		this.rise = rise;
	}

	public Time getSet() {
		return set;
	}

	public void setSet(Time set) {
		this.set = set;
	}

	public double getRiseAzimuth() {
		return riseAzimuth;
	}

	public void setRiseAzimuth(double riseAzimuth) {
		this.riseAzimuth = riseAzimuth;
	}

	public double getSetAzimuth() {
		return setAzimuth;
	}

	public void setSetAzimuth(double setAzimuth) {
		this.setAzimuth = setAzimuth;
	}

	public Time getMeridianCrossing() {
		return meridianCrossing;
	}

	public void setMeridianCrossing(Time meridianCrossing) {
		this.meridianCrossing = meridianCrossing;
	}

	public Time getAntimeridianCrossing() {
		return antimeridianCrossing;
	}

	public void setAntimeridianCrossing(Time antimeridianCrossing) {
		this.antimeridianCrossing = antimeridianCrossing;
	}

	public Time getRisenAmount() {
		return risenAmount;
	}

	public void setRisenAmount(Time risenAmount) {
		this.risenAmount = risenAmount;
	}

	public Time getSetAmount() {
		return setAmount;
	}

	public void setSetAmount(Time setAmount) {
		this.setAmount = setAmount;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPositionTomorrow() {
		return positionTomorrow;
	}

	public void setPositionTomorrow(Position positionTomorrow) {
		this.positionTomorrow = positionTomorrow;
	}

	public String getZodiac() {
		return zodiac;
	}

	public void setZodiac(String zodiac) {
		this.zodiac = zodiac;
	}
	
	public HorizonToHorizonCrossing getType() {
		return type;
	}

	public void setType(HorizonToHorizonCrossing type) {
		this.type = type;
	}

	/**
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

	@Override
	public String toString() {
		String str = getName() + "\t" +
				 " JulianDate "+ getJulianDate() + "\t" +
				 " Date " + getDate() + "\t";
		if(getRise() != null) {
			str = str + " Rise " + formatTimeAndAzimuth(getRise(), getRiseAzimuth()) + "\t" ;
		}
		if(getSet() != null) {
			str = str + " Set " + formatTimeAndAzimuth(getSet(), getSetAzimuth()) + "\t";
		}
		str = str + " Long. Ecliptic " + getPosition().getLongitudeEcliptic() + "\t" +
				 " Right Ascention " + getPosition().getRightAscention() + "\t" +
				 " Zodiac " + getZodiac() +"\n";
		return str;
	}
	
	/**
	 * Check azimuth (Double) in time, hour and minute.
	 * @param t Time 
	 * @param azimuth double value 
	 * @return String value 
	 */
	public String formatTimeAndAzimuth(Time t, double azimuth) {
		if(t == null) {
			return " ----------- None ----------- ";
		}
		
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		
		writer.printf("(%s, azimuth %5.1f degrees)",
				  replaceNull(t), azimuth);
		
		writer.flush();
		return sw.getBuffer().toString();
		
	}
	/**
	 * Return String value ("--") for invalidates time ( hour and minute )
	 * @param s Time
	 * @return String value ("--")
	 */
	private String replaceNull(Time s) {
		return s == null ? "--" : s.toString();
	}
}
