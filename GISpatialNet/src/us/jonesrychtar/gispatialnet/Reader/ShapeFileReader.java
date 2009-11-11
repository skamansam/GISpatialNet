/*
 * This is the Shapefile reader class
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 *
 */
package us.jonesrychtar.gispatialnet.Reader;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.type.FeatureType;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;

/**
 *
 * @author Charles Bevan
 * @date September 30, 2009
 * @version 0.0.1
 */
public class ShapeFileReader {

    private File file = null;
    private File fileEdge = null;
    private ShapefileDataStore store = null;
    private FeatureCollection featureCollection = null;

    public ShapeFileReader(String filenameNodes, String filenameEdges) {
        fileEdge = new File(filenameEdges);
        file = new File(filenameNodes);
    }
    /**
     * Reads shapefile
     * @return Matrix[] 0=x 1=y 2=adj 3=attb
     * @throws java.lang.Exception
     */
    public Matrix[] Read() throws MalformedURLException, IOException  {
        Matrix x = MatrixFactory.emptyMatrix();
        Matrix y = MatrixFactory.emptyMatrix();
        Matrix attb = MatrixFactory.emptyMatrix();
        Matrix adj = MatrixFactory.emptyMatrix();
        URL shapeURL;

        if (file != null) {
            shapeURL = file.toURI().toURL();
            store = new ShapefileDataStore(shapeURL);
            String[] names = store.getTypeNames(); //filenames
            FeatureSource source = store.getFeatureSource(names[0]);
            featureCollection = source.getFeatures(); // featureCollection queries data of Shapefile
            int featureCount = featureCollection.size();
            //FeatureType type = featureCollection.getSchema(); //gets schema of db
            FeatureIterator fi = featureCollection.features();
            //set up matrix
            x = MatrixFactory.zeros(org.ujmp.core.enums.ValueType.DOUBLE, featureCount, 1);
            y = MatrixFactory.zeros(org.ujmp.core.enums.ValueType.DOUBLE, featureCount, 1);
            attb = MatrixFactory.zeros(org.ujmp.core.enums.ValueType.STRING, featureCount, names.length);
            //copy featureCollection to Matrix
            //set col labels
            for (int i = 0; i < names.length; i++) {
                attb.setColumnLabel(i, names[i]);
            }

            for (int i = 0; i < featureCount; i++) {
                //x,y matrix
                Feature data = fi.next();
                Iterator<? extends Property> iterator = data.getValue().iterator();
                Point pt = (Point) iterator.next().getValue();
                x.setAsDouble(pt.getX(), i, 0);
                y.setAsDouble(pt.getY(), i, 0);

                //attb matrix
                int col = 1;
                while (iterator.hasNext()) {
                    Object temp = iterator.next().getValue();
                    if (temp instanceof java.lang.Double) {
                        attb.setAsDouble((Double) temp, i, col);
                    } else {
                        attb.setAsString((String) temp, i, col);
                    }
                    col++;
                }
            }
        }
        if (fileEdge != null) {
            shapeURL = fileEdge.toURI().toURL();
            store = new ShapefileDataStore(shapeURL);
            String[] names = store.getTypeNames(); //filenames
            FeatureSource source = store.getFeatureSource(names[0]);
            featureCollection = source.getFeatures(); // featureCollection queries data of Shapefile
            int featureCount = (int) x.getRowCount();
            FeatureType type = featureCollection.getSchema(); //gets schema of db
            FeatureIterator fi = featureCollection.features();
            adj = MatrixFactory.zeros(org.ujmp.core.enums.ValueType.DOUBLE, featureCount, featureCount);

            //copy feature collection to adj matrix
            for (int row = 0; row < featureCount; row++) {
                int setr, setc;
                MultiLineString temp = (MultiLineString) fi.next().getValue().iterator().next().getValue();
                Coordinate[] ctemp = temp.getCoordinates();

                //find id number to first x,y pair
                double xn, yn;
                int id1, id2;
                xn = ctemp[0].x;
                yn = ctemp[0].y;
                id1 = _seqSearch(xn, yn, x, y);
                //find id number of second x,y pair
                xn = ctemp[1].x;
                yn = ctemp[1].y;
                id2 = _seqSearch(xn, yn, x, y);
                //set id1, id2 in adj matirx
                adj.setAsDouble(1, id1, id2);
                adj.setAsDouble(1, id2, id1);
            }
        }
        return new Matrix[]{x, y, adj, attb};
    }

    private int _seqSearch(double x, double y, Matrix mapX, Matrix mapY) {
        for (int id = 0; id < mapX.getRowCount(); id++) {
            if (x == mapX.getAsDouble(id, 0) && y == mapY.getAsDouble(id, 0)) {
                return id;
            }
        }
        return -1;
    }
}
