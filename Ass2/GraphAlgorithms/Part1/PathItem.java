import java.util.List;
/**
 * AStar search uses a priority queue of partial paths
 * that the search is building.
 * Each partial path needs several pieces of information, to specify
 * the path to that point, its cost so far, and its estimated total cost
 */

public class PathItem implements Comparable<PathItem> {

    private Stop stop;
    private Edge edge;
    
    private double length;
    private double estimate;

    public PathItem(Stop stop, Edge edge, double length, double estimate) {
        this.stop = stop;
        this.edge = edge;
        this.length = length;
        this.estimate = estimate;
    }

    public int compareTo(PathItem o) {
        if (this.estimate < o.estimate) {return -1;} // if this is less than other
        else if (this.estimate > o.estimate) {return 1;} // if this is greater than other
        else {return 0;} // if this is equal to other
    }

    public double getEstimate() {
        return this.estimate;
    }

    public Stop getStop() {
        return this.stop;
    }

    public double getLengthSoFar() {
        return length;
    }

    public Edge getEdge() {
        return edge;
    }
}
