/*
 * This prints the command line interface.
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 *
 */
package us.jonesrychtar.gispatialnet;

import us.jonesrychtar.gispatialnet.GISpatialNet;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.CannotProceedException;
import jxl.write.WriteException;
import org.boehn.kmlframework.kml.KmlException;
import org.geotools.feature.SchemaException;

/**
 *
 * @author Sam Tyler, Charles Bevan
 * @date September 17, 2009
 * @version 0.0.1
 */
public class cli extends userinterface {

    private static cli c;
    private static util u;
    private int statusLevel = 0;
    Scanner sc = new Scanner(System.in);
    private GISpatialNet gsn = new GISpatialNet();

    public static void main(String[] args) {
        c = new cli();
        u = new util();
        while (true) {
            c.Menu();
        }
    }

    public void cli() {
    	gsn.setDebugLevel(statusLevel);
    }

    //Menus----------------------------------------------------------------------------------
    public void Menu() {
        int option = getMenu(
                "Main Menu:",
                gsn.getStatus(),
                new String[]{"Load data", "Save Data", "Analyze Data", "Print Full Status", "Clear Data", "Exit"});

        switch (option) {
            case 1:
                LoadMenu1();
                break;
            case 2:
                SaveMenu();
                break;
            case 3:
                AnalyzeMenu();
                break;
            case 4:
                System.out.println(gsn.getStatus(3));
                break;
            case 5:
                gsn.ClearData();
                break;
            case 6:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input. Please re-enter.");
                break;
        }
    }

    private void LoadMenu1() {
        int option = getMenu(
                "Load Menu:",
                gsn.getStatus(statusLevel),
                new String[]{"Delimited text file (.csv,.txt)",
                    "DL/ucinet (.txt,.dat)", "Pajek (.net)", "Excel file (.xls)",
                    "Google Earth (.kml)", "Shape File (.shp)", "Back"});
        if (option <= 4 && option > 0) {
            _LoadMenu2(option);
        } else if (option == 5) {
        } else if (option == 6) {
            if (gsn.getData().hasX() || gsn.getData().hasY() || gsn.getData().hasAdj() || gsn.getData().hasAttb()) {
                if (_overwrite()) {
                    System.out.println("Enter the name for the node shapefile (with .shp extension):");
                    String n = sc.next();
                    System.out.println("Enter the name for the edge shapefile (with .shp extension:");
                    String e = sc.next();
                    try {
                        gsn.getData().loadShapefile(n, e);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        System.out.println("File not found.");
                        ex.getMessage();
                    }
                } else {
                    //Unsupported
                    System.out.println("Function unsupported");
                }
            } else {
                System.out.println("Enter the name for the node shapefile (with .shp extension):");
                String n = sc.next();
                System.out.println("Enter the name for the edge shapefile (with .shp extension:");
                String e = sc.next();
                try {
                	gsn.getData().loadShapefile(n, e);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    System.out.println("File not found.");
                    System.out.println(ex.getMessage());
                }
            }
        } else {
            Menu();
        }

    }
    // what: 1=txt/csv 2=dl/ucinet 3=pajek 4=excel

    private void _LoadMenu2(int what) {
        int option = getMenu(
                "Load File Menu:",
                gsn.getStatus(statusLevel),
                new String[]{"Node data (nodes with attributes)", "Graph/Network/Edge Data",
                    "Node Coordinate/Location Data", "Attribute Data", "Main Menu"});

        boolean merge = false;
        switch (option) {
            case 1: {
                if (gsn.getData().hasX() || gsn.getData().hasY() || gsn.getData().hasAttb()) {
                    merge = _overwrite();
                }
                break;
            }
            case 2: {
                if (gsn.getData().hasAdj()) {
                    merge = _overwrite();
                }
                break;
            }
            case 3: {
                if (gsn.getData().hasX() || gsn.getData().hasY()) {
                    merge = _overwrite();
                }
                break;
            }
            case 4: {
                if (gsn.getData().hasAttb()) {
                    merge = _overwrite();
                }
                break;
            }
            case 5: {
                Menu();
                break;
            }
        }
        System.out.println("What is the filename: ");
        String fn = sc.next();
        int format = _MatrixType();
        System.out.println("Enter the number of rows: ");
        int rows = sc.nextInt();
        System.out.println("Enter the number of columns: ");
        int cols = sc.nextInt();

        switch (what) {
            case 1: { //txt/csv
                System.out.print("What is the field seperator? ");
                char sp = sc.next().charAt(0);
                if (!merge) {
                    try {
                    	gsn.getData().loadTxt(fn, option, format, rows, cols, sp);
                    } catch (Exception ex) {
                        Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Merging not supported yet.");
                }
                break;
            }
            case 2: { //dl ucinet (adj matrix only!)
                if (!merge) {
                    try {
                    	gsn.getData().loadDL(fn, option, format, rows, cols);
                    } catch (Exception ex) {
                        Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Merging not supported yet.");
                }
                break;
            }
            case 3: { //pajek
                if (!merge) {
                    try {
                    	gsn.getData().loadPajek(fn, option, format, rows, cols);
                    } catch (Exception ex) {
                        Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Merging not supported yet.");
                }
                break;
            }
            case 4: { //excel
                if (!merge) {
                    try {
                    	gsn.getData().loadExcel(fn, option, format,rows, cols);
                    } catch (Exception ex) {
                        Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.out.println("Merging not supported yet.");
                }
                break;
            }
            case 5:
                LoadMenu1();
                break;
            default:
                System.out.println("Invalid input. Please re-enter.");
                break;
        }
    }

    private void SaveMenu() {
        int option = getMenu(
                "Save Data:",
                gsn.getStatus(statusLevel),
                new String[]{"Delimited text file (.csv,.txt)",
                    "DL/ucinet (.txt,.dat)", "Pajek (.net)", "Excel file (.xls)",
                    "Google Earth (.kml)", "Shape File (.shp)", "Back"});
        switch (option) {
            case 1: { //txt,csv
                System.out.println("Enter field seperator: ");
                char sp = sc.next().charAt(0);
                System.out.println("Enter the name of the Node File: ");
                String fnn = sc.next();
                System.out.println("Enter the name of the Edge File:");
                String efn = sc.next();
                try {
                	gsn.getData().saveCSV(fnn, efn, sp);
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
                	gsn.getData().saveDL(fnn, type);
                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
                break;
            }
            case 3: { //pajek
                System.out.println("Enter the name of the File: ");
                String fnn = sc.next();
                try {
                	gsn.getData().savePajek(fnn);
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
                	gsn.getData().saveExcel(fnn, efn);
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
                	gsn.getData().saveGoogleEarth(fnn);
                } catch (KmlException ex) {
                    Logger.getLogger(cli.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                break;
            }
            case 6: { //shapefile
                if (!(gsn.getData().hasX() && gsn.getData().hasX())) {
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
                            	gsn.getData().saveShapefileUnknown(fnn, efn, alg, ht, wd);
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
                            	gsn.getData().saveShapefile(fnn, efn);
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
                    	gsn.getData().saveShapefile(fnn, efn);
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
                Menu();
                break;
            default:
                System.out.println("Invalid input. Please re-enter.");
                break;
        }
    }

    private void AnalyzeMenu() {
        int option = getMenu(
                "Analyze Data:",
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
                        System.out.println("Enter the number of randomizations: ");
                        perm = sc.nextInt();
                        args = new String[]{"-s", fa, fb, Integer.toString(perm)};
                        break;
                    }
                    case 2: { //-s -e
                        System.out.println("Enter the name of the first file: ");
                        fa = sc.next();
                        System.out.println("Enter the name of the second file: ");
                        fb = sc.next();
                        System.out.println("Enter the number of randomizations: ");
                        perm = sc.nextInt();
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
                        System.out.println("Enter the number of randomizations: ");
                        perm = sc.nextInt();
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
                        System.out.println("Enter the number of randomizations: ");
                        perm = sc.nextInt();
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
                        System.out.println("Enter the number of randomizations: ");
                        perm = sc.nextInt();
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
                        System.out.println("Enter the number of randomizations: ");
                        perm = sc.nextInt();
                        args = new String[]{"-pre", fa, fb, fc, Integer.toString(perm)};
                        break;
                    }
                }

                try {
                    u.QAP(args);
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
                System.out.println("Function not supported yet.");
                break;
            }
            case 3: { //Borders
                System.out.println("Input file to analyze: ");
                String filename = sc.next();
                int op = getMenu("Select Algorithm",
                        "",
                        new String[]{"Original Borders Algorithm"});
                try {
                	gsn.getData().Border(filename, op);
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
                            "Less than median length", "More than median length", "Top 10%"
                        });
                try {
                	gsn.getData().Highlight(op - 1, filename, nfilename);
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
                switch (op) {
                    case 1: { //translate
                        System.out.println("Input amount to move on x direction: ");
                        double xm = sc.nextDouble();
                        System.out.println("input amount to move on y direction: ");
                        double ym = sc.nextDouble();
                        gsn.getData().translate(xm, ym);
                        break;
                    }
                    case 2: {
                        int axis = getMenu("Select Axis:", "", new String[]{"X axis", "Y axis"}) - 1;
                        gsn.getData().reflect(axis);
                        break;
                    }
                    case 3: {
                        System.out.println("Enter number of degrees: ");
                        double deg = sc.nextDouble();
                        gsn.getData().rotate(deg);
                        break;
                    }
                    case 4: {
                        System.out.println("Enter scale factor: ");
                        double fac = sc.nextDouble();
                        gsn.getData().scale(fac);
                        break;
                    }
                }
                break;
            }
            case 6:
                Menu();
                break;
            default:
                System.out.println("Invalid input. Please re-enter.");
                break;
        }
    }
    //End menus ---------------------------------------------------------------------------
    //helper menus

    private boolean _overwrite() {
        int t = getMenu(
                "Data already exists. Do you wish to overwrite or merge data?",
                "Merging not supported yet.",
                new String[]{"Overwirte", "Merge"});
        switch (t) {
            case 1:
                return true;
            case 2:
                return false;
            default:
                return true;
        }
    }

    private int _MatrixType() {
        return getMenu("What format is the file in?",
                "",
                new String[]{"Full Matrix", "Upper Matrix", "Lower Matrix"});
    }
    //end helper menus

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
            //print the menu.
            System.out.println("\n\n" + title + "\n");
            System.out.print(info + "\n");
            for (int i = 0; i < items.length; i++) {
                System.out.println("\t" + (i + 1) + ") " + items[i] + "\n");
            }
            System.out.println("Please enter your selection (1-" + items.length + "): ");

            //get user input
            option = sc.nextInt();

            //validate input
            if (option >= 1 && option <= items.length) {
                validInput = true;
            } else {
                System.out.println("Invalid Input.");
            }
        }

        return option;
    }
}
