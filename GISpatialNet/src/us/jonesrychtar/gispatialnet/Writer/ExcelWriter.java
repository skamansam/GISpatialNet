/*
 * This is a kml writer class, note that the input matrix
 * has a required format.
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP and jxl
 *
 */
package us.jonesrychtar.gispatialnet.Writer;

import java.io.File;
import java.util.Locale;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.ujmp.core.Matrix;

/**
 *
 * @author Charles Bevan
 * @date September 28, 2009
 * @version 0.0.1
 */
public class ExcelWriter {

    private String filen;
    private Matrix workingset;
    private WritableWorkbook workbook;

    public ExcelWriter(Matrix map, String Filename){
        filen = Filename;
        workingset = map;
    }
    
    public void WriteFile(){
        try{
            //setup workbook
            WorkbookSettings ws = new WorkbookSettings();
            ws.setLocale(new Locale("en", "EN"));
            workbook = Workbook.createWorkbook(new File(filen), ws);
            
            WritableSheet s = workbook.createSheet("Sheet1", 0);
            WritableCellFormat wrappedText = new WritableCellFormat
             (WritableWorkbook.ARIAL_10_PT);
            wrappedText.setWrap(true);

            //write book
            Label l;
            Number n;
            Label t;
            //All coordinates are row, col
            for(int i=0; i<workingset.getColumnCount(); i++){
                l = new Label(0, i, (String) workingset.getColumnLabel(i), wrappedText);
                for (int j = 0; j < workingset.getRowCount(); j++) {
                    Object o = workingset.getAsObject(j,i);
                    if (o instanceof java.lang.Double ) {
                        //numbers
                        n = new Number(j, i, (Double) o );
                        s.addCell(n);
                    }
                    else {
                        //text
                        t = new Label(j,i,(String) o, wrappedText);
                        s.addCell(t);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
