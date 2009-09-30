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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.LineString;
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
    private Matrix x; //format: id, xcoordinate
    private Matrix y; //format: id, y coordinate
    private Matrix adj; 
    private Matrix attb = null; //format: id, attributes...
    private String schemeNodes;
    private String schemeEdges;

    public convertKnown(Matrix xin, Matrix yin, Matrix adjin, Matrix attbin){
        //setup mapreader and shapewriter
        x = xin;
        y = yin;
        adj = adjin;
        attb = attbin;

        schemeNodes=analyzeScheme(attbin);
        schemeEdges = "*l:LineString";

        outN = new ShapefileWriter("outN",schemeNodes);
        outE = new ShapefileWriter("outE",schemeEdges);
        
        convert();
    }
    public convertKnown(Matrix xin, Matrix yin, Matrix adjin){
        //setup mapreader and shapewriter
        x = xin;
        y = yin;
        adj = adjin;

        schemeNodes= "*geom:Point";
        schemeEdges = "*l:LineString";

        outN = new ShapefileWriter("outN",schemeNodes);
        outE = new ShapefileWriter("outE",schemeEdges);

        convert();
    }
    private void convert() {
        GeometryFactory gfact = new GeometryFactory();
        //write Node Shapefile
        //use X, Y, and attb
        //for each row make coordinate
        for(int r =0; r<x.getRowCount(); r++){
            Object[] data;
            if(attb != null){
                data = new Object[(int)attb.getColumnCount()+1];
            }
            else
                data = new Object[1];
            Coordinate coord = new Coordinate(x.getAsDouble(r,1),y.getAsDouble(r,1));
            Point geo1 = gfact.createPoint(coord);
            data[0]=geo1;
            //add data in attb for row
            if(attb != null){
                for(int j=1; j<attb.getColumnCount(); j++){
                    data[j] = attb.getAsString(r,j);
                }
            }
            outN.addData(data);
        }
        //edge shapefile
        for(int r=0; r<x.getRowCount(); r++){
            Object[] data = new Object[1];
            for(int r2=0; r2<adj.getRowCount(); r2++){
                for(int c=0; c<adj.getColumnCount(); c++){
                    if(adj.getAsDouble(r2,c) > 0){
                        Coordinate coord = new Coordinate(x.getAsDouble(r2,1),y.getAsDouble(r2,1));
                        Coordinate coord2 = new Coordinate(x.getAsDouble(c,1),y.getAsDouble(c,1));
                        Coordinate[] points = {coord,coord2};
                        LineString ln = gfact.createLineString(points);
                        data[0] = ln;
                        outE.addData(data);
                    }
                }
            }
        }               
    }
    private String analyzeScheme(Matrix in){
        String sch="*geom:Point";
        for(int i=0; i<in.getColumnCount(); i++){
            if (in.getColumnObject(i).getClass().isInstance(java.lang.Number.class)) {
                sch += ", " + in.getColumnLabel(i) + ":Float";
            } else {
                sch += ", " + in.getColumnLabel(i) + ":String";
            }
        }
        return sch;
    }
}
