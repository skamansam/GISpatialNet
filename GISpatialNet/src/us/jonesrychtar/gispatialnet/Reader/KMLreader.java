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
import org.boehn.kmlframework.kml.Placemark;

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

    public Matrix[] read(){
        try{
            kml.createKml(file);

        }catch(Exception e){
            e.printStackTrace();
        }
        return new Matrix[]{xout,yout,adjout,attbout};
    }
}
