package astar;

public class Point implements Comparable<Point> {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point add(Point delta) {
        return new Point(this.x + delta.x, this.y + delta.y);
    }

    @Override
    public int compareTo(Point other) {
        if (this.x == other.x) {
            return this.y - other.y;
        }

        return this.x - other.x;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Point)) return false;

        Point o = (Point)other;
        return (this.x == o.x && this.y == o.y);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime * Integer.hashCode(this.x) * Integer.hashCode(this.y);
    }

    @Override
    public String toString() {
        return "AStarPoint(" + Integer.toString(this.x) + "," + Integer.toString(this.y) + ")";
    }
}