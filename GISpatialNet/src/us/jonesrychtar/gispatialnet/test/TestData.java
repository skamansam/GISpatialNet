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

    public Matrix ZeroMatrix(int rows, int cols){
        return MatrixFactory.zeros(rows,cols);
    }
    public Matrix RandomMatrix(int rows, int cols,int min, int max){
        Random rand = new Random();
        Matrix out = MatrixFactory.zeros(rows,cols);
        for(int i=0; i<rows; i++)
            for(int j=0; j<cols; j++)
                out.setAsDouble((rand.nextInt()%(max-min))+min,i,j);
        return out;
    }

}
