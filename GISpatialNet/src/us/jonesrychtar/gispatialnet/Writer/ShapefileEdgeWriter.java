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
import org.geotools.styling.Style;

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
    private Matrix adjStyled; //line at x,y has style I
    private String schemeEdges;
    private boolean hasStyle;
    private Style[] Styles;

    /**
     * Constructor
     * @param filename Name of output file
     * @param xin Vector matrix of X coordinate values
     * @param yin Vector matrix of Y coordinate values
     * @param adjin Matrix containing edge values between nodes
     * @param adjStyledin Matrix containing data on which style to use on each edge
     * @param Style Array of styles where the index matches number in Styledin Matrix
     */
    public ShapefileEdgeWriter(String filename, Matrix xin, Matrix yin, Matrix adjin, Matrix adjStyledin, Style[] Style) {

        x = xin;
        y = yin;
        adj = adjin;
        hasStyle = true;
        Styles = Style;
        adjStyled = adjStyledin;
        schemeEdges = "*l:LineString,value:Number,style:Style";

        outE = new ShapefileWriter(filename, schemeEdges);

    }

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
        hasStyle = false;
        schemeEdges = "*l:LineString,value:Float";

        outE = new ShapefileWriter(filename, schemeEdges);
    }

    /**
     * Write data to edge shapefile
     */
    public void write() {
        GeometryFactory gfact = new GeometryFactory();
        if (hasStyle) {
            for (int r = 0; r < x.getRowCount(); r++) {
                //output data that will be written to shapefile
                Object[] data = new Object[3];

                for (int r2 = 0; r2 < adj.getRowCount(); r2++) {
                    for (int c = 0; c < adj.getColumnCount(); c++) {
                        if (adj.getAsDouble(r2, c) > 0) {
                            //set coordinates of line
                            Coordinate coord = new Coordinate(x.getAsDouble(r2, 0), y.getAsDouble(r2, 0));
                            Coordinate coord2 = new Coordinate(x.getAsDouble(c, 0), y.getAsDouble(c, 0));
                            Coordinate[] points = {coord, coord2};

                            //create line
                            LineString ln = gfact.createLineString(points);
                            // create data for line
                            data[0] = ln;
                            data[1] = adj.getAsDouble(r2, c);
                            data[2] = Styles[adjStyled.getAsInt(r2, c)];
                            //add line
                            outE.addData(data);
                        }
                    }
                }
            }

        } else { //no style lines
            for (int r = 0; r < x.getRowCount(); r++) {
                //output to be written
                Object[] data = new Object[1];
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
}
