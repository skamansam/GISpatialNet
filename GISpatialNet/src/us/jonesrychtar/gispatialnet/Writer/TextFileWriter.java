/*
 * This is an abstract class for text file writers (csv and dl/ucinet)
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP
 *
 */
package us.jonesrychtar.gispatialnet.Writer;

import java.io.File;
import org.ujmp.core.Matrix;

/**
 *
 * @author Charles Bevan
 * @date September 16, 2009
 * @version 0.0.1
 */
public abstract class TextFileWriter {

    private File file;
    private Matrix workingset;


    public void setFile(File var){
        file=var;
    }

    public File getFile(){
        return file;
    }

    public abstract File CreateFile(String name);

    public Matrix getWorkingset(){
        return workingset;
    }

    public void setWorkingset(Matrix val){
        workingset=val;
    }
    
    public abstract void WriteFile();

}
