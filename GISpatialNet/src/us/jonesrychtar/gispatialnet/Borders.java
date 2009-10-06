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
    int Alg = 0;
    public Borders(Matrix x, Matrix y, Matrix adj, int Algorithm){
        X = x;
        Y = y;
        Adj = adj;
        Alg = Algorithm;
        
    }
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

        ShapefileEdgeWriter sfew = new ShapefileEdgeWriter(X,Y, Adj, adjStyle,st);
        sfew.write();
        
    }

    private boolean _border1(){
        //TODO: Source code request in progress

        return false;
    }
}
