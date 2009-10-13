/**
 * This class is for Reading in CSV files.
 * The CSV file's first line may have header information. If not, the first 
 * cell begins with a number, otherwise, it begins with a letter.
 */
package us.jonesrychtar.gispatialnet.Reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.exceptions.MatrixException;

import com.mindprod.csv.CSVReader;

/**
 * @author Samuel C. Tyler
 *
 */
public class CSVFileReader extends TextFileReader{
	private Matrix theMatrix,pubMatrix;
	private boolean includeEgo=false;
	CSVReader matrixReader;

    //formatting for file
    private char distanceSeparatorChar=',';
    private char quoteChar='\"';
    private String commentChars="#";
    private boolean hideComments = true;
    private boolean trimQuoted=true;
    private boolean allowMultiLineFields = false;

    public CSVFileReader(String filename){
        this.setFile(new File(filename));
    }

	@Override
	public Matrix Read(int type, int rows, int cols) 
		throws FileNotFoundException, IllegalArgumentException,IOException {
		//try to read the file
		matrixReader = new CSVReader(new FileReader(this.getFile()),
				distanceSeparatorChar, quoteChar, commentChars, hideComments, trimQuoted, allowMultiLineFields);
		
		switch(type){
		case TextFileReader.FULL_MATRIX:
			if(readFullMatrix(matrixReader,rows,cols)){
				return theMatrix;
			}
			break;
		case TextFileReader.LOWER_MATRIX:
			if(readUpperMatrix(matrixReader,rows,cols)){
				return theMatrix;
			}
			break;
		case TextFileReader.UPPER_MATRIX:
			if(readUpperMatrix(matrixReader,rows,cols)){
				return theMatrix;
			}
			break;
		default:	
		
		}
		
			
		return null;
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
	 */
	public boolean readFullMatrix(CSVReader reader,int rows, int cols) {
		try {
			reader.skipToNextLine(); //skip over the header row (or skip to next graph if previously reading)
		} catch (IOException e1) {
			System.out.println("Error reading the network data for the next network.");
			return false;
		}
		
		//if we are using an ego, add one to each row,col count
		theMatrix = MatrixFactory.zeros(rows+(includeEgo?1:0), cols+(includeEgo?1:0));

		//read each row
		for (int i = 0; i < rows; i++) {
			try {
				reader.skip(2); //skip the two leading fields
				//read the fields
				for (int j = 0; j < cols; j++) {
					float value = reader.getFloat();
					theMatrix.setAsDouble(value, i, j);
				}
			} catch (MatrixException e) {
				System.out.println("Error: Internal data writing error on line " + i + " of current network data.");
			} catch (NumberFormatException e) {
				System.out.println("Error: Reading an entry on line " + i + " of current network data which was not a properly formatted number.");
			} catch (IOException e) {
				System.out.println("An unspecified error occured at line " + i + " of the current network data.");
			}
		}
		
		//Success!
		return true;
	}

	/**
	 * @param reader
	 * @param rows
	 * @param cols
	 * @return
	 */
	public boolean readUpperMatrix(CSVReader reader, int rows, int cols) {
		return false;
/*		String exitCondition = "";
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
		*/
	}

	/**
	 * @param reader
	 * @param rows
	 * @param cols
	 * @return
	 */
	public boolean readLowerMatrix(CSVReader reader, int rows, int cols) {
		return false;
/*		String exitCondition = "";
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
*/	}
	
	
}
