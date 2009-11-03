/*
 * This is a excel reader class.
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP and jxl
 *
 */
package us.jonesrychtar.gispatialnet.Reader;

import java.io.File;
import jxl.Workbook;
import org.ujmp.core.Matrix;
//import org.ujmp.core.objectmatrix;
import org.ujmp.core.MatrixFactory;

/**
 *
 * @author Charles Bevan
 * @date September 28, 2009
 * @version 0.0.1
 */
public class ExcelReader {

    private File in;

    public ExcelReader(String filename) {
        in = new File(filename);
    }

    public Matrix read(int MatrixFormat, int rows, int c) throws Exception {
        Matrix out = MatrixFactory.zeros(rows, c);
        Workbook w1 = Workbook.getWorkbook(in);
        String loc;
        //cols characters
        char cols[] = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        //read all col, rows of sheet 1
        boolean quit = false;

        switch (MatrixFormat) {
            case 0: {
                //i is row j is col
                for (int i = 1; i <= rows; i++) {
                    for (int j = 0; j < c && j <= 256; j++) { //Max 256 col
                        //calculate col (A - IV)
                        String pos = "";
                        if (j / 26 > 1) {
                            pos += cols[(j / 26) - 1];
                        }
                        pos += cols[(j % 26)];
                        loc = "Sheet1!" + pos + i;
                        //get contents of cell
                        String content = w1.getCell(loc).getContents();

                        //if this is col header
                        if (i == 1) {
                            out.setColumnLabel(i - 1, content);
                        } else {
                            out.setAsDouble(new Double(content), i - 1, j);
                        }
                    }
                }
                break;
            }
            case 1:{ //lower
                for (int i = 1; i <= rows; i++) {
                    for (int j = 0; j < i-1 && j <= 256; j++) { //Max 256 col
                        //calculate col (A - IV)
                        String pos = "";
                        if (j / 26 > 1) {
                            pos += cols[(j / 26) - 1];
                        }
                        pos += cols[(j % 26)];
                        loc = "Sheet1!" + pos + i;
                        //get contents of cell
                        String content = w1.getCell(loc).getContents();

                        //if this is col header
                        if (i == 1) {
                            out.setColumnLabel(i - 1, content);
                        } else {
                            out.setAsDouble(new Double(content), i - 1, j);
                        }
                    }
                }
                out = ReaderUtil.LowerToFull(out);
                break;
            }
            case 2:{ //upper
                for (int i = 1; i <= rows; i++) {
                    for (int j = i-1; j < c && j <= 256; j++) { //Max 256 col
                        //calculate col (A - IV)
                        String pos = "";
                        if (j / 26 > 1) {
                            pos += cols[(j / 26) - 1];
                        }
                        pos += cols[(j % 26)];
                        loc = "Sheet1!" + pos + i;
                        //get contents of cell
                        String content = w1.getCell(loc).getContents();

                        //if this is col header
                        if (i == 1) {
                            out.setColumnLabel(i - 1, content);
                        } else {
                            out.setAsDouble(new Double(content), i - 1, j);
                        }
                    }
                }
                out = ReaderUtil.UpperToFull(out);
                break;
            }
        }
        return out;
    }
}
