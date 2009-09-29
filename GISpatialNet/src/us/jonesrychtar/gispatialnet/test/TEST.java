package us.jonesrychtar.gispatialnet.test;

/*
 * This is a dummy test class used to test other classes
 */


import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.objectmatrix.EmptyMatrix;
import us.jonesrychtar.gispatialnet.convertKnown;

public class TEST {

    Matrix x,y,a;
    static TestData td = new TestData();

    public static void main(String[] args) {
       TEST run = new TEST();
    }
    public TEST(){
         x = td.RandomMatrix(10,2,0,10);
         y = td.RandomMatrix(10, 2, 0, 10);
         a = td.RandomMatrix(10, 10, 0, 1);
         

         convertKnown ck = new convertKnown(x,y,a, MatrixFactory.zeros(org.ujmp.core.enums.ValueType.STRING, 10, 2));
         
         System.out.println(x);
         System.out.println(y);
    }
}
