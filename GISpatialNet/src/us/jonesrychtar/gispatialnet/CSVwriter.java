/*
 * This program will convert network file with known geographic coordinates
 * into a shapefile.
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP and Mindprod CSV Writer
 *
 */
package us.jonesrychtar.gispatialnet;

import com.mindprod.csv.CSVWriter;
import java.io.File;
import java.io.PrintWriter;
import org.ujmp.core.Matrix;

/*
 *
 * @author Charles Bevan
 * @date September 16, 2009
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
    //consturctor (makes .csv file)
    public CSVwriter(Matrix map, String filename){
        this.setWorkingset(map);
        this.setFile(CreateFile(filename));
    }
    //consturctor with ability to define a different seperator (will make .txt file)
    public CSVwriter(Matrix map, String filename,char sep){
        seperator=sep;
        this.setWorkingset(map);
        this.setFile(CreateFile(filename));
    }

    //getters
    public int getQuoteLevel() {
        return quoteLevel;
    }
    
    public char getComment() {
    return comment;
    }

    public char getQuote() {
    return quote;
    }

    public char getSeperator() {
    return seperator;
    }

    public boolean isTrimQuote() {
    return trimQuote;
    }

    
    //setters
    public void setQuoteLevel(int quoteLevel) {
        this.quoteLevel = quoteLevel;
    }
    
    public void setComment(char comment) {
        this.comment = comment;
    }

    public void setQuote(char quote) {
        this.quote = quote;
    }

    public void setSeperator(char seperator) {
        this.seperator = seperator;
    }

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
    public void WriteFile() {
        try {
            //create output writer
            out = new CSVWriter(new PrintWriter(this.getFile()), quoteLevel, seperator, quote, comment, trimQuote);
            //write headers
            for(int i=0;i<this.getWorkingset().getColumnCount();i++)
                out.put(this.getWorkingset().getColumnLabel(i));
            out.nl();
            //iterate through the matrix and write to file
            for(int j=1;j<this.getWorkingset().getRowCount();j++){

                for(int k=0; k<this.getWorkingset().getColumnCount();k++){
                    out.put(this.getWorkingset().getAsString(j,k));
                }
                out.nl();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
