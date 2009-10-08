/*
 * This will write a node with attributes shapefile
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP
 */

package us.jonesrychtar.gispatialnet.Writer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.ujmp.core.Matrix;

/**
 *
 * @author cfbevan
 * @date Oct 8, 2009
 * @version 0.0.1
 */
public class ShapefileNodeWriter {

    private ShapefileWriter outN;
    private Matrix x; //format: xcoordinate
    private Matrix y; //format: y coordinate
    private Matrix attb = null; //format: attributes...
    private String schemeNodes;

    /**
     *Constructor
     * @param filename Name of output file
     * @param xin Vector matrix of x coordinate values
     * @param yin Vector matrix of y coordinate values
     * @param attbin Matrix containing attribute data for nodes
     */
    public ShapefileNodeWriter(String filename, Matrix xin, Matrix yin, Matrix attbin){

        x = xin;
        y = yin;
        attb = attbin;
        schemeNodes=_analyzeScheme(attbin);
        outN = new ShapefileWriter(filename,schemeNodes);
    }

    /**
     * Constructor (no attributes)
     * @param filename Name of output file
     * @param xin Vector matrix of x coordinate values
     * @param yin Vector matrix of y coordinate values
     */
    public ShapefileNodeWriter(String filename, Matrix xin, Matrix yin){

        x = xin;
        y = yin;
        schemeNodes= "*geom:Point";
        outN = new ShapefileWriter(filename,schemeNodes);
    }

    /**
     * Writes node data to shapefile
     */
    public void write(){
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
            Coordinate coord = new Coordinate(x.getAsDouble(r,0),y.getAsDouble(r,0));
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
    }

    /**
     * Analyzes scheme of attribute data
     * @param in MAtrix to analyze
     * @return String containing the scheme
     */
    private String _analyzeScheme(Matrix in){
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
