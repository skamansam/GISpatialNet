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
import java.awt.Color;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
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
        shpout = new ShapefileWriter(filename,"*line:LineString, style:Style");
        Alg = Algorithm;
        
    }
    private boolean _Highlight(double edge){
        boolean ans;
        switch(Alg){
            case 0: ans = _border1();
                break;
            default:
                return false;
        }
        return ans;
    }
    public void Write(){
        //line shape and style builders
        GeometryFactory gfact = new GeometryFactory();
        StyleBuilder builder = new StyleBuilder();
        Style origLineStyle = builder.createStyle(builder.createLineSymbolizer(Color.BLACK,1));
        Style linestyle = builder.createStyle(builder.createLineSymbolizer(Color.GREEN, 2));

        for (int r = 0; r < X.getRowCount(); r++) {
            //output data that will be written to shapefile
            Object[] data = new Object[2];

            for (int r2 = 0; r2 < Adj.getRowCount(); r2++) {
                for (int c = 0; c < Adj.getColumnCount(); c++) {
                    if (Adj.getAsDouble(r2, c) > 0) {
                        //set coordinates of line
                        Coordinate coord = new Coordinate(X.getAsDouble(r2, 0), Y.getAsDouble(r2, 0));
                        Coordinate coord2 = new Coordinate(X.getAsDouble(c, 0), Y.getAsDouble(c, 0));
                        Coordinate[] points = {coord, coord2};

                        //create line
                        LineString ln = gfact.createLineString(points);
                        if(_Highlight(Adj.getAsDouble(r2,c))){
                            //highlight line ln
                           data[1]= linestyle;
                        }
                        else
                            data[1] = origLineStyle;
                        data[0] = ln;
                        shpout.addData(data);
                    }
                }
            }
        }
    }

    private boolean _border1(){
        //TODO: Source code request in progress
        return false;
    }
}
