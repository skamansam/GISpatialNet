/**
 * Class that accesses other functions in the algorithm package
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: geotools, ujmp
 */
package us.jonesrychtar.gispatialnet.Algorithm;

import java.io.IOException;
import java.net.MalformedURLException;
import javax.naming.CannotProceedException;
import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
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
        QAP q = new QAP(arg.length - 2, arg);
    }
    /**
     * Highlighrs edges and saves edge shapefile
     * @param alg Which algorithm to use
     * @param filename prefix output filename of edge shapefiles
     * Algorithms:  0 less than average length
     *              1 less than median length
     *              2 more than median length
     *              3 top 10 percent
     * @param nodeFilenamefilename of node shapefile output
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
        if(alg!=5){
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
     * @return
     */
    public static SpatialGraphBase SNB(Matrix x,Matrix y, Matrix adj, double bias){
    	SpatialGraphBase sg=new SpatialGraphBase(x,y,adj);
    	sg.setBias(bias);
    	sg.calculateBiasAndEstimates();
    	return sg;
    }

}
