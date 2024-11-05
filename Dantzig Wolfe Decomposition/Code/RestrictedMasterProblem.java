import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;
import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RestrictedMasterProblem {
    private Instance instance;
    private IloCplex cplex;
    private Map<Integer, IloNumVar> varMap;
    private Map<Integer, IloRange> constraints;
    private Map<Integer, List<List<Double>>> addedVarInfo;

    public RestrictedMasterProblem(Instance newInstance) throws IloException {
        this.instance = newInstance;
        this.cplex = new IloCplex();
        this.constraints = new LinkedHashMap<>();
        this.varMap = new LinkedHashMap<>();
        addVariables();
        addConstraints();
        addObjective();
        cplex.setOut(null);
    }

    // Initialize, s equals the capacity, t equals minimal 1
    private void addConstraints() throws IloException {
        for (int i = 0; i < varMap.size(); i++) {
            IloNumVar variable = varMap.get(i);
            IloRange constraint;
            if (i < instance.getNumberOfArcs()) {
                constraint = cplex.addEq(instance.getCapacityOnArcs().get(i), variable);
            } else {
                constraint = cplex.addEq(1, variable);
            }
            constraints.put(i, constraint);
        }
    }



        private void addObjective() throws IloException {
        IloNumExpr obj = cplex.constant(0);
        double M = 1000;
        int dummy=0;
        for (int i = 0; i < instance.getNumberOfCommodities(); i++) {
            //For the objective function you only need the last three variables.
            //The first ten are reserved for the capacity constraints
            IloNumVar var = varMap.get((instance.getNumberOfArcs() + i));
            IloNumExpr term = cplex.prod(var, M);
            obj = cplex.sum(obj, term);
        }
        cplex.addMinimize(obj);
    }

//    private void addObjective() throws IloException {
//        IloNumExpr obj = cplex.constant(0);
//        double M = 1000;
//        int dummy=0;
//
//        for (int i = 0; i < varMap.size(); i++) {
//            IloNumVar var = varMap.get(i);
//            IloNumExpr term;
//            if(i<10)
//            {
//                term = cplex.prod(var, dummy);
//            }
//            else
//                {
//                    term = cplex.prod(var, M);
//                }
//            obj = cplex.sum(obj, term);
//
//        }
//        cplex.addMinimize(obj);
//    }

    private void addVariables() throws IloException {
        for (int i = 0; i < ( instance.getNumberOfArcs() + instance.getNumberOfCommodities()); i++) {
            IloNumVar var = cplex.numVar(0, Double.MAX_VALUE);
            varMap.put(i, var);
        }

    }

    public void solve() throws IloException
    {
        cplex.solve();
    }

    public List<Double> getSolution() throws IloException {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < varMap.size(); i++)
        {
            IloNumVar var = varMap.get(i);
            double value = cplex.getValue(var);
            result.add(value);
        }
        return result;
    }

    public double getObjective() throws IloException
    {
        return cplex.getObjValue();
    }

    //Returns dual variables
    public List<Double> getDuals() throws IloException
    {
        List<Double> duals = new ArrayList<Double>();
        for (int i=0; i<constraints.size();i++  ){
            Double constraintDual = cplex.getDual(constraints.get(i));
            duals.add(constraintDual);
        }
        return duals;
    }
}


