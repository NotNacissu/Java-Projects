import javafx.util.Pair;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

/**
* Write a description of class PageRank here.
*
* @author (your name)
* @version (a version number or a date)
*/
public class PageRank
{
    //class members 
    private static double dampingFactor = .85;
    private static int iter = 10;
    
    private static Map<String, Double> pageRanks;  // Add this line
    /**
    * build the fromLinks and toLinks 
    */
    //TODO: Build the data structure to support Page rank. Compute the fromLinks and toLinks for each node
    public static void computeLinks(Graph graph){
        // TODO
        for(Edge links : graph.getOriginalEdges()){ //iterate through each edge
            links.toNode().addFromLinks(links.fromNode()); //add fromCity to toCity
            links.fromNode().addToLinks(links.toNode()); //add toCity to fromCity
        }

        //printPageRankGraphData(graph);  ////may help in debugging
        // END TODO
    }

    public static void printPageRankGraphData(Graph graph){
        System.out.println("\nPage Rank Graph");

        for (Gnode node : graph.getNodes().values()){
            System.out.print("\nNode: "+node.toString());
            //for each node display the in edges 
            System.out.print("\nIn links to nodes:");
            for(Gnode c:node.getFromLinks()){

                System.out.print("["+c.getId()+"] ");
            }

            System.out.print("\nOut links to nodes:");
            //for each node display the out edges 
            for(Gnode c: node.getToLinks()){
                System.out.print("["+c.getId()+"] ");
            }
            System.out.println();;

        }    
        System.out.println("=================");
    }
    //TODO: Compute rank of all nodes in the network and display them at the console
    public static void computePageRank(Graph graph) {
        int nNodes = graph.getNodes().size();
    
        // Initialize page ranks
        pageRanks = new HashMap<>();
        for (Gnode node : graph.getNodes().values()) {
            pageRanks.put(node.getId(), 1.0 / nNodes);
        }
    
        int count = 1;
        do {
            double noOutLinkShare = 0;
    
            // Calculate contribution from nodes with no outlinks
            for (Gnode node : graph.getNodes().values()) {
                if (node.getToLinks().isEmpty()) {
                    noOutLinkShare += dampingFactor * (pageRanks.get(node.getId()) / nNodes);
                }
            }
    
            Map<String, Double> newPageRanks = new HashMap<>();
            // Calculate new page ranks for each node
            for (Gnode node : graph.getNodes().values()) {
                double nRank = noOutLinkShare + (1 - dampingFactor) / nNodes;
    
                double neighboursShare = 0;
                // Calculate contribution from neighbors
                for (Gnode backNeighbor : node.getFromLinks()) {
                    int outEdgeCount = backNeighbor.getToLinks().size();
                    neighboursShare += pageRanks.get(backNeighbor.getId()) / outEdgeCount;
                }
    
                double newPageRank = nRank + dampingFactor * neighboursShare;
                newPageRanks.put(node.getId(), newPageRank);
            }
    
            // Update page ranks
            pageRanks = newPageRanks;
            count++;
        } while (count <= iter);
    
        // Display the page ranks
        System.out.println("\nIteration " + iter + ":");
        for (Map.Entry<String, Double> entry : pageRanks.entrySet()) {
            String nodeId = entry.getKey();
            double pageRank = entry.getValue();
            System.out.println("Node: " + nodeId + ", Page Rank: " + pageRank);
        }
        // Compute the most important neighbor for each node
        computeMostImpneighbour(graph); //Challenge method 
    }

    
    public static void computeMostImpneighbour(Graph graph){ //Method for challenge
        System.out.println("\n===============================================");
        System.out.println("Most Important Neighbour for Each Node: \n");
        for (Gnode node : graph.getNodes().values()) {
            String mostImportantNeighbor = null;
            double maxPageRankDrop = 0.0;  // Initialize to 0
    
            // Calculate the PageRank contribution of each neighbour
            Map<Gnode, Double> contributions = new HashMap<>();
            for (Gnode neighbor : node.getFromLinks()) {
                int outEdgeCount = neighbor.getToLinks().size();
                double contribution = pageRanks.get(neighbor.getId()) / outEdgeCount;
                contributions.put(neighbor, contribution);
            }
    
            // Also consider the nodes that the current node links to
            for (Gnode neighbor : node.getToLinks()) {
                int outEdgeCount = neighbor.getFromLinks().size();
                double contribution = pageRanks.get(neighbor.getId()) / outEdgeCount;
                contributions.put(neighbor, contribution);
            }
    
            // Find the neighbour that causes the greatest PageRank drop
            for (Map.Entry<Gnode, Double> entry : contributions.entrySet()) {
                Gnode neighbor = entry.getKey();
                double contribution = entry.getValue();
    
                // Calculate the new PageRank if the neighbour stopped linking to the node
                double newPageRank = pageRanks.get(node.getId()) - dampingFactor * contribution;
    
                // Check if the PageRank drop is the largest so far
                double pageRankDrop = pageRanks.get(node.getId()) - newPageRank;
                if (pageRankDrop > maxPageRankDrop) {
                    maxPageRankDrop = pageRankDrop;
                    mostImportantNeighbor = neighbor.getName();
                }
            }
    
            // Print the most important neighbour for the current node
            System.out.println("Node " + node.getName() + ": " + mostImportantNeighbor);
        }
    }

    
}
    

