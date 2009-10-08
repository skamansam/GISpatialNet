/*
 * This program will write DL/UCINET files
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

/**
 *
 * @author Charles Bevan
 * @date September 16, 2009
 * @version 0.0.1
 */
public class DLwriter extends TextFileWriter {

    //public constants for file type
    public final static int DAT = 0;
    public final static int TXT = 1;
    //public final static int XXH = 2; //actually "##h"

    //file format data
    private String ext = ".dat";

    //constructors
    /**
     * Constructor
     * @param map Matrix to be written to file (adj matrix)
     * @param filename name of output file
     */
    public DLwriter(Matrix map, String filename){
        this.setWorkingset(map);
        this.CreateFile(filename);
    }

    /**
     * Constructor
     * @param map MAtrix to be written to file (adj matrix)
     * @param filename name of output file
     * @param type select extension (0:.dat, 1:.txt)
     */
    public DLwriter(Matrix map, String filename, int type){
        this.setWorkingset(map);
        switch(type){
            case 0: ext=".dat"; break;
            case 1: ext=".txt"; break;
            default: ext=".dat"; break;
        }
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
                    //all labels except last have ,
                    pw.print(this.getWorkingset().getColumnLabel(i)+", ");
                else
                    pw.print(this.getWorkingset().getColumnLabel(i));
            }
            pw.print("\n");
            //write data
            pw.println("data:");
            pw.print(this.getWorkingset()); //this should print whole matrix to file
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
