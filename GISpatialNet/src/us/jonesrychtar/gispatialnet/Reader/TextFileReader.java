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
import org.ujmp.core.Matrix;

/*
 *
 * @author Charles Bevan
 * @date September 17, 2009
 * @version 0.0.1
 */
public abstract class TextFileReader {
    //Type Definitions
    public static int FULL_MATRIX= 0;
    public static int LOWER_MATRIX= 1;
    public static int UPPER_MATRIX= 2;

    protected File file;

    public File getFile(){
        return file;
    }
    public void setFile(File var){
        file=var;
    }
    public File openFile(String filename){
        return new File(filename);
    };
    public abstract Matrix Read(int type, int rows, int col);

}
