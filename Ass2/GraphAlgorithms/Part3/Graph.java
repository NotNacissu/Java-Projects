import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;


/**
 * Graph is the data structure that stores the collection of stops, lines and connections. 
 * The Graph constructor is passed a Map of the stops, indexed by stopId and
 *  a Map of the Lines, indexed by lineId.
 * The Stops in the map have their id, name and GIS location.
 * The Lines in the map have their id, and lists of the stopIDs an times (in order)
 *
 * To build the actual graph structure, it is necessary to
 *  build the list of Edges out of each stop
 * Each pair of adjacent stops in a Line is an edge.
 * We also need to create walking edges between every pair of stops in the whole
 *  network that are closer than walkingDistance.
 */
public class Graph {

    private Collection<Stop> stops;
    private Collection<Line> lines;
    private Collection<Edge> edges = new HashSet<Edge>();      // edges between Stops
    
    
    /**
     * Construct a new graph given a collection of stops and a collection of lines.
     * Remove any stops that are not on any lines since they cannot be accessed from anywhere.
     */
    public Graph(Collection<Stop> stps, Collection<Line> lns) {
        stops = new TreeSet<Stop>(stps);
        stops.removeIf((Stop s) -> s.getLines().isEmpty());
        
        lines = lns;

        createAndConnectEdges();
        computeNeighbors();
        
        printGraphData();   // you could uncomment this to help in debugging your code
    }


    /** Print out the lines and stops in the graph to System.out */
    public void printGraphData(){
        System.out.println("============================\nLines:");
        for (Line line : lines){
            System.out.println(line.getId()+ "("+line.getStops().size()+" stops)");
        }
        System.out.println("\n=============================\nStops:");
        for (Stop stop : stops){
            System.out.println(stop);
            //System.out.println("  "+stop.getEdges().size()+"  out edges; ");
        }
        System.out.println("===============");
    }


    //============================================
    // Build the graph structure.
    //============================================

    /** 
     * From the loaded Line and Stop information,
     *  identify all the edges that connect stops along a Line.
     * - Construct the collection of all Edges in the graph  and
     * - Construct the forward neighbour edges of each Stop.
     */
    private void createAndConnectEdges() {
        // Iterate over each line
        for (Line line : lines) {
            // Retrieve the type of transportation for the line
            String transpType = line.getType();
            
            // Retrieve the list of stops on the line
            List<Stop> stops = line.getStops();
            
            // Iterate over adjacent pairs of stops in the line
            for (int i = 0; i < stops.size() - 1; i++) {
                Stop from = stops.get(i);
                Stop to = stops.get(i + 1);
                
                // Calculate the distance between the stops
                double distance = from.distanceTo(to);
                
                // Construct an edge between the adjacent stops
                Edge edge = new Edge(from, to, transpType, line, distance);
                
                // Add the edge to the graph
                edges.add(edge);
                
                // Construct forward neighbor edges of each stop
                from.addNeighbor(to);
                to.addNeighbor(from);
            }
        }
    }


    
    /** 
     * Construct the undirected graph of neighbours for each Stop:
     * For each Stop, construct a set of the stops that are its neighbours
     * from the forward and backward neighbour edges.
     * It may assume that there are no walking edges at this point.
     */
    public void computeNeighbors() {
        for (Stop stop : stops) {
            stop.clearNeighbors(); // Clear existing neighbors
            for (Edge edge : edges) {
                if (edge.fromStop().equals(stop)) {
                    stop.addNeighbor(edge.toStop());
                }
                if (edge.toStop().equals(stop)) {
                    stop.addNeighbor(edge.fromStop());
                }
            }
        }
    }




    
 
    //=============================================================================
    //  ***  Recompute Walking edges and add to the graph  ***
    //=============================================================================
    //
        /** 
         * Reconstruct all the current walking edges in the graph,
         * based on the specified walkingDistance:
         * identify all pairs of stops that are at most walkingDistance apart,
         * and construct edges (both ways) between the stops
         * Assumes that all the previous walking edges have been removed
         */
    public void recomputeWalkingEdges(double walkingDistance) {
        int count = 0;
        
        
        // Recreate walking edges based on walking distance
        for (Stop fromS : stops) {
            for (Stop toS : stops) {
                if (fromS != toS && fromS.distanceTo(toS) <= walkingDistance) {
                    Edge edge = new Edge(fromS, toS, Transport.WALKING, null, fromS.distanceTo(toS));
                    // Add neighbors bidirectionally between 'fromS' and 'toS'
                    edges.add(edge);
                    fromS.addNeighbor(toS);
                    toS.addNeighbor(fromS);
                    count++; // Increment the count of edges added
                }
            }
        }

    
        System.out.println("Number of walking edges added: " + count);
    }






    /** 
    * Remove all the current walking edges in the graph
    * - from the edges field (the collection of all the edges in the graph)
    * - from the forward neighbours of each Stop.
    */
    public void removeWalkingEdges()  {  
        // Remove walking edges from the edges collection
        edges.removeIf(edge -> edge.transpType().equals(Transport.WALKING));


    }


    //=============================================================================
    //  Methods to access data from the graph. 
    //=============================================================================

    /**
     * Return a collection of all the lines in the network
     */        
    public Collection<Line> getLines() {
        return Collections.unmodifiableCollection(lines);
    }

    /**
     * Return a collection of all the stops in the network
     */        
    public Collection<Stop> getStops() {
        return Collections.unmodifiableCollection(stops);
    }

    /**
     * Return a collection of all the edges in the network
     */        
    public Collection<Edge> getEdges() {
        return Collections.unmodifiableCollection(edges);
    }

    /**
     * Return the first stop that starts with the specified prefix
     * (first by alphabetic order of name)
     */
    public Stop getFirstMatchingStop(String prefix) {
        prefix = prefix.toLowerCase();
        for (Stop stop : stops) {
            if (stop.getName().toLowerCase().startsWith(prefix)) {
                return stop;
            }
        }
        return null;
    }

    /** 
     * Return all the stops that start with the specified prefix
     * in alphabetic order.
     * This version is not very efficient
     */
    public List<Stop> getAllMatchingStops(String prefix) {
        List<Stop> ans = new ArrayList<Stop>();
        prefix = prefix.toLowerCase();
        for (Stop stop : stops) {
            if (stop.getName().toLowerCase().startsWith(prefix)) {
                ans.add(stop);
            }
        }
        return ans;
    }

 


}
