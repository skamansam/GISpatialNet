/**
 * 
 */
package us.jonesrychtar.gispatialnet;

import java.util.Vector;



import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.calculation.Calculation;


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

    private Vector<String> loadedFiles = new Vector<String>();

    private int Detail=2;

    /**
	 * 
	 */
	public DataSet() {
		//creates dataset with empty data
	}

    public DataSet(Matrix x, Matrix y, Matrix adj){
        this.x=x;
        this.y=y;
        this.adj=adj;
    }
    public DataSet(Matrix x, Matrix y){
        this.x=x;
        this.y=y;
    }
    public DataSet(Matrix adj){
        this.adj=adj;
    }
    public DataSet(Matrix x, Matrix y, Matrix adj, Matrix attb){
        this.x=x;
        this.y=y;
        this.adj=adj;
        this.attb=attb;
    }

	/**
	 * Copy Constructor. Copies all the values of one DataSet into the new one.
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
	public void addX(Matrix x) {this.x=util.combine(this.x, x);}
	public boolean hasX(){return !x.isEmpty();}
	public boolean hasY(){return !y.isEmpty();}
	public boolean hasAdj(){return !adj.isEmpty();}
	public boolean hasAttb(){return !attb.isEmpty();}

	/**
	 * @return the y
	 */
	public Matrix getY() {return y;}

	/**
	 * @param y the y to set
	 */
	public void setY(Matrix y) {this.y = y;}
	public void addY(Matrix y) {this.y=util.combine(this.y, y);}

	/**
	 * @return the y
	 */
	public Matrix getXY() {
		return util.combine(x, y);
	}

	/**
	 * @param y the y to set
	 */
	public void setXY(Matrix xy) {
		Matrix[] tmp=util.SplitXYAttb(xy);
		this.x = tmp[0];
		this.y = tmp[1];
	}

	/**
	 * @param x
	 * @param y
	 */
	public void setXY(Matrix x, Matrix y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @param x
	 * @param y
	 */
	public void addXY(Matrix x, Matrix y) {
		this.x = util.combine(this.x,x);
		this.y = util.combine(this.y,y);
	}
	public boolean hasXY(){return (!x.isEmpty() && !y.isEmpty());}

	/**
	 * @return the adj
	 */
	public Matrix getAdj() {return adj;}
	public Matrix getAdjacencyMatrix() {return adj;}

	/**
	 * @param adj the adj to set
	 */
	public void setAdj(Matrix adj) {this.adj = adj;}
	public void setAdjacencyMatrix(Matrix adj) {this.adj = adj;}
	

	/**
	 * @return the attb
	 */
	public Matrix getAttb() {return attb;}
	public Matrix getAttr() {return attb;}
	public Matrix getAttributeMatrix() {return attb;}

	/**
	 * @param attb the attb to set
	 */
	public void setAttb(Matrix attb) {this.attb = attb;}
	public void setAttr(Matrix attb) {this.attb = attb;}
	public void setAttributeMatrix(Matrix attb) {this.attb = attb;}

    /**
     * @param files file list to set
     */
    public void setFileList(Vector<String> fl){
        loadedFiles = fl;
    }
    public void addFile(String file){
        loadedFiles.add(file);
    }
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
	@Override
	public String toString() {
		String out=new String();
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
        }else{
        	out+="There are no X values loaded!\n";
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
        }else{
        	out+="There are no Y values loaded!\n";
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
        }else{
        	out+="There are no adjacency values loaded!\n";
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
     * @param ds the DataSet to Append to this
     * @return A new DataSet
     */
    public DataSet add(DataSet ds1,DataSet ds2){
    	DataSet ret=new DataSet(ds1);
    	ret.append(ds2);
    	return ret;
    }
    //converts a stored polar coordinates to stored xy coordinates
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
     * @param x the X-Coords
     * @param y the Y-Coords
     * @return combined XY Matrix. use SplitXYAttb() to split into respective coords.
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
}
