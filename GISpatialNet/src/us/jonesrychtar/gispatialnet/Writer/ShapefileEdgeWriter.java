/*
 * This will write an edge shapefile with or without style
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

   public ShapefileEdgeWriter(Matrix xin, Matrix yin, Matrix adjin, Matrix adjStyledin, Style[] Style){

        x = xin;
        y = yin;
        adj = adjin;
        hasStyle = true;
        Styles = Style;
        adjStyled = adjStyledin;
        schemeEdges = "*l:LineString,value:Number,style:Style";

        outE = new ShapefileWriter("outE",schemeEdges);

    }
    public ShapefileEdgeWriter(Matrix xin, Matrix yin, Matrix adjin){

        x = xin;
        y = yin;
        adj = adjin;
        hasStyle = false;
        schemeEdges = "*l:LineString,value:Number";

        outE = new ShapefileWriter("outE",schemeEdges);
    }

     public void write(){
         GeometryFactory gfact = new GeometryFactory();
        if(hasStyle){
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

                        data[0] = ln;
                        data[1] = adj.getAsDouble(r2,c);
                        data[2]=Styles[adjStyled.getAsInt(r2,c)];
                        outE.addData(data);
                    }
                }
            }
        }

        }
        else{
            for(int r=0; r<x.getRowCount(); r++){
                Object[] data = new Object[1];
                for(int r2=0; r2<adj.getRowCount(); r2++){
                    for(int c=0; c<adj.getColumnCount(); c++){
                        if(adj.getAsDouble(r2,c) > 0){
                            Coordinate coord = new Coordinate(x.getAsDouble(r2,0),y.getAsDouble(r2,0));
                            Coordinate coord2 = new Coordinate(x.getAsDouble(c,0),y.getAsDouble(c,0));
                            Coordinate[] points = {coord,coord2};
                            LineString ln = gfact.createLineString(points);
                            data[0] = ln;
                            data[1] = adj.getAsDouble(r2,c);
                            outE.addData(data);
                        }
                    }
                }
            }
        }
     }

}
