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

	public void setCoordinateFormat() {
		System.out.println("Is the coordinate dataset in x,y format, or in direction and distance format?\n" +
			"Please enter the option number from the menu below.\n" +
			"\t1. x,y format.\n" +
			"\t2. Polar ( and angle and distance) format.\n");
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
