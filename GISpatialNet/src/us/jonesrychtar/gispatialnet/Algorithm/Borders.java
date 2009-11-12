/*
 * This program will perform a borders algorithm on data and output shapefile.
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: geotools, ujmp
 *
 */
package us.jonesrychtar.gispatialnet.Algorithm;

import java.io.IOException;
import java.net.MalformedURLException;
import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import us.jonesrychtar.gispatialnet.Writer.ShapefileEdgeWriter;

/**
 *
 * @author cfbevan
 */
public class Borders {
    private Matrix X;
    private Matrix Y;
    private Matrix Adj, AdjH;
    int Alg = 0;
    String filenameE, filenameEH;
    /**
     *
     * @param filenameEdgesin Name of output edgefile name
     * @param x vector matrix of x values for nodes
     * @param y vector matrix of y values for nodes
     * @param adj adjacency matrix for edge values
     * @param Algorithm which algorithm to use:
     *          0 default border algorithm
     *          1 ...
     */
    public Borders(String filenameEdgesin, Matrix x, Matrix y, Matrix adj, int Algorithm){
        X = x;
        Y = y;
        Adj = adj;
        Alg = Algorithm;
        filenameE = filenameEdgesin;
        filenameEH = filenameEdgesin+"Highlighted";
    }
    /**
     *
     * @param edge number value on edge
     * @return does edge need to be highlighted
     */
    private boolean _Highlight(double edge){
        boolean ans;
        switch(Alg){
            case 0: ans = _border1();
                break;
            default:
                return false;
        }
        return ans;
    }
    /**
     * writes data to edgeshapefile
     * @throws IllegalArgumentException
     * @throws MalformedURLException
     * @throws IOException
     * @throws SchemaException
     */
    public void Write() throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{
        //prepare Adj matricies
        AdjH = MatrixFactory.zeros( org.ujmp.core.enums.ValueType.DOUBLE, Adj.getRowCount(), Adj.getColumnCount());
        for(int row=0; row < Adj.getRowCount(); row++){
            for(int col=0; col < Adj.getColumnCount(); col++){
                if(_Highlight(Adj.getAsDouble(row,col))){
                    AdjH.setAsDouble(Adj.getAsDouble(row,col), row,col);
                    Adj.setAsDouble(0,row,col);
                }
            }
        }

        //write shapefiles
        ShapefileEdgeWriter sfew = new ShapefileEdgeWriter(filenameE, X,Y, Adj);
        ShapefileEdgeWriter highlighted = new ShapefileEdgeWriter(filenameEH, X, Y, AdjH);
        highlighted.write();
        sfew.write();
    }
    /**
     * Default borders algorithm
     * @return not implemented
     */
    private boolean _border1(){
        //TODO: Source code request in progress

        return false;
    }
}
