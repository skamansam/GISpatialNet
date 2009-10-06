/*
 * This program will highlight edges according to user specification and output a shapefile
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
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation;
import us.jonesrychtar.gispatialnet.Writer.ShapefileEdgeWriter;

/**
 *
 * @author cfbevan
 */
public class HighlightEdges {

    private int Alg;
    private Matrix X;
    private Matrix Y;
    private Matrix Adj;
    private Matrix adjStyle;

    //stats
    private double avg;
    private double median;
    private double top10percent;

    public HighlightEdges(Matrix x, Matrix y, Matrix adj, String filename, int Algorithm){
        Alg = Algorithm;
        X = x;
        Y = y;
        Adj = adj;

    }

    public void write(){
        StyleBuilder builder = new StyleBuilder();
        Style origLineStyle = builder.createStyle(builder.createLineSymbolizer(Color.BLACK,1));
        Style linestyle = builder.createStyle(builder.createLineSymbolizer(Color.GREEN, 2));
        Style[] st = {origLineStyle,linestyle};

        //make adjStyle
        prepareStat();
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

    private boolean _Highlight(double edge){
        boolean ans;
        switch(Alg){
            case 0: ans = _LessThanAvgLen(edge);
                break;
            case 1: ans = _LessThanMedianLen(edge);
                break;
            case 2: ans = _GreaterThanMedianLen(edge);
                break;
            case 3: ans = _Top10percent(edge);
                break;
            default:
                return false;
        }
        return ans;
    }

    private boolean _LessThanAvgLen(double edge) {
        return edge < avg;
    }

    private boolean _LessThanMedianLen(double edge) {
        return edge < median;
    }
    
    private boolean _GreaterThanMedianLen(double edge) {
        return edge > median;
    }

    private boolean _Top10percent(double edge) {
       return edge >= top10percent;
    }

    private void prepareStat() {
        Matrix temp = Adj;
        temp = Adj.sort(Calculation.Ret.NEW);
        int[] rc;

        //avg
        avg = Adj.getMeanValue();
        //median
        int middle = Math.round((temp.getRowCount() * temp.getColumnCount())/2);
        rc = _getRowCol(middle, temp);
        median = temp.getAsDouble(rc[0],rc[1]);
        //10 percent
        int p = Math.round((temp.getRowCount() * temp.getColumnCount())/10);
        rc = _getRowCol(p,temp);
        top10percent = temp.getAsDouble(rc[0],rc[1]);

    }
    private int[] _getRowCol(int place, Matrix temp){
        int row;
        int col;

        if(place%temp.getColumnCount() == 0){
            row = (place/(int)temp.getColumnCount()) -1;
            col = (int)temp.getColumnCount() -1;
        }
        else{
            row = place/(int)temp.getColumnCount();
            col = place%(int)temp.getColumnCount() -1;
        }

        return new int[] {row,col};

    }


}
