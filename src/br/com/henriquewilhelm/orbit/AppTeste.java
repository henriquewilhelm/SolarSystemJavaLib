package br.com.henriquewilhelm.orbit;
import java.util.Calendar;
import java.util.TimeZone;

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
	 * Main of Test App for current date
	 * @param args String args
	 */
	public static void main(String[] args) { 

		GpsCoordinate gps = new GpsCoordinate(LATITUDE, LONGITUDE);
		// current date	
		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
//		calendar.set(Calendar.YEAR, 2018);
//		calendar.set(Calendar.MONTH, 0);
//		calendar.set(Calendar.DAY_OF_MONTH, 10);
//		calendar.set(Calendar.HOUR_OF_DAY, 1);
//		calendar.set(Calendar.MINUTE, 30);
		System.out.printf("**Calculations for %s****\n", calendar.getTime());
		
		Calculator calculator = new Calculator(gps, calendar);
		Result result = calculator.calculate(gps, calendar);
		System.out.println(result.toString());
	}
}
