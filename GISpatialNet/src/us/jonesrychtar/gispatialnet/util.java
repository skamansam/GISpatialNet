/*
 * This is the main program. It coordinates all other classes
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 *
 */
package us.jonesrychtar.gispatialnet;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Vector;
import javax.naming.CannotProceedException;
import javax.naming.OperationNotSupportedException;
import jxl.write.WriteException;
import org.boehn.kmlframework.kml.KmlException;
import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.calculation.Calculation;
import us.jonesrychtar.gispatialnet.Writer.*;
import us.jonesrychtar.gispatialnet.Algorithm.Borders;
import us.jonesrychtar.gispatialnet.Algorithm.HighlightEdges;
import us.jonesrychtar.gispatialnet.Algorithm.QAP;
import us.jonesrychtar.gispatialnet.Reader.*;
import us.jonesrychtar.socialnetwork.SpatialGraph.SpatialGraphBase;

/**
 *
 * @author Sam Tyler, Charles Bevan
 * @date September 17, 2009
 * @version 0.0.1
 */
public class util {

    //Matrix data
    //use 'MatrixFactory.emptyMatrix()'
/*    private Matrix x = MatrixFactory.emptyMatrix(); //vector matrix (1 col) of x coordinates
    private Matrix y = MatrixFactory.emptyMatrix(); //vector matrix of y coordinates
    private Matrix adj = MatrixFactory.emptyMatrix(); //matrix of size x by y where if ij >= 1 there is a line connecting (xi,yi) to (xj,yj). Always stored as FULL MATRIX
    private Matrix attb = MatrixFactory.emptyMatrix(); //attributes for node (xi,yi) where i is the row of attb
*/
//    private Vector<String> loadedFiles = new Vector<String>();
/*    public util(Matrix x,Matrix y,Matrix adj,Matrix attb){
    	this.x=x;
    	this.y=y;
    	this.adj=adj;
    	this.attb=attb;
    }
*/    
/*    public void setAttMatrix(){}
    public void setAttMatrix(){}
    public void setAttMatrix(){}
    public void setAttMatrix(){}
    public void setAttMatrix(){}
    public void setAttMatrix(){}
    public void setAttMatrix(){}
*/
    //loading functions-----------------------------------------------------------------------------------
    //analyzing functions---------------------------------------------------------------------------------
    /**
     * Performes a borders analysis on data, writes out edge shapefile
     * @param alg Algorithm to use
     * Options: 0 - default Borders algorithm
     */
    public void Border(String filename, int alg,Matrix x,Matrix y, Matrix adj) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{
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
    public void QAP(String arg[]) throws IllegalArgumentException, IOException, Error, CannotProceedException{
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
    public void Highlight(int alg, String filename, String nodeFilename,Matrix x,Matrix y, Matrix adj) throws IllegalArgumentException, MalformedURLException, IOException, SchemaException{
        ShapefileNodeWriter sfnw = new ShapefileNodeWriter(nodeFilename, x,y);
        sfnw.write();
        HighlightEdges h = new HighlightEdges(filename, x,y,adj,alg);
        h.write();
    }
    /**
     * Not supported yet
     * @throws javax.naming.OperationNotSupportedException
     */
    public SpatialGraphBase SNB(Matrix x,Matrix y, Matrix adj, double bias){
    	SpatialGraphBase sg=new SpatialGraphBase(x,y,adj);
    	sg.setBias(bias);
    	sg.calculateBiasAndEstimates();
    	return sg;
    }

    //TODO: Make the translation functions more atomic; for only one matrix
    //Matrix conversion functions-------------------------------------------------------------------------
    /**
     * Translates the stored xy to a new xy
     * @param xmove amount to move in x direction
     * @param ymove amount to move in y direction
     */
    public void translate(double xmove, double ymove,Matrix x,Matrix y, Matrix adj)throws IllegalStateException{
        if(!x.isEmpty() && !y.isEmpty()){
            MatrixConversion mc = new MatrixConversion();
            Matrix temp = combine(x,y);
            temp = mc.Translation(temp, xmove, ymove);
            x = temp.selectColumns(Calculation.Ret.NEW, 0);
            y = temp.selectColumns(Calculation.Ret.NEW, 1);
        }
        else throw new IllegalStateException("no XY data loaded");
    }
    /**
     * Reflects the xy data over an axis
     * @param Axis Axis to reflect over (X=0, Y=1)
     * @throws java.lang.IllegalStateException
     */
    public void reflect(DataSet theData, int Axis)throws IllegalStateException{
        //x=0 y=1
        if(theData.hasXY()){
            MatrixConversion mc = new MatrixConversion();
            Matrix temp = theData.getXY();
            temp = mc.Reflection(temp, Axis);
            theData.setX(temp.selectColumns(Calculation.Ret.NEW, 0));
            theData.setY(temp.selectColumns(Calculation.Ret.NEW, 1));
        } else throw new IllegalStateException("no XY data loaded");
    }
    /**
     * Reflects the xy data over an axis
     * @param Axis Axis to reflect over (X=0, Y=1)
     * @throws java.lang.IllegalStateException
     */
    public Matrix reflect(Matrix x,Matrix y, int Axis)throws IllegalStateException{
        //x=0 y=1
        if(!x.isEmpty() && !y.isEmpty()){
            MatrixConversion mc = new MatrixConversion();
            Matrix temp = combine(x,y);
            temp = mc.Reflection(temp, Axis);
            return combine(temp.selectColumns(Calculation.Ret.NEW, 0),
            		temp.selectColumns(Calculation.Ret.NEW, 1));
        } else throw new IllegalStateException("no XY data loaded");
    }
    /**
     * Rotates the xy data about the origin
     * @param Degrees Degrees to rotate
     * @throws java.lang.IllegalStateException
     */
    public Matrix rotate(double Degrees,Matrix x,Matrix y, Matrix adj)throws IllegalStateException{
        if(!x.isEmpty() && !y.isEmpty()){
	        MatrixConversion mc = new MatrixConversion();
	        Matrix temp = combine(x,y);
	        temp = mc.RotateClockwise(temp, Degrees);
            return combine(temp.selectColumns(Calculation.Ret.NEW, 0),
            		temp.selectColumns(Calculation.Ret.NEW, 1));
        } else throw new IllegalStateException("no XY data loaded");
    }

    /**
     * @param factor Factor to scale by
     * @param x the X-Coords
     * @param y the Y-Coords
     * @return combined XY Matrix. use SplitXYAttb() to split into respective coords.
     * @throws IllegalStateException
     */
    public Matrix scale(double factor,Matrix x,Matrix y)throws IllegalStateException{
        if(!x.isEmpty() && !y.isEmpty()){
            MatrixConversion mc = new MatrixConversion();
            Matrix temp = combine(x,y);
            temp = mc.Scale(temp, factor);
            return combine(temp.selectColumns(Calculation.Ret.NEW, 0),
            		temp.selectColumns(Calculation.Ret.NEW, 1));
        } else throw new IllegalStateException("no XY data loaded");
    }

    //extra functions-------------------------------------------------------------------------------------
    
    /**
     * Combines Matrix A and B
     * @param a Matrix to append to
     * @param b Matrix to append
     * @return B appended to A horizontallly
     * @throws java.lang.IllegalArgumentException
     */
    public static Matrix combine(Matrix a, Matrix b) throws IllegalArgumentException{
        if(a.getRowCount() == b.getRowCount()){
           Matrix temp = a.appendHorizontally(b);
           //set headers
           int col=0;
           while(col<a.getColumnCount()){
               temp.setColumnLabel(col, a.getColumnLabel(col));
               col++;
           }
           while(col< a.getColumnCount()+b.getColumnCount()){
               temp.setColumnLabel(col, b.getColumnLabel(col-a.getColumnCount()));
               col++;
           }
            return temp;
        }else
            throw new IllegalArgumentException("Matrix Sizes do not match.");
    }
    /**
     * Splits a big matrix into 3 smaller matricies
     * @param in matrix containing x, y, and attb
     * @return matrix array where out[0] = x, out[1] =y , and out[2] = attb
     */
    public static Matrix[] SplitXYAttb(Matrix in){
        Matrix[] out = new Matrix[3];
        //copy all rows of col 0 to out[0]
        out[0] = in.selectColumns(Calculation.Ret.NEW, 0);
        //copy all rows of col 1 to out[1]
        out[1] = in.selectColumns(Calculation.Ret.NEW, 1);
        //copy rest to out[2]
        out[2] = in.selectColumns(Calculation.Ret.NEW, 2, in.getColumnCount()-1);
        return out;
    }
    /**
     * Adds a numbered column to the front of the matrix
     * @param in Matrix to add number column to
     * @return Original matrix with numbered column added to front
     */
    public Matrix addNumberCol(Matrix in){
        Matrix numCol = MatrixFactory.zeros(org.ujmp.core.enums.ValueType.INT, in.getRowCount(), 1);
        for(int row=0; row<numCol.getRowCount(); row++){
            numCol.setAsInt(row+1, row, 0);
        }
        numCol.setColumnLabel(0, "id");
        return combine(numCol,in);
    }

    /**
     * Removes number colu (used when reading XY or XYAttb)
     * @param in Matrix to strip from
     * @return New MAtrix without first column
     */
    public Matrix stripNumCol(Matrix in){
        return in.selectColumns(Calculation.Ret.NEW, 1, in.getColumnCount()-1);
    }

    //funcrtion to test if matricies already have data
    /**
     *
     * @param Matrix number corresponding to Matrix to check: 0 = x, 1 = y, 2 = Adj, 3 = Attb
     * @return True if Matrix has data, false otherwise;
     */
    /*
     * X = 0
     * Y = 1
     * Adj = 2
     * Attb = 3
     * */
/*    public boolean HasData(int Matrix){
        //testing
        boolean empty = x.isEmpty();
        //testing

        switch(Matrix){
            case 0: return !(x.isEmpty());
            case 1: return !(y.isEmpty());
            case 2: return !(adj.isEmpty());
            case 3: return !(attb.isEmpty());
            default: return false;
        }
    }*/
}
