/*
 * This is an abstract class for text file reading (csv and dl/ucinet)
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP
 *
 */
package us.jonesrychtar.gispatialnet.Reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.FileNotFoundException;
import us.jonesrychtar.gispatialnet.Enums.*;

import java.util.Vector;
//import org.ujmp.core.Matrix;
import us.jonesrychtar.gispatialnet.DataSet;

/*
 *
 * @author Charles Bevan
 * @date September 17, 2009
 * @version 0.0.1
 */
/**
 *
 * @author cfbevan
 */
public abstract class TextFileReader {
    //Type Definitions
    /**
     * Signifies the format of a Full Matrix
     */
    public static final int FULL_MATRIX= 0;
    /**
     * Signifies the format of a Lower Matrix
     */
    public static final int LOWER_MATRIX= 1;
    /**
     * Signifies the format of an Upper Matrix
     */
    public static final int UPPER_MATRIX= 2;

    /**
     * File that is being used
     */
    protected File file;
    protected int SortByColumn;
	protected boolean hasHeader;
    
	public boolean getHasHeader() {
		return hasHeader;
	}
	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}
	public int getSortByColumn() {
		return SortByColumn;
	}
	public void setSortByColumn(int sortByColumn) {
		SortByColumn = sortByColumn;
	}

    /**
     *
     * @return the file
     */
    public File getFile(){
        return file;
    }
    /**
     * Sets the file being used
     * @param var File to use
     */
    public void setFile(File var){
        file=var;
    }
    /**
     * Opens a file
     * @param filename file to open
     * @return file object
     */
    public File openFile(String filename){
        return new File(filename);
    }
    
    
    /**
     * Reads the file
     * @param type Format of stored data [FULL_MATRIX, LOWER_MATRIX, UPPER_MATRIX]
     * @param rows number of rows per set of data
     * @param col number of columns per set of data
     * @return Vector of data sets
     * @throws java.lang.Exception
     */
    public abstract Vector<DataSet> Read(MatrixFormat mit, DataSetMatrixType dst,int rows, int col) throws Exception;
	
    public Vector<DataSet> Read(MatrixFormat mit,DataSetMatrixType dst, int rows, int cols, int colSort,boolean hasHeader) throws Exception{
		this.SortByColumn=colSort;
		this.hasHeader=hasHeader;
		return Read(mit, dst,rows, cols);
	}
}
