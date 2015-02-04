package br.com.henriquewilhelm.orbit;

import java.util.Calendar;

public class BasicCalculator {

	/**
	 * Returns the Julian Day as 12h on 1 January 2000
	 * @param calendar calendar Instance
	 * @return double value Julian Date since year 2000
	 */
	private double daySinceJ2000(Calendar calendar) {
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		//System.out.println("UTC: " + month + "/" + day + "/" + year + "  " + hour + ":" + minute + ":" + second);

		if ((month == 1) || (month == 2)) {
			year = year - 1;
			month = month + 12;
		}

		double a = Math.floor(year / 100);
		double b = (2 - a + Math.floor(a / 4));
		double c = Math.floor(365.25 * year);
		double d = Math.floor(30.6001 * (month + 1)); // +1

		// days since J2000.0
		return (b + c + d - 730550.5 + day + (hour + minute / 60.0 + second / 3600.0) / 24.0);
	}
}
