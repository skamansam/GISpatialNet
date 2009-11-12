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
    
    XMLReader xmlReader;
    InputSource is = null;
    KMLContentHandler data = new KMLContentHandler();
    

    /**
     * Constructor
     * @param Filename name of file to read
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.FileNotFoundException
     * @throws javax.xml.bind.JAXBException
     * @throws java.io.IOException
     */
    public KMLreader(String Filename) throws ParserConfigurationException, SAXException, FileNotFoundException, JAXBException, IOException{

        FileInputStream instream = null;

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        xmlReader = spf.newSAXParser().getXMLReader();

        xmlReader.setContentHandler(data);

        instream = new FileInputStream(Filename);
        is = new InputSource(instream);
    }

    /**
     *
     * @return Matrix[] of read data
     * @throws java.lang.Exception
     */
    public Matrix[] read() throws Exception{
         xmlReader.parse(is);
         
         return new Matrix[]{data.getX(), data.getY(), data.getAdj(), data.getAttb()};
    }
}
