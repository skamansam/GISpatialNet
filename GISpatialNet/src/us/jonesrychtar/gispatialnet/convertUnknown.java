/*
 * This program will convert network file with unknown geographic coordinates
 * into a shapefile.
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: geotools, convertKnown
 *
 */
package us.jonesrychtar.gispatialnet;

import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;

/**
 *
 * @author cfbevan
 * @version 0.0.1
 */
public class convertUnknown {

    private Matrix x; //format: id, xcoordinate
    private Matrix y; //format: id, y coordinate
    private Matrix adj; //adjancency matrix
    convertKnown ck; //handles the writing of shapefile

    /**
     * Constructor
     * @param filenameE name of edge shapefile
     * @param filenameN name of node shapefile
     * @param adjin adjacency matrix to use
     * @param attbin attribute matrix to use
     * @param alg algorithm to use to create xy data
     * @param args extra arguments to algorithms
     * @throws IllegalArgumentException
     * @throws MalformedURLException
     * @throws IOException
     * @throws SchemaException
     */
    public convertUnknown(String filenameE, String filenameN, Matrix adjin, Matrix attbin, int alg, Dimension args) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{
        //set adj
        adj = adjin;

        //set x, y
        x = MatrixFactory.zeros(adj.getRowCount(),1);
        y = MatrixFactory.zeros(adj.getRowCount(),1);
        _use(alg,args);
        
        //make convert known
        ck = new convertKnown(filenameE, filenameN, x,y,adjin, attbin);
    }


    /**
     * Constructor
     * @param filenameE name of edge shapefile
     * @param filenameN name of node shapefile
     * @param adjin adjacency matrix to use
     * @param alg algorithm to use to create xy data
     * @param args args extra arguments to algorithms
     */
    public convertUnknown(String filenameE, String filenameN, Matrix adjin, int alg, Dimension args){
        //set adj
        adj = adjin;

        //set x, y
        x = MatrixFactory.zeros(adj.getRowCount(),1);
        y = MatrixFactory.zeros(adj.getRowCount(),1);
        _use(alg,args);

        try {
            //make convert known
            ck = new convertKnown(filenameE, filenameN, x, y, adjin);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(convertUnknown.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(convertUnknown.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(convertUnknown.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SchemaException ex) {
            Logger.getLogger(convertUnknown.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Helper function that decides which layout algorightm to use
     * @param algorithm chooses which algorithm
     * Options:
     *      0 egonetAlgorithm
     * @param args Dimension of final output map
     */
    private void _use(int algorithm, Dimension args) {
        switch(algorithm){
            case 0: _egonet(args);
            break;
            default: _egonet(args);
            break;
        }
    }

    /**
     *
     * @param dim diminsion of total graph
     */
    private void _egonet(Dimension dim){
        //http://egonet.svn.sourceforge.net/viewvc/egonet/trunk/src/com/endlessloopsoftware/ego/client/graph/ELSFRLayout2.java?view=markup
        Dimension d = dim;
        Random rand = new Random();

        int xIsolate=25,yIsolate=0;
        for(int i=0; i<adj.getRowCount(); i++){
            if (_getIncidentEdges(i) != 0) {
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
    }
    /**
     * Get the number of edges attached to node
     * @param vertex node in graph
     * @return number of edges connected to node
     */
    private int _getIncidentEdges(int vertex){
        int out = 0;
        for(int col=0; col<adj.getColumnCount(); col++){
            if(adj.getAsDouble(vertex,col) > 0){
                out++;
            }
        }
        return out;
    }
}
