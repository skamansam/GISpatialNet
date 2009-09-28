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
import java.io.PrintWriter;
import org.ujmp.core.Matrix;

/**
 *
 * @author Charles Bevan
 * @date September 16, 2009
 * @version 0.0.1
 */
public class PajekWriter extends TextFileWriter{

    Matrix verticies, arcs;
    public PajekWriter(Matrix vert, Matrix arc, String filename){
        this.setFile(CreateFile(filename));
    }
    @Override
    public File CreateFile(String name) {
        return new File(name+".net");
    }

    @Override
    public void WriteFile() {
         try {
            PrintWriter pw = new PrintWriter(this.getFile());
            //write verticies
            pw.println("*Verticies\t"+verticies.getRowCount());
            for(int i=0; i< verticies.getColumnCount(); i++){
                for(int j=0; j< verticies.getRowCount(); j++){
                    //matrix get function (row, col)
                    pw.print(verticies.getAsString(j,i));
                }
                pw.print("\n");
            }
            //write arcs
            pw.println("*Arcs ");
            for(int i=0; i< arcs.getColumnCount(); i++){
                for(int j=0; j< arcs.getRowCount(); j++){
                    pw.print(arcs.getAsString(j,i));
                }
                pw.print("\n");
            }
            
         }catch(Exception e){
             e.printStackTrace();
         }
    }

}
