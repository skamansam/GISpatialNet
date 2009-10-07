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
    private String filenameE;

    //stats
    private double avg;
    private double median;
    private double top10percent;

    /**
     * Writes shapefile with edges highlighted to user specification
     * @param filename output filename of edge shapefile
     * @param x vector matrix of x coordinates
     * @param y vector matrix of y coordinates
     * @param adj adjacency matrix of edges
     * @param Algorithm algorithm to use:
     *          0 less than average length
     *          1 less than median length
     *          2 more than median length
     *          3 top 10 percent
     */
    public HighlightEdges(String filename, Matrix x, Matrix y, Matrix adj, int Algorithm){
        Alg = Algorithm;
        X = x;
        Y = y;
        Adj = adj;
        filenameE = filename;

    }

    /**
     * Writes data to edge shapefile
     */
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

        ShapefileEdgeWriter sfew = new ShapefileEdgeWriter(filenameE, X,Y, Adj, adjStyle,st);
        sfew.write();
    }

    /**
     * Tells if edge needs to be highlighted
     * @param edge value of edge
     * @return does edge need to be highlighted
     */
    private boolean _Highlight(double edge){
        boolean ans;
        switch(Alg){
            case 0: ans = _LessThanAvgLen(edge);
                break;
            case 1: ans = _LessThanMedianLen(edge);
                break;
            case 2: ans = !(_LessThanMedianLen(edge));
                break;
            case 3: ans = _Top10percent(edge);
                break;
            default:
                return false;
        }
        return ans;
    }

    /**
     * Tells if edge is less than average length
     * @param edge value of edge
     * @return true if edge is less than averge length, flase if edge is greater than average length
     */
    private boolean _LessThanAvgLen(double edge) {
        return edge < avg;
    }

    /**
     * Tells if edge is les than median length
     * @param edge value of edge
     * @return true if edge is less than median length, false if edge is greater than median length
     */
    private boolean _LessThanMedianLen(double edge) {
        return edge < median;
    }
    

    /**
     * Tells if edge is in top 10%
     * @param edge value of edge
     * @return true if edge is in top 10%, false if it is not in top 10%
     */
    private boolean _Top10percent(double edge) {
       return edge >= top10percent;
    }

    /**
     * Prepares statistics needed in algorithms
     */
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
    /**
     * Helper function that returns a row and column given a number in a matrix
     * @param place cell number of matrix
     * @param temp matrix you are using
     * @return array where out[0] is row of matrix and out[1] is col of matrix
     */
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
