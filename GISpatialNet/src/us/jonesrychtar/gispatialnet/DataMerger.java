/*
 * This class will merge sets of data into one set of data
 * Unlike Algorithm.SimpleMerge, This class will handle attributes as well.
 */

package us.jonesrychtar.gispatialnet;

import org.ujmp.core.Matrix;

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
    public DataMerger(DataSet A, DataSet B){
        xyAttb1 = util.combine(A.getXY(),A.getAttb());
        adj1 = A.getAdj();

        xyAttb2 = util.combine(B.getXY(),B.getAttb());
        adj2 = B.getAdj();
    }

    /**
     * Merges data based on selected columns
     * @param MatchOn
     * @return Merged data where Matrix[0] is XYAttb and Matrix[1] is Adj
     */
    public DataSet Merge(int[] MatchOn){
        //TODO: implement matching alg
        return new DataSet();
    }
}
