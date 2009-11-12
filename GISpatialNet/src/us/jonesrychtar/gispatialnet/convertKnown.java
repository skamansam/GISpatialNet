/*
 * This program will convert network file with known geographic coordinates
 * into a shapefile.
 * 
 * For research by Eric Jones and Jan Rychtar.
 * 
 * Requires: geotools,
 * 
 */
package us.jonesrychtar.gispatialnet;
import java.io.IOException;
import java.net.MalformedURLException;
import org.geotools.feature.SchemaException;
import us.jonesrychtar.gispatialnet.Writer.*;

import org.ujmp.core.Matrix;

/**
 *
 * @author Charles Bevan
 * @date September 8, 2009
 * @version 0.0.1
 */
public class convertKnown {

    //need map and shapewriter
    private ShapefileEdgeWriter sfew;
    private ShapefileNodeWriter sfnw;
   
    /**
    * Converts Data with known xy coordinates into a shapefile
    * @param filenameE name of output edge shapefile
    * @param filenameN name of output node shapefile
    * @param xin vector matrix of x coordinates
    * @param yin vector matrix of y coordinates
    * @param adjin matrix containing edge data
     * @param attbin matrix containing attribute data
     * @throws IllegalArgumentException
     * @throws MalformedURLException
     * @throws SchemaException
     * @throws IOException
    */
    public convertKnown(String filenameE, String filenameN, Matrix xin, Matrix yin, Matrix adjin, Matrix attbin) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{

        sfew = new ShapefileEdgeWriter(filenameE, xin,yin,adjin);
        sfnw = new ShapefileNodeWriter(filenameN, xin,yin,attbin);

        sfew.write();
        sfnw.write();
    }

    /**
    * Converts Data with known xy coordinates into a shapefile
    * @param filenameE name of output edge shapefile
    * @param filenameN name of output node shapefile
    * @param xin vector matrix of x coordinates
    * @param yin vector matrix of y coordinates
     * @param adjin matrix containing edge data
     * @throws IllegalArgumentException
     * @throws MalformedURLException
     * @throws IOException
     * @throws SchemaException
    */
    public convertKnown(String filenameE, String filenameN,Matrix xin, Matrix yin, Matrix adjin) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{

        sfew = new ShapefileEdgeWriter(filenameE, xin,yin,adjin);
        sfnw = new ShapefileNodeWriter(filenameN, xin,yin);

        sfew.write();
        sfnw.write();
    }
}
    