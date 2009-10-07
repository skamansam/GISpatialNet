/*
 * This program will perform a borders algorithm on data and output shapefile.
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: geotools,
 *
 */
package us.jonesrychtar.gispatialnet;

import java.awt.Color;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import us.jonesrychtar.gispatialnet.Writer.ShapefileEdgeWriter;
import us.jonesrychtar.gispatialnet.Writer.ShapefileWriter;

/**
 *
 * @author cfbevan
 */
public class Borders {
    private Matrix X;
    private Matrix Y;
    private Matrix Adj;
    private Matrix adjStyle;
    private String filenameE;
    int Alg = 0;
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
     */
    public void Write(){
        StyleBuilder builder = new StyleBuilder();
        Style origLineStyle = builder.createStyle(builder.createLineSymbolizer(Color.BLACK,1));
        Style linestyle = builder.createStyle(builder.createLineSymbolizer(Color.GREEN, 2));
        Style[] st = {origLineStyle,linestyle};

        //make adjStyle
        adjStyle = MatrixFactory.zeros(Adj.getRowCount(), Adj.getColumnCount());
        for(int row=0; row<Adj.getRowCount(); row++)
            for(int col=0; col<Adj.getColumnCount(); col++)
                if(_Highlight(Adj.getAsDouble(row,col)))
                    adjStyle.setAsInt(1,row,col);
                else{
                    adjStyle.setAsInt(0,row,col);
                }

        ShapefileEdgeWriter sfew = new ShapefileEdgeWriter(filenameE, X,Y, Adj, adjStyle,st);
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
