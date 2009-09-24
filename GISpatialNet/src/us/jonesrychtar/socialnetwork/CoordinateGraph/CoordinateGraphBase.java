/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package us.jonesrychtar.socialnetwork.CoordinateGraph;

import java.util.Scanner;

/**
 *
 * @author Samuel C. Tyler <skamansam@gmail.com>
 */
public class CoordinateGraphBase {
    
    CoordinateGraphBase theFormat=null;

    public CoordinateGraphBase() {
    }
   public void readValue(java.util.Scanner sc) {
        System.err.println("Please override readValue() in your coordinate class.");
   }
   public boolean isValidNodeValue() {
        System.err.println("Please override isValidNodeValue() in your coordinate class.");
        return false;
   }
	public void setCoordinateFormat() {
        //TODO: Make menu.
		System.out.println("Is the coordinate dataset in x,y format, or in direction and distance format?\n" +
			"Please enter the option number from the menu below.\n" +
			"\t1. x,y format.\n" +
			"\t2. Polar ( and angle and distance) format.\n");
        //Menu.createMenu("New Menu Option:",["Option 1","Option 2"],\&setCoordinateFormat())
			Scanner sc = new Scanner(System.in);
			int option = 0;
		while( option >= 2 && option <= 1 ) {
   			option = sc.nextInt();
			switch(option){
                case 1:
                    theFormat=new XY();
                    break;
                case 2:
                    theFormat=new Polar();
                    break;
                default:
                    System.out.println("Invalid input. Please re-enter.");
            }
		}
	}


}
