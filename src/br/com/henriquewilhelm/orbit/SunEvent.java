package br.com.henriquewilhelm.orbit;
import java.io.PrintWriter;
import java.io.Serializable;
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
public class SunEvent extends Event implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2521828917882788823L;

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
	public SunEvent() { }
	
	/**
	 * Construtor with MoonEvent
	 * @param event Event class
	 */
	public SunEvent(Event event) {
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
			String str = getName() + "\t" +
				 " JulianDate "+ getJulianDate() + "\t" +
				 " Date " + getDate() + "\t" +
			 	 " Rise " + formatTimeAndAzimuth(getRise(), getRiseAzimuth()) + "\t" +
			 	 " Set " + formatTimeAndAzimuth(getSet(), getSetAzimuth()) + "\t" +
			 	 " Long. Ecliptic " + getPosition().getLongitudeEcliptic() + "\t" +
				 " Right Ascention " + getPosition().getRightAscention() + "\t" +
				 " Zodiac " + getZodiac() +"\t"+
				 " Eclipse Type "+ getEclipseType() + "\t" +
				 " Date Begin " + getDateBegin() + "\t" + 
				 " Date End " + getDateEnd() + "\n";
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



	