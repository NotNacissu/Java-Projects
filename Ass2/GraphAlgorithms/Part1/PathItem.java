
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
        return Double.compare(this.estimate, o.estimate);
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
        return this.edge;
    }
}
