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
public class Polar extends CoordinateGraphBase {
   	public enum angleType { RADIANS, DEGREES,DECIMAL;}
    private angleType theType;

    public Polar() {

        //Get type from user
		Scanner sc = new Scanner(System.in);
		int option = 0;
		while( option >= 3 && option <= 1 ) {
           	System.out.println("Is the Polar dataset format using distance and : \n" +
        		"\t1. Radians (e.g. 3.14159)\n" +
            	"\t2. Degree (e.g. 180)\n" +
                "\t3. Decimal Degree (e.g. 0.5)\nYour Choice: ");
            option = sc.nextInt();
		}
        //make the angleType the correct type
        switch(option){
            case 1:
            theType=angleType.RADIANS;
            break;
            case 2:
            theType=angleType.DEGREES;
            break;
            case 3:
            theType=angleType.DECIMAL;
            break;
        }
    }

    public Polar(int angleType) {
    }
}
