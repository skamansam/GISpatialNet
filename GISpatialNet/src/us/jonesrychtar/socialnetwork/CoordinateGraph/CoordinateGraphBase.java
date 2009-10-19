/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package us.jonesrychtar.socialnetwork.CoordinateGraph;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 * @author Samuel C. Tyler <skamansam@gmail.com>
 */
public class CoordinateGraphBase {

	CoordinateGraphBase theFormat = null;

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
		// TODO: Make menu.
		System.out
				.println("Is the coordinate dataset in x,y format, or in direction and distance format?\n"
						+ "Please enter the option number from the menu below.\n"
						+ "\t1. x,y format.\n"
						+ "\t2. Polar ( and angle and distance) format.\n");
		// Menu.createMenu("New Menu Option:",["Option 1","Option 2"],\&setCoordinateFormat())
		Scanner sc = new Scanner(System.in);
		int option = 0;
		while (option >= 2 && option <= 1) {
			option = sc.nextInt();
			switch (option) {
			case 1:
				theFormat = new XY();
				break;
			case 2:
				theFormat = new Polar();
				break;
			default:
				System.out.println("Invalid input. Please re-enter.");
			}
		}
	}

	@SuppressWarnings("unused")
	private void createRandomCoordinateGraphDataInFiles()
			throws FileNotFoundException {
		Random rand = new Random();
		PrintStream outx = new PrintStream(new FileOutputStream("x.csv"));
		PrintStream outy = new PrintStream(new FileOutputStream("y.csv"));
		PrintStream outa = new PrintStream(new FileOutputStream("a.csv"));

		for (int i = 0; i < 25; i++) {
			outx.print(rand.nextDouble() * 10);
			if (i < 24) {
				outx.print(",");
			}
		}
		outx.close();

		for (int i = 0; i < 25; i++) {
			outy.print(rand.nextDouble() * 10);
			if (i < 24) {
				outy.print(",");
			}
		}
		outy.close();

		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 25; j++) {
				if (i == j) {
					outa.print(0);
				} else {
					outa.print(rand.nextInt() % 2);
				}
				if (j < 24) {
					outa.print(",");
				}
			}
			outa.println("");
		}
		outa.close();
	}

}
