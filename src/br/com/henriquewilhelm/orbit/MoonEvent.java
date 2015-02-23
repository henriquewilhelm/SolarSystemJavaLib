package br.com.henriquewilhelm.orbit;

import java.io.PrintWriter;
import java.io.StringWriter;

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
	private double ageInDays;
	/**
	 * Percent Illumination of Moon
	 */
	private double illuminationPercent;
	/**
	 * String Phase
	 */
	private String phase;
	/**
	 * String Perigee Or Apogee
	 */
	private String perigeeOrApogee;
	
	private double anglePhase;
	
	//Getters and Setters
	public double getAgeInDays() {
		return ageInDays;
	}
	public void setAgeInDays(double ageInDays) {
		this.ageInDays = ageInDays;
	}
	public double getIlluminationPercent() {
		return illuminationPercent;
	}
	public void setIlluminationPercent(double illuminationPercent) {
		this.illuminationPercent = illuminationPercent;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public String getPerigeeOrApogee() {
		return perigeeOrApogee;
	}
	public void setPerigeeOrApogee(String perigeeOrApogee) {
		this.perigeeOrApogee = perigeeOrApogee;
	}
	
	public double getAnglePhase() {
		return anglePhase;
	}
	public void setAnglePhase(double anglePhase) {
		this.anglePhase = anglePhase;
	}
	/**
	 * Construtor without Event
	 */
	public MoonEvent() { }
	/**
	 * Construtor with MoonEvent
	 * @param event Event class
	 */
	public MoonEvent(Event event) {
		setRise(event.getRise());
		setSet(event.getSet());
		setRiseAzimuth(event.getRiseAzimuth());
		setSetAzimuth(event.getSetAzimuth());
		setType(event.getType());
		setMeridianCrossing(event.getMeridianCrossing());
		setAntimeridianCrossing(event.getAntimeridianCrossing());
		setRisenAmount(event.getRisenAmount());
		setSetAmount(event.getSetAmount());
		setPosition(event.getPosition());
		setJulianDate(event.getJulianDate());
		setDate(event.getDate());
		setZodiac(event.getZodiac());
		setName("Moon");
	}
	
	@Override
	public String toString() {
		String str;
		str = getName() + "\t" +
				 " Julian Date "+ getJulianDate() + "\t" +
				 " Date " + getDate() + "\t";
		if(getRise() != null) {
			str = str + " Moonrise " + formatTimeAndAzimuth(getRise(), getRiseAzimuth()) + "\t";
		}
		if(getSet() != null) {
			str = str +  " Moonset " + formatTimeAndAzimuth(getSet(), getSetAzimuth()) + "\t";
		}
		str = str +  " Moon age " + getAgeInDays() + "\t" +
				 " Phase " + getPhase() + "\t" +
				 " Illumination "+ getIlluminationPercent() + "\t" +
				 " Long. Ecliptic " + getPosition().getLongitudeEcliptic() + "\t" +
				 " Lat. Ecliptic " + getPosition().getLatitudeEcliptic() + "\t" +
				 " Right Ascention " + getPosition().getRightAscention() + "\t" +
				 " Distance " +  getPosition().getDistance() + "\t" + 
				 " Zodiac " + getZodiac() + "\t" +
				 " AnglePhase " + getAnglePhase() + "\t" + getPerigeeOrApogee() +"\n";
		return str;
	}
	
}
