/*
 * This program will convert network file with known geographic coordinates
 * into a shapefile and highlight the borders acording to a specified constraint.
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: geotools,
 *
 */
package us.jonesrychtar.gispatialnet;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import org.ujmp.core.Matrix;
import us.jonesrychtar.gispatialnet.Writer.ShapefileWriter;

/**
 *
 * @author cfbevan
 */
public class Borders {
    private Matrix X;
    private Matrix Y;
    private Matrix Adj;
    private ShapefileWriter shpout;
    int Alg = 0;
    public Borders(Matrix x, Matrix y, Matrix adj, String filename, int Algorithm){
        X = x;
        Y = y;
        Adj = adj;
        shpout = new ShapefileWriter(filename,"*line:LineString");
        Alg = Algorithm;
        
    }
    private boolean _Highlight(double edge){

        switch(Alg){
            case 0:
                break;
            default:
                return false;
        }
        return false;
    }
    public void Write(){
        GeometryFactory gfact = new GeometryFactory();
        for (int r = 0; r < X.getRowCount(); r++) {
            Object[] data = new Object[1];
            for (int r2 = 0; r2 < Adj.getRowCount(); r2++) {
                for (int c = 0; c < Adj.getColumnCount(); c++) {
                    if (Adj.getAsDouble(r2, c) > 0) {
                        Coordinate coord = new Coordinate(X.getAsDouble(r2, 0), Y.getAsDouble(r2, 0));
                        Coordinate coord2 = new Coordinate(X.getAsDouble(c, 0), Y.getAsDouble(c, 0));
                        Coordinate[] points = {coord, coord2};
                        LineString ln = gfact.createLineString(points);
                        if(_Highlight(Adj.getAsDouble(r2,c))){
                            ln.set
                        }
                        data[0] = ln;
                        shpout.addData(data);
                    }
                }
            }
        }
    }
}
