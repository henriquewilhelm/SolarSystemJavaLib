package br.com.henriquewilhelm.orbit;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

/**
 * The only reason this class exists is so that this reference
 * algorithm can be translated slightly more easily to another 
 * language.
 * 
 * @author zoglmannk v1.0.0
 * @version v1.0.0
 */
public class Time implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1533636938589830203L;
	private final int hour;
	private final int min;
	
	public int getHour() {
		return hour;
	}
	public int getMin() {
		return min;
	}
	/**
	 * Construtor with Hour and minute
	 * @param hour value
	 * @param min value
	 */
	public Time(int hour, int min) {
		this.hour = hour;
		this.min = min;
	}
	/**
	 * Return String on format hh:mm 
	 * @return String value
	 */
	public String toString() {
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		
		writer.printf("%d:%02d", hour, min);
		
		writer.flush();
		return sw.getBuffer().toString();
	}
}
