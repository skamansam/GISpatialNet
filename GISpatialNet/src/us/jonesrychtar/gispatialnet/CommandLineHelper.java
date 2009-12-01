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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.CannotProceedException;
import us.jonesrychtar.gispatialnet.Algorithm.Algorithm;
import us.jonesrychtar.gispatialnet.Algorithm.SimpleMerge;
import us.jonesrychtar.gispatialnet.Reader.Reader;
import us.jonesrychtar.gispatialnet.Writer.DLwriter;
import us.jonesrychtar.gispatialnet.Writer.Writer;

/**
 *
 * @author cfbevan
 */
public class CommandLineHelper {

    /**
     *
     */
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

    /**
     * Executes proper actions based on command line options
     * @param act Algorithm to use (if set)
     * @param i input file type
     * @param o output file type
     * @param id input directory
     * @param od output directory
     */
    public static void exec(int act, int i, int o, String id, String od) {
        //TODO: fix this to work on other systems
        //char sepChar ='/';
        char sepChar = File.separatorChar;
        boolean unknown=false;
        int alg=0;
        Scanner sc = new Scanner(System.in);
        GISpatialNet gsn = new GISpatialNet();
        int nrow=0, ncol=0, erow=0, ecol=0, row=0, col=0, Matrix_Format=0;
        char sep = ',';
        //get input
        //get extra data if needed
        switch (i) {
            case 'C': //need row,col,seperator, format
                System.out.println("Enter seperator character: ");
                sep = (char) sc.nextInt();
            case 'D': //need row,col, format

            case 'E': //need row,col, format
                Matrix_Format = new cli().getMenu("What format is the Adjacency file in?",
                    "",
                    new String[]{"Full Matrix", "Upper Matrix", "Lower Matrix"});

            case 'P': //need row,col?
                if(i!='D'){
                    System.out.println("Enter number of rows per Node Data Set: ");
                    nrow = sc.nextInt();
                    System.out.println("Enter the number of columns per  Node Data Set: ");
                    ncol = sc.nextInt();
                }
                System.out.println("Enter number of rows per Edge Data Set: ");
                erow = sc.nextInt();
                System.out.println("Enter the number of columns per Edge Data Set: ");
                ecol = sc.nextInt();
                break;
            case 'K': //no data needed
                break;
            case 'S': //no data needed
                break;
        }
        Matrix_Format -- ;
        File INdir = new File(id);
        String[] inFiles = INdir.list();
        for (int j = 0; j < inFiles.length; j++) {
            int Matrix=0;
            //get matrix type from filename
            String typeCode = inFiles[j].substring(inFiles[j].length()-7, inFiles[j].length()-4);
            if(typeCode.equals("xya")){
                Matrix = 0;
                row = nrow;
                col = ncol;
            } else if(typeCode.equals("adj")){
                Matrix = 1;
                row = erow;
                col = ecol;
            } else if(!(typeCode.equals("sfN")||typeCode.equals("sfE"))){
                //error
                System.out.println("file: "+inFiles[j]+ "does not have a proper extension (_xya or _adj or _sfN or _sfE)");
                System.exit(1);
            }

             Vector<DataSet> ds;
             String indir = id+sepChar;
            try{
                switch (i) { //read file to appropriate data set
                    case 'C':
                        ds = Reader.loadTxt(indir+inFiles[j], Matrix, Matrix_Format, row, col, sep);
                        for(int k=0; k<ds.size(); k++)
                            gsn.add(ds.elementAt(k));
                        break;
                    case 'D':
                        ds = Reader.loadDL(indir+inFiles[j], Matrix_Format, row, col);
                        for(int k=0; k<ds.size(); k++)
                            gsn.add(ds.elementAt(k));
                        break;
                    case 'E':
                        ds = Reader.loadExcel(indir+inFiles[j], Matrix, Matrix_Format, row, col);
                        for(int k=0; k<ds.size(); k++)
                            gsn.add(ds.elementAt(k));
                        break;
                    case 'K':
                        DataSet ds1 = Reader.loadGoogleEarth(indir+inFiles[j]);
                        gsn.add(ds1);
                        break;
                    case 'P':
                        ds = Reader.loadPajek(indir+inFiles[j], Matrix, row, col);
                        for(int k=0; k<ds.size(); k++)
                            gsn.add(ds.elementAt(k));
                        break;
                    case 'S':
                        //shapefiles will have form 1_sfE.shp and 1_sfN.shp, must be in order in list to work
                        DataSet ds2 = Reader.loadShapefile(indir+inFiles[j+1], indir+inFiles[j]);
                        gsn.add(ds2);
                        j++;
                        break;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        //Merge datasets according to filename
        mergeByFilename(gsn);

        if (act != -1) { //perform action and save to od
            double bias = 0.0;

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
                        String prefix = gsn.getDataSets().elementAt(k).GetLoadedFiles().elementAt(0);
                        String pre2 = prefix.substring(0,(prefix.length()-8));
                        pre2= pre2.split(String.valueOf(sepChar))[pre2.split(String.valueOf(sepChar)).length-1];
                        String outFnN = od + sepChar + pre2 + "_Node";
                        String outFnE = od + sepChar + pre2 + "_Edge";
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
                    sep = sc.next().charAt(0);
                    break;
                case 'S': //need to know if XY is known
                    System.out.println("Is XY known?(y or n)");
                    char uk = sc.next().charAt(0);
                    
                    if(uk == 'n')
                        unknown = true;

                    if(unknown){
                        //get algorithm to use
                        System.out.println("Choose an algorithm:\n"+
                                   "1) GeoNet Algorithm");
                        while(!sc.hasNextInt())
                            System.out.println("Input not a number. Choose an algorithm:\n"+
                                   "1) GeoNet Algorithm");
                        alg = sc.nextInt();
                        while(alg<0 || alg>1){
                            System.out.println("Incorrect choice. Choose an algorithm:\n"+
                                   "1) GeoNet Algorithm");
                        while(!sc.hasNextInt())
                            System.out.println("Input not a number. Choose an algorithm:\n"+
                                   "1) GeoNet Algorithm");
                        alg = sc.nextInt();
                        }
                    }
                    break;
                default:
                    break;
            }

            //save all datasets
            for (int k = 0; k < gsn.NumberOfDataSets(); k++) {
                //get filenames for output
                String base = gsn.getDataSets().elementAt(k).GetLoadedFiles().elementAt(0);
                String outFn = od + sepChar + (base.substring(id.length()+1, base.length()-8));
                String outFnN = outFn + "_Node";
                String outFnE = outFn + "_Edge";

                //get dataset
                DataSet ds = gsn.getDataSets().elementAt(k);
                try {
                    switch (o) { //write data to file
                        case 'C':
                            
                            Writer.saveCSV(outFnN, outFnE, sep, ds);
                            break;
                        case 'D':
                           
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
                            
                            Writer.savePajek(outFn, ds);
                            break;
                        case 'S':
                            int h = (int) (ds.getX().getColumnCount() * 100);
                            if(unknown){
                                Writer.saveShapefileUnknown(outFnE, outFnN, alg, h, h, ds);
                            }else
                                Writer.saveShapefile(outFnE, outFnN, ds);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }
    /**
     * Merges all datasets with same name
     * @param gsn GISpatialNet object containing all datasets to merge
     */
    public static void mergeByFilename(GISpatialNet gsn){
        for (int i = 0; i < gsn.NumberOfDataSets() - 1; i++) {
            for (int j = i + 1; j < gsn.NumberOfDataSets(); j++) {
                String filename1 = gsn.getDataSets().elementAt(i).GetLoadedFiles().elementAt(0);
                String filename2 = gsn.getDataSets().elementAt(j).GetLoadedFiles().elementAt(0);
                String f1 = filename1.substring(0, filename1.length() - 8);
                String f2 = filename2.substring(0, filename2.length() - 8);
                if (f1.equals(f2)) {
                    DataSet ds1 = gsn.getData(i);
                    DataSet ds2 = gsn.getData(j);
                    DataSet newI = new SimpleMerge(ds1,ds2).Merge();
                    gsn.setData(i, newI);
                    gsn.Remove(j);
                }
            }
        }
    }
}
