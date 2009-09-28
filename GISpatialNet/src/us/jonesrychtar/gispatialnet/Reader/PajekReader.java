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

/**
 *
 * @author Sam Tyler, Charles Bevan
 * @date September 17, 2009
 * @version 0.0.1
 */
public class PajekReader extends TextFileReader {

    public PajekReader(Matrix in, String filename){
        this.setFile(this.openFile(filename));
    }

    @Override
    public Matrix Read(int type, int rows, int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
