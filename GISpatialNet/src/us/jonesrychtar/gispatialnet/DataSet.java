/**
 * 
 */
package us.jonesrychtar.gispatialnet;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;
import java.util.regex.*;

import jxl.write.WriteException;

import org.boehn.kmlframework.kml.KmlException;
import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.calculation.Calculation;

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
 * @author sam,charles
 * This class holds the primary data for GISpatialNet. Code is pulled from 
 * the util class, because these are not utility functions, they are functions
 * for manipulating the data we have. Also, this allows for calling 
 * GISPatialNet gsn = new GISPatialNet;
 * if ((gsn.getData()).loadCSV("data.csv")){
 * 		//do something with data
 * }
 *
 */
public class DataSet {

	private Matrix x = MatrixFactory.emptyMatrix(); //vector matrix (1 col) of x coordinates
    private Matrix y = MatrixFactory.emptyMatrix(); //vector matrix of y coordinates
    private Matrix adj = MatrixFactory.emptyMatrix(); //matrix of size x by y where if ij >= 1 there is a line connecting (xi,yi) to (xj,yj). Always stored as FULL MATRIX
    private Matrix attb = MatrixFactory.emptyMatrix(); //attributes for node (xi,yi) where i is the row of attb

    private Vector<String> loadedFiles = new Vector<String>();

    private int Detail=2;

    /**
	 * 
	 */
	public DataSet() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the x
	 */
	public Matrix getX() {return x;}

	/**
	 * @param x the x to set
	 */
	public void setX(Matrix x) {this.x = x;}
	public void addX(Matrix x) {this.x=util.combine(this.x, x);}

	/**
	 * @return the y
	 */
	public Matrix getY() {return y;}

	/**
	 * @param y the y to set
	 */
	public void setY(Matrix y) {this.y = y;}
	public void addY(Matrix y) {this.y=util.combine(this.y, y);}

	/**
	 * @return the y
	 */
	public Matrix getXY() {
		return util.combine(x, y);
	}

	/**
	 * @param y the y to set
	 */
	public void setXY(Matrix xy) {
		Matrix[] tmp=util.SplitXYAttb(xy);
		this.x = tmp[0];
		this.y = tmp[1];
	}

	/**
	 * @param x
	 * @param y
	 */
	public void setXY(Matrix x, Matrix y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @param x
	 * @param y
	 */
	public void addXY(Matrix x, Matrix y) {
		this.x = util.combine(this.x,x);
		this.y = util.combine(this.y,y);
	}
	public boolean hasXY(){return (!x.isEmpty() && !y.isEmpty());}

	/**
	 * @return the adj
	 */
	public Matrix getAdj() {return adj;}
	public Matrix getAdjacencyMatrix() {return adj;}

	/**
	 * @param adj the adj to set
	 */
	public void setAdj(Matrix adj) {this.adj = adj;}
	public void setAdjacencyMatrix(Matrix adj) {this.adj = adj;}
	

	/**
	 * @return the attb
	 */
	public Matrix getAttb() {return attb;}
	public Matrix getAttr() {return attb;}
	public Matrix getAttributeMatrix() {return attb;}

	/**
	 * @param attb the attb to set
	 */
	public void setAttb(Matrix attb) {this.attb = attb;}
	public void setAttr(Matrix attb) {this.attb = attb;}
	public void setAttributeMatrix(Matrix attb) {this.attb = attb;}

	/**
	 * @return the current detail level
	 */
	public int getDetailLevel() {return this.Detail;}
	/**
	 * @param d The level of detail you want to use when calling toString().
	 * The values are:
	 * 	1: Print all the matrix values.
	 * 	2: Also print the Matrix.toString() value
	 */
	public void setDetailLevel(int d) {this.Detail = d;}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String out=new String();
        if(!x.isEmpty()){
            out+="X: ["+x.getRowCount()+","+x.getColumnCount()+"] \n";
            if(Detail>=1){
                //print headers
                for(int i=0; i<x.getColumnCount(); i++)
                    out+=x.getColumnLabel(i)+" ";
                out+="\n";
            }
            if(Detail>=2){
                //print matrix
                out+= x.toString()+"\n";
            }
        }else{
        	out+="There are no X values loaded!\n";
        }
        //add y attributes
        if(!y.isEmpty()){
            out+="Y: ["+y.getRowCount()+","+y.getColumnCount()+"] \n";
            if(Detail>=1){
                //print headers
                for(int i=0; i<y.getColumnCount(); i++)
                    out+=y.getColumnLabel(i)+" ";
                out+="\n";
            }
            if(Detail>=2){
                //print matrix
                out+= y.toString()+"\n";
            }
        }else{
        	out+="There are no Y values loaded!\n";
        }
        //add adj attributes
        if(!adj.isEmpty()){
            out+="Edges: ["+adj.getRowCount()+","+adj.getColumnCount()+"] \n";
            if(Detail>=1){
                //print headers
                for(int i=0; i<adj.getColumnCount(); i++)
                    out+=adj.getColumnLabel(i)+" ";
                out+="\n";
            }
            if(Detail>=2){
                //print matrix
                out+= adj.toString()+"\n";
            }
        }else{
        	out+="There are no adjacency values loaded!\n";
        }
        //add attb attributes
        if(!attb.isEmpty()){
            out+="Attributes: ["+attb.getRowCount()+","+attb.getColumnCount()+"] \n";
            if(Detail>=1){
                //print headers
                for(int i=0; i<attb.getColumnCount(); i++)
                    out+=attb.getColumnLabel(i)+" ";
                out+="\n";
            }
            if(Detail>=2){
                //print matrix
                out+= attb.toString()+"\n";
            }
        }else{
        	out+="There are no attribute values loaded!\n";
        }

		return out;
	}

	/**
	 * @param filename
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void LoadFile(String filename) throws MalformedURLException, IOException{
		
	}
	
	/**
     * Loads data from a shapefile into memory
     * @param filenameN name of Node shapefile
     * @param filenameE name of edge shapefile
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     */
    public void loadShapefile(String filenameN, String filenameE) throws MalformedURLException, IOException  {
        ShapeFileReader sfr = new ShapeFileReader(filenameN, filenameE);
        Matrix temp[] = sfr.Read();

        loadedFiles.add(filenameE);
        loadedFiles.add(filenameN);

        x = temp[0];
        y = temp[1];
        adj = temp[2];
        attb = temp[3];
    }
    /**
     *Loads data from shapefile into memory
     * @param filenameN File name of node data
     * @param filenameE File name of edge data
     * @param MergeOn Array containing index of columns to match during merge
     * @throws java.lang.Exception
     */
    public void loadShapefile(String filenameN, String filenameE, int[] MergeOn) throws Exception {
        ShapeFileReader sfr = new ShapeFileReader(filenameN, filenameE);
        Matrix temp[] = sfr.Read();

        loadedFiles.add(filenameE);
        loadedFiles.add(filenameN);
        DataMerger m = new DataMerger(
        		util.combine(
        			util.combine(
        				x,
        				y
        			),
        			attb
        		), 
        		adj, 
        		util.combine(
        			util.combine(temp[0], temp[1]),
        			temp[3]
        		), 
        		temp[2]
        		);
        Matrix temp1[] = m.Merge(MergeOn);
        Matrix temp2[] = util.SplitXYAttb(temp1[0]);

        x = temp2[0];
        y = temp2[1];
        adj = temp1[1];
        attb = temp2[2];
    }
    /**
     * Loads data from a google earth file into memory
     * @param filename name of file to load
     * @throws java.lang.Exception
     */
    public void loadGoogleEarth(String filename) throws Exception {
        KMLreader kmlr = new KMLreader(filename);
        Matrix temp[] = kmlr.read();

        loadedFiles.add(filename);

        x = temp[0];
        y = temp[1];
        adj = temp[2];
        attb = temp[3];
    }
    /**
     * Loads data from a google earth file into memory
     * @param filename name of file to load
     * @param MergeOn Array containing index of columns to match during merge
     * @throws java.lang.Exception
     */
    public void loadGoogleEarth(String filename, int[] MergeOn) throws Exception{
        KMLreader kmlr = new KMLreader(filename);
        Matrix temp[] = kmlr.read();

        loadedFiles.add(filename);
        DataMerger m = new DataMerger(util.combine(util.combine(x,y),attb), adj, util.combine(util.combine(temp[0], temp[1]), temp[3]), temp[2]);
        Matrix temp1[] = m.Merge(MergeOn);
        Matrix temp2[] = util.SplitXYAttb(temp1[0]);
        x = temp2[0];
        y = temp2[1];
        adj = temp1[1];
        attb = temp2[2];
    }
    /*****************************************
     * Handled in Util:
     * Matrix to be loaded:
     * 0 XYAttb
     * 1 Adj
     * 2 XY
     * 3 Attb
     *
     * Handled in Reader:
     * MatrixType:
     * 0 Full Matrix
     * 1 Lower Matrix
     * 2 Upper Matrix
     * ***************************************
     * */
    
    /**
     * Load data from Excel file into memory
     * @param filename File to load
     * @param Matrix Which matrix to load into
     * @param MatrixType Format of data in file
     * @throws java.lang.Exception
     */
    public void loadExcel(String filename, int Matrix, int MatrixType, int row, int col) throws Exception{
        ExcelReader er = new ExcelReader(filename);
        Matrix temp = er.read(MatrixType, row, col);

        loadedFiles.add(filename);
        switch(Matrix){
            case 0: {
                Matrix[] t2 = util.SplitXYAttb(temp);
                x = t2[0];
                y = t2[1];
                attb = t2[2];
                break;
            }
            case 1:{
                adj = temp;
                break;
            }
            case 2:{
                x = temp.selectColumns(Calculation.Ret.NEW, 0);
                y = temp.selectColumns(Calculation.Ret.NEW, 1);
                break;
            }
            case 3:{
                attb = temp;
                break;
            }
        }
    }
    /**
     * Load data from Excel file into memory
     * @param filename file to load
     * @param Matrix Matrix to load into
     * @param MatrixType Format of file
     * @param MergeOn Array containing index of columns to match during merge
     * @throws java.lang.Exception
     */
    public void loadExcel(String filename, int Matrix, int MatrixType, int row, int col, int[] MergeOn) throws Exception{
        ExcelReader er = new ExcelReader(filename);
        Matrix temp = er.read(MatrixType, row, col);
        loadedFiles.add(filename);
        //Merge data not supported yet
    }
    /**
     * Loads Data from a Pajek file into memory
     * @param filename Name of file to load
     * @param Matrix Matrix to load data into
     * @param MatrixType Format of saved data
     * @param rows Number of rows in File
     * @param cols Number of cols in file
     * @throws java.lang.Exception
     */
    public void loadPajek(String filename, int Matrix, int MatrixType, int rows, int cols) throws Exception{
        PajekReader pr = new PajekReader(filename);
        Matrix temp = pr.Read(MatrixType, rows, cols);

        loadedFiles.add(filename);
        switch(Matrix){
            case 0: //no attb matrix in pajek
            {
                throw new IllegalArgumentException("No attributes in a Pajek file");
            }
            case 1:{
                adj = temp;
            }
            case 2:{
                x = temp.selectColumns(Calculation.Ret.NEW, 0);
                y = temp.selectColumns(Calculation.Ret.NEW, 1);
                break;
            }
            case 3:
            {
                throw new IllegalArgumentException("No attributes in a Pajek file");
            }
        }
    }
    /**
     * Loads Data from a Pajek file into memory
     * @param filename Name of file to load
     * @param Matrix Matrix to load data into
     * @param MatrixType Format of saved data
     * @param rows Number of rows in File
     * @param cols Number of cols in file
     * @param MergeOn Array containing index of columns to match during merge
     * @throws java.lang.Exception
     */
    public void loadPajek(String filename, int Matrix, int MatrixType, int rows, int cols, int[] MergeOn) throws Exception{
        PajekReader pr = new PajekReader(filename);
        Matrix temp = pr.Read(MatrixType, rows, cols);

        loadedFiles.add(filename);
        //merge not supported
    }
    /**
     * Loads data from a DL/UCINET file into memory
     * @param filename name of file to load
     * @param Matrix Matrix to load into
     * @param MatrixType Format of data (may be overwritten if defined in file)
     * @param rows number of rows (may be overwritten if defined in file)
     * @param col number of cols (may be overwritten if defined  in file)
     * @throws java.lang.Exception
     */
    public void loadDL(String filename, int Matrix, int MatrixType, int rows, int col) throws Exception{
        DLreader dlr = new DLreader(filename);
        Matrix temp = dlr.Read(MatrixType, rows, col);

        loadedFiles.add(filename);
        switch(Matrix){
            case 0:{
                throw new IllegalArgumentException("No attributes in a DL/UCINET file");
            }
            case 1:{
                adj = temp;
            }
            case 2:{
               throw new IllegalArgumentException("No coordinates in DL/UCINET file");
            }
            case 3:{
                throw new IllegalArgumentException("No attributes in a DL/UCINET file");
            }
        }
    }
    /**
      * Loads data from a DL/UCINET file into memory
     * @param filename name of file to load
     * @param Matrix Matrix to load into
     * @param MatrixType Format of data (may be overwritten if defined in file)
     * @param rows number of rows (may be overwritten if defined in file)
     * @param col number of cols (may be overwritten if defined  in file)
     * @param MergeOn Array containing index of columns to match during merge
     * @throws java.lang.Exception
     */
    public void loadDL(String filename, int Matrix, int MatrixType, int rows, int col, int[] MergeOn) throws Exception{
        DLreader dlr = new DLreader(filename);
        Matrix temp = dlr.Read(MatrixType, rows, col);

        loadedFiles.add(filename);
        //Merge not supported yet
    }
    /**
     * Loads data from a txt/csv file into memory
     * @param filename name of file to load
     * @param Matrix Matrix to load into
     * @param MatrixType Format of file
     * @param rows number of rows in file
     * @param col number of cols in file
     * @param sep Field Seperator character
     * @throws java.lang.Exception
     */
    public void loadTxt(String filename, int Matrix, int MatrixType, int rows, int col, char sep) throws Exception{
        CSVFileReader csvr = new CSVFileReader(filename);
        //cswvr.setSep(sep);
        Matrix temp = csvr.Read(MatrixType, rows, col);
        loadedFiles.add(filename);

        switch(Matrix){
            case 0: {
                Matrix[] t2 = util.SplitXYAttb(temp);
                x = t2[0];
                y = t2[1];
                attb = t2[2];
                break;
            }
            case 1:{
                adj=temp;
                break;
            }
            case 2:{
                x = temp.selectColumns(Calculation.Ret.NEW, 0);
                y = temp.selectColumns(Calculation.Ret.NEW, 1);
                break;
            }
            case 3:{
                attb = temp;
                break;
            }
        }
    }
    /**
     * Loads data from a txt/csv file into memory
     * @param filename name of file to load
     * @param Matrix Matrix to load into
     * @param MatrixType Format of file
     * @param rows number of rows in file
     * @param col number of cols in file
     * @param sep Field Seperator character
     * @param MergeOn Array containing index of columns to match during merge
     * @throws java.lang.Exception
     */
    public void loadTxt(String filename, int Matrix, int MatrixType, int rows, int col, char sep, int[] MergeOn) throws Exception{
        CSVFileReader csvr = new CSVFileReader(filename);
        //csvr.setSep(sep);
        Matrix temp = csvr.Read(MatrixType, rows, col);
        loadedFiles.add(filename);
    }
    //saving functions------------------------------------------------------------------------------------
    /**
     * Saves to 2 shapefiles, one with nodes, one with edges
     */
    public void saveShapefile(String Edgefilename, String Nodefilename) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{
            if (!(attb.isEmpty())) {
                new convertKnown(Edgefilename, Nodefilename, x, y, adj, attb);
            } else {
                new convertKnown(Edgefilename, Nodefilename, x, y, adj);
            }
    }
    /**
     * Saves to 2 shapefiles, one with nodes, one with edges
     * @param alg which layout algorithm to use
     * @param Height height of final map
     * @param Width width of final map
     */
    public void saveShapefileUnknown(String Edgefilename, String Nodefilename, int alg, int Height, int Width) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{
        Dimension temp = new Dimension(Height,Width);
        if(attb.isEmpty())
            new convertUnknown(Edgefilename, Nodefilename,adj, alg , temp);
        else
            new convertUnknown(Edgefilename, Nodefilename,adj,attb, alg , temp);
    }
    /**
     * Saves to Google Earth kml format
     * @param filename Name of output file without extension
     */
    public void saveGoogleEarth(String filename) throws KmlException, IOException{
        new KMLwriter(util.combine(util.combine(x,y),attb),filename).WriteFile();
    }
    /**
     * Saves to Pajek .net format
     * @param filename name of output file without extension
     */
    public void savePajek(String filename) throws FileNotFoundException{
        new PajekWriter(x.appendHorizontally(y), adj, filename).WriteFile();
    }
    /**
     * Saves to DL/UCINET format
     * @param filename name of output file without extension
     * @param ext extension (0:.dat, 1:.txt)
     */
    public void saveDL(String filename, int ext) throws FileNotFoundException{
        new DLwriter(adj,filename,ext).WriteFile();
    }
    /**
     * Saves to excel .xls format
     * @param filenameNodes name of output file for nodes without extension
     * @param filenameArcs name of output file for edges without extension
     */
    public void saveExcel(String filenameNodes, String filenameArcs) throws IOException, WriteException{
        new ExcelWriter(util.combine(util.combine(x,y),attb), filenameNodes).WriteFile();
        new ExcelWriter(adj, filenameArcs).WriteFile();
    }
    /**
     * Saves in a seperated value format (.csv, .txt)
     * @param filenameNodes name of output file for nodes without extension
     * @param filenameArcs name of output file for edges without extension
     * @param seperator character that seperates values
     */
    public void saveCSV(String filenameNodes, String filenameArcs, char seperator) throws FileNotFoundException{
        new CSVwriter(util.combine(util.combine(x,y),attb), filenameNodes, seperator).WriteFile();
        new CSVwriter(adj, filenameArcs, seperator).WriteFile();
    }

    /**
     * Clears all matricies and loaded filenames
     */
    public void ClearData(){
        x = MatrixFactory.emptyMatrix();
        y = MatrixFactory.emptyMatrix();
        adj = MatrixFactory.emptyMatrix();
        attb = MatrixFactory.emptyMatrix(); 

        loadedFiles.clear();

    }

    /**
     * Displays detail of loaded files
     * @param Detail level of detail to be recorded (the higher the number, the more detail)
     * @return String containing detail information
     */
    public String Status(int Detail){
        String out= "Loaded Files: ";

        //add loaded files
        if(loadedFiles.isEmpty()){
            out+=" NO FILES LOADED";
        }
        else
            for(int i=0; i< loadedFiles.size(); i++){
                out+=loadedFiles.get(i)+" ";
            }
            
        out+="\n";
        //add x attributes
        if(!x.isEmpty()){
            out+="X: ["+x.getRowCount()+","+x.getColumnCount()+"] \n";
            if(Detail>=1){
                //print headers
                for(int i=0; i<x.getColumnCount(); i++)
                    out+=x.getColumnLabel(i)+" ";
                out+="\n";
            }
            if(Detail>=2){
                //print matrix
                out+= x.toString()+"\n";
            }
        }
        //add y attributes
        if(!y.isEmpty()){
            out+="Y: ["+y.getRowCount()+","+y.getColumnCount()+"] \n";
            if(Detail>=1){
                //print headers
                for(int i=0; i<y.getColumnCount(); i++)
                    out+=y.getColumnLabel(i)+" ";
                out+="\n";
            }
            if(Detail>=2){
                //print matrix
                out+= y.toString()+"\n";
            }
        }
        //add adj attributes
        if(!adj.isEmpty()){
            out+="Edges: ["+adj.getRowCount()+","+adj.getColumnCount()+"] \n";
            if(Detail>=1){
                //print headers
                for(int i=0; i<adj.getColumnCount(); i++)
                    out+=adj.getColumnLabel(i)+" ";
                out+="\n";
            }
            if(Detail>=2){
                //print matrix
                out+= adj.toString()+"\n";
            }
        }
        //add attb attributes
        if(!attb.isEmpty()){
            out+="Attributes: ["+attb.getRowCount()+","+attb.getColumnCount()+"] \n";
            if(Detail>=1){
                //print headers
                for(int i=0; i<attb.getColumnCount(); i++)
                    out+=attb.getColumnLabel(i)+" ";
                out+="\n";
            }
            if(Detail>=2){
                //print matrix
                out+= attb.toString()+"\n";
            }
        }
        return out;
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
