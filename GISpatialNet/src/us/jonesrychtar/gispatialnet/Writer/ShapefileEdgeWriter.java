/*
 * This will write an edge shapefile with or without style
 * 
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP
 */
package us.jonesrychtar.gispatialnet.Writer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import org.ujmp.core.Matrix;

/**
 *
 * @author cfbevan
 * @date Oct 8, 2009
 * @version 0.0.1
 */
public class ShapefileEdgeWriter {

    private ShapefileWriter outE;
    private Matrix x; //format: xcoordinate
    private Matrix y; //format: y coordinate
    private Matrix adj; //format: attributes...
    private String schemeEdges;

    /**
     *Constructor
     * @param filename Name of output file
     * @param xin Vector matrix containing X coordinate data
     * @param yin Vector matrix containing Y coordinate data
     * @param adjin Matrix containing edge values between nodes
     */
    public ShapefileEdgeWriter(String filename, Matrix xin, Matrix yin, Matrix adjin) {

        x = xin;
        y = yin;
        adj = adjin;
        
        schemeEdges = "*l:LineString,value:Float";

        outE = new ShapefileWriter(filename, schemeEdges);
    }

    /**
     * Write data to edge shapefile
     */
    public void write() {
        GeometryFactory gfact = new GeometryFactory();
        
            for (int r = 0; r < x.getRowCount(); r++) {
                //output to be written
                Object[] data = new Object[2];
                //iterate through adj matrix to find edges
                for (int r2 = 0; r2 < adj.getRowCount(); r2++) {
                    for (int c = 0; c < adj.getColumnCount(); c++) {
                        if (adj.getAsDouble(r2, c) > 0) {
                            //create coordinates for found edge
                            Coordinate coord = new Coordinate(x.getAsDouble(r2, 0), y.getAsDouble(r2, 0));
                            Coordinate coord2 = new Coordinate(x.getAsDouble(c, 0), y.getAsDouble(c, 0));
                            Coordinate[] points = {coord, coord2};
                            LineString ln = gfact.createLineString(points);
                            //create data for edge
                            data[0] = ln;
                            data[1] = adj.getAsDouble(r2, c);
                            //write edge
                            outE.addData(data);
                        }
                    }
                }
            }
        }
}
