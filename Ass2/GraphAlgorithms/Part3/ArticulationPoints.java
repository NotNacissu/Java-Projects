import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

//=============================================================================
//   Finding Articulation Points
//   Finds and returns a collection of all the articulation points in the undirected
//   graph.
//   Uses the algorithm from the lectures, but modified to cope with a not completely
//   connected graph. For a not fully connected graph, an articulation point is one
//   that would break a currently connected component into two or more components
//============================================================================
public class ArticulationPoints {
    // Method to find articulation points in a graph
    public static Set<Stop> findArticulationPoints(Graph graph) {
        Set<Stop> articulationPoints = new HashSet<>();

        // Initialize depths of all nodes to -1
        for (Stop node : graph.getStops()) {
            node.setDepth(-1);
        }

        // Iterate over each node in the graph
        for (Stop node : graph.getStops()) {
            if (node.getDepth() == -1) {
                Set<Stop> aPoints = new HashSet<>();
                int numSubtrees = 0;
                node.setDepth(0); // Mark the start node as visited and set depth to 0
                // Explore neighbors of the current node
                for (Stop neighbour : node.getNeighbors().keySet()) {
                    if (neighbour.getDepth() == -1) { // If the neighbor hasn't been visited
                        // Recursively find articulation points
                        recArtPts(neighbour, 1, node, aPoints);
                        numSubtrees++; // Increment the number of subtrees
                    }
                }
                // If the number of subtrees is more than 1, then the node is an articulation point
                if (numSubtrees > 1) {
                    articulationPoints.add(node);
                }
                // Add articulation points found in the subtree rooted at 'node' to the result set
                articulationPoints.addAll(aPoints);
            }
        }
        return articulationPoints;
    }

    // Recursive function to find articulation points
    private static int recArtPts(Stop node, int depth, Stop fromNode, Set<Stop> articulationPoints) {
        node.setDepth(depth); // Mark the current node as visited and set its depth
        int reachBack = depth; // Initialize reachBack to the current depth

        // Explore neighbors of the current node
        for (Stop neighbour : node.getNeighbors().keySet()) {
            if (neighbour.equals(fromNode)) {
                continue; // Skip if the neighbor is the parent node
            } else if (neighbour.getDepth() != -1) { // If the neighbor has already been visited
                reachBack = Math.min(neighbour.getDepth(), reachBack); // Update reachBack
            } else {
                // Recursively find articulation points in the subtree rooted at the neighbor
                int childReach = recArtPts(neighbour, depth + 1, node, articulationPoints);
                // If the depth of the child is greater than or equal to the current depth,
                // then the current node is an articulation point
                if (childReach >= depth) {
                    articulationPoints.add(node);
                }
                // Update reachBack based on the child's reachBack value
                reachBack = Math.min(childReach, reachBack);
            }
        }

        return reachBack; // Return the reachBack value
    }
}


    








