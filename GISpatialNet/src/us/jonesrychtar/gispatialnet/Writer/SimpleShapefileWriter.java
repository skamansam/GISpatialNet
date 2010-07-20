/**
 * 
 */
package us.jonesrychtar.gispatialnet.Writer;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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

	private List<Integer> attList;
	private int attCount=1; 		//should always be attList.length()+1
	private int idCol=0;			//the index of the column that contains the ID
	
	/**
	 * @param nodeFile
	 * @param edgeFile
	 * @param ds
	 */
	public SimpleShapefileWriter(File simpleFile, DataSet ds) {
		this.nodeFile = simpleFile;
		this.edgeFile = simpleFile;
		this.ds = ds;
		this.attCount=(int) (ds.getAttb().getColumnCount()+1);
	}
	public SimpleShapefileWriter(File nodeFile, File edgeFile, DataSet ds) {
		this.nodeFile = nodeFile;
		this.edgeFile = edgeFile;
		this.ds = ds;
		this.attCount=(int) (ds.getAttb().getColumnCount()+1);
	}
	
	/**
	 * @return the attList
	 */
	public List<Integer> getAttList() {return attList;}
	/**
	 * @param attList the attList to set
	 */
	public void setAttList(List<Integer> attList) {this.attList = attList;}
	/**
	 * @return the attCount
	 */
	public int getAttCount() {return attCount;}
	/**
	 * @param attCount the attCount to set
	 */
	public void setAttCount(int attCount) {this.attCount = attCount;}
	/**
	 * @return the nodeFile
	 */
	public File getNodeFile() {return nodeFile;}
	/**
	 * @param nodeFile the nodeFile to set
	 */
	public void setNodeFile(File nodeFile) {this.nodeFile = nodeFile;}
	/**
	 * @return the edgeFile
	 */
	public File getEdgeFile() {return edgeFile;}
	/**
	 * @param edgeFile the edgeFile to set
	 */
	public void setEdgeFile(File edgeFile) {this.edgeFile = edgeFile;}
	/**
	 * @return the ds
	 */
	public DataSet getData() {return ds;}
	/**
	 * @param ds the ds to set
	 */
	public void setData(DataSet ds) {this.ds = ds;}
	/**
	 * @return the schema
	 */
	public SimpleFeatureType getSchema() {return schema;}
	/**
	 * @param schema the schema to set
	 */
	public void setSchema(SimpleFeatureType schema) {this.schema = schema;}

	public void writeNodes(){}
	public void writeEdges(){}
	public void writeAttributes(){}
	
	public void write(){
		try {
			SimpleFeatureType PersonWithIDType = DataUtilities.createType("PersonWithID", // <- the  name for our feature type
					"location:Point:srid=4326," + // <- the geometry attribute: Point type
							"id:String" // <- a String attribute
			);
			featureBuilder = new SimpleFeatureBuilder(PersonWithIDType);
			for(int i=0;i<ds.getX().getRowCount();i++){
				//storing points
				double x=ds.getX().getAsDouble(i,0);
				double y=ds.getY().getAsDouble(i,0);
				String id=ds.getAttb().getAsString(i,0);
				Point point = geometryFactory.createPoint(new Coordinate(x, y));
				featureBuilder.add(point);
				featureBuilder.add(id);
				SimpleFeature feature = featureBuilder.buildFeature(null);
				collection.add(feature);

			}
		
			DataStoreFactorySpi dataStoreFactory = new ShapefileDataStoreFactory();
	
			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("url", nodeFile.toURI().toURL());
			params.put("create spatial index", Boolean.TRUE);
	
			ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory
					.createNewDataStore(params);
			newDataStore.createSchema(PersonWithIDType);
	
			/*
			 * You can comment out this line if you are using the createFeatureType
			 * method (at end of class file) rather than DataUtilities.createType
			 */
			//newDataStore.forceSchemaCRS(DefaultGeographicCRS. .WGS84);
			/*
			 * Write the features to the shapefile
			 */
			Transaction transaction = new DefaultTransaction("create");
	
			String typeName = newDataStore.getTypeNames()[0];
			FeatureSource featureSource = newDataStore.getFeatureSource(typeName);
	
			if (featureSource instanceof FeatureStore) {
				FeatureStore featureStore = (FeatureStore) featureSource;
	
				featureStore.setTransaction(transaction);
				try {
					featureStore.addFeatures(collection);
					transaction.commit();
	
				} catch (Exception problem) {
					problem.printStackTrace();
					transaction.rollback();
	
				} finally {
					transaction.close();
				}
			}
		} catch (SchemaException e) {
			System.err.println("Sorry! Invalid schema specified!");
		} catch (IOException e) {
			System.err.println("Sorry! Cannot save file: "+nodeFile);
		}finally{}

	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataSet d=new DataSet();

	}
}
