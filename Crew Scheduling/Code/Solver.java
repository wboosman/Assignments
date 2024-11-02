import ilog.concert.IloException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Solver
{
    private List<Double> finalSolution;
    private Duties finalDuties;
    private double finalObjective;
    private long runRMP;
    private long runPricing;

    public Solver() {
        this.finalSolution = new ArrayList<>();
        this.finalDuties = new Duties();
        this.finalObjective =0 ;
        this.runPricing = 0;
        this.runRMP = 0;
    }

    public long getRunRMP() {
        return runRMP;
    }

    public long getRunPricing() {
        return runPricing;
    }

    public List<Double> getFinalSolution() {
        return finalSolution;
    }

    public Duties getFinalDuties() {
        return finalDuties;
    }

    public double getFinalObjective() {
        return finalObjective;
    }


    //Addition Added AllArcs
    public void solve(HashMap<Integer , Graph> megaGraph, Duties duties, HashMap<Integer, Task> allTask, HashMap<Integer, List<Task>> childrenList, List<Integer> originList){
        try
        {
            int iteration = 0;
            double Obj = 0.0;
            while(true)
            {
                System.out.println("Iteration: " + iteration);
                long start = System.currentTimeMillis();
                RestrictedMasterModel rmp = new RestrictedMasterModel(duties , new ArrayList<>());
                // We solve the model and print the objective value.
                rmp.solve();
                long end = System.currentTimeMillis();
                runRMP = runRMP + (end - start);

                System.out.println("Restricted master problem: " + rmp.getObjective());
//                System.out.println("Restricted master solition: " + rmp.getSolution());
                HashMap<String, List<Double>> duals = rmp.getDuals();
//                System.out.println(duals);
                finalObjective = rmp.getObjective();
                finalSolution = rmp.getSolution();
                rmp.cleanup();
//                System.out.println(duals.get("maxMeanLabourConstraint"));
//                System.out.println(duals.get("presenceConstraints"));
//                previousObj = rmp.getObjective();
//                rmp.cleanup();

                long start2 = System.currentTimeMillis();

                PricingProblem pp = new PricingProblem(megaGraph, allTask, childrenList, originList, duals);

                long end2 = System.currentTimeMillis();
                runPricing = runPricing +(end2 - start2);

                boolean betterDutyFound = false;
                // We solve the model and print the objective value.
                System.out.println("Lowest reduced cost: " + pp.getMinimumRC());
//                System.out.println("Added duty:" + pp.getNewDuty());
                if (pp.getMinimumRC() < -0.0001)
                {
                    betterDutyFound = true;
                    duties.addDuty(pp.getNewDuty());
                }

                if(!betterDutyFound)
                {
//                    finalDuties = duties;
                    break;
                }
                iteration++;
            }
            finalDuties=duties;

        }
        catch (IloException e)
        {
            e.printStackTrace();
        }
    }


}
