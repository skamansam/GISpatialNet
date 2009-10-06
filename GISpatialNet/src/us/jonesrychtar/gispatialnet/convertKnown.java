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
   

    public convertKnown(Matrix xin, Matrix yin, Matrix adjin, Matrix attbin){

        sfew = new ShapefileEdgeWriter(xin,yin,adjin);
        sfnw = new ShapefileNodeWriter(xin,yin,attbin);

        sfew.write();
        sfnw.write();
    }
}
    