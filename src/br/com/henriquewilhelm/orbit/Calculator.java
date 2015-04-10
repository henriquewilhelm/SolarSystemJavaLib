package br.com.henriquewilhelm.orbit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * System Solar Calculator
 * 
 *  <p>Note that the definition of civil, nautical, and astronomical twilight is defined 
 *  respectively as 6, 12, and 18 degrees below the horizon. I'm choosing a slightly 
 *	different astronomical offset to better match published times over a wide range 
 *	of dates. Negative values mean below the horizon. Positive are above the horizon.</p>
 *
 * @author Henrique Wilhelm v2.0.0
 * @author zoglmannk v1.0.0
 * 
 * @version v2.0.0
 * 
 */
public class Calculator {
	
	private GpsCoordinate gps;
	
	private Calendar calendar;
	//Getters ands Setters
	public ArrayList<MoonEvent> getApogeeList() {
			return apogeeList;
	}
	public void setApogeuList(ArrayList<MoonEvent> apogeuList) {
			this.apogeeList = apogeuList;
	}
	public ArrayList<MoonEvent> getPerigeeList() {
			return perigeeList;
	}
	public void setPerigeuList(ArrayList<MoonEvent> perigeuList) {
			this.perigeeList = perigeuList;
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

	public ArrayList<MoonEvent> getMonthLunar() {
		return monthLunar;
	}
	public void setMonthLunar(ArrayList<MoonEvent> monthLunar) {
		this.monthLunar = monthLunar;
	}
	public ArrayList<SunEvent> getMonthSolar() {
		return monthSolar;
	}
	public void setMonthSolar(ArrayList<SunEvent> monthSolar) {
		this.monthSolar = monthSolar;
	}
	public ArrayList<ArrayList<Event>> getMonthPlanet() {
		return monthPlanet;
	}
	public void setMonthPlanet(ArrayList<ArrayList<Event>> monthPlanet) {
		this.monthPlanet = monthPlanet;
	}
	public ArrayList<ArrayList<ArrayList<Event>>> getPlanetYear() {
		return planetYear;
	}
	public void setPlanetYear(ArrayList<ArrayList<ArrayList<Event>>> planetYear) {
		this.planetYear = planetYear;
	}
	public ArrayList<ArrayList<MoonEvent>> getLunarYear() {
		return lunarYear;
	}
	public void setLunarYear(ArrayList<ArrayList<MoonEvent>> lunarYear) {
		this.lunarYear = lunarYear;
	}
	public ArrayList<ArrayList<SunEvent>> getSolarYear() {
		return solarYear;
	}
	public void setSolarYear(ArrayList<ArrayList<SunEvent>> solarYear) {
		this.solarYear = solarYear;
	}


	/**
	 * {#value #ERRORCORRECTIONDATE} Correction error in date class
	 */
	private static final int ERRORCORRECTIONDATE = 1900;
	
	/**
	 * {@value #NEW_MOON_EPOC}
	 */
	private static final double NEW_MOON_EPOC = 2451550.1; // (January 6, 2000)
	/**
	 * {@value #DAYS_IN_LUNAR_MONTH}
	 */
	private static final double DAYS_IN_LUNAR_MONTH = 29.530588853;
	/**
	 * {@value #NEW_STANDARD_EPOC}
	 */
	private static final int NEW_STANDARD_EPOC = 2451545; // (January 1, 2000)
	/**
	 * {@value #NUM_DAYS_IN_CENTURY}
	 */
	private static final int NUM_DAYS_IN_CENTURY = 36525; // 365 days * 100 years + 25 extra days for leap years
	/**
	 * {@value #HOURS_IN_DAY}
	 */
	private static final int HOURS_IN_DAY = 24;
	/**
	 * {@value #MINUTE_IN_HOURS}
	 */
	private static final int MINUTE_IN_HOURS = 60;
	/**
	 * {@value #DR}
	 */
	private static final double DR = Math.PI/180.0; //degrees to radians constant
	/**
	 * {@value #K1}
	 */
	private static final double K1 = 15.0 * DR * 1.0027379;
	/**
	 * Type OFFSET SUNRISE_SUNET_OFFSET
	 */
	private static final Offset SUNRISE_SUNET_OFFSET        = new Offset(0    , true);
	/**
	 * Type OFFSET CIVIL_TWILIGHT_OFFSET
	 */
	private static final Offset CIVIL_TWILIGHT_OFFSET       = new Offset(-6   , false);
	/**
	 * Type OFFSET NAUTICAL_TWILIGHT_OFFSET
	 */
	private static final Offset NAUTICAL_TWILIGHT_OFFSET    = new Offset(-12  , false);
	/**
	 * Type OFFSET ASTRONOMICAL_TWILIGHT_OFFSET
	 */
	private static final Offset ASTRONOMICAL_TWILIGHT_OFFSET= new Offset(-17.8, false);
	/**
	 * Type OFFSET GOLDEN_HOUR_OFFSET
	 */
	private static final Offset GOLDEN_HOUR_OFFSET          = new Offset(10.0 , false);
	/**
	 * Type OFFSET MOONRISE_MOONSET_OFFSET
	 */
	private static final Offset MOONRISE_MOONSET_OFFSET     = new Offset(0    , false);
	
	/**
	 * Class responsible for dates of Perigee and Apogee
	 */
	private PerigeeApogeeCalculator perigeeApogeeCalculator;
	/**
	 * Apogee List
	 */
	private ArrayList<MoonEvent> apogeeList;
	/**
	 * Perigee List
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

	ArrayList<MoonEvent> monthLunar;
	ArrayList<SunEvent> monthSolar;
	ArrayList<ArrayList<Event>> monthPlanet;
	ArrayList<ArrayList<ArrayList<Event>>> planetYear;
	ArrayList<ArrayList<MoonEvent>> lunarYear;
	ArrayList<ArrayList<SunEvent>> solarYear;
	/**
	 * Account For Atmospheric Refraction
	 *
	 * @author zoglmannk v1.0.0
	 */
	private static class Offset {
		final double fromHorizon; //in degrees
		final boolean accountForAtmosphericRefraction;
		/**
		 * Consturtor 
		 * @param fromHorizon double value
		 * @param accountForAtmosphericRefraction double value
		 */
		public Offset(double fromHorizon, boolean accountForAtmosphericRefraction) {
			this.fromHorizon = fromHorizon;
			this.accountForAtmosphericRefraction = accountForAtmosphericRefraction;
		}
	}
	/**
	 * Construtor of Solar system Calculator 
	 * @param gps
	 * @param calendar
	 */
	public Calculator(GpsCoordinate gps, Calendar calendar) {
		this.perigeeApogeeCalculator = new PerigeeApogeeCalculator(calendar);
		this.apogeeList = new ArrayList<MoonEvent>();
		this.perigeeList = new ArrayList<MoonEvent>();
		this.gps = gps;   
		this.calendar = calendar;
	}
	
	/** 
	 * Calculate Sunset, sunrise and Positions (...)
	 * 
	 * @param gps         location of user
	 * @param calendar        date of interest for calculation
	 * @return result
	 */
	public Result calculate (GpsCoordinate gps, Calendar calendar) {
		this.gps = gps;   
		this.calendar = calendar;
		return calculate ();
	}
		
	/** 
	 * Calculate Sunset, sunrise and Positions (...)
	 * 
	 * @return result
	 */
	public Result calculate () {
		
		int julianDate = calculateJulianDate(this.calendar); //note that the julianDate is truncated
//		System.out.println("- Julian date: "+julianDate);
		
		double daysFromEpoc = (julianDate - NEW_STANDARD_EPOC) + 0.5;
		System.out.println("Julian Date OF J2000 "+daysFromEpoc);
		
		double utcToLocal = calculateUtcToDSTLocal(calendar);
		double timeZoneShift = -1  * utcToLocal/HOURS_IN_DAY;
		System.out.println("timeZoneShift "+timeZoneShift);
		System.out.println("utcToLocal "+utcToLocal);
		double LST     = calculateLST(daysFromEpoc  , timeZoneShift, this.gps.getLongitude());
		double nextLST = calculateLST(daysFromEpoc+1, timeZoneShift, this.gps.getLongitude());
		daysFromEpoc = daysFromEpoc + timeZoneShift;
		Result ret = new Result();
		
		ret.setTimeZone(this.calendar.getTimeZone());
		
		//calculate Sun related times
		Position sunToday    = calculateSunPosition(daysFromEpoc);		
		Position sunTomorrow = calculateSunPosition(daysFromEpoc+1);
		sunTomorrow = ensureSecondAscentionGreater(sunToday, sunTomorrow);
		ret.setSun(calculate(SUNRISE_SUNET_OFFSET, this.gps, LST, sunToday, sunTomorrow));		
		ret.setGoldenHour(calculate(GOLDEN_HOUR_OFFSET, this.gps, LST, sunToday, sunTomorrow));
		ret.setCivilTwilight(calculate(CIVIL_TWILIGHT_OFFSET, this.gps, LST, sunToday, sunTomorrow));
		ret.setNauticalTwilight(calculate(NAUTICAL_TWILIGHT_OFFSET, this.gps, LST, sunToday, sunTomorrow));
		ret.setAstronomicalTwilight(calculate(ASTRONOMICAL_TWILIGHT_OFFSET, this.gps, LST, sunToday, sunTomorrow));
		ret.getSun().setZodiac(zodiac(sunToday.getLongitudeEcliptic()));
		ret.getSun().setPosition(sunToday);
		
		//calculate today moon
		Position moonToday    = calculateMoonPosition(daysFromEpoc);
		Position moonTomorrow = calculateMoonPosition(daysFromEpoc+1);
		moonTomorrow = ensureSecondAscentionGreater(moonToday, moonTomorrow);		
		ret.setMoonToday(new MoonEvent(calculate(MOONRISE_MOONSET_OFFSET, this.gps, LST, moonToday, moonTomorrow)));
		ret.getMoonToday().setAgeInDays(calculateMoonsAge(julianDate+1));
		ret.getMoonToday().setIlluminationPercent(calculateMoonIlluminationPercent(ret.getMoonToday().getAgeInDays()+1));
		ret.getMoonToday().setZodiac(zodiac(moonToday.getLongitudeEcliptic()));
		ret.getMoonToday().setPhase(phase(ret.getMoonToday().getAgeInDays()));
		ret.getMoonToday().setDate(julianDatetoDate((double) julianDate));
		ret.getMoonToday().setPerigeeOrApogee(isApogeuOrPerigeu(ret.getMoonToday()));
		ret.getMoonToday().setAnglePhase(getAnglePhase(this.calendar.getTime()));
		ret.getMoonToday().setJulianDate(julianDate);
		//calculate tomorrow moon
		moonTomorrow = calculateMoonPosition(daysFromEpoc+1);
		Position moonDayAfter = calculateMoonPosition(daysFromEpoc+2);
		moonDayAfter = ensureSecondAscentionGreater(moonTomorrow, moonDayAfter);
		ret.setMoonTomorrow(new MoonEvent(calculate(MOONRISE_MOONSET_OFFSET, this.gps, nextLST, moonTomorrow, moonDayAfter)));
		ret.getMoonTomorrow().setAgeInDays(calculateMoonsAge(julianDate+2));
		ret.getMoonTomorrow().setIlluminationPercent(calculateMoonIlluminationPercent(ret.getMoonTomorrow().getAgeInDays()+1));
		ret.getMoonTomorrow().setZodiac(zodiac(moonTomorrow.getLongitudeEcliptic()));
		ret.getMoonTomorrow().setPhase(phase(ret.getMoonTomorrow().getAgeInDays()));
		ret.getMoonTomorrow().setDate(julianDatetoDate((double) julianDate+1));
		ret.getMoonTomorrow().setPerigeeOrApogee(isApogeuOrPerigeu(ret.getMoonTomorrow()));
		ret.getMoonTomorrow().setAnglePhase(getAnglePhase(this.calendar.getTime()));
		ret.getMoonTomorrow().setJulianDate(julianDate);
		
		OrbitCalculator planetCalc = new OrbitCalculator();
		ArrayList<Event> planetList;
		ArrayList<Event> list = new ArrayList<Event>();
		planetList = planetCalc.computeElementsPosition(daysFromEpoc);
		
		for (int i=0; i<planetList.size(); i++){
			Position planetToday    = planetList.get(i).getPosition();
			Position planetTomorrow = planetList.get(i).getPositionTomorrow();
			planetList.get(i).setPosition(planetToday);
			
			Event aux = calculate(SUNRISE_SUNET_OFFSET, this.gps, LST, planetToday, planetTomorrow);
			//Teste NAUTICAL_TWILIGHT_OFFSET
			aux.setDate(calendar.getTime());
			aux.setJulianDate(julianDate);
			aux.setName(planetList.get(i).getName());
			aux.setPosition(planetToday);
			aux.setZodiac(zodiac(planetToday.getLongitudeEcliptic()));
			list.add(aux);
		}
		
		calculateYear(this.calendar, this.gps);
		ret.setLunarYear(getLunarYear());
		ret.setSolarYear(getSolarYear());
		ret.setPlanetYear(getPlanetYear());
		ret.setApogeeList(getApogeeList());
		ret.setPerigeeList(getPerigeeList());
		ret.setEclipseLunar(getEclipseLunar());
		ret.setEclipseSolar(getEclipseSolar());
		
		ret.setPlanetList(list);
		return ret; 
	}
	
	/**
	 * checks if the date is in the Summe Time
	 * @param calendar instance of calendar
	 * @return double value of real DST (Date Summer Time)
	 */
	private double calculateUtcToDSTLocal(Calendar calendar) {	
		int offset = calendar.get(Calendar.DST_OFFSET);
		Double rawOffSet = (double) calendar.getTimeZone().getRawOffset();
		Double dstSavings = (double) calendar.getTimeZone().getDSTSavings();
		Double UTC_TO_DST = 0d;
		try{
			UTC_TO_DST = rawOffSet / dstSavings;
		}
		catch (ArithmeticException e){
			System.out.println("Erro function calculateUtcToDSTLocal "+e.getMessage());
		}
		if (offset>0){
			UTC_TO_DST = UTC_TO_DST + 1;
//			System.out.println("Summer Time");
		}
		return UTC_TO_DST;
	}

	/**
	 * Calculate Moon Age
	 * 
	 * @param julianDate julian Date
	 * @return age moon Age
	 */
	public double calculateMoonsAge(double julianDate) {
		double temp=(julianDate-NEW_MOON_EPOC)/DAYS_IN_LUNAR_MONTH;
		double age = temp - ((int) temp);
		
		if(age < 0) {
			age++;
		}
		age = age*DAYS_IN_LUNAR_MONTH;
		
		return age;
	}
	
	/**
	 * Until I find a more accurate formula, this will have to do. Seems to be accurate to within +/- 5%.
	 * @param ageInDaysSinceNewMoon age In Days Since New Moon 
	 * @return double percent value
	 */
	public double calculateMoonIlluminationPercent(double ageInDaysSinceNewMoon) {
		return .5 * (1 + Math.cos( (Math.round(ageInDaysSinceNewMoon)+DAYS_IN_LUNAR_MONTH/2)/DAYS_IN_LUNAR_MONTH*2*Math.PI)) * 100;
	}

	/**
	 * Get ensure Ascention
	 * @param first Position
	 * @param second Position
	 * @return ensure Position
	 */
	public Position ensureSecondAscentionGreater(Position first, Position second ) {
		if (second.getRightAscention() < first.getRightAscention()) {
			double ascention = second.getRightAscention()+2*Math.PI;
			double longitudeEcliptical = Math.toDegrees(ascention);
			if (longitudeEcliptical > 360)
				longitudeEcliptical = longitudeEcliptical - 360;
			second = new Position(ascention, second.getRightAscention(), longitudeEcliptical, 0);
		}
		
		return second;
	}
	
	/** 
	 * Calcule and generate Positions
	 * 
	 * @param offset Offset
	 * @param gps GpsCoordinate
	 * @param LST Calendar
	 * @param today Position
	 * @param tomorrow Position
	 * @return novo evento Event
	 */
	private Event calculate(
			Offset offset, 
			GpsCoordinate gps, 
			double LST, 
			Position today,
			Position tomorrow) {
		
		double previousAscention = today.getRightAscention();
		double previousDeclination = today.getDeclination();
		
//		System.out.println(previousDeclination);
		
		double changeInAscention   = tomorrow.getRightAscention() - today.getRightAscention();
		double changeInDeclination = tomorrow.getDeclination()    - today.getDeclination();
		
		double previousV = 0; //arbitrary initial value
		
		TestResult testResult = new TestResult();
		
		
		for(int hourOfDay=0; hourOfDay<HOURS_IN_DAY; hourOfDay++) {
			
			double fractionOfDay = (hourOfDay+1) / ((double)HOURS_IN_DAY);
			double asention    = today.getRightAscention() + fractionOfDay*changeInAscention;
			double declination = today.getDeclination()    + fractionOfDay*changeInDeclination;
					
			TestResult intermediateTestResult =  testHourForEvent(
										   hourOfDay,
										   offset,
										   previousAscention,   asention, 
										   previousDeclination, declination,
										   previousV, gps, LST);
			
			if(intermediateTestResult.rise != null) {
				testResult.rise       = intermediateTestResult.rise;
				testResult.riseAzimuth = intermediateTestResult.riseAzimuth;
			}
			
			if(intermediateTestResult.set != null) {
				testResult.set       = intermediateTestResult.set;
				testResult.setAzimuth = intermediateTestResult.setAzimuth;
			}
			
			previousAscention   = asention;
			previousDeclination = declination;
			previousV           = intermediateTestResult.V;
//			System.out.println(hourOfDay + " " +declination);
		}
	
		return createEvent(testResult, today);
	}
	
	/** 
	 * generate Positions
	 * 
	 * @param testResult testResult
	 * @return novo evento Event 
	 */
	public Event createEvent(TestResult testResult, Position today) {
		Event ret = new Event();
		
		ret.setRise(testResult.rise);
		ret.setSet(testResult.set);
		ret.setRiseAzimuth(testResult.riseAzimuth);
		ret.setSetAzimuth(testResult.setAzimuth);
		ret.setPosition(today);
		ret.setType(findTypeOfDay(testResult, testResult.V));
		setRisenAndSetAmounts(ret);
		setMeridianCrossing(ret);
		setAntimeridianCrossing(ret);
		
		return ret;
	}
		
	
	/**
	 * Test an hour for an event
	 * 
	 * @param hourOfDay Hour of Day 
	 * @param offset Off Set 
	 * @param previousAscention previous Ascention 
	 * @param ascention ascention
	 * @param previousDeclination previous Declination
	 * @param declination declination
	 * @param previousV previous Value
	 * @param gps GpsCoordinate
	 * @param LST LST
	 * @return Test Result TestResult
	 */	
	public TestResult testHourForEvent(
			int hourOfDay, Offset offset,
			double previousAscention, double ascention,
			double previousDeclination, double declination, 
			double previousV, 
			GpsCoordinate gps, double LST) {

		
		TestResult ret = new TestResult();
		
		//90.833 is for atmospheric refraction when sun is at the horizon.
		//ie the sun slips below the horizon at sunset before you actually see it go below the horizon
		double zenithDistance = DR * (offset.accountForAtmosphericRefraction ? 90.833 : 90.0); 
		
		double S = Math.sin(gps.getLatitude()*DR);
		double C = Math.cos(gps.getLatitude()*DR);
		double Z = Math.cos(zenithDistance) + offset.fromHorizon*DR;
		
		double L0 = LST + hourOfDay*K1;
		double L2 = L0 + K1;

		double H0 = L0 - previousAscention;
		double H2 = L2 - ascention;
		
		double H1 = (H2+H0) / 2.0; //  Hour angle,
		double D1 = (declination+previousDeclination) / 2.0; //  declination at half hour
		
		if (hourOfDay == 0) {
			previousV = S * Math.sin(previousDeclination) + C*Math.cos(previousDeclination)*Math.cos(H0)-Z;
		}

		double V = S*Math.sin(declination) + C*Math.cos(declination)*Math.cos(H2) - Z;
		
		if(objectCrossedHorizon(previousV, V)) {
			double V1 = S*Math.sin(D1) + C*Math.cos(D1)*Math.cos(H1) - Z;
			
			double A = 2*V - 4*V1 + 2*previousV;
			double B = 4*V1 - 3*previousV - V;
			double D = B*B - 4*A*previousV;

			if (D >= 0) {
				D = Math.sqrt(D);
				
				double E = (-B+D) / (2*A);
				if (E>1 || E<0) {
					E = (-B-D) / (2*A);
				}
				
				double H7 = H0 + E*(H2-H0);
				double N7 = -1 * Math.cos(D1)*Math.sin(H7);
				double D7 = C*Math.sin(D1) - S*Math.cos(D1)*Math.cos(H7);
				double azimuth = Math.atan(N7/D7)/DR;
				
				if(D7 < 0) {
					azimuth = azimuth+180;
				}

				if(azimuth < 0) {
					azimuth = azimuth+360;
				}

				if(azimuth > 360) {
					azimuth = azimuth-360;
				}
				

				double T3=hourOfDay + E + 1/120; //Round off
				int hour = (int) T3;
				int min = (int) ((T3-hour)*60);
				
				
				if (previousV<0 && V>0) {
					ret.rise = new Time(hour, min);
					ret.riseAzimuth = azimuth;
				}

				if (previousV>0 && V<0) {
					ret.set = new Time(hour, min);
					ret.setAzimuth = azimuth;
				}							
			}
		}	
		ret.V = V;
		return ret;	
	}
	
	/**
	 * Test Result
	 * @author He
	 *
	 */
	public static class TestResult {
		Time rise, set;
		double riseAzimuth, setAzimuth;
		double V;
	}

	/**
	 * Return true if objectCrossedHorizon
	 * @param previousV previous Value
	 * @param V value
	 * @return boolean (true or false)
	 */
	public boolean objectCrossedHorizon(double previousV, double V) {
		return sgn(previousV) != sgn(V);
	}
	/** 
	 * Check signal (return 1 if val more 0 and 0 if val less 0)
	 * @param val double value
	 * @return (0 or 1) int value
	 */
	public int sgn(double val) {
		return val == 0 ? 0 : (val > 0 ? 1 : 0);
	}
	
	/**
	 * Drops any full revolutions and then converts revolutions to radians 
	 * @param revolutions revolutions Value
	 * @return double radians value
	 */
	public double revolutionsToTruncatedRadians(double revolutions) {
//		System.out.println(revolutions);
		return 2*Math.PI*(revolutions - ((int) revolutions));
	}
	
	/**
	 * calculateSunPosition 
	 * @param daysFromEpoc days From Epoc
	 * @return position of sun @{link Position} 
	 */
	public Position calculateSunPosition(double daysFromEpoc) {
		double numCenturiesSince1900 = daysFromEpoc/NUM_DAYS_IN_CENTURY + 1;
		
		//   Fundamental arguments  (Van Flandern & Pulkkinen, 1979)
		double meanLongitudeOfSun = revolutionsToTruncatedRadians(.779072 + .00273790931*daysFromEpoc);
		double meanAnomalyOfSun   = revolutionsToTruncatedRadians(.993126 + .00273777850*daysFromEpoc);		
		double meanLongitudeOfMoon           = revolutionsToTruncatedRadians(.606434 + .03660110129*daysFromEpoc);
		double longitudeOfLunarAscendingNode = revolutionsToTruncatedRadians(.347343 - .00014709391*daysFromEpoc);
		double meanAnomalyOfVenus            = revolutionsToTruncatedRadians(.140023 + .00445036173*daysFromEpoc);
		double meanAnomalyOfMars             = revolutionsToTruncatedRadians(.053856 + .00145561327*daysFromEpoc);
		double meanAnomalyOfJupiter          = revolutionsToTruncatedRadians(.056531 + .00023080893*daysFromEpoc);


		double V;
		V =     .39785 * Math.sin(meanLongitudeOfSun);
		V = V - .01000 * Math.sin(meanLongitudeOfSun-meanAnomalyOfSun);
		V = V + .00333 * Math.sin(meanLongitudeOfSun+meanAnomalyOfSun);
		V = V - .00021 * numCenturiesSince1900 * Math.sin(meanLongitudeOfSun);
		V = V + .00004 * Math.sin(meanLongitudeOfSun+2*meanAnomalyOfSun);
		V = V - .00004 * Math.cos(meanLongitudeOfSun);
		V = V - .00004 * Math.sin(longitudeOfLunarAscendingNode-meanLongitudeOfSun);
		V = V + .00003 * numCenturiesSince1900 * Math.sin(meanLongitudeOfSun-meanAnomalyOfSun);
		
		double U;
		U = 1 - .03349 * Math.cos(meanAnomalyOfSun);
		U = U - .00014 * Math.cos(2*meanLongitudeOfSun);
		U = U + .00008 * Math.cos(meanLongitudeOfSun);
		U = U - .00003 * Math.sin(meanAnomalyOfSun-meanAnomalyOfJupiter);

		double W;
		W =    -.04129 * Math.sin(2*meanLongitudeOfSun);
		W = W + .03211 * Math.sin(meanAnomalyOfSun);
		W = W + .00104 * Math.sin(2*meanLongitudeOfSun-meanAnomalyOfSun);
		W = W - .00035 * Math.sin(2*meanLongitudeOfSun+meanAnomalyOfSun);
		W = W - .00010;
		W = W - .00008 * numCenturiesSince1900 * Math.sin(meanAnomalyOfSun);
		W = W - .00008 * Math.sin(longitudeOfLunarAscendingNode);
		W = W + .00007 * Math.sin(2*meanAnomalyOfSun);
		W = W + .00005 * numCenturiesSince1900 * Math.sin(2*meanLongitudeOfSun);
		W = W + .00003 * Math.sin(meanLongitudeOfMoon-meanLongitudeOfSun);
		W = W - .00002 * Math.cos(meanAnomalyOfSun-meanAnomalyOfJupiter);
		W = W + .00002 * Math.sin(4*meanAnomalyOfSun-8*meanAnomalyOfMars+3*meanAnomalyOfJupiter);
		W = W - .00002 * Math.sin(meanAnomalyOfSun-meanAnomalyOfVenus);
		W = W - .00002 * Math.cos(2*meanAnomalyOfSun-2*meanAnomalyOfVenus);
		
		Position position = calculatePosition(meanLongitudeOfSun, U, V, W);

	    return position;
	}
	
	public Position calculateMoonPosition(double daysFromEpoc) {
		double numCenturiesSince1900 = daysFromEpoc/NUM_DAYS_IN_CENTURY + 1;
		
		//   Fundamental arguments (Van Flandern & Pulkkinen, 1979)		
		double meanLongitudeOfMoon     = revolutionsToTruncatedRadians(.606434 + .03660110129*daysFromEpoc); // 1
		double meanAnomalyOfMoon       = revolutionsToTruncatedRadians(.374897 + .03629164709*daysFromEpoc); // 2
		double argumentOfLatitudeOfMoon= revolutionsToTruncatedRadians(.259091 + .03674819520*daysFromEpoc); // 3
		double meanElongationOfMoon    = revolutionsToTruncatedRadians(.827362 + .03386319198*daysFromEpoc); // 4
		double longitudeOfLunarAscendingNode= revolutionsToTruncatedRadians(.347343 - .00014709391*daysFromEpoc); // 5
		double meanLongitudeOfSun      = revolutionsToTruncatedRadians(.779072 + .00273790931*daysFromEpoc); // 7
		double meanAnomalyOfSun        = revolutionsToTruncatedRadians(.993126 + .00273777850*daysFromEpoc); // 8
		double meanLongitudeOfVenus    = revolutionsToTruncatedRadians(0.505498 + .00445046867*daysFromEpoc); // 12
		
	
		double A = meanAnomalyOfMoon ;            // 2
		double B = argumentOfLatitudeOfMoon ;     // 3
		double C = meanElongationOfMoon;          // 4
		double D = longitudeOfLunarAscendingNode; // 5
		double E = meanLongitudeOfSun;            // 7
		double F = meanAnomalyOfSun;              // 8
		double G = meanLongitudeOfVenus;          // 12
		
		double V;
		V =     .39558 * Math.sin(B+D);
		V = V + .08200 * Math.sin(B);
		V = V + .03257 * Math.sin(A-B-D);
		V = V + .01092 * Math.sin(A+B+D);
		V = V + .00666 * Math.sin(A-B);
		V = V - .00644 * Math.sin(A+B-2*C+D);
		V = V - .00331 * Math.sin(B-2*C+D);
		V = V - .00304 * Math.sin(B-2*C);
		V = V - .00240 * Math.sin(A-B-2*C-D);
		V = V + .00226 * Math.sin(A+B);
		V = V - .00108 * Math.sin(A+B-2*C);
		V = V - .00079 * Math.sin(B-D);
		V = V + .00078 * Math.sin(B+2*C+D);
		V = V + .00066 * Math.sin(B+D-F);
		V = V - .00062 * Math.sin(B+D+F);
		V = V - .00050 * Math.sin(A-B-2*C);
		V = V + .00045 * Math.sin(2*A+B+D);
		V = V - .00031 * Math.sin(2*A+B-2*C+D);
		V = V - .00027 * Math.sin(A+B-2*C+D+F);
		V = V - .00024 * Math.sin(B-2*C+D+F);
		V = V - .00021 * numCenturiesSince1900 * Math.sin(B+D);
		V = V + .00018 * Math.sin(B-C+D);
		V = V + .00016 * Math.sin(B+2*C);
		V = V + .00016 * Math.sin(A-B-D-F);
		V = V - .00016 * Math.sin(2*A-B-D);
		V = V - .00015 * Math.sin(B-2*C+F);
		V = V - .00012 * Math.sin(A-B-2*C-D+F);
		V = V - .00011 * Math.sin(A-B-D+F);
		V = V + .00009 * Math.sin(A+B+D-F);
		V = V + .00009 * Math.sin(2*A+B);
		V = V + .00008 * Math.sin(2*A-B);
		V = V + .00008 * Math.sin(A+B+2*C+D);
		V = V - .00008 * Math.sin(3*B-2*C+D);
		V = V + .00007 * Math.sin(A-B+2*C);
		V = V - .00007 * Math.sin(2*A-B-2*C-D);
		V = V - .00007 * Math.sin(A+B+D+F);
		V = V - .00006 * Math.sin(B+C+D);
		V = V + .00006 * Math.sin(B-2*C-F);
		V = V + .00006 * Math.sin(A-B+D);
		V = V + .00006 * Math.sin(B+2*C+D-F);
		V = V - .00005 * Math.sin(A+B-2*C+F);
		V = V - .00004 * Math.sin(2*A+B-2*C);
		V = V + .00004 * Math.sin(A-3*B-D);
		V = V + .00004 * Math.sin(A-B-F);
		V = V - .00003 * Math.sin(A-B+F);
		V = V + .00003 * Math.sin(B-C);
		V = V + .00003 * Math.sin(B-2*C+D-F);
		V = V - .00003 * Math.sin(B-2*C-D);
		V = V + .00003 * Math.sin(A+B-2*C+D-F);
		V = V + .00003 * Math.sin(B-F);
		V = V - .00003 * Math.sin(B-C+D-F);
		V = V - .00002 * Math.sin(A-B-2*C+F);
		V = V - .00002 * Math.sin(B+F);
		V = V + .00002 * Math.sin(A+B-C+D);
		V = V - .00002 * Math.sin(A+B-D);
		V = V + .00002 * Math.sin(3*A+B+D);
		V = V - .00002 * Math.sin(2*A-B-4*C-D);
		V = V + .00002 * Math.sin(A-B-2*C-D-F);
		V = V - .00002 * numCenturiesSince1900 * Math.sin(A-B-D);
		V = V - .00002 * Math.sin(A-B-4*C-D);
		V = V - .00002 * Math.sin(A+B-4*C);
		V = V - .00002 * Math.sin(2*A-B-2*C);
		V = V + .00002 * Math.sin(A+B+2*C);
		V = V + .00002 * Math.sin(A+B-F);
		
		
		double U;
		U = 1 - .10828 * Math.cos(A);
		U = U - .01880 * Math.cos(A-2*C);
		U = U - .01479 * Math.cos(2*C);
		U = U + .00181 * Math.cos(2*A-2*C);
		U = U - .00147 * Math.cos(2*A);
		U = U - .00105 * Math.cos(2*C-F);
		U = U - .00075 * Math.cos(A-2*C+F);
		U = U - .00067 * Math.cos(A-F);
		U = U + .00057 * Math.cos(C);
		U = U + .00055 * Math.cos(A+F);
		U = U - .00046 * Math.cos(A+2*C);
		U = U + .00041 * Math.cos(A-2*B);
		U = U + .00024 * Math.cos(F);
		U = U + .00017 * Math.cos(2*C+F);
		U = U - .00013 * Math.cos(A-2*C-F);
		U = U - .00010 * Math.cos(A-4*C);
		U = U - .00009 * Math.cos(C+F);
		U = U + .00007 * Math.cos(2*A-2*C+F);
		U = U + .00006 * Math.cos(3*A-2*C);
		U = U + .00006 * Math.cos(2*B-2*C);
		U = U - .00005 * Math.cos(2*C-2*F);
		U = U - .00005 * Math.cos(2*A-4*C);
		U = U + .00005 * Math.cos(A+2*B-2*C);
		U = U - .00005 * Math.cos(A-C);
		U = U - .00004 * Math.cos(A+2*C-F);
		U = U - .00004 * Math.cos(3*A);
		U = U - .00003 * Math.cos(A-4*C+F);
		U = U - .00003 * Math.cos(2*A-2*B);
		U = U - .00003 * Math.cos(2*B);

		
		double W;
		W =     .10478 * Math.sin(A);
		W = W - .04105 * Math.sin(2*B+2*D);
		W = W - .02130 * Math.sin(A-2*C);
		W = W - .01779 * Math.sin(2*B+D);
		W = W + .01774 * Math.sin(D);
		W = W + .00987 * Math.sin(2*C);
		W = W - .00338 * Math.sin(A-2*B-2*D);
		W = W - .00309 * Math.sin(F);
		W = W - .00190 * Math.sin(2*B);
		W = W - .00144 * Math.sin(A+D);
		W = W - .00144 * Math.sin(A-2*B-D);
		W = W - .00113 * Math.sin(A+2*B+2*D);
		W = W - .00094 * Math.sin(A-2*C+F);
		W = W - .00092 * Math.sin(2*A-2*C);
		W = W + .00071 * Math.sin(2*C-F);
		W = W + .00070 * Math.sin(2*A);
		W = W + .00067 * Math.sin(A+2*B-2*C+2*D);
		W = W + .00066 * Math.sin(2*B-2*C+D);
		W = W - .00066 * Math.sin(2*C+D);
		W = W + .00061 * Math.sin(A-F);
		W = W - .00058 * Math.sin(C);
		W = W - .00049 * Math.sin(A+2*B+D);
		W = W - .00049 * Math.sin(A-D);
		W = W - .00042 * Math.sin(A+F);
		W = W + .00034 * Math.sin(2*B-2*C+2*D);
		W = W - .00026 * Math.sin(2*B-2*C);
		W = W + .00025 * Math.sin(A-2*B-2*C-2*D);
		W = W + .00024 * Math.sin(A-2*B);
		W = W + .00023 * Math.sin(A+2*B-2*C+D);
		W = W + .00023 * Math.sin(A-2*C-D);
		W = W + .00019 * Math.sin(A+2*C);
		W = W + .00012 * Math.sin(A-2*C-F);
		W = W + .00011 * Math.sin(A-2*C+D);
		W = W + .00011 * Math.sin(A-2*B-2*C-D);
		W = W - .00010 * Math.sin(2*C+F);
		W = W + .00009 * Math.sin(A-C);
		W = W + .00008 * Math.sin(C+F);
		W = W - .00008 * Math.sin(2*B+2*C+2*D);
		W = W - .00008 * Math.sin(2*D);
		W = W - .00007 * Math.sin(2*B+2*D-F);
		W = W + .00006 * Math.sin(2*B+2*D+F);
		W = W - .00005 * Math.sin(A+2*B);
		W = W + .00005 * Math.sin(3*A);
		W = W - .00005 * Math.sin(A+16*E-18*G);
		W = W - .00005 * Math.sin(2*A+2*B+2*D);
		W = W + .00004 * numCenturiesSince1900 * Math.sin(2*B+2*D);
		W = W + .00004 * Math.cos(A+16*E-18*G);
		W = W - .00004 * Math.sin(A-2*B+2*C);
		W = W - .00004 * Math.sin(A-4*C);
		W = W - .00004 * Math.sin(3*A-2*C);
		W = W - .00004 * Math.sin(2*B+2*C+D);
		W = W - .00004 * Math.sin(2*C-D);
		W = W - .00003 * Math.sin(2*F);
		W = W - .00003 * Math.sin(A-2*C+2*F);
		W = W + .00003 * Math.sin(2*B-2*C+D+F);
		W = W - .00003 * Math.sin(2*C+D-F);
		W = W + .00003 * Math.sin(2*A+2*B-2*C+2*D);
		W = W + .00003 * Math.sin(2*C-2*F);
		W = W - .00003 * Math.sin(2*A-2*C+F);
		W = W + .00003 * Math.sin(A+2*B-2*C+2*D+F);
		W = W - .00003 * Math.sin(2*A-4*C);
		W = W + .00002 * Math.sin(2*B-2*C+2*D+F);
		W = W - .00002 * Math.sin(2*A+2*B+D);
		W = W - .00002 * Math.sin(2*A-D);
		W = W + .00002 * numCenturiesSince1900 * Math.cos(A+16*E-18*G);
		W = W + .00002 * Math.sin(4*C);
		W = W - .00002 * Math.sin(2*B-C+2*D);
		W = W - .00002 * Math.sin(A+2*B-2*C);
		W = W - .00002 * Math.sin(2*A+D);
		W = W - .00002 * Math.sin(2*A-2*B-D);
		W = W + .00002 * Math.sin(A+2*C-F);
		W = W + .00002 * Math.sin(2*A-F);
		W = W - .00002 * Math.sin(A-4*C+F);
		W = W + .00002 * numCenturiesSince1900 * Math.sin(A+16*E-18*G);
		W = W - .00002 * Math.sin(A-2*B-2*D-F);
		W = W + .00002 * Math.sin(2*A-2*B-2*D);
		W = W - .00002 * Math.sin(A+2*C+D);
		W = W - .00002 * Math.sin(A-2*B+2*C-D);
		
		Position position = calculatePosition(meanLongitudeOfMoon, U, V, W);
		double julianDate  = (daysFromEpoc + NEW_STANDARD_EPOC)-0.5;
		double age = normalize((julianDate - NEW_MOON_EPOC) / DAYS_IN_LUNAR_MONTH);
		double newAge = age * 2 * Math.PI;// Convert phase to radians
		// calculate moon's distance anomalistic phase
		double distance = 2 * Math.PI * normalize((julianDate - 2451562.2) / 27.55454988); //anomalistic month
		// Set Moon Distance
		distance = roundToDecimal(60.5 - 3.3 * Math.cos(distance) - 0.6 * 
							Math.cos(2 * newAge - distance) - 0.5 * Math.cos(2 * newAge));
		position.setDistance(distance);
		
		// calculate moon's ecliptic latitude
	    double latitudeEcliptic = 2*Math.PI*normalize( ( julianDate - 2451565.2 ) / 27.212220817 );
	    latitudeEcliptic = 5.1*Math.sin( latitudeEcliptic );
	    position.setLatitudeEcliptic(latitudeEcliptic);
		return position;
	}
	
	/**
	 * Normalize method value (range 0-1)
	 * @param v  Double number
	 * @return Double number
	 */
	public Double normalize(Double v) {
		v = v - Math.floor(v); // arredonda p baixo
		if (v < 0) {
			v = v + 1;
		}
		return v;
	}
	
	/**
	 *   Convert  Julian  date  to  year,  month, day, which are
     *returned as an Array.  
     *
     * @param jd Double value 
     * @return Date yyyy/mm/dd
     */
	public Date julianDatetoDate(Double jd) {
			Double z, f, a, alpha, b, c, d, e;
			
			jd += 0.5;
			z = Math.floor(jd);
			f = jd - z;
			
			if (z < 2299161.0) {
			 a = z;
			} else {
			 alpha = Math.floor((z - 1867216.25) / 36524.25);
			 a = z + 1 + alpha - Math.floor(alpha / 4);
			}
			
			b = a + 1524;
			c = Math.floor((b - 122.1) / 365.25);
			d = Math.floor(365.25 * c);
			e = Math.floor((b - d) / 30.6001);
			int month = (int) Math.floor((e < 14) ? (e - 1) : (e - 13));
			
			int year = (int) Math.floor((month > 2) ? (c - 4716) : (c - 4715));
			int day = (int) Math.floor(b - d - Math.floor(30.6001 * e) + f);
	
			return new Date(year-ERRORCORRECTIONDATE, month-1, day+1);
}
	
	/**
	 * Change to 2 decimal places
	 * @param x Double number
	 * @return Double number
	 */
	public Double roundToDecimal(Double x) {
		return (Math.round(100 * x) / 100.0); // arredonda p cima
	}

	/**
	 * Calculates RA (Right Ascension), Dec (Declination) and Longitude Ecliptical from
	 * intermediate calculations 
	 * @param meanLongitude longitude
	 * @param U u
	 * @param V v
	 * @param W w
	 * @return position of sun @{link Position} 
	 */
	public Position calculatePosition(double meanLongitude, double U, double V, double W) {
		double S = W / Math.sqrt(U - V*V);
		double rightAscention = meanLongitude + Math.asin(S);
		
		//System.out.println(Math.toDegrees(rightAscention));
		
		double declination = Math.asin(V / Math.sqrt(U));
		
		double longitudeEcliptical = Math.toDegrees(rightAscention)-1;
		
		longitudeEcliptical = longitudeEcliptical < 360 ? longitudeEcliptical : longitudeEcliptical-360;

//		System.err.println("calculatePosition: ("+rightAscention+" , "+declination+") + "+meanLongitude+" , "+U+" , "+V+" , "+W);
		
		return new Position(rightAscention, declination, longitudeEcliptical, 0);
	}
	
	
	/**
	 * calculate LST (Local Sideral Time at 0h zone time)
	 * @param daysFromEpoc days From Epoc
	 * @param timeZoneShift time Zone Shift
	 * @param longitude longitude of your location
	 * @return LST double value
	 */
	public double calculateLST(double daysFromEpoc, double timeZoneShift, double longitude) {
		double L = longitude/360;
		double ret = daysFromEpoc/NUM_DAYS_IN_CENTURY;

		double S;
		S = 24110.5 + 8640184.813*ret;
		S = S + 86636.6*timeZoneShift + 86400*L;
		
		S = S/86400.0;
		S = S - ((int) S);
		
		ret = S * 360.0 * DR;
		
//		System.err.println("calculateLST: "+ret);
		return ret;
	}
	
	/**
	 * Compute truncated Julian Date.
	 * @param calendar calendar Instance
	 * @return add +0.5 for non-truncated Julian Date
	 */
	public int calculateJulianDate(Calendar calendar) {
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH)+1;
		int year = calendar.get(Calendar.YEAR);
		int julianDate = -1 * (int) ( 7 * (((month+9)/12)+year) / 4);
		
		int offset = 0;
		boolean after1583 = year >= 1583;
		if(after1583) {
			int S = sgn(month-9);
			int A = Math.abs(month-9);
			
			offset = year + S * (A/7);
			offset = -1 * ( (offset/100) +1) * 3/4;
		}
		
		
		julianDate = julianDate + (275*month/9) + day + offset;
		julianDate = julianDate + 1721027 + (after1583 ? 2 : 0) + 367*year;

		julianDate--; //truncate
		return julianDate;
	}
	
	/**
	 * Set Risen And Set Amounts
	 * @param event Event 
	 */
	public void setRisenAndSetAmounts(Event event) {		
		Time midnight = new Time(24,00);
		
		switch(event.getType()) {
		case NO_CHANGE_PREVIOUSLY_RISEN:
			event.setRisenAmount(new Time(24,0));
			event.setSetAmount(new Time(0,0));
			break;
		case NO_CHANGE_PREVIOUSLY_SET:
			event.setRisenAmount(new Time(0,0));
			event.setSetAmount(new Time(24,0));
			break;
		case ONLY_SET:
			event.setRisenAmount(event.getSet());
			event.setSetAmount(difference(midnight, event.getSet()));
			break;
		case ONLY_RISEN:
			event.setRisenAmount(difference(midnight, event.getRise()));
			event.setSetAmount(event.getRise());
			break;
		default:
			event.setRisenAmount(difference(event.getSet(), event.getRise()));
			event.setSetAmount(difference(event.getRise(), event.getSet()));
		}
		
	}
	
	/**
	 * Check the difference of Times
	 * @param t1 Time 
	 * @param t2 Time
	 * @return Time difference
	 */
	public Time difference(Time t1, Time t2) {		
		int hour = t1.getHour() - t2.getHour();
		int min = t1.getMin() - t2.getMin();
		
		if(min < 0) {
			hour--;
			min+=60;
		}
		
		if(hour < 0) {
			hour += 24;
		}
		
		return new Time(hour,min);
	}
	
	/**
	 * Set Meridian Crossing (noon)
	 * @param event Event
	 */
	public void setMeridianCrossing(Event event) {
		switch(event.getType()) {
		case RISEN_AND_SET:
			Time lengthOfDay = event.getRisenAmount();
			int totalMins = (lengthOfDay.getHour()*60 + lengthOfDay.getMin())/2;
			
			int hour = event.getRise().getHour() + (totalMins/60);
			int min  = event.getRise().getMin()  + (totalMins%60);
			
			if(min >= 60) {
				hour++;
				min = min - 60;
			}
			event.setMeridianCrossing(new Time(hour, min));
			break;
		default:
			event.setMeridianCrossing(null);
		}
	}
	/**
	 * Set Anti Meridian Crossing (midnight)
	 * @param event Event
	 */
	public void setAntimeridianCrossing(Event event) {
		switch(event.getType()) {
		case RISEN_AND_SET:
			Time lengthOfNight = event.getSetAmount();
			int totalMins = (lengthOfNight.getHour()*60 + lengthOfNight.getMin())/2;
			
			int hour = event.getSet().getHour() + (totalMins/60);
			int min  = event.getSet().getMin()  + (totalMins%60);
			
			if(min >= 60) {
				hour++;
				min = min - 60;
			}
			
			if(hour >= 24) {
				hour -= 24;
			}
			event.setAntimeridianCrossing(new Time(hour, min));
			break;
		default:
			event.setAntimeridianCrossing(null);
		}
	}
	/**
	 * Find a type of Day 
	 * NO_CHANGE_PREVIOUSLY_SET
	 * NO_CHANGE_PREVIOUSLY_RISEN
	 * ONLY_SET
	 * ONLY_RISEN
	 * RISEN_AND_SET
	 * 
	 * @param result TestResult result 
	 * @param lastV Double last Value
	 * @return Event.HorizonToHorizonCrossing 
	 */
	public Event.HorizonToHorizonCrossing findTypeOfDay(TestResult result, double lastV) {

		if(result.rise==null && result.set==null) {
			if (lastV < 0) {
				return Event.HorizonToHorizonCrossing.NO_CHANGE_PREVIOUSLY_SET;
			} else {
				return Event.HorizonToHorizonCrossing.NO_CHANGE_PREVIOUSLY_RISEN;
			}
			
		} else if(result.rise==null) {
			return Event.HorizonToHorizonCrossing.ONLY_SET;
			
		} else if(result.set==null) {
			return Event.HorizonToHorizonCrossing.ONLY_RISEN;
			
		} else {
			return Event.HorizonToHorizonCrossing.RISEN_AND_SET;
		}
	}

	
	
	/**
	 *  Check for valid date, if the month hesitate 28 or 29 days in February, and 30 or 31 days (other months)
	 *  
	 *  @param day 
	 *  		Day of month
	 *  @param month
	 *  		Month of year
	 *  @param year 
	 *  		Year
	 *  @return boolean value
	 */
	public boolean isDayOfMonth(int day, int month, int year) {

		int isDayOfMonth;

		if ((month < 1) || (12 < month)) {
			return false; // invalid month
		}
		switch (month) {
		case 2:
			isDayOfMonth = 28; // Feb normal
			if (year % 4 == 0) {
				if ((year % 100 != 0) || (year % 400 == 0)) {
					isDayOfMonth = 29; // Feb leap year
				}
			}
			break;
		case 4:
			isDayOfMonth = 30;
			break;
		case 6:
			isDayOfMonth = 30;
			break;
		case 9:
			isDayOfMonth = 30;
			break;
		case 11:
			isDayOfMonth = 30;
			break;

		default:
			isDayOfMonth = 31;
			break;
		}

		return ((0 < day) && (day <= isDayOfMonth));
	}

	/**
	 * Check Moon's String Phase
	 * 
	 * @param ageInDays Moon Age in Days
	 * @return String Phase
	 */
	public String phase(double ageInDays){
			String phase;
			
			if (ageInDays <  1.84566)
				phase = "NEW";
		    else if (ageInDays <  5.53699)
		    	phase = "Waxing crescent";
		    else if (ageInDays <  9.22831 )
		    	phase = "First quarter";
		    else if (ageInDays < 12.91963 )
		    	phase = "Waxing minguante";
		    else if (ageInDays < 16.61096 )
		    	phase = "FULL";
		    else if (ageInDays < 20.30228 )
		    	phase = "Waning gibbous";
		    else if (ageInDays < 23.99361 )
		    	phase = "Last quarter";
		    else if (ageInDays < 27.68493 )
		    	phase = "Waning crescent";
		    else                     
		    	phase = "NEW";
			return  phase;
	}
	
	/**
	 * Convert degrees to a valid angle:
	 * @param deg
	 * @return Double valid angle:
	 */
	double angle(double deg) {
		while (deg >= 360.)
			deg -= 360.;
		while (deg < 0.)
			deg += 360.;
		return deg *  (Math.PI / 180);
	}

	
	/**
	 * Return the phase angle for the given date, in RADIANS.
	 * Equation from Meeus eqn. 46.4.
	 * 
	 * @param date
	 * 			{@link Date}
	 * @return Double Angle of Moon to Draw
	 */
	public Double getAnglePhase(Date date) {
		if (date == null) {
			date = new Date();
		}

		// Time measured in Julian centuries from epoch J2000.0:
		Date Tepoch = new Date(2000, 0, 1);
		Tepoch.setTime(getTime(Tepoch.getTime()));
		Double T = (decimalYears(date)+ERRORCORRECTIONDATE - decimalYears(Tepoch)) / 100.;
		Double T2 = T * T;
		Double T3 = T2 * T;
		Double T4 = T3 * T;

		// Mean elongation of the moon:
		Double D = angle(297.8502042 + 445267.1115168 * T - 0.0016300 * T2 + T3
				/ 545868 + T4 / 113065000);
		// Sun's mean anomaly:
		Double M = angle(357.5291092 + 35999.0502909 * T - 0.0001536 * T2 + T3
				/ 24490000);
		// Moon's mean anomaly:
		Double Mprime = angle(134.9634114 + 477198.8676313 * T + 0.0089970 * T2
				- T3 / 3536000 + T4 / 14712000);

		return (Double) (angle(180 - (D /  (Math.PI / 180)) - 6.289 * Math.sin(Mprime) + 2.100
				* Math.sin(M) - 1.274 * Math.sin(2 * D - Mprime) - 0.658
				* Math.sin(2 * D) - 0.214 * Math.sin(2 * Mprime) - 0.110
				* Math.sin(D)));
	}

	/** 
	 * Convert to DecimalYear
	 * 
	 * @param date
	 * 			{@link Date}
	 * @return decimal Years
	 */
	public double decimalYears(Date date) {
		// getTime() returns milliseconds Math.since Jan 1, 1970, so:
		return date.getTime() / 365.242191 / (24 * 60 * 60 * 1000);
	}
	
	/** 
	 * Correction in date time milliseconds
	 * 
	 * @param time
	 * 			Time in milliseconds
	 * @return long Years
	 */
	public Long getTime(Long time) {

		// see if the day is decimal:
		String decimal = ".5";
		float correction = Float.valueOf(decimal).floatValue() * 24 * 60 * 60
				* 1000;

		// This is so stupid -- Date has a ctor to set itself
		// from a string, but has no way of setting itself
		// from a string after construction! Sigh.
		Date stupid;
		try {
			stupid = new Date();
			stupid.setTime(time);
		} catch (java.lang.IllegalArgumentException e) {
			stupid = new Date();
		}

		// Now add the appropriate decimal days:
		return stupid.getTime() + (long) correction;

	}

//	/**
//	 * Check Moon's Zodiac Sideral utiliza as constelações como referência
//	 * 
//	 * @param longitudeEcliptical Moon 
//	 * @return String zodiac
//	 */
//	public static String zodiacSidereal(double longitudeEcliptical) {
//		String zodiac;
//		if (longitudeEcliptical < 25.07)
//			zodiac = "Pisces";
//		else if (longitudeEcliptical < 55.13)
//			zodiac = context.getString(R.string.aries);
//		else if (longitudeEcliptical < 83.59)
//			zodiac = context.getString(R.string.taurus);
//		else if (longitudeEcliptical < 112.36)
//			zodiac = context.getString(R.string.gemini);
//		else if (longitudeEcliptical < 142.16)
//			zodiac = context.getString(R.string.cancer);
//		else if (longitudeEcliptical< 172.15)
//			zodiac = context.getString(R.string.leo);
//		else if (longitudeEcliptical< 201.43)
//			zodiac = context.getString(R.string.virgo);
//		else if (longitudeEcliptical < 232.40)
//			zodiac = context.getString(R.string.libra);
//		else if (longitudeEcliptical < 263.03)
//			zodiac = context.getString(R.string.scorpio);
//		else if (longitudeEcliptical < 293.37)
//			zodiac = context.getString(R.string.sagittarius);
//		else if (longitudeEcliptical < 325.05)
//			zodiac = context.getString(R.string.capricorn);
//		else if (longitudeEcliptical < 354.15)
//			zodiac = context.getString(R.string.aquarius);
//		else
//			zodiac = context.getString(R.string.pisces);
//		return zodiac;
//	}
//	
//	/**
//	 * Check Moon's Zodiac UAI União Astronômica Internacional 
//	 * Também utiliza as constelações como referência considerando Ofiúco (Serpentario) 13a constelacao
//	 * 
//	 * @param longitudeEcliptical Moon 
//	 * @return String zodiac
//	 */
//	public static String zodiacIAU(Context context, double longitudeEcliptical) {
//		String zodiac;
//		if (longitudeEcliptical < 28.31)
//			zodiac = "Pisces";
//		else if (longitudeEcliptical < 53.03)
//			zodiac = context.getString(R.string.aries);
//		else if (longitudeEcliptical < 90.25)
//			zodiac = context.getString(R.string.taurus);
//		else if (longitudeEcliptical < 118.05)
//			zodiac = context.getString(R.string.gemini);
//		else if (longitudeEcliptical < 138.10)
//			zodiac = context.getString(R.string.cancer);
//		else if (longitudeEcliptical< 173.56)
//			zodiac = context.getString(R.string.leo);
//		else if (longitudeEcliptical< 217.21)
//			zodiac = context.getString(R.string.virgo);
//		else if (longitudeEcliptical < 241.29)
//			zodiac = context.getString(R.string.libra);
//		else if (longitudeEcliptical < 248.34)
//			zodiac = context.getString(R.string.scorpio);
//		else if (longitudeEcliptical < 265.50)
//			zodiac = context.getString(R.string.ophiuchus);
//		else if (longitudeEcliptical < 300.28)
//			zodiac = context.getString(R.string.sagittarius);
//		else if (longitudeEcliptical < 327.51)
//			zodiac = context.getString(R.string.capricorn);
//		else if (longitudeEcliptical < 350.59)
//			zodiac = context.getString(R.string.aquarius);
//		else
//			zodiac = context.getString(R.string.pisces);
//		return zodiac;
//	}
//	
//	/**
//	 * Check Moon's Zodiac / Ocidental / Signos utiliza o sol como referência.
//	 * 
//	 * @param longitudeEcliptical Moon 
//	 * @return String zodiac
//	 */
//	public static String zodiacTropical(Context context, double longitudeEcliptical) {
//		String zodiac;
//		if (longitudeEcliptical < 30)
//			zodiac = "Pisces";
//		else if (longitudeEcliptical < 60)
//			zodiac = context.getString(R.string.aries);
//		else if (longitudeEcliptical < 90)
//			zodiac = context.getString(R.string.taurus);
//		else if (longitudeEcliptical < 120)
//			zodiac = context.getString(R.string.gemini);
//		else if (longitudeEcliptical < 150)
//			zodiac = context.getString(R.string.cancer);
//		else if (longitudeEcliptical< 180)
//			zodiac = context.getString(R.string.leo);
//		else if (longitudeEcliptical< 210)
//			zodiac = context.getString(R.string.virgo);
//		else if (longitudeEcliptical < 240)
//			zodiac = context.getString(R.string.libra);
//		else if (longitudeEcliptical < 270)
//			zodiac = context.getString(R.string.scorpio);
//		else if (longitudeEcliptical < 300)
//			zodiac = context.getString(R.string.sagittarius);
//		else if (longitudeEcliptical < 330)
//			zodiac = context.getString(R.string.capricorn);
//		else if (longitudeEcliptical < 360)
//			zodiac = context.getString(R.string.aquarius);
//		else
//			zodiac = context.getString(R.string.pisces);
//		return zodiac;
//	}
	
	/**
	 * Check Moon's Zodiac
	 * 
	 * @param longitudeEcliptical Moon 
	 * @return String zodiac
	 */
	public String zodiac(double longitudeEcliptical) {
		String zodiac;
		if (longitudeEcliptical < 33.18)
			zodiac = "Pisces";
		else if (longitudeEcliptical < 51.16)
			zodiac = "Aries";
		else if (longitudeEcliptical < 93.44)
			zodiac = "Taurus";
		else if (longitudeEcliptical < 119.48)
			zodiac = "Gemini";
		else if (longitudeEcliptical < 135.30)
			zodiac = "Cancer";
		else if (longitudeEcliptical< 173.34)
			zodiac = "Leo";
		else if (longitudeEcliptical< 224.17)
			zodiac = "Virgo";
		else if (longitudeEcliptical < 242.57)
			zodiac = "Libra";
		else if (longitudeEcliptical < 271.26)
			zodiac = "Scorpio";
		else if (longitudeEcliptical < 302.49)
			zodiac = "Sagittarius";
		else if (longitudeEcliptical < 311.72)
			zodiac = "Capricorn";
		else if (longitudeEcliptical < 348.58)
			zodiac = "Aquarius";
		else
			zodiac = "Pisces";
		return zodiac;
	}
	
	/**
	 * Generates and adds the moons of the current Year
	 * 
	 * @param calendar Astronomical Calendar
	 * @param gps GpsCoordinate
	 * @return ArrayList of lunar year 
	 */
	public void calculateYear(Calendar calendar, GpsCoordinate gps) {
		double utcToLocal = 0d;
		double timeZoneShift;
		int julianDate = 0;//note that the julianDate is truncated
		double daysFromEpoc = 0d;
		double LST = 0d;
		
		MoonEvent moon = null;
		SunEvent sun = null;
		eclipseLunar = new ArrayList<MoonEvent>();
		eclipseSolar = new ArrayList<SunEvent>();
		planetYear = new ArrayList<ArrayList<ArrayList<Event>>>();
		lunarYear = new ArrayList<ArrayList<MoonEvent>>();
		solarYear = new ArrayList<ArrayList<SunEvent>>();
		
		int ano = calendar.get(Calendar.YEAR);
		int mes = calendar.get(Calendar.MONTH);
		int dia = calendar.get(Calendar.DAY_OF_MONTH);

		Position moonToday = null;
		Position moonTomorrow;
		for (int iMonth = 0; iMonth < 14; iMonth++) {
				monthLunar = new ArrayList<MoonEvent>();
				monthSolar = new ArrayList<SunEvent>();
				if (iMonth >= 1 && iMonth <= 12){
					monthPlanet = new ArrayList<ArrayList<Event>>(9);
					for (int iDay = 0; iDay < 9; iDay++) {
						monthPlanet.add(new ArrayList<Event>());
					};
				}
				for (int iDay = 1; iDay <= 31; iDay++) {
					if (iMonth == 0){
								if (isDayOfMonth(iDay, 12,ano-1)) {
										calendar.set(Calendar.YEAR, ano-1);
										calendar.set(Calendar.MONTH, 11);
										calendar.set(Calendar.DAY_OF_MONTH, iDay);
										
										utcToLocal = calculateUtcToDSTLocal(calendar);
										timeZoneShift = -1  * utcToLocal/HOURS_IN_DAY;
										julianDate = calculateJulianDate(calendar); //note that the julianDate is truncated
										daysFromEpoc = (julianDate - NEW_STANDARD_EPOC) + 0.5;
										LST     = calculateLST(daysFromEpoc  , timeZoneShift, gps.getLongitude());
										daysFromEpoc = daysFromEpoc + timeZoneShift;
										//calculate today sun
										Position sunToday    = calculateSunPosition(daysFromEpoc);	
										Position sunTomorrow    = calculateSunPosition(daysFromEpoc+1);
										sunTomorrow = ensureSecondAscentionGreater(sunToday, sunTomorrow);
										
										//calculate today moon
										moonToday    = calculateMoonPosition(daysFromEpoc);
										moonTomorrow = calculateMoonPosition(daysFromEpoc+1);
														
										moonTomorrow = ensureSecondAscentionGreater(moonToday, moonTomorrow);		
										moon = new MoonEvent(calculate(MOONRISE_MOONSET_OFFSET, gps, LST, moonToday, moonTomorrow));
										moon.setAgeInDays(calculateMoonsAge(julianDate+1));
										moon.setIlluminationPercent(calculateMoonIlluminationPercent(moon.getAgeInDays()+1));
										moon.setZodiac(zodiac(moonToday.getLongitudeEcliptic()));
										moon.setPhase(phase(moon.getAgeInDays()));
										moon.setDate(calendar.getTime());
										moon.setPerigeeOrApogee(setApogeuAndPerigeu(moon));
										moon.setJulianDate(julianDate);
										moon.setAnglePhase(getAnglePhase(calendar.getTime()));
										monthLunar.add(moon);
								}
					}
					if (iMonth >= 1 && iMonth <= 12){
								if (isDayOfMonth(iDay, iMonth,ano)) {
									calendar.set(Calendar.YEAR, ano);
									calendar.set(Calendar.MONTH, iMonth-1);
									calendar.set(Calendar.DAY_OF_MONTH, iDay);
									
									utcToLocal = calculateUtcToDSTLocal(calendar);
									timeZoneShift = -1  * utcToLocal/HOURS_IN_DAY;
									julianDate = calculateJulianDate(calendar); //note that the julianDate is truncated
									daysFromEpoc = (julianDate - NEW_STANDARD_EPOC) + 0.5;
									LST     = calculateLST(daysFromEpoc  , timeZoneShift, gps.getLongitude());
									daysFromEpoc = daysFromEpoc + timeZoneShift;
									
									//calculate sun
									Position sunToday    = calculateSunPosition(daysFromEpoc);	
									Position sunTomorrow    = calculateSunPosition(daysFromEpoc+1);
									sunTomorrow = ensureSecondAscentionGreater(sunToday, sunTomorrow);
									sun = new SunEvent(calculate(SUNRISE_SUNET_OFFSET, gps, LST, sunToday, sunTomorrow));		
									sun.setName("Sun");
									sun.setZodiac(zodiac(sunToday.getLongitudeEcliptic()));
									sun.setPosition(sunToday);
									sun.setDate(calendar.getTime());
									sun.setJulianDate(julianDate);
									
									//calculate moon
									moonToday    = calculateMoonPosition(daysFromEpoc);
									moonTomorrow = calculateMoonPosition(daysFromEpoc+1);
													
									moonTomorrow = ensureSecondAscentionGreater(moonToday, moonTomorrow);		
									moon = new MoonEvent(calculate(MOONRISE_MOONSET_OFFSET, gps, LST, moonToday, moonTomorrow));
									moon.setName("Moon");
									moon.setAgeInDays(calculateMoonsAge(julianDate+1));
									moon.setIlluminationPercent(calculateMoonIlluminationPercent(moon.getAgeInDays()+1));
									moon.setZodiac(zodiac(moonToday.getLongitudeEcliptic()));
									moon.setPhase(phase(moon.getAgeInDays()));
									moon.setDate(calendar.getTime());
									moon.setJulianDate(julianDate);
									moon.setAnglePhase(getAnglePhase(calendar.getTime()));
									moon.setPerigeeOrApogee(setApogeuAndPerigeu(moon));
									
									isEclipseLunar(moon,moonTomorrow,sunToday,sunTomorrow);
									isEclipseSolar(sun, moon,moonTomorrow,sunToday,sunTomorrow);
									
									monthSolar.add(sun);
									monthLunar.add(moon);
									
									//calculate planets
									OrbitCalculator planetCalc = new OrbitCalculator();
									ArrayList<Event> planetList = planetCalc.computeElementsPosition(daysFromEpoc);
									
									
									for (int i=0; i<planetList.size(); i++){
										Position planetToday    = planetList.get(i).getPosition();
										Position planetTomorrow = planetList.get(i).getPositionTomorrow();
										planetList.get(i).setPosition(planetToday);
										
										Event aux = calculate(SUNRISE_SUNET_OFFSET, this.gps, LST, planetToday, planetTomorrow);
										//Teste NAUTICAL_TWILIGHT_OFFSET
										aux.setDate(calendar.getTime());
										aux.setJulianDate(julianDate);
										aux.setName(planetList.get(i).getName());
										aux.setPosition(planetToday);
										
										aux.setZodiac(zodiac(planetToday.getLongitudeEcliptic()));
										  if (aux.getName().equals("Mercury")){
											  	monthPlanet.get(i).add(aux);
										  }
										  else if (aux.getName().equals("Venus")){
											  	monthPlanet.get(i).add(aux);
										  }
										  else if (aux.getName().equals("Mars")){
											  	monthPlanet.get(i).add(aux);
										  }
										  else if (aux.getName().equals("Earth")){
											  	monthPlanet.get(i).add(aux);
										  }
										  else if (aux.getName().equals("Jupiter")){
											  	monthPlanet.get(i).add(aux);
										  }
										  else if (aux.getName().equals("Saturn")){
											  	monthPlanet.get(i).add(aux);
										  }
										  else if (aux.getName().equals("Uranus")){
											  	monthPlanet.get(i).add(aux);
										  }
										  else if (aux.getName().equals("Neptune")){
											  	monthPlanet.get(i).add(aux);
										  }
										  else if (aux.getName().equals("Pluto")){
											  	monthPlanet.get(i).add(aux);
										  }
									}
								}
					}
					if (iMonth == 13){
								if (isDayOfMonth(iDay, 1,ano+1)) {
									calendar.set(Calendar.YEAR, ano+1);
									calendar.set(Calendar.MONTH, 0);
									calendar.set(Calendar.DAY_OF_MONTH, iDay);
									
									utcToLocal = calculateUtcToDSTLocal(calendar);
									timeZoneShift = -1  * utcToLocal/HOURS_IN_DAY;
									julianDate = calculateJulianDate(calendar); //note that the julianDate is truncated
									daysFromEpoc = (julianDate - NEW_STANDARD_EPOC) + 0.5;
									LST     = calculateLST(daysFromEpoc  , timeZoneShift, gps.getLongitude());
									daysFromEpoc = daysFromEpoc + timeZoneShift;
									
									//calculate today sun
									Position sunToday    = calculateSunPosition(daysFromEpoc);	
									Position sunTomorrow    = calculateSunPosition(daysFromEpoc+1);	
									
									sunTomorrow = ensureSecondAscentionGreater(sunToday, sunTomorrow);
									
									//calculate today moon
									moonToday    = calculateMoonPosition(daysFromEpoc);
									moonTomorrow = calculateMoonPosition(daysFromEpoc+1);
													
									moonTomorrow = ensureSecondAscentionGreater(moonToday, moonTomorrow);		
									moon = new MoonEvent(calculate(MOONRISE_MOONSET_OFFSET, gps, LST, moonToday, moonTomorrow));
									moon.setAgeInDays(calculateMoonsAge(julianDate+1));
									moon.setIlluminationPercent(calculateMoonIlluminationPercent(moon.getAgeInDays()+1));
									moon.setZodiac(zodiac(moonToday.getLongitudeEcliptic()));
									moon.setPhase(phase(moon.getAgeInDays()));
									moon.setDate(calendar.getTime());
									moon.setPerigeeOrApogee(setApogeuAndPerigeu(moon));
									moon.setJulianDate(julianDate);
									moon.setAnglePhase(getAnglePhase(calendar.getTime()));
									monthLunar.add(moon);
								}	
					}	
//				System.out.println("- Julian date: "+julianDate+ " "+calendar.getTime() + " " + moon.position.date);
				}
				lunarYear.add(monthLunar);
				if (iMonth >= 1 && iMonth <= 12){
					solarYear.add(monthSolar);
					planetYear.add(monthPlanet);
				}	
		}
		// Update to current date time
		calendar.set(Calendar.YEAR, ano);
		calendar.set(Calendar.MONTH, mes);
		calendar.set(Calendar.DAY_OF_MONTH, dia);
	}	
	
	/**
	 * 
	 * @param calendar current calendar date
	 * @return String value "Apogee" or "Perigee"
	 */
	public String setApogeuAndPerigeu(MoonEvent moon) {
		// System.out.println("APOGEU "+calendar.getTime());
		
		for (int index = 0; index < perigeeApogeeCalculator.getApogeeList().size(); index++) {
			if (perigeeApogeeCalculator.getApogeeList().get(index).getYear() == moon.getDate().getYear()
					&& perigeeApogeeCalculator.getApogeeList().get(index).getMonth() == moon.getDate().getMonth()
					&& perigeeApogeeCalculator.getApogeeList().get(index).getDate() == moon.getDate().getDate()) {
				moon.setDate(perigeeApogeeCalculator.getApogeeList().get(index));
				getApogeeList().add(moon);
				return "Apogee";
			}
			// System.out.println(getApogeuList().get(index).getData());
		}

		// System.out.println("PERIGEU "+calendar.getTime());
		for (int index = 0; index < perigeeApogeeCalculator.getPerigeeList().size(); index++) {
			if (perigeeApogeeCalculator.getPerigeeList().get(index).getYear() == moon.getDate().getYear()
					&& perigeeApogeeCalculator.getPerigeeList().get(index).getMonth() == moon.getDate().getMonth()
					&& perigeeApogeeCalculator.getPerigeeList().get(index).getDate() == moon.getDate().getDate()) {
				moon.setDate(perigeeApogeeCalculator.getPerigeeList().get(index));
				getPerigeeList().add(moon);
				return "Perigee";
			}
			// System.out.println(getPerigeuList().get(index).getData());
		}
		return "";
	}
	
	/**
	 * 
	 * @param calendar current calendar date
	 * @return String value "Apogee" or "Perigee"
	 */
	public String isApogeuOrPerigeu(MoonEvent moon) {
		// System.out.println("APOGEU "+calendar.getTime());
		
		for (int index = 0; index < perigeeApogeeCalculator.getApogeeList().size(); index++) {
			if (perigeeApogeeCalculator.getApogeeList().get(index).getYear() == moon.getDate().getYear()
					&& perigeeApogeeCalculator.getApogeeList().get(index).getMonth() == moon.getDate().getMonth()
					&& perigeeApogeeCalculator.getApogeeList().get(index).getDate() == moon.getDate().getDate()) {
				return "Apogee";
			}
			// System.out.println(getApogeuList().get(index).getData());
		}

		// System.out.println("PERIGEU "+calendar.getTime());
		for (int index = 0; index < perigeeApogeeCalculator.getPerigeeList().size(); index++) {
			if (perigeeApogeeCalculator.getPerigeeList().get(index).getYear() == moon.getDate().getYear()
					&& perigeeApogeeCalculator.getPerigeeList().get(index).getMonth() == moon.getDate().getMonth()
					&& perigeeApogeeCalculator.getPerigeeList().get(index).getDate() == moon.getDate().getDate()) {
				return "Perigee";
			}
			// System.out.println(getPerigeuList().get(index).getData());
		}
		return "";
	}
	
	public void isEclipseSolar(SunEvent sun, MoonEvent moonToday, Position moonTomorrow, Position sunToday, Position sunTomorrow) {

		Date dateBegin = null;
		Date dateEnd = null;
		double hour = 0;
		double minute = 0;
		
		boolean solar = false;
		double changeInDeclinationMoon = moonTomorrow.getDeclination() - moonToday.getPosition().getDeclination();
		double changeInAscentionMoon   = moonTomorrow.getRightAscention() - moonToday.getPosition().getRightAscention();
		double changeInLongitudeMoon;
		if (moonTomorrow.getLongitudeEcliptic() >= moonToday.getPosition().getLongitudeEcliptic())
		 changeInLongitudeMoon = moonTomorrow.getLongitudeEcliptic() - moonToday.getPosition().getLongitudeEcliptic();
		else
			changeInLongitudeMoon = (moonTomorrow.getLongitudeEcliptic()+360) - moonToday.getPosition().getLongitudeEcliptic();
		if (changeInLongitudeMoon > 360){
			changeInLongitudeMoon = changeInLongitudeMoon - 360;
		}
		double changeInDeclinationSun = sunTomorrow.getDeclination() - sunToday.getDeclination();
		double changeInAscentionSun   = sunTomorrow.getRightAscention() - sunToday.getRightAscention();
		double changeInLongitudeSun;
		if (sunTomorrow.getLongitudeEcliptic() > sunToday.getLongitudeEcliptic())
			changeInLongitudeSun   = sunTomorrow.getLongitudeEcliptic() - sunToday.getLongitudeEcliptic();
		else
			changeInLongitudeSun = (sunTomorrow.getLongitudeEcliptic()+360) - sunToday.getLongitudeEcliptic();
		if (changeInLongitudeSun > 360){
			changeInLongitudeSun = changeInLongitudeSun - 360;
		}
		for(int hourOfDay=0; hourOfDay<HOURS_IN_DAY * MINUTE_IN_HOURS; hourOfDay++) {
			
			double fractionOfDay = (hourOfDay+1) / ((double)HOURS_IN_DAY * MINUTE_IN_HOURS);
			
			double declinationMoon = moonToday.getPosition().getDeclination() + fractionOfDay*changeInDeclinationMoon;
			double ascentionMoon = moonToday.getPosition().getRightAscention() + fractionOfDay*changeInAscentionMoon;
			double longitudeMoon = moonToday.getPosition().getLongitudeEcliptic() + fractionOfDay*changeInLongitudeMoon;
		
			double declinationSun = sunToday.getDeclination() + fractionOfDay*changeInDeclinationSun;
			double ascentionSun = sunToday.getRightAscention() + fractionOfDay*changeInAscentionSun;
			double longitudeSun = sunToday.getLongitudeEcliptic()+ fractionOfDay*changeInLongitudeSun;
			
			// Eclipse Solar
			double longitude = longitudeMoon - longitudeSun;
			double declination = declinationMoon - declinationSun;
			double ascention = ascentionMoon - ascentionSun;
			
			if ((declination <= 0.0265 && declination >= -0.0265) &&
					(moonToday.getPosition().getLatitudeEcliptic() > -2.0)) {
				
					if ((longitude >= -2 && longitude <= 0.31)){
						hour =  (hourOfDay) / 60;
						minute =  (hourOfDay) / 24;
						if (ascention >= -0.03 &&  ascention <= 0.000046){
							if (dateBegin !=  moonToday.getDate()){
								solar = true;
								dateBegin = moonToday.getDate();
								dateBegin.setHours((int) hour);
								dateBegin.setMinutes((int) minute);
//								eclipseMoon = new SunEvent(moonToday);
								sun.setDateBegin(dateBegin);
								sun.setEclipseType("Eclipse Solar");
							}
						}
					}
			}
		}
		if (solar){
			dateEnd = moonToday.getDate();
			dateEnd.setHours((int) hour);
			dateEnd.setMinutes((int) minute);
			sun.setDateEnd(dateEnd);
			getEclipseSolar().add(sun);
		}
	}
	
	public void isEclipseLunar(MoonEvent moonToday, Position moonTomorrow, Position sunToday, Position sunTomorrow) {

		Date dateBegin = null;
		Date dateEnd = null;
		double hour = 0;
		double minute = 0;
		
		boolean lunar = false;
		double changeInDeclinationMoon = moonTomorrow.getDeclination() - moonToday.getPosition().getDeclination();
		double changeInAscentionMoon   = moonTomorrow.getRightAscention() - moonToday.getPosition().getRightAscention();
		double changeInLongitudeMoon;
		if (moonTomorrow.getLongitudeEcliptic() >= moonToday.getPosition().getLongitudeEcliptic())
		 changeInLongitudeMoon = moonTomorrow.getLongitudeEcliptic() - moonToday.getPosition().getLongitudeEcliptic();
		else
			changeInLongitudeMoon = (moonTomorrow.getLongitudeEcliptic()+360) - moonToday.getPosition().getLongitudeEcliptic();
		if (changeInLongitudeMoon > 360){
			changeInLongitudeMoon = changeInLongitudeMoon - 360;
		}
		double changeInDeclinationSun = sunTomorrow.getDeclination() - sunToday.getDeclination();
		double changeInAscentionSun   = sunTomorrow.getRightAscention() - sunToday.getRightAscention();
		double changeInLongitudeSun;
		if (sunTomorrow.getLongitudeEcliptic() > sunToday.getLongitudeEcliptic())
			changeInLongitudeSun   = sunTomorrow.getLongitudeEcliptic() - sunToday.getLongitudeEcliptic();
		else
			changeInLongitudeSun = (sunTomorrow.getLongitudeEcliptic()+360) - sunToday.getLongitudeEcliptic();
		if (changeInLongitudeSun > 360){
			changeInLongitudeSun = changeInLongitudeSun - 360;
		}
		for(int hourOfDay=0; hourOfDay<HOURS_IN_DAY * MINUTE_IN_HOURS; hourOfDay++) {
			
			double fractionOfDay = (hourOfDay+1) / ((double)HOURS_IN_DAY * MINUTE_IN_HOURS);
			
			double declinationMoon = moonToday.getPosition().getDeclination() + fractionOfDay*changeInDeclinationMoon;
			double ascentionMoon = moonToday.getPosition().getRightAscention() + fractionOfDay*changeInAscentionMoon;
			double longitudeMoon = moonToday.getPosition().getLongitudeEcliptic() + fractionOfDay*changeInLongitudeMoon;
			if (longitudeMoon > 360){
				longitudeMoon = longitudeMoon - 360;
			}
			double declinationSun = sunToday.getDeclination() + fractionOfDay*changeInDeclinationSun;
			double ascentionSun = sunToday.getRightAscention() + fractionOfDay*changeInAscentionSun;
			double longitudeSun = sunToday.getLongitudeEcliptic() + 180;
			if (longitudeSun>360){
				longitudeSun = longitudeSun - 360;
			}
			longitudeSun = longitudeSun + fractionOfDay*changeInLongitudeSun;
			
			declinationSun = Math.abs(declinationSun);
			declinationMoon= Math.abs(declinationMoon);
			
			ascentionSun = Math.abs(ascentionSun);
			ascentionMoon= Math.abs(ascentionMoon);
	
			double longitude = longitudeMoon - longitudeSun;
			double declination = Math.abs(declinationMoon) - Math.abs(declinationSun);
			double ascention = ascentionMoon - ascentionSun;
			if( (longitude >= -1.2 && longitude <= 1.2) &&
					(declination >= -0.024 && declination <= 0.024) &&
						(ascention >= 3.14 || ascention <= -3.135) && 
						(moonToday.getPosition().getLatitudeEcliptic() > -3.0) ){
					hour =  (hourOfDay+1) / ((double) HOURS_IN_DAY * MINUTE_IN_HOURS) * 24;
					minute =  (hourOfDay+1) / ((double) HOURS_IN_DAY * MINUTE_IN_HOURS) * 60;
					if (dateBegin != moonToday.getDate()){
						lunar = true;
						dateBegin = moonToday.getDate();
						dateBegin.setHours((int) hour);
						dateBegin.setMinutes((int) minute);
//						eclipseMoon = new MoonEvent(moonToday);
						moonToday.setDateBegin(dateBegin);
						moonToday.setEclipseType("Eclipse Lunar");
					}
			}
		}
		if (lunar){
			dateEnd = moonToday.getDate();
			dateEnd.setHours((int) hour);
			dateEnd.setMinutes((int) minute);
			moonToday.setDateEnd(dateEnd);
			getEclipseLunar().add(moonToday);
		}
	}
	
	double arredondar(double valor, int casas, int ceilOrFloor) {  
	    double arredondado = valor;  
	    arredondado *= (Math.pow(10, casas));  
	    if (ceilOrFloor == 0) {  
	        arredondado = Math.ceil(arredondado);             
	    } else {  
	        arredondado = Math.floor(arredondado);  
	    }  
	    arredondado /= (Math.pow(10, casas));  
	    return arredondado;  
	} 
}