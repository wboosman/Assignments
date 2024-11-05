import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DynamicProgram {

    private ProductionInstance instance;
    private int upperBound;
    private int obj;
//    private HashMap<Integer, List<Integer>> dpVars;
    private HashMap<Integer, List<Integer>> backTrack;


    public DynamicProgram(ProductionInstance instance, int upperBound) {
        this.instance = instance;
        this.upperBound = upperBound;

    }

    public Integer prodCost(int x){
        int cost = 0;

        if (x > 0) {
            cost = instance.getFixProduction() + (x * instance.getVarProduction());
        }
        return cost;
    }


    public Integer holdCost(int i){
        int cost = 0;

        if (i > 0) {
            cost =  instance.getFixHolding() + (i * instance.getVarHolding());
        }
        return cost;
    }


    public List<Integer> initialization(){
        List<Integer> initList = new ArrayList<>();
        for (int b = 0; b <= upperBound; b++){
            if(b < prodCost(instance.getProdSpecifics().get("Demand").get(0)) ){
                initList.add(Integer.MIN_VALUE);
            }
            else if( b>= (prodCost(instance.getProdSpecifics().get("Capacity").get(0)) + holdCost(instance.getProdSpecifics().get("Capacity").get(0) - instance.getProdSpecifics().get("Demand").get(0))))
            {
                initList.add(instance.getProdSpecifics().get("Capacity").get(0) - instance.getProdSpecifics().get("Demand").get(0));
            }
            else
                {
                    int x = 0;
                    while(prodCost(x) + holdCost(x -  instance.getProdSpecifics().get("Demand").get(0)) <= b)
                    {
                        x++;
                    }
                    x--;
                    initList.add(x - instance.getProdSpecifics().get("Demand").get(0));
                }
        }

        return initList;
    }



    public Integer maxInventoryDP(List<Integer> initialization){
        int[][] DPvar = new int[2][upperBound+1];
        List<Integer> init = initialization();
        for (int u = 0; u <= upperBound; u++){
            DPvar[0][u] = init.get(u);
        }

        HashMap<Integer, List<Integer>> backTrackMap = new HashMap<>();
        backTrackMap.put(0, init);

        for (int i = 1 ; i < instance.getNumberPeriods(); i++) {
            List<Integer> backtrackList = new ArrayList<>();


            if (i % 2 == 0) {
                for (int b = 0; b <= upperBound; b++) {

                List<Integer> F1 = new ArrayList<>();
                List<Integer> F2 = new ArrayList<>();

                for (int a = 0; a <= b; a++) {
                    // First make sure that if the the previous dp variable with the given a is infeasible, this one is as well....

                    if (DPvar[1][a] < 0) {
                        F1.add(Integer.MIN_VALUE);
                        F2.add(Integer.MIN_VALUE);
                    }
                    //Is this not the case -->
                    else {
                        // The following could also be achieved with an for loop and an if statement, in that case an extra list is needed. In the end, pick the highest ite fro the list. (Maybe better)
                        //Add a value to the F1 list.
                        int x = Math.max(0,instance.getProdSpecifics().get("Demand").get(i) - DPvar[1][a]) ;
                        if(prodCost(x) + holdCost(DPvar[1][a] + x - instance.getProdSpecifics().get("Demand").get(i)) > ( b - a )){
                            F1.add(Integer.MIN_VALUE);
                        }
                        else {
                            while (prodCost(x) + holdCost(DPvar[1][a] + x - instance.getProdSpecifics().get("Demand").get(i)) <= ( b - a ) && x <= instance.getProdSpecifics().get("Capacity").get(i)) {
                                x++;
                            }
                            if (x > 0) {
                                x--;
                            }
                            F1.add(DPvar[1][a] + x - instance.getProdSpecifics().get("Demand").get(i));
                        }
                        int It = 0;
                        if(DPvar[1][a] - instance.getProdSpecifics().get("Demand").get(i) < 1)
                        {
                            F2.add(Integer.MIN_VALUE);
                        }
                        else {
                            while (holdCost(It) <= ( b - a ) && It < DPvar[1][a] - instance.getProdSpecifics().get("Demand").get(i)) {
                                It++;
                            }
                            if (It > 0) {
                                It--;
                            }
                            F2.add(It);
                        }

                    }
                    }
                    // COMPARE/MERGE F1 LIST WITH F2 LIST AND GET MAXIMUM. KEEP TRACK FOR WHICH A THIS IS. DONT OVERWRTIE THAT VALUE. IN THE END YOU NEED AN B BY TIMEPERIODS MATRIX WITH A VALUES.
                    List<Integer> maxValComp = new ArrayList<>();
                    for(int z =0; z< F1.size(); z++){
                        int c1 = F1.get(z);
                        int c2 = F2.get(z);
                        maxValComp.add(Math.max(c1,c2));
                    }
                    int maxVal = Collections.max(maxValComp); // should return 7
                    int maxIdx = maxValComp.indexOf(maxVal);
                    DPvar[0][b] = maxVal;
                    backtrackList.add(maxIdx);
                }
            }

            else
            {
                for (int b = 0; b <= upperBound; b++) {

                    List<Integer> F1 = new ArrayList<>();
                    List<Integer> F2 = new ArrayList<>();

                    for (int a = 0; a <= b; a++) {
                        // First make sure that if the the previous dp variable with the given a is infeasible, this one is as well....

                        if (DPvar[0][a] < 0) {
                            F1.add(Integer.MIN_VALUE);
                            F2.add(Integer.MIN_VALUE);
                        }
                        //Is this not the case -->
                        else {
                            // The following could also be achieved with an for loop and an if statement, in that case an extra list is needed. In the end, pick the highest ite fro the list. (Maybe better)
                            //Add a value to the F1 list.
                            int x = Math.max(0,instance.getProdSpecifics().get("Demand").get(i) - DPvar[0][a]) ;
                            if(prodCost(x) + holdCost(DPvar[0][a] + x - instance.getProdSpecifics().get("Demand").get(i)) > ( b - a )){
                                F1.add(Integer.MIN_VALUE);
                            }
                            else {
                                while (prodCost(x) + holdCost(DPvar[0][a] + x - instance.getProdSpecifics().get("Demand").get(i)) <= ( b - a ) && x <= instance.getProdSpecifics().get("Capacity").get(i)) {
                                    x++;
                                }
                                if (x > 0) {
                                    x--;
                                }
                                F1.add(DPvar[0][a] + x - instance.getProdSpecifics().get("Demand").get(i));
                            }
                            int It = 0;
                            if(DPvar[0][a] - instance.getProdSpecifics().get("Demand").get(i) < 1)
                            {
                                F2.add(Integer.MIN_VALUE);
                            }
                            else {
                                while (holdCost(It) <= ( b - a ) && It < DPvar[0][a] - instance.getProdSpecifics().get("Demand").get(i)) {
                                    It++;
                                }
                                if (It > 0) {
                                    It--;
                                }
                                F2.add(It);
                            }

                        }
                    }

                    List<Integer> maxValComp = new ArrayList<>();
                    for(int z =0; z< F1.size(); z++){
                        int c1 = F1.get(z);
                        int c2 = F2.get(z);
                        maxValComp.add(Math.max(c1,c2));
                    }
                    Integer maxVal = Collections.max(maxValComp);
                    Integer maxIdx = maxValComp.indexOf(maxVal);
                    DPvar[1][b] = maxVal;
                    backtrackList.add(maxIdx);
                }
            }
            backTrackMap.put(i,backtrackList);
        }
        obj=0;
        if(instance.getNumberPeriods()%2==0){
            while(DPvar[1][obj] < 0 ){
                obj++;
            }
        }
        else
        {
            while(DPvar[0][obj] < 0 ){
                obj++;
            }
        }
        backTrack = backTrackMap;
        return obj;
    }



    public HashMap<String, List<Integer>> DPSolution(){
        HashMap<Integer,Integer> spent = new HashMap<>();
        HashMap<String, List<Integer>> result = new HashMap<>();
        int seek = obj;
        int periodBudget;
        int budgetToPrevPeriodsAlloc = 0;
        for (int i= (instance.getNumberPeriods()-1); i> 0; i--){
            int index = seek;
            budgetToPrevPeriodsAlloc = backTrack.get(i).get(index);
            periodBudget = seek - backTrack.get(i).get(index);
            seek = budgetToPrevPeriodsAlloc;
            spent.put(i, periodBudget);
        }
        spent.put(0, backTrack.get(1).get(budgetToPrevPeriodsAlloc));
        System.out.println(spent);


        List<Integer> production = new ArrayList<>();
        List<Integer> inventory = new ArrayList<>();
        int residualInv = 0;


        for (int i : spent.keySet()){
            for ( int x = 0 ; x <= instance.getProdSpecifics().get("Capacity").get(i); x++){
                if ((prodCost(x) + holdCost(x + residualInv - instance.getProdSpecifics().get("Demand").get(i))) == spent.get(i))
                {
                    production.add(x);
                    residualInv = x + residualInv - instance.getProdSpecifics().get("Demand").get(i);
                    inventory.add(residualInv);
                    break;
                }
            }
        }
        result.put("Production" , production);
        result.put("Inventory" , inventory);


        return result;
    }


    public int getObj() {
        return obj;
    }
}
