/**
 *
 */
package us.jonesrychtar.gispatialnet;

import java.io.IOException;
import java.net.MalformedURLException;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;

/**
 * @author sam
 * This class holds all the relevant functions for GISpatialNet, including
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

    public void LoadFile(String filename) throws MalformedURLException, IOException{
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
     * Temporarily sets the Detail level the given 
     * level and returns getStatus()
     * @return Status information see {@link getStatus()}
     */
    public String getStatus(int level){
    	int curLvl=this.debugLevel;		//store current level
    	this.setDebugLevel(level);		//set the new level
    	String out=this.getStatus();	//getStatus()
    	theData.setDetailLevel(curLvl);	//restore the level
    	return out;
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
     * "Router" function. Calls ClearData() on the current DataSet.
     */
    public void ClearData(){theData.ClearData();}
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
