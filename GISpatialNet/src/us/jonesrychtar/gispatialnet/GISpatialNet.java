/**
 *
 */
package us.jonesrychtar.gispatialnet;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.Vector;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import us.jonesrychtar.gispatialnet.Reader.Reader;

/**
 * @author sam, cfbevan
 * This class holds all the relevant functions for GISpatialNet, including
 * all the matrices. Most of this was taken from the util class from CVS
 * revision 37. 
 */
public class GISpatialNet {

	//DataSet theData=new DataSet();
    //TODO: rewrite to allow for multiple data sets
    Vector<DataSet> theData = new Vector<DataSet>();
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
    	Reader.LoadFile(filename);
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
    public void ClearData(){
        for(int i=0; i<theData.size(); i++){
            theData.elementAt(i).ClearData();
        }
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        if(args.length < 1 || args[0].charAt(0) != '-'){
            PrintUsage();
            System.exit(0);
        }
        else{
            char op = args[0].charAt(1);
            switch(op){
                case 'c': cli.main(args);
                break;
                case 'g': break;
                case 's': break;
                case 'h': PrintUsage(); break;
                case 'v': System.out.println("0.0.01a PreAlpha Prototype \n"); break;
                case 'l': PrintLicense(); break;
                default: System.out.println("Invalid input."); PrintUsage(); break;
            }
        }
	}
    private static void PrintUsage(){
        System.out.println(
                "Useage: java -jar GISpatialNet.jar [option] <arguments> \n" +
                "Options   Argumets           Operation\n" +
                "---------------------------------------------------------------------------------\n" +
                "-c        no arguments       Starts command line interface.\n" +
                "-g        no arguments       Starts graphical user interface. \n" +
                "-s        file1 file2 ...    Performs spatial net bias on listed files.\n"+
                "-v        no arguments       Prints version number.\n" +
                "-l        no arguments       Prints license information. \n" +
                "-h        no arguments       Prints help (this text).\n");
    }
    private static void PrintLicense(){
        System.out.print(
    			"GISpatialNet is the brain child of Dr. Eric Jones and Jan Rychtar. " +
    			"It was programmed by " +
    			"Robert Gove, Charles Bevan, and Samuel Tyler. Collaborators include " +
    			"Martin Smith and Christopher Nicholson.\n\n" +
    			"You can find more information about GISPatialNet at its homepage, " +
    			"http://sourceforge.net/apps/trac/spatialnet/\n\n" +
    			"This software is governed under the GNU Greater Public Liscence, Version " +
    			"2 (GPLv2). If you have not obtained the LGPL with this software, " +
    			"you can obtain it from http://www.gnu.org/licenses/gpl.html. GISpatialNet uses software governed under the GPL," +
    			"LGPL, Apache License, New BSD License\n");
    }

}
