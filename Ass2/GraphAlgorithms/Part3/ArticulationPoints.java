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
    private static int time = 0;
    private static Stop start;

    public static Set<Stop> findArticulationPoints(Graph graph) {
        Set<Stop> articulationPoints = new HashSet<>();
        Map<Stop, Integer> discovery = new HashMap<>();
        Map<Stop, Integer> low = new HashMap<>();
        Set<Stop> visited = new HashSet<>();

        for (Stop node : graph.getStops()) {
            if (!visited.contains(node)) {
                start = node;
                findArticulationPointsHelper(node, null, discovery, low, visited, articulationPoints);
            }
        }

        return articulationPoints;
    }

    private static void findArticulationPointsHelper(Stop u, Stop parent, Map<Stop, Integer> discovery,
                                                     Map<Stop, Integer> low, Set<Stop> visited,
                                                     Set<Stop> articulationPoints) {
        visited.add(u);
        discovery.put(u, time);
        low.put(u, time);
        time++;
        int children = 0;

        for (Stop v : u.getNeighbors()) {
            if (v.equals(parent)) {
                continue; // Skip the parent in the DFS tree
            }
            if (!visited.contains(v)) {
                children++;
                findArticulationPointsHelper(v, u, discovery, low, visited, articulationPoints);
                low.put(u, Math.min(low.get(u), low.get(v)));

                // Check if u is an articulation point
                if ((u.equals(start) && children > 1) || (!u.equals(start) && low.get(v) >= discovery.get(u))) {
                    articulationPoints.add(u);
                }
            } else {
                low.put(u, Math.min(low.get(u), discovery.get(v)));
            }
        }
    }
}





