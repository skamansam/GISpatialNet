/*
 * This is an abstract class for text file writers (csv, dl/ucinet, pajek)
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
 * @version 0.0.1
 */
public abstract class TextFileWriter {

    private File file;
    private Matrix workingset;


    /**
     * Sets the file that is being used
     * @param var File being used
     */
    public void setFile(File var){
        file=var;
    }

    /**
     *
     * @return File being used
     */
    public File getFile(){
        return file;
    }

    /**
     * Abstract function used to open file
     * @param name name of file to be used without extension
     * @return An open file
     */
    public abstract File CreateFile(String name);

    /**
     *
     * @return Matrix being used
     */
    public Matrix getWorkingset(){
        return workingset;
    }

    /**
     * Sets matrix being used
     * @param val Matrix to use
     */
    public void setWorkingset(Matrix val){
        workingset=val;
    }

    /**
     * Abstract function used to write file
     * @throws Exception
     */
    public abstract void WriteFile() throws Exception;

}
