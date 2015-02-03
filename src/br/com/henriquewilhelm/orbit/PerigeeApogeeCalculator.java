package br.com.henriquewilhelm.orbit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This program helps those who need to know stage, age, distance and position
 * of the moon along the ecliptic on any date within several thousand years in
 * the past or in the future. The age of the moon day, as well as its visual
 * phase are given. The elliptical longitude moon is calculated, and the
 * corresponding constellation zodiac. The calculated position of the moon based
 * on the Julian day numbers corresponding to the calendar date. The date is set
 * for the valid day of the month.   *
 * http://www.abecedarical.com/zenosamples/zs_lunarphasecalc.html
 */
public class PerigeeApogeeCalculator {

	/**
	 * String to a given length with a given fill character.
	 */
	public final static String[] Months = new String[] { "Jan", "Feb", "Mar",
			"Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	int[] date = new int[3];
	int[] time = new int[3];

	MoonEvent lua;
	/**
	 * Current Calendar
	 */
	Calendar calendar;
	/**
	 * Apogee List
	 */
	private ArrayList<Date> apogeeList;
	/**
	 * Perigee List
	 */
	private ArrayList<Date> perigeeList;

	public ArrayList<Date> getApogeeList() {
		return apogeeList;
	}

	public void setApogeeList(ArrayList<Date> apogeeList) {
		this.apogeeList = apogeeList;
		
	}

	public ArrayList<Date> getPerigeeList() {
		return perigeeList;
	}

	public void setPerigeeList(ArrayList<Date> perigeeList) {
		this.perigeeList = perigeeList;
	}
	
	public PerigeeApogeeCalculator(Calendar calendar) {
		calculationPerigeeApogee(calendar);
	}

	/**
	 * Convert Julian Date Number to Date (year, month, day), which are returned as
	 * an Array.
	 * 
	 * @param td Julian Date Number to Date
	 * @return Double[] Array Double {YEAR, MONTH, DAY}
	 */
	public int[] julianYear(Double td) {
		Double z, f, a, alpha, c, b, d, e, mm;

		td += 0.5;
		z = Math.floor(td);
		f = td - z;

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
		mm = Math.floor((e < 14) ? (e - 1) : (e - 13));

		// System.out.println( Math.floor((mm > 2) ? (c - 4716) : (c - 4715))
		// +"/"+mm+"/"+Math.floor(b - d - Math.floor(30.6001 * e) + f) );

		return new int[] { (int) Math.floor((mm > 2) ? (c - 4716) : (c - 4715)),
				mm.intValue(), (int) Math.floor(b - d - Math.floor(30.6001 * e) + f) };
	}

	/**
	 * Convert Julian time to hour, minutes, and seconds, returned as a
	 * three-element array.
	 * 
	 * @param jd Julian Date Number 
	 * @return Double[] Array Double {HOUR, MINUTE, SECOND}
	 */
	public int[] julianTimeToHourMinuteSecond(Double jd) {
		Double ij;

		jd += 0.5; /* Astronomical to civil */
		ij = (jd - Math.floor(jd)) * 86400.0;
		return new int[] { (int) Math.floor(ij / 3600),
				(int) Math.floor((ij / 60) % 60), (int) Math.floor(ij % 60) };
	}

	/**
	 *  Degrees to radians.
	 *  
	 *  @param d Angle Degrees
	 *  @return Double value in Radians
	 */

	private Double degreesToRadians(Double d) {
		return (d * Math.PI) / 180.0;
	}

	/**
	 *  Range reduce angle in degrees. 
	 *  
	 *  @param a Angle Radians
	 *  @return Double value in Degrees
	 */

	private Double fixAngleInDegrees(Double a) {
		return a - 360.0 * (Math.floor((a) / 360.0));
	}

	private Double sumSeries(String trig, Double D, Double M, Double F,
			Double T, int[] argtab, Double[] coeff, int[] tfix, Double[] tfixc) {
		int i, j = 0, n = 0;
		Double sum = 0.0, arg, coef;

		D = degreesToRadians(fixAngleInDegrees(D));
		M = degreesToRadians(fixAngleInDegrees(M));
		F = degreesToRadians(fixAngleInDegrees(F));

		for (i = 0; coeff[i] != 0.0; i++) {
			arg = (D * argtab[j]) + (M * argtab[j + 1]) + (F * argtab[j + 2]);
			j += 3;
			coef = coeff[i];
			if (i == tfix[n]) {
				coef += T * tfixc[n++];
			}
			if (trig.equals("sin"))
				sum += coef * Math.sin(arg);
			else if (trig.equals("cos"))
				sum += coef * Math.cos(arg);
		}

		return sum;
	}

	/**
	 * We define the perigee and apogee period term arrays statically to avoid
	 * re-constructing them on every invocation of moonPerigeeApogee().
	 */
	int[] periarg = new int[] {
	/* D, M, F */
	2, 0, 0, 4, 0, 0, 6, 0, 0, 8, 0, 0, 2, -1, 0, 0, 1, 0, 10, 0, 0, 4, -1, 0,
			6, -1, 0, 12, 0, 0, 1, 0, 0, 8, -1, 0, 14, 0, 0, 0, 0, 2, 3, 0, 0,
			10, -1, 0, 16, 0, 0, 12, -1, 0, 5, 0, 0, 2, 0, 2, 18, 0, 0, 14, -1,
			0, 7, 0, 0, 2, 1, 0, 20, 0, 0, 1, 1, 0, 16, -1, 0, 4, 1, 0, 9, 0,
			0, 4, 0, 2,

			2, -2, 0, 4, -2, 0, 6, -2, 0, 22, 0, 0, 18, -1, 0, 6, 1, 0, 11, 0,
			0, 8, 1, 0, 4, 0, -2, 6, 0, 2, 3, 1, 0, 5, 1, 0, 13, 0, 0, 20, -1,
			0, 3, 2, 0, 4, -2, 2, 1, 2, 0, 22, -1, 0, 0, 0, 4, 6, 0, -2, 2, 1,
			-2, 0, 2, 0, 0, -1, 2, 2, 0, 4, 0, -2, 2, 2, 2, -2, 24, 0, 0, 4, 0,
			-4, 2, 2, 0, 1, -1, 0 };

	Double[] pericoeff = new Double[] { -1.6769, 0.4589, -0.1856, 0.0883,
			-0.0773, 0.0502, -0.0460, 0.0422, -0.0256, 0.0253, 0.0237, 0.0162,
			-0.0145, 0.0129, -0.0112, -0.0104, 0.0086, 0.0069, 0.0066, -0.0053,
			-0.0052, -0.0046, -0.0041, 0.0040, 0.0032, -0.0032, 0.0031,
			-0.0029, 0.0027, 0.0027,

			-0.0027, 0.0024, -0.0021, -0.0021, -0.0021, 0.0019, -0.0018,
			-0.0014, -0.0014, -0.0014, 0.0014, -0.0014, 0.0013, 0.0013, 0.0011,
			-0.0011, -0.0010, -0.0009, -0.0008, 0.0008, 0.0008, 0.0007, 0.0007,
			0.0007, -0.0006, -0.0006, 0.0006, 0.0005, 0.0005, -0.0004, 0.0 };

	int[] peritft = new int[] { 4, 5, 7, -1 };

	Double[] peritfc = new Double[] { 0.00019, -0.00013, -0.00011 };

	int[] apoarg = new int[] {
	/* D, M, F */
	2, 0, 0, 4, 0, 0, 0, 1, 0, 2, -1, 0, 0, 0, 2, 1, 0, 0, 6, 0, 0, 4, -1, 0,
			2, 0, 2, 1, 1, 0, 8, 0, 0, 6, -1, 0, 2, 0, -2, 2, -2, 0, 3, 0, 0,
			4, 0, 2,

			8, -1, 0, 4, -2, 0, 10, 0, 0, 3, 1, 0, 0, 2, 0, 2, 1, 0, 2, 2, 0,
			6, 0, 2, 6, -2, 0, 10, -1, 0, 5, 0, 0, 4, 0, -2, 0, 1, 2, 12, 0, 0,
			2, -1, 2, 1, -1, 0 };

	Double[] apocoeff = new Double[] { 0.4392, 0.0684, 0.0456, 0.0426, 0.0212,
			-0.0189, 0.0144, 0.0113, 0.0047, 0.0036, 0.0035, 0.0034, -0.0034,
			0.0022, -0.0017, 0.0013,

			0.0011, 0.0010, 0.0009, 0.0007, 0.0006, 0.0005, 0.0005, 0.0004,
			0.0004, 0.0004, -0.0004, -0.0004, 0.0003, 0.0003, 0.0003, -0.0003,
			0.0 };

	int[] apotft = new int[] { 2, 3, -1 };

	Double[] apotfc = new Double[] { -0.00011, -0.00011 };

	int[] periparg = new int[] {
	/* D, M, F */
	0, 0, 0, 2, 0, 0, 4, 0, 0, 2, -1, 0, 6, 0, 0, 1, 0, 0, 8, 0, 0, 0, 1, 0, 0,
			0, 2, 4, -1, 0, 2, 0, -2, 10, 0, 0, 6, -1, 0, 3, 0, 0, 2, 1, 0, 1,
			1, 0, 12, 0, 0, 8, -1, 0, 2, 0, 2, 2, -2, 0, 5, 0, 0, 14, 0, 0,

			10, -1, 0, 4, 1, 0, 12, -1, 0, 4, -2, 0, 7, 0, 0, 4, 0, 2, 16, 0,
			0, 3, 1, 0, 1, -1, 0, 6, 1, 0, 0, 2, 0, 14, -1, 0, 2, 2, 0, 6, -2,
			0, 2, -1, -2, 9, 0, 0, 18, 0, 0, 6, 0, 2, 0, -1, 2, 16, -1, 0, 4,
			0, -2, 8, 1, 0, 11, 0, 0, 5, 1, 0, 20, 0, 0 };

	Double[] peripcoeff = new Double[] { 3629.215, 63.224, -6.990, 2.834,
			1.927, -1.263, -0.702, 0.696, -0.690, -0.629, -0.392, 0.297, 0.260,
			0.201, -0.161, 0.157, -0.138, -0.127, 0.104, 0.104, -0.079, 0.068,

			0.067, 0.054, -0.038, -0.038, 0.037, -0.037, -0.035, -0.030, 0.029,
			-0.025, 0.023, 0.023, -0.023, 0.022, -0.021, -0.020, 0.019, 0.017,
			0.014, -0.014, 0.013, 0.012, 0.011, 0.010, -0.010,

			0.0 };

	int[] periptft = new int[] { 3, 7, 9, -1 };

	Double[] periptfc = new Double[] { -0.0071, -0.0017, 0.0016 };

	int[] apoparg = new int[] {
	/* D, M, F */
	0, 0, 0, 2, 0, 0, 1, 0, 0, 0, 0, 2, 0, 1, 0, 4, 0, 0, 2, -1, 0, 1, 1, 0, 4,
			-1, 0, 6, 0, 0, 2, 1, 0, 2, 0, 2, 2, 0, -2, 2, -2, 0, 2, 2, 0, 0,
			2, 0, 6, -1, 0, 8, 0, 0 };

	Double[] apopcoeff = new Double[] { 3245.251, -9.147, -0.841, 0.697,
			-0.656, 0.355, 0.159, 0.127, 0.065,

			0.052, 0.043, 0.031, -0.023, 0.022, 0.019, -0.016, 0.014, 0.010,

			0.0 };

	int[] apoptft = new int[] { 4, -1 };

	Double[] apoptfc = new Double[] { 0.0016, -1.0 };

	/**
	 * Calculate perigee or apogee from index number.
	 *
	 * @param k Double value
	 * @return Double[] value
	 */
	private Double[] moonPerigeeApogee(Double k) {
		Double t, t2, t3, t4, JDE, D, M, F, par;
		boolean apogee;
		Double EarthRad = 6378.14;

		t = k - Math.floor(k);
		if (t > 0.499 && t < 0.501) {
			apogee = true;
		} else if (t > 0.999 || t < 0.001) {
			apogee = false;
		} else {
			System.out.println("Abort");
			return null;
		}

		t = k / 1325.55;
		t4 = t * (t3 = t * (t2 = t * t));

		/* Mean time of perigee or apogee */
		JDE = 2451534.6698 + 27.55454989 * k - 0.0006691 * t2 - 0.000001098
				* t3 + 0.0000000052 * t4;

		/* Mean elongation of the Moon */
		D = 171.9179 + 335.9106046 * k - 0.0100383 * t2 - 0.00001156 * t3
				+ 0.000000055 * t4;

		/* Mean anomaly of the Sun */
		M = 347.3477 + 27.1577721 * k - 0.0008130 * t2 - 0.0000010 * t3;

		/* Moon's argument of latitude */
		F = 316.6109 + 364.5287911 * k - 0.0125053 * t2 - 0.0000148 * t3;

		JDE += sumSeries("sin", D, M, F, t, apogee ? apoarg : periarg,
				apogee ? apocoeff : pericoeff, apogee ? apotft : peritft,
				apogee ? apotfc : peritfc);
		par = sumSeries("cos", D, M, F, t, apogee ? apoparg : periparg,
				apogee ? apopcoeff : peripcoeff, apogee ? apoptft : periptft,
				apogee ? apoptfc : periptfc);

		par = degreesToRadians(par / 3600.0);
		return new Double[] { JDE, par, EarthRad / Math.sin(par) };
	}

	/**
	 * Julian Date Numer to Date
	 * @param jd Julian Date Number
	 */
	public Date eDate(Double jd) {

		jd += (30.0 / (24 * 60 * 60)); // Round to nearest minute
		date = julianYear(jd);
		time = julianTimeToHourMinuteSecond(jd);
		// System.out.println(date[0].intValue()-1900+"/"+(date[1].intValue()-1)+" /"+date[2].intValue()
		// + " "+ time[0].intValue() + "h"+ time[1].intValue() + "m ");
		return new Date(date[0] - 1900, date[1] - 1,
				date[2], time[0], time[1]);
	}
	
	/**
	 * Generate Perigee and Apogee Times
	 * @param calc Calculator
	 * @param gps GpsCoordinate	
	 * @param calendar Calendar 
	 * @return Double value
	 */

	public void calculationPerigeeApogee(Calendar calendar) {
		apogeeList = new ArrayList<Date>();
		perigeeList = new ArrayList<Date>();
		int l, m = 0, year = calendar.get(Calendar.YEAR); //
		ArrayList<Double[]> evt;
		Double[] dat, kr;
		Double sk;

		Position moonToday;
		Position moonTomorrow;

		sk = Math.floor((year - 1999.97) * 13.2555);
		dat = new Double[31];
		evt = new ArrayList<Double[]>();

		// Tabulate perigees and apogees for the year

		for (l = 0; true; l++) {
			
			kr = moonPerigeeApogee(sk);
			date = julianYear(kr[0]);

			if (date[0] == year) {
				
				dat[m] = sk;
				evt.add(kr);
				m++;
			}
			if (date[0] > year) {
				break;
			}
			sk += 0.5;
		}
		// Generate perigee and apogee table
		for (l = 0; l < m; l++) {

			sk = dat[l];
			kr = evt.get(l);
	
			
			if (l % 2 == 0)
				getApogeeList().add(eDate(kr[0]));
			else
				getPerigeeList().add(eDate(kr[0]));

		}
	}
}