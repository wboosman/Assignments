import java.util.*;
import java.util.stream.Collectors;

public class PricingProblem {
    private final HashMap<Integer, Task> allTask;
    private final HashMap<Integer, List<Task>> childrenList;
    public static HashMap<Integer , Graph> megaGraph;

    //    private Integer origin;                         //First Task in the graph, also the one connected to S.
    private final int fixCosts= 900;
    private  LinkedHashMap<Integer, List<Arc>> graph;
    private HashMap<Integer, Double> reducedCost;
    private List<Integer> newDuty;
    private final List<Integer> originList;
    private double minimumRC;
    private HashMap<String, List<Double>> dualMap;



    public PricingProblem(HashMap<Integer , Graph> megaGraph, HashMap<Integer, Task> allTask, HashMap<Integer, List<Task>> childrenList, List<Integer> originList, HashMap<String, List<Double>> dualMap) {
        this.allTask = allTask;
        this.megaGraph = megaGraph;
        this.childrenList = childrenList;
        this.dualMap = dualMap;
        this.newDuty = new ArrayList<>();
        this.reducedCost= new HashMap<>();
        this.originList = originList;
        getObjValue();

    }


    public LinkedHashMap<Integer, List<Arc>> getGraph() {
        return graph;
    }

    public List<Integer> neighborhood(int time, Task task){

        List<Integer> ListA = new ArrayList<>();
        for (Task i : allTask.values()){

            if (task.getEndTime() < i.getStartTime() && i.getEndTime()<= task.getStartTime()+time) {
                ListA.add(i.getTaskNumber());
            }
        }
        return ListA;
    }




    public void shortestPath(LinkedHashMap<Integer, List<Arc>> graph) {
        int numberOfNodes = graph.size();
        HashMap<Integer, Integer> predecessor = new HashMap<>();
        reducedCost = new HashMap<>();
        reducedCost.put(-1, 0.0);
        predecessor.put(-1,-1);

        for (int i : graph.keySet()) {
            if (reducedCost.get(i) != null) {
                List<Arc> adjacentArcs = graph.get(i);
                if (adjacentArcs != null) {
                    for (Arc arc : adjacentArcs) {
                        double newReducedCost = reducedCost.get(i) + arc.getPrice();
                        if (reducedCost.get(arc.getTo()) == null) {
                            reducedCost.put(arc.getTo(), newReducedCost);
                            predecessor.put(arc.getTo(), arc.getFrom());
                        } else {
                            if (newReducedCost < reducedCost.get(arc.getTo())){
                                reducedCost.put(arc.getTo(), newReducedCost);
                                predecessor.put(arc.getTo(), arc.getFrom());
                            }
                        }
                    }
                }
            }
        }
        List<Integer> getPath = new ArrayList<>();
        getPath.add(0,1000);
        int previous1 = 1000;
        boolean stop = false;
        while (true){
            if (predecessor.get(previous1) == null){
                getPath = new ArrayList<>();
                reducedCost.put(1000,1.0);
                break;
            }
            int previous2 = predecessor.get(previous1);
            if (previous1 == -1){
                break;
            }
            getPath.add(0, previous2);
            previous1 = previous2;
        }
        newDuty = getPath;
    }

    public List<Integer> getNewDuty() {
        return newDuty;
    }

    public double getMinimumRC() {
        return minimumRC;
    }



    public void getObjValue(){
        double lowestReducedCost = 0.0;
        List<Integer> lowestReducedCostPath= new ArrayList<>();
        //The minus two in the next line still is to debug. Making the final version should look at the exact structure
        List<Integer> duty = new ArrayList<>(Collections.nCopies(allTask.size() - 2, 0));

        for (int i : originList){
           Graph graph = megaGraph.get(i);
//            System.out.println(graph.getGraph().keySet());
           graph.updatePrices(dualMap, allTask, i);
//            System.out.println(graph.getGraph());
           shortestPath(graph.getGraph());

            if (reducedCost.get(1000) < lowestReducedCost){
                lowestReducedCost = reducedCost.get(1000);
                lowestReducedCostPath = newDuty;

            }
        }
        for(int d = 0; d < duty.size(); d++){
            if(lowestReducedCostPath.contains(d)){
                duty.set(d,1);
            }
        }
        if(lowestReducedCost < -0.00001){
            int lengthDuty = -allTask.get(lowestReducedCostPath.get(1)).getStartTime() + allTask.get(lowestReducedCostPath.get(lowestReducedCostPath.size()-2)).getEndTime();
            duty.add(0, lengthDuty);
            duty.add(0, fixCosts + lengthDuty);
        }

        newDuty = duty;
        minimumRC = lowestReducedCost;
    }

}

//    ------------------------------------------------------ ORIGINAL ----------------------------------------------------------------------
//    public PricingProblem(HashMap<Integer , Graph> megaGraph, HashMap<Integer, Task> allTask, HashMap<Integer, List<Task>> childrenList, List<Integer> originList, HashMap<String, List<Double>> dualMap) {
//        this.allTask = allTask;
//        this.megaGraph = megaGraph;
//        this.childrenList = childrenList;
//        this.dualMap = dualMap;
//        this.newDuty = new ArrayList<>();
//        this.reducedCost= new HashMap<>();
//        this.originList = originList;
//        getObjValue();
//
//    }
//
//
//    public LinkedHashMap<Integer, List<Arc>> getGraph() {
//        return graph;
//    }
//
//    public List<Integer> neighborhood(int time, Task task){
//
//        List<Integer> ListA = new ArrayList<>();
//        for (Task i : allTask.values()){
//
//            if (task.getEndTime() < i.getStartTime() && i.getEndTime()<= task.getStartTime()+time) {
//                ListA.add(i.getTaskNumber());
//            }
//        }
//        return ListA;
//    }
//
//
//
//
//    public void shortestPath(LinkedHashMap<Integer, List<Arc>> graph) {
//        int numberOfNodes = graph.size();
//        HashMap<Integer, Integer> predecessor = new HashMap<>();
//        reducedCost = new HashMap<>();
//        reducedCost.put(-1, 0.0);
//        predecessor.put(-1,-1);
//
//        for (int i : graph.keySet()) {
//            if (reducedCost.get(i) != null) {
//                List<Arc> adjacentArcs = graph.get(i);
//                if (adjacentArcs != null) {
//                    for (Arc arc : adjacentArcs) {
//                        double newReducedCost = reducedCost.get(i) + arc.getPrice();
//                        if (reducedCost.get(arc.getTo()) == null) {
//                            reducedCost.put(arc.getTo(), newReducedCost);
//                            predecessor.put(arc.getTo(), arc.getFrom());
//                        } else {
//                            if (newReducedCost < reducedCost.get(arc.getTo())){
//                                reducedCost.put(arc.getTo(), newReducedCost);
//                                predecessor.put(arc.getTo(), arc.getFrom());
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        List<Integer> getPath = new ArrayList<>();
//        getPath.add(0,1000);
//        int previous1 = 1000;
//        boolean stop = false;
//        while (!stop){
//            if (predecessor.get(previous1) == null){
//                getPath = new ArrayList<>();
//                reducedCost.put(1000,1.0);
//                break;
//            }
//            int previous2 = predecessor.get(previous1);
//            getPath.add(0, previous2);
//            previous1 = previous2;
//            if (previous1 == -1){
//                stop = true;
//            }
//        }
//        newDuty = getPath;
//    }
//
//    public List<Integer> getNewDuty() {
//        return newDuty;
//    }
//
//    public double getMinimumRC() {
//        return minimumRC;
//    }
//
//
//
//    public void getObjValue(){
//        double lowestReducedCost = 0.0;
//        List<Integer> lowestReducedCostPath= new ArrayList<>();
//        //The minus two in the next line still is to debug. Making the final version should look at the exact structure
//        List<Integer> duty = new ArrayList<>(Collections.nCopies(allTask.size() - 2, 0));
//
//        for (int i : originList){
//           Graph graph = megaGraph.get(i);
////            System.out.println(graph.getGraph().keySet());
//           graph.updatePrices(dualMap, allTask, i);
////            System.out.println(graph.getGraph());
//           shortestPath(graph.getGraph());
//
//            if (reducedCost.get(1000) < lowestReducedCost){
//                lowestReducedCost = reducedCost.get(1000);
//                lowestReducedCostPath = newDuty;
//
//            }
//        }
//        for(int d = 0; d < duty.size(); d++){
//            if(lowestReducedCostPath.contains(d)){
//                duty.set(d,1);
//            }
//        }
//        if(lowestReducedCost < -0.00001){
//            int lengthDuty = -allTask.get(lowestReducedCostPath.get(1)).getStartTime() + allTask.get(lowestReducedCostPath.get(lowestReducedCostPath.size()-2)).getEndTime();
//            duty.add(0, lengthDuty);
//            duty.add(0, fixCosts + lengthDuty);
//        }
//
//        newDuty = duty;
//        minimumRC = lowestReducedCost;
//    }
//
//}

//----------------------------------------------------------------------------END ORIGINAL -----------------------------------------------------------------------------------------


//    private void constructGraph(int origin) {
//        graph = new LinkedHashMap<>();
//        graph.put(-1, new ArrayList<>());
//        graph.put(origin, new ArrayList<>());
//        int maxLabour = 570;
//        List<Integer> neighborhoodOrigin = neighborhood(maxLabour, allTask.get(origin));
////        System.out.println(neighborhoodOrigin);
//        for (int i : neighborhood(maxLabour, allTask.get(origin))){
//            graph.put(i, new ArrayList<>());
//        }
//        graph.put(1000, new ArrayList<>());
//        int numberOfNodes = graph.size();
//
//        // First add arc from S to a certain starting platform A/B.
//        int lengthOrigin = allTask.get(origin).getEndTime()- allTask.get(origin).getStartTime();
//        int maxMeanLabour = 432;
//        double price = fixCosts - mu* maxMeanLabour + (1 + mu)*(lengthOrigin) - lambda.get(origin);
//        graph.get(-1).add(new Arc(-1, origin, price));
//
//        //Draw all arcs from the origin (is first task) toward all its children
//        List<Integer> end = new ArrayList<>();
//        for (Task i : childrenList.get(origin)) {
//            price = ( 1 + mu ) * ( i.getEndTime() - allTask.get(origin).getEndTime() ) - lambda.get(i.getTaskNumber());
//            graph.get(origin).add(new Arc(origin, i.getTaskNumber(), price));
//            end.add(i.getTaskNumber());
//            //If task i has end station same as start, then there
//            //must be the possibility to terminate the duty and return home.
//            if (i.getEndPlatform().equals(allTask.get(origin).getStartPlatform())) {
//                graph.get(i.getTaskNumber()).add(new Arc(i.getTaskNumber(), 1000, 0));
//            }
//        }
//
//            boolean stop = false;
//            while(!stop){
//                List<Integer> end2 = new ArrayList<>();
//                for(Integer i : end) {
//                    for(Task t : childrenList.get(i)){
//                        if(neighborhoodOrigin.contains(t.getTaskNumber()))
//                        {
//                            //If task t in neighborhood of A and a child from one of the tasks in the end list --> add arc
//                            price = (1 + mu)*(t.getEndTime()-allTask.get(i).getEndTime()) - lambda.get(t.getTaskNumber());
//                            graph.get(i).add(new Arc(i, t.getTaskNumber(), price));
//                            end2.add(t.getTaskNumber());
//
//                            //If the to part of the arc has as ending platform the same as the starting platform --> add arc
//                            if (t.getEndPlatform().equals(allTask.get(origin).getStartPlatform())) {
//                                graph.get(t.getTaskNumber()).add(new Arc(t.getTaskNumber(), 1000, 0));
//                            }
//                        }
//                    }
//                }
//                end = end2.stream()
//                        .distinct()
//                        .collect(Collectors.toList());
//
//                if (end.isEmpty()){
//                    stop = true;
//                }
//            }
//        }


//    static class Arc{
//        private final int from;
//        private final int to;
//        private final double price;
//
//        public Arc(int from, int to, double price) {
//            this.from = from;
//            this.to = to;
//            this.price = price;
//        }
//
//        public int getFrom() {
//            return from;
//        }
//
//        public int getTo() {
//            return to;
//        }
//
//        public double getPrice() {
//            return price;
//        }
//
//        @Override
//        public String toString() {
//            return "Arc{" +
//                    "from=" + from +
//                    ", to=" + to +
//                    ", price=" + price +
//                    '}';
//        }
//    }