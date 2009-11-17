/*
 * This is a generic csv writer
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP and Mindprod CSV Writer
 *
 */
package us.jonesrychtar.gispatialnet.Writer;

import com.mindprod.csv.CSVWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.ujmp.core.Matrix;

/**
 *
 * @author cfbevan
 * @version 0.0.1
 */
public class CSVwriter extends TextFileWriter {
    //formatting for file
    private int quoteLevel=0;
    private char seperator=',';
    private char quote='\"';
    private char comment='#';
    private boolean trimQuote=true;
    //writer
    private CSVWriter out;

    /**
     * Constructor
     * @param map Matrix to be written to file
     * @param filename name of output file
     */
    public CSVwriter(Matrix map, String filename){
        this.setWorkingset(map);
        this.setFile(CreateFile(filename));
    }
    
    /**
     * Constructor
     * @param map Matrix to be written to file
     * @param filename name of output file
     * @param sep character that seperates values
     */
    public CSVwriter(Matrix map, String filename,char sep){
        seperator=sep;
        this.setWorkingset(map);
        this.setFile(CreateFile(filename));
    }

    //getters
    /**
     *
     * @return quote leve
     */
    public int getQuoteLevel() {
        return quoteLevel;
    }
    
    /**
     *
     * @return comment character
     */
    public char getComment() {
        return comment;
    }

    /**
     *
     * @return quote character
     */
    public char getQuote() {
        return quote;
    }

    /**
     *
     * @return seperator character
     */
    public char getSeperator() {
        return seperator;
    }

    /**
     *
     * @return true if trim quote is on
     */
    public boolean isTrimQuote() {
        return trimQuote;
    }

    
    //setters
    /**
     * Sets quote level
     * @param quoteLevel
     */
    public void setQuoteLevel(int quoteLevel) {
        this.quoteLevel = quoteLevel;
    }
    
    /**
     * sets comment character
     * @param comment Character that signifies a comment
     */
    public void setComment(char comment) {
        this.comment = comment;
    }

    /**
     * sets quote character
     * @param quote Character that signifies a quote
     */
    public void setQuote(char quote) {
        this.quote = quote;
    }

    /**
     * sets seperator character
     * @param seperator character that seperates data
     */
    public void setSeperator(char seperator) {
        this.seperator = seperator;
    }

    /**
     * set trim quote
     * @param trimQuote
     */
    public void setTrimQuote(boolean trimQuote) {
        this.trimQuote = trimQuote;
    }

    @Override
    public File CreateFile(String name) {
        if(seperator !=',')
            return new File(name+".txt");
        else
            return new File(name+".csv");
    }

    @Override
    public void WriteFile() throws FileNotFoundException {
        //create output writer
        out = new CSVWriter(new PrintWriter(this.getFile()), quoteLevel, seperator, quote, comment, trimQuote);
        //write headers
        for (int i = 0; i < this.getWorkingset().getColumnCount(); i++) {
            out.put(this.getWorkingset().getColumnLabel(i));
        }
        out.nl();
        //iterate through the matrix and write to file
        for (int j = 0; j < this.getWorkingset().getRowCount(); j++) {
            //out.put(j);
            //out.put("Euclidean");
            for (int k = 0; k < this.getWorkingset().getColumnCount(); k++) {
                out.put(this.getWorkingset().getAsString(j, k));
            }
            out.nl();
        }
        out.close();
    }
}
