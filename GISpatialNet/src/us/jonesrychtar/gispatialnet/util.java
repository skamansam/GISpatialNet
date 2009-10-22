/*
 * This is the main program. It coordinates all other classes
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 *
 */
package us.jonesrychtar.gispatialnet;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;
import javax.naming.OperationNotSupportedException;
import jxl.write.WriteException;
import org.boehn.kmlframework.kml.KmlException;
import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.calculation.Calculation;
import us.jonesrychtar.gispatialnet.Writer.*;
import us.jonesrychtar.gispatialnet.Reader.*;

/**
 *
 * @author Sam Tyler, Charles Bevan
 * @date September 17, 2009
 * @version 0.0.1
 */
public class util {

    //Matrix data
    //use 'MatrixFactory.emptyMatrix()'
    private Matrix x = MatrixFactory.emptyMatrix(); //vector matrix (1 col) of x coordinates
    private Matrix y = MatrixFactory.emptyMatrix(); //vector matrix of y coordinates
    private Matrix adj = MatrixFactory.emptyMatrix(); //matrix of size x by y where if ij >= 1 there is a line connecting (xi,yi) to (xj,yj). Always stored as FULL MATRIX
    private Matrix attb = MatrixFactory.emptyMatrix(); //attributes for node (xi,yi) where i is the row of attb

    private Vector<String> loadedFiles = new Vector<String>();

    //loading functions-----------------------------------------------------------------------------------
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
        DataMerger m = new DataMerger(combine(combine(x,y),attb), adj, combine(combine(temp[0], temp[1]), temp[3]), temp[2]);
        Matrix temp1[] = m.Merge(MergeOn);
        Matrix temp2[] = _splitXYAttb(temp1[0]);
        adj = temp1[1];
        x = temp2[0];
        y = temp2[1];
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
        DataMerger m = new DataMerger(combine(combine(x,y),attb), adj, combine(combine(temp[0], temp[1]), temp[3]), temp[2]);
        Matrix temp1[] = m.Merge(MergeOn);
        Matrix temp2[] = _splitXYAttb(temp1[0]);
        adj = temp1[1];
        x = temp2[0];
        y = temp2[1];
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
    public void loadExcel(String filename, int Matrix, int MatrixType) throws Exception{
        ExcelReader er = new ExcelReader(filename);
        Matrix temp = er.read();

        loadedFiles.add(filename);
        switch(Matrix){
            case 0: {
                Matrix[] t2 = _splitXYAttb(temp);
                x = t2[0];
                y = t2[1];
                adj = t2[2];
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
     * Load data from Excel file into mamory
     * @param filename file to load
     * @param Matrix Matrix to load into
     * @param MatrixType Format of file
     * @param MergeOn Array containing index of columns to match during merge
     * @throws java.lang.Exception
     */
    public void loadExcel(String filename, int Matrix, int MatrixType, int[] MergeOn) throws Exception{
        ExcelReader er = new ExcelReader(filename);
        Matrix temp = er.read();
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
                x = temp.selectColumns(Calculation.Ret.NEW, 0);
                y = temp.selectColumns(Calculation.Ret.NEW, 1);
                break;
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
                Matrix[] t2 = _splitXYAttb(temp);
                x = t2[0];
                y = t2[1];
                adj = t2[2];
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
        new KMLwriter(combine(combine(x,y),attb),filename).WriteFile();
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
        new ExcelWriter(combine(combine(x,y),attb), filenameNodes).WriteFile();
        new ExcelWriter(adj, filenameArcs).WriteFile();
    }
    /**
     * Saves in a seperated value format (.csv, .txt)
     * @param filenameNodes name of output file for nodes without extension
     * @param filenameArcs name of output file for edges without extension
     * @param seperator character that seperates values
     */
    public void saveCSV(String filenameNodes, String filenameArcs, char seperator) throws FileNotFoundException{
        new CSVwriter(combine(combine(x,y),attb), filenameNodes, seperator).WriteFile();
        new CSVwriter(adj, filenameArcs, seperator).WriteFile();
    }
   
    //analyzing functions---------------------------------------------------------------------------------
    /**
     * Performes a borders analysis on data, writes out edge shapefile
     * @param alg Algorithm to use
     * Options: 0 - default Borders algorithm
     */
    public void Border(String filename, int alg) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{
        Borders b = new Borders(filename, x,y,adj,alg);
        b.Write();
    }
    /**
     * Performs qap analysis on data
     * @param arg Examples:
     *  	Simple Mantel test: -s file1 file2 number_of_randomizations
			Partial Mantel test: -p file1 file2 file3 number_of_randomizations
			Options:
			-r partial Mantel test with raw option
			-e force exact permutations procedure
			-l print licence terms
			-h display help
     */
    public void QAP(String arg[]){
        qap q = new qap (arg.length-2, arg);
    }
    /**
     * Highlighrs edges and saves edge shapefile
     * @param alg Which algorithm to use
     * @param filename output filename
     * Algorithms:  0 less than average length
     *              1 less than median length
     *              2 more than median length
     *              3 top 10 percent
     */
    public void Highlight(int alg, String filename, String nodeFilename) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{
        ShapefileNodeWriter sfnw = new ShapefileNodeWriter(nodeFilename, x,y);
        sfnw.write();
        HighlightEdges h = new HighlightEdges(filename, x,y,adj,alg);
        h.write();
    }
    /**
     * Not supported yet
     * @throws javax.naming.OperationNotSupportedException
     */
    public void SNB() throws OperationNotSupportedException{
        //TODO: write SNB (this is robert's function)
        throw new OperationNotSupportedException("function not done yet");
    }
    //Matrix conversion functions-------------------------------------------------------------------------
    /**
     * Translates the stored xy to a new xy
     * @param xmove amount to move in x direction
     * @param ymove amount to move in y direction
     */
    public void translate(double xmove, double ymove)throws IllegalStateException{
        if(HasData(0) && HasData(1)){
            MatrixConversion mc = new MatrixConversion();
            Matrix temp = combine(x,y);
            temp = mc.Translation(temp, xmove, ymove);
            x = temp.selectColumns(Calculation.Ret.NEW, 0);
            y = temp.selectColumns(Calculation.Ret.NEW, 1);
        }
        else throw new IllegalStateException("no XY data loaded");
    }
    /**
     * Reflects the xy data over an axis
     * @param Axis Axis to reflect over (X=0, Y=1)
     * @throws java.lang.IllegalStateException
     */
    public void reflect(int Axis)throws IllegalStateException{
        //x=0 y=1
        if(HasData(0) && HasData(1)){
            MatrixConversion mc = new MatrixConversion();
            Matrix temp = combine(x,y);
            temp = mc.Reflection(temp, Axis);
            x = temp.selectColumns(Calculation.Ret.NEW, 0);
            y = temp.selectColumns(Calculation.Ret.NEW, 1);
        } else throw new IllegalStateException("no XY data loaded");
    }
    /**
     * Rotates the xy data about the origin
     * @param Degrees Degrees to rotate
     * @throws java.lang.IllegalStateException
     */
    public void rotate(double Degrees)throws IllegalStateException{
        if(HasData(0) && HasData(1)){
        MatrixConversion mc = new MatrixConversion();
        Matrix temp = combine(x,y);
        temp = mc.RotateClockwise(temp, Degrees);
        x = temp.selectColumns(Calculation.Ret.NEW, 0);
        y = temp.selectColumns(Calculation.Ret.NEW, 1);
        } else throw new IllegalStateException("no XY data loaded");
    }
    /**
     * Scales the xy data
     * @param factor Factor to scale by
     * @throws java.lang.IllegalStateException
     */
    public void scale(double factor)throws IllegalStateException{
        if(HasData(0) && HasData(1)){
            MatrixConversion mc = new MatrixConversion();
            Matrix temp = combine(x,y);
            temp = mc.Scale(temp, factor);
            x = temp.selectColumns(Calculation.Ret.NEW, 0);
            y = temp.selectColumns(Calculation.Ret.NEW, 1);
        } else throw new IllegalStateException("no XY data loaded");
    }

    //extra functions-------------------------------------------------------------------------------------
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
     * Combines Matrix A and B
     * @param a Matrix to append to
     * @param b Matrix to append
     * @return B appended to A horizontallly
     * @throws java.lang.IllegalArgumentException
     */
    public Matrix combine(Matrix a, Matrix b) throws IllegalArgumentException{
        if(a.getRowCount() == b.getRowCount()){
           Matrix temp = a.appendHorizontally(b);
           //set headers
           int col=0;
           while(col<a.getColumnCount()){
               temp.setColumnLabel(col, a.getColumnLabel(col));
               col++;
           }
           while(col< a.getColumnCount()+b.getColumnCount()){
               temp.setColumnLabel(col, b.getColumnLabel(col-a.getColumnCount()));
               col++;
           }
            return temp;
        }else
            throw new IllegalArgumentException("Matrix Sizes do not match.");
    }
    /**
     * Splits a big matrix into 3 smaller matricies
     * @param in matrix containing x, y, and attb
     * @return matrix array where out[0] = x, out[1] =y , and out[2] = attb
     */
    private Matrix[] _splitXYAttb(Matrix in){
        Matrix[] out = new Matrix[3];
        //copy all rows of col 0 to out[0]
        out[0] = in.selectColumns(Calculation.Ret.NEW, 0);
        //copy all rows of col 1 to out[1]
        out[1] = in.selectColumns(Calculation.Ret.NEW, 1);
        //copy rest to out[2]
        out[2] = in.selectColumns(Calculation.Ret.NEW, 2, in.getColumnCount());
        return out;
    }
    /**
     * Adds a numbered column to the front of the matrix
     * @param in Matrix to add number column to
     * @return Original matrix with numbered column added to front
     */
    public Matrix addNumberCol(Matrix in){
        Matrix numCol = MatrixFactory.zeros(org.ujmp.core.enums.ValueType.INT, in.getRowCount(), 1);
        for(int row=0; row<numCol.getRowCount(); row++){
            numCol.setAsInt(row+1, row, 0);
        }
        numCol.setColumnLabel(0, "id");
        return combine(numCol,in);
    }

    //funcrtion to test if matricies already have data
    /**
     *
     * @param Matrix number corresponding to Matrix to check: 0 = x, 1 = y, 2 = Adj, 3 = Attb
     * @return True if Matrix has data, false otherwise;
     */
    public boolean HasData(int Matrix){
        /*
         * X = 0
         * Y = 1
         * Adj = 2
         * Attb = 3
         * */
        //testing
        boolean empty = x.isEmpty();
        //testing

        switch(Matrix){
            case 0: return !(x.isEmpty());
            case 1: return !(y.isEmpty());
            case 2: return !(adj.isEmpty());
            case 3: return !(attb.isEmpty());
            default: return false;
        }
    }
}
