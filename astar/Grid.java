package astar;

public class Grid {
    private final Node[][] nodes;
    public final int height;
    public final int width;

    public Grid(int width, int height) {
        this.height = height;
        this.width = width;
        this.nodes = new Node[height][width];
    }

    public boolean isInside(int x, int y) {
        if (y >= 0 && y < this.height && x >= 0 && x < this.width) {
            return true;
        }

        return false;
    }

    public Node get(int x, int y) {
        if (isInside(x, y)) {
            return this.nodes[y][x];
        }

        return null;
    }

    public void set(int x, int y, Node node) {
        if (isInside(x, y)) {
            this.nodes[y][x] = node;
        }
    }

    public static Grid createFromMap(map.Map map) {
        Grid grid = new Grid(map.width, map.height);

        for(int y = 0; y < map.height; y++) {
            for(int x = 0; x < map.width; x++) {
                Character c = map.get(x, y);
                if (c == '#') {
                    grid.set(x, y, new Node(false, x, y, 1));
                } else {
                    grid.set(x, y, new Node(true, x, y, 1));
                }
            }
        }

        return grid;
    }
}
