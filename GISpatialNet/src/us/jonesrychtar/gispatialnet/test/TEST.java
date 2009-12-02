package us.jonesrychtar.gispatialnet.test;

/*
 * This is a dummy test class used to test other classes
 */

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.CannotProceedException;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import jxl.write.WriteException;
import org.boehn.kmlframework.kml.KmlException;
import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.xml.sax.SAXException;
import us.jonesrychtar.gispatialnet.MatrixConversion;
import us.jonesrychtar.gispatialnet.Algorithm.HighlightEdges;
import us.jonesrychtar.gispatialnet.Algorithm.HighlightEdgesByVal;
import us.jonesrychtar.gispatialnet.Algorithm.QAP;
import us.jonesrychtar.gispatialnet.DataSet;
import us.jonesrychtar.gispatialnet.Reader.*;
import us.jonesrychtar.gispatialnet.Writer.*;
import us.jonesrychtar.gispatialnet.convertKnown;
import us.jonesrychtar.gispatialnet.convertUnknown;
import us.jonesrychtar.gispatialnet.util;

/**
 *
 * @author cfbevan
 */
public class TEST {

    Matrix x,y,a, attb;
    static TestData td = new TestData();
    static util u = new util();

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
       TEST run = new TEST();
       //run.printData();

       //functions
       //run.testHE(); 
       //run.testConversion();
       //run.testUnknownShapeFile();
       //run.testQAP(); 
       //run.testHighlightByVal();

       //writers
       //run.TESTShapefileWriter();
       //run.TestXLSwriter();
       //run.TestKMLwriter();
       //run.TestCSVwriter();
       //run.TestDLwriter();
       //run.TestPajekWriter();


       //readers
       //run.TestSHPreader();
       run.TestExcelReader(); 
       //run.testKMLreader();
       //run.testPajekReader();
       //run.testDLreader();
       //run.testCSVreader();

    }
    /**
     *
     */
    public TEST(){
        makeData();
    }

    private void makeData(){
        x = td.RandomMatrix(10,1,0,10);
        y = td.RandomMatrix(10,1,0,10);
        a = td.RandomMatrix(10,10,0,1);
    }

    /**
     *
     */
    public void printData(){
        System.out.println("  "+x.getColumnLabel(0)+"  "+y.getColumnLabel(0));
        System.out.println(u.combine(x, y));
        System.out.println(a);
    }

    //test functions--------------------------------------------------------------------------------------
    /**
     *
     */
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
    /**
     *
     */
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
    /**
     *
     */
    public void testUnknownShapeFile(){
        //works
        //Dimension needs to have large numbers (about 10xNumber of rows)
        convertUnknown cu = new convertUnknown("unE","unN",a, 0, new Dimension(100,100));
    }
    /**
     *
     */
    public void testQAP(){
        //works
        try {
              QAP q = new QAP(3, new String[]{"-s", "assoc.txt", "gond.txt", "10000"});
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
    /**
     *
     */
    public void testHighlightByVal(){
        //a = td.RandomMatrix(10, 10, 0, 3);
        try{
            HighlightEdgesByVal hebv = new HighlightEdgesByVal("out",x,y,a);
            hebv.write();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    //test writers----------------------------------------------------------------------------------------
    /**
     *
     */
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
    /**
     *
     */
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
    /**
     *
     */
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
    /**
     *
     */
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
    /**
     *
     */
    public void TestDLwriter(){
        //works

        //System.out.println("Temp Labels:  "+temp.getColumnLabel(0)+"  "+temp.getColumnLabel(1));
        DLwriter dlw = new DLwriter(a, "test");
        try {
            dlw.WriteFile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     *
     */
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
    /**
     *
     */
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

    /**
     *
     */
    public void TestExcelReader(){
        //works
        ExcelReader er = new ExcelReader("nodes.xls");
        Vector<Matrix> temp = new Vector<Matrix>();
        try {
            temp = er.read(0, 10, 2);
        } catch (Exception ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
        u.stripNumCol(temp.elementAt(0));

       System.out.println(temp.elementAt(0));

      Vector<Matrix> tempa = new Vector<Matrix>();
       ExcelReader era = new ExcelReader("edges.xls");
        try {
            tempa = era.read(1, 10, 10);
        } catch (Exception ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
       System.out.println(tempa.elementAt(0));
    }
    //-----------------------------------------------------------------------------------------------------------
    /**
     *
     */
    public void testKMLreader(){
        KMLreader kr = null;
        try {
            kr = new KMLreader("test.kml");
        }catch (IOException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
        Matrix[] temp = new Matrix[]{MatrixFactory.emptyMatrix()};
        try {
            temp = kr.read();
        } catch (Exception ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
        for( int m=0; m<temp.length;m++)
            System.out.println(temp[m]);
    }
    /**
     *
     */
    public void testPajekReader(){
        PajekReader pr = new PajekReader("Test.net");
        Vector<DataSet> ds= new Vector<DataSet>();
        try {
            ds = pr.Read(0, 10, 10);
        } catch (Exception ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i=0; i<ds.size(); i++)
            System.out.println(ds.elementAt(i));
    }
    /**
     *
     */
    public void testDLreader(){
        //works
        DLreader dlr = new DLreader("test.dat");
        Vector<DataSet> temp = null;
        try {
            //temp = dlr.Read(DLreader.FULL_MATRIX, 5, 5);
            //temp = dlr.Read(DLreader.LOWER_MATRIX, 5, 5);
            temp = dlr.Read(TextFileReader.UPPER_MATRIX, 5, 5);
        } catch (Exception ex) {
            Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }
        //for(int i=0; i<temp.getColumnCount(); i++)
        //    System.out.print(temp.getColumnLabel(i)+" ");
        System.out.println("\n"+temp);
    }
    /**
     *
     */
    public void testCSVreader(){
        CSVFileReader cr = new CSVFileReader("test.csv");
        Vector<Matrix> temp = null;
        /*try {
        temp = cr.Read(0, 11, 2);
        } catch (FileNotFoundException ex) {
        Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
        Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
        Logger.getLogger(TEST.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        System.out.println(temp);
    }
}
