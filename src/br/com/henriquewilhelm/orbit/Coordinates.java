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
	private double a;
	/** 
	 * Eccentricity of orbit
	 */
	private double e; 
	/**
	 * Inclination of orbit [deg]
	 */
	private double i; 
	/**
	 * Longitude of the ascending node [deg]
	 */
	private double O; 
	/**
	 * Longitude of perihelion [deg]
	 */
	private double w; 
	/** 
	 * Mean longitude [deg]
	 */
	private double L; 
	
	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getE() {
		return e;
	}

	public void setE(double e) {
		this.e = e;
	}

	public double getI() {
		return i;
	}

	public void setI(double i) {
		this.i = i;
	}

	public double getO() {
		return O;
	}

	public void setO(double o) {
		O = o;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	public double getL() {
		return L;
	}

	public void setL(double l) {
		L = l;
	}

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
