package us.jonesrychtar.gispatialnet.test;

/*
 * This is a dummy test class used to test other classes
 */

import org.ujmp.core.Matrix;
import us.jonesrychtar.gispatialnet.HighlightEdges;
import us.jonesrychtar.gispatialnet.MatrixConversion;
import us.jonesrychtar.gispatialnet.Reader.ShapeFileReader;
import us.jonesrychtar.gispatialnet.convertKnown;

public class TEST {

    Matrix x,y,a, attb;
    static TestData td = new TestData();

    public static void main(String[] args) {
       TEST run = new TEST();
       //run.printData();

       //functions
       //run.testHE();
       //run.testConversion();

       //writers
       //run.TESTShapefileWriter();

       //readers
       //run.TestSHPreader();
    }
    public TEST(){
        makeData();
    }
    private void makeData(){
        x = td.RandomMatrix(10,1,0,10);
        y = td.RandomMatrix(10,1,0,10);
        a = td.RandomMatrix(10,10,0,1);
    }

    public void printData(){
         System.out.println(x);
         System.out.println(y);
         System.out.println(a);
    }

    //test functions
    public void testHE(){
        //does write shapefile, not tested algs
        HighlightEdges he = new HighlightEdges("OutEH",x,y,a,0);
        he.write();
    }
    public void testConversion(){
        //Works
        Matrix xy = x.appendHorizontally(y);
        System.out.println(xy);

        MatrixConversion mc = new MatrixConversion();
        //mc.Reflection(xy, mc.XAXIS);
        //mc.Reflection(xy, mc.YAXIS);
        //mc.RotateClockwise(xy, 90);
        //mc.RotateCounterclockwise(xy, 90);
        //mc.Scale(xy, 2);
        //mc.Translation(xy, 1, 1);

        System.out.println(xy);
    }

    //test writers
    public void TESTShapefileWriter(){
        //Works
        //tests shapefileEdgeWriter, ChapefileNodeWriter, ShapefileWriter, and convertKnown
         convertKnown ck = new convertKnown("outE","outN",x,y,a);    
    }

    //test readers
    public void TestSHPreader(){
        //Works
       try{
           ShapeFileReader shpr = new ShapeFileReader("outN.shp", "outE.shp");
            shpr.Read();
            printData();
       }
       catch (Exception e){
           e.printStackTrace();
       }
    }
}
