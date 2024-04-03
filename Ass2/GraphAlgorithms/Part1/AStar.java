/**
 * Implements the A* search algorithm to find the shortest path
 *  in a graph between a start node and a goal node.
 * If start or goal are null, it returns null
 * If start and goal are the same, it returns an empty path
 * Otherwise, it returns a Path consisting of a list of Edges that will
 * connect the start node to the goal node.
 */

import java.util.Collections;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;


public class AStar {
   
    /**
     * Finds the shortest path between two stops
     *"distance"


    /**
     * A Star search algorithm to find the shortest path between two stops
     * @param start
     * @param goal
     * @param timeOrDistance
     * @return
     */
    public static List<Edge> findShortestPath(Stop start, Stop goal) {
        if (start == null || goal == null ) {
            System.out.println("Start or goal is null. Cannot find path.");
            return null;}
        // Initialize data structures
        PriorityQueue <PathItem> queue = new PriorityQueue<>(); // priority queue
        Set<Stop> visited = new HashSet<>(); // visited stops
        Map<Stop, Edge> backpointers = new HashMap<>(); // backpointers
        
        // Initialize start node
        PathItem startItem = new PathItem(start, null, 0.0, heuristic(start, goal));
        queue.add(startItem);
        
        while (!queue.isEmpty()) {
            PathItem current = queue.poll(); // path item - lowest estimated cost
            
            // Goal check
            if (current.getStop().equals(goal)) {
                // Reconstruct the path
                List<Edge> path = new ArrayList<>();
                Stop stop = goal;
                
                while (!stop.equals(start)) {
                    Edge edge = backpointers.get(stop);
                    path.add(edge);
                    stop = edge.fromStop();
                }
                
                Collections.reverse(path);
                System.out.println("Shortest path found: " + path);
                return path;
            } 
            
            if(visited.contains(current.getStop())) { // check if visited stop
                continue;
            }
            
            // Mark current stop as visited
            visited.add(current.getStop());
            
            // Iterate over neighbors (edges from current stop)
            for (Edge edge : current.getStop().getEdges()) {
                 
                Stop neighbor = edge.toStop();
                if (visited.contains(neighbor)) {
                    continue;
                }
                
                double newLength =  current.getLengthSoFar() + edgeCost(edge);; //  calc cost - new path
                double newEstimate = newLength + heuristic(neighbor, goal); // Calc estcosts - new path
                // Add neighbor to the queue with updated value
                System.out.println("Neighbor: " + neighbor + ", new length: " + newLength + ", new estimate: " + newEstimate);
                PathItem neighborItem = new PathItem(neighbor, null, newLength, newEstimate);
                queue.add(neighborItem);
                // Update backpointer for the neighbor
                backpointers.put(neighbor, edge);
            } 
        }
        System.out.println("No path found.");
        return null; // No path found
    }

    /** 
     * Return the heuristic estimate of the cost to get from a stop to the goal 
     */
    public static double heuristic(Stop current, Stop goal) {
        return current.distanceTo(goal); // Using the distance between geographical points as the heuristic
    }
 

    public static double edgeCost(Edge edge) {
        return edge.distance();
    }
    
    
}
