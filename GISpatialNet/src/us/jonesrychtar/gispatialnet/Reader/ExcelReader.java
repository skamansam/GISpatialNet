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
import java.util.Vector;
import jxl.Workbook;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;

/**
 *
 * @author Charles Bevan
 * @version 0.0.1
 */
public class ExcelReader {

    private File in;

    /**
     * constructor
     * @param filename name of file to read
     */
    public ExcelReader(String filename) {
        in = new File(filename);
    }

    /**
     *
     * @param MatrixFormat format of stored matrix (As defined in TextFileReader)
     * @param rows number of rows per dataset
     * @param c number of columns per dataset
     * @return vector of matrix containing read data
     * @throws java.lang.Exception
     */
    public Vector<Matrix> read(int MatrixFormat, int rows, int c) throws Exception {

        Workbook w1 = Workbook.getWorkbook(in);
        Vector<Matrix> out = new Vector<Matrix>();
        String loc;
        //cols characters
        char cols[] = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        //read all col, rows of sheet s
        for (int s = 1; s <= w1.getNumberOfSheets(); s++) {
            Matrix temp = MatrixFactory.zeros(rows, c);
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
                            loc = "Sheet" + s + "!" + pos + i;
                            //get contents of cell
                            String content = w1.getCell(loc).getContents();

                            //if this is col header
                            if (i == 1) {
                                temp.setColumnLabel(i - 1, content);
                            } else {
                                temp.setAsDouble(new Double(content), i - 1, j);
                            }
                        }
                    }
                    break;
                }
                case 1: { //lower
                    for (int i = 1; i <= rows; i++) {
                        for (int j = 0; j < i - 1 && j <= 256; j++) { //Max 256 col
                            //calculate col (A - IV)
                            String pos = "";
                            if (j / 26 > 1) {
                                pos += cols[(j / 26) - 1];
                            }
                            pos += cols[(j % 26)];
                            loc = "Sheet" + s + "!" + pos + i;
                            //get contents of cell
                            String content = w1.getCell(loc).getContents();

                            //if this is col header
                            if (i == 1) {
                                temp.setColumnLabel(i - 1, content);
                            } else {
                                temp.setAsDouble(new Double(content), i - 1, j);
                            }
                        }
                    }
                    temp = ReaderUtil.LowerToFull(temp);
                    break;
                }
                case 2: { //upper
                    for (int i = 1; i <= rows; i++) {
                        for (int j = i - 1; j < c && j <= 256; j++) { //Max 256 col
                            //calculate col (A - IV)
                            String pos = "";
                            if (j / 26 > 1) {
                                pos += cols[(j / 26) - 1];
                            }
                            pos += cols[(j % 26)];
                            loc = "Sheet" + s + "!" + pos + i;
                            //get contents of cell
                            String content = w1.getCell(loc).getContents();

                            //if this is col header
                            if (i == 1) {
                                temp.setColumnLabel(i - 1, content);
                            } else {
                                temp.setAsDouble(new Double(content), i - 1, j);
                            }
                        }
                    }
                    temp = ReaderUtil.UpperToFull(temp);
                    break;
                }
            }
            out.add(temp);
        }
        return out;
    }
}
