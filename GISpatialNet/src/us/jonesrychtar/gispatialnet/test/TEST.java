package us.jonesrychtar.gispatialnet.test;

/*
 * This is a dummy test class used to test other classes
 */

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.CannotProceedException;
import jxl.write.WriteException;
import org.boehn.kmlframework.kml.KmlException;
import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import us.jonesrychtar.gispatialnet.HighlightEdges;
import us.jonesrychtar.gispatialnet.MatrixConversion;
import us.jonesrychtar.gispatialnet.Reader.*;
import us.jonesrychtar.gispatialnet.Writer.*;
import us.jonesrychtar.gispatialnet.convertKnown;
import us.jonesrychtar.gispatialnet.convertUnknown;
import us.jonesrychtar.gispatialnet.qap;
import us.jonesrychtar.gispatialnet.util;

public class TEST {

    Matrix x,y,a, attb;
    static TestData td = new TestData();
    static util u = new util();

    public static void main(String[] args) {
       TEST run = new TEST();
       //run.printData();

       //functions
       //run.testHE();
       //run.testConversion();
       //run.testUnknownShapeFile();
       //run.testQAP();

       //writers
       //run.TESTShapefileWriter();
       //run.TestXLSwriter();
       //run.TestKMLwriter();
       //run.TestCSVwriter();
       //run.TestDLwriter();
       //run.TestPajekWriter();


       //readers
       //run.TestSHPreader();
       //run.TestExcelReader();
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
        System.out.println("  "+x.getColumnLabel(0)+"  "+y.getColumnLabel(0));
        System.out.println(u.combine(x, y));
        System.out.println(a);
    }

    //test functions--------------------------------------------------------------------------------------
    public void testHE() {
        try {
            ShapefileNodeWriter sfnw = new ShapefileNodeWriter("OutNH", x, y);
            sfnw.write();
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SchemaException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
        //does write shapefile, not tested algs
        //alg == 0-3
        int alg = 0;
        HighlightEdges he = new HighlightEdges("OutEH",x,y,a,alg);
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
    public void testUnknownShapeFile(){
        //works
        //Dimension needs to have large numbers (about 10xNumber of rows)
        convertUnknown cu = new convertUnknown("unE","unN",a, 0, new Dimension(100,100));
    }
    public void testQAP(){
        //works
        try {
              qap q = new qap(3, new String[]{"-s", "assoc.txt", "gond.txt", "10000"});
        } catch (CannotProceedException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Error ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //test writers----------------------------------------------------------------------------------------
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
            ExcelWriter ew = new ExcelWriter(u.combine(x,y),"nodes.xls");
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
        //System.out.println(u.combine(x, y));
        //System.out.println(attb);
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
        //works
        Matrix temp = u.combine(x,y);
        //System.out.println("Temp Labels:  "+temp.getColumnLabel(0)+"  "+temp.getColumnLabel(1));
        DLwriter dlw = new DLwriter(temp, "test");
        try {
            dlw.WriteFile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void TestPajekWriter(){
        //works
        PajekWriter pw = new PajekWriter(u.addNumberCol(u.combine(x,y)),a,"Test");
        try {
            pw.WriteFile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    //test readers----------------------------------------------------------------------------------------
    public void TestSHPreader(){
        //Works
       try{
           ShapeFileReader shpr = new ShapeFileReader("outN.shp", "outE.shp");
            Matrix[] temp = shpr.Read();
            x = temp[0];
            y = temp[1];
            a = temp[2];
            attb = temp[3];
            printData();
       }
       catch (Exception e){
           e.printStackTrace();
       }
    }

    public void TestExcelReader(){
        //works
        ExcelReader er = new ExcelReader("nodes.xls");
        Matrix temp = MatrixFactory.emptyMatrix();
        try {
            temp = er.read(0, 10, 3);
        } catch (Exception ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
        u.stripNumCol(temp);

       System.out.println(temp);

       Matrix tempa = MatrixFactory.emptyMatrix();
       ExcelReader era = new ExcelReader("edges.xls");
        try {
            tempa = era.read(1, 10, 10);
        } catch (Exception ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }

       System.out.println(tempa);
    }
}
