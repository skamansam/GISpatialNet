package us.jonesrychtar.socialnetwork;

import java.util.Random;

import org.ujmp.core.MatrixFactory;

public class SquareLatticeSpatialGraph extends SpatialGraph {
	protected int size;
	protected double rewire;
	
	/**
	 * Constructs a new SquareLatticeSpatialGraph object.
	 * 
	 * @param size the size of the square lattice.
	 * @param rewire a real number between 0 and 1.
	 * @param metricFunction
	 */
	public SquareLatticeSpatialGraph(int size, double rewire, Metric metricFunction) {
		this.size = size;
		this.numVerts = (2*size+1)*(2*size+1); //number of vertices
		this.rewire = rewire;
		this.metricFunction = metricFunction;
	}
	/**
	 * Generates a spatial graph from a square lattice.
	 */
	public void generateGraph() {
		this.numVerts = (2*size+1)*(2*size+1);
		Y = MatrixFactory.zeros(numVerts, 1);
		X = MatrixFactory.zeros(numVerts, 1);
		A = MatrixFactory.zeros(numVerts, numVerts); //adjacency matrix is initially zeros
		
		Random rand = new Random();
		
		//generate x,y coordinates and adjacency matrix
		for(int row = 0; row <= 2*size; row++) {
			for(int col = 0; col <= 2*size; col++) {
				int label = row*(2*size+1)+col;
				X.setAsDouble((double)col/size, label, 0); //x-coord
				Y.setAsDouble((double)row/size, label, 0); //y-coord

				//compute adjacency matrix entries
				if(row < 2*size) {
					A.setAsDouble(1, label, label+2*size+1);
					A.setAsDouble(1, label+2*size+1, label);
				}
				if(row > 0) {
					A.setAsDouble(1, label, label-2*size-1);
					A.setAsDouble(1, label-2*size-1, label);
				}
				if(col < 2*size) {
					A.setAsDouble(1, label, label+1);
					A.setAsDouble(1, label+1, label);
				}
				if(col > 0) {
					A.setAsDouble(1, label, label-1);
					A.setAsDouble(1, label-1, label);
				}
			}
		}
		
		//rewire the graph
		for(int c = 0; c < 4*numVerts*rewire; c++) {
			int label1 = rand.nextInt((int)numVerts); //integer from [0,N-1]
			int label2 = rand.nextInt((int)numVerts);
			int nbhs = 0;
			for(int i = 0; i < numVerts; i++) {
				nbhs += A.getAsDouble(label1, i);
			}
			
			//check if there exist edges to remove
			if (nbhs > 0) { 
				int r = rand.nextInt(nbhs); //select random neighbor
				//remove the edge to the random neighbor
				for(int i = 0; i < numVerts; i++) {
					//cycle through until we find the rth neighbor
					if(A.getAsDouble(i, label1) == 1) {
						if(r == 0) {
							A.setAsDouble(0, label1, i);
							A.setAsDouble(0, i, label1);
						}
						r--;
					}
				}
			}
				
			
			
			//add a new edge to a random vertex
			do {
				label2 = rand.nextInt((int)numVerts);
			} while(label2 == label1);
			A.setAsDouble(1, label1, label2);
			A.setAsDouble(1, label2, label1);
		}
		
		calculateBiasAndEstimates();
	}
	
	
	/**
	 * Generates a spatial graph from a square lattice using periodic method.
	 */
	public void generatePeriodicGraph() {
		this.numVerts = (2*size)*(2*size);
		Y = MatrixFactory.zeros(numVerts, 1);
		X = MatrixFactory.zeros(numVerts, 1);
		A = MatrixFactory.zeros(numVerts, numVerts); //adjacency matrix is initially zeros
		
		Random rand = new Random();
		
		//generate x,y coordinates and adjacency matrix
		for(int row = 0; row <= 2*size-1; row++) {
			for(int col = 0; col <= 2*size-1; col++) {
				int label = row*(2*size)+col;
				X.setAsDouble((double)col/size, label, 0); //x-coord
				Y.setAsDouble((double)row/size, label, 0); //y-coord

				//compute adjacency matrix entries
				int upRow = 0;
				if (row < 2*size-1) {
					upRow = row + 1;
				}
				int upLabel = upRow*2*size+col;
				A.setAsDouble(1, label, upLabel);
				A.setAsDouble(1, upLabel, label);
				
				int rightCol = 0;
				if (col < 2*size-1) {
					rightCol = col + 1;
				}
				int rightLabel = row*2*size+rightCol;
				A.setAsDouble(1, label, rightLabel);
				A.setAsDouble(1, rightLabel, label);
			}
		}
		
		//rewire the graph
		for(int c = 0; c < 4*numVerts*rewire; c++) {
			int label1 = rand.nextInt((int)numVerts); //integer from [0,N-1]
			int label2 = rand.nextInt((int)numVerts);
			int nbhs = 0;
			for(int i = 0; i < numVerts; i++) {
				nbhs += A.getAsDouble(label1, i);
			}
			
			//check if there exist edges to remove
			if (nbhs > 0) { 
				int r = rand.nextInt(nbhs); //select random neighbor
				//remove the edge to the random neighbor
				for(int i = 0; i < numVerts; i++) {
					//cycle through until we find the rth neighbor
					if(A.getAsDouble(i, label1) == 1) {
						if(r == 0) {
							A.setAsDouble(0, label1, i);
							A.setAsDouble(0, i, label1);
						}
						r--;
					}
				}
			}
			
			//add a new edge to a random vertex
			do {
				label2 = rand.nextInt((int)numVerts);
			} while(label2 == label1);
			A.setAsDouble(1, label1, label2);
			A.setAsDouble(1, label2, label1);
		}
		
		calculateBiasAndEstimates();
	}

	/**
	 * Get the graph identification (the size and rewire parameters).
	 * 
	 * @return a string with the size and rewire parameters, comma separated.
	 */
	public String getGraphID() {
		return size + "," + rewire + "," + 1.0/size;
	}
	
	/**
	 * Gets the following values, comma separated:
	 * 1/size,
	 * bias,
	 * |1/size-bias|,
	 * bias*size
	 * 
	 * @return 1/size,bias,|1/size-bias|,bias*size
	 */
	public String getGraphBiasAndEstimates() {
		return bias + "," + Math.abs((double)1.0/size-bias) + "," + (double)bias*size;
	}
}
