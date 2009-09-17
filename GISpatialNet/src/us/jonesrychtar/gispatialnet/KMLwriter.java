/*
 * This is a kml writer class, note that the input matrix
 * has a required format.
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: UJMP and org.boehn.kmlframework
 *
 */
package us.jonesrychtar.gispatialnet;

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
    String file;
    Matrix workingset;

    //note Matrix must be in form {longitude, latitude, Name, description}
    public KMLwriter(Matrix map, String filename){
        workingset = map;
        file = filename;
        kml=new Kml();
        doc= new Document();
        kml.setFeature(doc);
    }

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
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
