/*
 * This is the main program. It coordinates all other classes
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 *
 */
package us.jonesrychtar.gispatialnet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.calculation.Calculation;
import org.ujmp.core.calculation.Calculation.Ret;

/**
 *
 * @author Sam Tyler, Charles Bevan
 * @version 0.0.1
 */
public class util {
	
    
    //Matrix conversion functions-------------------------------------------------------------------------
    /**
     * Translates the stored xy to a new xy
     * @param xmove amount to move in x direction
     * @param ymove amount to move in y direction
     * @param theData dataset to modify
     * @throws IllegalStateException
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
     * @param theData dataset to modify
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
     * @param theData dataset to modify
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
     * @param theData dataset to modify
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
    	//System.out.println("gispatialnet.util.combine("+a.getColumnCount()+","+b.getColumnCount()+"):");
        
    	
    	if(a.getColumnCount()==0 && b.getColumnCount()==0) {
        	//System.out.println("returning empty.");
        	return MatrixFactory.emptyMatrix();
        }
        if(a.getColumnCount()>0 && b.getColumnCount()==0) {
        	//System.out.println("returning a.");
        	return MatrixFactory.copyFromMatrix(a);
        }
        if(a.getColumnCount()==0 && b.getColumnCount()>0) {
        	//System.out.println("returning b.");
        	return MatrixFactory.copyFromMatrix(b);
        }
    	
    	if(a.getRowCount() != b.getRowCount())
        	throw new IllegalArgumentException("util.combine(): Matrix sizes do not have the same row count. ("+a.getRowCount()+" != "+b.getRowCount()+")");
    	
    	//System.out.println("\tMatrix a:\n"+a);
    	//System.out.println("\tMatrix b:\n"+b);

    	Matrix temp=a.appendHorizontally(b);
    	//System.out.println("\tMatrix temp:"+temp);

    	//System.out.println("FIXME: Header copy in gispatialnet.util.combine()");
        long aCols=a.getColumnCount();
        long bCols=b.getColumnCount();
        
        long col=0;
        for(;col < aCols;col++){
        	String lbl=a.getColumnLabel(col);
        	//System.out.println("col: "+col+" lbl: "+lbl);
			   temp.setColumnLabel(col, lbl);
        }
        for(;col < temp.getColumnCount();col++){
        	long curB=col-aCols;
        	String lbl=b.getColumnLabel(curB);
        	//System.out.println("col: "+col+" curB: "+curB+" lbl: "+lbl);
        	temp.setColumnLabel(col, lbl);
        }
   		//System.out.println("\tMatrix ret Headers:");
   		printHeaders(temp);
   		
        return temp;
/*        }else
            throw new IllegalArgumentException("Matrix Sizes do not match. ("+a.getColumnCount()+","+b.getColumnCount()+")");
            */
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
        if(in.getColumnCount()>2)
            out[2] = in.selectColumns(Calculation.Ret.NEW, 2, in.getColumnCount()-1);
        else
            out[2] = MatrixFactory.emptyMatrix();
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
     * Removes number column (used when reading XY or XYAttb)
     * @param in Matrix to strip from
     * @return New MAtrix without first column
     */
    public static Matrix stripNumCol(Matrix in){
        return in.selectColumns(Calculation.Ret.NEW, 1, in.getColumnCount()-1);
    }

    /*
     * Get the extension of a file.
     */  
    public static String getFileExtension(File f) {
        String ext = null;
        String fname = f.getName();
        int i = fname.lastIndexOf('.');

        if (i > 0 &&  i < fname.length() - 1) {
            ext = fname.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
    public static String determineFileType(File f){
		String ext = getFileExtension(f);
		if(ext.equals("xls")) return "excel";
		if(ext.equals("csv")) return "csv";
		if(ext.equals("shp")) return "shapefile";
    	try {
			Scanner sc = new Scanner(f);
			String first = sc.next();
			
			if(first.endsWith(",")) return "csv";
			if(first.startsWith("*")) return "payek";
			if(first.startsWith("<?xml")) return "kml";
			if(first.toLowerCase().equals("dl")) return "ucinet";
			
		} catch (FileNotFoundException e) {System.err.println("Can't read "+f.getAbsolutePath());}
    	return null;
    }
    
    public String determineFileType(String f){
    	return determineFileType(new File(f));
    }
    
    public static void copyColumnLabels(Matrix from, Matrix to){
    	printHeaders(from);
    	if(from.getColumnCount()!=to.getColumnCount()){
    		System.err.println("Matrix columns not equal ("+from.getColumnCount()+" != "+to.getColumnCount()+"). Cannot set column headers.");
    		return;
    	}
    	for(int i=0;i<from.getColumnCount();i++){
    		to.setColumnLabel(i, from.getColumnLabel(i));
    	}
    }
    public static void addRow(Matrix from, Matrix to,int rowNum){
    	Matrix m = MatrixFactory.emptyMatrix();
    	from.selectRows(Calculation.Ret.NEW, 0);
    	for(int i=0;i<from.getColumnCount();i++){
    		//to.set(i, from.getColumnLabel(i));
    	}
    	to.appendVertically(m);
    }
    public static void printHeaders(Matrix m){
		/*if(m.getColumnLabel(0).equals(null) || m.getColumnLabel(0).equals("") || m.getColumnLabel(0).equals(" ") ){
			System.out.println("No header found for "+m.getLabel());return;
		}*/
    	//System.out.print("Header: ");
		for (int i=0;i<m.getColumnCount();i++){
			System.out.print("'"+m.getColumnLabel(i)+"', ");
		}			
		System.out.print("\n");
    }
    
    public static void printLabels(Matrix m){
		for (int i=0;i<m.getRowCount();i++){
			System.out.print("'"+m.getRowLabel(i)+"', ");
		}			
		System.out.print("\n");
    }

    //moves row number to column labels
	public static void readHeader(Matrix m,int rowNum) {
		for(int i=0;i<m.getColumnCount();i++){
			m.setColumnLabel(i, m.getAsString(rowNum,i));
		}
		m=m.deleteRows(Calculation.Ret.NEW, rowNum);
		//System.out.println("__util.readHeader().headers:__");
		//util.printHeaders(m);
	}
	
	public static void convertKnown(DataSet ds){
		
	}
	public static void printMatrix(Matrix m){
		int colWidth=-10;		
		System.out.println("Name: "+m.getLabel());
		//util.printHeaders(m);

		//print the headers first
		System.out.format("%"+colWidth+"s|","xxx");
		for (int col=0;col<m.getColumnCount();col++)
			System.out.format("%"+colWidth+"s", m.getColumnLabel(col));
		System.out.println("");
		for (int col=0;col<=m.getColumnCount();col++)
			System.out.format("%"+colWidth+"s", "----------");
		System.out.println("");
		
		for (int row=0;row<m.getRowCount();row++){
			System.out.format("%"+colWidth+"s|", m.getRowLabel(row));
			for (int col=0;col<m.getColumnCount();col++){
				System.out.format("%"+colWidth+"s", m.getAsString(row,col));
			}			
			System.out.println("");
		}
	}

	public static String matrixToString(Matrix m){
		String ret="";
		String dimensions="["+m.getColumnCount()+"x"+m.getRowCount()+"]";
		int colWidth=-10;		
		ret+="Name: "+m.getLabel()+"\n";
		//util.printHeaders(m);
		
		//print the headers first
		ret+=String.format("%"+colWidth+"s|",dimensions);
		for (int col=0;col<m.getColumnCount();col++)
			ret+=String.format("%"+colWidth+"s", m.getColumnLabel(col));
		ret+="\n";
		for (int col=0;col<=m.getColumnCount();col++)
			ret+=String.format("%"+colWidth+"s", "----------");
		ret+="\n";
		
		for (int row=0;row<m.getRowCount();row++){
			ret+=String.format("%"+colWidth+"s|", m.getRowLabel(row));
			for (int col=0;col<m.getColumnCount();col++){
				ret+=String.format("%"+colWidth+"s", m.getAsString(row,col));
			}			
			ret+="\n";
		}
		return ret;
	}

	public static Matrix deleteColumns(Matrix m, Calculation.Ret ret, long... cols) {
		Matrix tmp = MatrixFactory.copyFromMatrix(m);
		tmp = tmp.deleteColumns(ret, cols);
		
		//set labels
		for (long row=0;row<m.getRowCount();row++){
			tmp.setRowLabel(row, m.getRowLabel(row));
		}
		
		//set headers
		int found=0;
		for (int col=0;col<m.getColumnCount();col++){
			for(int i =0;i < cols.length;i++)
				if(cols[i]==col){ found++;continue;}
			tmp.setColumnLabel(col-found, m.getColumnLabel(col));
		}

		//System.out.println("deleteColumns() return: ");
		//util.printMatrix(tmp);
		
		return tmp;
	}


	public static Matrix deleteRows(Matrix m, Calculation.Ret ret, long... rows) {
		Matrix tmp = MatrixFactory.copyFromMatrix(m);
		tmp = tmp.deleteColumns(ret, rows);

		//set labels
		int found=0;
		for (long row=0;row<m.getRowCount();row++){
			for(int i =0;i < rows.length;i++)
				if(rows[i]==row){ found++;continue;}
			tmp.setRowLabel(row-found, m.getRowLabel(row));
		}
		
		//set headers
		for (int col=0;col<m.getColumnCount();col++){
			tmp.setColumnLabel(col, m.getColumnLabel(col));
		}
		return tmp;
	}

	//return deleted columns
	public static Matrix detachColumns(Matrix m, Calculation.Ret ret, long... cols) {
		Matrix tmp = MatrixFactory.zeros(m.getRowCount(),cols.length);

		for(int colidx = 0;colidx < cols.length;colidx++){
			tmp.setColumnObject(colidx, m.getColumnObject(cols[colidx]));
		}
		
		m=deleteColumns(m,ret, cols);
		return tmp;
	}

	//return deleted rows
	public static Matrix detachRows(Matrix m, Calculation.Ret ret, long... rows) {
		Matrix tmp = MatrixFactory.zeros(m.getColumnCount(),rows.length);

		for(int rowidx = 0;rowidx < rows.length;rowidx++){
			tmp.setColumnObject(rowidx, m.getColumnObject(rows[rowidx]));
		}
		
		m=deleteColumns(m,ret, rows);
		return tmp;
	}
	
	public static void copyLabels(Matrix a, Matrix b){
		for (long row=0;row<b.getRowCount() || row<a.getRowCount();row++)
			b.setRowLabel(row, a.getRowLabel(row));
	}
	
	public static void main(String[] args){
		Matrix a = MatrixFactory.dense(2,2);
		Matrix b = MatrixFactory.dense(2,1);
		Matrix empty=MatrixFactory.emptyMatrix();
		
		a.setColumnLabel(0, "One");
		a.setColumnLabel(1, "Two");
		b.setColumnLabel(0, "Three");
//		b.setColumnLabel(1, "Four");
		
		System.out.println("Matrix a:");
		printHeaders(a);
		System.out.println(a);

		System.out.println("Matrix b:");
		printHeaders(b);
		System.out.println(b);
		
		Matrix c=combine(a,b);
		System.out.println("Matrix c:");
		printHeaders(c);
		System.out.println(c);
		
		

	}
	
}