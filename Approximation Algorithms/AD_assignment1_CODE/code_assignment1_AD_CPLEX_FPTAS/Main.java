import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ilog.concert.IloException;

public class Main {
	private static int n;
	private static int Q;
	private static int[] values;
	private static int[] weights;
	private static double[] epsilon = {10, 1, 0.1, 0};

	public static void main(String[] args) throws FileNotFoundException, IloException {
		for(int i=1; i<=10; i++) {
//			File fileToRead = new File("lib/test_instance.txt");
			File fileToRead = new File("lib/instance" + i + ".txt");
			read(fileToRead); 
//			System.out.println("Instance " + i + ":");
//
//			long start = System.currentTimeMillis();
//			knapsackBinary a = new knapsackBinary(n, Q, values, weights);
//			long aTime = System.currentTimeMillis();
//			System.out.println("A with running time: "+ (aTime-start) + " ms");
//			
//			dynamicProgramming b = new dynamicProgramming(n, Q, values, weights);
//			long bTime = System.currentTimeMillis();
//			System.out.println("B with running time: "+ (bTime-aTime) + " ms");

			for(double e : epsilon) {
				FPTAS c = new FPTAS(n, Q, values, weights, e);
			}
		}
	}

	private static void read(File fileToRead) throws FileNotFoundException {
		//Try to open the file and initialize the use of a so called scanner.
		try (Scanner s = new Scanner(fileToRead)) {
			n = s.nextInt();
			Q = s.nextInt();

			values = new int[n];
			weights = new int[n];
			for(int i=0; i<n; i++) {
				values[i] = s.nextInt();
			}
			for(int i=0; i<n; i++) {
				weights[i] = s.nextInt();
			}
		}
	}	
}