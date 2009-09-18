package us.jonesrychtar.socialnetwork;

public class Metric {
	public final static Metric EUCLIDEAN = new Metric("Euclidean");
	public final static Metric MAX = new Metric("Max");
	public final static Metric PERIODIC_EUCLIDEAN = new Metric("Periodic Euclidean");
	public final static Metric PERIODIC_MAX = new Metric("Periodic Max");
	String metricName;
	
	
	/**
	 * Creates a Metric object.
	 * 
	 * @param name the name of the function
	 */
	public Metric(String name) {
		metricName = name;
	}
	
	/**
	 * Copies an existing Metric object.
	 * 
	 * @param inMetric the Metric object to copy
	 */
	public Metric(Metric inMetric) {
		this.metricName = inMetric.metricName;
	}
	
	/**
	 * Determines whether another object is equal to this one.
	 * 
	 * @param obj the object to test equality with this one.
	 * @return true if the objects are the same; false otherwise.
	 */
	public boolean equals(Object obj) {
		return obj instanceof Metric && ((Metric)obj).metricName == this.metricName;
	}
	
	public String toString() {
		return metricName;
	}
}
