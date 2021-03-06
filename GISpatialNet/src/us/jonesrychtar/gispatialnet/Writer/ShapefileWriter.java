/*
 * This is a shapefile writer. Note that all input must match
 * the originally defined scheme.
 * 
 * For research by Eric Jones and Jan Rychtar.
 * 
 * Requires: geotools
 * 
 */
package us.jonesrychtar.gispatialnet.Writer;
//TODO: make export tie down to location
/**
 *
 * @author Charles Bevan, Samuel Tyler
 * @date September 10, 2009, May 24, 2010
 * @version 0.0.2
 */
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.simple.SimpleFeature;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;

import us.jonesrychtar.gispatialnet.DataSet;
import us.jonesrychtar.gispatialnet.convertKnown;
import us.jonesrychtar.gispatialnet.convertUnknown;

/**
 *
 * @author cfbevan
 */
public class ShapefileWriter {

    private File file;
    private DataStore myData;
    private FeatureStore<SimpleFeatureType, SimpleFeature> store;
    private FeatureCollection<SimpleFeatureType, SimpleFeature> collection;
    private String scheme;
    private DataSet ds=new DataSet();	//pointer to the dataset we want to work with
    String filename="shapefile";

    /**
     * Default constructor, used for testing
     */
    //public ShapefileWriter() {
    //    createfile("out", "*geom:Point,name:String");
    //}
    /**
     * @param filename the name of the output shapefile
     * @param scheme the format of the database (list of field names and field Types) ex:"geom:Point,name:String"
     * @throws IllegalArgumentException
     * @throws SchemaException
     * @throws MalformedURLException
     * @throws IOException
     * */
    public ShapefileWriter(String filename, String scheme) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException {
            createfile(filename, scheme);
    }

    /**
     * @param name name of the output file without extenstion
     * @param format scheme from above
     * */
    private void createfile(String name, String format) throws MalformedURLException, IOException, SchemaException {
        //factory to format shapefile data
        FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
        //make the file
        file = new File(name + ".shp");
        //map to store data in memory until it is written to file
        //TODO: make this warning-free by making map the correct type: Map<String,URL> so it works with factory.createNewDataStore(map)
        Map map = Collections.singletonMap("url", file.toURI().toURL());
        //use factory and map to make a datastore
        myData = factory.createNewDataStore(map);
        //Feature type defines the scheme of data
        SimpleFeatureType featureType = DataUtilities.createType("my", format);
        myData.createSchema(featureType);
    }

    /**
     * @param input an array of objects, objects must be in same order as defined scheme.
     * ex: scheme="geom:Point,name:String" then input will be [Point,String]
     * @throws IOException
     * */
    public void addData(Object[] input) throws IOException {
        //get object that holds temp data
        FeatureSource<SimpleFeatureType, SimpleFeature> source = myData.getFeatureSource("my");
        store = (FeatureStore<SimpleFeatureType, SimpleFeature>) source;
        //create collection to hold new data
        collection = (FeatureCollection<SimpleFeatureType, SimpleFeature>) FeatureCollections.newCollection();
        SimpleFeatureType type = store.getSchema();
        //add data to builder, builder formats data correctly
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        for (int i = 0; i < input.length; i++) {
            builder.add(input[i]);
        }
        //build data to collection
        collection.add((SimpleFeature) builder.buildFeature("my1"));
        write();
    }

    public void setFilename(String fn){this.filename=fn;}
    /**
     * Writes data to shapefile
     */
    private void write() throws IOException {
        //transaction makes writing faster and safer with ability to rollback
        DefaultTransaction transaction = new DefaultTransaction("Add");
         store.setTransaction(transaction);
        try {
            //try to write data to file
            store.addFeatures(collection);
            transaction.commit();
        //System.out.print("transaction successful.\n");
        } catch (Exception eek) {
            transaction.rollback();
            //System.err.print("Transaction failed.\n");
            eek.printStackTrace();
        }

        transaction.close();
    }
    
    public void writeEdges(String filename) throws IOException{}
    public void writeNodes(String filename) throws IOException{}
    
}
