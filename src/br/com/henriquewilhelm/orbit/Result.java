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
	public Event sun;
	/**
	 * Event sun - calculate sun events
	 */
	public ArrayList<Event> planetList;
	/**
	 * Events of  sun - Other types of events
	 */
	public Event astronomicalTwilight, nauticalTwilight, civilTwilight;
	/**
	 * Event sun - goldenHour
	 */
	public Event goldenHour;
	
	/**
	 * Event Moon - Other types of events
	 */
	public MoonEvent moonToday, moonTomorrow;
	
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
		
		if(sun.rise != null) {
			writer.printf("Sunrise %s", formatTimeAndAzimuth(sun.rise, sun.riseAzimuth));
		}
		
		if(sun.set != null) {
			if(sun.rise != null) {
				writer.print(", ");
			}
			writer.printf("Sunset %s ", formatTimeAndAzimuth(sun.set, sun.setAzimuth));
		}
		
		
		// potential special-message
		switch(sun.type) {
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
		
		writer.printf("Longitude Ecliptical %s\n", sun.position.longitudeEcliptic);
		
		
		writer.printf("Day Length  : %s", sun.risenAmount);
		if(sun.meridianCrossing==null) {
			writer.println("");
		} else {
			writer.println(", Solar Noon    : "+sun.meridianCrossing);
		}
		
		writer.printf("Night Length: %s", sun.setAmount);
		if(sun.antimeridianCrossing==null) {
			writer.println("");
		} else {
			writer.println(", Solar Midnight: "+sun.antimeridianCrossing);
		}

		writer.printf("Golden Hour          : (sunrise to %s, %s to sunset)\n", 
				      replaceNull(goldenHour.rise),
				      replaceNull(goldenHour.set));

		
		writer.printf("Civil Twilight       : (%s to sunrise, sunset to %s)",
					  replaceNull(civilTwilight.rise), 
					  replaceNull(civilTwilight.set));
		writer.printf(", Civil Night Length       : %s\n",
					  replaceNull(civilTwilight.setAmount));
		
		writer.printf("Nautical Twilight    : (%s to sunrise, sunset to %s)",
					  replaceNull(nauticalTwilight.rise),
					  replaceNull(nauticalTwilight.set));
		writer.printf(", Nautical Night Length    : %s\n",
					  replaceNull(nauticalTwilight.setAmount));
		
		writer.printf("Astronomical Twilight: (%s to sunrise, sunset to %s)",
					  replaceNull(astronomicalTwilight.rise),
					  replaceNull(astronomicalTwilight.set));
		writer.printf(", Astronomical Night Length: %s\n", 
					  replaceNull(astronomicalTwilight.setAmount));

		
		writer.printf("Today's    Moonrise: %s   Moonset: %s   Moon age: %3.0f days   Illumination: %3.0f%%	Long. Ecliptic%3.1f	\n" ,
					  formatTimeAndAzimuth(moonToday.rise, moonToday.riseAzimuth),
					  formatTimeAndAzimuth(moonToday.set , moonToday.setAzimuth),
					  moonToday.ageInDays, 
					  moonToday.illuminationPercent,
					  moonToday.position.longitudeEcliptic);
		writer.printf("Tomorrow's Moonrise: %s   Moonset: %s   Moon age: %3.0f days   Illumination: %3.0f%%	Long. Ecliptic %3.1f\n", 
					  formatTimeAndAzimuth(moonTomorrow.rise, moonTomorrow.riseAzimuth),
					  formatTimeAndAzimuth(moonTomorrow.set,  moonTomorrow.setAzimuth),
					  moonTomorrow.ageInDays, 
					  moonTomorrow.illuminationPercent,
					  moonTomorrow.position.longitudeEcliptic);
		
		for (int i = 0; i < planetList.size(); i++) {
			writer.printf("Today %s planet (set: %s) (rise %s) (Long. Ecliptic %3.1f) (Distance %f3,1)\n", 
					planetList.get(i).name,
					formatTimeAndAzimuth(planetList.get(i).rise, planetList.get(i).riseAzimuth),
					formatTimeAndAzimuth(planetList.get(i).set,  planetList.get(i).setAzimuth),
					planetList.get(i).position.longitudeEcliptic,
					planetList.get(i).position.distance);
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
			return "--None--";
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