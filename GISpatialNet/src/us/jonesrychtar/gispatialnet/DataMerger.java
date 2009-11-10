/*
 * This class will merge sets of data into one set of data
 * Unlike Algorithm.SimpleMerge, This class will handle attributes as well.
 */

package us.jonesrychtar.gispatialnet;

import org.ujmp.core.Matrix;

//TODO: Merge will now operate on two DataSets and return one DataSet.
public class DataMerger {

    Matrix xyAttb1, xyAttb2;
    Matrix adj1, adj2;
    Matrix xyAttbOut, adjOut;

    /**
     * Merge two sets of data into one
     * @param data1 Matrix containing columns X,Y, Attributes
     * @param adjacency1 Adjacency matrix
     * @param data2 Matrix containing columns X,Y, Attributes
     * @param adjacency2 Adjacency matrix
     */
    public DataMerger(Matrix data1, Matrix adjacency1, Matrix data2, Matrix adjacency2){
        xyAttb1 = data1;
        adj1 = adjacency1;

        xyAttb2 = data2;
        adj2 = adjacency2;
    }

    /**
     * Merges data based on selected columns
     * @param MatchOn
     * @return Merged data where Matrix[0] is XYAttb and Matrix[1] is Adj
     */
    public Matrix[] Merge(int[] MatchOn){
        //TODO: implement matching alg
        return new Matrix[]{xyAttbOut, adjOut};
    }
}
