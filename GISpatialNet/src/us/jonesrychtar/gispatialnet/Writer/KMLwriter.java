/*
 * This is a kml writer class, note that the input matrix
 * has a required format.
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP and org.boehn.kmlframework
 *
 */
package us.jonesrychtar.gispatialnet.Writer;

import org.boehn.kmlframework.kml.Document;
import org.boehn.kmlframework.kml.Kml;
import org.boehn.kmlframework.kml.Placemark;
import org.ujmp.core.Matrix;

/*
 *
 * @author Charles Bevan
 * @date September 17, 2009
 * @version 0.0.1
 */
public class KMLwriter {

    private Kml kml;
    private Document doc;
    private String file;
    private Matrix workingset;

    /**
     * Constructor
     * @param map Matrix of the form {longitude, latitude, Name, description}
     * @param filename Name of output file
     */
    public KMLwriter(Matrix map, String filename){
        workingset = map;
        file = filename;
        kml=new Kml();
        doc= new Document();
        kml.setFeature(doc);
    }

    /**
     * Write data to file
     */
    public void WriteFile() {
        try{
            //point data
            String name = "";
            String Description= "";
            float lat = (float) 0.00;
            float log = (float) 0.00;

            //for each point in matrix
            for(int i=0;i<workingset.getRowCount();i++){
                //get row from matrix
                log=workingset.getAsFloat(i,0);
                lat=workingset.getAsFloat(i,1);
                name=workingset.getAsString(i,2);
                Description=workingset.getAsString(i,3);

                //set data
                Placemark point = new Placemark(name);
                point.setDescription(Description);
                point.setLocation(log, lat);

                //add point to file
                doc.addFeature(point);
            }
            //write file
            kml.createKml(file);
            kml.write(kml);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
