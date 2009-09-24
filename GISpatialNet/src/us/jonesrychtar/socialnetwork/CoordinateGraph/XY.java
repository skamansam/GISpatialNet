
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package us.jonesrychtar.socialnetwork.CoordinateGraph;

/**
 *
 * @author Samuel C. Tyler <skamansam@gmail.com>
 */
public class XY extends CoordinateGraphBase {
    private double xCoord, yCoord;

    // TODO: Here is Sam
    public void readValue(java.util.Scanner sc) {
        xCoord = sc.getDouble();
        yCoord = sc.getDouble();

        if ((xCoord == 9999) || (yCoord == 9999)) {
            missingNodes.add(i);
        } else {
            x.setAsDouble(xCoord, i, 0);
            y.setAsDouble(yCoord, i, 0);
            totalXCoord += xCoord;
            totalYCoord += yCoord;
        }
    }

    public boolean isValidNodeValue() {
        if ((xCoord == 9999) || (yCoord == 9999)) {
            return true;
        }

        return false;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
