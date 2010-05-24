/**
 * Class that accesses other functions in the algorithm package
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: geotools, ujmp
 */
package us.jonesrychtar.gispatialnet.Algorithm;

import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.CannotProceedException;
import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;

import us.jonesrychtar.gispatialnet.DataSet;
import us.jonesrychtar.gispatialnet.convertKnown;
import us.jonesrychtar.gispatialnet.convertUnknown;
import us.jonesrychtar.gispatialnet.Writer.ShapefileNodeWriter;
import us.jonesrychtar.socialnetwork.SpatialGraph.SpatialGraphBase;

/**
 * @author sam , cfbevan
 *
 */
public class Algorithm {

    //analyzing functions---------------------------------------------------------------------------------
    /**
     * Performes a borders analysis on data, writes out edge shapefile
     * @param filename name of file to use
     * @param alg Algorithm to use
     * Options: 0 - default Borders algorithm
     * @param y y coordinates to use
     * @param x x coordinates to use
     * @param adj adj matrix to use
     * @throws IllegalArgumentException
     * @throws MalformedURLException
     * @throws IOException
     * @throws SchemaException
     */
    public static void Border(String filename, int alg,Matrix x,Matrix y, Matrix adj) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{
        Borders b = new Borders(filename, x,y,adj,alg);
        b.Write();
    }
    /**
     * Performs QAP analysis on data
     * @param arg Examples:
     *  	Simple Mantel test: -s file1 file2 number_of_randomizations
			Partial Mantel test: -p file1 file2 file3 number_of_randomizations
			Options:
			-r partial Mantel test with raw option
			-e force exact permutations procedure
			-l print licence terms
            -h display help
     * @throws IllegalArgumentException
     * @throws IOException
     * @throws Error
     * @throws CannotProceedException
     */
    public static void QAP(String arg[]) throws IllegalArgumentException, IOException, Error, CannotProceedException{
        //QAP q = new QAP(arg.length - 2, arg);
        new QAP(arg.length - 2, arg);
    }
    /**
     * Highlighrs edges and saves edge shapefile
     * @param alg Which algorithm to use
     * @param filename prefix output filename of edge shapefiles
     * Algorithms:  0 less than average length
     *              1 less than median length
     *              2 more than median length
     *              3 top 10 percent
     * @param nodeFilename name of node file
     * @param y y data to use
     * @param x x data to use
     * @param adj adj data to use
     * @throws IllegalArgumentException
     * @throws MalformedURLException
     * @throws IOException
     * @throws SchemaException
     */
    public static void Highlight(int alg, String filename, String nodeFilename,Matrix x,Matrix y, Matrix adj) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{
        ShapefileNodeWriter sfnw = new ShapefileNodeWriter(nodeFilename, x,y);
        sfnw.write();
        if(alg!=4){
            HighlightEdges h = new HighlightEdges(filename, x,y,adj,alg);
            h.write();
        } else{
            HighlightEdgesByVal h = new HighlightEdgesByVal(filename, x, y, adj);
            h.write();
        }
    }

    /**
     * Not supported yet
     * @param x 
     * @param y 
     * @param adj
     * @param bias
     * @return not supported yet
     */
    public static SpatialGraphBase SNB(Matrix x,Matrix y, Matrix adj, double bias){
    	SpatialGraphBase sg=new SpatialGraphBase(x,y,adj);
    	sg.setBias(bias);
    	sg.calculateBiasAndEstimates();
    	return sg;
    }
    
    
    public static void SNB(DataSet ds, double bias){
    	SpatialGraphBase sg=new SpatialGraphBase(ds.getX(),ds.getY(),ds.getAdj());
    	sg.setBias(bias);
    	sg.calculateBiasAndEstimates();
    	ds.setAdj(sg.getA());
    	ds.setX(sg.getX());
    	ds.setY(sg.getY());
    	//return sg;
    }
    public static DataSet SNB(DataSet ds, double bias,boolean b){
    	DataSet d=new DataSet(ds);
    	SpatialGraphBase sg=new SpatialGraphBase(d);
    	sg.setBias(bias);
    	sg.calculateBiasAndEstimates();
    	d.setAdj(sg.getA());
    	d.setX(sg.getX());
    	d.setY(sg.getY());
    	return d;
    }
    
    /**
    *
    * @param dim diminsion of total graph
    */
   public static void CalculateUnknownCoordinates(DataSet ds,Dimension dim){
       if(!ds.hasAdj()){
    	   System.err.println("Cannot convert unknown coordinates without a valid adjacency matrix.");
    	   return;
       }

	   
	   //http://egonet.svn.sourceforge.net/viewvc/egonet/trunk/src/com/endlessloopsoftware/ego/client/graph/ELSFRLayout2.java?view=markup
       Dimension d = dim;
       Random rand = new Random();
       Matrix x=ds.getX();
       Matrix y = ds.getY();
       Matrix adj=ds.getAdj();
       
       int xIsolate=25,yIsolate=0;
       for(int i=0; i<adj.getRowCount(); i++){
           if (_getIncidentEdges(adj,i) != 0) {
               //lock(v,true);

               // can expand to the right
               if (yIsolate + 25 > d.height && xIsolate + 25 <= d.width) {
                   // hit bottom, new column
                   yIsolate = 0;
                   xIsolate += 25;
               } // can't expand down or to the right
               else if (yIsolate + 25 > d.height && xIsolate + 25 > d.width) {
                   // reset entire thing
                   yIsolate = 0;
                   xIsolate = 0;
               } // just go downward first
               else {
                   yIsolate += 25;
               }
               
               //set x,y
               x.setAsDouble(xIsolate, i, 0);
               y.setAsDouble(yIsolate, i, 0);
           } else {
               double xt, yt;
               xt = rand.nextDouble() * d.width;
               yt = rand.nextDouble() * d.height;
               
               //set x,y
               x.setAsDouble(xt, i, 0);
               y.setAsDouble(yt, i, 0);
           }
       }

       //just make sure we set the right ones
       ds.setX(x);
       ds.setY(y);
       ds.setAdj(adj);
   }

   /**
    * Get the number of edges attached to node
    * @param vertex node in graph
    * @return number of edges connected to node
    */
   private static int _getIncidentEdges(Matrix adj,int vertex){
       int out = 0;
       for(int col=0; col<adj.getColumnCount(); col++){
           if(adj.getAsDouble(vertex,col) > 0){
               out++;
           }
       }
       return out;
   }

}
