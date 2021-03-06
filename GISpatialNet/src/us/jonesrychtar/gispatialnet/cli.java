/*
 * This prints the command line interface.
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp, org.boehn.kmlframework, geotools
 *
 */
package us.jonesrychtar.gispatialnet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.CannotProceedException;
import jxl.write.WriteException;
import org.boehn.kmlframework.kml.KmlException;
import org.geotools.feature.SchemaException;
import us.jonesrychtar.gispatialnet.Algorithm.Algorithm;
import us.jonesrychtar.gispatialnet.Algorithm.SimpleMerge;
import us.jonesrychtar.gispatialnet.Reader.Reader;
import us.jonesrychtar.gispatialnet.Writer.Writer;
import us.jonesrychtar.gispatialnet.Enums.*;

//import us.jonesrychtar.socialnetwork.SpatialGraph.SpatialGraphBase;

/**
 *
 * @author Sam Tyler, Charles Bevan
 * @version 0.0.1
 */
public class cli extends userinterface {

    private static cli c;
    private int statusLevel = 0;
    private Scanner sc = new Scanner(System.in);
    private GISpatialNet gsn = new GISpatialNet();

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        c = new cli();
        while (true) {
            c.mainMenu();
        }
    }

    /**
     *
     */
    public cli() {
        for(int i=0; i<gsn.NumberOfDataSets(); i++)
            gsn.setDebugLevel(i, statusLevel);
    }

    //Menus----------------------------------------------------------------------------------
    /**
     *
     */
    public void mainMenu() {
        
        if(gsn.NumberOfDataSets()==0){
            int option = getMenu(
                "Main Menu:",
                gsn.getStatus(statusLevel),
                new String[]{"Load data", "About GISpatialNet", "Exit"});
            switch(option){
                case 1: loadDataMenu(); break;
                case 2: printAbout(); break;
                case 3: System.exit(0);
                    break;
            }
            
        }
        else{
            int option = getMenu(
                "Main Menu:",
                gsn.getStatus(statusLevel),
                new String[]{"Load data", "Save Data", "Analyse Data","Merge Data", "Print Full Status", "Clear Data", "About GISpatialNet","Add Ego to Data", "Exit"});
            switch (option) {
                case 1:
                    loadDataMenu();
                    break;
                case 2:
                    SaveMenu();
                    break;
                case 3:
                    AnalyzeMenu();
                    break;
                case 4:
                    if(gsn.NumberOfDataSets() == 1)
                        System.out.println("Only one data set loaded.");
                    else
                        MergeMenu();
                    break;
                case 5:
                	System.out.println("Do you want to display as GUI? ");
                	if(sc.next().charAt(0) == 'y'){
                		for(DataSet m : gsn.getDataSets()){
                			m.toGUI();
                		}
                	}
                    System.out.println(gsn.getStatus(3));
                    
                    break;
                case 6:
                    gsn.ClearData(-1);
                    break;
                case 7:
                    printAbout();
                    break;
                case 8:
                    addEgo();
                    break;
                case 9:
                    System.exit(0);
                    break;
            }
        }
    }

   // Added to GISpatialNet.java
    private void printAbout(){
    	System.out.print(
    			"GISpatialNet is the brain child of Dr. Eric Jones and Jan Rychtar. " +
    			"It was programmed by " +
    			"Robert Gove, Charles Bevan, and Samuel Tyler. Collaborators include " +
    			"Martin Smith and Christopher Nicholson.\n\n" +
    			"You can find more information about GISPatialNet at its homepage, " +
    			"http://sourceforge.net/apps/trac/spatialnet/\n\n" +
    			"This software is governed under the GNU Greater Public Licence, Version " +
    			"2 (GPLv2). If you have not obtained the LGPL with this software, " +
    			"you can obtain it from http://www.gnu.org/licenses/gpl.html. GISpatialNet uses software governed under the GPL," +
    			"LGPL, Apache License, New BSD License");
    	
    }
    
    private void loadDataMenu() {
        int option = getMenu(
                "Load Menu:",
                gsn.getStatus(statusLevel),
                new String[]{"Delimited text file (.csv,.txt)", "Excel file (.xls)",
                    "DL/ucinet (.txt,.dat)", "Pajek (.net)", 
                    "Google Earth (.kml)", "Shape File (.shp)", "Back"});
        switch(option){
            case 1: _loadDBMenu(option); break;//csv
            case 2: _loadDBMenu(option); break; //excel
            case 3: { //dl
                System.out.println("What is the file name: ");
                String fn = sc.next();
                int format = _MatrixType();
                System.out.println("Enter the number of rows: ");
                int rows = _getPosInt(sc);
                System.out.println("Enter the number of columns: ");
                int cols = _getPosInt(sc);
                Vector<DataSet> vds;
                try {
                    vds = Reader.loadDL(fn, MatrixFormat.fromInt(--format), rows, cols);
                    for(int i=0; i<vds.size(); i++)
                        gsn.getDataSets().add(vds.elementAt(i));
                } catch (Exception ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }            
            case 4:{ //pajek
                System.out.println("What is the file name: ");
                String fn = sc.next();
                int format = _MatrixType();
                System.out.println("Enter the number of rows: ");
                int rows = _getPosInt(sc);
                System.out.println("Enter the number of columns: ");
                int cols = _getPosInt(sc);
                Vector<DataSet> vds;
                try {
                    vds = Reader.loadPajek(fn, MatrixFormat.fromInt(format),DataSetMatrixType.COORDINATE, rows, cols);
                    for(int i=0; i<vds.size(); i++)
                        gsn.getDataSets().add(vds.elementAt(i));
                } catch (Exception ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case 5:{ //google earth
                System.out.println("What is the file name: ");
                String fn = sc.next();
                
                DataSet ds;
            try {
                ds = Reader.loadGoogleEarth(fn);
                gsn.getDataSets().add(ds);
                } catch (Exception ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case 6:{ //shapefile
                    System.out.println("Enter the name for the node shapefile (with .shp extension):");
                    String n = sc.next();
                    System.out.println("Enter the name for the edge shapefile (with .shp extension:");
                    String e = sc.next();
                    try {
                        DataSet ds = Reader.loadShapefile(n, e);
                        gsn.getDataSets().add(ds);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        System.out.println("File not found.");
                        ex.getMessage();
                    }
                break;
            }
            case 7: break;
        }    
    }
    
    private void _loadDBMenu(int what) { 
        int option = getMenu(
                "Load File Menu:",
                gsn.getStatus(statusLevel),
                new String[]{"Node data (nodes with attributes)", "Adjacency data",
                    "Node data only", "Attribute data", "Main Menu"});
         int format =0, rows=0,cols=0;
         String fn="";
        if(option!=5){
        System.out.println("What is the file name: ");
        fn = sc.next();
        format = _MatrixType();
        System.out.println("Enter the number of rows: ");
        rows = _getPosInt(sc);
            System.out.println("Enter the number of columns: ");
            cols = _getPosInt(sc);
        }
        int dataType = -1;
        if (option == 1 || option == 3) {
            dataType = getMenu( //graph coordinates may be XY or polar
                    "Data Format:",
                    "What format is the data in?",
                    new String[]{"XY decimal", "Polar"});
        }
        //fix stuff
        format--;
        option--;
        if (option != 4) {
            switch (what) {
                case 1: { //txt/csv
                    System.out.print("What is the field separator? ");
                    char sp = sc.next().charAt(0);
                    try {
                        Vector<DataSet> vds = Reader.loadTxt(fn, DataSetMatrixType.fromInt(option), MatrixFormat.fromInt(format), rows, cols, sp);

                        for (int i = 0; i < vds.size(); i++) {
                            if (dataType == 2) {
                                vds.elementAt(i).PolarToXY();
                            }
                            gsn.getDataSets().add(vds.elementAt(i));
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case 2: { //excel
                    try {
                        Vector<DataSet> vds = Reader.loadExcel(fn, DataSetMatrixType.fromInt(option), MatrixFormat.fromInt(format), rows, cols);
                        for (int i = 0; i < vds.size(); i++) {
                            if (dataType == 2) {
                                vds.elementAt(i).PolarToXY();
                            }
                            gsn.getDataSets().add(vds.elementAt(i));
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                case 3:
                    loadDataMenu();
                    break;
            }
        }
    }

/*    private void getCSVParams(){
    	
    }
*/    
    private void SaveMenu() {
        int matrix=0;
        int option = getMenu(
                "Save Data:",
                gsn.getStatus(statusLevel),
                new String[]{"Delimited text file (.csv,.txt)",
                    "DL/ucinet (.txt,.dat)", "Pajek (.net)", "Excel file (.xls)",
                    "Google Earth (.kml)", "Shape File (.shp)", "Back"});
        if(option !=7)
            matrix = this._MatrixChoice();
        switch (option) {
            case 1: { //txt,csv
                System.out.println("Enter field separator: ");
                char sp = sc.next().charAt(0);
                System.out.println("Enter the name of the Node File: ");
                String fnn = sc.next();
                System.out.println("Enter the name of the Edge File:");
                String efn = sc.next();
                try {
                	Writer.saveCSV(fnn, efn, sp, gsn.getData(matrix));
                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
                break;
            }
            case 2: { //dl ucinet
                int type = getMenu("Select extension type: ",
                        "",
                        new String[]{".txt", ".dat"});
                System.out.println("Enter the name of the File: ");
                String fnn = sc.next();
                try {
                	Writer.saveDL(fnn, type, gsn.getData(matrix));
                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
                break;
            }
            case 3: { //pajek
                System.out.println("Enter the name of the File: ");
                String fnn = sc.next();
                try {
                	Writer.savePajek(fnn, gsn.getData(matrix));
                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
                break;
            }
            case 4: { //excel
                System.out.println("Enter the name of the Node File: ");
                String fnn = sc.next();
                System.out.println("Enter the name of the Edge File:");
                String efn = sc.next();
                try {
                	Writer.saveExcel(fnn, efn, gsn.getData(matrix));
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                } catch (WriteException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case 5: { //google earth
                System.out.println("Enter the name of the File: ");
                String fnn = sc.next();
                try {
                	Writer.saveGoogleEarth(fnn,gsn.getData(matrix));
                } catch (KmlException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                break;
            }
            case 6: { //shapefile
                if (!(gsn.getData(matrix).hasX() && gsn.getData(matrix).hasY())) {
                    int op = getMenu("Node data not found: ",
                            "What do you want to do?",
                            new String[]{"Create XY data", "Write only Edge file"});
                    System.out.println("Enter the name of the Node File: ");
                    String fnn = sc.next();
                    System.out.println("Enter the name of the Edge File:");
                    String efn = sc.next();
                    switch (op) {
                        case 1: {
                            int alg = getMenu("Choose an algorithm:",
                                    "",
                                    new String[]{"GeoNet Algorithm"});
                            System.out.println("Enter max height of network (usually 10xNumber of rows): ");
                            int ht = sc.nextInt();
                            System.out.println("Enter max width of network (usually 10xNumber of rows): ");
                            int wd = sc.nextInt();
                            try {
                            	Writer.saveShapefileUnknown(fnn, efn, alg, ht, wd, gsn.getData(matrix));
                            } catch (IllegalArgumentException ex) {
                                System.out.println(ex.getMessage());
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                System.out.println(ex.getMessage());
                            } catch (SchemaException ex) {
                                Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        case 2: {
                            try {
                            	Writer.saveShapefile(fnn, efn, gsn.getData(matrix));
                            } catch (IllegalArgumentException ex) {
                                System.out.println(ex.getMessage());
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                System.out.println(ex.getMessage());
                            } catch (SchemaException ex) {
                                Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                } else {
                    System.out.println("Enter the name of the Node File: ");
                    String fnn = sc.next();
                    System.out.println("Enter the name of the Edge File:");
                    String efn = sc.next();
                    try {
                    	Writer.saveShapefile(fnn, efn, gsn.getData(matrix));
                    } catch (IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    } catch (SchemaException ex) {
                        Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            }
            case 7:
                //Menu();
                break;
        }
    }

    private void AnalyzeMenu() {
        int option = getMenu(
                "Analyse Data:",
                gsn.getStatus(statusLevel),
                new String[]{"QAP", "Sample Network Bias", "Borders",
                    "Highlight Edges", "Matrix Conversion", "Back"});
        switch (option) {
            case 1: { //QAP
                int o = getMenu("Choose Options: ",
                        "",
                        new String[]{"Simple Mantel Test", "SMT with exact permutation",
                            "Partial Mantel Test", "PMT with exact permutation", "PMT with raw values",
                            "PMT with exact permutation and raw values"});
                String[] args = new String[4];
                String fa, fb, fc;
                int perm = 0;

                switch (o) {
                    case 1: { //-s
                        System.out.println("Enter the name of the first file: ");
                        fa = sc.next();
                        System.out.println("Enter the name of the second file: ");
                        fb = sc.next();
                        System.out.println("Enter the number of randomisations: ");
                        perm = _getPosInt(sc);
                        args = new String[]{"-s", fa, fb, Integer.toString(perm)};
                        break;
                    }
                    case 2: { //-s -e
                        System.out.println("Enter the name of the first file: ");
                        fa = sc.next();
                        System.out.println("Enter the name of the second file: ");
                        fb = sc.next();
                        System.out.println("Enter the number of randomisations: ");
                        perm = _getPosInt(sc);
                        args = new String[]{"-se", fa, fb, Integer.toString(perm)};
                        break;
                    }
                    case 3: { //-p
                        System.out.println("Enter the name of the first file: ");
                        fa = sc.next();
                        System.out.println("Enter the name of the second file: ");
                        fb = sc.next();
                        System.out.println("Enter the name of the third file: ");
                        fc = sc.next();
                        System.out.println("Enter the number of randomisations: ");
                        perm = _getPosInt(sc);
                        args = new String[]{"-p", fa, fb, fc, Integer.toString(perm)};
                        break;
                    }
                    case 4: { //-p -e
                        System.out.println("Enter the name of the first file: ");
                        fa = sc.next();
                        System.out.println("Enter the name of the second file: ");
                        fb = sc.next();
                        System.out.println("Enter the name of the third file: ");
                        fc = sc.next();
                        System.out.println("Enter the number of randomisations: ");
                        perm = _getPosInt(sc);
                        args = new String[]{"-pe", fa, fb, fc, Integer.toString(perm)};
                        break;
                    }
                    case 5: { //-p -r
                        System.out.println("Enter the name of the first file: ");
                        fa = sc.next();
                        System.out.println("Enter the name of the second file: ");
                        fb = sc.next();
                        System.out.println("Enter the name of the third file: ");
                        fc = sc.next();
                        System.out.println("Enter the number of randomisations: ");
                        perm = _getPosInt(sc);
                        args = new String[]{"-pr", fa, fb, fc, Integer.toString(perm)};
                        break;
                    }
                    case 6: { //-pre
                        System.out.println("Enter the name of the first file: ");
                        fa = sc.next();
                        System.out.println("Enter the name of the second file: ");
                        fb = sc.next();
                        System.out.println("Enter the name of the third file: ");
                        fc = sc.next();
                        System.out.println("Enter the number of randomisations: ");
                        perm = _getPosInt(sc);
                        args = new String[]{"-pre", fa, fb, fc, Integer.toString(perm)};
                        break;
                    }
                }

                try {
                    Algorithm.QAP(args);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Error ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CannotProceedException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case 2: { //SNB
                int dataset_idx = this._MatrixChoice();
                //System.out.println("Function not supported yet.");
                System.out.println("Enter a bias: ");
                double bias=sc.nextDouble();
                Algorithm.SNB(gsn.getData(dataset_idx), bias);
                break;
            }
            case 3: { //Borders
                System.out.println("Output file name: ");
                String filename = sc.next();
                int op = getMenu("Select Algorithm",
                        "",
                        new String[]{"Original Borders Algorithm"});
                int matrix = this._MatrixChoice();
                try {
                	Algorithm.Border(filename, op, gsn.getData(matrix).getX(), gsn.getData(matrix).getY(), gsn.getData(matrix).getAdj());
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SchemaException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case 4: { //Highlight edges
                System.out.println("Output Node File name: ");
                String nfilename = sc.next();
                System.out.println("Output Edge Files prefix: ");
                String filename = sc.next();
                int op = getMenu("Select Highlighting Algorithm",
                        "",
                        new String[]{"Less than average length",
                            "Less than median length", "More than median length", "Top 10%", "By value"
                        });
                int matrix = this._MatrixChoice();
                try {
                	Algorithm.Highlight(op - 1, filename, nfilename, gsn.getData(matrix).getX(), gsn.getData(matrix).getY(), gsn.getData(matrix).getAdj());
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SchemaException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case 5: { //Conversions
                int op = getMenu("Select Conversion",
                        "",
                        new String[]{"Translate", "Reflect", "Rotate", "Scale"});
                int matrix = this._MatrixChoice();
                switch (op) {
                    case 1: { //translate
                        System.out.println("Input amount to move on x direction: ");
                        double xm = _getPosDouble(sc);
                        System.out.println("input amount to move on y direction: ");
                        double ym = _getPosDouble(sc);
                        gsn.getData(matrix).translate(xm, ym);
                        break;
                    }
                    case 2: {
                        int axis = getMenu("Select Axis:", "", new String[]{"X axis", "Y axis"}) - 1;
                        gsn.getData(matrix).reflect(axis);
                        break;
                    }
                    case 3: {
                        System.out.println("Enter number of degrees: ");
                        double deg = _getPosDouble(sc);
                        gsn.getData(matrix).rotate(deg);
                        break;
                    }
                    case 4: {
                        System.out.println("Enter scale factor: ");
                        double fac = _getPosDouble(sc);
                        gsn.getData(matrix).scale(fac);
                        break;
                    }
                }
                break;
            }
            case 6:
                //Menu();
                break;
        }
    }
    private void MergeMenu(){
        int option = getMenu(
                "Merge Menu:",
                "",
                new String[] {"Merge two data sets","Merge all data sets", "Back"});
        switch(option){
            case 1: {
                int ds1 = _MatrixChoice();
                int ds2 = _MatrixChoice();
                SimpleMerge sdm = new SimpleMerge(gsn.getData(ds1), gsn.getData(ds2));
                DataSet temp = sdm.Merge();
                gsn.setData(ds1, temp);
                gsn.Remove(ds2);
                break;
            }
            case 2:{
                //DataSet temp;
                while(gsn.NumberOfDataSets() > 1){
                    SimpleMerge sdm = new SimpleMerge(gsn.getData(0), gsn.getData(1));
                    DataSet temp2 = sdm.Merge();
                    gsn.setData(0, temp2);
                    gsn.Remove(1);
                }
                break;
            }
            case 3: break;
        }
    }
    private void addEgo(){
        int option = getMenu(
                "Add Ego to:",
                "",
                new String[] {"Single Data Set","All Data","Back"});
        switch(option){
            case 1:{
                int m = _MatrixChoice();
                gsn.AddEgo(--m);
                break;
            }
            case 2:{
                for(int i=0; i<gsn.NumberOfDataSets(); i++){
                    gsn.AddEgo(i);
                }
                break;
            }
            case 3: break;
        }
    }
    //End menus ---------------------------------------------------------------------------
    //helper menus

    private int _MatrixChoice(){
        int out;
        System.out.println("Enter the number of the Data Set to use["+1+"-"+gsn.NumberOfDataSets()+"]: ");
        if(sc.hasNextInt())
            out = sc.nextInt();
        else{
            out=-1;
            sc.next();
        }
        while(out<1 || out>gsn.NumberOfDataSets()){
            System.out.println("Invalid input. Enter the number of the Data Set to use["+1+"-"+gsn.NumberOfDataSets()+"]: ");
            if(sc.hasNextInt())
                out = sc.nextInt();
            else{
                out=-1;
                sc.next();
            }
        }
        out --;
        return out;
    }

    private int _MatrixType() {
        return getMenu("What format is the file in?",
                "",
                new String[]{"Full Matrix", "Upper Matrix", "Lower Matrix"});
    }
    //end helper menus

    /**
     * Gets int from command line (error checking included)
     * @param sc Scanner used for command line
     * @return Int from command line
     */
    private static int _getPosInt(Scanner sc){
        int out=-1;
        if(sc.hasNextInt())
            out = sc.nextInt();
        else
            sc.next();//clear junk
        while(out<1){
            System.out.println("Invalid input.");
            if(sc.hasNextInt())
                out = sc.nextInt();
            else
                sc.next();//clear junk
            }
        return out;
    }

    /**
     * Gets double from command line (error checking included)
     * @param sc Scanner used in command line
     * @return Double from command line
     */
    private static double _getPosDouble(Scanner sc){
        double out=-1;
        if(sc.hasNextDouble())
            out = sc.nextDouble();
        else
            sc.next();//clear junk
        while(out<1){
            System.out.println("Invalid input.");
            if(sc.hasNextDouble())
                out = sc.nextDouble();
            else
                sc.next();//clear junk
            }
        return out;
    }
    /**
     * Creates a formatted menu and validates user input for menu choice
     * @param title Menu title to be printed at top of menu
     * @param info Extra information to be printed below menu title
     * @param items  String array of menu choices
     * @return int representing menu item chosen
     */
    public int getMenu(String title, String info, String[] items) {
        //return and err out if length of items is less than one.
        if (items.length < 1) {
            System.err.println("Incorrect length for menu items.");
            return 0;
        }

        int option = 0;                           //the option input value
        boolean validInput = false;               //whether the input is valid

        //do menu while the input is not valid
        while (!validInput) {
            validInput = true;
            //print the menu.
            System.out.println("\n\n" + title + "\n");
            System.out.print(info + "\n");
            for (int i = 0; i < items.length; i++) {
                System.out.println("\t" + (i + 1) + ") " + items[i] + "\n");
            }
            System.out.println("Please enter your selection (1-" + items.length + "): ");

            //get user input
            if(sc.hasNextInt()){
                option = sc.nextInt();
            }else{
                System.out.println("Input is not a number. ");
                option = -1;
                //flush first bad input
               sc.next();
            }

            //validate input
            if (option >= 1 && option <= items.length) {
                //do nothing
            } else {
                System.out.println("Invalid Input.");
                validInput=false;
            }
        }

        return option;
    }
}
