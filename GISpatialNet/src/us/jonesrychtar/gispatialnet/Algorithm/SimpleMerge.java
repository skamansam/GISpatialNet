/*
 * This will naively merge two datasets. This does not handle Attributes only xy and adj data
 */

package us.jonesrychtar.gispatialnet.Algorithm;

import java.util.Vector;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import us.jonesrychtar.gispatialnet.DataSet;
import us.jonesrychtar.gispatialnet.util;

/**
 *
 * @author cfbevan
 */

public class SimpleMerge {
    DataSet A,B;

    /**
     *Constructor
     * @param A 1st Data set to use
     * @param B 2nd Data set to use
     */
    public SimpleMerge(DataSet A, DataSet B){
        this.A = A;
        this.B = B;
    }
    /**
     *
     * @param A Set the 1st Data set to use
     */
    public void setA(DataSet A) {
        this.A = A;
    }

    /**
     *
     * @param B set the 2nd data set to use
     */
    public void setB(DataSet B) {
        this.B = B;
    }

    /**
     *
     * @return 1st data set being used
     */
    public DataSet getA() {
        return A;
    }

    /**
     *
     * @return 2nd data set being used
     */
    public DataSet getB() {
        return B;
    }

    /**
     *
     * @return data set A and data set B merged into one data set
     */
    public DataSet Merge(){
        //check to see if this is a merge of xy in one ds and adj in another
        if(((A.hasAdj() && !A.hasXY()) && (B.hasXY() && !B.hasAdj())) ||
                ((B.hasAdj() && !B.hasXY()) && ((A.hasXY()&&!A.hasAdj())))){
            DataSet ret;
            if(A.hasXY())
                ret = new DataSet(A.getX(),A.getY(),B.getAdj(),MatrixFactory.emptyMatrix());
            else
                ret = new DataSet(B.getX(),B.getY(),A.getAdj(),MatrixFactory.emptyMatrix());
            //copy loaded files from each
            Vector<String> fl = new Vector<String>();
            for (int i = 0; i < A.GetLoadedFiles().size(); i++) {
                fl.add(A.GetLoadedFiles().elementAt(i));
            }
            for (int i = 0; i < B.GetLoadedFiles().size(); i++) {
                fl.add(B.GetLoadedFiles().elementAt(i));
            }
            ret.setFileList(fl);
            return ret;
        }
        Matrix X= MatrixFactory.zeros(A.getX().getRowCount()+B.getX().getRowCount(),1);
        Matrix Y = MatrixFactory.zeros(A.getY().getRowCount()+B.getY().getRowCount(),1);
        Matrix Adj = MatrixFactory.zeros(A.getAdj().getRowCount()+B.getAdj().getRowCount(), A.getAdj().getColumnCount() + B.getAdj().getColumnCount());
       boolean douplicate = false;
        //copy xy data
        for(int i=0; i<A.getXY().getRowCount(); i++){
            X.setAsDouble(A.getXY().getAsDouble(i,0), i,0);
            Y.setAsDouble(A.getXY().getAsDouble(i,1), i,0);
        }
        for(int i=(int) A.getXY().getRowCount(); (i-A.getXY().getRowCount()) < B.getXY().getRowCount(); i++){
            double newX = B.getXY().getAsDouble(i-A.getXY().getRowCount(),0);
            double newY = B.getXY().getAsDouble(i-A.getXY().getRowCount(),1);
            if(!(_has(util.combine(X, Y), newX, newY))){
                X.setAsDouble(newX, i,0);
                Y.setAsDouble(newY, i,0);
            }
            else
                douplicate = true;
        }
        //copy adj data
       if(!douplicate){ //no douplicates, make quick generation
            Matrix adjA = A.getAdj();
            Matrix adjB = B.getAdj();
            Matrix zeroA = MatrixFactory.zeros(adjA.getRowCount(),adjB.getColumnCount());
            Matrix zeroB = MatrixFactory.zeros(adjB.getRowCount(), adjA.getColumnCount());

            Matrix AdjTemp1 = adjA.appendHorizontally(zeroA);
            Matrix AdjTemp2 = zeroB.appendHorizontally(adjB);

            Adj = AdjTemp1.appendVertically(AdjTemp2);
       }
       else{ //perform long hand generation
            Matrix adjTemp = MatrixFactory.zeros(X.getRowCount(), X.getRowCount());
            for(int row=0; row<adjTemp.getRowCount(); row++)
                for(int col=0; col<adjTemp.getColumnCount(); col++){
                    int indexArow = _getIndex(A.getXY(), X.getAsDouble(row,0), Y.getAsDouble(row,0));
                    int indexBrow = _getIndex(B.getXY(), X.getAsDouble(row,0), Y.getAsDouble(row,0));
                    int indexAcol = _getIndex(A.getXY(), X.getAsDouble(col,0), Y.getAsDouble(col,0));
                    int indexBcol = _getIndex(B.getXY(), X.getAsDouble(col,0), Y.getAsDouble(col,0));
                    if(indexArow != -1 && indexAcol !=-1){
                        adjTemp.setAsDouble(A.getAdj().getAsDouble(indexArow,indexAcol), row,col);
                    }
                    else{
                        adjTemp.setAsDouble(B.getAdj().getAsDouble(indexBrow,indexBcol), row,col);
                    }
                }
            Adj = adjTemp;
       }
        X = X.reshape(Adj.getRowCount(),1);
        Y = Y.reshape(Adj.getRowCount(),1);
        DataSet ret = new DataSet(X,Y,Adj,MatrixFactory.emptyMatrix());

        //copy loaded files from each
        Vector<String> fl = new Vector<String>();
        for(int i=0; i<A.GetLoadedFiles().size(); i++){
            fl.add(A.GetLoadedFiles().elementAt(i));
        }
        for(int i=0; i<B.GetLoadedFiles().size(); i++){
            fl.add(B.GetLoadedFiles().elementAt(i));
        }
        ret.setFileList(fl);
        return ret;
    }
    /**
     * Tests if the coordinate checkX,checkY is in matrix in
     * @param in XY matrix to check against
     * @param checkX x coordinate to find
     * @param checkY y coordinate to find
     * @return true if checkX,checkY is in matrix in (must be in same row)
     */
    private boolean _has(Matrix in, double checkX, double checkY){
        for(int row=0; row<in.getRowCount(); row++)
            if(checkX == in.getAsDouble(row,0) && checkY == in.getAsDouble(row,1))
                return true;
        return false;
    }
    /**
     * Finds the index of checkX, checkY in matrix in
     * @param in Matrix to check against
     * @param checkX x coordinate to find
     * @param checkY y coordinate to find
     * @return index of row checkX, checkY in matrix in (-1 if not found)
     */
    private int _getIndex(Matrix in, double checkX, double checkY){
        for(int row=0; row<in.getRowCount(); row++)
            if(checkX == in.getAsDouble(row,0) && checkY == in.getAsDouble(row,1))
                return row;
        return -1;
    }
}
