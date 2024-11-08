import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts a the pricing problem into a mathematical programming model managed by CPLEX.
 * @author Wessel Boosman
 */
public class PricingModel {
    private List<Double> dualVars;
    private InitialPacking instance;
    private BinPackingInstance instanceBin;
    private IloCplex cplex;
    private Map<Integer, IloNumVar> varMap;
    private Map<Integer, IloRange> constraints;


    /**
     *
     * @param instanceBin Bin instance which returns itemsizes and bin size
     * @param instance    Instance with packings
     * @param dualVars    Vector of dual variables needed to solve the problem
     * @throws IloException
     */

    public PricingModel(BinPackingInstance instanceBin, InitialPacking instance, List<Double> dualVars) throws IloException
    {
        // Initialize the instance variables
        this.instanceBin = instanceBin;
        this.instance = instance;
        this.cplex = new IloCplex();
        // Create a map to link items to variables
        this.varMap = new LinkedHashMap<>();
        this.constraints = new LinkedHashMap<>();
        this.dualVars = dualVars;
        // Initialize the model. It is important to initialize the variables first!
            addVariables();
            addKnapSackConstraint();
            addObjective();

        // Optionally: export the model to a file, so we can check the mathematical
        // program generated by CPLEX
        cplex.exportModel("PricingModel1.lp");
//        // Optionally: suppress the output of CPLEX
//        cplex.setOut(null);
//        cleanup();

    }


//Constraints of the pricing problem, bin volume can not be exceeded
    private void addKnapSackConstraint() throws IloException {
        IloNumExpr lhs = cplex.constant(0);
        for (int i =0; i<instance.getNumItems(); i++)
        {
            IloNumExpr term = cplex.prod(instanceBin.getItems().get(i), varMap.get(i));
        // Add the term to the left hand side summation
        lhs = cplex.sum(lhs, term);
        }
        cplex.addLe(lhs, instanceBin.getBinSize());
    }

    private void addVariables() throws IloException {
        for (int i =0; i<instance.getNumItems(); i++)
        {
            IloNumVar var = cplex.boolVar();
            varMap.put(i, var);
        }

    }
//Objective of the pricing model = Reduced cost
    private void addObjective() throws IloException
    {
        // Initialize the objective sum to 0
        IloNumExpr obj = cplex.constant(0);
        for (int i =0; i< instance.getNumItems(); i++)
        {
            IloNumVar var = varMap.get(i);
            double dual = dualVars.get(i);
            IloNumExpr product = cplex.prod(dual, var);
            obj = cplex.sum(obj, product);
        }
        cplex.addMaximize(obj);
    }

    public void solve() throws IloException
    {
        cplex.solve();
    }

    public double getObjective() throws IloException
    {
        return cplex.getObjValue();
    }

    //Get optimal packing
    public List<Integer> getPacking() throws IloException
    {
        List<Integer> result = new ArrayList<>();
        for (int i=0; i< instance.getNumItems();i++)
//        for (int i = 0; i<instance.getInitialPackings().size(); i++)
        {
            IloNumVar var = varMap.get(i);
            double value = cplex.getValue(var);
            if (value >= 0.5)
            {
                result.add(i);
            }
        }
        return result;
    }
}
