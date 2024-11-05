import ilog.concert.IloException;
import ilog.cplex.IloCplex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IloException {

        File fileToRead = new File("lib/MCFInstanceLarge.txt");

        try {

            /**
             * Check the Instance
             */
            Instance test = Instance.read(fileToRead);
//            System.out.println("Number of Nodes:");
//            System.out.println(test.getNumberOfNodes());
//            System.out.println("Number of Arcs");
//            System.out.println(test.getNumberOfArcs());
//            System.out.println("Number of Commodoties");
//            System.out.println(test.getNumberOfCommodities());
//            System.out.println("Cost on Arcs");
//            System.out.println(test.getCostOnArcs());
//            System.out.println("Capacities on Arcs");
//            System.out.println(test.getCapacityOnArcs());
//            System.out.println("Hashmap b");
//            System.out.println(test.getB().get(0));
//            System.out.println(test.getB().get(1));
//            System.out.println(test.getB().get(2));
//            System.out.println("Matrix D");
//            System.out.println(test.getD());
//            System.out.println(test.getCapacityOnArcs().get(1));


            /**
             * Code for the small Instance, different than for the large Instance since you need to keep track of different information for the report.
             */
            //Some initializations of thinks i want to keep track of!
//            List<Double> solution = new ArrayList<>();
//            List<Double> solutionListPerIteration = new ArrayList<>();
//            Map<Integer, List<List<Double>>> testMap = new LinkedHashMap<>();
//            boolean optimal = false;
//            for (int iteration = 0; iteration < 10; iteration++) {
////                System.out.println("Optimal solution found? " + optimal);
////                System.out.println(testMap);
//                if (optimal) {
//                    System.out.println("Optimal after iteration: " + ( iteration - 1 ));
//                    break;
//                }
//                RMP_V2 model = new RMP_V2(test, testMap);
//                model.solve();
//                solution = model.getSolution();
//                System.out.println("Iteration: " + iteration);
//                System.out.println(model.getObjective());
//
//                for (int numberSP = 1; numberSP < test.getNumberOfCommodities() + 1; numberSP++) {
//                    SubProblem SP = new SubProblem(test, model.getDuals(), testMap, numberSP, iteration);
//                    SP.solve();
////
//                    if (SP.getReducedCost() < 0.0) {
//                        System.out.println("Reduced Cost: "+SP.getReducedCost());
//                        System.out.println("Extreme Direction: "+SP.getSolution());
//                        System.out.println("Commodity: "+numberSP);
//
//                        testMap = SP.getModifiedVarInfo();
//                        break;
//                    }
//                    if (numberSP == test.getNumberOfCommodities()) {
//                        optimal = true;
//                    }
//                }
//            }
////                System.out.println(solutionListPerIteration);
//
////            System.out.println(solutionListPerIteration);
////            }
////            System.out.println(solution);
//                /**
//                 * This part is meant to retrieve the flowpaths of each commodity. Eventually the flow is stored in the solutionMap which contains the amount of flow of each commodity over each arc.
//                 */
//            List<List<Double>> solutionFlows = new ArrayList<>();
//
//            for (int j = 0; j< testMap.size(); j++)
//            {
//                List<Double> dummy = new ArrayList<>();
//                for (int k =0; k< (test.getNumberOfArcs());k++)
//                {
//                    dummy.add(solution.get(test.getNumberOfArcs()+test.getNumberOfCommodities()+(j))*testMap.get(j).get(3).get(k));
//                }
//                solutionFlows.add(dummy);
//            }
//
//            Map<Integer, List<Double>> solutionMap= new LinkedHashMap<>();
//            ArrayList<Double> dummy = new ArrayList<>(Collections.nCopies(test.getNumberOfArcs(), 0.0));
//            for (int p=0;p< test.getNumberOfCommodities(); p++)
//            {
//                solutionMap.put(p,dummy);
//            }
//
//
//            for (int i = 0; i< testMap.size(); i++)
//            {
//                int commodity;
//                List<Double> partialSolution;
//                partialSolution= solutionFlows.get(i);
//                commodity = testMap.get(i).get(1).indexOf(1.0);
//                List<Double> result = new ArrayList<>();
//                for (int j = 0; j<test.getNumberOfArcs();j++)
//                {
//                    double addition = solutionMap.get(commodity).get(j) + partialSolution.get(j);
//                    result.add(addition);
//                }
//                solutionMap.put(commodity,result);
//            }
//
//            System.out.println(solutionMap);


////////////////////////////////////        LARGE FILE                ///////////////////////////////////////////////

            /**
             * Code for the Large Instance, different than for the small Instance since you need to keep track of different information for the report.
             */
            //Some initializations of thinks i want to keep track of!
//            List<Double> solution = new ArrayList<>();
            List<Double> solutionListPerIteration = new ArrayList<>();
            Map<Integer, List<List<Double>>> testMap = new LinkedHashMap<>();
//            boolean optimal = false;
//            long start = System.currentTimeMillis();
//            long end = start + 5*1000;
//            while (System.currentTimeMillis() < end) {
            // Some expensive operation on the item.

            for (int iteration = 0; iteration < 150; iteration++) {

                RMP_V2 model = new RMP_V2(test, testMap);
                model.solve();
//                solution = model.getSolution();
                solutionListPerIteration.add(model.getObjective());
                System.out.println(model.getObjective());
//                System.out.println("Iteration: " + iteration + "objec: " + model.getObjective());

//                List<Double> RC = new ArrayList<>();
                for (int numberSP = 1; numberSP < test.getNumberOfCommodities() + 1; numberSP++) {
                    SubProblem SP = new SubProblem(test, model.getDuals(), testMap, numberSP, iteration);
                    SP.solve();

                    if (SP.getReducedCost() < 0.0) {
                        testMap = SP.getModifiedVarInfo();
                        break;
                    }
                    SP.cleanUp();
                }
                model.cleanUp();
            }
            FileWriter writer = new FileWriter("output.txt");
            for (Double dou : solutionListPerIteration) {
                writer.write(dou + System.lineSeparator());
            }
            writer.close();
        }

        catch( FileNotFoundException ex)
            {
                System.out.println("There was an error reading file " + fileToRead);
                ex.printStackTrace();
            } catch (IOException e) {
            e.printStackTrace();
        }

    }


    }

