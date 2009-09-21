/*
 * This program will convert network file with known geographic coordinates
 * into a shapefile.
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
import org.ujmp.core.enums.FileFormat;

/*
 *
 * @author Charles Bevan
 * @date September 16, 2009
 * @version 0.0.1
 */
public class DLwriter extends TextFileWriter {

    //public constants for file type
    public final static boolean DAT = true;
    public final static boolean TXT = false;

    //file format data
    private String ext = ".dat";

    //constructors
    public DLwriter(Matrix map, String filename){
        this.setWorkingset(map);
        this.CreateFile(filename);
    }
    public DLwriter(Matrix map, String filename, boolean type){
        this.setWorkingset(map);
        if(type)
            ext=".dat";
        else
            ext=".txt";
        this.CreateFile(filename);
    }

    @Override
    public File CreateFile(String name) {
        return new File(name, ext);
    }

    @Override
    public void WriteFile() {
        try {
            PrintWriter pw = new PrintWriter(this.getFile());
            //write header information
            pw.print("dl nr="+this.getWorkingset().getRowCount()+" nc="+this.getWorkingset().getColumnCount()+"\n");
            //write labels
            pw.print("col labels:\n");
            for(int i=0; i<this.getWorkingset().getColumnCount();i++){
                if(i<this.getWorkingset().getColumnCount()-1)
                    pw.print(this.getWorkingset().getColumnLabel(i)+", ");
                else
                    pw.print(this.getWorkingset().getColumnLabel(i));
            }
            pw.print("\n");
            //write data
            pw.println("data:");
            pw.print(this.getWorkingset().exportToString(FileFormat.TXT));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
