/*
 * This is the main program. It coordinates all other classes
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

public class cli {

    private static cli c;

    public static void main(String[] args) {
       c = new cli();
       c.Menu();
    }

    public void cli(){
    }

     //Menus----------------------------------------------------------------------------------
    public void Menu() {
        System.out.println("\n\nMain menu:");
        System.out.println("1) Load Data\n" +
                "2) Save Data\n" +
                "3) Analyze Data\n" +
                "4) Exit\n");
        while (true) {
            Scanner sc = new Scanner(System.in);
            int option = sc.nextInt();
            switch (option) {
                case 1:
                    LoadMenu();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input. Please re-enter.");
                    break;
            }

        }
    }

    private void LoadMenu() {
        System.out.println("\n\nLoad Data menu:");
        System.out.println("1) Delimited text file (.csv,.txt)\n" +
                "2) DL/UCINET (.txt,.dat)\n" +
                "3) Pajek (.net)\n" +
                "4) Shape File\n" +
                "5) Back");
        while (true) {
            Scanner sc = new Scanner(System.in);
            int option = sc.nextInt();
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
    }
    private void SaveMenu() {
        System.out.println("\n\nSave Data menu:");
        System.out.println("1) Delimited text file (.csv,.txt)\n" +
                "2) DL/UCINET (.txt,.dat)\n" +
                "3) Pajek (.net)\n" +
                "4) Shape File\n" +
                "4) Back");
        while (true) {
            Scanner sc = new Scanner(System.in);
            int option = sc.nextInt();
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
    }
    private void AnalyzeMenu() {
        System.out.println("\n\nAnalyze Data menu:");
        System.out.println("1) \n" +
                "2) \n" +
                "3) \n" +
                "4) \n" +
                "4) Back");
        while (true) {
            Scanner sc = new Scanner(System.in);
            int option = sc.nextInt();
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
    }
    //End menus ----------------------------------------------------------------------------

}
