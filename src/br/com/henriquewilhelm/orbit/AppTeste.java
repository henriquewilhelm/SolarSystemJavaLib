package br.com.henriquewilhelm.orbit;

import java.util.Calendar;

/**
 * Classe of Test App for current date
 * 
 * @author Henrique Wilhelm v2.0.0
 * @author zoglmannk v1.0.0
 * @version v2.0.0
 */
public class AppTeste {
	/**
	 * LATITUDE YOUR LOCATION (North latitudes positive)
	 */
	private static final double LATITUDE  = -27.185768; //Latitude of Florianopolis, Brazil
	/**
	 * LONGITUDE YOUR LOCATION (West longitudes negative)
	 */
	private static final double LONGITUDE = -48.575556; //Longitude of Florianopolis, Brazil 
	/**
	 * UTC_TO_DST (West is negative)													
	 */
	private static final int    UTC_TO_DST = -2; // Brazil (Summer Time)
	/**
	 * Main of Test App for current date
	 * @param args String args
	 */
	public static void main(String[] args) { 
		
		GpsCoordinate gps = new GpsCoordinate(LATITUDE, LONGITUDE);
		// current date	
		Calendar calendar = Calendar.getInstance();
		System.out.printf("**Calculations for %s****\n", calendar.getTime());
		
		Calculator calculator = new Calculator();
		Result result = calculator.calculate(gps, UTC_TO_DST, calendar);
		System.out.println(result.toString());
	}
}
