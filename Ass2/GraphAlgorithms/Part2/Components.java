import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

//=============================================================================
//   Finding Components
//   Finds all the strongly connected subgraphs in the graph
//   Constructs a Map recording the number of the subgraph for each Stop
//=============================================================================

public class Components{

    // Use Kosaraju's algorithm.
    // In the forward search, record which nodes are visited with a visited set.
    // In the backward search, use the setSubGraphId and getSubGraphID methods
    // on the stop to record the component (and whether the node has been visited
    // during the backward search).
    // Alternatively, during the backward pass, you could use a Map<Stop,Stop>
    // to record the "root" node of each component, following the original version
    // of Kosaraju's algorithm, but this is unnecessarily complex.

    
    public static void findComponents(Graph graph) {
        System.out.println("calling findComponents");
        graph.resetSubGraphIds();
        //int componentNum = 0;

        //for (Stop stop : graph.getStops()) {
        //   stop.setSubGraphId(9);
        //}

        int componentNum = 0;

        List<Stop> nodeList = new ArrayList<>(); // order stops visited 
        Set<Stop> visited = new HashSet<>(); // stops visited
        
        for (Stop stop : graph.getStops()) { // search from unvisited stops
            
            if (!visited.contains(stop)) {
                forwardVisit(stop, nodeList, visited);
            }
        }
    
        //for (Stop stop : nodeList) {
        for(int i = nodeList.size() -1; i >= 0; i--){ // search from each stop in reverse
            Stop stop = nodeList.get(i);
            
            if (stop.getSubGraphId() == -1) { // if not assigned 
                backwardVisit(stop, componentNum);
                componentNum++;  
            }
        }
        graph.setSubGraphCount(componentNum);  // subgraph count - number found
    }
    
    private static void forwardVisit(Stop stop, List<Stop> nodeList, Set<Stop> visited) {
        if (!visited.contains(stop)) {
            visited.add(stop);
            
            for (Edge e : stop.getOutEdges()) { //all stops reachable
                Stop s = e.toStop();
                forwardVisit(s, nodeList, visited);
            }
            nodeList.add(stop); // add stop - front of list
        }
    }
    
    private static void backwardVisit(Stop stop, int componentNum) {
        if (stop.getSubGraphId() == -1) {
            stop.setSubGraphId(componentNum); // stop assigned to component
            
            for (Edge e : stop.getInEdges()) { //stops reachable - stop
                Stop s = e.fromStop();
                backwardVisit(s, componentNum);
            }
        }
    }
}    