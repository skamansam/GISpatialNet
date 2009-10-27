/*
 * This class will make storing data easier
 */

package us.jonesrychtar.gispatialnet.Reader;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;

/**
 *
 * @author cfbevan
 */
public class ReaderUtil {

    public Matrix UpperToFull(Matrix in){
        Matrix out = MatrixFactory.zeros(in.getRowCount(), in.getColumnCount());

        for(int row=0; row<in.getRowCount(); row++)
            for(int col=row; col<in.getColumnCount(); col++)
            {
                out.setAsDouble(in.getAsDouble(row,col), row,col);
                out.setAsDouble(in.getAsDouble(row,col),col,row);
            }

        return out;
    }
    public Matrix LowerToFull (Matrix in){
        Matrix out = MatrixFactory.zeros(in.getRowCount(), in.getColumnCount());

        for(int col=0; col<in.getColumnCount(); col++)
            for(int row=col; row<in.getRowCount(); row++){
                out.setAsDouble(in.getAsDouble(row,col), row,col);
                out.setAsDouble(in.getAsDouble(row,col),col,row);
            }
        return out;
    }
    public void addDiag (Matrix in){
        for(int col=0; col<in.getColumnCount(); col++)
            for(int row=col; row==col; row++){
                in.setAsDouble(1, row,col);
            }
    }
    
}
