package us.jonesrychtar.socialnetwork.SpatialGraph;

import us.jonesrychtar.socialnetwork.*;
import us.jonesrychtar.socialnetwork.SpatialGraph.SpatialGraphBase;
import java.util.Random;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;

public class Poisson extends SpatialGraphBase {
	protected double rho;
	protected double H;
	protected double alpha;
	protected double beta;
	
	/**
	 * Constructs a new Poisson object.
	 * 
	 * @param numVerts the number of vertices in the graph.
	 * @param rho density parameter.
	 * @param metricFunction which metric function to use.
	 * @param H parameter for edge distribution function.
	 * @param alpha parameter for edge distribution function, between 0 and 1.
	 * @param beta parameter for edge distribution function, between 0 and 1.
	 */
	public Poisson(int numVerts, double rho, Metric metricFunction, double H, double alpha, double beta) {
		this.numVerts = numVerts;
		this.rho = rho;
		this.metricFunction = metricFunction;
		this.H = H;
		this.alpha = alpha;
		this.beta = beta;
	}
	
	/**
	 * Generates a spatial graph using the Poisson process.
	 */
	public void generateGraph() {
		Y = MatrixFactory.zeros(numVerts, 1);
		X = MatrixFactory.zeros(numVerts, 1);
		A = MatrixFactory.zeros(numVerts, numVerts); //adjacency matrix is initially zeros
		
		Random rand = new Random(); //random number generator
		
		Matrix theta = MatrixFactory.zeros(numVerts, 1);
		Matrix O = MatrixFactory.zeros(numVerts, 1);
		Matrix R = MatrixFactory.zeros(numVerts, 1);
		
		//calculate theta, O, and R
		for(int i = 0; i < numVerts; i++) {
			theta.setAsDouble(rand.nextDouble()*2*Math.PI, i, 0); //range: 0 to 2*pi
			O.setAsDouble(-Math.log(rand.nextDouble())/rho, i, 0);
			double RTemp = 0;
			for(int j = 0; j < i; j++) {
				RTemp += O.getAsDouble(j, 0);
			}
			R.setAsDouble(Math.sqrt(RTemp/Math.PI), i, 0);
		}
		
		//calculate X and Y
		for(int i = 0; i < numVerts; i++) {
			X.setAsDouble(R.getAsDouble(i, 0)*Math.cos(theta.getAsDouble(i, 0))/R.getAsDouble(numVerts-1, 0), i, 0);
			Y.setAsDouble(R.getAsDouble(i, 0)*Math.sin(theta.getAsDouble(i, 0))/R.getAsDouble(numVerts-1, 0), i, 0);
		}
		
		//generate the adjacency matrix
		for(int i = 0; i < numVerts; i++) {
			for(int j = i+1; j < numVerts; j++) {
				double dist = computeDistance(i, j, metricFunction);
				double p = rand.nextDouble();
				if((dist <= H && p < alpha) || (dist > H && p < beta)) {
					A.setAsDouble(1, i, j);
					A.setAsDouble(1, j, i);
				}
				//otherwise A(i,j) and A(j,i) are 0, but they are already set above 
			}
		}
		
		calculateBiasAndEstimates();
	}
	
	/**
	 * Gets the following parameters, comma separated:
	 * the number of vertices,
	 * the metric function,
	 * H,
	 * alpha,
	 * beta
	 * 
	 * @return number of vertices,metric function,H,alpha,beta
	 */
	public String getGraphID() {
		return numVerts + "," + metricFunction + "," + H + "," + alpha + "," + beta;
	}
	
	/**
	 * Gets the graph bias, lower neighborhood size estimate,
	 * and upper neighborhood size estimate.
	 * 
	 * @return bias,lower neighborhood size estimate,upper neighborhood size estimate.
	 */
	public String getGraphBiasAndEstimates() {
		return bias + "," + nbhdSizeLowerEstimate + "," + nbhdSizeUpperEstimate;
	}
}
