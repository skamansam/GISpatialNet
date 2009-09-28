/*
 * This is a kml writer class, note that the input matrix
 * has a required format.
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP and jxl
 *
 */
package us.jonesrychtar.gispatialnet.Reader;

import java.io.File;
import jxl.Workbook;
import org.ujmp.core.Matrix;
import org.ujmp.core.objectmatrix.EmptyMatrix;

/**
 *
 * @author Charles Bevan
 * @date September 28, 2009
 * @version 0.0.1
 */
public class ExcelReader {

    private File in;

    public ExcelReader(String filename){
        in = new File(filename);
    }
    public Matrix read(){
        Matrix out = new EmptyMatrix();
        try{
            Workbook w1 = Workbook.getWorkbook(in);
            String loc;
            //TODO: Find a better way to do this
            //cols A to IV
            char cols[] = new char[] {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
            //read all col, rows of sheet 1
            boolean quit = false;
            //i is row j is col
            for(int i = 1; !quit; i++){
                boolean nextLine = false;
                for(int j = 0; !nextLine; j++){
                    loc = "Sheet1!"+cols[j]+i;
                    //check if cell is empty, if yes, go to next line
                    if(w1.getCell(loc).getContents().isEmpty()){
                        nextLine = true;
                    }
                    //if this is col header
                    else if(i==1){
                        out.setColumnLabel(i, w1.getCell(loc).getContents());
                    }
                    else
                        out.setAsDouble(new Double(w1.getCell(loc).getContents()),i,j);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return out;
    }

}
