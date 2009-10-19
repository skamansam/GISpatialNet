/*
 * This is the DL/UCINET reader class
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 *
 */
package us.jonesrychtar.gispatialnet.Reader;

import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import java.util.Scanner;
import java.util.Vector;

/**
 *
 * @author Sam Tyler, Charles Bevan
 * @date September 17, 2009
 * @version 0.0.1
 */
public class DLreader extends TextFileReader {

    private class DLHeaders {
        //covers nr, nc, and n

        public int nr,  nc;
        //format (defined in FileReader)
        //fullmatrix =0, lowerhalf = 1, upperhalf =2;
        //BlockMatrix and LinkedList Matrix (nodelist, edgelist, ranked list) not supported
        public int format = 0;
        //Labels: and col lables:
        //separated either by spaces, commas or carriage returns (or both).
        //Labels cannot contain embedded spaces unless you enclose them in quotes, as in "Tom Smith".
        public Vector<String> labels;
        //row labels embedded
        public boolean rembed = false;
        public Vector<String> rlabels; //used in row labels:
        //col labels embedded
        public boolean cembed = false;
        public Vector<String> clables; //used in col labels:
        //number of matricies nm
        public int nm = 1;
        //matrix labels
        public Vector<String> mlabels;
        //datafile, defines a pointer to data in seperate file
        public String datafileName;
        //diagonal: absent
        public boolean isDiagAbsent = false;
    }
    DLHeaders header = new DLHeaders();

    public DLreader(String file) {
        this.setFile(this.openFile(file));
    }

    @Override
    public Matrix Read(int type, int rows, int col) throws Exception {
        Scanner sc;
        header.nc = col;
        header.nr = rows;
        //make empty matrix
        Matrix output = MatrixFactory.zeros(org.ujmp.core.enums.ValueType.STRING, header.nr, header.nc);
        sc = new Scanner(this.getFile());
        //read headers
        String word = sc.next().toLowerCase();
        while (!(word.equals("data:"))) {
            //analyze header
            _analyzeHeader(word, sc);
            word = sc.next().toLowerCase();
        }
        //read matrix

        //type 0 = full matrix
        if (type == 0) {
            //if col labels embedded, read labels
            if (header.cembed) {
                for (int c = 0; c < header.nc; c++) {
                    output.setColumnLabel(c, sc.next());
                }
            }
            for (int i = 0; i < rows; i++) {
                if (header.rembed) {
                    output.setAsString(sc.next(), i, 0);
                    for (int j = 1; j <= col; j++) {
                        if ((!header.rembed && (header.isDiagAbsent && i == j)) ||
                                (header.rembed && (header.isDiagAbsent && i == j + 1))) {
                            output.setAsString("0", i, j);
                        } else {
                            output.setAsString(sc.next(), i, j);
                        }
                    }
                } else {
                    for (int j = 0; j < col; j++) {
                        output.setAsString(sc.next(), i, j);
                    }
                }
            }
        } //type 1 = lower matrix
        else if (type == 1) {
            if (header.cembed) {
                for (int c = 0; c < header.nc; c++) {
                    output.setColumnLabel(c, sc.next());
                }
            }
            if (header.rembed) {
            }
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < (col - i); j++) {
                    output.setAsString(sc.next(), i, j);
                }
            }
        } //type 2 = upper matrix
        else if (type == 2) {
            if (header.cembed) {
                for (int c = 0; c < header.nc; c++) {
                    output.setColumnLabel(c, sc.next());
                }
            }
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < (col - (col - (i + 1))); j++) {
                    output.setAsString(sc.next(), i, j);
                }
            }
        } else {
            System.err.println("Invalid format");
        }
        return output;
    }

    private boolean _analyzeHeader(String in, Scanner sc) throws Exception {
        //check for n
        if (in.equals("n") || in.equals("n=")) {
            if (in.equals("n")) {
                sc.next();
            }
            int temp = sc.nextInt();
            header.nc = temp;
            header.nr = temp;
            return true;
        } //check for nc
        else if (in.equals("nc") || in.equals("nc=")) {
            if (in.equals("nc")) {
                sc.next();
            }
            header.nc = sc.nextInt();
            return true;
        } //check for nr
        else if (in.equals("nr") || in.equals("nr=")) {
            if (in.equals("nr")) {
                sc.next();
            }
            header.nr = sc.nextInt();
        } //check for format
        else if (in.equals("format") || in.equals("format=")) {
            if (in.equals("format")) {
                sc.next();
            }
            String form = sc.next().toLowerCase();
            if (form.equals("fullmatrix")) {
                header.format = 0;
            }
            else if (form.equals("lowerhalf")) {
                header.format = 1;
            }
            else if (form.equals("upperhalf")) {
                header.format = 2;
            }
            else if (form.equals("blockmatrix")){
                //unsupported
                throw new Exception("blockmatrix not supported");
            }
            return true;
        } //check for row
        else if (in.equals("row")) {
            String word = sc.next().toLowerCase();
            //row labes:
            if (word.equals("labels:")) {
                in = word;
            }
            //row labels embeded
            if (word.equals("lables") && sc.next().equals("embedded")){
                header.rembed =true;
                return true;
            }
        }
        //check for col
        else if (in.equals("col")) {
            String word = sc.next().toLowerCase();
            //col labes:
            if (word.equals("labels:")) {
                //TODO: this needs to set first col of entire table

            }
            //row labels embeded
            if (word.equals("lables") && sc.next().equals("embedded")){
                header.cembed =true;
                return true;
            }
        }
        //nm (number of matricies
        else if(in.equals("nm") || in.equals("nm=")){
            if(in.equals("nm")){
                sc.next();
            }
            header.nm = sc.nextInt();
            return true;
        }
        //check for diagonal: absent
        else if(in.equals("diagonal:") && sc.next().equals("absent")){
            header.isDiagAbsent=true;
            return true;
        }

        //unsupported headers (throw exception)
        //check for matrix labels
        else if(in.equals("matrix") && sc.next().equals("labels:")){
            throw new Exception("\"matrix labels:\" not supported");
        }
        //check for datafile
        else if (in.equals("datafile")){
            throw new Exception("\"datafile\" not supported");
        }

        //end unsupported headers
        
        //check for labels
        if (in.equals("labels:")) {
            String word = sc.next();
            if (!(_analyzeHeader(word, sc))) {
                header.labels.add(word);
            }
            return true;
        }
        return false;
    }
}
