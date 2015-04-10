package br.com.henriquewilhelm.orbit;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.TimeZone;


/**
 * This class gathered the results of calculations performed by the calculator of the solar system.
 * The calculations are called Event
 * 
 * @author Henrique Wilhelm v2.0.0
 * @author zoglmannk v1.0.0
 * 
 * @version v2.0.0
 */

public class Result implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1035726271215559506L;
	/**
	 * Event sun - calculate sun events
	 */
	private Event sun;
	/**
	 * Event sun - calculate sun events
	 */
	private ArrayList<Event> planetList;
	/**
	 * Events of  sun - Other types of events
	 */
	private Event astronomicalTwilight, nauticalTwilight, civilTwilight;
	/**
	 * Event sun - goldenHour
	 */
	private Event goldenHour;
	
	/**
	 * Event Moon - Other types of events
	 */
	private MoonEvent moonToday, moonTomorrow;
	
	/**
	 * Array list of Event Moon 
	 */
	private ArrayList<ArrayList<MoonEvent>> lunarYear;
	
	/**
	 * Array list of Event Moon 
	 */
	private ArrayList<ArrayList<SunEvent>> solarYear;
	
	/**
	 * Array list of Event Moon 
	 */
	private ArrayList<ArrayList<ArrayList<Event>>> planetYear;
	
	/**
	 * Moon's Apogeu ArrayList per year
	 */
	private ArrayList<MoonEvent> apogeeList;
	
	/**
	 * Moon's Perigeu ArrayList per year
	 */
	private ArrayList<MoonEvent> perigeeList;
	/**
	 * Eclipse Lunar List
	 */
	private ArrayList<MoonEvent> eclipseLunar;
	/**
	 * Eclipse Solar List
	 */
	private ArrayList<SunEvent> eclipseSolar;
	
	private TimeZone timeZone;
	
	public Event getSun() {
		return sun;
	}
	public void setSun(Event sun) {
		this.sun = sun;
	}
	public ArrayList<Event> getPlanetList() {
		return planetList;
	}
	public void setPlanetList(ArrayList<Event> planetList) {
		this.planetList = planetList;
	}
	public Event getAstronomicalTwilight() {
		return astronomicalTwilight;
	}
	public void setAstronomicalTwilight(Event astronomicalTwilight) {
		this.astronomicalTwilight = astronomicalTwilight;
	}
	public Event getNauticalTwilight() {
		return nauticalTwilight;
	}
	public void setNauticalTwilight(Event nauticalTwilight) {
		this.nauticalTwilight = nauticalTwilight;
	}
	public Event getCivilTwilight() {
		return civilTwilight;
	}
	public void setCivilTwilight(Event civilTwilight) {
		this.civilTwilight = civilTwilight;
	}
	public Event getGoldenHour() {
		return goldenHour;
	}
	public void setGoldenHour(Event goldenHour) {
		this.goldenHour = goldenHour;
	}
	public MoonEvent getMoonToday() {
		return moonToday;
	}
	public void setMoonToday(MoonEvent moonToday) {
		this.moonToday = moonToday;
	}
	public MoonEvent getMoonTomorrow() {
		return moonTomorrow;
	}
	public void setMoonTomorrow(MoonEvent moonTomorrow) {
		this.moonTomorrow = moonTomorrow;
	}
	public ArrayList<ArrayList<MoonEvent>> getLunarYear() {
		return lunarYear;
	}
	public void setLunarYear(ArrayList<ArrayList<MoonEvent>> lunarYear) {
		this.lunarYear = lunarYear;
	}
	public ArrayList<MoonEvent> getApogeeList() {
		return apogeeList;
	}
	public void setApogeeList(ArrayList<MoonEvent> apogeeList) {
		this.apogeeList = apogeeList;
	}
	public ArrayList<MoonEvent> getPerigeeList() {
		return perigeeList;
	}
	public void setPerigeeList(ArrayList<MoonEvent> perigeeList) {
		this.perigeeList = perigeeList;
	}
	public ArrayList<MoonEvent> getEclipseLunar() {
		return eclipseLunar;
	}
	public void setEclipseLunar(ArrayList<MoonEvent> eclipseLunar) {
		this.eclipseLunar = eclipseLunar;
	}
	public ArrayList<SunEvent> getEclipseSolar() {
		return eclipseSolar;
	}
	public void setEclipseSolar(ArrayList<SunEvent> eclipseSolar) {
		this.eclipseSolar = eclipseSolar;
	}
	public ArrayList<ArrayList<SunEvent>> getSolarYear() {
		return solarYear;
	}
	public void setSolarYear(ArrayList<ArrayList<SunEvent>> solarYear) {
		this.solarYear = solarYear;
	}
	public ArrayList<ArrayList<ArrayList<Event>>> getPlanetYear() {
		return planetYear;
	}
	public void setPlanetYear(ArrayList<ArrayList<ArrayList<Event>	>> planetYear) {
		this.planetYear = planetYear;
	}
	public TimeZone getTimeZone() {
		return timeZone;
	}
	
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	/**
	 * Contrutor of Result
	 */   
	public Result() {
		super();
	}
	
	/**
	 * Return the results of calculations performed by the calculator of the solar system.
	 * @return String value of Result
	 */
	public String toString() {
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		
		writer.printf("TimeZone: " + getTimeZone().getID() + "\n");
		if(sun.getRise() != null) {
			writer.printf("\nSunrise %s", formatTimeAndAzimuth(sun.getRise(), sun.getRiseAzimuth()));
		}
		
		if(sun.getSet() != null) {
			if(sun.getSet() != null) {
				writer.print(", ");
			}
			writer.printf("Sunset %s ", formatTimeAndAzimuth(sun.getSet(), sun.getSetAzimuth()));
		}
		
		
		// potential special-message
		switch(sun.getType()) {
		case NO_CHANGE_PREVIOUSLY_RISEN:
			writer.println("Sun up all day");
			break;
		case NO_CHANGE_PREVIOUSLY_SET:
			writer.println("Sun down all day");
			break;
		case ONLY_SET:
			writer.println("No sunrise this date");
			break;
		case ONLY_RISEN:
			writer.println("No sunset this date");
			break;
		default:
			//nothing
		}
		
		writer.printf("\nLongitude Ecliptical %s", sun.getPosition().getLongitudeEcliptic());
		writer.printf("\tZodiac %s", sun.getZodiac());
		writer.printf("\tRA %s\n", sun.getPosition().getRightAscention());
		writer.printf("Day Length  : %s", sun.getRisenAmount());
		if(sun.getMeridianCrossing()==null) {
			writer.println("");
		} else {
			writer.println(", Solar Noon    : "+sun.getMeridianCrossing());
		}
		
		writer.printf("Night Length: %s", sun.getSetAmount());
		if(sun.getAntimeridianCrossing()==null) {
			writer.println("");
		} else {
			writer.println(", Solar Midnight: "+sun.getAntimeridianCrossing());
		}

		writer.printf("Golden Hour          : (sunrise to %s, %s to sunset)\n", 
			      replaceNull(goldenHour.getRise()),
			      replaceNull(goldenHour.getSet()));
	
		writer.printf("Civil Twilight       : (%s to sunrise, sunset to %s)",
					  replaceNull(civilTwilight.getRise()), 
					  replaceNull(civilTwilight.getSet()));
		writer.printf(", Civil Night Length       : %s\n",
					  replaceNull(civilTwilight.getSetAmount()));
		
		writer.printf("Nautical Twilight    : (%s to sunrise, sunset to %s)",
					  replaceNull(nauticalTwilight.getRise()),
					  replaceNull(nauticalTwilight.getSet()));
		writer.printf(", Nautical Night Length    : %s\n",
					  replaceNull(nauticalTwilight.getSetAmount()));
		
		writer.printf("Astronomical Twilight: (%s to sunrise, sunset to %s)",
					  replaceNull(astronomicalTwilight.getRise()),
					  replaceNull(astronomicalTwilight.getSet()));
		writer.printf(", Astronomical Night Length: %s\n", 
					  replaceNull(astronomicalTwilight.getSetAmount()));

		writer.printf("\nMoon Today\n%s	Moonrise: %s   Moonset: %s   Moon age: %3.0f days   Phase %s	Illumination: %3.0f%%	Long. Ecliptic%3.1f	Distance %3.2f	Zodiac %s	%s	AnglePhase: %3.2f\n" ,
				 	moonToday.getDate(),
					  formatTimeAndAzimuth(moonToday.getRise(), moonToday.getRiseAzimuth()),
					  formatTimeAndAzimuth(moonToday.getSet() , moonToday.getSetAzimuth()),
					  moonToday.getAgeInDays(),
					  moonToday.getPhase(),
					  moonToday.getIlluminationPercent(),
					  moonToday.getPosition().getLongitudeEcliptic(),
					  moonToday.getPosition().getDistance(),
					  moonToday.getZodiac(),
					  moonToday.getPerigeeOrApogee(),
					  moonToday.getAnglePhase());
		writer.printf("Moon Tomorrow\n%s	Moonrise: %s   Moonset: %s   Moon age: %3.0f days   Phase %s	Illumination: %3.0f%%	Long. Ecliptic%3.1f	Distance %3.2f	Zodiac %s	%s	AnglePhase: %3.2f\n" ,
				moonTomorrow.getDate(),
					formatTimeAndAzimuth(moonTomorrow.getRise(), moonTomorrow.getRiseAzimuth()),
					  formatTimeAndAzimuth(moonTomorrow.getSet(),  moonTomorrow.getSetAzimuth()),
					  moonTomorrow.getAgeInDays(),
					  moonTomorrow.getPhase(),
					  moonTomorrow.getIlluminationPercent(),
					  moonTomorrow.getPosition().getLongitudeEcliptic(),
					  moonTomorrow.getPosition().getDistance(),
					  moonTomorrow.getZodiac(),
					  moonTomorrow.getPerigeeOrApogee(),
					  moonTomorrow.getAnglePhase());
		
//		writer.println("\nPlanets");
//		for (int i = 0; i < planetList.size(); i++) {
//			writer.printf(planetList.get(i).toString());
//		}
//		
//		writer.println("\nSolar Year");
//		for (int i = 0; i < solarYear.size(); i++) {
//			for (int j = 0; j < solarYear.get(i).size(); j++) {
//				writer.printf(solarYear.get(i).get(j).toString());
//			}
//		}
//		
		writer.println("\nLunar Year");
		for (int i = 0; i < lunarYear.size(); i++) {
			for (int j = 0; j < lunarYear.get(i).size(); j++) {
				writer.printf(lunarYear.get(i).get(j).toString());
			}
		}
//		writer.println("\nApogee");
//		for (int j = 0; j < apogeeList.size(); j++) {
//			writer.printf(apogeeList.get(j).toString());
//		}
//		writer.println("\nPerigee");
//		for (int j = 0; j < perigeeList.size(); j++) {
//			writer.printf(perigeeList.get(j).toString());
//		}
//		
		writer.println("\nEclipse Lunar");
		for (int i = 0; i < eclipseLunar.size(); i++) {
				writer.printf(eclipseLunar.get(i).toString());
			
		}
		writer.println("\nEclipse Solar");
		for (int i = 0; i < eclipseSolar.size(); i++) {
				writer.printf(eclipseSolar.get(i).toString());
		}
//		writer.println("\nPlanets Year");
//		for (int i = 0; i < planetYear.size(); i++) {
//			for (int j = 0; j < planetYear.get(i).size(); j++) {
//				for (int l = 0; l < planetYear.get(i).get(j).size(); l++) {
//					writer.printf(planetYear.get(i).get(j).get(l) + " ");
//				}
//				writer.printf("\n");
//			}
//		}
		writer.flush();
		return sw.getBuffer().toString();
	}
	/**
	 * Check azimuth (Double) in time, hour and minute.
	 * @param t Time 
	 * @param azimuth double value 
	 * @return String value 
	 */
	private String formatTimeAndAzimuth(Time t, double azimuth) {
		if(t == null) {
			return "None";
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
