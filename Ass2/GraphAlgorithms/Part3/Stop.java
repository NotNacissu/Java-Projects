import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

/**
 * Structure for holding stop information
 * Needs to be Comparable in order to be able to order them for input in the interface
 */

public class Stop implements Comparable<Stop> {    
    // Location of the stop
    private GisPoint loc;
    private String name;
    private String id;
    // Data structure for holding the (directed) edges out of the stop
    private Collection<Stop> neighbors = new HashSet<>(); 
    // Data structure for holding a link to the lines that stop is part of   
    private Collection<Line> lines = new HashSet<>(); 
    // Depth for articulation points
    private int depth;

    /**
     * Constructor for a stop
     * 
     * @param lon Longitude of the stop
     * @param lat Latitude of the stop
     * @param name Long name for the stop
     * @param id 4 or 5 digit stop id
     */
    public Stop(double lon, double lat, String name, String id) {
        this.loc = new GisPoint(lon, lat);
        this.name = name;
        this.id = id;
        this.depth = -1; // Initialize depth to -1
    }
    


    /**
     * Get the location of the stop
     * @return GisPoint object of location on earth
     */
    public GisPoint getPoint() {
        return this.loc;
    }

    /**
     * Get the name of the stop
     * @return Name of the stop
     */
    public String getName() {
        return name;
    }

    /**
     * Get the ID of the stop
     * @return ID of the stop
     */
    public String getId() {
        return id;
    }

    /**
     * Returns distance in meters between this stop and a GisPoint
     * @param loc GisPoint to calculate distance to
     * @return Distance in meters
     */
    public double distanceTo(GisPoint loc) {
        return this.loc.distance(loc);
    }
    
    /**
     * Returns distance in meters between this stop and another stop
     * @param toStop Stop to calculate distance to
     * @return Distance in meters
     */
    public double distanceTo(Stop toStop) {
        return this.loc.distance(toStop.loc);
    }

    /**
     * Compare by alphabetic order of name,
     * If two stops have the same name, then
     * compare their id's in case they are not the same stop.
     * @param other Stop to compare to
     * @return Comparison result
     */
    public int compareTo(Stop other){
        int ans = this.name.compareTo(other.name);
        if (ans!=0) {return ans;}
        return this.id.compareTo(other.id);
    }

    /** 
     * Display a stop
     * @return string of the stop information in the format: XXXX: long name at (lon,lat)
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(id).append(": ");
        str.append(name).append(" at (").append(loc.getLon()).append(", ").append(loc.getLat()).append(")");
        return str.toString();
    }

    /**
     * @param point GisPoint to check if the stop is in an **identical** location
     * @return is this stop in the same location as the given point
     */
    public boolean atLocation(GisPoint point) {
        return this.loc.equals(point);
    }

    //-------------------------
    // Setting and getting the lines through this stop
    //-------------------------

    /**
     * adding a line that goes through this stop
     * @param line
     */
    public void addLine(Line line) {
        this.lines.add(line);
    }

    /**
     * Return the lines that this stop is on 
     */
    public Collection<Line> getLines() {
        return Collections.unmodifiableCollection(this.lines);
    }

    //--------------------------------------------
    //  Setting and getting the neighbours of the stop
    //
    //  edges is a collection of the (directed) edges out of the stop, 
    //--------------------------------------------
    
    /** Get the collection of neigbor*/
    public Collection<Stop> getNeighbors() {
        return new HashSet<>(neighbors);
    }
         
    /** add a new neighbor */
    public void addNeighbor(Stop neighbor) {
        neighbors.add(neighbor);
    }
    
    public void removeNeighbor(Stop neighbor) {
        neighbors.remove(neighbor);
    }
    
    public void clearNeighbors() {
        neighbors.clear();
    }
    
    public int getDepth() {
        return depth;
    }
    
    public void setDepth(int depth) {
        this.depth = depth;
    }
    

    
    
    



}

