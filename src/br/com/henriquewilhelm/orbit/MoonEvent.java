package br.com.henriquewilhelm.orbit;

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
}
