/*
 * Robert Gove
 * May 2009
 * 
 * For research by Eric Jones and Jan Rychtar.
 * 
 * Requires:
 * ujmp http://www.ujmp.org/ Tested with version 0.2.2
 */

package us.jonesrychtar.socialnetwork;

//import us.jonesrychtar.socialnetwork.SpatialGraph.SpatialGraphBase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.exceptions.MatrixException;

import us.jonesrychtar.socialnetwork.CoordinateGraph.*;
import us.jonesrychtar.socialnetwork.MetricFunction.*;
import us.jonesrychtar.socialnetwork.Network.*;
import us.jonesrychtar.socialnetwork.Reader.*;
import us.jonesrychtar.socialnetwork.SpatialGraph.*;
import us.jonesrychtar.socialnetwork.Writer.*;

import com.mindprod.csv.CSVReader;
import us.jonesrychtar.socialnetwork.SpatialGraph.Poisson;

public class SpatialNetworkBias {
	//private int coordinateFormat; // 1 = xy, 2 = radians, 3 = degrees, 4 = decimal degrees
	private int distanceFormat;
	private int timeUnits;
	private int distanceUnits;
	private int includeEgo; // 1 if add second input file for ego; 2 otherwise
	private int filesHaveEgo; // 1 if ego is already included; 2 otherwise
	private String egoFileName; // name of the file that has network data for ego
	private int networkFormat; // 1 if full matrix; 2 if upper matrix; 3 if lower matrix; 4 if node list; 5 if edge list
	private int edgeFormat; // 1 if binary; 2 multi-value
	private int MinMissingEdgeValue;
	private int MaxMissingEdgeValue;
	private int edgeMinValue;
	private int numVerts;
	private int numNetworks;
	private Matrix x = MatrixFactory.emptyMatrix();
	private Matrix y = MatrixFactory.emptyMatrix();
	private Matrix A = MatrixFactory.emptyMatrix();
	private Metric metricFunction = Metric.EUCLIDEAN;
	private char networkSeparatorChar;
	private char distanceSeparatorChar;
    private char quoteChar;
    private String commentChars;
    private boolean hideComments;
    private boolean trimQuoted;
    private boolean allowMultiLineFields;
    private Random rand;

    private CoordinateGraphBase theGraph;
    private SpatialGraphBase theSpatialGraph;
    private MetricFunctionBase theFunction;
    private NetworkBase theNetwork;
    private ReaderBase theReader;
    private WriterBase theWriter;

	public SpatialNetworkBias() {
		//coordinateFormat = 0;
		distanceFormat = 0;
		timeUnits = 0;
		distanceUnits = 0;
		includeEgo = 0;
		networkFormat = 0;
		edgeFormat = 0;
		edgeMinValue = 0;
		MinMissingEdgeValue = 0;
		MaxMissingEdgeValue = 3;
		x = MatrixFactory.emptyMatrix();
		y = MatrixFactory.emptyMatrix();
		A = MatrixFactory.emptyMatrix();
		metricFunction = Metric.EUCLIDEAN;
		distanceSeparatorChar = ',';
		networkSeparatorChar = ',';
        quoteChar = '\"';
        commentChars = "#";
        hideComments = true;
        trimQuoted = true;
        allowMultiLineFields = true;
        rand = new Random();
        theGraph=new CoordinateGraphBase();
        theSpatialGraph = new SpatialGraphBase();
        theFunction = new MetricFunctionBase();
        theNetwork = new NetworkBase();
        theReader = new ReaderBase();
        theWriter = new WriterBase();
	}
	
	   /*public static void main(String[] args) {
    SpatialNetworkBias snb = new SpatialNetworkBias();

    snb.theGraph.setCoordinateFormat();

    if (snb.coordinateFormat == 2) {
    snb.setDistanceFormat();
    }

    if (snb.distanceFormat == 1) {
    snb.setTimeUnits();
    } else if (snb.distanceFormat == 2) {
    snb.setDistanceUnits();
    }

    snb.setMetricFunction();

    snb.setNetworkFormat();

    snb.setEdgeFormat();

    if (snb.edgeFormat == 1) {
    snb.setEdgeMinValue();
    }

    snb.setMinMissingEdgeValue();
    snb.setMaxMissingEdgeValue();

    snb.setNumVerts();

    snb.setNumNetworks();

    snb.setIncludeEgo();

    snb.setDistanceSeparatorChar();

    snb.setNetworkSeparatorChar();

    snb.readInputFiles();

    System.out.println("Program completed successfully.");
    }*/


	@SuppressWarnings("unused")
	private void createCoordinateGraphData() throws FileNotFoundException {
		rand = new Random();
		PrintStream outx = new PrintStream(new FileOutputStream("x.csv"));
		PrintStream outy = new PrintStream(new FileOutputStream("y.csv"));
		PrintStream outa = new PrintStream(new FileOutputStream("a.csv"));

		for (int i = 0; i < 25; i++) {
			outx.print(rand.nextDouble()*10);
			if (i < 24) { outx.print(","); }
		}
		outx.close();

		for (int i = 0; i < 25; i++) {
			outy.print(rand.nextDouble()*10);
			if (i < 24) { outy.print(","); }
		}
		outy.close();

		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 25; j++) {
				if (i==j) {
					outa.print(0);
				} else {
					outa.print(rand.nextInt()%2);
				}
				if (j < 24) { outa.print(","); }
			}
			outa.println("");
		}
		outa.close();
	}
	
	
/*	private void setCoordinateFormat() {
		System.out.println("Is the coordinate dataset in x,y format, or in direction and distance format?\n" +
			"Please enter the option number from the menu below.\n" +
			"\t1. x,y format.\n" +
			"\t2. Radians (e.g. 3.14159) and distance format.\n" +
			"\t3. Degree (e.g. 180) and distance format.\n" +
			"\t4. Decimal degree (e.g. 0.5) and distance format.\n");
		while(true) {
			Scanner sc = new Scanner(System.in);
			int option = sc.nextInt();
			if(option == 1) {
				coordinateFormat = 1;
				break;
			} else if(option == 2) {
				coordinateFormat = 2;
				break;
			} else if(option == 3) {
				coordinateFormat = 3;
				break;
			} else if(option == 4) {
				coordinateFormat = 4;
				break;
			} else {
				System.out.println("Invalid input. Please re-enter.");
			}
		}
	}
*/
	
	private void setDistanceFormat() {
		System.out.println("Is distance in time, or in (linear geographic) distance?\n" +
			"\t1. Distance is in time.\n" +
			"\t2. Distance is in linear geographic distance.\n");
		while(true) {
			Scanner sc = new Scanner(System.in);
			int option = sc.nextInt();
			if(option == 1) {
				distanceFormat = 1;
				break;
			} else if(option == 2) {
				distanceFormat = 2;
				break;
			} else {
				System.out.println("Invalid input. Please re-enter.");
			}
		}
	}
	
	private void setNetworkFormat() {
		System.out.println("What is the format of the network?\n" +
				"\t1. Full matrix.\n" +
				"\t2. Upper half of full matrix.\n" +
				"\t3. Lower half of full matrix.\n" +
				"\t4. Node list.\n" +
				"\t5. Edge list.\n");
			while(true) {
				Scanner sc = new Scanner(System.in);
				int option = sc.nextInt();
				if(option == 1) {
					networkFormat = 1;
					break;
				} else if(option == 2) {
					networkFormat = 2;
					break;
				} else if(option == 3) {
					networkFormat = 3;
					break;
				} else if(option == 4) {
					networkFormat = 4;
					break;
				} else if(option == 5) {
					networkFormat = 5;
					break;
				} else {
					System.out.println("Invalid input. Please re-enter.");
				}
			}
	}
	
	private void setEdgeFormat() {
		System.out.println("Should network edges be read as binary (0s and 1s) or read as multiple values?\n" +
			"\t1. Binary\n" +
			"\t2. Multiple values.\n");
		while(true) {
			Scanner sc = new Scanner(System.in);
			int option = sc.nextInt();
			if(option == 1) {
				edgeFormat = 1;
				break;
			} else if(option == 2) {
				edgeFormat = 2;
				break;
			} else {
				System.out.println("Invalid input. Please re-enter.");
			}
		}
	}
	
	private void setEdgeMinValue() {
		System.out.println("Please enter the minimum edge value. All values greater or equal to this will be counted as an edge.");
		Scanner sc = new Scanner(System.in);
		edgeMinValue = sc.nextInt();
	}
	
	private void setMinMissingEdgeValue() {
		System.out.println("If there is missing edge data, a random number will be chosen for that edge value from a certain range. What should be the smallest value in that range?");
		Scanner sc = new Scanner(System.in);
		MinMissingEdgeValue = sc.nextInt();
	}
	
	private void setMaxMissingEdgeValue() {
		System.out.println("What should be the largest value in that range?");
		Scanner sc = new Scanner(System.in);
		MinMissingEdgeValue = sc.nextInt();
	}
	
	private void setTimeUnits() {
		System.out.println("Is time in minutes, hours, or days?\n" +
			"\t1. Minutes.\n" +
			"\t2. Hours.\n" +
			"\t3. Days.\n");
		while(true) {
			Scanner sc = new Scanner(System.in);
			int option = sc.nextInt();
			if(option == 1) {
				timeUnits = 1;
				break;
			} else if(option == 2) {
				timeUnits = 2;
				break;
			} else if(option == 3) {
				timeUnits = 2;
				break;
			} else {
				System.out.println("Invalid input. Please re-enter.");
			}
		}
	}
	
	
	private void setDistanceUnits() {
		System.out.println("Is distance in miles or meters?\n" +
			"\t1. Miles.\n" +
			"\t2. Meters.\n");
		while(true) {
			Scanner sc = new Scanner(System.in);
			int option = sc.nextInt();
			if(option == 1) {
				distanceUnits = 1;
				break;
			} else if(option == 2) {
				distanceUnits = 2;
				break;
			} else {
				System.out.println("Invalid input. Please re-enter.");
			}
		}
	}
	
	private void setMetricFunction() {
		System.out.println("What metric function do you want to use in the calculations?\n" +
			"\t1. Euclidean\n" +
			"\t2. Max\n" +
			"\t3. Periodic Euclidean\n" +
			"\t4. Periodic Max\n" +
			"\t5. All metric functions.");
		while(true) {
			Scanner sc = new Scanner(System.in);
			int option = sc.nextInt();
			if(option == 1) {
				metricFunction = Metric.EUCLIDEAN;
				break;
			} else if(option == 2) {
				metricFunction = Metric.MAX;
				break;
			} else if(option == 3) {
				metricFunction = Metric.PERIODIC_EUCLIDEAN;
				break;
			} else if(option == 4) {
				metricFunction = Metric.PERIODIC_MAX;
				break;
			} else if(option == 5) {
				metricFunction = null;
				break;
			} else {
				System.out.println("Invalid input. Please re-enter.");
			}
		}
	}
	
	private void setNumVerts() {
		System.out.println("How many vertices are there per network?");
		Scanner sc = new Scanner(System.in);
		numVerts = sc.nextInt();
	}
	
	private void setNumNetworks() {
		System.out.println("How many networks are there in the input files?");
		Scanner sc = new Scanner(System.in);
		numNetworks = sc.nextInt();
	}
	
	private void setIncludeEgo() {
		System.out.println("Do you have a file that would add the valued ties between egos and alters? This should be a list of n egos (with each ego listed m times) in the first column and, in the second column is the list of the values for each of the m alters for each ego.\n" +
			"\t1. Yes.\n" +
			"\t2. No.\n");
		while(true) {
			Scanner sc = new Scanner(System.in);
			int option = sc.nextInt();
			if(option == 1) {
				includeEgo = 1;
				break;
			} else if(option == 2) {
				includeEgo = 2;
				break;
			} else {
				System.out.println("Invalid input. Please re-enter.");
			}
		}
		
		if (includeEgo == 1) {
			System.out.println("Is the data for ego already in the distance and network input files?\n" +
				"\t1. Yes.\n" +
				"\t2. No.\n");
			while(true) {
				Scanner sc = new Scanner(System.in);
				int option = sc.nextInt();
				if(option == 1) {
					filesHaveEgo = 1;
					break;
				} else if(option == 2) {
					filesHaveEgo = 2;
					break;
				} else {
					System.out.println("Invalid input. Please re-enter.");
				}
			}
		}
		
		if (filesHaveEgo == 2) {
			System.out.println("What file contains the network data for ego?\n");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				try {
					egoFileName = in.readLine();
				} catch (IOException e) {
					System.out.println("Error reading input");
					e.printStackTrace();
				}
		}
	}

	private void setDistanceSeparatorChar() {
		System.out.println("What character does the XY file use as a delimiter?\n" +
			"\t1. space\n" +
			"\t2. tab\n" +
			"\t3. semicolon\n" +
			"\t4. comma\n");
		while(true) {
			Scanner sc = new Scanner(System.in);
			int option = sc.nextInt();
			if(option == 1) {
				distanceSeparatorChar = ' ';
				break;
			} else if(option == 2) {
				distanceSeparatorChar = '\t';
				break;
			} else if(option == 3) {
				distanceSeparatorChar = ';';
				break;
			} else if(option == 4) {
				distanceSeparatorChar = ',';
				break;
			} else {
				System.out.println("Invalid input. Please re-enter.");
			}
		}
	}
	
	private void setNetworkSeparatorChar() {
		System.out.println("What character does the network file use as a delimiter?\n" +
			"\t1. space\n" +
			"\t2. tab\n" +
			"\t3. semicolon\n" +
			"\t4. comma\n");
		while(true) {
			Scanner sc = new Scanner(System.in);
			int option = sc.nextInt();
			if(option == 1) {
				networkSeparatorChar = ' ';
				break;
			} else if(option == 2) {
				networkSeparatorChar = '\t';
				break;
			} else if(option == 3) {
				networkSeparatorChar = ';';
				break;
			} else if(option == 4) {
				networkSeparatorChar = ',';
				break;
			} else {
				System.out.println("Invalid input. Please re-enter.");
			}
		}
	}
	
	private void readInputFiles() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		//CSVReader distanceReader;
		//CSVReader networkReader;
		try {
			System.out.println("Please enter the file name of the distance data.");
			CSVReader distanceReader = new CSVReader(new FileReader(new File(in.readLine())),
					distanceSeparatorChar, quoteChar, commentChars, hideComments, trimQuoted, allowMultiLineFields);
			
			System.out.println("Please enter the file name of the network data.");
			CSVReader networkReader = new CSVReader(new FileReader(new File(in.readLine())),
					networkSeparatorChar, quoteChar, commentChars, hideComments, trimQuoted, allowMultiLineFields);
			
			System.out.println("Please enter the output file name.");
			FileWriter fw = new FileWriter(new File(in.readLine()));
			CSVReader egoReader = null;
			if (includeEgo == 1) {
				egoReader = new CSVReader(new FileReader(egoFileName));
			}
			
			if (metricFunction == null) {
				fw.write("Network,Metric,Bias,Radius,Relative_neighborhood_size_lower,Neighborhood_size_lower,Relative_neighborhood_size_upper,Neighborhood_size_upper," +
						"Metric,Bias,Radius,Relative_neighborhood_size_lower,Neighborhood_size_lower,Relative_neighborhood_size_upper,Neighborhood_size_upper," +
						"Metric,Bias,Radius,Relative_neighborhood_size_lower,Neighborhood_size_lower,Relative_neighborhood_size_upper,Neighborhood_size_upper," +
						"Metric,Bias,Radius,Relative_neighborhood_size_lower,Neighborhood_size_lower,Relative_neighborhood_size_upper,Neighborhood_size_upper\r\n");
			} else {
				fw.write("Network,Metric,Bias,Radius,Relative_neighborhood_size_lower,Neighborhood_size_lower,Relative_neighborhood_size_upper,Neighborhood_size_upper\r\n");
			}
			
			if (networkFormat == 1) {
				for (int i = 1; i <= numNetworks && readDistance(distanceReader) && readMatrixAsFullMatrix(networkReader); i++) {
					if (includeEgo == 1) {
						readEgo(egoReader);
					}
					createGraphAndWriteOutput(fw, i);
				}
			} else if (networkFormat == 2) {
				for (int i = 1; i <= numNetworks && readDistance(distanceReader) && readMatrixAsUpperMatrix(networkReader); i++) {
					if (includeEgo == 1) {
						readEgo(egoReader);
					}
					createGraphAndWriteOutput(fw, i);
				}
			} else if (networkFormat == 3) {
				for (int i = 1; i <= numNetworks && readDistance(distanceReader) && readMatrixAsLowerMatrix(networkReader); i++) {
					if (includeEgo == 1) {
						readEgo(egoReader);
					}
					createGraphAndWriteOutput(fw, i);
				}
			} else if (networkFormat == 4 || networkFormat == 5) {
				networkReader.skipToNextLine();
				networkReader.skip(1);
				int graphNum = networkReader.getInt();
				int nextGraphNum = graphNum;
				for (int i = 1; i <= numNetworks && readDistance(distanceReader); i++) {
					// reset the adjacency matrix
					if (includeEgo == 1) {
						A = MatrixFactory.zeros(numVerts+1, numVerts+1);
					} else {
						A = MatrixFactory.zeros(numVerts, numVerts);
					}
					do {
						nextGraphNum = graphNum;
						int row = networkReader.getInt();
						int col = networkReader.getInt();
						if (networkFormat == 4) {
							A.setAsDouble(1, row-1, col-1);
							A.setAsDouble(1, col-1, row-1);
						} else {
							int value = networkReader.getInt();
							if (value > 3) {
								value = getRandomEdgeValue();
							}
							if (edgeFormat == 1) {
								if (value >= edgeMinValue) {
									A.setAsDouble(1, row-1, col-1);
									A.setAsDouble(1, col-1, row-1);
								} else {
									A.setAsDouble(0, row-1, col-1);
									A.setAsDouble(0, col-1, row-1);
								}
							} else {
								A.setAsDouble(value, row-1, col-1);
								A.setAsDouble(value, col-1, row-1);
							}
						}
						networkReader.skipToNextLine();
						networkReader.skip(1);
						nextGraphNum = networkReader.getInt();
					} while (graphNum == nextGraphNum);
					if (includeEgo == 1) {
						readEgo(egoReader);
					}
					createGraphAndWriteOutput(fw, i);
				}
			}
			
			
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean readDistance(CSVReader reader) {
		String exitCondition = "";
		try {
			reader.skipToNextLine(); //skip over the header row (or skip to next graph if previously reading)
		} catch (IOException e1) {
			exitCondition += "Error reading the spatial data for the next network.";
			e1.printStackTrace();
		}
		
		//reset the matrices
		if (includeEgo == 1) {
			x = MatrixFactory.zeros(numVerts+1, 1);
			y = MatrixFactory.zeros(numVerts+1, 1);
		} else {
			x = MatrixFactory.zeros(numVerts, 1);
			y = MatrixFactory.zeros(numVerts, 1);
		}
		
		// keep track of which nodes have missing spatial data
		LinkedList<Integer> missingNodes = new LinkedList<Integer>(); 
		double totalXCoord = 0;
		double totalYCoord = 0;

		for (int i = 0; i < numVerts && exitCondition.equals(""); i++) {
			try {
				reader.skip(2); //skip the two leading fields
                theGraph.readValues(reader);
                if (theGraph.isValidNodeValue())
                    missingNodes.add(i);
                else{
					x.setAsDouble(xCoord, i, 0);
					y.setAsDouble(yCoord, i, 0);
					totalXCoord += xCoord;
					totalYCoord += yCoord;
                }
				if (coordinateFormat == 2) {
					//radians
					double radius = reader.getDouble();
					double theta = reader.getDouble();
					if (radius == 9999 || theta == 9999) {
						missingNodes.add(i);
					} else {
						double xCoord = radius*Math.cos(theta);
						double yCoord = radius*Math.sin(theta);
						x.setAsDouble(xCoord, i, 0);
						y.setAsDouble(yCoord, i, 0);
						totalXCoord += xCoord;
						totalYCoord += yCoord;
					}
				} else if (coordinateFormat == 3) {
					//degrees
					double radius = reader.getDouble();
					double theta = reader.getDouble();
					if (radius == 9999 || theta == 9999) {
						missingNodes.add(i);
					} else {
						double xCoord = radius*Math.cos(theta*Math.PI/180.0);
						double yCoord = radius*Math.sin(theta*Math.PI/180.0);
						x.setAsDouble(xCoord, i, 0);
						y.setAsDouble(yCoord, i, 0);
						totalXCoord += xCoord;
						totalYCoord += yCoord;
					}
				} else if (coordinateFormat == 4) {
					//decimal degrees. 1 decimal degree = 360 degrees = 2pi radians
					double radius = reader.getDouble();
					double theta = reader.getDouble();
					if (radius == 9999 || theta == 9999) {
						missingNodes.add(i);
					} else {
						double xCoord = radius*Math.cos(theta*2*Math.PI);
						double yCoord = radius*Math.sin(theta*2*Math.PI);
						x.setAsDouble(xCoord, i, 0);
						y.setAsDouble(yCoord, i, 0);
						totalXCoord += xCoord;
						totalYCoord += yCoord;
					}
				}
				if (i < numVerts-1) {
					reader.skipToNextLine();
				}
			} catch (MatrixException e) {
				exitCondition += "Error: Internal data writing error on line " + i + " of current distance data.";
				e.printStackTrace();
			} catch (NumberFormatException e) {
				exitCondition += "Error: Reading an entry on line " + i + " of current spatial data which was not a properly formatted number.";
				e.printStackTrace();
			} catch (IOException e) {
				exitCondition += "An unspecified error occured at line " + i + " of the current spatial data.";
				e.printStackTrace();
			}
		}
		
		// if we have nodes with missing spatial data
		if (missingNodes.size() > 0) {
			for (int i : missingNodes) {
				// set the missing x- and y-coords to the average of the coords that we have
				x.setAsDouble(totalXCoord/((double) (numVerts - missingNodes.size())), i, 0);
				y.setAsDouble(totalYCoord/((double) (numVerts - missingNodes.size())), i, 0);
			}
		}

		if (exitCondition.equals("")) {
			return true;
		} else {
			System.out.println(exitCondition);
			return false;
		}
	}

	public boolean readMatrixAsFullMatrix(CSVReader reader) {
		String exitCondition = "";
		try {
			reader.skipToNextLine(); //skip over the header row (or skip to next graph if previously reading)
		} catch (IOException e1) {
			exitCondition += "Error reading the network data for the next network.";
			e1.printStackTrace();
		}
		
		// reset the matrix
		if (includeEgo == 1) {
			A = MatrixFactory.zeros(numVerts+1, numVerts+1);
		} else {
			A = MatrixFactory.zeros(numVerts, numVerts);
		}

		for (int i = 0; i < numVerts && exitCondition.equals(""); i++) {
			try {
				reader.skip(2); //skip the two leading fields
				for (int j = 0; j < numVerts && exitCondition.equals(""); j++) {
					int value = reader.getInt();
					if (value > 3) {
						value = getRandomEdgeValue();
					}
					if (edgeFormat == 1) {
						if (value >= edgeMinValue) {
							A.setAsDouble(1, i, j);
						} else {
							A.setAsDouble(0, i, j);
						}
					} else {
						A.setAsDouble(value, i, j);
					}
				}
				if (i < numVerts-1) {
					reader.skipToNextLine(); //done reading this line, go to next line
				}
			} catch (MatrixException e) {
				exitCondition += "Error: Internal data writing error on line " + i + " of current network data.";
				e.printStackTrace();
			} catch (NumberFormatException e) {
				exitCondition += "Error: Reading an entry on line " + i + " of current network data which was not a properly formatted number.";
				e.printStackTrace();
			} catch (IOException e) {
				exitCondition += "An unspecified error occured at line " + i + " of the current network data.";
				e.printStackTrace();
			}
		}

		if (exitCondition.equals("")) {
			return true;
		} else {
			System.out.println(exitCondition);
			return false;
		}
	}

	public boolean readMatrixAsUpperMatrix(CSVReader reader) {
		String exitCondition = "";
		try {
			reader.skipToNextLine(); //skip over the header row (or skip to next graph if previously reading)
		} catch (IOException e1) {
			exitCondition += "Error reading the network data for the next network.";
			e1.printStackTrace();
		}
		
		// reset the matrix
		if (includeEgo == 1) {
			A = MatrixFactory.zeros(numVerts+1, numVerts+1);
		} else {
			A = MatrixFactory.zeros(numVerts, numVerts);
		}

		for (int i = 0; i < numVerts && exitCondition.equals(""); i++) {
			try {
				reader.skip(2+i+1); //skip the two leading fields
				for (int j = i+1; j < numVerts && exitCondition.equals(""); j++) {
					int value = reader.getInt();
					if (value > 3) {
						value = getRandomEdgeValue();
					}
					if (edgeFormat == 1) {
						if (value >= edgeMinValue) {
							A.setAsDouble(1, i, j);
							A.setAsDouble(1, j, i);
						} else {
							A.setAsDouble(0, i, j);
							A.setAsDouble(0, j, i);
						}
					} else {
						A.setAsDouble(value, i, j);
						A.setAsDouble(value, j, i);
					}
				}
				if (i < numVerts-1) {
					reader.skipToNextLine(); //done reading this line, go to next line
				}
			} catch (MatrixException e) {
				exitCondition += "Error: Internal data writing error on line " + i + " of current network data.";
				e.printStackTrace();
			} catch (NumberFormatException e) {
				exitCondition += "Error: Reading an entry on line " + i + " of current network data which was not a properly formatted number.";
				e.printStackTrace();
			} catch (IOException e) {
				exitCondition += "An unspecified error occured at line " + i + " of the current network data.";
				e.printStackTrace();
			}
		}

		if (exitCondition.equals("")) {
			return true;
		} else {
			System.out.println(exitCondition);
			return false;
		}
	}

	public boolean readMatrixAsLowerMatrix(CSVReader reader) {
		String exitCondition = "";
		try {
			reader.skipToNextLine(); //skip over the header row (or skip to next graph if previously reading)
		} catch (IOException e1) {
			exitCondition += "Error reading the network data for the next network.";
			e1.printStackTrace();
		}
		
		// reset the matrix
		if (includeEgo == 1) {
			A = MatrixFactory.zeros(numVerts+1, numVerts+1);
		} else {
			A = MatrixFactory.zeros(numVerts, numVerts);
		}

		for (int i = 0; i < numVerts && exitCondition.equals(""); i++) {
			try {
				reader.skip(2); //skip the two leading fields
				for (int j = 0; j < i && exitCondition.equals(""); j++) {
					int value = reader.getInt();
					if (value > 3) {
						value = getRandomEdgeValue();
					}
					if (edgeFormat == 1) {
						if (value >= edgeMinValue) {
							A.setAsDouble(1, i, j);
							A.setAsDouble(1, j, i);
						} else {
							A.setAsDouble(0, i, j);
							A.setAsDouble(0, j, i);
						}
					} else {
						A.setAsDouble(value, i, j);
						A.setAsDouble(value, j, i);
					}
				}
				if (i < numVerts-1) {
					reader.skipToNextLine(); //done reading this line, go to next line
				}
			} catch (MatrixException e) {
				exitCondition += "Error: Internal data writing error on line " + i + " of current network data.";
				e.printStackTrace();
			} catch (NumberFormatException e) {
				exitCondition += "Error: Reading an entry on line " + i + " of current network data which was not a properly formatted number.";
				e.printStackTrace();
			} catch (IOException e) {
				exitCondition += "An unspecified error occured at line " + i + " of the current network data.";
				e.printStackTrace();
			}
		}

		if (exitCondition.equals("")) {
			return true;
		} else {
			System.out.println(exitCondition);
			return false;
		}
	}
	
	public boolean readEgo(CSVReader reader) {
		String exitCondition = "";
		try {
			reader.skipToNextLine(); //skip over the header row (or skip to next graph if previously reading)
		} catch (IOException e1) {
			exitCondition += "Error reading the ego data for the next network.";
			e1.printStackTrace();
		}

		for (int i = 0; i < numVerts && exitCondition.equals(""); i++) {
			try {
				reader.skip(2); //skip the two leading fields
				int value = reader.getInt();
				if (value > 3) {
					value = getRandomEdgeValue();
				}
				if (edgeFormat == 1) {
					if (value >= edgeMinValue) {
						A.setAsDouble(1, i, numVerts);
						A.setAsDouble(1, numVerts, i);
					} else {
						A.setAsDouble(0, i, numVerts);
						A.setAsDouble(0, numVerts, i);
					}
				} else {
					A.setAsDouble(value, i, numVerts);
					A.setAsDouble(value, numVerts, i);
				}
			} catch (MatrixException e) {
				exitCondition += "Error: Internal data writing error on line " + i + " of current network data.";
				e.printStackTrace();
			} catch (NumberFormatException e) {
				exitCondition += "Error: Reading an entry on line " + i + " of current network data which was not a properly formatted number.";
				e.printStackTrace();
			} catch (IOException e) {
				exitCondition += "An unspecified error occured at line " + i + " of the current network data.";
				e.printStackTrace();
			}
		}

		if (exitCondition.equals("")) {
			return true;
		} else {
			System.out.println(exitCondition);
			return false;
		}
	}
	
	public int getRandomEdgeValue() {
		// get a random int in the range [MinMissingEdgeValue, MaxMissingEdgeValue]
		return rand.nextInt(Math.abs(MinMissingEdgeValue-MaxMissingEdgeValue)+1)+MinMissingEdgeValue;
	}
	
	public void createGraphAndWriteOutput(FileWriter fw, int graphNum) throws IOException {
		SpatialGraphBase sg = new SpatialGraphBase(x, y, A);
		if (metricFunction == null) {
			sg.setMetricFunction(Metric.EUCLIDEAN);
			sg.calculateBiasAndEstimates();
			String result = graphNum + ","  + "Euclidean" + "," + sg.getBias() + "," + sg.getRadius() + "," + sg.getNbhdSizeLowerEstimate() + "," + sg.getRadius()*sg.getNbhdSizeLowerEstimate() + "," + sg.getNbhdSizeUpperEstimate() + "," + sg.getRadius()*sg.getNbhdSizeUpperEstimate() + ",";
			sg.setMetricFunction(Metric.MAX);
			sg.calculateBiasAndEstimates();
			result += "Max" + "," + sg.getBias() + "," + sg.getRadius() + "," + sg.getNbhdSizeLowerEstimate() + "," + sg.getRadius()*sg.getNbhdSizeLowerEstimate() + "," + sg.getNbhdSizeUpperEstimate() + "," + sg.getRadius()*sg.getNbhdSizeUpperEstimate() + ",";
			sg.setMetricFunction(Metric.PERIODIC_EUCLIDEAN);
			sg.calculateBiasAndEstimates();
			result += "Periodic_Euclidean" + "," + sg.getBias() + "," + sg.getRadius() + "," + sg.getNbhdSizeLowerEstimate() + "," + sg.getRadius()*sg.getNbhdSizeLowerEstimate() + "," + sg.getNbhdSizeUpperEstimate() + "," + sg.getRadius()*sg.getNbhdSizeUpperEstimate() + ",";
			sg.setMetricFunction(Metric.PERIODIC_MAX);
			sg.calculateBiasAndEstimates();
			result += "Periodic_Max" + "," + sg.getBias() + "," + sg.getRadius() + "," + sg.getNbhdSizeLowerEstimate() + "," + sg.getRadius()*sg.getNbhdSizeLowerEstimate() + "," + sg.getNbhdSizeUpperEstimate() + "," + sg.getRadius()*sg.getNbhdSizeUpperEstimate() + "\r\n";
			fw.write(result);
		} else {
			sg.setMetricFunction(metricFunction);
			sg.calculateBiasAndEstimates();
			String metric = (metricFunction == Metric.EUCLIDEAN)? "Euclidean" : (metricFunction == Metric.MAX)? "Max" : (metricFunction == Metric.PERIODIC_EUCLIDEAN)? "Periodic_Euclidean" : "Periodic_Max";
			fw.write(graphNum + ","  + metric + "," + sg.getBias() + "," + sg.getRadius() + "," + sg.getNbhdSizeLowerEstimate() + "," + sg.getRadius()*sg.getNbhdSizeLowerEstimate() + "," + sg.getNbhdSizeUpperEstimate() + "," + sg.getRadius()*sg.getNbhdSizeUpperEstimate() + "\r\n");
		}
	}
}
