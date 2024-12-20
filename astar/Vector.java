package astar;

public class Vector implements Comparable<Vector> {
    public final int x;
    public final int y;
    public final int dx;
    public final int dy;

    public Vector(int x, int y, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public Vector(Point p, Point d) {
        this.x = p.x;
        this.y = p.y;
        this.dx = d.x;
        this.dy = d.y;
    }

    public Vector move(int times) {
        return new Vector(this.x + this.dx * times, this.y + this.dy * times, this.dx, this.dy);
    }

    @Override
    public int compareTo(Vector other) {
        if (this.x == other.x) {
            return this.y - other.y;
        }

        return this.x - other.x;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Vector)) return false;

        Vector o = (Vector)other;
        return (this.x == o.x && this.y == o.y && this.dx == o.dx && this.dy == o.dy);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime * Integer.hashCode(this.x) * Integer.hashCode(this.y) * Integer.hashCode(this.dx) * Integer.hashCode(this.dy);
    }

    @Override
    public String toString() {
        return "AStarVector(" + Integer.toString(this.x) + ", " + Integer.toString(this.y) + ", " + Integer.toString(this.dx) + ", " + Integer.toString(this.dy) + ")";
    }
}