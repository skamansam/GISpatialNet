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
import org.ujmp.core.calculation.Calculation;
import org.ujmp.core.objectmatrix.EmptyMatrix;
import us.jonesrychtar.gispatialnet.Writer.*;
import us.jonesrychtar.gispatialnet.Reader.*;

/**
 *
 * @author Sam Tyler, Charles Bevan
 * @date September 17, 2009
 * @version 0.0.1
 */
public class util {

    /**
     * @param args the command line arguments
     */
    private Matrix x = new EmptyMatrix(); //vector matrix (1 col) of x coordinates
    private Matrix y = new EmptyMatrix(); //vector matrix of y coordinates
    private Matrix adj = new EmptyMatrix(); //matrix of size x by y where if ij >= 1 there is a line connecting (xi,yi) to (xj,yj)
    private Matrix attb = new EmptyMatrix(); //attributes for node (xi,yi) where i is the row of attb

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
    //TODO: comment functions
    public void saveShapefile(){
        if (attb != null) {
            new convertKnown(x, y, adj, attb);
        } else {
            new convertKnown(x, y, adj);
        }
    }
    public void saveShapefileUnknown(int alg, int Height, int Width){
        Dimension temp = new Dimension(Height,Width);
        new convertUnknown(x,y, alg , temp);
    }
    public void saveGoogleEarth(String filename){
        new KMLwriter(combineXYAttb(),filename);
    }
    public void savePajek(String filename){
        new PajekWriter(x.appendHorizontally(y), adj, filename).WriteFile();
    }
    public void saveDL(String filename, int ext){
        new DLwriter(adj,filename,ext).WriteFile();
    }
    public void saveExcel(String filenameNodes, String filenameArcs){
        new ExcelWriter(combineXYAttb(), filenameNodes).WriteFile();
        new ExcelWriter(adj, filenameArcs).WriteFile();
    }
    public void saveCSV(String filenameNodes, String filenameArcs, char seperator){
        new CSVwriter(combineXYAttb(), filenameNodes, seperator);
        new CSVwriter(adj, filenameArcs, seperator);
    }
   
    //analyzing functions
    public void Border(int alg){
        Borders b = new Borders(x,y,adj,alg);
        b.Write();
    }
    public void QAP(String arg[]){
        qap q = new qap (arg.length-2, arg);
    }
    public void Highlight(int alg, String filename){
        HighlightEdges h = new HighlightEdges(x,y,adj,filename,alg);
        h.write();
    }
    public void SNB() throws OperationNotSupportedException{
        throw new OperationNotSupportedException("function not done yet");
    }

    //extra functions
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
    public void ClearData(){
        x = new EmptyMatrix();
        y = new EmptyMatrix();
        adj = new EmptyMatrix();
        attb = new EmptyMatrix(); 

        loadedFiles = new String[]{};

    }
    public Matrix combineXYAttb() throws IllegalArgumentException{
       if(x.getRowCount() == y.getRowCount() && y.getRowCount() == attb.getRowCount())
            return x.appendHorizontally(y).appendHorizontally(attb);
       else
           throw new IllegalArgumentException("Matrix Sizes do not match.");
    }
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
