import ilog.concert.IloException;
import ilog.cplex.IloCplex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IloException, FileNotFoundException {
    	for(int i=1; i<=10; i++) {
//			File fileToRead = new File("lib/test_instance.txt");
			File fileToRead = new File("lib/instance" + i + ".txt");

			KnapsackInstance instance = KnapsackInstance.read(fileToRead);

			System.out.println("INSTANCE"+ i + ":");
			DynamicProgram DP = new DynamicProgram(instance);
			System.out.println("Objective value of the memory efficient Dynamic programming algorithm "+ DP.knapsackDPMemoryEfficient());
		}


	}
}
