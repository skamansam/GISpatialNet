/*
 * This is a Pajek file writer writer
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP
 *
 */
package us.jonesrychtar.gispatialnet.Writer;

import java.io.File;
import org.ujmp.core.Matrix;

/*
 *
 * @author Charles Bevan
 * @date September 16, 2009
 * @version 0.0.1
 */
public class PajekWriter extends TextFileWriter{

    public PajekWriter(Matrix in, String filename){
        this.setWorkingset(in);
        this.setFile(CreateFile(filename));
    }
    @Override
    public File CreateFile(String name) {
        return new File(name+".net");
    }

    @Override
    public void WriteFile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
