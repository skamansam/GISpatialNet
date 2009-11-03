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
        public int rORc = 0; //temp used in analyzing label lists
        //covers nr, nc, and n
        public int nr,  nc;
        //format (defined in FileReader)
        //fullmatrix =0, lowerhalf = 1, upperhalf =2;
        //BlockMatrix and LinkedList Matrix (nodelist, edgelist, ranked list) not supported
        public int format = 0;
        //Labels: and col lables:
        //separated either by spaces, commas or carriage returns (or both).
        //Labels cannot contain embedded spaces unless you enclose them in quotes, as in "Tom Smith".
        public Vector<String> labels = new Vector<String>();
        //row labels embedded
        public boolean rembed = false;
        public Vector<String> rlabels = new Vector<String>(); //used in row labels:
        //col labels embedded
        public boolean cembed = false;
        public Vector<String> clables = new Vector<String>(); //used in col labels:
        //number of matricies nm
        public int nm = 1;
        //matrix labels
        public Vector<String> mlabels = new Vector<String>();
        //datafile, defines a pointer to data in seperate file
        public String datafileName;
        //diagonal: absent
        public boolean isDiagAbsent = false;
        //used if labels are last in header
        boolean ended = false;
    }
    private DLHeaders header = new DLHeaders();
    private ReaderUtil ru = new ReaderUtil();

    public DLreader(String file) {
        this.setFile(this.openFile(file));
    }

    @Override
    public Matrix Read(int type, int rows, int col) throws Exception {
        Scanner sc;
        header.nc = col;
        header.nr = rows;
        //make empty matrix
        Matrix output = MatrixFactory.zeros(header.nr, header.nc);
        sc = new Scanner(this.getFile());
        //read headers
        String word = sc.next().toLowerCase();
        while (!(word.equals("data:")) && !header.ended) {
            //analyze header
            _analyzeHeader(word, sc);
            if(!header.ended)
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
                if (header.rembed)
                    sc.next();//do nothing with it
                for (int j = 0; j < col; j++) {
                    if (header.isDiagAbsent && i == j) {
                        output.setAsString("1", i, j);
                    } else {
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
            for (int i = 0; i < rows; i++) {
                if (header.rembed) {
                //get header and do nothing
                sc.next();
                }
                for (int j = 0; j <= i; j++) {
                    if (header.isDiagAbsent && i == j) {
                        output.setAsString("1", i, j);
                    } else {
                        output.setAsString(sc.next(), i, j);
                    }
                }
            }
            output = ReaderUtil.LowerToFull(output);
        } //type 2 = upper matrix
        else if (type == 2) {
            if (header.cembed) {
                for (int c = 0; c < header.nc; c++) {
                    output.setColumnLabel(c, sc.next());
                }
            }
            for (int i = 0; i < rows; i++) {
                if (header.rembed) {
                //get header and do nothing
                sc.next();
                }
                for (int j = i; j < (col); j++) {
                    if (header.isDiagAbsent && i == j) {
                        output.setAsString("1", i, j);
                    } else {
                        output.setAsString(sc.next(), i, j);
                    }
                }
            }
            output = ReaderUtil.UpperToFull(output);
        } else {
            System.err.println("Invalid format");
        }

        if(header.isDiagAbsent){
            ru.addDiag(output);
        }

        //add labels if needed
        if(header.clables.size()>0)
            for(int c=0; c<header.clables.size(); c++){
                output.setColumnLabel(c, header.clables.elementAt(c));
            }
        return output;
    }

    private boolean _analyzeHeader(String in, Scanner sc) throws Exception {
        //check for n
        in = in.toLowerCase();
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
                header.rORc = 1;
            }
            //row labels embeded
            String temp = sc.next();
            if (word.equals("labels") && temp.equals("embedded")){
                header.rembed =true;
                return true;
            }
        }
        //check for col
        else if (in.equals("col") || in.equals("column")) {
            String word = sc.next().toLowerCase();
            //col labes:
            if (word.equals("labels:")) {
                in = word;
                header.rORc = 2;
            }
            //col labels embeded
             String temp = sc.next();
            if (word.equals("labels") && temp.equals("embedded")){
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
        else if((in.equals("diagonal:") || in.equals("diagonal"))){
            String temp = sc.next();
            if(temp.equals("absent")){
                header.isDiagAbsent=true;
                return true;
            }
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
            while (!(_analyzeHeader(word, sc)) && !(word.toLowerCase().equals("data:"))) {
                if(header.rORc==1)
                    header.labels.add(word);
                else if(header.rORc==2)
                    header.clables.add(word);
                word = sc.next();
            }
            if(word.toLowerCase().equals("data:"))
                header.ended = true;
            return true;
        }
        return false;
    }
}
