/*
 * This is the Shapefile reader class
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 *
 */
package us.jonesrychtar.gispatialnet.Reader;

import com.vividsolutions.jts.geom.Point;
import java.io.File;
import java.net.URL;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
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

    private File file;
    private ShapefileDataStore store = null;
    private FeatureCollection featureCollection = null;

    public ShapeFileReader(String filename){
        file = new File(filename);
    }
    public Matrix[] Read(){
        Matrix x = MatrixFactory.emptyMatrix();
        Matrix y = MatrixFactory.emptyMatrix() ;
        Matrix attb = MatrixFactory.emptyMatrix();
        Matrix adj = MatrixFactory.emptyMatrix();
        URL shapeURL;

        try{
            shapeURL = file.toURI().toURL();
            store = new ShapefileDataStore(shapeURL);
            String[] names = store.getTypeNames(); //filenames
            FeatureSource source = store.getFeatureSource(names[0]);
            featureCollection = source.getFeatures(); // featureCollection queries data of Shapefile
            int featureCount = featureCollection.size();
            FeatureType type = featureCollection.getSchema(); //gets schema of db
            FeatureIterator fi = featureCollection.features();
            //set up matrix
            x = MatrixFactory.zeros(org.ujmp.core.enums.ValueType.FLOAT,featureCount,2);
            y = MatrixFactory.zeros(org.ujmp.core.enums.ValueType.FLOAT,featureCount,2);
            adj = MatrixFactory.zeros(org.ujmp.core.enums.ValueType.DOUBLE,featureCount, featureCount);
            attb = MatrixFactory.zeros(org.ujmp.core.enums.ValueType.STRING, featureCount, names.length );
            //copy featureCollection to Matrix
            //set col labels
            for (int i=0; i<names.length; i++){
                attb.setColumnLabel(i, names[i]);
            }
            //TESTING DATA--------------------------------
            //FeatureIterator fi = featureCollection.features();
            //for (int i=0; i<featureCount; i++){
            //    Point out =(Point) fi.next().getValue().iterator().next().getValue();
            //    System.out.println(out.getX()+","+out.getY());
            //}
            //TESTING DATA--------------------------------
            
            for(int i=0; i<featureCount; i++){
                //x,y matrix
                Feature data = fi.next();
                Point pt = (Point)data.getValue().iterator().next().getValue();
                x.setAsDouble(i,i,0);
                x.setAsDouble(pt.getX(), i, 1);
                x.setAsDouble(i,i,0);
                y.setAsDouble(pt.getY(),i, 1);

                //attb matrix
                Object[] out = data.getValue().toArray();
                
            }
            //adj matrix

        }catch(Exception e){
            e.printStackTrace();
        }

        return new Matrix[] {x,y,adj,attb};
    }

}
