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
                node.setDepth(0); // visit start
                // Explore neighbors of the current node
                for (Stop neighbour : node.getNeighbors().keySet()) {
                    if (neighbour.getDepth() == -1) { // not visited yet
                        recArtPts(neighbour, 1, node, aPoints);
                        numSubtrees++;
                    }
                }
                if (numSubtrees > 1) {
                    articulationPoints.add(node);
                }
                articulationPoints.addAll(aPoints);
            }
        }
        return articulationPoints;
    }

    private static int recArtPts(Stop node, int depth, Stop fromNode, Set<Stop> articulationPoints) {
        node.setDepth(depth); // visit node
        int reachBack = depth; // how far up this node can reach

        // Explore neighbors of the current node
        for (Stop neighbour : node.getNeighbors().keySet()) {
            if (neighbour.equals(fromNode)) {
                continue;
            } else if (neighbour.getDepth() != -1) { // already visited
                reachBack = Math.min(neighbour.getDepth(), reachBack);
            } else {
                int childReach = recArtPts(neighbour, depth + 1, node, articulationPoints);
                if (childReach >= depth) {
                    articulationPoints.add(node);
                }
                reachBack = Math.min(childReach, reachBack);
            }
        }

        return reachBack;
    }
}

    








