/*
 * This class makes test data.
 * */

package us.jonesrychtar.gispatialnet.test;

import java.util.Random;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;

/**
 *
 * @author cfbevan
 */
public class TestData {

    /**
     *
     * @param rows number of rows
     * @param cols number of columns
     * @return A matrix of zeros
     */
    public Matrix ZeroMatrix(int rows, int cols){
        return MatrixFactory.zeros(rows,cols);
    }
    /**
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param min minimum random number
     * @param max maximum random number
     * @return Matrix of random values
     */
    public Matrix RandomMatrix(int rows, int cols,int min, int max){

        max ++;
        Random rand = new Random();
        Matrix out = MatrixFactory.zeros(org.ujmp.core.enums.ValueType.DOUBLE, rows,cols);
        //set col headers
        for(int col=0; col<cols; col++){
            out.setColumnLabel(col, "Label");
        }
        for(int i=0; i<rows; i++)
            for(int j=0; j<cols; j++)
                out.setAsDouble((rand.nextInt(max))+min,i,j);
        return out;
    }

}
