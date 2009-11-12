/*
 * This does not work yet!!!
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 */

package us.jonesrychtar.gispatialnet;

import org.ujmp.core.Matrix;
/**
 *
 * @author cfbevan
 * This does not work yet
 */
/**
 *
 * @author cfbevan
 */
public class DataMerger {

    Matrix xyAttb1, xyAttb2;
    Matrix adj1, adj2;
    Matrix xyAttbOut, adjOut;

    /**
     * Merge two sets of data into one
     * @param A 
     * @param A 1st data set to use
     * @param B 
     * @param B 2nd data set to use
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
