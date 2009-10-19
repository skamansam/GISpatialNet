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

/**
 *
 * @author Sam Tyler, Charles Bevan
 * @date September 17, 2009
 * @version 0.0.1
 */
public class PajekReader extends TextFileReader {
	Matrix vertices, arcs, edges;
	
    public PajekReader(Matrix in, String filename){
        this.setFile(this.openFile(filename));
    }

    @Override
    public Matrix Read(int type, int rows, int col) throws Exception{
        //TODO: not implemented yet
    	Scanner theFile = new Scanner(this.file);
    	Matrix theMatrix=MatrixFactory.emptyMatrix();
    	while (theFile.hasNext()){
    		String theType=theFile.next();
    		if (theType.equalsIgnoreCase("*Vertices")){
    			int numVerts=theFile.nextInt();
    			for(int i=0;i<numVerts;i++){
    				int theID=theFile.nextInt();
    				String theName=theFile.next();
    				int theCol=0;
    				while(theFile.hasNextFloat())
    					theMatrix.setAsFloat(theFile.nextFloat(), i,theCol);
    				
    			}
    		}else if(theType.equalsIgnoreCase("*Arcs")){
    			
    		}else if(theType.equalsIgnoreCase("*Edges")){
    			
    		}
    	}
    	
    	
    	return theMatrix;
    }

}
