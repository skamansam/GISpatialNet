/*
 * This is a Pajek file writer
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP
 *
 */
package us.jonesrychtar.gispatialnet.Writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.ujmp.core.Matrix;

/**
 *
 * @author Charles Bevan
 * @version 0.0.1
 */
public class PajekWriter extends TextFileWriter {

    Matrix verticies, arcs;

    /**
     * Constructor
     * @param vert Matrix containing nodes (x,y)
     * @param arc Matrix containing edges
     * @param filename name of output file
     */
    public PajekWriter(Matrix vert, Matrix arc, String filename) {
        this.setFile(CreateFile(filename));
        verticies = vert;
        arcs = arc;
    }

    @Override
    public File CreateFile(String name) {
        return new File(name + ".net");
    }

    @Override
    public void WriteFile() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(this.getFile());
        //write verticies
        pw.println("*Verticies\t" + verticies.getRowCount());
        for (int row = 0; row < verticies.getRowCount(); row++) {
            for (int col = 0; col < verticies.getColumnCount(); col++) {
                //matrix get function (row, col)
                pw.print(verticies.getAsString(row, col)+" ");
            }
            pw.print("\n");
        }
        //write arcs
        pw.println("*Arcs ");
        for (int row = 0; row < arcs.getRowCount(); row++) {
            for (int col = 0; col < arcs.getColumnCount(); col++) {
                if(arcs.getAsDouble(row,col) > 0)
                    pw.println(" "+(row+1)+" "+(col+1)+" "+arcs.getAsString(row, col));
            }
        }
        pw.close();
    }
}
