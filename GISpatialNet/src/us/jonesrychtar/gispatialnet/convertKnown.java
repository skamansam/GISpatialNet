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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.ujmp.core.Matrix;

/**
 *
 * @author Charles Bevan
 * @date September 8, 2009
 * @version 0.0.1
 */
public class convertKnown {

    //need map and shapewriter
    private ShapefileWriter outN;
    private ShapefileWriter outE;
    private Matrix workingSetNodes;
    private Matrix workingSetEdges;
    private String schemeNodes;
    private String schemeEdges;

    public convertKnown(Matrix mapNodes, Matrix mapEdges){
        //setup mapreader and shapewriter
        workingSetNodes = mapNodes;
        workingSetEdges = mapEdges;

        schemeNodes=analyzeScheme(workingSetNodes);
        schemeEdges=analyzeScheme(workingSetEdges);

        outN = new ShapefileWriter("outN",schemeNodes);
        outE = new ShapefileWriter("outE",schemeEdges);
        
        convert();
    }
    private void convert() {
        GeometryFactory gfact = new GeometryFactory();
        //write Node Shapefile
        Coordinate coord = new Coordinate(0,0);
        Point geo1 = gfact.createPoint(coord);
        Object[] data={geo1,"Hello World"};
        outN.addData(data);
        
        //write Edge Shapefile
        
    }
    private String analyzeScheme(Matrix in){
        String sch="";
        
        return sch;
    }
}
