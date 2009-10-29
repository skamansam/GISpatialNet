/*
 * This is the Google KML reader class
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp, org.boehn.kmlframework
 *
 */

package us.jonesrychtar.gispatialnet.Reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author cfbevan
 * @date October 5, 2009
 * @version 0.0.1
 */
public class KMLreader {
    
    private JAXBElement o;
    private Unmarshaller unmarshaller;
    XMLReader xmlReader;
    InputSource is = null;
    

    public KMLreader(String Filename) throws ParserConfigurationException, SAXException, FileNotFoundException, JAXBException, IOException{

        FileInputStream instream = null;

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        xmlReader = spf.newSAXParser().getXMLReader();

        xmlReader.setContentHandler(new KMLContentHandler());

        instream = new FileInputStream(Filename);
        is = new InputSource(instream);
    }

    public Matrix[] read() throws Exception{
         //TODO: parse xml
         xmlReader.parse(is);
         
         //get nodes
         
         System.out.println(" ");
         //get name

         //get desc

         //get coord
         //CollectionType.
        
        return new Matrix[]{MatrixFactory.emptyMatrix()};
    }
}
