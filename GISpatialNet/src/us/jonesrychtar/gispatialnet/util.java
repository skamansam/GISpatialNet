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
import javax.naming.OperationNotSupportedException;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.calculation.Calculation;
//TODO: remove EmptyMatrix - it won't compile on all platforms. 
//TODO: replace 'new EmptyMatrix()' with 'MatrixFactory.emptyMatrix()'
//import org.ujmp.core.objectmatrix.EmptyMatrix;
import us.jonesrychtar.gispatialnet.Writer.*;
import us.jonesrychtar.gispatialnet.Reader.*;

/**
 *
 * @author Sam Tyler, Charles Bevan
 * @date September 17, 2009
 * @version 0.0.1
 */
public class util {

    private Matrix x = MatrixFactory.emptyMatrix(); //vector matrix (1 col) of x coordinates
    private Matrix y = MatrixFactory.emptyMatrix(); //vector matrix of y coordinates
    private Matrix adj = MatrixFactory.emptyMatrix(); //matrix of size x by y where if ij >= 1 there is a line connecting (xi,yi) to (xj,yj)
    private Matrix attb = MatrixFactory.emptyMatrix(); //attributes for node (xi,yi) where i is the row of attb

    /*private Matrix x = new EmptyMatrix(); //vector matrix (1 col) of x coordinates
    private Matrix y = new EmptyMatrix(); //vector matrix of y coordinates
    private Matrix adj = new EmptyMatrix(); //matrix of size x by y where if ij >= 1 there is a line connecting (xi,yi) to (xj,yj)
    private Matrix attb = new EmptyMatrix(); //attributes for node (xi,yi) where i is the row of attb
     */
    private String[] loadedFiles = new String[]{};

    //loading functions

       /* FileType:
     * 0 Shapefile
     * 1 Shapefile Unknown XY
     * 2 Google Earth (.kml)
     * 3 Pajek (.net)
     * 4 DL/UCINET (.txt, .dat)
     * 5 Excel (.csv, .xls)
     * 6 txt file (.txt)
     *
     * Matrix:
     * 0 XYAttb
     * 1 Adj
     * 2 X
     * 3 Y
     * 4 Attb
     *
     * MatrixType
     * 0 Full Matrix
     * 1 Lower Matrix
     * 2 Upper Matrix
     * ...
     * */
        
    //saving functions
    /**
     * Saves to 2 shapefiles, one with nodes, one with edges
     */
    public void saveShapefile(String Edgefilename, String Nodefilename){
            if (attb != null) {
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
    public void saveShapefileUnknown(String Edgefilename, String Nodefilename, int alg, int Height, int Width){
        Dimension temp = new Dimension(Height,Width);
        new convertUnknown(Edgefilename, Nodefilename,x,y, alg , temp);
    }
    /**
     * Saves to Google Earth kml format
     * @param filename Name of output file without extension
     */
    public void saveGoogleEarth(String filename){
        new KMLwriter(combineXYAttb(),filename);
    }
    /**
     * Saves to PAjek .net format
     * @param filename name of output file without extension
     */
    public void savePajek(String filename){
        new PajekWriter(x.appendHorizontally(y), adj, filename).WriteFile();
    }
    /**
     * Saves to DL/UCINET format
     * @param filename name of output file without extension
     * @param ext extension (0:.dat, 1:.txt)
     */
    public void saveDL(String filename, int ext){
        new DLwriter(adj,filename,ext).WriteFile();
    }
    /**
     * Saves to excel .xls format
     * @param filenameNodes name of output file for nodes without extension
     * @param filenameArcs name of output file for edges without extension
     */
    public void saveExcel(String filenameNodes, String filenameArcs){
        new ExcelWriter(combineXYAttb(), filenameNodes).WriteFile();
        new ExcelWriter(adj, filenameArcs).WriteFile();
    }
    /**
     * Saves in a seperated value format (.csv, .txt)
     * @param filenameNodes name of output file for nodes without extension
     * @param filenameArcs name of output file for edges without extension
     * @param seperator character that seperates values
     */
    public void saveCSV(String filenameNodes, String filenameArcs, char seperator){
        new CSVwriter(combineXYAttb(), filenameNodes, seperator);
        new CSVwriter(adj, filenameArcs, seperator);
    }
   
    //analyzing functions
    /**
     * Performes a borders analysis on data, writes out edge shapefile
     * @param alg Algorithm to use
     * Options: 0 - default Borders algorithm
     */
    public void Border(String filename, int alg){
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
    public void Highlight(int alg, String filename){
        HighlightEdges h = new HighlightEdges(filename, x,y,adj,alg);
        h.write();
    }
    /**
     * Not supported yet
     * @throws javax.naming.OperationNotSupportedException
     */
    public void SNB() throws OperationNotSupportedException{
        throw new OperationNotSupportedException("function not done yet");
    }

    //extra functions
    /**
     * Displays detail of loaded files
     * @param Detail level of detail to be recorded (the higher the number, the more detail)
     * @return String containing detail information
     */
    public String Status(int Detail){
        String out= "Loaded Files: ";

        //add loaded files
        if(loadedFiles.length == 0){
            out+=" NO FILES LOADED";
        }
        else
            for(int i=0; i< loadedFiles.length; i++){
                out+=loadedFiles[i]+" ";
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
            out+="X: ["+y.getRowCount()+","+y.getColumnCount()+"] \n";
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
            out+="X: ["+adj.getRowCount()+","+adj.getColumnCount()+"] \n";
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
            out+="X: ["+attb.getRowCount()+","+attb.getColumnCount()+"] \n";
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

        loadedFiles = new String[]{};

    }
    /**
     * Combines x y and attb into one matrix
     * @return a single matrix made from x, y, and attb
     * @throws java.lang.IllegalArgumentException
     */
    public Matrix combineXYAttb() throws IllegalArgumentException{
       if(x.getRowCount() == y.getRowCount() && y.getRowCount() == attb.getRowCount())
            return x.appendHorizontally(y).appendHorizontally(attb);
       else
           throw new IllegalArgumentException("Matrix Sizes do not match.");
    }
    /**
     * Splits a big matrix into 3 smaller matricies
     * @param in matrix containing x, y, and attb
     * @return matrix array where out[0] = x, out[1] =y , and out[2] = attb
     */
    public Matrix[] splitXYAttb(Matrix in){
        Matrix[] out = new Matrix[3];
        //copy all rows of col 0 to out[0]
        out[0] = in.selectColumns(Calculation.Ret.NEW, 0);
        //copy all rows of col 1 to out[1]
        out[1] = in.selectColumns(Calculation.Ret.NEW, 1);
        //copy rest to out[2]
        out[2] = in.selectColumns(Calculation.Ret.NEW, 2, in.getColumnCount());
        return out;
    }
}
