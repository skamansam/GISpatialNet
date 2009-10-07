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
import java.util.Random;
import org.ujmp.core.Matrix;

/**
 *
 * @author cfbevan
 * @date October 5, 2009
 * @version 0.0.1
 */
public class convertUnknown {

    private Matrix x; //format: id, xcoordinate
    private Matrix y; //format: id, y coordinate
    private Matrix adj; //adjancency matrix
    convertKnown ck; //handles the writing of shapefile

    public convertUnknown(Matrix adjin, Matrix attbin, int alg, Dimension args){
        //set x, y
        _use(alg,args);

        //set adj
        adj = adjin;
        
        //make convert known
        ck = new convertKnown(x,y,adjin, attbin);
    }

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
                //set x,y
                x.setAsDouble(rand.nextDouble() * d.width, i, 0);
                y.setAsDouble(rand.nextDouble() * d.height, i, 0);
            }
        }
    }
    /**
     *
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
