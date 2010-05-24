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
import java.io.IOException;
import java.net.MalformedURLException;
import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;

import us.jonesrychtar.gispatialnet.DataSet;

/**
 *
 * @author cfbevan
 * @version 0.0.1
 */
public class ShapefileEdgeWriter {

    private ShapefileWriter outE;
    private String schemeEdges;
    private DataSet ds=new DataSet();	//pointer to the dataset we want to work with
    /**
     *Constructor
     * @param filename Name of output file
     * @param xin Vector matrix containing X coordinate data
     * @param yin Vector matrix containing Y coordinate data
     * @param adjin Matrix containing edge values between nodes
     * @throws IllegalArgumentException
     * @throws MalformedURLException
     * @throws IOException
     * @throws SchemaException
     */
    public ShapefileEdgeWriter(String filename, Matrix xin, Matrix yin, Matrix adjin) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException {
       
        this.ds=new DataSet(xin,yin,adjin);
        
        schemeEdges = "*l:LineString,value:Float";

        outE = new ShapefileWriter(filename, schemeEdges);
    }

    public ShapefileEdgeWriter(String edgefilename, DataSet ds) {
		this.ds=new DataSet(ds);
	}

	/**
     * Write data to edge shapefile
     * @throws IOException
     */
    public void write() throws IOException {
        GeometryFactory gfact = new GeometryFactory();
        
            for (int r = 0; r < ds.getX().getRowCount(); r++) {
                //output to be written
                Object[] data = new Object[2];
                //iterate through adj matrix to find edges
                for (int r2 = 0; r2 < ds.getAdj().getRowCount(); r2++) {
                    for (int c = 0; c < ds.getAdj().getColumnCount(); c++) {
                        if (ds.getAdj().getAsDouble(r2, c) > 0) {
                            //create coordinates for found edge
                            Coordinate coord = new Coordinate(ds.getX().getAsDouble(r2, 0), ds.getY().getAsDouble(r2, 0));
                            Coordinate coord2 = new Coordinate(ds.getX().getAsDouble(c, 0), ds.getY().getAsDouble(c, 0));
                            Coordinate[] points = {coord, coord2};
                            LineString ln = gfact.createLineString(points);
                            //create data for edge
                            data[0] = ln;
                            data[1] = ds.getAdj().getAsDouble(r2, c);
                            //write edge
                            outE.addData(data);
                        }
                    }
                }
            }
        }
}
