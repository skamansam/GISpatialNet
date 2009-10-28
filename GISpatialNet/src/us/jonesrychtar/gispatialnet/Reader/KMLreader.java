/*
 * This is the Google KML reader class
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp, org.boehn.kmlframework
 *
 */

package us.jonesrychtar.gispatialnet.Reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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
    
    FileInputStream instream = null;
    InputSource is=null;
    XMLReader xmlReader;
    Collection collection;
    
     private Matrix xout,  yout,  adjout,  attbout;

    public KMLreader(String Filename) throws ParserConfigurationException, SAXException, FileNotFoundException, JAXBException{
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        xmlReader = spf.newSAXParser().getXMLReader();

        instream = new FileInputStream(Filename);
        is = new InputSource(instream);

        JAXBContext jc = JAXBContext.newInstance("test.jaxb");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        collection= (Collection)unmarshaller.unmarshal(new File(Filename));


    }

    public Matrix[] read() throws Exception{
         xmlReader.parse(is);
         //TODO: parse xml
         //CollectionType.
        
        return new Matrix[]{MatrixFactory.emptyMatrix()};
    }
}
