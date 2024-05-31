
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Map;
import javafx.util.Pair;
import java.util.PriorityQueue;

/** Edmond karp algorithm to find augmentation paths and network flow.
 * 
 * This would include building the supporting data structures:
 * 
 * a) Building the residual graph(that includes original and backward (reverse) edges.)
 *     - maintain a map of Edges where for every edge in the original graph we add a reverse edge in the residual graph.
 *     - The map of edges are set to include original edges at even indices and reverse edges at odd indices (this helps accessing the corresponding backward edge easily)
 *     
 *     
 * b) Using this residual graph, for each city maintain a list of edges out of the city (this helps accessing the neighbours of a node (both original and reverse))

 * The class finds : augmentation paths, their corresponing flows and the total flow
 * 
 * 
 */

public class EdmondKarp {
    // class members

    //data structure to maintain a list of forward and reverse edges - forward edges stored at even indices and reverse edges stored at odd indices
    private static Map<String,Edge> edges; 

    // Augmentation path and the corresponding flow
    private static ArrayList<Pair<ArrayList<String>, Integer>> augmentationPaths =null;

    
    //TODO:Build the residual graph that includes original and reverse edges 
    public static void computeResidualGraph(Graph graph){
        // TODO
        
        edges = new HashMap<String,Edge>();
        int counter = 0;

        for(Edge e : graph.getOriginalEdges()){
            //forward edge -> add to map
            edges.put(Integer.toString(counter), 
            new Edge(e.fromCity(), e.toCity(), e.capacity(), 0)); //forward
            e.fromCity().addEdgeId(Integer.toString(counter)); //add forward to map using counter as key
            
            //backward edge
            Edge bEdge = new Edge(e.toCity(), e.fromCity(), 0, 0); //backward

            edges.put(Integer.toString(counter+1), bEdge); //add bEdge to map using counter as key
            counter += 2; //increment by 2 for next pair
        }
        
        printResidualGraphData(graph);  //may help in debugging
        // END TODO
    }

    // Method to print Residual Graph 
    public static void printResidualGraphData(Graph graph){
        System.out.println("\nResidual Graph");
        System.out.println("\n=============================\nCities:");
        for (City city : graph.getCities().values()){
            System.out.print(city.toString());

            // for each city display the out edges 
            for(String eId: city.getEdgeIds()){
                System.out.print("["+eId+"] ");
            }
            System.out.println();
        }
        System.out.println("\n=============================\nEdges(Original(with even Id) and Reverse(with odd Id):");
        edges.forEach((eId, edge)->
                System.out.println("["+eId+"] " +edge.toString()));

        System.out.println("===============");
    }

    //=============================================================================
    //  Methods to access data from the graph. 
    //=============================================================================
    /**
     * Return the corresonding edge for a given key
     */

    public static Edge getEdge(String id){
        return edges.get(id);
    }

    /** find maximum flow
     * 
     */
    // TODO: Find augmentation paths and their corresponding flows
    public static ArrayList<Pair<ArrayList<String>, Integer>> calcMaxflows(Graph graph, City from, City to) {
                
        computeResidualGraph(graph); //compute residual based on original grpah
        augmentationPaths = new ArrayList<>(); //list of aPaths
        
        Pair<ArrayList<String>, Integer> p = bfs(graph,from,to); //find first path using bfs

        while(p != null){ //iterate -> no more paths found
            augmentationPaths.add(p); //add path -> list
            p = bfs(graph,from,to); //find next path
        }
        
        return augmentationPaths; //return list
    }
    
    
        public static void updateResidualGraph(int pathFlow, ArrayList<String>augmentationPath){
        for(String edgeID : augmentationPath){ //iterate through each edge
            Edge fEdge = edges.get(edgeID); //forward edge
            Edge bEdge = edges.get(Integer.toString((Integer.parseInt(edgeID) + 1))); //backward edge
            
            fEdge.setFlow(fEdge.flow() + pathFlow); //update forward flow
            fEdge.setCapacity(fEdge.capacity() - pathFlow); //update forward capacity
            bEdge.setCapacity(bEdge.capacity() + pathFlow); //update backward capacity
        }
    }
    
    public static int BN(ArrayList<String> augmentationPath) {
        int bn = Integer.MAX_VALUE; //initialize to max value
        
        for (String path : augmentationPath) { //iterate over each path
            Edge e = edges.get(path); //get corresponding edge from map
            int edgeCapacity = e.capacity(); //get capacity of edge
            
            if (edgeCapacity < bn) {bn = edgeCapacity;} //check capacity is smaller than bnValue -> update value if true
        }
        
        return bn; //return bottleneck value
    }

    // TODO:Use BFS to find a path from s to t along with the correponding bottleneck flow
    public static Pair<ArrayList<String>, Integer>  bfs(Graph graph, City s, City t) {

        ArrayList<String> augmentationPath = new ArrayList<String>();
        HashMap<String, String> bP = new HashMap<String, String>();
        
        Queue<City>q = new PriorityQueue<>(); //queue -> bfs traversal
        q.add(s);

        while(!q.isEmpty()){ //iterate until queue is empty
            City cur = q.poll(); //city at front of queue

            for(String outEdge : cur.getEdgeIds()){ //iterate over each edge
                City next = edges.get(outEdge).toCity(); //get next from outgoing

                if(next != s && bP.get(next.getId()) == null && edges.get(outEdge).capacity() != 0){
                    bP.put(edges.get(outEdge).toCity().getId(),outEdge); //backpointer of next city to outedge

                    if(bP.get(t.getId()) != null){ //if dest city has been reached
                        String pEdge = bP.get(t.getId()); //construct aPath following backward pointers
                        
                        while(pEdge != null){ //path not null
                            augmentationPath.add(pEdge); //add edge to path
                            pEdge = bP.get(edges.get(pEdge).fromCity().getId()); //update edge to previous
                        }
                        
                        Collections.reverse(augmentationPath); //reverse augmentation path
                        int bottleNeck = BN(augmentationPath); //calculate bottleneck
                        
                        updateResidualGraph(bottleNeck, augmentationPath); //update residual 
                        return new Pair(augmentationPath, bottleNeck); //return pair of path and bottleneck
                    }
                    q.add(edges.get(outEdge).toCity()); //add next city to queue for traversal
                }
            }
        }

        //return new Pair(null,0);
        return null;
    }

   
}


