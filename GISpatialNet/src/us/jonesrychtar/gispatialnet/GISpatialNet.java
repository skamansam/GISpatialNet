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
import us.jonesrychtar.gispatialnet.gui.GSNFrame;

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
	 * Constructor
	 */
	public GISpatialNet(){
    	DataSet theData=new DataSet();
    	theData.setX(MatrixFactory.emptyMatrix());
    	theData.setY(MatrixFactory.emptyMatrix());
    	theData.setAttb(MatrixFactory.emptyMatrix());
    	theData.setAdj(MatrixFactory.emptyMatrix());
    }
    
    /**
     * Constructor
     * @param XY initial X and Y matrix to use
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
     * Constructor
     * @param XY initial xy matrix to use
     * @param adj initial Adjacency matrix to use
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
     * Constructor
     * @param XY initial XY matrix to use
     * @param adj initial Adjacency matrix to use
     * @param attb initial Attribute matrix to use
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
     * Constructor
     * @param X Initial X matrix to use
     * @param Y Initial Y matrix to use
     * @param adj Initial Adjacency matrix to use
     * @param attb Initial Attribute matrix to use
     */
    public GISpatialNet(Matrix X, Matrix Y, Matrix adj, Matrix attb){
    	DataSet theData=new DataSet();
    	theData.setX(X);
    	theData.setY(Y);
    	theData.setAttb(attb);
    	theData.setAdj(adj);
    }
    /**
     * Set the XY matrix being used
     * @param Matrix Index of DataSet
     * @param X X matrix to use
     * @param Y Y matrix to use
     */
    public void setXY(int Matrix, Matrix X, Matrix Y){
    	theData.elementAt(Matrix).setX(X);
    	theData.elementAt(Matrix).setY(Y);
    }
    
    /**
     * Append XY data to current XY data
     * @param Matrix Index of DataSet to change
     * @param X X data to append
     * @param Y Y data to append
     */
    public void addXY(int Matrix, Matrix X, Matrix Y){
    	theData.elementAt(Matrix).addX(X);
    	theData.elementAt(Matrix).addY(Y);
    }
    
    /**
     * Get a dataset from data
     * @param Matrix Index of dataset
     * @return DataSet at indicated index from data
     */
    public DataSet getData(int Matrix){
    	return theData.elementAt(Matrix);
    }
    /**
     * Set a dataset
     * @param where Index of dataset to set
     * @param data DataSet to copy into storage
     */
    public void setData(int where, DataSet data){
        theData.elementAt(where).setX(data.getX());
        theData.elementAt(where).setY(data.getY());
        theData.elementAt(where).setAdj(data.getAdj());
        theData.elementAt(where).setAttb(data.getAttb());
        theData.elementAt(where).setFileList(data.GetLoadedFiles());
    }
    /**
     * 
     * @return Number of DataSets stored
     */
    public int NumberOfDataSets(){
        return theData.size();
    }
    /**
     *
     * @return All DataSets in storage
     */
    public Vector<DataSet> getDataSets(){
        return theData;
    }
    /**
     *
     * @param DataSet Index of dataset to remove from storage
     */
    public void Remove(int DataSet){
        theData.removeElementAt(DataSet);
    }
    /**
     * Adds a data set to list
     * @param ds Data to add
     */
    public void add(DataSet ds){
        theData.add(ds);
    }
    /**
     * Calls Reader.LoadFile(filename)
     * @param filename filename to load
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     */
    public void LoadFile(String filename) throws MalformedURLException, IOException{
    	Reader.LoadFile(filename);
    }
    /**
     * Prints all loaded data
     */
    public void printStatus(){
    	System.out.print(theData);
    }
    /**
     * @param Matrix Index of dataset to look at
     * @return Status of indicated dataset
     */
    public String toString(int Matrix){
    	return theData.elementAt(Matrix).toString();
    }

    /**
     * Temporarily sets the Detail level the given 
     * level and returns getStatus()
     * @param level integer value indicating level of detail needed
     * @return Status information see {@link getStatus()}
     */
    public String getStatus(int level){
    	int curLvl=this.debugLevel;		//store current level
        String out="";
        for(int i=0; i<this.NumberOfDataSets(); i++){
            this.setDebugLevel(i, level);		//set the new level
            out+="Data Set "+(i+1)+" :\n"+this.toString(i)+"\n";	//getStatus()
            theData.elementAt(i).setDetailLevel(curLvl);	//restore the level
        }
    	return out;
    }
        
    /**
     * Sets detail level
     * @param Matrix Index of dataset to set level on
     * @param i Detail level to use
     */
    public void setDebugLevel(int Matrix, int i){
    	debugLevel=i;
    	theData.elementAt(Matrix).setDetailLevel(i);
    }

    /**
     * @return Detail level being used
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
     * Adds an ego to a data set
     * @param Data Index of data set to add ego to
     */
    public void AddEgo(int Data){
        Matrix Xz = MatrixFactory.zeros(1,1);
        Matrix Yz = MatrixFactory.zeros(1,1);
        Matrix a = MatrixFactory.zeros(1, theData.elementAt(Data).getAdj().getColumnCount());
        Matrix a2 = MatrixFactory.zeros(theData.elementAt(Data).getAdj().getColumnCount(),1);
        Matrix attb = MatrixFactory.zeros(1, theData.elementAt(Data).getAttb().getColumnCount());

        //set a to all 1s
        for(int col=0; col< a.getColumnCount(); col++){
            a.setAsDouble(1, 0, col);
        }
        for(int row=0; row <a2.getRowCount(); row++){
            a2.setAsDouble(1,row,0);
        }

        //add data
        if(theData.get(Data).hasX())
            theData.get(Data).setX(theData.elementAt(Data).getX().appendVertically(Xz));
        if(theData.get(Data).hasY())
            theData.get(Data).setY(theData.elementAt(Data).getY().appendVertically(Yz));
        if(theData.get(Data).hasAttb())
            theData.get(Data).setAttb(theData.elementAt(Data).getAttb().appendVertically(attb));
        if(theData.get(Data).hasAdj()){
            theData.get(Data).setAdj(theData.elementAt(Data).getAdj().appendVertically(a));
            theData.get(Data).setAdj(theData.elementAt(Data).getAdj().appendHorizontally(a2));
        }
    }
	/**
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
        if((args.length < 1  || args.length > 5) || args[0].charAt(0) != '-'){
            PrintUsage();
            System.exit(0);
        }
        else{
            Getopt g = new Getopt("GISpatialNet", args, "cgsbeqhvlC:D:E:K:P:S:");
            int op;
            boolean inputTypeSet = false, 
            	outputTypeSet=false,
            	iscmdLine = false;
            int in=-1,out=-1,action=-1;
            String InDir=null, OutDir=null;
            while((op=g.getopt())!=-1){
                switch(op){
                    case 'c': cli.main(args); iscmdLine=true; break;
                    case 'g': System.err.println("WARNING: GISpatialNet GUI Not yet complete."); iscmdLine=false; break;
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
            if(!outputTypeSet && iscmdLine){
                OutDir = args[g.getOptind()];
            }
            if(iscmdLine)
            	CommandLineHelper.exec(action, in, out, InDir, OutDir);
            else
            	buildGUI();
        }
	}
	
	public static void buildGUI(){
		GSNFrame gf = new GSNFrame();
		gf.setVisible(true);
	}
	
    /**
     * Prints usage of command line functions
     */
    private static void PrintUsage(){
        System.out.println(
                "Usage: java -jar GISpatialNet.jar [option]\n" +
                "Usage: java -jar GISpatialNet.jar [option] [input file type] [input folder] [output folder] \n" +
                "Usage: java -jar GISPatialNet.jar [input file type] [input folder] [output file type] [output folder]\n"+
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
    /**
     * prints program License
     */
    private static void PrintLicense(){
        System.out.print(
    			"GISpatialNet is the brain child of Dr. Eric Jones and Jan Rychtar. " +
    			"It was programmed by " +
    			"Robert Gove, Charles Bevan, and Samuel Tyler. Collaborators include " +
    			"Martin Smith and Christopher Nicholson.\n\n" +
    			"You can find more information about GISPatialNet at its homepage, " +
    			"http://sourceforge.net/apps/trac/spatialnet/\n\n" +
    			"This software is governed under the GNU Greater Public License, Version " +
    			"2 (GPLv2). If you have not obtained the LGPL with this software, " +
    			"you can obtain it from http://www.gnu.org/licenses/gpl.html. GISpatialNet uses software governed under the GPL," +
    			"LGPL, Apache License, New BSD License\n");
    }
}
