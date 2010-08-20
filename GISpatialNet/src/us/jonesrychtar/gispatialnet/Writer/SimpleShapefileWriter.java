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
import com.vividsolutions.jts.geom.LineString;
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
	//private FeatureCollection collection = FeatureCollections.newCollection();
	//private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
	//private SimpleFeatureBuilder featureBuilder;

	private String nodeSchemaString = "location:Point:srid=4326,Node_ID:String";
//	private String nodeSchemaString = "location:Point:srid=15498,id:String";
	private String nodeSchemaName = "PersonWithID";
	
	private String edgeSchemaString = "edge:Line,From:String,To:String";
	private String edgeSchemaName = "EdgesWithConnections";

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
	public SimpleShapefileWriter(String filenames, DataSet ds) {
		this.nodeFile = new File(filenames+".shp");
		this.edgeFile = new File(filenames+"_edges.shp");
		this.ds = ds;
		this.attCount=(int) (ds.getAttb().getColumnCount()+1);

	}
	public SimpleShapefileWriter(String nodeFile, String edgeFile,DataSet ds) {
		this.nodeFile = new File(nodeFile+".shp");
		this.edgeFile = new File(edgeFile+".shp");
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

	public void makeNodeFeatureType() {
	}
	
	private FeatureCollection getEdgeCollection(SimpleFeatureBuilder featureBuilder){

		FeatureCollection collection = FeatureCollections.newCollection();
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

        for (int row = 0; row < ds.getX().getRowCount(); row++) {
            //output to be written
            Object[] data = new Object[2];
            //iterate through adj matrix to find edges
            for (int adjRow = 0; adjRow < ds.getAdj().getRowCount(); adjRow++) {
                for (int adjCol = 0; adjCol < ds.getAdj().getColumnCount(); adjCol++) {
                    if (ds.getAdj().getAsDouble(adjRow, adjCol) > 0) {
                        //create coordinates for found edge
                        Coordinate from = new Coordinate(ds.getX().getAsDouble(adjRow, 0), ds.getY().getAsDouble(adjRow, 0));
                        Coordinate to = new Coordinate(ds.getX().getAsDouble(adjCol, 0), ds.getY().getAsDouble(adjCol, 0));
                        Coordinate[] points = {from, to};
                        LineString line = geometryFactory.createLineString(points);
                        //create data for edge
                        /*data[0] = ln;
                        data[1] = ds.getAdj().getAsDouble(adjRow, adjCol);*/
                        //write edge
                        featureBuilder.add(line);
                        featureBuilder.add(ds.getAttr().getAsString(row,this.idCol));
                        featureBuilder.add(line);
                    }
                }
            }
        }

		return collection;
	}
	
	public void writeEdges() {

		if(!ds.hasAdj())
			System.out.println("DataSet does not have an adjacency matrix.");
		try {
			SimpleFeatureBuilder edgeFeatures=new SimpleFeatureBuilder(DataUtilities.createType(edgeSchemaName,edgeSchemaString));

			DataStoreFactorySpi dataStoreFactory = new ShapefileDataStoreFactory();

			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("url", nodeFile.toURI().toURL());
			params.put("create spatial index", Boolean.TRUE);

			ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
			newDataStore.createSchema(calculateNodeFeatureType());

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
					featureStore.addFeatures(getEdgeCollection(edgeFeatures));
					transaction.commit();

				} catch (Exception problem) {
					problem.printStackTrace();
					transaction.rollback();

				} finally {
					transaction.close();
				}
			}
		} catch (IOException e) {
			System.err.println("Sorry! Cannot save file: " + nodeFile);
		} catch (SchemaException e) {
			System.err.println("Sorry! Invalid edge schema specified!");
		} finally {
		}


	}
	
	private FeatureCollection getNodeCollection(SimpleFeatureBuilder featureBuilder){

		FeatureCollection collection = FeatureCollections.newCollection();
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		
		for (int row = 0; row < ds.getX().getRowCount(); row++) {
			// storing points
			double x = ds.getX().getAsDouble(row, 0);
			double y = ds.getY().getAsDouble(row, 0);
			String id = ds.getAttb().getAsString(row, this.idCol);
			Point point = geometryFactory.createPoint(new Coordinate(x, y));
			featureBuilder.add(point);
			featureBuilder.add(id);
			for (int col = 0; col < this.ds.getAttb().getColumnCount(); col++)
				if (col != this.idCol)
					featureBuilder.add(ds.getAttb().getAsString(row, col));
			SimpleFeature feature = featureBuilder.buildFeature(null);
			collection.add(feature);
		}
		return collection;

	}
	private SimpleFeatureType calculateNodeFeatureType(){
		SimpleFeatureType NodeFeatureType = null;
		//the number of attributes set to 1 for testing.
		//this.attCount=1;
		// add more info if applicable
		if (this.ds.getAttb().getColumnCount() > 1) {
			nodeSchemaName = "PersonWithAttributes";
			for (int i = 0; i < this.ds.getAttb().getColumnCount(); i++)
				if (i != this.idCol){
					String lbl=ds.getAttr().getColumnLabel(i);
					if (lbl==null) lbl="Att"+i;
					nodeSchemaString += "," + lbl + ":String";
				}
		}
		System.out.println("Creating Schema:\n\t"+nodeSchemaString);
		try{
			NodeFeatureType = DataUtilities.createType(nodeSchemaName,nodeSchemaString);
		} catch (SchemaException e) {
			System.err.println("Sorry! Invalid node schema specified!");
			System.err.println(e.toString());
		}
		return NodeFeatureType;
	}

	public void writeNodes_old() {
		SimpleFeatureBuilder nodeFeatures=new SimpleFeatureBuilder(calculateNodeFeatureType());
		
		// add more info if applicable
/*		if (this.attCount > 1) {
			nodeSchemaName = "PersonWithAttributes";
			for (int i = 0; i < this.attList.size(); i++)
				if (i != this.idCol)
					nodeSchemaString += "," + ds.getAttr().getColumnLabel(i) + ":String";
		}
*/
		try {
/*			SimpleFeatureType NodeFeatureType = DataUtilities.createType(nodeSchemaName,nodeSchemaString);
			SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(NodeFeatureType);
*/
//			FeatureCollection collection = FeatureCollections.newCollection();
//			GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

/*			for (int row = 0; row < ds.getX().getRowCount(); row++) {
				// storing points
				double x = ds.getX().getAsDouble(row, 0);
				double y = ds.getY().getAsDouble(row, 0);
				String id = ds.getAttb().getAsString(row, this.idCol);
				Point point = geometryFactory.createPoint(new Coordinate(x, y));
				featureBuilder.add(point);
				featureBuilder.add(id);
				for (int col = 0; col < this.attList.size(); col++)
					if (col != this.idCol)
						featureBuilder.add(ds.getAttb().getAsString(row, col));
				SimpleFeature feature = featureBuilder.buildFeature(null);
				collection.add(feature);
			}
*/

			DataStoreFactorySpi dataStoreFactory = new ShapefileDataStoreFactory();

			Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put("url", nodeFile.toURI().toURL());
			params.put("create spatial index", Boolean.TRUE);

			ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
			newDataStore.createSchema(calculateNodeFeatureType());

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
					featureStore.addFeatures(getNodeCollection(nodeFeatures));
					transaction.commit();

				} catch (Exception problem) {
					problem.printStackTrace();
					transaction.rollback();

				} finally {
					transaction.close();
				}
			}
		} catch (IOException e) {
			System.err.println("Sorry! Cannot save file: " + nodeFile);
		} finally {
		}

	}

	public void writeNodes() {

        try {
    		final SimpleFeatureType TYPE = DataUtilities.createType(this.nodeSchemaName, this.nodeSchemaString);
    		
    		/*
             * We create a FeatureCollection into which we will put each Feature created from a record
             * in the input csv data file
             */
            FeatureCollection collection = FeatureCollections.newCollection();

            /*
             * GeometryFactory will be used to create the geometry attribute of each feature (a Point
             * object for the location)
             */
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

            SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

            for (int row=0;row<this.ds.getX().getRowCount();row++) {
                    double xcoord = this.ds.getX().getAsDouble(row,0);
                    double ycoord = this.ds.getY().getAsDouble(row,0);
                    String name = this.ds.getAttb().getAsString(row,0);

                    /* Longitude (= x coord) first ! */
                    Point point = geometryFactory.createPoint(new Coordinate(xcoord, ycoord));

                    featureBuilder.add(point);
                    featureBuilder.add(name);
                    SimpleFeature feature = featureBuilder.buildFeature(null);
                    collection.add(feature);
            }
            DataStoreFactorySpi dataStoreFactory = new ShapefileDataStoreFactory();

            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put("url", nodeFile.toURI().toURL());
            params.put("create spatial index", Boolean.TRUE);

            ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory
                    .createNewDataStore(params);
            newDataStore.createSchema(TYPE);

            /*
             * You can comment out this line if you are using the createFeatureType
             * method (at end of class file) rather than DataUtilities.createType
             */
            newDataStore.forceSchemaCRS(DefaultGeographicCRS.WGS84);

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
            } else {
                System.out.println(typeName + " does not support read/write access");
            }
        }catch(Exception e){
        	System.out.println("Cannot create features!\n"+e.getMessage());
        }
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataSet d=new DataSet();

	}
}
