package br.com.henriquewilhelm.orbit;
/**
 * This class joins the necessary variables to the calculations performed on each element of the solar system
 * @author Henrique Wilhelm v2.0.0
 * @version v2.0.0
 */
public class Coordinates {
	/**
	 * Semi-major axis [AU]
	 */
	double a;
	/** 
	 * Eccentricity of orbit
	 */
	double e; 
	/**
	 * Inclination of orbit [deg]
	 */
	double i; 
	/**
	 * Longitude of the ascending node [deg]
	 */
	double O; 
	/**
	 * Longitude of perihelion [deg]
	 */
	double w; 
	/** 
	 * Mean longitude [deg]
	 */
	double L; 

	public String toString() {
		// TODO Auto-generated method stub
		return a + " semi-major axis [AU]\n" + e
				+ " eccentricity of orbit\n" + i
				+ " inclination of orbit [deg]\n" + O
				+ " longitude of the ascending node [deg]\n" + w
				+ " longitude of perihelion [deg]\n" + L
				+ " mean longitude [deg]\n";
	}
}
