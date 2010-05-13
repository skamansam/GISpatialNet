/*
 * Robert Gove
 * May 2009
 * 
 * For research by Eric Jones and Jan Rychtar.
 * 
 * Requires:
 * ujmp http://www.ujmp.org/ Tested with version 0.2.2
 */

package us.jonesrychtar.socialnetwork.SpatialGraph;

import us.jonesrychtar.gispatialnet.DataSet;
import us.jonesrychtar.socialnetwork.*;
import java.io.PrintStream;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;

public class SpatialGraphBase {
	protected Matrix X; //stores x coordinates of all vertices
	protected Matrix Y; //stores y coordinates of all vertices
	protected Matrix A; //stores adjacency matrix of the network
	protected long numVerts;
	protected Metric metricFunction;
	protected double bias;
	protected double nbhdSizeLowerEstimate;
	protected double nbhdSizeUpperEstimate;
	protected double radius;
	
	/**
	 * Constructs blank spatial graph object.
	 */
	public SpatialGraphBase() {
		X = MatrixFactory.emptyMatrix();
		Y = MatrixFactory.emptyMatrix();
		A = MatrixFactory.emptyMatrix();
		numVerts = 0;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param inGraph the graph to copy. 
	 */
	public SpatialGraphBase(SpatialGraphBase inGraph) {
		X = MatrixFactory.copyFromMatrix(inGraph.getX());
		Y = MatrixFactory.copyFromMatrix(inGraph.getY());
		A = MatrixFactory.copyFromMatrix(inGraph.getA());
		numVerts = A.getRowCount();
		metricFunction = new Metric(inGraph.getMetricFunction());
		bias = inGraph.getBias();
		nbhdSizeLowerEstimate = inGraph.getNbhdSizeLowerEstimate();
		nbhdSizeUpperEstimate = inGraph.getNbhdSizeUpperEstimate();
	}
	
	/**
	 * Create a SpatialGraphBase object from existing data.
	 * 
	 * @param inX the x-coordinates for each vertex.
	 * @param inY the y-coordinates for each vertex.
	 * @param inA the adjacency matrix of the graph.
	 */
	public SpatialGraphBase(Matrix inX, Matrix inY, Matrix inA) {
		X = MatrixFactory.copyFromMatrix(inX);
		Y = MatrixFactory.copyFromMatrix(inY);
		A = MatrixFactory.copyFromMatrix(inA);
		numVerts = A.getRowCount();
		metricFunction = Metric.EUCLIDEAN; //default to Euclidean
	}
	
	public SpatialGraphBase(DataSet ds) {
		X = MatrixFactory.copyFromMatrix(ds.getX());
		Y = MatrixFactory.copyFromMatrix(ds.getY());
		A = MatrixFactory.copyFromMatrix(ds.getAdj());
		numVerts = A.getRowCount();
		metricFunction = Metric.EUCLIDEAN; //default to Euclidean
	}
	/**
	 * Gets the matrix of x-coordinates.
	 * 
	 * @return the matrix of x-coordinates
	 */
	public Matrix getX() {
		return X;
	}
	
	/**
	 * Gets the matrix of y-coordinates
	 * 
	 * @return the matrix of y-coordinates.
	 */
	public Matrix getY() {
		return Y;
	}
	
	/**
	 * Gets the adjacency matrix.
	 * 
	 * @return the adjacency matrix.
	 */
	public Matrix getA() {
		return A;
	}
	
	/**
	 * Gets the number of vertices in the graph.
	 * 
	 * @return the number of vertices in the graph.
	 */
	public long getNumVerts() {
		return numVerts;
	}
	
	/**
	 * Gets the metric function currently set for the graph.
	 * 
	 * @return the metric function for the graph.
	 */
	public Metric getMetricFunction() {
		return metricFunction;
	}
	
	/**
	 * Gets the calculated "bias" of the graph.
	 * 
	 * @return the bias of the graph.
	 */
	public double getBias() {
		return bias;
	}
	
	/**
	 * Gets the radius (1/2 maximal distance between any two vertices).
	 * Should be 1 for non-periodic metrics.
	 * 
	 * @return the radius of the graph.
	 */
	public double getRadius() {
		return radius;
	}
	
	/**
	 * Gets the lower estimate for the neighborhood size of the graph.
	 * 
	 * @return the lower estimate for the neighborhood size.
	 */
	public double getNbhdSizeLowerEstimate() {
		return nbhdSizeLowerEstimate;
	}
	
	/**
	 * Gets the upper estimate for the neighborhood size of the graph.
	 * 
	 * @return the upper estimate for the neighborhood size.
	 */
	public double getNbhdSizeUpperEstimate() {
		return nbhdSizeUpperEstimate;
	}
	
	/**
	 * Set the number of vertices in the graph.
	 * 
	 * @param numVerts the number of vertices in the graph.
	 */
	public void setNumVerts(long numVerts) {
		this.numVerts = numVerts;
	}
	
	/**
	 * Set the metric function used by the graph.
	 * 
	 * @param metricFunction set the metric function used in the graph.
	 */
	public void setMetricFunction(Metric metricFunction) {
		this.metricFunction = metricFunction;
	}
	
	/**
	 * Set the "bias" of the graph.
	 * 
	 * @param bias the bias of the graph.
	 */
	public void setBias(double bias) {
		this.bias = bias;
	}
	
	/**
	 * Set the lower estimate for the neighborhood size of the graph.
	 * 
	 * @param nbhdSizeLowerEstimate the lower estimate for the neighborhood size.
	 */
	public void setNbhdSizeLowerEstimate(double nbhdSizeLowerEstimate) {
		this.nbhdSizeLowerEstimate = nbhdSizeLowerEstimate;
	}
	
	/**
	 * Set the upper estimate for the neighborhood size of the graph.
	 * 
	 * @param nbhdSizeLowerEstimate the upper estimate for the neighborhood size.
	 */
	public void setNbhdSizeUpperEstimate(double nbhdSizeUpperEstimate) {
		this.nbhdSizeUpperEstimate = nbhdSizeUpperEstimate;
	}
	
	/**
	 * Computes the distance between point i and point j using metric function m.
	 * 
	 * @param i the first point.
	 * @param j the second point.
	 * @param m the metric function.
	 * @return the distance between the points.
	 */
	public double computeDistance(int i, int j, Metric m) {
		if(m.equals(Metric.MAX)) {
			return Math.max(Math.abs(X.getAsDouble(i, 0) - X.getAsDouble(j, 0)),
					Math.abs(Y.getAsDouble(i, 0) - Y.getAsDouble(j, 0)));
			
		} else if(m.equals(Metric.PERIODIC_EUCLIDEAN)) {
			double I1 = Math.min(Math.abs(X.getAsDouble(i, 0) - X.getAsDouble(j, 0)),
					Math.min(Math.abs(X.getAsDouble(i, 0) - X.getAsDouble(j, 0) + 2*radius),
							Math.abs(X.getAsDouble(i, 0) - X.getAsDouble(j, 0) - 2*radius)));
			double I2 = Math.min(Math.abs(Y.getAsDouble(i, 0) - Y.getAsDouble(j, 0)),
					Math.min(Math.abs(Y.getAsDouble(i, 0) - Y.getAsDouble(j, 0) + 2*radius),
							Math.abs(Y.getAsDouble(i, 0) - Y.getAsDouble(j, 0) - 2*radius)));
			return Math.sqrt(I1*I1 + I2*I2);
			
		} else if(m.equals(Metric.PERIODIC_MAX)) {
			double I1 = Math.min(Math.abs(X.getAsDouble(i, 0) - X.getAsDouble(j, 0)),
					Math.min(Math.abs(X.getAsDouble(i, 0) - X.getAsDouble(j, 0) + 2*radius),
							Math.abs(X.getAsDouble(i, 0) - X.getAsDouble(j, 0) - 2*radius)));
			double I2 = Math.min(Math.abs(Y.getAsDouble(i, 0) - Y.getAsDouble(j, 0)),
					Math.min(Math.abs(Y.getAsDouble(i, 0) - Y.getAsDouble(j, 0) + 2*radius),
							Math.abs(Y.getAsDouble(i, 0) - Y.getAsDouble(j, 0) - 2*radius)));
			return Math.max(I1, I2);
			
		} else { //use the Euclidean metric
			return Math.sqrt(Math.pow(X.getAsDouble(i, 0) - X.getAsDouble(j, 0), 2) + 
					Math.pow(Y.getAsDouble(i, 0) - Y.getAsDouble(j, 0), 2));
		}
	}

	/**
	 * Calculates the bias, upper neighborhood size estimate,
	 * and lower neighborhood size limit of the graph based on
	 * the current graph parameters.
	 */
	public void calculateBiasAndEstimates() {
		double sumDistances = 0; //sum of all distances
		int numEdges = 0; //number of edges
		double sumEdgeLengths = 0; //sum of all edge lengths
		long N = A.getRowCount(); //number of vertices
		
		radius = 1;
		
		if (metricFunction.equals(Metric.PERIODIC_EUCLIDEAN) ||
				metricFunction.equals(Metric.PERIODIC_MAX)) {
			radius = 0;
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					if(i > j) {
						double dist = computeDistance(i, j, Metric.EUCLIDEAN);
						if (dist > radius) {
							radius = dist;
						}
					}
				}
			}
			radius = .5*radius; // half of max distance
		}
		
		//compute the distances between each vertex pair
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				if(i > j) {
					double dist = computeDistance(i, j, metricFunction);
					sumDistances += dist;
					numEdges += A.getAsInt(i, j);
					sumEdgeLengths += A.getAsInt(i, j)*dist;
				}
			}
		}
		
		//the average distance between all pairs of vertices
		double avgAllPairs = sumDistances*2/(N*(N-1));
		
		//the average length of an edge
		double avgEdgeLength = sumEdgeLengths/numEdges;
		
		//calculate the bias
		bias = avgEdgeLength/avgAllPairs;
		
		//calculate the relative neighborhood size
		nbhdSizeLowerEstimate = -1;
		nbhdSizeUpperEstimate = -1;
		if(bias <= 1) {
			nbhdSizeLowerEstimate = Math.sqrt(2*numEdges*(1-bias))/N;
			nbhdSizeUpperEstimate = Math.sqrt(1-bias);
		}
	}

	/**
	 * Gets the graph identification (the number of vertices).
	 * 
	 * @param out
	 * @return the number of vertices
	 */
	public String getGraphID(PrintStream out) {
		return Long.toString(numVerts);
	}
}
