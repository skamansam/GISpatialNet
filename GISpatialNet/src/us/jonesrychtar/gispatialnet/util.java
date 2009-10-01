/*
 * This is the main program. It coordinates all other classes
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 *
 */
package us.jonesrychtar.gispatialnet;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.objectmatrix.EmptyMatrix;

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
    private Matrix adj = new EmptyMatrix(); //matrix of size x by y where if ij >= 1 there is a line connecting (x,y)i to (x,y)j
    private Matrix attb = new EmptyMatrix(); //attributes for node (x,y)i where i is the row of attb

    private String[] loadedFiles = new String[]{};

    //loading functions

    //saving functions

    //analyzing functions


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

}
