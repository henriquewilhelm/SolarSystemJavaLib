package br.com.henriquewilhelm.orbit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;


/**
 * This class gathered the results of calculations performed by the calculator of the solar system.
 * The calculations are called Event
 * 
 * @author Henrique Wilhelm v2.0.0
 * @author zoglmannk v1.0.0
 * 
 * @version v2.0.0
 */

public class Result {
	
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
	private ArrayList<EclipseEvent> eclipseLunar;
	/**
	 * Eclipse Solar List
	 */
	private ArrayList<EclipseEvent> eclipseSolar;
	
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
	public ArrayList<EclipseEvent> getEclipseLunar() {
		return eclipseLunar;
	}
	public void setEclipseLunar(ArrayList<EclipseEvent> eclipseLunar) {
		this.eclipseLunar = eclipseLunar;
	}
	public ArrayList<EclipseEvent> getEclipseSolar() {
		return eclipseSolar;
	}
	public void setEclipseSolar(ArrayList<EclipseEvent> eclipseSolar) {
		this.eclipseSolar = eclipseSolar;
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
		
		writer.printf("\nLongitude Ecliptical %s	", sun.getPosition().getLongitudeEcliptic());
		writer.printf("\nZodiac %s\n", sun.getZodiac());
		
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

		writer.printf("\nMoon Today\n%s	Moonrise: %s   Moonset: %s   Moon age: %3.0f days   Phase %s	Illumination: %3.0f%%	Long. Ecliptic%3.1f	Distance %3.2f	Zodiac %s	%s\n" ,
				 	moonToday.getDate(),
					  formatTimeAndAzimuth(moonToday.getRise(), moonToday.getRiseAzimuth()),
					  formatTimeAndAzimuth(moonToday.getSet() , moonToday.getSetAzimuth()),
					  moonToday.getAgeInDays(),
					  moonToday.getPhase(),
					  moonToday.getIlluminationPercent(),
					  moonToday.getPosition().getLongitudeEcliptic(),
					  moonToday.getPosition().getDistance(),
					  moonToday.getZodiac(),
					  moonToday.getPerigeeOrApogee());
		writer.printf("Moon Tomorrow\n%s	Moonrise: %s   Moonset: %s   Moon age: %3.0f days   Phase %s	Illumination: %3.0f%%	Long. Ecliptic%3.1f	Distance %3.2f	Zodiac %s	%s\n" ,
				moonTomorrow.getDate(),
					formatTimeAndAzimuth(moonTomorrow.getRise(), moonTomorrow.getRiseAzimuth()),
					  formatTimeAndAzimuth(moonTomorrow.getSet(),  moonTomorrow.getSetAzimuth()),
					  moonTomorrow.getAgeInDays(),
					  moonTomorrow.getPhase(),
					  moonTomorrow.getIlluminationPercent(),
					  moonTomorrow.getPosition().getLongitudeEcliptic(),
					  moonTomorrow.getPosition().getDistance(),
					  moonTomorrow.getZodiac(),
					  moonTomorrow.getPerigeeOrApogee());
	
		writer.println("\nLunar Year");
		for (int i = 0; i < lunarYear.size(); i++) {
			for (int j = 0; j < lunarYear.get(i).size(); j++) {
				writer.printf("%3.0f	%s	Moonrise: %s	Moonset: %s	Moon age: %3.0f days	Phase %s	Illumination: %3.0f%%	Long. Ecliptic%3.1f	Distance %3.2f	Zodiac %s	%s\n" ,
						lunarYear.get(i).get(j).getJulianDate(), lunarYear.get(i).get(j).getDate(),
						  formatTimeAndAzimuth(lunarYear.get(i).get(j).getRise(), lunarYear.get(i).get(j).getRiseAzimuth()),
						  formatTimeAndAzimuth(lunarYear.get(i).get(j).getSet() , lunarYear.get(i).get(j).getSetAzimuth()),
						  lunarYear.get(i).get(j).getAgeInDays(),
						  lunarYear.get(i).get(j).getPhase(),
						  lunarYear.get(i).get(j).getIlluminationPercent(),
						  lunarYear.get(i).get(j).getPosition().getLongitudeEcliptic(),						  
						  lunarYear.get(i).get(j).getPosition().getDistance(),
						  lunarYear.get(i).get(j).getZodiac(),
						  lunarYear.get(i).get(j).getPerigeeOrApogee());
			}
		}
//		writer.println("\nApogee");
//		for (int j = 0; j < apogeeList.size(); j++) {
//			writer.printf("%s	Moonrise: %s	Moonset: %s	Moon age: %3.0f days	Phase %s	"
//					+ "Illumination: %3.0f%%	Long. Ecliptic%3.1f	Distance %3.2f	Zodiac %s\n" ,
//					apogeeList.get(j).getDate(),
//					  formatTimeAndAzimuth(apogeeList.get(j).getRise(), apogeeList.get(j).getRiseAzimuth()),
//					  formatTimeAndAzimuth(apogeeList.get(j).getSet() , apogeeList.get(j).getSetAzimuth()),
//					  apogeeList.get(j).getAgeInDays(), 
//					  apogeeList.get(j).getPhase(),
//					  apogeeList.get(j).getIlluminationPercent(),
//					  apogeeList.get(j).getPosition().getLongitudeEcliptic(),
//					  apogeeList.get(j).getPosition().getDistance(),
//					  apogeeList.get(j).getZodiac());
//		}
//		writer.println("\nPerigee");
//		for (int j = 0; j < perigeeList.size(); j++) {
//			writer.printf("%s	Moonrise: %s	Moonset: %s	Moon age: %3.0f	days   Phase %s	"
//					+ "Illumination: %3.0f%%	Long. Ecliptic%3.1f	Distance %3.2f	Zodiac %s\n" ,
//					perigeeList.get(j).getDate(),
//					  formatTimeAndAzimuth(perigeeList.get(j).getRise(), perigeeList.get(j).getRiseAzimuth()),
//					  formatTimeAndAzimuth(perigeeList.get(j).getSet() , perigeeList.get(j).getSetAzimuth()),
//					  perigeeList.get(j).getAgeInDays(),
//					  perigeeList.get(j).getPhase(),
//					  perigeeList.get(j).getIlluminationPercent(),
//					  perigeeList.get(j).getPosition().getLongitudeEcliptic(),
//					  perigeeList.get(j).getPosition().getDistance(),
//					  perigeeList.get(j).getZodiac());
//		}
//		writer.println("\nPlanets");
//		for (int i = 0; i < planetList.size(); i++) {
//			writer.printf("Today %s	planet SET: %s	RISE %s	Long. Ecliptic %3.1f	Distance %f3,1	Zodiac %s\n", 
//					planetList.get(i).getName(),
//					formatTimeAndAzimuth(planetList.get(i).getRise(), planetList.get(i).getRiseAzimuth()),
//					formatTimeAndAzimuth(planetList.get(i).getSet(),  planetList.get(i).getSetAzimuth()),
//					planetList.get(i).getPosition().getLongitudeEcliptic(),
//					planetList.get(i).getPosition().getDistance(),
//					planetList.get(i).getZodiac());
//		}
		writer.println("\nEclipse Lunar");
		for (int i = 0; i < eclipseLunar.size(); i++) {
				writer.printf("%3.0f	%s	Moonrise: %s	Moonset: %s	Moon age: %3.0f days	Phase %s	Illumination: %3.0f%%	Long. Ecliptic%3.1f	Distance %3.2f	Zodiac %s	%s\n" ,
						eclipseLunar.get(i).getJulianDate(), eclipseLunar.get(i).getDate(),
						  formatTimeAndAzimuth(eclipseLunar.get(i).getRise(), eclipseLunar.get(i).getRiseAzimuth()),
						  formatTimeAndAzimuth(eclipseLunar.get(i).getSet() , eclipseLunar.get(i).getSetAzimuth()),
						  eclipseLunar.get(i).getAgeInDays(),
						  eclipseLunar.get(i).getPhase(),
						  eclipseLunar.get(i).getIlluminationPercent(),
						  eclipseLunar.get(i).getPosition().getLongitudeEcliptic(),						  
						  eclipseLunar.get(i).getPosition().getDistance(),
						  eclipseLunar.get(i).getZodiac(),
						  eclipseLunar.get(i).getPerigeeOrApogee());
			
		}
		writer.println("\nEclipse Solar");
		for (int i = 0; i < eclipseSolar.size(); i++) {
				writer.printf("%3.0f	%s	%s	Moonrise: %s	Moonset: %s	Moon age: %3.0f days	Phase %s	Illumination: %3.0f%%	Long. Ecliptic%3.1f	Distance %3.2f	Zodiac %s	%s\n" ,
						eclipseSolar.get(i).getJulianDate(), eclipseSolar.get(i).getDateBegin(), eclipseSolar.get(i).getDateEnd(),
						  formatTimeAndAzimuth(eclipseSolar.get(i).getRise(), eclipseSolar.get(i).getRiseAzimuth()),
						  formatTimeAndAzimuth(eclipseSolar.get(i).getSet() , eclipseSolar.get(i).getSetAzimuth()),
						  eclipseSolar.get(i).getAgeInDays(),
						  eclipseSolar.get(i).getPhase(),
						  eclipseSolar.get(i).getIlluminationPercent(),
						  eclipseSolar.get(i).getPosition().getLongitudeEcliptic(),						  
						  eclipseSolar.get(i).getPosition().getDistance(),
						  eclipseSolar.get(i).getZodiac(),
						  eclipseSolar.get(i).getPerigeeOrApogee());
			
		}
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
