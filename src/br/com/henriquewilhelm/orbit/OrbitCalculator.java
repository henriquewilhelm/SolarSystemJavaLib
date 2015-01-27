package br.com.henriquewilhelm.orbit;
import java.util.ArrayList;
import java.util.Calendar;
import java.lang.Math;
/**
 * This class computes the right ascension, declination, longitudeEcliptic and distance of the sun and the 	planets.
 * The orbits of the major planets can be modeled as ellipses with the Sun at one focus. The effect of gravitational
 * interactions between the planets perturbs these orbits so that an ellipse is not a exact match with a true orbit. 
 * Six numbers, the mean orbital elements, specify an elliptical orbit. Mean orbital elements average the effects
 * of gravitational forces between planets. Calculation of a planets position based on these mean elements can be
 * inaccurate by a few minutes of arc.
 * 
 * The position of a planet (the word originally meant wandering star) varies with time. 
 * The daily motion changes the mean longitude by the average number of degrees the planet moves in one (mean solar) day.
 * The other elements change slowly with time. They are modeled using power series expansions of centuries from some fundamental
 * epoch. Here, we use the elements with their linear rates of change from the epoch J2000 (12:00 UT, Jan 1, 2000).
 * 
 * Planet positions are computed in the Equatorial coordinate system as right ascension (RA) and declination (DEC). 
 * These give the coordinates of the planet with respect to the fixed stars. The origin for RA is the vernal equinox.
 * Because the orientation of the Earth's axis is changing slowly with time, celestial coordinates must always be
 * referred to an epoch, or date. By using orbital elements referred to epoch J2000, the orbits of the planets are
 * described in a coordinate system that is based on the position the vernal equinox will have at J2000. 
 * The effect of nutation (the Earth's axis is nodding) is ignored since positions are relative to the mean 
 * ecliptic of J2000. The aberration effect caused by the finite speed of light is also ignored.
 * 
 * Reference:
 * 
 * @see <a href="http://www.stellarium.org">Stellarium - Stellarium is a free open source planetarium for your computer</a>
 * @see <a href="http://www.abecedarical.com/javascript/script_planet_orbits.html">ABCDEDARICAL - Abecedarical Systems 
 * (Free Mathematics Tutorials and Software)</a>
 * 
 * @author Henrique Wilhelm
 * @version 2.0.0
 */
public class OrbitCalculator {
	/**
	 *  Planet List
	 */
	private ArrayList<Event> planetList;
														
	private double DEGS = (180 / Math.PI); // convert radians to degrees
	private double RADS = (Math.PI / 180); // convert degrees to radians
	private double EPS = 1.0E-12; // machine error constant

	/**
	 * Current time
	 */
	private Calendar calendar;
	/**
	 * Name of elements os system solar 
	 * <p>"Mercury", "Venus  ", "Earth", "Mars",
	 * "Jupiter", "Saturn ", "Uranus ", "Neptune", "Pluto"</p>
	 */
	private final String[] name = new String[] { "Mercury", "Venus  ", "Earth", "Mars",
									"Jupiter", "Saturn ", "Uranus ", "Neptune", "Pluto" };

	/**
	 * Total of day in one CENTURIE
	 */
	private final int CENTURIES = 36525;
	/**
	 * Construtor Orbit Calculator
	 */
	public OrbitCalculator() {
		this.planetList = new ArrayList<Event>();
		this.calendar = Calendar.getInstance();
	}

	/**
	 * This method computes the position of elements returns (List Of Elements of Solar System)
	 * @return computeElementsPosition ArrayList
	 */
	public ArrayList<Event> computeElementsPosition() {

		Event obj;
		Event objNext;
		double dt;
		// compute day number for date/time (days since J2000)
		dt = daySinceJ2000(calendar);
		System.out.println("Julian Date OF J2000 "+dt);
		// compute location of objects
		for (int i = 0; i < 9; i++) {
			obj = get_coord(i, dt);
			objNext = get_coord(i, dt+1);//+1 == Tomorrow
			obj.positionTomorrow = objNext.position;
			planetList.add(obj);
		}
		return planetList;
	}

	/**
	 * Returns the Julian Day as 12h on 1 January 2000
	 * @param calendar calendar Instance
	 * @return double value Julian Date since year 2000
	 */
	private double daySinceJ2000(Calendar calendar) {
		
		this.calendar = calendar;
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
//		 System.out.println("UTC: " + month + "/" + day + "/" + year + "  " + hour + ":" + minute + ":" + second);

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

	/**
	 *  compute RA, DEC, Longitude Ecliptical and distance of element for day number-dt
	 *  result returned in structure obj in degrees and astronomical units
	 *  @param element (0-9 int value) of element of solar system 
	 *  @param dateTime value of current dateTime since 2000
	 *  @return Event Event of Elements
	 */
	 
	private Event get_coord(int element, double dateTime) {
		Event obj = new Event();
		Coordinates coordObj = new Coordinates();
		obj.name = name[element];
		mean_elements(coordObj, element, dateTime);
		double ap = coordObj.a;
		double ep = coordObj.e;
		double ip = coordObj.i;
		double op = coordObj.O;
		double pp = coordObj.w;
		double lp = coordObj.L;

		Coordinates coordEarth = new Coordinates();
		mean_elements(coordEarth, 2, dateTime);
		double ae = coordEarth.a;
		double ee = coordEarth.e;
		//double ie = coordEarth.i;
		//double oe = coordEarth.O;
		double pe = coordEarth.w;
		double le = coordEarth.L;

		 // position of Earth in its orbit
	    double me = mod2pi(le - pe);
	    double ve = true_anomaly(me, ee);
	    double re = ae*(1 - ee*ee)/(1 + ee*Math.cos(ve));
	    
	    // heliocentric rectangular coordinates of Earth
	    double xe = re*Math.cos(ve + pe);
	    double ye = re*Math.sin(ve + pe);
	    double ze = 0.0;
	    
	    // position of planet in its orbit
	    double mp = mod2pi(lp - pp);
	    double vp = true_anomaly(mp, coordObj.e);
	    double rp = ap*(1 - ep*ep)/(1 + ep*Math.cos(vp));
	    
	    // heliocentric rectangular coordinates of planet
	    double xh = rp*(Math.cos(op)*Math.cos(vp + pp - op) - Math.sin(op)*Math.sin(vp + pp - op)*Math.cos(ip));
	    double yh = rp*(Math.sin(op)*Math.cos(vp + pp - op) + Math.cos(op)*Math.sin(vp + pp - op)*Math.cos(ip));
	    double zh = rp*(Math.sin(vp + pp - op)*Math.sin(ip));

	    if (element == 2)  // earth --> compute sun
	    {
	        xh = 0;
	        yh = 0;
	        zh = 0;
	    }
	    
	    // convert to geocentric rectangular coordinates
	    double xg = xh - xe;
	    double yg = yh - ye;
	    double zg = zh - ze;
	    
	    // rotate around x axis from ecliptic to equatorial coords
	    double ecl = 23.439281*RADS;            //value for J2000.0 frame
	    double xeq = xg;
	    double yeq = yg*Math.cos(ecl) - zg*Math.sin(ecl);
	    double zeq = yg*Math.sin(ecl) + zg*Math.cos(ecl);
	    
	    // find the RA and DEC from the rectangular equatorial coords
	    double rightAscention = mod2pi(Math.atan2(yeq, xeq)); 
	    double declination  = Math.atan(zeq/Math.sqrt(xeq*xeq + yeq*yeq));
	    double distance = Math.sqrt(xeq*xeq + yeq*yeq + zeq*zeq);
	    double longitudeEcliptic =rightAscention*DEGS;
	    
	    obj.position = new Position(rightAscention, declination, longitudeEcliptic, distance);
		return obj;
	}

	/**
	 * Compute the orbit position for the elements of Solar System at dateTime
	 * result is returned Coordinate coords
	 * @param coords Coord class - major axis [AU], eccentricity of orbit, inclination of orbit [deg],
	 *  longitude of the ascending node [deg], longitude of perihelion [deg], mean longitude [deg]
	 * @param element value of Element of Solar System (0-8)
	 * @param dateTime value of current dateTime since 2000
	 */
	private void mean_elements(Coordinates coords, int element, double dateTime) {
		double cy = dateTime / CENTURIES; // centuries since J2000

		 switch (element)
		    {
		    case 0: // Mercury
		        coords.a = 0.38709893 + 0.00000066*cy;
		        coords.e = 0.20563069 + 0.00002527*cy;
		        coords.i = ( 7.00487  -  23.51*cy/3600)*RADS;
		        coords.O = (48.33167  - 446.30*cy/3600)*RADS;
		        coords.w = (77.45645  + 573.57*cy/3600)*RADS;
		        coords.L = mod2pi((252.25084 + 538101628.29*cy/3600)*RADS);
		        break;
		    case 1: // Venus
		        coords.a = 0.72333199 + 0.00000092*cy;
		        coords.e = 0.00677323 - 0.00004938*cy;
		        coords.i = (  3.39471 -   2.86*cy/3600)*RADS;
		        coords.O = ( 76.68069 - 996.89*cy/3600)*RADS;
		        coords.w = (131.53298 - 108.80*cy/3600)*RADS;
		        coords.L = mod2pi((181.97973 + 210664136.06*cy/3600)*RADS);
		        break;
		    case 2: // Earth/Sun
		        coords.a = 1.00000011 - 0.00000005*cy;
		        coords.e = 0.01671022 - 0.00003804*cy;
		        coords.i = (  0.00005 -    46.94*cy/3600)*RADS;
		        coords.O = (-11.26064 - 18228.25*cy/3600)*RADS;
		        coords.w = (102.94719 +  1198.28*cy/3600)*RADS;
		        coords.L = mod2pi((100.46435 + 129597740.63*cy/3600)*RADS);
		        break;
		    case 3: // Mars
		        coords.a = 1.52366231 - 0.00007221*cy;
		        coords.e = 0.09341233 + 0.00011902*cy;
		        coords.i = (  1.85061 -   25.47*cy/3600)*RADS;
		        coords.O = ( 49.57854 - 1020.19*cy/3600)*RADS;
		        coords.w = (336.04084 + 1560.78*cy/3600)*RADS;
		        coords.L = mod2pi((355.45332 + 68905103.78*cy/3600)*RADS);
		        break;
		    case 4: // Jupiter
		        coords.a = 5.20336301 + 0.00060737*cy;
		        coords.e = 0.04839266 - 0.00012880*cy;
		        coords.i = (  1.30530 -    4.15*cy/3600)*RADS;
		        coords.O = (100.55615 + 1217.17*cy/3600)*RADS;
		        coords.w = ( 14.75385 +  839.93*cy/3600)*RADS;
		        coords.L = mod2pi((34.40438 + 10925078.35*cy/3600)*RADS);
		        break;
		    case 5: // Saturn
		        coords.a = 9.53707032 - 0.00301530*cy;
		        coords.e = 0.05415060 - 0.00036762*cy;
		        coords.i = (  2.48446 +    6.11*cy/3600)*RADS;
		        coords.O = (113.71504 - 1591.05*cy/3600)*RADS;
		        coords.w = ( 92.43194 - 1948.89*cy/3600)*RADS;
		        coords.L = mod2pi((49.94432 + 4401052.95*cy/3600)*RADS);
		        break;
		    case 6: // Uranus
		        coords.a = 19.19126393 + 0.00152025*cy;
		        coords.e =  0.04716771 - 0.00019150*cy;
		        coords.i = (  0.76986  -    2.09*cy/3600)*RADS;
		        coords.O = ( 74.22988  - 1681.40*cy/3600)*RADS;
		        coords.w = (170.96424  + 1312.56*cy/3600)*RADS;
		        coords.L = mod2pi((313.23218 + 1542547.79*cy/3600)*RADS);
		        break;
		    case 7: // Neptune
		        coords.a = 30.06896348 - 0.00125196*cy;
		        coords.e =  0.00858587 + 0.00002510*cy;
		        coords.i = (  1.76917  -   3.64*cy/3600)*RADS;
		        coords.O = (131.72169  - 151.25*cy/3600)*RADS;
		        coords.w = ( 44.97135  - 844.43*cy/3600)*RADS;
		        coords.L = mod2pi((304.88003 + 786449.21*cy/3600)*RADS);
		        break;
		    case 8: // Pluto
		        coords.a = 39.48168677 - 0.00076912*cy;
		        coords.e =  0.24880766 + 0.00006465*cy;
		        coords.i = ( 17.14175  +  11.07*cy/3600)*RADS;
		        coords.O = (110.30347  -  37.33*cy/3600)*RADS;
		        coords.w = (224.06676  - 132.25*cy/3600)*RADS;
		        coords.L = mod2pi((238.92881 + 522747.90*cy/3600)*RADS);
		        break;
		    default:
			System.out.print("function mean_elements() failed!");
		}
	}

	/** 
	 * Compute the true anomaly from mean anomaly using iteration
	 * @param M - mean anomaly in radians
	 * @param e - orbit eccentricity
	 * @return double true anomaly
	 */
	private double true_anomaly(double M, double e) {
		double V, E1;

		// initial approximation of eccentric anomaly
		double E = M + e * Math.sin(M) * (1.0 + e * Math.cos(M));

		do { // iterate to improve accuracy
			E1 = E;
			E = E1 - (E1 - e * Math.sin(E1) - M) / (1 - e * Math.cos(E1));
		} while (Math.abs(E - E1) > EPS);

		// convert eccentric anomaly to true anomaly
		V = 2 * Math.atan(Math.sqrt((1 + e) / (1 - e)) * Math.tan(0.5 * E));

		if (V < 0)
			V = V + (2 * Math.PI); // modulo 2pi

		return V;
	}

	/**
	 * Return the integer part of a number
	 * @param x double value 
	 * @return double rounded number 
	 */
	private double abs_floor(double x) {
		double r;
		if (x >= 0.0)
			r = Math.floor(x);
		else
			r = Math.ceil(x);
		return r;
	}

	/**
	 * Return an angle in the range 0 to 2pi radians
	 * @param x double value
	 * @return range 0 to 2pi radians
	 */
	private double mod2pi(double x) {
		double b = x / (2 * Math.PI);
		double a = (2 * Math.PI) * (b - abs_floor(b));
		if (a < 0)
			a = (2 * Math.PI) + a;
		return a;
	}
	
	public ArrayList<Event> getPlanetList() {
		return planetList;
	}

	public void setPlanetList(ArrayList<Event> planetList) {
		this.planetList = planetList;
	}

	
}
