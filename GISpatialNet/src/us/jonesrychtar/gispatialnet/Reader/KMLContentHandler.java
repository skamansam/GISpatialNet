/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package us.jonesrychtar.gispatialnet.Reader;

import java.util.Vector;
import org.ujmp.core.Matrix;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.Attributes;

/**
 *
 * @author cfbevan
 */
public class KMLContentHandler extends XMLFilterImpl {

    boolean printMe = false;
    private Vector<Double> xout = new Vector<Double>();
    private Vector<Double> yout = new Vector<Double>();
    private Vector<Vector<Double>> adjout;
    private Vector<String> attbout = new Vector<String>();
    int Row =0;

    private boolean getPrintValue() {
        return printMe;
    }
    private void  setPrintValue(boolean printValue) {
        printMe = printValue;
    }

    public void characters(char[] ch, int start, int length) {
        String gotString = new String(ch, start, length);
        if (printMe) {
           if (! gotString.trim().equals("")) {
               System.out.println( gotString.trim());
           }
        }
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) {

        if (localName.equals("Point")) {
            printMe = true;
        }else{
            
           int attrsLength = attributes.getLength();
           for (int i = 0 ; i < attrsLength ; i ++ ) {

           }
        }
    }

    public void endElement(String namespaceURI,String localName,String qName){
        if (localName.equals("")) {
            
        }

    }
}
