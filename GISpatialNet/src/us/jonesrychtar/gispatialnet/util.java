/*
 * This is the main program. It coordinates all other classes
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 *
 */
package us.jonesrychtar.gispatialnet;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.calculation.Calculation;

/**
 *
 * @author Sam Tyler, Charles Bevan
 * @date September 17, 2009
 * @version 0.0.1
 */
public class util {
        
    //Matrix conversion functions-------------------------------------------------------------------------
    /**
     * Translates the stored xy to a new xy
     * @param xmove amount to move in x direction
     * @param ymove amount to move in y direction
     */
    public static void translate(double xmove, double ymove,DataSet theData)throws IllegalStateException{
        if(theData.hasXY()){
            MatrixConversion mc = new MatrixConversion();
            Matrix temp = theData.getXY();
            temp = mc.Translation(temp, xmove, ymove);
            theData.setX(temp.selectColumns(Calculation.Ret.NEW, 0));
            theData.setY(temp.selectColumns(Calculation.Ret.NEW, 1));
        }
        else throw new IllegalStateException("no XY data loaded");
    }
    /**
     * Reflects the xy data over an axis
     * @param Axis Axis to reflect over (X=0, Y=1)
     * @throws java.lang.IllegalStateException
     */
    public static void reflect(DataSet theData, int Axis)throws IllegalStateException{
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
     * Rotates the xy data about the origin
     * @param Degrees Degrees to rotate
     * @throws java.lang.IllegalStateException
     */
    public static void rotate(double Degrees,DataSet theData)throws IllegalStateException{
        if(theData.hasXY()){
	        MatrixConversion mc = new MatrixConversion();
	        Matrix temp = theData.getXY();
	        temp = mc.RotateClockwise(temp, Degrees);
            theData.setX(temp.selectColumns(Calculation.Ret.NEW, 0));
            theData.setY(temp.selectColumns(Calculation.Ret.NEW, 1));
        } else throw new IllegalStateException("no XY data loaded");
    }

    /**
     * @param factor Factor to scale by
     * @param x the X-Coords
     * @param y the Y-Coords
     * @throws IllegalStateException
     */
    public static void scale(double factor,DataSet theData)throws IllegalStateException{
        if(theData.hasXY()){
            MatrixConversion mc = new MatrixConversion();
            Matrix temp = theData.getXY();
            temp = mc.Scale(temp, factor);
            theData.setX(temp.selectColumns(Calculation.Ret.NEW, 0));
            theData.setY(temp.selectColumns(Calculation.Ret.NEW, 1));
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
    public static Matrix addNumberCol(Matrix in){
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
    public static Matrix stripNumCol(Matrix in){
        return in.selectColumns(Calculation.Ret.NEW, 1, in.getColumnCount()-1);
    }
}
