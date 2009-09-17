/*
 * This is an abstract class for text file reading (csv and dl/ucinet)
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP
 *
 */
package us.jonesrychtar.gispatialnet;

import java.io.File;
import org.ujmp.core.Matrix;

/*
 *
 * @author Charles Bevan
 * @date September 17, 2009
 * @version 0.0.1
 */
public abstract class FileReader {
    private File file;

    public File getFile(){
        return file;
    }
    public void setFile(File var){
        file=var;
    }
    public abstract void openFile(String filename);
    public abstract Matrix Read();

}
