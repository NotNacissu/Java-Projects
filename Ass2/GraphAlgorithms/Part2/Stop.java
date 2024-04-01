import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public class Stop implements Comparable<Stop> {    
    private GisPoint loc;
    private String name;
    private String id;

    // Collection of outgoing edges
    private Collection<Edge> outEdges = new HashSet<>();

    // Collection of incoming edges
    private Collection<Edge> inEdges = new HashSet<>();

    // Collection of lines that the stop is part of   
    private Collection<Line> lines = new HashSet<>();
    
    //Field to record the different subgraphs
    private int subGraphId = -1; // used to denote which subgraph the stop belongs to. -1 to indicate no subgraphs yet.

    public Stop(double lon, double lat, String name, String id) {
        this.loc = new GisPoint(lon, lat);
        this.name = name;
        this.id = id;
    }
    
    /** 
     * @param subGraphId the id of the graph so stops in the game subgraph can be drawn in the same colour or highlighted
     */
    public void setSubGraphId(int id) {
        this.subGraphId = id;
    }
    
    public int getSubGraphId() {
        return subGraphId;
    }

    // Getters for basic properties of a Stop
    public GisPoint getPoint() {
        return this.loc;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public double distanceTo(GisPoint loc) {
        return this.loc.distance(loc);
    }
    
    public double distanceTo(Stop toStop) {
        return this.loc.distance(toStop.loc);
    }
    
        public Collection<Edge> getOutgoingEdges() {
        Collection<Edge> outgoingEdges = new HashSet<>();
        for (Edge edge : outEdges) {
            if (edge.fromStop().equals(this)) {
                outgoingEdges.add(edge);
            }
        }
        return Collections.unmodifiableCollection(outgoingEdges);
    }

    // Comparable implementation
    public int compareTo(Stop other){
        int ans = this.name.compareTo(other.name);
        if (ans != 0) {
            return ans;
        }
        return this.id.compareTo(other.id);
    }

    // Display a stop
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(id).append(": ");
        str.append(name).
            append(" at (").
            append(loc.getLon()).
            append(", ").
            append(loc.getLat()).
            append(")");
        return str.toString();
    }

    // Check if the stop is in the same location as the given point
    public boolean atLocation(GisPoint point) {
        return this.loc.equals(point);
    }

    // Methods for managing lines through this stop
    public void addLine(Line line) {
        this.lines.add(line);
    }

    public Collection<Line> getLines() {
        return Collections.unmodifiableCollection(this.lines);
    }

    // Methods for managing outgoing edges
    public Collection<Edge> getOutEdges() {
        return Collections.unmodifiableCollection(outEdges);
    }
         
    public void addOutEdge(Edge edge) {
        this.outEdges.add(edge);
    }

    // Methods for managing incoming edges
    public Collection<Edge> getInEdges() {
        return Collections.unmodifiableCollection(inEdges);
    }

    public void addInEdge(Edge edge) {
        this.inEdges.add(edge);
    }

    public void deleteEdgesOfType(String type) {
        outEdges.removeIf((Edge e) -> type.equals(e.transpType()));
        inEdges.removeIf((Edge e) -> type.equals(e.transpType()));
    }
    
    
}
