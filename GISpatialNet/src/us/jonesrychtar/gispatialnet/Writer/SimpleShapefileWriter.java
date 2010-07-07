/**
 * 
 */
package us.jonesrychtar.gispatialnet.Writer;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import us.jonesrychtar.gispatialnet.DataSet;

/**
 * @author sctyler
 *
 */
public class SimpleShapefileWriter {
	private File nodeFile=new File("nodes.shp");
	private File edgeFile=new File("edges.shp");
	private DataSet ds;
	private SimpleFeatureType schema;
	private FeatureCollection collection = FeatureCollections.newCollection();
	private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
	private SimpleFeatureBuilder featureBuilder;

	/**
	 * @param nodeFile
	 * @param edgeFile
	 * @param ds
	 */
	public SimpleShapefileWriter(File simpleFile, DataSet ds) {
		this.nodeFile = simpleFile;
		this.edgeFile = simpleFile;
		this.ds = ds;
	}
	public SimpleShapefileWriter(File nodeFile, File edgeFile, DataSet ds) {
		this.nodeFile = nodeFile;
		this.edgeFile = edgeFile;
		this.ds = ds;
	}
	private void write(){
		try {
			SimpleFeatureType PersonWithIDType = DataUtilities.createType("PersonWithID", // <- the  name for our feature type
					"location:Point:srid=4326," + // <- the geometry attribute: Point type
							"id:String" // <- a String attribute
			);
			featureBuilder = new SimpleFeatureBuilder(PersonWithIDType);
			for(int i=0;i<ds.getX().getRowCount();i++){
				//storing points
				double x=ds.getX().getAsDouble(0,i);
				double y=ds.getY().getAsDouble(0,i);
				String id=ds.getAttb().getAsString(0,i);
				Point point = geometryFactory.createPoint(new Coordinate(x, y));
				featureBuilder.add(point);
				featureBuilder.add(id);
				SimpleFeature feature = featureBuilder.buildFeature(null);
				collection.add(feature);

			}
		} catch (SchemaException e) {
			System.err.println("Sorry! Invalis schema specified!");
		}finally{
			
		}

	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataSet d=new DataSet();

	}
}
