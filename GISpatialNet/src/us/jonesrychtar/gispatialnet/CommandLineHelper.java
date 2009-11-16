/*
 * helps execute from command line
 * trying to keep GISpatialNet.jar smaller
 *
 * 
 */
package us.jonesrychtar.gispatialnet;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.CannotProceedException;
import us.jonesrychtar.gispatialnet.Algorithm.Algorithm;
import us.jonesrychtar.gispatialnet.Reader.Reader;
import us.jonesrychtar.gispatialnet.Writer.DLwriter;
import us.jonesrychtar.gispatialnet.Writer.Writer;

/**
 *
 * @author cfbevan
 */
public class CommandLineHelper {

    public static void qap() { //copied from cli
        Scanner sc = new Scanner(System.in);
        int o = new cli().getMenu("Choose Options: ",
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
    }

    public static void exec(int act, int i, int o, String id, String od) {
        Scanner sc = new Scanner(System.in);
        GISpatialNet gsn = new GISpatialNet();
        int row=0, col=0, Matrix_Format=0;
        char sep = ',';
        //get input
        //get extra data if needed
        switch (i) {
            case 'C': //need row,col,seperator, format
                System.out.println("Enter seperator character: ");
                sep = (char) sc.nextInt();
            case 'D': //need row,col, format

            case 'E': //need row,col, format
                Matrix_Format = new cli().getMenu("What format is the file in?",
                    "",
                    new String[]{"Full Matrix", "Upper Matrix", "Lower Matrix"});

            case 'P': //need row,col?
                System.out.println("Enter number of rows per Data Set: ");
                row = sc.nextInt();
                System.out.println("Enter the number of columns per Data Set: ");
                col = sc.nextInt();
                break;
            case 'K': //no data needed
                break;
            case 'S': //no data needed
                break;
        }
        String[] inFiles = new File(id).list();
        for (int j = 0; j < inFiles.length; j++) {
            int Matrix=0;
            //get matrix type from filename
            String typeCode = inFiles[j].substring(inFiles[j].length()-6, inFiles[j].length());
            if(typeCode.equals("xya")){
                Matrix = 0;
            } else if(typeCode.equals("adj")){
                Matrix = 3;
            } else{
                //eror
                System.out.println("file: "+inFiles[j]+ "does not have a proper extension (_xya or _adj)");
                System.exit(1);
            }

            try{
                switch (i) { //read file to appropriate data set
                    case 'C':
                        Reader.loadTxt(inFiles[j], Matrix, Matrix_Format, row, col, sep);
                        break;
                    case 'D':
                        Reader.loadDL(inFiles[j], Matrix_Format, row, col);
                        break;
                    case 'E':
                        Reader.loadExcel(inFiles[j], Matrix, Matrix_Format, row, col);
                        break;
                    case 'K':
                        Reader.loadGoogleEarth(inFiles[j]);
                    case 'P':
                        Reader.loadPajek(inFiles[j], Matrix, row, col);
                    case 'S':
                        //Reader.loadShapefile(inFiles[j], inFiles[j]);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        //Merge datasets according to filename
        mergeByFilename();

        if (act != -1) { //perform action and save to od
            double bias = 0.0;
            int alg = 0;
            //get extra data if needed
            switch (act) {
                case 's': //need bias
                    System.out.println("What bias do you wish to use? ");
                    bias = sc.nextDouble();
                    break;
                case 'e': //need alg to use
                    alg = new cli().getMenu("Select Highlighting Algorithm",
                        "",
                        new String[]{"Less than average length",
                            "Less than median length", "More than median length", "Top 10%", "By value"
                        });
                    alg--;
                    break;
            }
            try {
                for (int k = 0; k < gsn.NumberOfDataSets(); k++) {
                    DataSet ds = gsn.getDataSets().elementAt(k);
                    //snb
                    if (act == 's') {
                        Algorithm.SNB(ds.getX(), ds.getY(), ds.getAdj(), bias);
                    } //borders
                    else if (act == 'b') {
                        
                    } //highlight edges
                    else if (act == 'e') {
                        String outFnN = od + "/" + (gsn.getDataSets().elementAt(k).GetLoadedFiles().elementAt(0)) + "_Node";
                        String outFnE = od + "/" + (gsn.getDataSets().elementAt(k).GetLoadedFiles().elementAt(0)) + "_Edge";
                        Algorithm.Highlight(alg, outFnE, outFnN, ds.getX(), ds.getY(), ds.getAdj());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else { //conversion only
            //output all datasets to od in o format
            //get extra information if needed
            switch (o) {
                case 'C': //need seperator
                    System.out.println("Enter seperator character: ");
                    sep = (char) sc.nextInt();
                    break;
                default:
                    break;
            }

            //save all datasets
            for (int k = 0; k < gsn.NumberOfDataSets(); k++) {
                //TODO: fix this to work on other systems
                //get filenames for output
                String outFn = od + "/" + (gsn.getDataSets().elementAt(k).GetLoadedFiles().elementAt(0));
                String outFnN = od + "/" + (gsn.getDataSets().elementAt(k).GetLoadedFiles().elementAt(0)) + "_Node";
                String outFnE = od + "/" + (gsn.getDataSets().elementAt(k).GetLoadedFiles().elementAt(0)) + "_Edge";

                //get dataset
                DataSet ds = gsn.getDataSets().elementAt(k);
                try {
                    switch (o) { //write data to file
                        case 'C':
                            outFnN += ".csv";
                            outFnE += ".csv";
                            Writer.saveCSV(outFnN, outFnE, sep, ds);
                            break;
                        case 'D':
                            outFn += ".dat";
                            Writer.saveDL(outFn, DLwriter.DAT, ds);
                            break;
                        case 'E':
                            outFnN += ".xls";
                            outFnE += ".xls";
                            Writer.saveExcel(outFnN, outFnE, ds);
                            break;
                        case 'K':
                            outFn += ".kml";
                            Writer.saveGoogleEarth(outFn, ds);
                            break;
                        case 'P':
                            outFn += ".net";
                            Writer.savePajek(outFn, ds);
                            break;
                        case 'S':
                            Writer.saveShapefile(outFnE, outFnE, ds);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }
    public static void mergeByFilename(){
        //TODO: This should merge all datasets that have the same file name assigned to them
    }
}
