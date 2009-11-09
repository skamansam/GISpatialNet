/*
 * This is the Pajek reader class
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 *
 */
package us.jonesrychtar.gispatialnet.Reader;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import java.util.Scanner;
import java.util.Vector;
import us.jonesrychtar.gispatialnet.DataSet;

/**
 *
 * @author Sam Tyler, Charles Bevan
 * @date September 17, 2009
 * @version 0.0.1
 */
public class PajekReader extends TextFileReader {
	//Matrix vertices, arcs, edges;
    //arcs and edges are the same thing
	Matrix vertXY, vertY, vertZ, attr, vertices, arcs;

    public PajekReader(String filename){
        this.setFile(this.openFile(filename));
    }

    @Override
    //TODO: This will now return dataset vector
    public Vector<DataSet> Read(int type, int rows, int col) throws Exception{
        
    	Scanner theFile = new Scanner(this.file);
    	//Matrix theMatrix=MatrixFactory.emptyMatrix();
        Matrix theMatrix = MatrixFactory.zeros(rows,col);
    	while (theFile.hasNext()){
    		String theType=theFile.next();
    		if (theType.equalsIgnoreCase("*Vertices")){
    			int numVerts=theFile.nextInt();
    			for(int i=0;i<numVerts;i++){
    				int theID=theFile.nextInt();
    				String theName=theFile.next();
    				int theCol=-1;
    				while(theFile.hasNextFloat())
    					vertXY.setAsFloat(theFile.nextFloat(), i,theCol++);    				
    			}
    		}else if(theType.equalsIgnoreCase("*Arcs")){
    			
    		}//else if(theType.equalsIgnoreCase("*Edges")){
    			
    		//}
    	}
    	
    	
    	return theMatrix;
    }

}
