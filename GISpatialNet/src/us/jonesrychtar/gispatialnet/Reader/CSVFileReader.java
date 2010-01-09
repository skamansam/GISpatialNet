/**
 * This class is for Reading in CSV files.
 * The CSV file's first line may have header information. If not, the first 
 * cell begins with a number, otherwise, it begins with a letter.
 */
package us.jonesrychtar.gispatialnet.Reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.calculation.Calculation;
import org.ujmp.core.enums.ValueType;
import org.ujmp.core.exceptions.MatrixException;
import org.ujmp.core.stringmatrix.impl.CSVMatrix;

import com.mindprod.csv.CSVReader;
import java.util.Vector;
import us.jonesrychtar.gispatialnet.DataSet;

/**
 * @author Samuel C. Tyler
 *
 */
public class CSVFileReader extends TextFileReader{
	private Vector<DataSet> theData; 	//for DataSets 
//	private Vector<Matrix> theMatrices; //for Matrices
	private boolean includeEgo=false;	//are we working with Egos?
	CSVReader matrixReader;				//the csv reader
	private String filename="";
	
    //formatting for file
    private String seperatorChar=",";
    private char quoteChar='\"';
    private String commentChars="#";
    private boolean hideComments = true;
    private boolean trimQuoted=true;
    private boolean allowMultiLineFields = false;

    /**
     *
     * @param filename
     */
    public CSVFileReader(String filename){
        this.setFile(new File(filename));
        this.filename=filename;
    }
    /**
     *
     * @return
     */
    public Vector<DataSet> getMatrices(){return theData;}
    
    /**
     *
     * @param type
     * @param rows
     * @param cols
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.lang.IllegalArgumentException
     * @throws java.io.IOException
     */
    @Override
    //TODO: New Output type needs coding
	public Vector<DataSet> Read(int type, int rows, int cols)
		throws FileNotFoundException, IllegalArgumentException,IOException {
		//try to read the file
//		matrixReader = new CSVReader(new FileReader(this.getFile()),
//				seperatorChar[0], quoteChar, commentChars, hideComments, trimQuoted, allowMultiLineFields);
		//return readFullMatrix(matrixReader,rows,cols);
		switch(type){
		case TextFileReader.FULL_MATRIX:
			//readFullMatrix(filename,rows,cols);
			break;
		case TextFileReader.LOWER_MATRIX:
			//readUpperMatrix(matrixReader,rows,cols);
			break;
		case TextFileReader.UPPER_MATRIX:
			//readUpperMatrix(matrixReader,rows,cols);
			break;
		default:	
		
		}
		
			
		return theData;
	}

	/**
	 * @return
	 */
	public char determineSeperator(){
		return ' ';
	}
	
	/**
	 * @param reader
	 * @param rows
	 * @param cols
	 * @return
	 */
	public boolean readDistance(CSVReader reader, int rows, int cols) {
		return false;
	}

	/**
	 * @param reader
	 * @param rows
	 * @param cols
	 * @return
	 * @throws IOException 
	 */
	public Vector<DataSet> readFullMatrix(CSVReader reader,int rows, int cols) throws IOException {
		Matrix m = this.getFileAsMatrix(new File(this.filename));
		Vector<Matrix> thisMatrix  = this.SplitMatrixAtNaN(m);
		Vector<DataSet> ret = new Vector<DataSet>();
		
		Vector<DataSet> v = new Vector<DataSet>();
		for (int i=0;i<v.size();i++){
			DataSet ds = new DataSet();
			//ds.addXY(x, y);
			//ret.add();
		}
		
		return ret;//theMatrices;
	}

	/**
	 * @param reader
	 * @param headers
	 * @param numRows
	 * @return
	 */
	private Matrix getNextDataSet(CSVReader reader,String[] headers,int numRows){
		Matrix theData=MatrixFactory.emptyMatrix();
		
		for (int row=0;row<numRows;row++){ // get the next row until a blank line is found
			for(int col=0;col<headers.length;col++){ //get next element
				try {
					theData.setAsDouble(reader.getDouble(), row, col);
				} catch (MatrixException e) {
					System.err.println("Cannot add data to matrix while reading csv file "+this.file.getName());
				} catch (NumberFormatException e) {
					System.err.println("Number not formatted correctly at column "+col+", row "+row+" while reading csv file "+this.file.getName());
				} catch (IOException e) {
					System.err.println("Tried to read file "+this.file.getName()+" as CSV, but cannot.");
				}
			}
		}
		
		
		return theData;
		
	}
	
	/**
	 * @param m
	 * @return
	 */
	public Vector<Matrix> SplitMatrixAtNaN(Matrix m){
		Vector<Matrix> ret = new Vector<Matrix>(0);
		Vector<Integer> ranges = new Vector<Integer>(0);
		
		//put 0
		ranges.add(0);
		
		//mark all ranges
		for(int row = 0;row<m.getRowCount();row++){
			String val=m.getAsString(row,0);
			if(val.equals("NaN")){
				System.out.println("New Matrix is at line "+(row+1));
				ranges.add(row-1);
				ranges.add(row+1);
			}
		}
		//put rowCnt
		ranges.add((int)m.getRowCount()-1);
		
		//append matrix to list
		for(int i = 0;i<ranges.size()-1;i+=2){
			int startRow=ranges.elementAt(i);
			int startCol = 0;
			int endRow = ranges.elementAt(i+1);
			int endCol=(int)m.getColumnCount()-1;
			ret.add(m.subMatrix(Calculation.Ret.NEW, startRow, startCol, endRow, endCol));
		}		
		
		//add the matrix to the vector if there is no split.
		if(ret.size()<1)
			ret.add(m);
		
		return ret;
	}
	
	/** Returns a Matrix of Strings for every cell in the CSV file. This includes headers, etc.
	 * @param f the File object
	 * @return the Matrix which represents this file
	 * @throws IOException if the File cannot be accessed
	 */
	public Matrix getFileAsMatrix(File f) throws IOException{
		

		Matrix mFromFile = new CSVMatrix(f.getAbsolutePath(),new String(this.seperatorChar));
		Matrix mNew = MatrixFactory.zeros(ValueType.DOUBLE, mFromFile.getRowCount()-1,mFromFile.getColumnCount()-1);
		int egoCol=-1;

		//set header labels
		for (int i=0;i<mFromFile.getColumnCount();i++){
			String label=mFromFile.getAsString(0,i);
			mNew.setColumnLabel(i, label);
			if(label.toLowerCase().contains("ego") && egoCol==-1)
				egoCol=i;
		}
		
		//set row labels to the EgoID, else, use first column
		if(egoCol!=-1){
			for (int row=0;row<mFromFile.getRowCount();row++){
				mNew.setRowLabel(row, mFromFile.getAsString(row,egoCol));
			}
		}else{
			for (int row=0;row<mFromFile.getRowCount();row++){
				mNew.setRowLabel(row, mFromFile.getAsString(row,0));
			}
			
		}
		mNew.showGUI();
		//convert Strings to Doubles
		for (int row=0;row<mFromFile.getRowCount();row++){
			for (int col=0;col<mFromFile.getColumnCount();col++){
				mNew.setAsDouble(mFromFile.getAsDouble(row,col),row, col);
			}
		}

		return mNew;
	}
	public Vector<Matrix> ReadAsMatrices(int matrixType, int rows, int cols) {
		Vector<Matrix> ret = new Vector<Matrix>();			//The return value
		Matrix theMatrix = MatrixFactory.emptyMatrix();		//the file as a Matrix

		//read in Matrix from CSV file
		try {
			theMatrix = this.getFileAsMatrix(this.file);
		} catch (IOException e) {
			System.err.println("An error occurred while reading data from "+this.file.getAbsolutePath());
		}
		
		System.out.println("Matrix has "+theMatrix.getRowCount()+" rows and "+theMatrix.getColumnCount()+" columns.");
		
		//parse rows, cols
		int curTotalRow=0;								//keep track of total rows seen
		while(curTotalRow<theMatrix.getRowCount()){		//loop over entire Matrix
			Matrix m = MatrixFactory.dense(rows,cols);  //create empty matrix, dim'd to (rows, cols)
			for(int row=0;row<rows && curTotalRow<theMatrix.getRowCount();row++){							//split by rows parameter
				System.out.println("NEW MATRIX:");
				for(int col=0;col<cols && col<theMatrix.getColumnCount();col++){			//set each column
					System.out.print("( "+row+" , "+col+" ) = ");
					Double cVal=theMatrix.getAsDouble(curTotalRow,col);
					System.out.print(""+cVal+"\n");
					m.setAsDouble(cVal, row,col);
				}
				curTotalRow++;
			}
			m.setLabel(this.file.getAbsolutePath());
			ret.add(m);
		}
		
		return ret;
	}
	
	public DataSet parseMatrix(Matrix m){
		DataSet theData=new DataSet();
		
		//loop through column headers
		for (int i=0;i<m.getColumnCount();i++){
			String label = m.getColumnLabel(i);
			if(label.contains("ego") || label.contains("Ego")){
				
			}else if(label.contains("ego") || label.contains("Ego")){}
			else if(label.contains("X")){}
			else if(label.contains("Y")){}
			else if(label.contains("ego") || label.contains("Ego")){}
		
		}
		return theData;
	}
	
	public static void main(String args[]) throws MatrixException, NumberFormatException, IOException{
		if (args.length<1){
			System.out.println("Please specify file!");
			return;
		}
		CSVFileReader theReader = new CSVFileReader(args[0]);
		Matrix n = theReader.getFileAsMatrix(new File(args[0]));
		
		Vector<Matrix> t = theReader.SplitMatrixAtNaN(n);
		for(int i=0;i<t.size();i++){
			System.out.println("MATRIX "+i+":");
			System.out.print(t.elementAt(i).toString());
		}
		//System.out.print(n.toString());

	
	}

	public void printHeaders(Matrix m){
		System.out.print("Headers: \n");
		for (int j=0;j<m.getColumnCount();j++){
			System.out.print("\t"+j+": "+m.getColumnLabel(j)+"\n");	
		}
	}
	public Matrix getNextMatrix(CSVReader csv, int rows, int cols) throws MatrixException, NumberFormatException, IOException{
		Matrix m = MatrixFactory.emptyMatrix();

		for(int row=0; row < rows; row++){
			for(int col=0; col < cols; col++){
				double tmp=csv.getDouble();
				System.out.println("[row:"+row+", col: "+col+"] = "+tmp);
				
				m.setAsDouble(tmp, row, col);
			}
		}
		
		return m;
	}
	/**
	 * @param reader
	 * @param rows
	 * @param cols
	 * @return
	 */

	/*	public Vector<DataSet> readUpperMatrix(CSVReader reader, int rows, int cols) {
		return new Vector<DataSet>();
		String exitCondition = "";
		try {
			reader.skipToNextLine(); //skip over the header row (or skip to next graph if previously reading)
		} catch (IOException e1) {
			exitCondition += "Error reading the network data for the next network.";
			e1.printStackTrace();
		}
		
		// reset the matrix
		if (includeEgo == 1) {
			A = MatrixFactory.zeros(numVerts+1, numVerts+1);
		} else {
			A = MatrixFactory.zeros(numVerts, numVerts);
		}

		for (int i = 0; i < numVerts && exitCondition.equals(""); i++) {
			try {
				reader.skip(2+i+1); //skip the two leading fields
				for (int j = i+1; j < numVerts && exitCondition.equals(""); j++) {
					int value = reader.getInt();
					if (value > 3) {
						value = getRandomEdgeValue();
					}
					if (edgeFormat == 1) {
						if (value >= edgeMinValue) {
							A.setAsDouble(1, i, j);
							A.setAsDouble(1, j, i);
						} else {
							A.setAsDouble(0, i, j);
							A.setAsDouble(0, j, i);
						}
					} else {
						A.setAsDouble(value, i, j);
						A.setAsDouble(value, j, i);
					}
				}
				if (i < numVerts-1) {
					reader.skipToNextLine(); //done reading this line, go to next line
				}
			} catch (MatrixException e) {
				exitCondition += "Error: Internal data writing error on line " + i + " of current network data.";
				e.printStackTrace();
			} catch (NumberFormatException e) {
				exitCondition += "Error: Reading an entry on line " + i + " of current network data which was not a properly formatted number.";
				e.printStackTrace();
			} catch (IOException e) {
				exitCondition += "An unspecified error occured at line " + i + " of the current network data.";
				e.printStackTrace();
			}
		}

		if (exitCondition.equals("")) {
			return true;
		} else {
			System.out.println(exitCondition);
			return false;
		}
		
	}*/

	/**
	 * @param reader
	 * @param rows
	 * @param cols
	 * @return
	 */
	/*	public Vector<DataSet> readLowerMatrix(CSVReader reader, int rows, int cols) {
		return new Vector<DataSet>();//theMatrices;
		String exitCondition = "";
		try {
			reader.skipToNextLine(); //skip over the header row (or skip to next graph if previously reading)
		} catch (IOException e1) {
			exitCondition += "Error reading the network data for the next network.";
			e1.printStackTrace();
		}
		
		// reset the matrix
		if (includeEgo == 1) {
			A = MatrixFactory.zeros(numVerts+1, numVerts+1);
		} else {
			A = MatrixFactory.zeros(numVerts, numVerts);
		}

		for (int i = 0; i < numVerts && exitCondition.equals(""); i++) {
			try {
				reader.skip(2); //skip the two leading fields
				for (int j = 0; j < i && exitCondition.equals(""); j++) {
					int value = reader.getInt();
					if (value > 3) {
						value = getRandomEdgeValue();
					}
					if (edgeFormat == 1) {
						if (value >= edgeMinValue) {
							A.setAsDouble(1, i, j);
							A.setAsDouble(1, j, i);
						} else {
							A.setAsDouble(0, i, j);
							A.setAsDouble(0, j, i);
						}
					} else {
						A.setAsDouble(value, i, j);
						A.setAsDouble(value, j, i);
					}
				}
				if (i < numVerts-1) {
					reader.skipToNextLine(); //done reading this line, go to next line
				}
			} catch (MatrixException e) {
				exitCondition += "Error: Internal data writing error on line " + i + " of current network data.";
				e.printStackTrace();
			} catch (NumberFormatException e) {
				exitCondition += "Error: Reading an entry on line " + i + " of current network data which was not a properly formatted number.";
				e.printStackTrace();
			} catch (IOException e) {
				exitCondition += "An unspecified error occured at line " + i + " of the current network data.";
				e.printStackTrace();
			}
		}

		if (exitCondition.equals("")) {
			return true;
		} else {
			System.out.println(exitCondition);
			return false;
		}
	}*/
	
}
