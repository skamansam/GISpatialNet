package us.jonesrychtar.gispatialnet.test;

/*
 * This is a dummy test class used to test other classes
 */


import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.objectmatrix.EmptyMatrix;
import us.jonesrychtar.gispatialnet.Reader.ShapeFileReader;
import us.jonesrychtar.gispatialnet.convertKnown;

public class TEST {

    Matrix x,y,a;
    static TestData td = new TestData();

    public static void main(String[] args) {
       TEST run = new TEST();
       //run.TESTShapefileWriter();
       run.TestSHPreader();
    }
    public TEST(){

    }
    public void TESTShapefileWriter(){
         x = td.RandomMatrix(10,2,0,10);
         y = td.RandomMatrix(10, 2, 0, 10);
         a = td.RandomMatrix(10, 10, 0, 1);
         

         convertKnown ck = new convertKnown(x,y,a);
         
         System.out.println(x);
         System.out.println(y);
         System.out.println(a);
    }
    public void TestSHPreader(){
        ShapeFileReader shpr = new ShapeFileReader("outN.shp", "outE.shp");
        shpr.Read();
    }
}
