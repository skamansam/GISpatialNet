/**
 *
 */
package us.jonesrychtar.gispatialnet;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Vector;

import jxl.write.WriteException;

import org.boehn.kmlframework.kml.KmlException;
import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.calculation.Calculation;

import us.jonesrychtar.gispatialnet.*;

import us.jonesrychtar.gispatialnet.Reader.CSVFileReader;
import us.jonesrychtar.gispatialnet.Reader.DLreader;
import us.jonesrychtar.gispatialnet.Reader.ExcelReader;
import us.jonesrychtar.gispatialnet.Reader.KMLreader;
import us.jonesrychtar.gispatialnet.Reader.PajekReader;
import us.jonesrychtar.gispatialnet.Reader.ShapeFileReader;
import us.jonesrychtar.gispatialnet.Writer.CSVwriter;
import us.jonesrychtar.gispatialnet.Writer.DLwriter;
import us.jonesrychtar.gispatialnet.Writer.ExcelWriter;
import us.jonesrychtar.gispatialnet.Writer.KMLwriter;
import us.jonesrychtar.gispatialnet.Writer.PajekWriter;

/**
 * @author sam
 * This class holds all the relavent functions for GISpatialNet, including
 * all the matrices. Most of this was taken from the util class from CVS
 * revision 37. 
 */
public class GISpatialNet {

	DataSet theData=new DataSet();
	int debugLevel=0;
    
	/**
	 * 
	 */
	public GISpatialNet(){
    	DataSet theData=new DataSet();
    	theData.setX(MatrixFactory.emptyMatrix());
    	theData.setY(MatrixFactory.emptyMatrix());
    	theData.setAttb(MatrixFactory.emptyMatrix());
    	theData.setAdj(MatrixFactory.emptyMatrix());
    }
    
	public GISpatialNet(Matrix XY){
    	DataSet theData=new DataSet();
    	Matrix[] xy=util.SplitXYAttb(XY);
    	theData.setX(xy[0]);
    	theData.setY(xy[1]);
    	theData.setAttb(MatrixFactory.emptyMatrix());
    	theData.setAdj(MatrixFactory.emptyMatrix());
    }
	public GISpatialNet(Matrix XY, Matrix adj){
    	DataSet theData=new DataSet();
    	Matrix[] xy=util.SplitXYAttb(XY);
    	theData.setX(xy[0]);
    	theData.setY(xy[1]);
    	theData.setAttb(MatrixFactory.emptyMatrix());
    	theData.setAdj(adj);
    }
	public GISpatialNet(Matrix XY, Matrix adj, Matrix attb){
    	DataSet theData=new DataSet();
    	Matrix[] xy=util.SplitXYAttb(XY);
    	theData.setX(xy[0]);
    	theData.setY(xy[1]);
    	theData.setAttb(attb);
    	theData.setAdj(adj);
    }
	
	public GISpatialNet(Matrix X, Matrix Y, Matrix adj, Matrix attb){
    	DataSet theData=new DataSet();
    	theData.setX(X);
    	theData.setY(Y);
    	theData.setAttb(attb);
    	theData.setAdj(adj);
    }
    /**
     * @param X
     * @param Y
     */
    public void setXY(Matrix X, Matrix Y){
    	theData.setX(MatrixFactory.emptyMatrix());
    	theData.setY(MatrixFactory.emptyMatrix());    	
    }
    
    public void addXY(Matrix X, Matrix Y){
    	theData.addX(X);
    	theData.addY(Y);    	
    }
    
    /**
     * @return
     */
    public DataSet getData(){
    	return theData;
    }

    public boolean LoadFile(String filename){
    	theData.LoadFile(filename);
    }
    /**
     * 
     */
    public void printStatus(){
    	System.out.print(theData);
    }
    /**
     * @return
     */
    public String getStatus(){
    	return theData.toString();
    }
    
    /**
     * @param i
     */
    public void setDebugLevel(int i){
    	debugLevel=i;
    	theData.setDetailLevel(i);
    }

    /**
     * @return
     */
    public int getDebugLevel(){
    	return debugLevel;
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
