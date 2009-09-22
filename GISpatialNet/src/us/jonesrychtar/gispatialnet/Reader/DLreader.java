/*
 * This is the DL/UCINET reader class
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
public class DLreader extends FileReader{

    public DLreader(String file){
        this.setFile(this.openFile(file));
    }

    @Override
    public Matrix Read(int type, int rows, int col) {
        Scanner sc;

        Matrix output = MatrixFactory.emptyMatrix();
        try {
            sc = new Scanner(this.getFile());
            //read headers
            
            //read matrix
            //type 0 = full matrix
            while(!(sc.next().equals("data:")));
            if (type == 1) {
                for(int i = 0; i<rows; i++)
                    for(int j=0; j<col; j++){
                        output.setAsString(sc.next(),i,j);
                    }
            } //type 1 = lower matrix
            else if (type == 2) {
                for(int i = 0; i<rows; i++)
                    for(int j=0; j<(col-i); j++){
                        output.setAsString(sc.next(),i,j);
                    }
            } //type 2 = upper matrix
            else if (type == 3) {
                for(int i = 0; i<rows; i++)
                    for(int j=0; j<(col-(col-(i+1))); j++){
                        output.setAsString(sc.next(),i,j);
                    }
            } else {
                System.err.println("Invalid number");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         return output;
    }

}
