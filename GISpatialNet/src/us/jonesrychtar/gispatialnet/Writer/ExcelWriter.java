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
import java.io.IOException;
import java.util.Locale;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
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

    /**
     * Constructor
     * @param map MAtrix to be written
     * @param Filename name of output file without extension
     */
    public ExcelWriter(Matrix map, String Filename) {
        filen = Filename;
        workingset = map;
    }

    /**
     * Write matrix to .xls file
     */
    public void WriteFile() throws IOException, WriteException {
        //setup workbook
        WorkbookSettings ws = new WorkbookSettings();
        ws.setLocale(new Locale("en", "EN"));
        workbook = Workbook.createWorkbook(new File(filen), ws);

        WritableSheet s = workbook.createSheet("Sheet1", 0);
        WritableCellFormat wrappedText = new WritableCellFormat(WritableWorkbook.ARIAL_10_PT);
        wrappedText.setWrap(true);

        //write book
        Label l;
        Number n;
        Label t;
        //All coordinates are row, col
        for (int i = 0; i < workingset.getColumnCount(); i++) {
            l = new Label(0, i, (String) workingset.getColumnLabel(i), wrappedText);
            for (int j = 0; j < workingset.getRowCount(); j++) {
                Object o = workingset.getAsObject(j, i);
                if (o instanceof java.lang.Double) {
                    //numbers
                    //col, row, val
                    n = new Number(i, j, (Double) o);
                    s.addCell(n);
                } else {
                    //text
                    t = new Label(i, j, (String) o, wrappedText);
                    s.addCell(t);
                }
            }
        }
        workbook.write();
        workbook.close();
    }
}
