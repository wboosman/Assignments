import ilog.concert.IloException;
import ilog.cplex.IloCplex;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IloException {
        try {
            // Read the bin packing instance
            ProductionInstance pi = ProductionInstance.read(new File("lib/instance_3.txt"));
//            System.out.println(pi.getNumberPeriods());
//            System.out.println(pi.getFixProduction());
//            System.out.println(pi.getVarProduction());
//            System.out.println(pi.getFixHolding());
////            System.out.println(pi.getVarHolding());
//            System.out.println(pi.getProdSpecifics().get("Demand"));
//            int sum = pi.getProdSpecifics().get("Demand").stream()
//                    .mapToInt(a -> a)
//                    .sum();
//            System.out.println(sum);
//
//\
//// ------------------------------------ ILP -----------------------------------------
//            long start =  System.currentTimeMillis();
//            ModelILP model =new  ModelILP(pi);
//            model.solve();
////            long end =  System.currentTimeMillis();
////
////            System.out.println(model.getSolution());
////////            System.out.println(model.);
////////            double sum2 = model.getSolution().get("ProductionPlan").stream().mapToDouble(a -> a).sum();
////////            System.out.println(sum2);
//////            System.out.println("Run Time: " + (end -start));
//            System.out.println("Objective MILP:" + model.getObjective());
////            System.out.println("Lower Bound: " + model.getLowerBound());
//            System.out.println("Solution MILP: " + model.getSolution());
//            HashMap<String, List<Integer>> myMap= new HashMap<>();
//            List<Integer> myList = new ArrayList<>();
//
//            for (double d: model.getSolution().get("Production") ){
//                int i = (int) Math.round(d);
//                myList.add(i);
//            }
//        myMap.put("Production" , myList);
//            myList = new ArrayList<>();
//            for (double d: model.getSolution().get("Inventory") ){
//                int i = (int) Math.round(d);
//                myList.add(i);
//            }
//            myMap.put("Inventory" , myList);
//            toTxtFile(myMap);
//            System.out.println(model.getSpentPerPeriod(model.getSolution()));


// --------------------------------------- Heuristic ----------------------------------
//            long start =  System.currentTimeMillis();
            Heuristic heuristic = new Heuristic(pi);
            toTxtFile(heuristic.heuristicSolution());
            System.out.println(heuristic.heuristicSolution());


//            System.out.println("Objective :" + heuristic.getHeuristicObjective());
//            long end =  System.currentTimeMillis();
//            System.out.println("Run Time: " + (end -start));
//            System.out.println("Objective :" + heuristic.getHeuristicObjective());


//           int x=0;
//           for (int x = 0 ; x< pi.getNumberPeriods(); x++){
//               System.out.println(x);
//
//           }
//            System.out.println(pi.getNumberPeriods());
//
//           int w = Integer.MIN_VALUE;
//           int z= Integer.MIN_VALUE;
//            System.out.println(Math.max(w,z));
//            DynamicProgram dp = new DynamicProgram()


// ----------------------------------------------- DP -------------------------------------
//            long start = System.currentTimeMillis();
////            DP2 dp2 = new DP2(pi, 1850);
//            DynamicProgram dp2 = new DynamicProgram(pi, 3000);
//
//            dp2.maxInventoryDP(dp2.initialization());
////            System.out.println(dp.initialization());
////            System.out.println(pi.getProdSpecifics().get("Demand").get(0));
////            System.out.println(pi.getProdSpecifics().get("Capacity").get(0) - pi.getProdSpecifics().get("Demand").get(0));
//            System.out.println(dp2.getObj());
//            long end = System.currentTimeMillis();
//            System.out.println(end-start);
//            System.out.println(dp2.DPSolution());
//            System.out.println(dp.getDpVars());
            // ----------------------------------------------- DP WITH FACTOR -------------------------------------
//            int F= 16;
//            long start = System.currentTimeMillis();
//            DPMEFACTORS dp = new DPMEFACTORS(pi, 52876,F );
//            dp.maxInventoryDP(dp.initialization());
//            long end = System.currentTimeMillis();
//            System.out.println("Objective, first b for which final inventory  >= 0 : " + dp.getObj());
//            System.out.println("Solution: " + dp.DPSolution2() );
////            System.out.println("Actual Cost (Upper Bound): " + dp.getActualCost(dp.DPSolution2()) );
//
//            System.out.println("Running time: " + (end-start));
//            System.out.println("Lower Bound: " + (dp.getObj()-(pi.getNumberPeriods()*F)));
//            toTxtFile(dp.DPSolution2());
//            List<Integer> myList = Arrays.asList(0,5,10,15,20,25,30,35);
//            System.out.println(dp.getA(24000));


////            sou\
//            System.out.println(dp.initialization());

            //------------------------------------Test -----------------------------
//            for (int i= 9; i> 0; i--){
//                System.out.println(i);
//            }
//            int b =20;
//            System.out.println(myList);
////            List<Integer> A =new ArrayList<>();
//            List rList = new ArrayList<>();
//            for (int i : myList){
//                rList.add(0,i);
//            }
//            System.out.println(rList);
//            rList.remove(rList.size()-1);
//            System.out.println(rList);
//            System.out.println(A);
//            List<Integer> A2 = new ArrayList<>();
//            myList.stream().filter( i -> i <= b).forEach(A2::add);
//            System.out.println(A2);
//
//            int B2 =( (Math.floorDiv(100, 10) + 11) *10 );
//            int check = 0;
//            int it = 0;
//            List<Integer> test = new ArrayList<>();
//            while(check <= B2)
//            {
//                test.add(check);
//                it++;
//                check = it * 10;
//            }
//            System.out.println(myList.indexOf(10));
//            System.out.println(B2);
//            System.out.println(test.size());
//            for (int u = 0; u < test.size(); u++){
//                System.out.println(u);
//                System.out.println(test.get(u));
//            }
//
//            System.out.println(test.indexOf(10));





        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void toTxtFile(HashMap<String, List<Integer>> input) throws IOException {
        File output = new File("solution_heur.txt");
        FileWriter fw = new FileWriter(output);
        PrintWriter pw = new PrintWriter(fw);
        for (int i = 0; i < input.get("Production").size(); i++) {
            for (String s : input.keySet()) {
                if (s.equals("Production")) {
                    pw.print(input.get("Production").get(i));
                    pw.print("  ");
                } else {
                    pw.print(input.get("Inventory").get(i));
                    pw.print("  ");
                }
            }
            pw.println();
        }
        pw.close();
    }

}

