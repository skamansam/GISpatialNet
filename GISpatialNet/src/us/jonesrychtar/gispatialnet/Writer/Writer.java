/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package us.jonesrychtar.gispatialnet.Writer;


import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import jxl.write.WriteException;
import org.boehn.kmlframework.kml.KmlException;
import org.geotools.feature.SchemaException;
import us.jonesrychtar.gispatialnet.DataSet;
import us.jonesrychtar.gispatialnet.convertKnown;
import us.jonesrychtar.gispatialnet.convertUnknown;
import us.jonesrychtar.gispatialnet.util;

/**
 *
 * @author cfbevan
 */
public class Writer {
    //saving functions------------------------------------------------------------------------------------
    /**
     * Saves to 2 shapefiles, one with nodes, one with edges
     */
    public static void saveShapefile(String Edgefilename, String Nodefilename, DataSet ds) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{
            if (!(ds.hasAttb())) {
                new convertKnown(Edgefilename, Nodefilename, ds.getX(),ds.getY(), ds.getAdj(), ds.getAttb());
            } else {
                new convertKnown(Edgefilename, Nodefilename, ds.getX(),ds.getY(), ds.getAdj());
            }
    }
    /**
     * Saves to 2 shapefiles, one with nodes, one with edges
     * @param alg which layout algorithm to use
     * @param Height height of final map
     * @param Width width of final map
     */
    public static void saveShapefileUnknown(String Edgefilename, String Nodefilename, int alg, int Height, int Width, DataSet ds) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{
        Dimension temp = new Dimension(Height,Width);
        if(!(ds.hasAttb()))
            new convertUnknown(Edgefilename, Nodefilename,ds.getAdj(), alg , temp);
        else
            new convertUnknown(Edgefilename, Nodefilename,ds.getAdj(),ds.getAttb(), alg , temp);
    }
    /**
     * Saves to Google Earth kml format
     * @param filename Name of output file without extension
     */
    public static void saveGoogleEarth(String filename, DataSet ds) throws KmlException, IOException{
        new KMLwriter(util.combine(ds.getXY(),ds.getAttb()),filename).WriteFile();
    }
    /**
     * Saves to Pajek .net format
     * @param filename name of output file without extension
     */
    public static void savePajek(String filename, DataSet ds) throws FileNotFoundException{
        new PajekWriter(ds.getXY(), ds.getAdj(), filename).WriteFile();
    }
    /**
     * Saves to DL/UCINET format
     * @param filename name of output file without extension
     * @param ext extension (0:.dat, 1:.txt)
     */
    public static void saveDL(String filename, int ext, DataSet ds) throws FileNotFoundException{
        new DLwriter(ds.getAdj(),filename,ext).WriteFile();
    }
    /**
     * Saves to excel .xls format
     * @param filenameNodes name of output file for nodes without extension
     * @param filenameArcs name of output file for edges without extension
     */
    public static void saveExcel(String filenameNodes, String filenameArcs, DataSet ds) throws IOException, WriteException{
        new ExcelWriter(util.combine(ds.getXY(),ds.getAttb()), filenameNodes).WriteFile();
        new ExcelWriter(ds.getAdj(), filenameArcs).WriteFile();
    }
    /**
     * Saves in a seperated value format (.csv, .txt)
     * @param filenameNodes name of output file for nodes without extension
     * @param filenameArcs name of output file for edges without extension
     * @param seperator character that seperates values
     */
    public static void saveCSV(String filenameNodes, String filenameArcs, char seperator, DataSet ds) throws FileNotFoundException{
        new CSVwriter(util.combine(ds.getAdj(),ds.getAttb()), filenameNodes, seperator).WriteFile();
        new CSVwriter(ds.getAdj(), filenameArcs, seperator).WriteFile();
    }
}
