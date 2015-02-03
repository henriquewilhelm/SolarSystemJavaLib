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
	 * double number DEGREES TO RADIANS
	 */
	private static final double DEG2RAD = Math.PI / 180;
	/**
	 * Current Calendar
	 */
	Calendar calendar;
	/**
	 * Apogee List
	 */
	private ArrayList<MoonEvent> apogeeList;
	/**
	 * Perigee List
	 */
	private ArrayList<MoonEvent> perigeeList;

	public ArrayList<MoonEvent> getApogeuList() {
		return apogeeList;
	}

	public void setApogeuList(ArrayList<MoonEvent> apogeuList) {
		this.apogeeList = apogeuList;
	}

	public ArrayList<MoonEvent> getPerigeuList() {
		return perigeeList;
	}

	public void setPerigeuList(ArrayList<MoonEvent> perigeuList) {
		this.perigeeList = perigeuList;
	}

	private void addApogeu(MoonEvent lua) {
		getApogeuList().add(lua);
	}

	private void addPerieu(MoonEvent lua) {
		getPerigeuList().add(lua);
	}
	/**
	 * 
	 * @param calendar current calendar date
	 * @return String value "Apogee" or "Perigee"
	 */
	public String isApogeuOrPerigeu(Calendar calendar) {
		// System.out.println("APOGEU "+calendar.getTime());

		for (int index = 0; index < getApogeuList().size(); index++) {
			if ((getApogeuList().get(index).date.getYear() + 1900) == calendar
					.get(Calendar.YEAR)
					&& getApogeuList().get(index).date.getMonth() == calendar
							.get(Calendar.MONTH)
					&& getApogeuList().get(index).date.getDate() == calendar
							.get(Calendar.DAY_OF_MONTH)) {

				return "Apogee";
			}
			// System.out.println(getApogeuList().get(index).getData());
		}

		// System.out.println("PERIGEU "+calendar.getTime());
		for (int index = 0; index < getPerigeuList().size(); index++) {
			if ((getPerigeuList().get(index).date.getYear() + 1900) == calendar
					.get(Calendar.YEAR)
					&& getPerigeuList().get(index).date.getMonth() == calendar
							.get(Calendar.MONTH)
					&& getPerigeuList().get(index).date.getDate() == calendar
							.get(Calendar.DAY_OF_MONTH)) {
				return "Perigee";
			}
			// System.out.println(getPerigeuList().get(index).getData());
		}
		return "";
	}

	public PerigeeApogeeCalculator(Calculator cal, GpsCoordinate gps, Calendar calendar) {
		calculationPerigeeApogee(cal, gps, calendar);
	}

	/**
	 * Convert Julian Date Number to Date (year, month, day), which are returned as
	 * an Array.
	 * 
	 * @param td Julian Date Number to Date
	 * @return Double[] Array Double {YEAR, MONTH, DAY}
	 */
	public Double[] julianYear(Double td) {
		Double z, f, a, alpha, b, c, d, e, mm;

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

		return new Double[] { Math.floor((mm > 2) ? (c - 4716) : (c - 4715)),
				mm, Math.floor(b - d - Math.floor(30.6001 * e) + f) };
	}

	/**
	 * Convert Julian time to hour, minutes, and seconds, returned as a
	 * three-element array.
	 * 
	 * @param jd Julian Date Number 
	 * @return Double[] Array Double {HOUR, MINUTE, SECOND}
	 */
	public Double[] julianTimeToHourMinuteSecond(Double jd) {
		Double ij;

		jd += 0.5; /* Astronomical to civil */
		ij = (jd - Math.floor(jd)) * 86400.0;
		return new Double[] { Math.floor(ij / 3600),
				Math.floor((ij / 60) % 60), Math.floor(ij % 60) };
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
	 * String to a given length with a given fill character.
	 */
	public final static String[] Months = new String[] { "Jan", "Feb", "Mar",
			"Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	Double[] date = new Double[3];
	Double[] time = new Double[3];

	MoonEvent lua;
	/**
	 * Julian Date Numer to Date
	 * @param j Julian Date Number
	 */
	public void eDate(Double j) {

		j += (30.0 / (24 * 60 * 60)); // Round to nearest minute
		date = julianYear(j);
		time = julianTimeToHourMinuteSecond(j);
		// System.out.println(date[0].intValue()-1900+"/"+(date[1].intValue()-1)+" /"+date[2].intValue()
		// + " "+ time[0].intValue() + "h"+ time[1].intValue() + "m ");
		lua.date = new Date(date[0].intValue() - 1900, date[1].intValue() - 1,
				date[2].intValue(), time[0].intValue(), time[1].intValue());
	}

	
	private Double degreesSin(Double x) {
		return Math.sin(degreesToRadians(x));
	}

	private Double degreesCos(Double x) {
		return Math.cos(degreesToRadians(x));
	}
	/**
	 * Given a K value used to determine the mean phase of the new
	 * moon, and a phase selector (0.0, 0.25, 0.5, 0.75), obtain the true,
	 * corrected phase time.
	 * @param k value Julian Date Number
	 * @param phase Double value
	 * @return Double truePhase value
	 */
	public Double truePhase(Double k, Double phase) {
		Double t, t2, t3, pt, m, mprime, f, SynMonth = 29.53058868; /*
																	 * Synodic
																	 * month
																	 * (mean
																	 * time from
																	 * new to
																	 * next new
																	 * Moon)
																	 */

		k += phase; /* Add phase to new moon time */
		t = k / 1236.85; /*
						 * Time in Julian centuries from 1900 January 0.5
						 */
		t2 = t * t; /* Square for frequent use */
		t3 = t2 * t; /* Cube for frequent use */
		pt = 2415020.75933 /* Mean time of phase */
				+ SynMonth * k + 0.0001178 * t2 - 0.000000155 * t3 + 0.00033
				* degreesSin(166.56 + 132.87 * t - 0.009173 * t2);

		m = 359.2242 /* Sun's mean anomaly */
				+ 29.10535608 * k - 0.0000333 * t2 - 0.00000347 * t3;
		mprime = 306.0253 /* Moon's mean anomaly */
				+ 385.81691806 * k + 0.0107306 * t2 + 0.00001236 * t3;
		f = 21.2964 /* Moon's argument of latitude */
				+ 390.67050646 * k - 0.0016528 * t2 - 0.00000239 * t3;
		if ((phase < 0.01) || (Math.abs(phase - 0.5) < 0.01)) {

			/* Corrections for New and Full Moon */

			pt += (0.1734 - 0.000393 * t) * degreesSin(m) + 0.0021
					* degreesSin(2 * m) - 0.4068 * degreesSin(mprime) + 0.0161
					* degreesSin(2 * mprime) - 0.0004 * degreesSin(3 * mprime)
					+ 0.0104 * degreesSin(2 * f) - 0.0051
					* degreesSin(m + mprime) - 0.0074 * degreesSin(m - mprime)
					+ 0.0004 * degreesSin(2 * f + m) - 0.0004
					* degreesSin(2 * f - m) - 0.0006
					* degreesSin(2 * f + mprime) + 0.0010
					* degreesSin(2 * f - mprime) + 0.0005
					* degreesSin(m + 2 * mprime);
		} else if ((Math.abs(phase - 0.25) < 0.01 || (Math.abs(phase - 0.75) < 0.01))) {
			pt += (0.1721 - 0.0004 * t) * degreesSin(m) + 0.0021
					* degreesSin(2 * m) - 0.6280 * degreesSin(mprime) + 0.0089
					* degreesSin(2 * mprime) - 0.0004 * degreesSin(3 * mprime)
					+ 0.0079 * degreesSin(2 * f) - 0.0119
					* degreesSin(m + mprime) - 0.0047 * degreesSin(m - mprime)
					+ 0.0003 * degreesSin(2 * f + m) - 0.0004
					* degreesSin(2 * f - m) - 0.0006
					* degreesSin(2 * f + mprime) + 0.0021
					* degreesSin(2 * f - mprime) + 0.0003
					* degreesSin(m + 2 * mprime) + 0.0004
					* degreesSin(m - 2 * mprime) - 0.0003
					* degreesSin(2 * m + mprime);
			if (phase < 0.5)
				/* First quarter correction */
				pt += 0.0028 - 0.0004 * degreesCos(m) + 0.0003
						* degreesCos(mprime);
			else
				/* Last quarter correction */
				pt += -0.0028 + 0.0004 * degreesCos(m) - 0.0003
						* degreesCos(mprime);
		}
		return pt;
	}
	/**
	 * Estimated Phase
	 * 
	 * @param jd Julian Date Number
	 * @param phaset Array List
	 * @return String value
	 */
	private String nearPhase(Double jd, ArrayList<Double> phaset) {
		int i, closest = 0;
		Double dt = Double.MAX_VALUE;
		String rs = "";

		for (i = 0; i < phaset.size(); i++) {
			if (Math.abs(jd - Math.abs(phaset.get(i))) < dt) {
				dt = Math.abs(jd - Math.abs(phaset.get(i)));
				closest = i;
			}
		}
		rs = (phaset.get(closest) < 0) ? "N" : "F";
		rs += (jd > Math.abs(phaset.get(closest))) ? "+" : "-";
		lua.phase = (rs);
		if (dt >= 1) {
			// lua.setDiaProxLua(dt.intValue());
			rs += Math.floor(dt) + "d";
			dt -= Math.floor(dt);
		} else {
			rs += "  ";
		}
		dt = Math.floor((dt * 86400) / 3600);
		if (dt < 10) {
			rs += " ";
		}
		// lua.setHoraProxLua(dt.intValue());
		rs += dt + "h";
		return rs;
	}

	/**
	 * Generate Perigee and Apogee Times
	 * @param calc Calculator
	 * @param gps GpsCoordinate	
	 * @param calendar Calendar 
	 * @return Double value
	 */

	public Double calculationPerigeeApogee(Calculator calc, GpsCoordinate gps,
			Calendar calendar) {
		apogeeList = new ArrayList<MoonEvent>();
		perigeeList = new ArrayList<MoonEvent>();
		double utcToLocal = 0d;
		double timeZoneShift;
		double daysFromEpoc = 0d;
		double LST = 0d;
		int l, m = 0, minx, year = calendar.get(Calendar.YEAR); //
		ArrayList<Double[]> evt;
		Double[] dat, kr;
		ArrayList<Double> phaset;
		Double pmin = Double.MAX_VALUE;
		Double pmax = Double.MIN_VALUE;
		Double sk, k1, mtime;

		Position moonToday = calc.calculateMoonPosition(daysFromEpoc);
		Position moonTomorrow = calc.calculateMoonPosition(daysFromEpoc + 1);

		sk = Math.floor((year - 1999.97) * 13.2555);
		dat = new Double[31];
		evt = new ArrayList<Double[]>();
		phaset = new ArrayList<Double>();

		// Tabulate perigees and apogees for the year

		for (l = 0; true; l++) {
			kr = moonPerigeeApogee(sk);
			date = julianYear(kr[0]);

			if (date[0] == year) {
				if (kr[2] < pmin) {
					pmin = kr[2];
				} else if (kr[2] > pmax) {
					pmax = kr[2];
				}
				dat[m] = sk;
				evt.add(kr);
				m++;
			}
			if (date[0] > year) {
				break;
			}
			sk += 0.5;
		}

		// Tabulate new and full moons surrounding the year

		k1 = Math.floor((year - 1900) * 12.3685);
		minx = 0;
		for (l = 0; true; l++) {
			mtime = truePhase(k1, (l & 1) * 0.5);

			date = julianYear(mtime);
			if (date[0] >= year) {
				minx++;
			}
			phaset.add(mtime * ((l & 1) == 0 ? -1 : 1));
			if (date[0] > year) {
				break;
			}
			k1 += l & 1;
		}
		// Generate perigee and apogee table
		for (l = 0; l < m; l++) {

			sk = dat[l];
			kr = evt.get(l);
			utcToLocal = calc.calculateUtcToDSTLocal(calendar);
			timeZoneShift = -1 * utcToLocal / calc.HOURS_IN_DAY;
			daysFromEpoc = ((kr[0].intValue() - 1) - calc.NEW_STANDARD_EPOC) + 0.5;
			daysFromEpoc = daysFromEpoc + timeZoneShift;
			LST = calc.calculateLST(daysFromEpoc, timeZoneShift, gps.longitude);
			// calculate today moon

			moonToday = calc.calculateMoonPosition(daysFromEpoc);
			moonTomorrow = calc.calculateMoonPosition(daysFromEpoc + 1);

			moonTomorrow = calc.ensureSecondAscentionGreater(moonToday,
					moonTomorrow);
			lua = new MoonEvent(calc.calculate(calc.MOONRISE_MOONSET_OFFSET,
					gps, LST, moonToday, moonTomorrow));

			lua.ageInDays = calc.calculateMoonsAge(kr[0]);
			lua.illuminationPercent = calc
					.calculateMoonIlluminationPercent(lua.ageInDays);
			lua.position = moonToday;
			lua.zodiac = calc.zodiac(lua.position.longitudeEcliptic);
			nearPhase(kr[0], phaset);

			eDate(kr[0]);
			// System.out.println(moonToday.date +" " +daysFromEpoc +
			// " "+Math.floor(kr[0]-1));
			lua.position.distance = kr[2];
			if (l % 2 == 0)
				addApogeu(lua);
			else
				addPerieu(lua);

		}
		// Generate Moon phase table
		for (l = 0; l < minx; l++) {
			Double mp = phaset.get(l);
			if (mp < 0) {
				mp = -mp;
			}
			eDate(mp);
		}
		return mtime;
	}

	/**
	 * Convert degrees to a valid angle:
	 * @param deg Degrees value
	 * @return double value
	 */
	double angle(double deg) {
		while (deg >= 360.)
			deg -= 360.;
		while (deg < 0.)
			deg += 360.;
		return deg * DEG2RAD;
	}

	 /**
	  * Phase Angle
	  * @param date Date
	  * @return double value, the phase angle for the given date, in RADIANS.
	  */
	 
	public double getPhaseAngle(Date date) {
		if (date == null) {
			date = new Date();
		}

		// Time measured in Julian centuries from epoch J2000.0:
		Date Tepoch = new Date(2000, 0, 1);
		Tepoch.setTime(getTime(Tepoch.getTime()));
		double T = (decimalYears(date) - decimalYears(Tepoch)) / 100.;
		double T2 = T * T;
		double T3 = T2 * T;
		double T4 = T3 * T;

		// Mean elongation of the moon:
		double D = angle(297.8502042 + 445267.1115168 * T - 0.0016300 * T2 + T3
				/ 545868 + T4 / 113065000);
		// Sun's mean anomaly:
		double M = angle(357.5291092 + 35999.0502909 * T - 0.0001536 * T2 + T3
				/ 24490000);
		// Moon's mean anomaly:
		double Mprime = angle(134.9634114 + 477198.8676313 * T + 0.0089970 * T2
				- T3 / 3536000 + T4 / 14712000);

		return (angle(180 - (D / DEG2RAD) - 6.289 * Math.sin(Mprime) + 2.100
				* Math.sin(M) - 1.274 * Math.sin(2 * D - Mprime) - 0.658
				* Math.sin(2 * D) - 0.214 * Math.sin(2 * Mprime) - 0.110
				* Math.sin(D)));
	}
	/**
	 * Date to Decimal Years
	 * @param date Date
	 * @return double decimal Years
	 */
	public double decimalYears(Date date) {
		// getTime() returns milliseconds Math.since Jan 1, 1970, so:
		return date.getTime() / 365.242191 / (24 * 60 * 60 * 1000);
	}
	/**
	 * Update long time
	 * @param time Long value
	 * @return time Long value
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
}