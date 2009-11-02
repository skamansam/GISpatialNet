/*
 * Another highlight edges file that will make one shapefile for each edge value.
 */

package us.jonesrychtar.gispatialnet.Algorithm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;
import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import us.jonesrychtar.gispatialnet.Writer.ShapefileEdgeWriter;

/**
 *
 * @author cfbevan
 */
public class HighlightEdgesByVal {

    private Matrix X;
    private Matrix Y;
    private Matrix adj;
    private Vector<Matrix> adjPrime = new Vector<Matrix>();
    private String prefix;
    private int numberOfValues = 0;
    private Vector <Double> used = new Vector<Double>();

    /**
     * Makes one shapefile for each individual value in the adj matrix
     * @param filename prefix to each output file
     * @param x vector matrix of x coordinates
     * @param y vector matrix of y coordinates
     * @param adjin Original adj matrix
     */
    public HighlightEdgesByVal(String filename, Matrix x, Matrix y, Matrix adjin){
        X = x;
        Y = y;
        adj = adjin;
        prefix = filename;
    }

    /**
     * Writes the shapefiles
     * @throws java.lang.IllegalArgumentException
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     * @throws org.geotools.feature.SchemaException
     */
    public void write() throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{
        analyzeAdj();
        MakeAdjPrime();

        for(int i=0; i<used.size(); i++){
            String filename = prefix+used.elementAt(i);
            ShapefileEdgeWriter sfew = new ShapefileEdgeWriter(filename, X, Y, adjPrime.elementAt(i));
            sfew.write();
        }
    }

    private void analyzeAdj(){ //makes a matrix of used values
        for(int row=0; row<adj.getRowCount(); row++){
            for(int col=0; col<adj.getColumnCount(); col++){
                Double temp = adj.getAsDouble(row,col);
                if(!(used.contains(temp))){
                    used.add(temp);
                    numberOfValues++;
                }
            }
        }
    }
    private void MakeAdjPrime(){
        for(int i=0; i<used.size(); i++){
            Matrix temp = MatrixFactory.zeros(adj.getRowCount(), adj.getColumnCount());
            //iterate through adj looking for used[i]
            for(int row=0; row<adj.getRowCount(); row++)
                for(int col=0; col<adj.getColumnCount(); col++){
                    if(adj.getAsDouble(row,col) == used.elementAt(i)){ //add to temp
                            temp.setAsDouble(adj.getAsDouble(row,col),row,col);
                    }
                    else{ //do not add to temp
                        if(adj.getAsDouble(row,col) == 0)
                            temp.setAsDouble(-1,row,col);
                        else
                            temp.setAsDouble(0,row,col);
                    }
                }
            //add temp to vector of adjPrime
            adjPrime.add(temp);
        }
    }
}
