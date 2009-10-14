/*
 * This class will merge sets of data into one set of data
 */

package us.jonesrychtar.gispatialnet;

import org.ujmp.core.Matrix;


public class DataMerger {

    Matrix xyAttb1, xyAttb2;
    Matrix adj1, adj2;

    public DataMerger(Matrix data1, Matrix adjacency1, Matrix data2, Matrix adjacency2){
        xyAttb1 = data1;
        adj1 = adjacency1;

        xyAttb2 = data2;
        adj2 = adjacency2;
    }

    public Merge(){
        
    }
}
