/**
 * Container class for matricies that make up data
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP
 */
package us.jonesrychtar.gispatialnet;

import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;



import org.geotools.feature.SchemaException;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.calculation.Calculation;

import us.jonesrychtar.gispatialnet.Algorithm.Algorithm;


/**
 * @author sam,charles
 * This class holds the primary data for GISpatialNet. Code is pulled from 
 * the util class, because these are not utility functions, they are functions
 * for manipulating the data we have. Also, this allows for calling 
 * GISPatialNet gsn = new GISPatialNet;
 * if ((gsn.getData()).loadCSV("data.csv")){
 * 		//do something with data
 * }
 *
 */
public class DataSet {

	private Matrix x = MatrixFactory.emptyMatrix(); //vector matrix (1 col) of x coordinates
    private Matrix y = MatrixFactory.emptyMatrix(); //vector matrix of y coordinates
    private Matrix adj = MatrixFactory.emptyMatrix(); //matrix of size x by y where if ij >= 1 there is a line connecting (xi,yi) to (xj,yj). Always stored as FULL MATRIX
    private Matrix attb = MatrixFactory.emptyMatrix(); //attributes for node (xi,yi) where i is the row of attb
    private String title="[no title]";
    private String extraTitle="";
    private Vector<String> loadedFiles = new Vector<String>();

    private int Detail=2;

    /**
	 * 
	 */
	public DataSet() {
		//creates dataset with empty data
	}

    /**
     * Creates dataset with given data
     * @param x x coordinate data (vector matrix)
     * @param y y coordinate data (vector matrix)
     * @param adj Adjacency matrix containing edge data coresponding to x,y coordinate data
     */
    public DataSet(Matrix x, Matrix y, Matrix adj){
        this.x=x;
        this.y=y;
        this.adj=adj;
    }
    /**
     * Creates data set with given data
     * @param x x coordinate data (vector matrix)
     * @param y y coordinate data (vector matrix)
     */
    public DataSet(Matrix x, Matrix y){
        this.x=x;
        this.y=y;
    }
    /**
     * Creates data set with given data
     * @param adj Adjacency matrix containing edge data
     */
    public DataSet(Matrix adj){
        this.adj=adj;
    }
    /**
     * Creates data set with given data
     * @param x x coordinate data (vector matrix)
     * @param y y coordinate data (vector matrix)
     * @param adj Adjacency matrix containing edge data coresponding to x,y coordinate data
     * @param attb Attribute data that coresponds to each x,y node
     */
    public DataSet(Matrix x, Matrix y, Matrix adj, Matrix attb){
        this.x=x;
        this.y=y;
        this.adj=adj;
        this.attb=attb;
    }

	/**
	 * Copy Constructor. Copies all the values of one DataSet into the new one.
     *
     * @param ds DataSet to copy
     */
	public DataSet(DataSet ds) {
		x=ds.getX().copy();
		y=ds.getY().copy();
		attb=ds.getAttb().copy();
		adj=ds.getAdj().copy();
		Detail=ds.getDetailLevel();
		loadedFiles = ds.loadedFiles;
		
	}
	

	/**
	 * This method generates random values for the x and y matrices.
     * @param nodeCount the number of nodes to generate.
     * @param lowerBound lowest random number to generate
     * @param upperBound highest random number to generate
	 */
	public void generateRandom(int nodeCount,double lowerBound, double upperBound){
		for (int i=0;i<nodeCount;i++){
			x.setAsDouble((Math.random()*(upperBound-lowerBound))+lowerBound,0, i);
			y.setAsDouble((Math.random()*(upperBound-lowerBound))+lowerBound,0, i);
		}
	}
	/**
	 * @return the x
	 */
	public Matrix getX() {return x;}

	/**
	 * @param x the x to set
	 */
	public void setX(Matrix x) {this.x = x;}
    /**
     * Appends data to x using util.combine
     * @param x Matrix to add to x
     */
	/**
	 * @return the x
	 */
	public String getTitle() {return title+(!extraTitle.equals("")?extraTitle:"");}

	public void setExtraTitle(String extraTitle) {
		this.extraTitle = extraTitle;
	}

	public String getExtraTitle() {
		return extraTitle;
	}

	/**
	 * @param x the x to set
	 */
	public void setTitle(String s) {this.title = s;}
    /**
     * Appends data to x using util.combine
     * @param x Matrix to add to x
     */
    public void addX(Matrix x) {this.x=util.combine(this.x, x);}
    /**
     *
     * @return true if X has data
     */
    public boolean hasX(){return !x.isEmpty();}
    /**
     *
     * @return true if Y had data
     */
    public boolean hasY(){return !y.isEmpty();}
    /**
     *
     * @return true if Adjacency Matrix has data
     */
    public boolean hasAdj(){return !adj.isEmpty();}
    /**
     *
     * @return true if Attribute Matrix has data
     */
    public boolean hasAttb(){return !attb.isEmpty();}

	/**
	 * @return the y
	 */
	public Matrix getY() {return y;}

	/**
	 * @param y the y to set
	 */
	public void setY(Matrix y) {this.y = y;}
    /**
     * Appends input to Y matrix using util.combine
     * @param y data to add to Y
     */
    public void addY(Matrix y) {this.y=util.combine(this.y, y);}

	/**
	 * @return the x,y matrix
	 */
	public Matrix getXY() {
		return util.combine(x, y);
	}

	/**
     *
     * @param xy Sets x and y matricies
     */
	public void setXY(Matrix xy) {
		Matrix[] tmp=util.SplitXYAttb(xy);
		this.x = tmp[0];
		this.y = tmp[1];
	}

	/**
     * Sets x and y matricies
	 * @param x
	 * @param y
	 */
	public void setXY(Matrix x, Matrix y) {
		this.x = x;
		this.y = y;
	}

	/**
     * Adds data to x and y matrix
	 * @param x
	 * @param y
	 */
	public void addXY(Matrix x, Matrix y) {
		this.x = util.combine(this.x,x);
		this.y = util.combine(this.y,y);
	}
    /**
     *
     * @return true if x and y have data
     */
    public boolean hasXY(){return (!x.isEmpty() && !y.isEmpty());}

	/**
	 * @return the adj
	 */
	public Matrix getAdj() {return adj;}
    /**
     * same as getAdj()
     * @return the adj
     */
    public Matrix getAdjacencyMatrix() {return adj;}

	/**
	 * @param adj the adj to set
	 */
	public void setAdj(Matrix adj) {this.adj = adj;}
    /**
     *
     * @param adj
     */
    public void setAdjacencyMatrix(Matrix adj) {this.adj = adj;}
	

	/**
	 * @return the attb
	 */
	public Matrix getAttb() {return attb;}
    /**
     * Same as getAttb()
     * @return attb
     */
    public Matrix getAttr() {return attb;}
    /**
     * Same as getAttb()
     * @return attb
     */
    public Matrix getAttributeMatrix() {return attb;}

	/**
	 * @param attb the attb to set
	 */
	public void setAttb(Matrix attb) {this.attb = attb;}
    /**
     * Same as setAttb(Matrix attb)
     * @param attb
     */
    public void setAttr(Matrix attb) {this.attb = attb;}
    /**
     * Same as setAttb(Matrix attb)
     * @param attb
     */
    public void setAttributeMatrix(Matrix attb) {this.attb = attb;}

    /**
     * Sets the list of loaded files
     * @param fl
     */
    public void setFileList(Vector<String> fl){
        loadedFiles = fl;
    }
    /**
     * Add a file to the loaded files list
     * @param file
     */
    public void addFile(String file){
        loadedFiles.add(file);
        if(this.title.equals("[no title]")) this.title=file;
    }
    /**
     *
     * @return vector of all files loaded
     */
    public Vector<String> GetLoadedFiles(){
        return loadedFiles;
    }

	/**
	 * @return the current detail level
	 */
	public int getDetailLevel() {return this.Detail;}
	/**
	 * @param d The level of detail you want to use when calling toString().
	 * The values are:
	 * 	1: Print all the matrix values.
	 * 	2: Also print the Matrix.toString() value
	 */
	public void setDetailLevel(int d) {this.Detail = d;}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
    /**
     *
     * @return Information about the data set in the form of a string
     */
    @Override
	public String toString() {
		String out=new String();
        if(!loadedFiles.isEmpty()){
            out+="Files: "+loadedFiles.elementAt(0);
            for(int i = 1; i<loadedFiles.size(); i++)
                out+=", "+loadedFiles.elementAt(i);
            out+="\n";
        }
        if(!x.isEmpty()){
            out+="X: \n";
            out+=util.matrixToString(x);
//            if(Detail>=1){
//                //print headers
//                for(int i=0; i<x.getColumnCount(); i++)
//                    out+=x.getColumnLabel(i)+" ";
//                out+="\n";
//            }
//            if(Detail>=2){
//                //print matrix
//                out+= x.toString()+"\n";
//            }
        }else{
        	out+="There are no X values loaded!\n";
        }
        //add y attributes
        if(!y.isEmpty()){
            out+="Y: \n";
            out+=util.matrixToString(y);
//            if(Detail>=1){
//                //print headers
//                for(int i=0; i<y.getColumnCount(); i++)
//                    out+=y.getColumnLabel(i)+" ";
//                out+="\n";
//            }
//            if(Detail>=2){
//                //print matrix
//                out+= y.toString()+"\n";
//            }
        }else{
        	out+="There are no Y values loaded!\n";
        }
        //add adj attributes
        if(!adj.isEmpty()){
            out+="Edges:\n";
            out+=util.matrixToString(adj);
//            if(Detail>=1){
//                //print headers
//                for(int i=0; i<adj.getColumnCount(); i++)
//                    out+=adj.getColumnLabel(i)+" ";
//                out+="\n";
//            }
//            if(Detail>=2){
//                //print matrix
//                out+= adj.toString()+"\n";
//            }
        }else{
        	out+="There are no adjacency values loaded!\n";
        }
        //add attb attributes
        if(!attb.isEmpty()){
            out+="Attributes: \n";
            out+=util.matrixToString(attb);
//            if(Detail>=1){
//                //print headers
//                for(int i=0; i<attb.getColumnCount(); i++)
//                    out+=attb.getColumnLabel(i)+" ";
//                out+="\n";
//            }
//            if(Detail>=2){
//                //print matrix
//                out+= attb.toString()+"\n";
//            }
        }else{
        	out+="There are no attribute values loaded!\n";
        }

		return out;
	}
//----------------------------------------------------------------------------------------------------------------

    /**
     * Clears all matricies and loaded filenames
     */
    public void ClearData(){
        x = MatrixFactory.emptyMatrix();
        y = MatrixFactory.emptyMatrix();
        adj = MatrixFactory.emptyMatrix();
        attb = MatrixFactory.emptyMatrix(); 

        loadedFiles=new Vector<String>();

    }

    /**
     * Displays detail of loaded files
     * @param Detail level of detail to be recorded (the higher the number, the more detail)
     * @return String containing detail information
     */
    public String Status(int Detail){
        String out= "Loaded Files: ";

        //add loaded files
        if(loadedFiles.isEmpty()){
            out+=" NO FILES LOADED";
        }
        else{
            for(int i=0; i<loadedFiles.size(); i++)
                out+=loadedFiles.elementAt(i)+" ";
            }
            
        out+="\n";
        //add x attributes
        if(!x.isEmpty()){
            out+="X: ["+x.getRowCount()+","+x.getColumnCount()+"] \n";
            if(Detail>=1){
                //print headers
                for(int i=0; i<x.getColumnCount(); i++)
                    out+=x.getColumnLabel(i)+" ";
                out+="\n";
            }
            if(Detail>=2){
                //print matrix
                out+= x.toString()+"\n";
            }
        }
        //add y attributes
        if(!y.isEmpty()){
            out+="Y: ["+y.getRowCount()+","+y.getColumnCount()+"] \n";
            if(Detail>=1){
                //print headers
                for(int i=0; i<y.getColumnCount(); i++)
                    out+=y.getColumnLabel(i)+" ";
                out+="\n";
            }
            if(Detail>=2){
                //print matrix
                out+= y.toString()+"\n";
            }
        }
        //add adj attributes
        if(!adj.isEmpty()){
            out+="Edges: ["+adj.getRowCount()+","+adj.getColumnCount()+"] \n";
            if(Detail>=1){
                //print headers
                for(int i=0; i<adj.getColumnCount(); i++)
                    out+=adj.getColumnLabel(i)+" ";
                out+="\n";
            }
            if(Detail>=2){
                //print matrix
                out+= adj.toString()+"\n";
            }
        }
        //add attb attributes
        if(!attb.isEmpty()){
            out+="Attributes: ["+attb.getRowCount()+","+attb.getColumnCount()+"] \n";
            if(Detail>=1){
                //print headers
                for(int i=0; i<attb.getColumnCount(); i++)
                    out+=attb.getColumnLabel(i)+" ";
                out+="\n";
            }
            if(Detail>=2){
                //print matrix
                out+= attb.toString()+"\n";
            }
        }
        return out;
    }

    
    /**
     * Appends another DataSet to this one. Appends each matrix in the new to this one.
     * @param ds the DataSet to Append to this
     */
    public void append(DataSet ds){
    	x=util.combine(x, ds.getX());
    	y=util.combine(y, ds.getY());
    	attb=util.combine(x, ds.getAttb());
    	adj=util.combine(x, ds.getAdj());
    }

    /**
     * Calls append() to add two DataSets together. This method does 
     * not change either DataSet. If you want to add one DataSet to another, 
     * use append().
     * @param ds1
     * @param ds2
     * @return A new DataSet
     */
    public DataSet add(DataSet ds1,DataSet ds2){
    	DataSet ret=new DataSet(ds1);
    	ret.append(ds2);
    	return ret;
    }
    
    /**
     * converts a stored polar coordinates to stored xy coordinates
     */
    public void PolarToXY(){
        double preX =0, preY=0, nextX, nextY;
        for(int row=0; row<x.getRowCount(); row++){
            double tempX = y.getAsDouble(row,0) * Math.cos(x.getAsDouble(row,0));
            double tempY = y.getAsDouble(row,0) * Math.sin(x.getAsDouble(row,0));

            nextX = preX + tempX;
            nextY = preY + tempY;

            x.setAsDouble(nextX, row, 0);
            y.setAsDouble(nextY, row, 0);

            preX = nextX;
            preY = nextY;
        }
    }
    /**
     * Translates the stored xy to a new xy
     * @param xmove amount to move in x direction
     * @param ymove amount to move in y direction
     * @throws IllegalStateException
     */
    public void translate(double xmove, double ymove)throws IllegalStateException{
        if(!x.isEmpty() && !y.isEmpty()){
            MatrixConversion mc = new MatrixConversion();
            Matrix temp = util.combine(x,y);
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
    public void reflect(int Axis)throws IllegalStateException{
        //x=0 y=1
        if(!x.isEmpty() && !y.isEmpty()){
            MatrixConversion mc = new MatrixConversion();
            Matrix temp = util.combine(x,y);
            temp = mc.Reflection(temp, Axis);
            x = temp.selectColumns(Calculation.Ret.NEW, 0);
            y = temp.selectColumns(Calculation.Ret.NEW, 1);
        } else throw new IllegalStateException("no XY data loaded");
    }
    /**
     * Rotates the xy data about the origin
     * @param Degrees Degrees to rotate
     * @throws java.lang.IllegalStateException
     */
    public void rotate(double Degrees)throws IllegalStateException{
        if(!x.isEmpty() && !y.isEmpty()){
	        MatrixConversion mc = new MatrixConversion();
	        Matrix temp = util.combine(x,y);
	        temp = mc.RotateClockwise(temp, Degrees);
            x = temp.selectColumns(Calculation.Ret.NEW, 0);
            y = temp.selectColumns(Calculation.Ret.NEW, 1);
        } else throw new IllegalStateException("no XY data loaded");
    }

    /**
     * @param factor Factor to scale by
     * @throws IllegalStateException
     */
    public void scale(double factor)throws IllegalStateException{
        if(!x.isEmpty() && !y.isEmpty()){
            MatrixConversion mc = new MatrixConversion();
            Matrix temp = util.combine(x,y);
            temp = mc.Scale(temp, factor);
            x = temp.selectColumns(Calculation.Ret.NEW, 0);
            y = temp.selectColumns(Calculation.Ret.NEW, 1);
        } else throw new IllegalStateException("no XY data loaded");
    }
    
    private void _setMatrixHeaders(Matrix m, String[] s){
    	for (int i=0;i<s.length;i++){	//loop through the string
    		if(i<m.getColumnCount()){	//make sure we can assign to valid columns
    			m.setColumnLabel(i, s[i]);
    		}else{
    			return;					//don't worry about extra header information
    		}
    	}
    	
    }
    
    public void toGUI(){
    	this.x.showGUI();
    	this.y.showGUI();
    	this.adj.showGUI();
    	this.attb.showGUI();
    }
    
    /**
     * Set the headers for x,y coordinate matricies
     * @param s A pair of string labels for the vector matricies
     */
    public void setCoordHeaders(String[] s){_setMatrixHeaders(x,s);_setMatrixHeaders(y,s);}
    /**
     * Sets the headers on the adjacency matrix
     * @param s Set of labels to use
     */
    public void setAdjHeaders(String[] s){_setMatrixHeaders(adj,s);}
    /**
     * Sets the headers on the attribute matrix
     * @param s Set of labels to use
     */
    public void setAttbHeaders(String[] s){_setMatrixHeaders(attb,s);}

	public void addHeuristic(Matrix theMatrix) {
		Matrix m = MatrixFactory.copyFromMatrix(theMatrix);
		util.copyColumnLabels(theMatrix, m);
		int egolbl=-1, idlbl=-1, xlbl=-1,ylbl=-1;
		
		for(int i=0;i<m.getColumnCount();i++){
			String lbl = m.getColumnLabel(i).toLowerCase();
			
			//get first x and y columns with labels that have 'dist' or 'coo' in them
			if( lbl.contains("coo") || lbl.contains("dist") ){
				if((lbl.startsWith("x") || lbl.endsWith("x") ) && xlbl==-1)
					xlbl=i;
				if((lbl.startsWith("y") || lbl.endsWith("y") ) && ylbl==-1)
					ylbl=i;
			}
			//TODO figure out adjacencies
		}
		
		//System.out.println("  Ego: "+egolbl+"  X: "+xlbl+"  Y: "+ylbl);
		if(xlbl!=-1){
			Matrix xm = m.selectColumns(Calculation.Ret.NEW, xlbl).toDoubleMatrix();
			m.setColumnLabel(0, m.getColumnLabel(xlbl));
			this.setX(xm);
			m = m.deleteColumns(Calculation.Ret.NEW,xlbl);
			ylbl--;
		}
		if(ylbl!=-1){
			Matrix ym = m.selectColumns(Calculation.Ret.NEW, ylbl).toDoubleMatrix();
			m.setColumnLabel(0, m.getColumnLabel(ylbl));
			this.setY(ym);
			m = m.deleteColumns(Calculation.Ret.NEW,ylbl);
		}

		this.setAttb(m);
	}

	public Vector<DataSet> SplitByColumn(Matrix theMatrix,int colNum,int rowStart) {
		Matrix mFull = MatrixFactory.copyFromMatrix(theMatrix); //don't destroy input matrix
		util.copyColumnLabels(theMatrix, mFull);
		util.printHeaders(theMatrix);
		Vector<DataSet> dslist = new Vector<DataSet>(); //the list returned
		Vector<Matrix> mlist = new Vector<Matrix>();	//new matrices
		
		//remove all rows from 0 to startRow
		if(rowStart>0)
			mFull = mFull.deleteRows(Calculation.Ret.NEW, 0, rowStart);

		//int egocol=-1;
		
		//get first ego column
		/*for(int i=0;i<m.getColumnCount();i++){
			String lbl = m.getColumnLabel(i).toLowerCase();
			if(lbl.contains("ego") && egocol==-1){
				egocol=i;
				break;
			}
		}
		if(egocol==-1)egocol=0;		//if no ego column found, default to first column 
		*/

		//the first ego name in matrix
		int totalRowCount=(int) mFull.getRowCount();
		boolean found = false;
		while(!mFull.isEmpty()){		//while matrix is viable
			String curID = mFull.getAsString(0,colNum);					//the current name
			Matrix next = mFull.selectRows(Calculation.Ret.NEW, 0);		//start matrix using first row
			util.copyColumnLabels(mFull, next);
			mFull = mFull.deleteRows(Calculation.Ret.NEW, 0);			//remove first row
			//if (!found) System.out.println("Processing for "+curID+". "+mFull.getRowCount()+"/"+totalRowCount+" rows remaining.");
			
			for(int row=0;row<mFull.getRowCount();row++){			//loop through each row
				String curGroup = mFull.getAsString(row,colNum);	//the current string for sorting
				if(curID.equals(curGroup)){							//test to see if this row has 
					next = next.appendVertically(mFull.selectRows(Calculation.Ret.NEW, row));
					mFull = mFull.deleteRows(Calculation.Ret.NEW, row);
//					mFull = moveRow(mFull,next,row);
					//System.out.println("Removing row "+row+" for "+curGroup+". New size is "+mFull.getRowCount());
					row--;
					found=true;
				}
			}
			//System.out.println("New matrix has "+next.getRowCount()+" rows.");
			util.copyColumnLabels(mFull, next);
			mlist.add(next);	//add new matrix to list
		}		
		for(Matrix thisM : mlist){
			DataSet n = new DataSet(thisM);
			n.addFile(this.GetLoadedFiles().elementAt(0));
			n.addHeuristic(thisM);
			dslist.add(n);
		}
		
		return dslist;
	}	
	private Matrix moveRow(Matrix from,Matrix to, int row){
		to.appendHorizontally(from.selectRows(Calculation.Ret.NEW, row));
		from = from.deleteRows(Calculation.Ret.NEW, row);
		return from;
		
	}

	public void convertUnknownCoords(int alg, Dimension args) {
		// set x, y
		x=(MatrixFactory.zeros(adj.getRowCount(), 1));
		y=(MatrixFactory.zeros(adj.getRowCount(), 1));
		Algorithm.CalculateUnknownCoordinates(this,args);
	}
}

