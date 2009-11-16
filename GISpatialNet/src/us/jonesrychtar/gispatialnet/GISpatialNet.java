/**
 *
 */
package us.jonesrychtar.gispatialnet;

import gnu.getopt.Getopt;
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
    
    private Vector<DataSet> theData = new Vector<DataSet>();
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
    
    /**
     *
     * @param XY
     */
    public GISpatialNet(Matrix XY){
    	DataSet theData=new DataSet();
    	Matrix[] xy=util.SplitXYAttb(XY);
    	theData.setX(xy[0]);
    	theData.setY(xy[1]);
    	theData.setAttb(MatrixFactory.emptyMatrix());
    	theData.setAdj(MatrixFactory.emptyMatrix());
    }
    /**
     *
     * @param XY
     * @param adj
     */
    public GISpatialNet(Matrix XY, Matrix adj){
    	DataSet theData=new DataSet();
    	Matrix[] xy=util.SplitXYAttb(XY);
    	theData.setX(xy[0]);
    	theData.setY(xy[1]);
    	theData.setAttb(MatrixFactory.emptyMatrix());
    	theData.setAdj(adj);
    }
    /**
     *
     * @param XY
     * @param adj
     * @param attb
     */
    public GISpatialNet(Matrix XY, Matrix adj, Matrix attb){
    	DataSet theData=new DataSet();
    	Matrix[] xy=util.SplitXYAttb(XY);
    	theData.setX(xy[0]);
    	theData.setY(xy[1]);
    	theData.setAttb(attb);
    	theData.setAdj(adj);
    }
	
    /**
     *
     * @param X
     * @param Y
     * @param adj
     * @param attb
     */
    public GISpatialNet(Matrix X, Matrix Y, Matrix adj, Matrix attb){
    	DataSet theData=new DataSet();
    	theData.setX(X);
    	theData.setY(Y);
    	theData.setAttb(attb);
    	theData.setAdj(adj);
    }
    /**
     * @param Matrix
     * @param X
     * @param Y
     */
    public void setXY(int Matrix, Matrix X, Matrix Y){
    	theData.elementAt(Matrix).setX(X);
    	theData.elementAt(Matrix).setY(Y);
    }
    
    /**
     *
     * @param Matrix
     * @param X
     * @param Y
     */
    public void addXY(int Matrix, Matrix X, Matrix Y){
    	theData.elementAt(Matrix).addX(X);
    	theData.elementAt(Matrix).addY(Y);
    }
    
    /**
     * @param Matrix
     * @return
     */
    public DataSet getData(int Matrix){
    	return theData.elementAt(Matrix);
    }
    /**
     *
     * @param where
     * @param data
     */
    public void setData(int where, DataSet data){
        theData.elementAt(where).setX(data.getX());
        theData.elementAt(where).setY(data.getY());
        theData.elementAt(where).setAdj(data.getAdj());
        theData.elementAt(where).setAttb(data.getAttb());
    }
    /**
     * 
     * @return
     */
    public int NumberOfDataSets(){
        return theData.size();
    }
    /**
     *
     * @return
     */
    public Vector<DataSet> getDataSets(){
        return theData;
    }
    /**
     *
     * @param DataSet
     */
    public void Remove(int DataSet){
        theData.removeElementAt(DataSet);
    }
    /**
     *
     * @param filename
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     */
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
     * @param Matrix
     * @return
     */
    public String toString(int Matrix){
    	return theData.elementAt(Matrix).toString();
    }

    /**
     * Temporarily sets the Detail level the given 
     * level and returns getStatus()
     * @param level
     * @return Status information see {@link getStatus()}
     */
    public String getStatus(int level){
    	int curLvl=this.debugLevel;		//store current level
        String out="";
        for(int i=0; i<this.NumberOfDataSets(); i++){
            this.setDebugLevel(i, level);		//set the new level
            out+=this.toString(i)+"\n";	//getStatus()
            theData.elementAt(i).setDetailLevel(curLvl);	//restore the level
        }
    	return out;
    }
    
    /**
     * @param Matrix
     * @param i
     */
    public void setDebugLevel(int Matrix, int i){
    	debugLevel=i;
    	theData.elementAt(i).setDetailLevel(i);
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
     *
     * @param Data
     */
    public void AddEgo(int Data){
        Matrix Xz = MatrixFactory.zeros(1,1);
        Matrix Yz = MatrixFactory.zeros(1,1);
        Matrix a = MatrixFactory.zeros(1, theData.elementAt(Data).getAdj().getColumnCount());
        Matrix attb = MatrixFactory.zeros(1, theData.elementAt(Data).getAttb().getColumnCount());

        //set a to all 1s
        for(int col=0; col< a.getColumnCount(); col++)
            a.setAsDouble(1, 1,col);

        //add data
        DataSet ds = new DataSet(Xz,Yz,a,attb);
        theData.elementAt(Data).append(ds);
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        if((args.length !=2 && args.length!=3) || args[0].charAt(0) != '-'){
            PrintUsage();
            System.exit(0);
        }
        else{
            Getopt g = new Getopt("GISpatialNet", args, "cgsbeqhvlC:D:E:K:P:S:");
            int op;
            boolean inputTypeSet = false, outputTypeSet=false;
            int in=-1,out=-1,action=-1;
            String InDir=null, OutDir=null;
            while((op=g.getopt())!=-1){
                switch(op){
                    case 'c': cli.main(args); break;
                    case 'g': System.out.println("No gui yet."); break;
                    case 's': action = op; break;
                    case 'b': action = op; System.out.println("Borders not finished."); break;
                    case 'e': action = op; break;
                    case 'q': CommandLineHelper.qap(); break;
                    case 'h': PrintUsage(); break;
                    case 'v': System.out.println("0.0.01a PreAlpha Prototype \n"); break;
                    case 'l': PrintLicense(); break;
                    case 'C': 
                    case 'D':
                    case 'E':
                    case 'K':
                    case 'P':
                    case 'S': 
                        if(!inputTypeSet){
                            in=op;
                            InDir=g.getOptarg();
                            inputTypeSet = true;
                        }
                        else{
                            out=op;
                            OutDir=g.getOptarg();
                            outputTypeSet = true;
                        }
                        break;
                    default: System.out.println("Invalid input."); PrintUsage(); break;
                }
            }
            if(!outputTypeSet){
                OutDir = args[g.getOptind()];
            }
            CommandLineHelper.exec(action, in, op, InDir, OutDir);
        }
	}
    private static void PrintUsage(){
        System.out.println(
                "Usage: java -jar GISpatialNet.jar [option] [input file type]=[input folder] [output folder] \n" +
                "Usage: java -jar GISPatialNet.jar [input file type]=[input folder] [output file type]=[output folder]"+
                "Options   Operation\n" +
                "---------------------------------------------------------------------------------\n" +
                "-c        Starts command line interface. No files needed.\n" +
                "-g        Starts graphical user interface. No files needed. \n" +
                "-s        Performs spatial net bias on listed files.\n"+
                "-b        Performs borders analysis on listed files.\n" +
                "-e        Performs highlight edge analysis on listed files.\n" +
                "-q        Starts qap program.\n" +
                "-v        Prints version number.\n" +
                "-l        Prints license information. \n" +
                "-h        Prints help (this text).\n\n"+
                "File Types\n"+
                "----------------------------------------------------------------------------------\n" +
                "-C        CSV files\n" +
                "-D        DL/UCINET format\n" +
                "-E        Excel 98 file format\n" +
                "-K        KML (google earth) format\n" +
                "-P        Pajek file format\n" +
                "-S        GIS Shapefile format\n");
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
