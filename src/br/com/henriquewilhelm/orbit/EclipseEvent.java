package br.com.henriquewilhelm.orbit;

import java.io.Serializable;
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
public class EclipseEvent extends MoonEvent implements Serializable {
	
	private static final long serialVersionUID = 1318620171715077216L;

	private String eclipseType;
	
	private Date dateBegin;
	
	private Date dateEnd;
	
	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getEclipseType() {
		return eclipseType;
	}
	public void setEclipseType(String eclipeType) {
		this.eclipseType = eclipeType;
	}
	
	/**
	 * Construtor without Event
	 */
	public EclipseEvent() { }
	/**
	 * Construtor with MoonEvent
	 * @param event Event class
	 */
	
	public EclipseEvent(MoonEvent event) {
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
		setPhase(event.getPhase());
		setIlluminationPercent(event.getIlluminationPercent());
		setAgeInDays(event.getAgeInDays());
		setAnglePhase(event.getAnglePhase());
		setName("Moon");
	}
	
	@Override
	public String toString() {
		String str;
		str = "Eclipse Type "+ getEclipseType() + "\t" +
				 " Julian Date "+ getJulianDate() + "\t" +
				 " Date Begin " + getDateBegin() + "\t" + 
				 " Date End " + getDateEnd() + "\t";

				str = str + " Moonrise " + formatTimeAndAzimuth(getRise(), getRiseAzimuth()) + "\t";
				
				str = str +  " Moonset " + formatTimeAndAzimuth(getSet(), getSetAzimuth()) + "\t";
				
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
