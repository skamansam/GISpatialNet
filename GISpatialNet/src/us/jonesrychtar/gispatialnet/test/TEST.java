package us.jonesrychtar.gispatialnet.test;

/*
 * This is a dummy test class used to test other classes
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.write.WriteException;
import org.boehn.kmlframework.kml.KmlException;
import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
import us.jonesrychtar.gispatialnet.HighlightEdges;
import us.jonesrychtar.gispatialnet.MatrixConversion;
import us.jonesrychtar.gispatialnet.Reader.*;
import us.jonesrychtar.gispatialnet.Writer.*;
import us.jonesrychtar.gispatialnet.convertKnown;
import us.jonesrychtar.gispatialnet.util;

public class TEST {

    Matrix x,y,a, attb;
    static TestData td = new TestData();
    static util u = new util();

    public static void main(String[] args) {
       TEST run = new TEST();
       run.printData();

       //functions
       //run.testHE();
       //run.testConversion();

       //writers
       //run.TESTShapefileWriter();
       //run.TestXLSwriter();
       //run.TestKMLwriter();
       //run.TestCSVwriter();
       run.TestDLwriter();


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
         System.out.println(u.combine(x, y));
         System.out.println(a);
    }

    //test functions
    public void testHE() {
        //does write shapefile, not tested algs
        HighlightEdges he = new HighlightEdges("OutEH",x,y,a,0);
        try {
            he.write();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SchemaException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            //Works
            //tests shapefileEdgeWriter, ChapefileNodeWriter, ShapefileWriter, and convertKnown
            convertKnown ck = new convertKnown("outE", "outN", x, y, a);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SchemaException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void TestXLSwriter(){
        //Works!
            ExcelWriter ew = new ExcelWriter(x.appendHorizontally(y),"nodes.xls");
            ExcelWriter eew = new ExcelWriter(a, "edges.xls");
        try {
            ew.WriteFile();
            eew.WriteFile();
        } catch (IOException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WriteException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void TestKMLwriter(){
        //works
        //must have "long, lat, name, desc" ONLY!
        attb = td.ZeroMatrix(10, 2);
        System.out.println(u.combine(x, y));
        System.out.println(attb);
        KMLwriter kmlw = new KMLwriter(u.combine(u.combine(x, y),attb),"test.kml");
        try {
            kmlw.WriteFile();
        } catch (KmlException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void TestCSVwriter(){
        //works
        CSVwriter csvw = new CSVwriter(u.combine(x, y),"test");
        try {
            csvw.setSeperator(' ');
            csvw.WriteFile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void TestDLwriter(){
        //does not write column labels correctly
        DLwriter dlw = new DLwriter(u.combine(x, y), "test");
        try {
            dlw.WriteFile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
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
