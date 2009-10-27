/*
 *    zt - Simple and partial Mantel Test - version 1.1
 *    copyright (c) Eric Bonnet 2001 - 2007
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *
 *
 */

package us.jonesrychtar.gispatialnet;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.lang.Math.*;
import javax.naming.CannotProceedException;

/**
 * This program was originally written in C, recoded into java by Charles Bevan
 * @author cfbevan
 */
@SuppressWarnings("all")
public class qap {

	//statics from rr.h
	private static int MIN_MAT_SIZE = 5;          /* mimimum size for matrices */

	private static int MAX_EXACT_SIZE = 12;       /* maximum size for exact permutation procedure */

	private static int EXACT_PROC_SIZE = 8;       /* size critical for automatic permutation procedure */

	private double matA[][];                /* matrix A */

	private double matB[][];                /* matrix B */

	private double matC[][];                /* matrix C */

	private File fileA;                  /* file A */

	private File fileB;                  /* file B */

	private File fileC;                  /*  file C */

	private String fnameA;             /*filename A */

	private String fnameB;             /* filename B */

	private String fnameC;             /* filename C */

	private int NA;                      /* matrix A size */

	private int NB;                      /* matrix B size */

	private int NC;                      /* matrix C size */
	//private long i, j;

	private int c;
	private int tmem,  res;

	private Random rand = new Random(System.currentTimeMillis()); //random number generator
    //private Random rand = new Random();
	private Scanner scA, scB, scC;

	class param {

		double coef;                /* reference statistic */

		double proba;               /* p-value */

		long numrand;               /* number of randomizations */

		int matsize;               /* size of matrices */

		int numelt;                /* number of elements in the half-matrix without diagonal values */

		int partial;                /* option partial 0|1 */

		int raw;                    /* option raw 0|1 */

		int help;                   /* option help 0|1 */

		int exact;                  /* option exact permutation 0|1 */

		int licence;                /* option licence terms 0|1 */

	};

    private param myp = new param();             /* struct for storing parameters */
    /**
     *
     * @param argc number of arguments
     * @param argv arguments and filenames
     */

	public qap(int argc, String argv[]) throws IllegalArgumentException, IOException, Error, CannotProceedException{


		tmem = 0;
		res = 0;

		myp.coef = 0;
		myp.proba = 0;
		myp.coef = 0;
		myp.numrand = 0;
		myp.matsize = 0;
		myp.numelt = 0;
		myp.partial = -1;
		myp.raw = 0;
		myp.help = 0;
		myp.exact = 0;
		myp.licence = 0;
		c = 0;


		/* seed for random function */
		//not needed in java
		//srand = new long ((unsigned) time (0));

		/* header */
		//System.out.print ("\nzt - version 1.1\n\n");
		System.out.print ("Original zt copyright (c) Eric Bonnet 2001 - 2007\n\n");
		System.out.print ("mailto: eric.bonnet@psb.ugent.be\n\n");

		/* parse arguments a la Kernighan & Ritchie */
		if (argc == 0 || argv[0].charAt(0) != '-') {
			myp.help = 1;
            throw new IllegalArgumentException("Error: unknown option");
			//System.out.println("Error: unknown option");
		} else {
			int a = 0;
			while (a < argc && argv[a].charAt(0) == '-') {
				c = argv[a].charAt(1);
				switch (c) {
					case 'p':
						myp.partial = 1;
						break;
					case 'r':
						myp.raw = 1;
						break;
					case 'h':
						myp.help = 1;
						break;
					case 'e':
						myp.exact = 1;
						break;
					case 's':
						myp.partial = 0;
						break;
					case 'l':
						myp.licence = 1;
						break;
					default:
                        myp.help = 1;
						argc = 0;
                        //throw new IllegalArgumentException("Error: unknown option"+c+"\n");
						System.out.print("Error: unknown option" + c + "\n");
                        break;
				}
				a++;
			}
		}

		/* print help usage */
		if (myp.help == 1) {
			System.out.print("\nUsage:\n\n");
			System.out.print("Simple Mantel test:\nzt -s file1 file2 number_of_randomizations\n\n");
			System.out.print("Partial Mantel test:\nzt -p file1 file2 file3 number_of_randomizations\n");
			System.out.print("\nOptions:\n");
			System.out.print("-r partial Mantel test with raw option\n");
			System.out.print("-e force exact permutations procedure\n");
			System.out.print("-l print licence terms\n");
			System.out.print("-h display this help\n\n");
            throw new CannotProceedException("Usage printed");
		//return(1);
		}

		/* print licence terms */
		if (myp.licence == 1) {
			System.out.print("\nThis program is free software; you can redistribute it and/or modify\n");
			System.out.print("it under the terms of the GNU General Public License as published by\n");
			System.out.print("the Free Software Foundation; either version 2 of the License, or\n");
			System.out.print("(at your option) any later version.\n\n");
			System.out.print("This program is distributed in the hope that it will be useful,\n");
			System.out.print("but WITHOUT ANY WARRANTY; without even the implied warranty of\n");
			System.out.print("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n");
			System.out.print("GNU General Public License for more details.\n\n");
			System.out.print("You should have received a copy of the GNU General Public License\n");
			System.out.print("along with this program; if not, write to the Free Software\n");
			System.out.print("Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307\n\n");
            throw new CannotProceedException("licence printed");
            //return(1);
		}

		/* test mandatory parameters */
		if (myp.partial == -1) {
            throw new IllegalArgumentException("Error: -s or -p mandatory parameters.\n\n");
			//System.out.print("Error: -s or -p mandatory parameters.\n\n");
		//return(1);
		}

		/* test number of arguments */
		if (myp.partial == 0) {
			if ((myp.exact == 0 && argc < 3) || (myp.exact == 1 && argc < 2)) {
                throw new IllegalArgumentException("Error: bad number of arguments");
				//System.out.print("Error: bad number of arguments.\n\n");
			//return(1);
			}
		} else {
			if ((myp.exact == 0 && argc < 4) || (myp.exact == 1 && argc < 3)) {
                throw new IllegalArgumentException("Error: bad number of arguments.\n\n");
				//System.out.print("Error: bad number of arguments.\n\n");
			//return(1);
			}
		}

		/* get filenames */
		fnameA = argv[argc-3 + 1];
		fnameB = argv[argc-3 + 2];
		if (myp.partial == 1) {
			fnameC = argv[argc-3 + 3];
		}

		/* open files and look for matrix size */
		try {
			fileA = new File(fnameA);
			if (fileA == null) {
                throw new IOException("Error: can't open FileA.\n");
				//System.out.print("Error: can't open fileA.\n");
			//return(1);
			}
			scA = new Scanner(fileA);
            NA = scA.nextInt();
			//fscanf (fileA, "%li", &NA);

			fileB = new File(fnameB);
			if (fileB == null) {
                throw new IOException("Error: can't open FileB.\n");
				//System.out.print("Error: can't open fileB.\n");
			//return(1);
			}
			scB = new Scanner(fileB);
            NB = scB.nextInt();
			//fscanf (fileB, "%li", &NB);

			if (myp.partial == 1) {
				fileC = new File(fnameC);
				if (fileC == null) {
                    throw new IOException("Error: can't open FileC.\n");
					//System.out.print("Error: can't open fileC\n");
				//return(1);
				}
				scC = new Scanner(fileC);
                NC = scC.nextInt();
			//fscanf (fileC, "%li", &NC);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* test matrices size */
		if (myp.partial == 0) {
			if (NA != NB) {
                throw new IllegalArgumentException("Error: matrices of different size.\n\n");
				//ystem.out.print("Error: matrices of different size.\n\n");
			//return(1);
			}
		} else {
			if (NA != NB || NA != NC || NB != NC) {
                throw new IllegalArgumentException("Error: matrices of different size.\n\n");
				//System.out.print("Error: matrices of different size.\n\n");
			//return(1);
			}
		}

		/* define and test matrices size */
		/*myp.matsize = NA + 1; */
		myp.matsize = NA;
		myp.numelt = (myp.matsize * (myp.matsize - 1)) / 2;

        
		if (myp.matsize < MIN_MAT_SIZE) {
            throw new IllegalArgumentException("Error: matrix size must be >= " + MIN_MAT_SIZE + "\n\n");
			//System.out.print("Error: matrix size must be >= " + MIN_MAT_SIZE + "\n\n");
		//return(1);
		}

		if (myp.exact == 1 && myp.matsize > MAX_EXACT_SIZE) {
            throw new IllegalArgumentException("Error: matrix size is too big for exact permutations (should be <=" + MAX_EXACT_SIZE + ").\n\n");
			//System.out.print("Error: matrix size is too big for exact permutations (should be <=" + MAX_EXACT_SIZE + ").\n\n");
		//return(1);
		}

		/* force exact permutation procedure if size too small */
		if (myp.matsize < EXACT_PROC_SIZE) {
			myp.exact = 1;
		}

		/* define and test number of randomizations */
		if (myp.exact == 0) {
            //myp.numrand = atoi (*++argv);
			myp.numrand = Integer.parseInt(argv[3]);

			if (myp.numrand < 99 || myp.numrand > 999999999) {
                throw new IllegalArgumentException("Error: Number of iterations must be between 99 and 999999999.\n\n");
				//System.out.print("Error: Number of iterations must be between 99 and 999999999.\n\n");
			//return(1);
			}
		}

		/* memory allocation */
		//matA = malloc (NA * sizeof (double));
        matA = new double[NA][];
		//matB = malloc (NA * sizeof (double));
        matB = new double[NB][];

		if (myp.partial == 1)
            //matC = malloc (NA * sizeof (double));
            matC = new double[NC][];
		if (myp.partial == 0) {
            for (int i = 0; i < NA; i++) {
                //matA[i] = malloc ((i + 1) * sizeof (double));
                matA[i] = new double[i+1];
                //matB[i] = malloc ((i + 1) * sizeof (double));
                matB[i] = new double[i+1];
                if (matA[i] == null || matB[i] == null) {
                    tmem = 1;
                    break;
                }
            }
		}
		else {
            for (int i = 0; i < NA; i++) {
                //matA[i] = malloc ((i + 1) * sizeof (double));
                matA[i] = new double[i+1];
                //matB[i] = malloc ((i + 1) * sizeof (double));
                matB[i] = new double[i+1];
                //matC[i] = malloc ((i + 1) * sizeof (double));
                matC[i] = new double[i+1];
                if (matA[i] == null || matB[i] == null || matC[i] == null) {
                    tmem = 1;
                    break;
                }
            }
		}

		/*if (tmem == 1) {
		System.out.print ("Error: not enough memory.\n\n");
		return(1);
		}*/


		/* get data into matrices */
		if (myp.partial == 0) {
			for (int i = 0; i < NA - 1; i++) {
				for (int j = 0; j <= i; j++) {
					matA[i][j] = scA.nextDouble();
					matB[i][j] = scB.nextDouble();
				//fscanf (fileA, "%lf", &matA[i][j]);
				//fscanf (fileB, "%lf", &matB[i][j]);
				}
			}
		} else {
			for (int i = 0; i < NA - 1; i++) {
				for (int j = 0; j <= i; j++) {
					matA[i][j] = scA.nextDouble();
					matB[i][j] = scB.nextDouble();
					matC[i][j] = scC.nextDouble();
				//fscanf (fileA, "%lf", &matA[i][j]);
				//fscanf (fileB, "%lf", &matB[i][j]);
				//fscanf (fileC, "%lf", &matC[i][j]);
				}
			}
		}

		if (myp.exact == 1) {
			myp.numrand = fact((int) myp.matsize);
		}

		/* print parameters of the test */
		System.out.print("File A:\t\t\t" + fnameA + "\n");
		System.out.print("File B:\t\t\t" + fnameB + "\n");
		if (myp.partial == 1) {
			System.out.print("File C:\t\t\t" + fnameC + "\n");
		}
		System.out.print("Size of matrices:\t" + myp.matsize + " x " + myp.matsize + "\n");
		System.out.print("Number of iterations:\t" + myp.numrand + "\n");
		System.out.print("Options:\t\t");
		if (myp.partial == 0) {
			System.out.print("simple ");
		} else {
			System.out.print("partial ");
			if (myp.raw == 1) {
				System.out.print("raw ");
			} else {
				System.out.print("residuals ");
			}
		}
		if (myp.exact == 1) {
			System.out.print("exact ");
		}
		System.out.print("\n\n");
		System.out.print("Randomizing...\n\n");

		/* launch the test */
		if (myp.partial == 1) {
			res = pmt(matA, matB, matC, myp);
			if (res != 0) {
				System.out.print("r =\t\t\t" + myp.coef + "\n");
				System.out.print("p =\t\t\t" + myp.proba + " (one-tailed)\n\n");
			} else {
                throw new Error("An error has occurred during permutation procedure.\nPlease retry.\n\n");
				//System.out.print("An error has occurred during permutation procedure.\nPlease retry.\n\n");
			}
		} else {
			res = smt(matA, matB, myp);
			if (res != 0) {
				System.out.printf("r =\t\t\t%f\n", myp.coef);
				System.out.printf("p =\t\t\t%f (one-tailed)\n\n", myp.proba);
			} else {
                throw new Error("An error has occurred during permutation procedure.\nPlease retry.\n\n");
				//System.out.print("An error has occurred during permutation procedure.\nPlease retry.\n\n");
			}
		}
	}

	//rr.c
	/*
	 *       fact: compute factorial
	 *       input integer n
	 *       return n! as long
	 */
	private long fact(int n) {

		int i;
		long ret = 1;
		for (i = 1; i <= n; i++) {
			ret *= i;
		}
		return ret;

	}

	/*
	 *       somx:   compute simple sum of elements in a half-matrix
	 *       input:  matrix pointer, size of the half-matrix
	 *       return: sum as double
	 */
	private double somx(double a[][], long stop) {

		int i;
		int j;
		double ret = 0;
		for (i = 0; i < stop; i++) {
			for (j = 0; j <= i; j++) {
				ret += a[i][j];
			}
		}
		return ret;

	}

	/*
	 *       somx2:  compute square sum of elements in a half-matrix
	 *       input:  matrix pointer, size of the half-matrix
	 *       return: square sum as double
	 */
	private double somx2(double a[][], long stop) {

		int i;
		int j;
		double ret = 0;
		for (i = 0; i < stop; i++) {
			for (j = 0; j <= i; j++) {
				ret += a[i][j] * a[i][j];
			}
		}
		return ret;

	}
	/*
	 *       moy:    compute mean for a half-matrix
	 *       input:  matrix pointer, size of the half-matrix
	 *       return: mean as double
	 */

	private double moy(double a[][], long stop) {

		int i;
		int j;
		long N;
		double ret = 0;
		N = (stop * (stop - 1) / 2) + stop;
		for (i = 0; i < stop; i++) {
			for (j = 0; j <= i; j++) {
				ret += a[i][j];
			}
		}
		ret = ret / N;
		return ret;

	}

	/*
	 *       ect:    compute standard deviation for a half-matrix
	 *       input:  matrix pointer, size of the half-matrix
	 *       return: standard deviation as double
	 */
	private double ect(double a[][], long stop) {

		long N;
		double ret = 0;
		double lsomx = somx(a, stop);
		double lsomx2 = somx2(a, stop);
		N = (stop * (stop - 1) / 2) + stop;
		ret = java.lang.Math.sqrt((lsomx2 - (lsomx * lsomx) / N )/ (N - 1));
		return ret;

	}

	/*
	 *       shake:  shaking elements of a vector at random
	 *       input:  array pointer, size of the array
	 */
	private void shake(long a[], long f) {

		int i;
		int aleat;
		long tmp;
		for (i = 0; i < f - 1; i++) {
			aleat = i + (1 + rand.nextInt((int)f-i-1));
			tmp = a[i];
			a[i] = a[aleat];
			a[aleat] = tmp;
		}

	}

	/*
	 *       sompx: compute square sum of mean deviations for a half matrix
	 *       input:  matrix pointer, size of the half-matrix
	 *       return: sum as double
	 */
	private double sompx(double a[][], long stop) {

		double ret = 0;
		long N;
		double lsomx = somx(a, stop);
		double lsomx2 = somx2(a, stop);
		N = ((stop * (stop - 1)) / 2) + stop;
        ret = (lsomx2 - ((lsomx * lsomx) / N));
		return ret;

	}

	/*
	 *       sompxy: sum x-mean(x) * y-mean(y) for two half matrices
	 *       input:  matrix A pointer, matrix B pointer, size of the matrices, mean matrix A, mean matrix B
	 *       return: sum as double
	 */
	private double sompxy(double a[][], double b[][], long stop, double lmoyA, double lmoyB) {

		int i;
		int j;
		double ret = 0;
		for (i = 0; i < stop; i++) {
			for (j = 0; j <= i; j++) {
				ret += (a[i][j] * b[i][j]) - (lmoyA * lmoyB);
			}
		}
		return ret;

	}

	/*
	 *       resid:  compute residuals of half-matrix A against half-matrix B
	 *       input:  matrix A pointer, matrix B pointer, size of the matrices, mean matrix A, mean matrix B
	 */
	private void resid(double a[][], double b[][], long stop, double lmoyA, double lmoyB) {

		double coef_b;
		double coef_a;
		int i;
		int j;
		coef_b = sompxy (a, b, stop, lmoyA, lmoyB) / sompx (b, stop);
        coef_a = lmoyA - (coef_b * lmoyB);
		for (i = 0; i < stop; i++) {
			for (j = 0; j <= i; j++) {
				a[i][j] = a[i][j] - (coef_a + (coef_b * b[i][j]));
			}
		}

	}

	/*
	 *       norm: normalization of a half-matrix
	 *       input:  matrix pointer, size of the matrix
	 */
	private void norm(double a[][], long stop) {

		double lmoya;
		double lecta;
		int i;
		int j;
		lmoya = moy(a, stop);
		lecta = ect(a, stop);
		for (i = 0; i < stop; i++) {
			for (j = 0; j <= i; j++) {
				a[i][j] = (a[i][j] - lmoya) / lecta;
			}
		}

	}

	/*
	 *       pmt:    partial Mantel test
	 *       input:  matrix A pointer, matrix B pointer, matrix C pointer, struct of parameters (see rr.h)
	 *       return: 1 if ok
	 */
	private int pmt(double A[][], double B[][], double C[][], param p) {

		double moyA;
		double moyC;
		int i;
		int j;
		long N;
		double r_abc = 0;
		double r_ab = 0;
		double r_ac = 0;
		double r_bc = 0;
		int ret = 0;
		N = p.matsize - 1;
		moyA = moy(A, N);
		moyC = moy(C, N);
		if (p.raw == 0) {
			resid(A, C, N, moyA, moyC);
		}
		norm(A, N);
		norm(B, N);
		norm(C, N);
		for (i = 0; i < N; i++) 
			for (j = 0; j <= i; j++) 
				r_ab += A[i][j] * B[i][j];
        r_ab = r_ab / (p.numelt - 1);
		
		for (i = 0; i < N; i++) 
			for (j = 0; j <= i; j++) 
				r_ac += A[i][j] * C[i][j];
        r_ac = r_ac / (p.numelt - 1);
		
		for (i = 0; i < N; i++) 
			for (j = 0; j <= i; j++) 
				r_bc += B[i][j] * C[i][j];
        r_bc = r_bc / (p.numelt - 1);
		r_abc =(r_ab -(r_ac * r_bc)) / (Math.sqrt (1 - (r_ac * r_ac)) * Math.sqrt (1 - (r_bc * r_bc)));
		p.coef = r_abc;
		if (p.exact == 0) {
			ret = pmt_perm(A, B, C, r_bc, p);
		} else {
			ret = pmt_perm_exact(A, B, C, r_bc, p);
		}
		return ret;

	}

	/*
	 *       pmt_perm:       randomization procedure for partial Mantel test
	 *       input:          matrix A pointer, matrix B pointer, matrix C pointer, r_bc pointer, struct for parameters
	 *       return:         1 if ok
	 */
	private int pmt_perm(double A[][], double B[][], double C[][], double r_bc, param p) {

		int i;
		int j;
		long r;
		long cptega;
		long cptinf;
		long cptsup;
		double r_ab;
		double r_ac;
		double rrand;
		double epsilon;
		long[] ord = new long[p.matsize];
		cptega = 1;
		cptsup = 0;
		cptinf = 0;
		epsilon = 0.0000001;
		//ord = malloc (p.matsize )* sizeof long );
           /*if (ord == null) {
		return -1;
		}*/
		for (i = 0; i < p.matsize; i++) {
			ord[i] = i;
		}
		for (r = 0; r < p.numrand; r++) {
			rrand = 0;
			shake(ord, p.matsize);
			r_ab = 0;
			for (i = 1; i < p.matsize; i++) {
				for (j = 0; j < i; j++) {
					if (ord[j] < ord[i]) {
						r_ab = B[i - 1][j] * A[(int) ord[i] - 1][(int) ord[j]];
					} else {
						r_ab = B[i - 1][j] * A[(int) ord[j] - 1][(int) ord[i]];
					}
				}
			}
			r_ab = r_ab / (p.numelt - 1);
			r_ac = 0;
			for (i = 1; i < p.matsize; i++) {
				for (j = 0; j < i; j++) {
					if (ord[j] < ord[i]) {
						r_ac = C[i - 1][j] * A[(int) ord[i] - 1][(int) ord[j]];
					} else {
						r_ac = C[i - 1][j] * A[(int) ord[j] - 1][(int) ord[i]];
					}
				}
			}
			r_ac = r_ac / p.numelt - 1;
			double s1 = java.lang.Math.sqrt(1 - r_ac * r_ac);
			double s2 = java.lang.Math.sqrt(1 - r_bc * r_bc);
			rrand = r_ab - r_ac * r_bc / s1 * s2;
			if (fabs(rrand - p.coef) <= epsilon * fabs(rrand)) {
				cptega = 1;
			} else {
				if (rrand > p.coef) {
					cptsup += 1;
				}
				if (rrand < p.coef) {
					cptinf += 1;
				}
			}
		}
		if (p.coef < 0) {
			p.proba = (double) (cptinf + cptega) / (p.numrand + 1);
		} else {
			p.proba = (double) (cptsup + cptega) / (p.numrand + 1);
		}
		return 1;

	}

	/*
	 *       smt:    simple Mantel test
	 *       input:  matrix A pointer, matrix B pointer, struct for results
	 *       return  1 if ok
	 */
	private int smt(double A[][], double B[][], param p) {

		int i;
		int j;
		double zini;
		long N = p.matsize - 1;
		int ret = 0;
		norm(A, N);
		norm(B, N);
		zini = 0;
		for (i = 0; i < N; i++) {
			for (j = 0; j <= i; j++) {
				zini += A[i][j] * B[i][j];
			}
		}
		p.coef = zini / (p.numelt - 1);
		if (p.exact == 0) {
			ret = smt_perm(A, B, p);
		} else {
			ret = smt_perm_exact(A, B, p);
		}
		return ret;

	}

	/*
	 *       smt_perm:       randomization procedure for simple Mantel test
	 *       input:          matrix A pointer, matrix B pointer, struct for results
	 *       return          1 if ok
	 */
	private int smt_perm(double A[][], double B[][], param p) {

		int i;
		int j;
		long r;
		long cptega;
		long cptinf;
		long cptsup;
		double zrand;
		double epsilon;
		long[] ord = new long[p.matsize];
		epsilon = 0.0000001;
		//ord = malloc (p (matsize )* sizeof long );
            /*if (ord == null) {
		return -1;
		}*/
		for (i = 0; i < p.matsize; i++) {
			ord[i] = i;
		}
		cptega = 1;
		cptsup = 0;
		cptinf = 0;
		zrand = 0;
		for (r = 0; r < p.numrand; r++) {
			zrand = 0;
			shake(ord, p.matsize);
			for (i = 1; i < p.matsize; i++) {
				for (j = 0; j < i; j++) {
					if (ord[j] < ord[i]) {
						zrand += A[i - 1][j] * B[(int) ord[i] - 1][(int) ord[j]];
					} else {
						zrand += A[i - 1][j] * B[(int) ord[j] - 1][(int) ord[i]];
					}
				}
			}
			zrand = zrand / (p.numelt - 1);
			if (fabs(zrand - p.coef) <= epsilon * fabs(zrand)) {
				cptega += 1;
			} else {
				if (zrand > p.coef) {
					cptsup += 1;
				}
				if (zrand < p.coef) {
					cptinf += 1;
				}
			}
		}
		if (p.coef < 0) {
			p.proba = (double) (cptinf + cptega) / (p.numrand + 1);
		} else {
			p.proba = (double) (cptsup + cptega) / (p.numrand + 1);
		}
		//free (ord );
		return 1;

	}

	/*
	 *       smt_perm_exact:         exact permutation procedure for simple Mantel test
	 *       input:                  matrix pointer A, matrix pointer B, struct for results
	 *       return                  1 if ok
	 */
	private int smt_perm_exact(double A[][], double B[][], param p) {

		int i;
		int j;
		long r;
		long s;
		long li;
		long lj;
		long temp;
		long n;
		long cpt;
		long cptega;
		long cptinf;
		long cptsup;
		long ord[] = new long[p.matsize];
		double zrand;
		double epsilon;
		epsilon = 0.0000001;
		//ord = malloc (p (matsize )* sizeof long );
           /*if (ord == null) {
		return -1;
		}*/
		for (i = 0; i < p.matsize; i++) {
			ord[i] = i;
		}
		i = 1;
		n = p.matsize - 1;
		cptega = 0;
		cptsup = 0;
		cptinf = 0;
		cpt = 0;
		while (i >= 0) {
			cpt++;
			zrand = 0;
			for (li = 1; li < p.matsize; li++) {
				for (lj = 0; lj < li; lj++) {
					if (ord[(int) lj] < ord[(int) li]) {
						zrand += A[(int) li - 1][(int) lj] * B[(int) ord[(int) li] - 1][(int) ord[(int) lj]];
					} else {
						zrand += A[(int) li - 1][(int) lj] * B[(int) ord[(int) lj] - 1][(int) ord[(int) li]];
					}
				}
			}
			zrand = zrand / (p.numelt - 1);
			if (fabs(zrand - p.coef) <= epsilon * fabs(zrand)) {
				cptega += 1;
			} else {
				if (zrand > p.coef) {
					cptsup += 1;
				}
				if (zrand < p.coef) {
					cptinf += 1;
				}
			}
			i = (int) n - 1;
			while (ord[i] > ord[i + 1]) {
				i--;
			}
			j = (int) n;
			while (ord[i] > ord[j]) {
				j--;
			}
			if (i >= 0) {
				temp = ord[i];
				ord[i] = ord[j];
				ord[j] = temp;
				r = n;
				s = i + 1;
				while (r > s) {
					temp = ord[(int) r];
					ord[(int) r] = (int) ord[(int) s];
					ord[(int) s] = (int) temp;
					r--;
					s++;
				}
			}
		}
		if (p.coef < 0) {
			p.proba = (double) (cptinf + cptega) / cpt;
		} else {
			p.proba = (double) (cptsup + cptega) / cpt;
		}
		//free (ord );
		return 1;

	}

	/*
	 *       pmt_perm_exact: exact permutation procedure for partial Mantel test
	 *       input:          matrix A pointer, matrix B pointer, matrix C pointer, r_bc pointer, struct for results
	 *       return          1 if ok
	 */
	private int pmt_perm_exact(double A[][], double B[][], double C[][], double r_bc, param p) {

		int i;
		int j;
		int r;
		int s;
		int li;
		int lj;
		int temp;
		int n;
		long cpt;
		long cptega;
		long cptinf;
		long cptsup;
		long ord[] = new long[p.matsize];
		double r_ab;
		double r_ac;
		double rrand;
		double epsilon;
		epsilon = 0.0000001;
		//ord = malloc (p (matsize )* sizeof long );
           /*if (ord == null) {
		return -1;
		}*/
		for (i = 0; i < p.matsize; i++) {
			ord[i] = i;
		}
		i = 1;
		n = p.matsize - 1;
		cptega = 0;
		cptsup = 0;
		cptinf = 0;
		cpt = 0;
		while (i >= 0) {
			cpt++;
			rrand = 0;
			r_ab = 0;
			r_ac = 0;
			for (li = 1; li < p.matsize; li++) {
				for (lj = 0; lj < li; lj++) {
					if (ord[lj] < ord[li]) {
						r_ab += B[li - 1][lj] * A[(int) ord[li] - 1][(int) ord[lj]];
					} else {
						r_ab += B[li - 1][lj] * A[(int) ord[lj] - 1][(int) ord[li]];
					}
				}
			}
			r_ab = r_ab / (p.numelt - 1);
			for (li = 1; li < p.matsize; li++) {
				for (lj = 0; lj < li; lj++) {
					if (ord[lj] < ord[li]) {
						r_ac += C[li - 1][lj] * A[(int) ord[li] - 1][(int) ord[lj]];
					} else {
						r_ac += C[li - 1][lj] * A[(int) ord[lj] - 1][(int) ord[li]];
					}
				}
			}
			r_ac = r_ac / p.numelt - 1;
			rrand = r_ab - r_ac * r_bc / java.lang.Math.sqrt(1 - r_ac * r_ac) * java.lang.Math.sqrt(1 - r_bc * r_bc);
			if (fabs(rrand - p.coef) <= epsilon * fabs(rrand)) {
				cptega += 1;
			} else {
				if (rrand > p.coef) {
					cptsup += 1;
				}
				if (rrand < p.coef) {
					cptinf += 1;
				}
			}
			i = n - 1;
			while (ord[i] > ord[i + 1]) {
				i--;
			}
			j = n;
			while (ord[i] > ord[j]) {
				j--;
			}

			if (i >= 0) {
				temp = (int) ord[i];
				ord[i] = ord[j];
				ord[j] = temp;
				r = n;
				s = i + 1;
				while (r > s) {
					temp = (int) ord[r];
					ord[r] = ord[s];
					ord[s] = temp;
					r--;
					s++;
				}
			}
		}
		if (p.coef < 0) {
			p.proba = (double) (cptinf + cptega) / cpt;
		} else {
			p.proba = (double) (cptsup + cptega) / cpt;
		}
		//free (ord );
		return 1;

	}

	//added functions
	private double fabs(double in) {
		return java.lang.Math.abs(in);
	}
}
