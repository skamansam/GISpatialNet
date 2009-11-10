/*
 * This will naively merge two datasets. This does not handle Attributes only xy and adj data
 */

package us.jonesrychtar.gispatialnet.Algorithm;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import us.jonesrychtar.gispatialnet.DataSet;

/**
 *
 * @author cfbevan
 */
//TODO: Right now this does not check for douplicate data
public class SimpleMerge {
    DataSet A,B;

    public SimpleMerge(DataSet A, DataSet B){
        this.A = A;
        this.B = B;
    }
    public void setA(DataSet A) {
        this.A = A;
    }

    public void setB(DataSet B) {
        this.B = B;
    }

    public DataSet getA() {
        return A;
    }

    public DataSet getB() {
        return B;
    }

    public DataSet Merge(){
        Matrix X= MatrixFactory.zeros(A.getX().getRowCount()+B.getX().getRowCount(),1);
        Matrix Y = MatrixFactory.zeros(A.getY().getRowCount()+B.getY().getRowCount(),1);
        Matrix Adj = MatrixFactory.zeros(A.getAdj().getRowCount()+B.getAdj().getRowCount(), A.getAdj().getColumnCount() + B.getAdj().getColumnCount());
       
        //copy xy data
        for(int i=0; i<A.getXY().getRowCount(); i++){
            X.setAsDouble(A.getXY().getAsDouble(i,0), i,0);
            Y.setAsDouble(A.getXY().getAsDouble(i,1), i,0);
        }
        for(int i=(int) A.getXY().getRowCount(); (i-A.getXY().getRowCount()) < B.getXY().getRowCount(); i++){
            X.setAsDouble(B.getXY().getAsDouble(i-A.getXY().getRowCount(),0), i,0);
            Y.setAsDouble(B.getXY().getAsDouble(i-A.getXY().getRowCount(),1), i,0);
        }
        //copy adj data
        Matrix adjA = A.getAdj();
        Matrix adjB = B.getAdj();
        Matrix zeroA = MatrixFactory.zeros(A.getAdj().getRowCount(), B.getAdj().getColumnCount());
        Matrix zeroB = MatrixFactory.zeros(B.getAdj().getRowCount(), A.getAdj().getRowCount());

        Matrix AdjTemp1 = adjA.appendHorizontally(zeroA);
        Matrix AdjTemp2 = adjB.appendHorizontally(zeroB);

        Adj = AdjTemp1.appendVertically(AdjTemp2);
        
        return new DataSet(X,Y,Adj,MatrixFactory.emptyMatrix());
    }
}
