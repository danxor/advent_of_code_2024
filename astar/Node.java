package astar;

public class Node implements Comparable<Node> {
    public final boolean isWalkable;
    public final int x;
    public final int y;
    public final int price;

    public Node(boolean isWalkable, int x, int y, int price) {
        this.isWalkable = isWalkable;
        this.x = x;
        this.y = y;
        this.price = price;
        this.g = 0;
        this.h = 0;
        this.cost = 0;
        this.parent = null;
    }

    public int g;
    public int h;
    public int cost;
    public Node parent;

    @Override
    public int compareTo(Node other) {
        if (this.cost == other.cost) {
            return this.price - other.price;
        }

        return this.cost - other.cost;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Node)) return false;

        Node o = (Node)other;
        return (this.x == o.x && this.y == o.y && this.price == o.price);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime * Integer.hashCode(this.x) * Integer.hashCode(this.y) * Float.hashCode(this.price);
    }

    @Override
    public String toString() {
        return "AStarNode(" + Integer.toString(this.x) + "," + Integer.toString(this.y) + ", " + Integer.toString(this.cost) + ")";
    }
}
