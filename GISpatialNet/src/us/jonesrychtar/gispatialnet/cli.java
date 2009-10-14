/*
 * This prints the command line interface.
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 *
 */
package us.jonesrychtar.gispatialnet;

import java.util.Scanner;

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

    public static void main(String[] args) {
       c = new cli();
       u = new util();
       c.Menu();
    }

    public void cli(){
    }

     //Menus----------------------------------------------------------------------------------
    public void Menu() {
        int option= getMenu(
                "Main Menu:",
                u.Status(statusLevel),
                new String[] {"Load data","Save Data","Analyze Data","Print Full Status","Exit"} );

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
                    u.Status(3);
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input. Please re-enter.");
                    break;
            }

//        }
    }

    private void LoadMenu1(){
        int option = getMenu(
                "Load Menu:",
                u.Status(statusLevel),
                new String[] {"Graph/Network Data","Node Coordinate/Location Data",
                "Attribute Data","Exit"} );
        if(option<3 && option>0)
            LoadMenu2(option);
        else
            Menu();

    }
    private void LoadMenu2(int what) {
        int option = getMenu(
                "Load File Menu:",
                u.Status(statusLevel),
                new String[] {"Delimited text file (.csv,.txt)",
                "DL/ucinet (.txt,.dat)","Pajek (.net)",
                "Back"});
          switch (option) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
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
                u.Status(statusLevel),
                new String[] {"Delimited text file (.csv,.txt)",
                "DL/ucinet (.txt,.dat)","Pajek (.net)",
                "Back"});
           switch (option) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
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
                u.Status(statusLevel),
                new String[] {"QAP"});
          switch (option) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                Menu();
                break;
            default:
                System.out.println("Invalid input. Please re-enter.");
                break;
        }
    }
    //End menus ---------------------------------------------------------------------------
    public int getMenu(String title, String info ,String[] items){
        //return and err out if length of items is less than one.
        if(items.length<1){
            System.err.println("Incorrect length for menu items.");
            return 0;
        }

        Scanner sc = new Scanner(System.in);    //new Scanner for getting input.
        int option=0;                           //the option input value
        boolean validInput=false;               //whether the input is valid

        //do menu while the input is not valid
        while(!validInput){
            //print the menu.
            System.out.println("\n\n"+title+"\n");
            System.out.print(info+"\n");
            for(int i=0;i<items.length;i++) System.out.println("\t"+(i+1)+") "+items[i]+"\n");
            System.out.println("Please enter your selection (1-"+items.length+"): ");

            //get user input
            option = sc.nextInt();

            //validate input
            if(option>=1 && option <=items.length) validInput=true;
            else System.out.println("Invalid Input.");
        }

        return option;
    }
}
