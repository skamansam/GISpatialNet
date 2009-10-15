/*
 * This is the Google KML reader class
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp, org.boehn.kmlframework
 *
 */

package us.jonesrychtar.gispatialnet.Reader;

import org.boehn.kmlframework.kml.Document;
import org.boehn.kmlframework.kml.Kml;
import org.ujmp.core.Matrix;
/**
 *
 * @author cfbevan
 * @date October 5, 2009
 * @version 0.0.1
 */
public class KMLreader {
    
    private Kml kml;
    private Document doc;
    private String file;
    private Matrix xout, yout, adjout, attbout;

    public KMLreader(String Filename){
        file = Filename;
        kml=new Kml();
        doc= new Document();
        kml.setFeature(doc);
    }

    public Matrix[] read() throws Exception{
        try{
            kml.createKml(file);

            //TODO: find/make a reader for KML syntax
        }catch(Exception e){
            throw e;
        }
        return new Matrix[]{xout,yout,adjout,attbout};
    }
}
