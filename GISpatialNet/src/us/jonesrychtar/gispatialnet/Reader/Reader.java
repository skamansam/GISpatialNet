/*
 * This class holds functions that access the other classes in the reader package
 * 
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 */

package us.jonesrychtar.gispatialnet.Reader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;
import org.ujmp.core.Matrix;
import us.jonesrychtar.gispatialnet.DataSet;
import us.jonesrychtar.gispatialnet.Enums;
import us.jonesrychtar.gispatialnet.util;
import us.jonesrychtar.gispatialnet.Enums.*;

/**
 *
 * @author cfbevan
 */
public class Reader {
    /**
     * loads file with default options
	 * @param filename
	 */
	public static void LoadFile(String filename) {
		String ext = filename.substring(filename.length()-3);
        if(ext.equals("csv")){
            //csv reader
        } else if(ext.equals("dat")){
            //dl/ucinet reader
        } else if(ext.equals("xls")){
            //excel reader
        } else if(ext.equals("kml")){
            //google earth reader
        } else if(ext.equals("net")){
            //pajek reader
        } else if(ext.equals("shp")){
            //shapefile reader
        } else if(ext.equals("txt")){
            //ambiguious can be csv or dl/ucinet
        }
	}

    /**
     * Loads data from a shapefile into memory
     * @param filenameN name of Node shapefile
     * @param filenameE name of edge shapefile
     * @return DataSet with data from shapefiles
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     */
    public static DataSet loadShapefile(String filenameN, String filenameE) throws MalformedURLException, IOException  {
        ShapeFileReader sfr = new ShapeFileReader(filenameN, filenameE);
        Matrix temp[] = sfr.Read();
        DataSet ds = new DataSet(temp[0],temp[1],temp[2],temp[3]);
        ds.addFile(filenameE);
        ds.addFile(filenameN);
        return ds;
    }
    
    /**
     * Loads data from a google earth file into memory
     * @param filename name of file to load
     * @return DataSet with data from file
     * @throws java.lang.Exception
     */
    public static DataSet loadGoogleEarth(String filename) throws Exception {
        KMLreader kmlr = new KMLreader(filename);
        Matrix temp[] = kmlr.read();
        DataSet ds = new DataSet(temp[0],temp[1],temp[2],temp[3]);
        ds.addFile(filename);
        return ds;
    }

    /**
     * Loads Data from a Pajek file into memory
     * @param filename Name of file to load
     * @param MatrixType Format of saved data
     * @param rows Number of rows in File
     * @param cols Number of cols in file
     * @return vector of datasets with data from file
     * @throws java.lang.Exception
     */
    public static Vector<DataSet> loadPajek(String filename, int MatrixType, int rows, int cols) throws Exception{
        PajekReader pr = new PajekReader(filename);
        Vector<DataSet> ds = pr.Read(MatrixInputType.fromInt(MatrixType), rows, cols);
        for(int i=0; i<ds.size(); i++)
            ds.elementAt(i).addFile(filename);
        return ds;
    }

    /**
     * Loads data from a DL/UCINET file into memory
     * @param filename name of file to load
     * @param MatrixType Format of data (may be overwritten if defined in file)
     * @param rows number of rows (may be overwritten if defined in file)
     * @param col number of cols (may be overwritten if defined  in file)
     * @return vector of datasets with data from file
     * @throws java.lang.Exception
     */
    public static Vector<DataSet> loadDL(String filename, int MatrixType, int rows, int col) throws Exception{
        DLreader dlr = new DLreader(filename);
        Vector<DataSet> ds = dlr.Read(MatrixType, rows, col);

        for(int i=0; i<ds.size(); i++)
            ds.elementAt(i).addFile(filename);
        return ds;
    }

    /*****************************************
     * Handled in Reader.java:
     * Matrix to be loaded:
     * 0 XYAttb
     * 1 Adj
     * 2 XY
     * 3 Attb
     *
     * Handled in other class:
     * MatrixType:
     * 0 Full Matrix
     * 1 Lower Matrix
     * 2 Upper Matrix
     * ***************************************
     * */

    /**
     * Load data from Excel file into memory
     * @param filename File to load
     * @param Matrix Which matrix to load into
     * @param MatrixType Format of data in file
     * @param row number of rows per set of data in file
     * @param col number of columns per set of data in file
     * @return vector of datasets containing data from file
     * @throws java.lang.Exception
     */
    public static Vector<DataSet> loadExcel(String filename, MatrixType type, MatrixInputType itype, int row, int col) throws Exception{
        ExcelReader er = new ExcelReader(filename);
        Vector<Matrix> out = er.read(itype, row, col);
        Vector<DataSet> ds = new Vector<DataSet>();
        for(int m=0; m<out.size(); m++){
            DataSet dstemp = new DataSet();
            Matrix temp = out.elementAt(m);
            switch (type) {
                case COORD_ATT: { //xy attb
                    Matrix[] t2 = util.SplitXYAttb(temp);
                    dstemp.setX(t2[0]);
                    dstemp.setY(t2[1]);
                    dstemp.setAttributeMatrix(t2[2]);
                    break;
                }
                case ADJACENCY: { //adj
                    dstemp.setAdj(temp);
                    break;
                }
                case COORDINATE: { //xy
                    dstemp.setXY(temp);
                    break;
                }
                case ATTRIBUTE: { //attb
                    dstemp.setAttb(temp);
                    break;
                }
            }
            ds.add(dstemp);
        }
        for(int i=0; i<ds.size(); i++)
            ds.elementAt(i).addFile(filename);
        return ds;
    }
    
    /**
     * Loads data from a txt/csv file into memory
     * @param filename name of file to load
     * @param Matrix Matrix to load into
     * @param MatrixType Format of file
     * @param rows number of rows in file
     * @param col number of cols in file
     * @param sep Field Seperator character
     * @return vector of datasets containing data from file
     * @throws java.lang.Exception
     */
    public static Vector<DataSet> loadTxt(String filename, int Matrix, int MatrixType, int rows, int col, char sep) throws Exception{
        CSVFileReader csvr = new CSVFileReader(filename);
        //cswvr.setSep(sep);
        Vector<DataSet> DSOut = csvr.Read(Enums.MatrixInputType.fromInt(MatrixType),rows, col);
        return DSOut;
    }
/*
        Vector<Matrix> out = csvr.ReadAsMatrices(MatrixType, rows, col);
        Vector<DataSet> ds = new Vector<DataSet>();
        for(int m=0; m<out.size(); m++){
        	//3System.out.println(out.get(m));
            DataSet dstemp = new DataSet();
            Matrix temp = out.elementAt(m);
            switch (Matrix) {
                case 0: { //xy attb
                    Matrix[] t2 = util.SplitXYAttb(temp);
                    dstemp.setX(t2[0]);
                    dstemp.setY(t2[1]);
                    dstemp.setAttributeMatrix(t2[2]);
                    break;
                }
                case 1: { //adj
                    dstemp.setAdj(temp);
                    break;
                }
                case 2: { //xy
                    dstemp.setXY(temp);
                    break;
                }
                case 3: { //attb
                    dstemp.setAttb(temp);
                    break;
                }
            }
            ds.add(dstemp);
        }
        for(int i=0; i<ds.size(); i++)
            ds.elementAt(i).addFile(filename);
        return ds;
    }
*/
}
