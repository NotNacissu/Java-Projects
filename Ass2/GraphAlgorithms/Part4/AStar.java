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
     * @return
     */
    public static List<Edge> findShortestPath(Stop start, Stop goal) {
        if (start == null || goal == null) {
            return null;
        }
        
        // Initialize data structures
        PriorityQueue<PathItem> fringe = new PriorityQueue<>(); // Priority queue
        Map<Stop, Edge> backpointers = new HashMap<>(); // Backpointers
        Set<Stop> visited = new HashSet<>(); // Visited stops
        
        // Put start node onto the fringe
        fringe.add(new PathItem(start, null, 0.0, heuristic(start, goal)));
        
        // Main A* loop
        while (!fringe.isEmpty()) {
            PathItem current = fringe.poll();
            Stop node = current.getStop();
            Edge edge = current.getEdge();
            double lengthToNode = current.getLengthSoFar();
            
            // Check if node is not visited
            if (!visited.contains(node)) {
                visited.add(node);
                backpointers.put(node, edge);
                
                // Check if the goal is reached
                if (node.equals(goal)) {
                    return reconstructPath(start, goal, backpointers);
                }
                
                // Iterate over neighbors
                for (Edge neighEdge : node.getEdges()) {
                    Stop neighbor = neighEdge.toStop();
                    
                    // Check if neighbor is not visited
                    if (!visited.contains(neighbor)) {
                        double lengthToNeighbor = lengthToNode + edgeCost(neighEdge);
                        double estimateTotalPath = lengthToNeighbor + heuristic(neighbor, goal);
                        fringe.add(new PathItem(neighbor, neighEdge, lengthToNeighbor, estimateTotalPath));
                    }
                }
            }
        }
        
        // No path found
        return null;
    }

    /** 
     * Reconstructs the shortest path from start to goal using backpointers.
     */
    private static List<Edge> reconstructPath(Stop start, Stop goal, Map<Stop, Edge> backpointers) {
        List<Edge> path = new ArrayList<>();
        Stop current = goal;
        
        while (!current.equals(start)) {
            Edge edge = backpointers.get(current);
            path.add(edge);
            current = edge.fromStop();
        }
        
        Collections.reverse(path);
        return path;
    }

    /** 
     * Returns the heuristic estimate of the cost to get from a stop to the goal.
     */
    private static double heuristic(Stop current, Stop goal) {
        return current.distanceTo(goal) / Transport.TRAIN_SPEED_MPS; // Using the distance between geographical points as the heuristic
    }
    
    /** Return the cost of traversing an edge in the graph */
    public static double edgeCost(Edge edge){
        return edge.time();
    }
    

    
}
