package us.jonesrychtar.socialnetwork;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Random;

public class GraphSimulation {
	public static void notmain(String[] args) {
		PoissonSpatialGraph psg = new PoissonSpatialGraph(25, 1, Metric.EUCLIDEAN, .25, .5, .25);
		do {
			psg.generateGraph();			
		} while (psg.getNbhdSizeLowerEstimate() < .225 || psg.getNbhdSizeLowerEstimate() > .275);
		
		System.out.println("Bias: " + psg.getBias());
		System.out.println("Neighborhood size: " + psg.getNbhdSizeLowerEstimate());
		System.out.println(psg.getX());
		System.out.println(psg.getY());
		System.out.println(psg.getA());
		
		
		
		
		String fileName = "task";
		String fileExtension = ".csv";
		GraphSimulation GS = new GraphSimulation();
		
		/*SquareLatticeSpatialGraph sg = new SquareLatticeSpatialGraph(2, .1, Metric.EUCLIDEAN);
		sg.generateGraph();
		System.out.println(sg.getA());
		System.out.println(sg.getX());
		System.out.println(sg.getY());
		
		sg = new SquareLatticeSpatialGraph(3, .05, Metric.EUCLIDEAN);
		sg.generateGraph();
		System.out.println(sg.getA());
		System.out.println(sg.getX());
		System.out.println(sg.getY());
		
		sg = new SquareLatticeSpatialGraph(3, .2, Metric.EUCLIDEAN);
		sg.generateGraph();
		System.out.println(sg.getA());
		System.out.println(sg.getX());
		System.out.println(sg.getY());*/
		
		
		/*
		//task 1
		Date start = new Date();
		//GS.task1(fileName + "1", fileExtension);
		///System.out.println("Done with task 1.");
		Date stop = new Date();
		
		long elapsedTime = stop.getTime() - start.getTime();
		long secondInMillis = 1000;
		long minuteInMillis = secondInMillis * 60;
		long hourInMillis = minuteInMillis * 60;

		long elapsedHours = elapsedTime / hourInMillis;
		elapsedTime = elapsedTime % hourInMillis;
		long elapsedMinutes = elapsedTime / minuteInMillis;
		elapsedTime = elapsedTime % minuteInMillis;
		long elapsedSeconds = elapsedTime / secondInMillis;

		System.out.println("Elapsed time: " + elapsedHours + " hours, " +
				elapsedMinutes + " minutes, " + elapsedSeconds + " seconds.");
		
		
		//task 2
		start = new Date();
		GS.task2(fileName + "2", fileExtension);
		System.out.println("Done with task 2.");
		stop = new Date();
		
		elapsedTime = stop.getTime() - start.getTime();
		secondInMillis = 1000;
		minuteInMillis = secondInMillis * 60;
		hourInMillis = minuteInMillis * 60;

		elapsedHours = elapsedTime / hourInMillis;
		elapsedTime = elapsedTime % hourInMillis;
		elapsedMinutes = elapsedTime / minuteInMillis;
		elapsedTime = elapsedTime % minuteInMillis;
		elapsedSeconds = elapsedTime / secondInMillis;

		System.out.println("Elapsed time: " + elapsedHours + " hours, " +
				elapsedMinutes + " minutes, " + elapsedSeconds + " seconds.");*/
	}
	
	void task1(String fileName, String fileExtension) {
		try {
			PrintStream out = new PrintStream(new FileOutputStream(fileName + fileExtension));

			out.println(",,,,,Euclidean,,,Max,,,,,,Periodic_Euclidean,,,,,,Periodic_Max,,,,,");
			out.println("Num verts,Metric,H,alpha,beta,bias,nbhd_size_lower_estimate,nbhd_size_upper_estimate,bias,nbhd_size_lower_estimate,nbhd_size_upper_estimate,bias_for_this_metric/bias_for_Euclidean,nbhd_size_lower_for_this_metric/nbhd_size_lower_for_Euclidean,nbhd_size_upper_for_this_metric/nbhd_size_upper_for_Euclidean,bias,nbhd_size_lower_estimate,nbhd_size_upper_estimate,bias_for_this_metric/bias_for_Euclidean,nbhd_size_lower_for_this_metric/nbhd_size_lower_for_Euclidean,nbhd_size_upper_for_this_metric/nbhd_size_upper_for_Euclidean,bias,nbhd_size_lower_estimate,nbhd_size_upper_estimate,bias_for_this_metric/bias_for_Euclidean,nbhd_size_lower_for_this_metric/nbhd_size_lower_for_Euclidean,nbhd_size_upper_for_this_metric/nbhd_size_upper_for_Euclidean");
			
			Random rand = new Random();
			for(int i = 0; i < 65000; i++) {
				String outString = "";
				PoissonSpatialGraph sg = new PoissonSpatialGraph(rand.nextInt(76)+25, 1, Metric.EUCLIDEAN, rand.nextDouble()*.49+.01, rand.nextDouble()*.5+.5, rand.nextDouble()*.48+.01);
				sg.generateGraph();
				double euclideanBias = sg.getBias();
				double euclideanLower = sg.getNbhdSizeLowerEstimate();
				double euclideanUpper = sg.getNbhdSizeUpperEstimate();
				outString += sg.getGraphID() + "," + sg.getGraphBiasAndEstimates();
				sg.setMetricFunction(Metric.MAX);
				sg.calculateBiasAndEstimates();
				outString += "," + sg.getGraphBiasAndEstimates() + "," + sg.getBias()/euclideanBias + "," + sg.getNbhdSizeLowerEstimate()/euclideanLower + "," + sg.getNbhdSizeUpperEstimate()/euclideanUpper;
				sg.setMetricFunction(Metric.PERIODIC_EUCLIDEAN);
				sg.calculateBiasAndEstimates();
				outString += "," + sg.getGraphBiasAndEstimates() + "," + sg.getBias()/euclideanBias + "," + sg.getNbhdSizeLowerEstimate()/euclideanLower + "," + sg.getNbhdSizeUpperEstimate()/euclideanUpper;
				sg.setMetricFunction(Metric.PERIODIC_MAX);
				sg.calculateBiasAndEstimates();
				outString += "," + sg.getGraphBiasAndEstimates() + "," + sg.getBias()/euclideanBias + "," + sg.getNbhdSizeLowerEstimate()/euclideanLower + "," + sg.getNbhdSizeUpperEstimate()/euclideanUpper;
				out.println(outString);
			}
			
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	void task2(String fileName, String fileExtension) {
		try {
			for(int size = 2; size <= 10; size++) {
				PrintStream out = new PrintStream(
						new FileOutputStream(fileName + "_size" +
								Integer.toString(size) + fileExtension)
				);
				PrintStream outPeriodic = new PrintStream(
						new FileOutputStream(fileName + "periodic_size" +
								Integer.toString(size) + fileExtension)
						);
				out.println(",,,Euclidean,,,Max,,");
				out.println("Size,Rewire,1/size,bias,|1/size-bias|,bias*size,bias,|1/size-bias|,bias*size");
				outPeriodic.println(",,,Periodic_Euclidean,,,Periodic_Max,,");
				outPeriodic.println("Size,Rewire,1/size,bias,|1/size-bias|,bias*size,bias,|1/size-bias|,bias*size");
				for(double rewire = 0.01; rewire <= 0.1; rewire += 0.01) {
					for(int i = 0; i < 10; i++) {
						String outString = "";
						String outPeriodicString = "";
						SquareLatticeSpatialGraph sg = new SquareLatticeSpatialGraph(size, rewire, Metric.EUCLIDEAN);
						sg.generateGraph();
						outString += sg.getGraphID() + "," + sg.getGraphBiasAndEstimates();
						sg.setMetricFunction(Metric.MAX);
						sg.calculateBiasAndEstimates();
						outString += "," + sg.getGraphBiasAndEstimates();
						out.println(outString);
						
						
						sg.setMetricFunction(Metric.PERIODIC_EUCLIDEAN);
						sg.generatePeriodicGraph();
						outPeriodicString += sg.getGraphID() + "," + sg.getGraphBiasAndEstimates();
						sg.setMetricFunction(Metric.PERIODIC_MAX);
						sg.calculateBiasAndEstimates();
						outPeriodicString += "," + sg.getGraphBiasAndEstimates();
						outPeriodic.println(outPeriodicString);
					}
				}
				for(double rewire = 0.2; rewire <= 0.5; rewire += 0.1) {
					for(int i = 0; i < 10; i++) {
						String outString = "";
						String outPeriodicString = "";
						SquareLatticeSpatialGraph sg = new SquareLatticeSpatialGraph(size, rewire, Metric.EUCLIDEAN);
						sg.generateGraph();
						outString += sg.getGraphID() + "," + sg.getGraphBiasAndEstimates();
						sg.setMetricFunction(Metric.MAX);
						sg.calculateBiasAndEstimates();
						outString += "," + sg.getGraphBiasAndEstimates();
						out.println(outString);
						
						
						sg.setMetricFunction(Metric.PERIODIC_EUCLIDEAN);
						sg.generatePeriodicGraph();
						outPeriodicString += sg.getGraphID() + "," + sg.getGraphBiasAndEstimates();
						sg.setMetricFunction(Metric.PERIODIC_MAX);
						sg.calculateBiasAndEstimates();
						outPeriodicString += "," + sg.getGraphBiasAndEstimates();
						outPeriodic.println(outPeriodicString);
					}
				}
				out.close();
				outPeriodic.close();
			}
			
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
