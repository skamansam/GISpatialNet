/*
 * This class will merge sets of data into one set of data
 */

package us.jonesrychtar.gispatialnet;

import org.ujmp.core.Matrix;


public class DataMerger {

    Matrix xyAttb1, xyAttb2;
    Matrix adj1, adj2;
    Matrix xyAttbOut, adjOut;

    public DataMerger(Matrix data1, Matrix adjacency1, Matrix data2, Matrix adjacency2){
        xyAttb1 = data1;
        adj1 = adjacency1;

        xyAttb2 = data2;
        adj2 = adjacency2;
    }

    public Matrix[] Merge(int[] MatchOn){
        //TODO: implement matching alg
        return new Matrix[]{xyAttbOut, adjOut};
    }
}
