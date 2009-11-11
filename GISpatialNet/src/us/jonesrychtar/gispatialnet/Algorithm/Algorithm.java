/**
 * 
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
     * @param alg Algorithm to use
     * Options: 0 - default Borders algorithm
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
     */
    public static void QAP(String arg[]) throws IllegalArgumentException, IOException, Error, CannotProceedException{
        QAP q = new QAP(arg.length - 2, arg);
    }
    /**
     * Highlighrs edges and saves edge shapefile
     * @param alg Which algorithm to use
     * @param filename output filename
     * Algorithms:  0 less than average length
     *              1 less than median length
     *              2 more than median length
     *              3 top 10 percent
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
     * @throws javax.naming.OperationNotSupportedException
     */
    public static SpatialGraphBase SNB(Matrix x,Matrix y, Matrix adj, double bias){
    	SpatialGraphBase sg=new SpatialGraphBase(x,y,adj);
    	sg.setBias(bias);
    	sg.calculateBiasAndEstimates();
    	return sg;
    }

}
