/*
 * This class is used to transform a matrix of x,y coordinates
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 */

package us.jonesrychtar.gispatialnet;

import org.ujmp.core.Matrix;
import java.lang.Math.*;

/**
 *
 * @author cfbevan
 */

public class MatrixConversion {
    /**
     * Signifies xaxis when using reflect
     */
    public static final int XAXIS = 0;
    /**
     * Signifies yaxis when using reflect
     */
    public static final int YAXIS = 1;

    /**
     * 
     * @param in Matrix containig x,y coordinates
     * @param moveX How far to move on x direction
     * @param moveY How fart to move on y direction
     * @return Matrix containing translated x,y coordinates
     */
    public Matrix Translation(Matrix in, double moveX, double moveY){
        for(int row=0; row < in.getRowCount(); row++){
            in.setAsDouble(in.getAsDouble(row,0)+moveX, row, 0);
            in.setAsDouble(in.getAsDouble(row,1)+moveY, row, 1);
        }
        return in;
    }

    /**
     * 
     * @param in Matrix containig x,y coordinates
     * @param Axis which axis to rotate over
     * @return Modified Matrix containing x,y coordinates
     */
    public Matrix Reflection(Matrix in, int Axis){
        for(int row=0; row < in.getRowCount(); row++){
            if(Axis == YAXIS)
                in.setAsDouble(in.getAsDouble(row,0)*(-1), row, 0);
            if(Axis == XAXIS)
                in.setAsDouble(in.getAsDouble(row,1)*(-1), row, 1);
        }
        return in;
    }

    /**
     *
     * @param in Matrix containing x,y coordinates
     * @param Degrees How far to rotate clockwise
     * @return Modified matrix containing x,y coordinates
     */
    public Matrix RotateClockwise(Matrix in, double Degrees){
        for(int row=0; row < in.getRowCount(); row++){
            double x = in.getAsDouble(row,0);
            double y = in.getAsDouble(row,1);
            double xPrime = (x*Math.cos(Degrees))-(y*Math.sin(Degrees));
            double yPrime = (x*Math.sin(Degrees))+(y*Math.cos(Degrees));
            in.setAsDouble(xPrime,row, 0);
            in.setAsDouble(yPrime,row, 1);
        }
        return in;
    }

    /**
     *
     * @param in Matrix containing x,y coordinates
     * @param Degrees How far to rotate counterclockwise
     * @return Modified matrix containing x,y coordinates
     */
    public Matrix RotateCounterclockwise(Matrix in, double Degrees){
        for(int row=0; row < in.getRowCount(); row++){
            double x = in.getAsDouble(row,0);
            double y = in.getAsDouble(row,1);
            double xPrime = (x*Math.cos(Degrees))+(y*Math.sin(Degrees));
            double yPrime = (y*Math.cos(Degrees))-(x*Math.sin(Degrees));
            in.setAsDouble(xPrime,row, 0);
            in.setAsDouble(yPrime,row, 1);
        }
        return in;
    }

    /**
     *
     * @param in Matrix containing x,y coordinates
     * @param Factor Scaling factor to multiply matrix by
     * @return Modified matrix containing x,y coordinates
     */
    public Matrix Scale(Matrix in, double Factor){
        for(int row=0; row < in.getRowCount(); row++){
            in.setAsDouble(in.getAsDouble(row,0)*Factor, row, 0);
            in.setAsDouble(in.getAsDouble(row,1)*Factor, row, 1);
        }
        return in;
    }

}
