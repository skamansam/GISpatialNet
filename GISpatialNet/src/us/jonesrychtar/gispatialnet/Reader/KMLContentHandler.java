/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package us.jonesrychtar.gispatialnet.Reader;

import java.util.Vector;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.Attributes;

/**
 *
 * @author cfbevan
 */
public class KMLContentHandler extends XMLFilterImpl {

    boolean point = false, line = false, attb = false;
    private Vector<Double> xout = new Vector<Double>();
    private Vector<Double> yout = new Vector<Double>();
    private Vector<Double> adjtemp = new Vector<Double>();
    private Vector<String> attbtemp = new Vector<String>();
    private Vector<Vector<Double>> adjout = new Vector<Vector<Double>>();
    private Vector<Vector<String>> attbout = new Vector<Vector<String>>();
    int Row = 0;


    public Matrix getX(){
        Matrix out = MatrixFactory.zeros(xout.size(), 1);
        for(int row = 0; row<xout.size(); row++){
            out.setAsDouble(xout.elementAt(row), row, 0);
        }
        return out;
    }
    public Matrix getY(){
        Matrix out = MatrixFactory.zeros(yout.size(), 1);
        for(int row = 0; row<yout.size(); row++){
            out.setAsDouble(yout.elementAt(row), row, 0);
        }
        return out;
    }
    public Matrix getAdj(){
        Matrix out = MatrixFactory.zeros(adjout.size(), adjout.elementAt(0).size());
        for(int row=0; row<adjout.size(); row++){
           for(int col=0; col<adjout.elementAt(row).size(); col++){
               out.setAsDouble(adjout.elementAt(row).elementAt(col), row,col);
           }
        }
        return out;
    }
    public Matrix getAttb(){
        Matrix out = MatrixFactory.zeros(attbout.size(), attbout.elementAt(0).size());
        for(int row=0; row<attbout.size(); row++){
           for(int col=0; col<attbout.elementAt(row).size(); col++){
               out.setAsString(attbout.elementAt(row).elementAt(col), row,col);
           }
        }
        return out;
    }
    
    public void characters(char[] ch, int start, int length) {
        String gotString = new String(ch, start, length);
        if (point) {
           if (! gotString.trim().equals("")) {
               String[] ptA = gotString.split(",");
               xout.add(Double.parseDouble(ptA[0]));
               yout.add(Double.parseDouble(ptA[1]));
               point = false;
           }
        }
        if (attb) {
            attbtemp.add(gotString);
            attb = false;
        }
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) {

        if (localName.equals("Point")) {
            point = true;
        }
        else if (localName.equals("name") ||
                localName.equals("description") ||
                localName.equals("scale")){
            attb = true;
        }
    }

    public void endElement(String namespaceURI,String localName,String qName){
        if (localName.equals("Placemark")){
            adjout.add(adjtemp);
            adjtemp = new Vector<Double>();
            attbout.add(attbtemp);
            attbtemp = new Vector<String>();
            Row++;
        }
    }
}
