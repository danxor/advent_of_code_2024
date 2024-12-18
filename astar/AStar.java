package astar;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.HashSet;

public class AStar {
    /**
     * Delta points for moving North, South, East and West.
     */
    public static final Point[] STRAIGHT_DELTA = new Point[] {
        new Point(-1, 0),
        new Point(0, -1),
        new Point(1, 0),
        new Point(0, 1)
    };

    /**
     * Delta points for moving North, South, East and West as well as moving
     * diagonally; i.e. North-West, South-East and so forth.
     */
    public static final Point[] DIAGONAL_DELTA = new Point[] {
        new Point(-1, -1),
        new Point(0, -1),
        new Point(1, -1),
        new Point(-1, 0),
        new Point(1, 0),
        new Point(-1, 1),
        new Point(0, 1),
        new Point(1, 1)
    };

    /**
     * Delta points for only moving diagonally; i.e. only North-West,
     * North-East, South-West and South-East.
     */
    public static final Point[] CROSS_DELTA = new Point[] {
        new Point(-1, -1),
        new Point(1, -1),
        new Point(-1, 1),
        new Point(1, -1)
    };

    private final IAStarSolver solver;
    private final Grid grid;

    public AStar(map.Map map, IAStarSolver solver) {
        this.grid = Grid.createFromMap(map);
        this.solver = solver;
    }

    public Result search(int x, int y) {
        Node start = this.grid.get(x, y);

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        HashSet<Node> closedSet = new HashSet<>();

        openSet.add(start);

        while(!openSet.isEmpty()) {
            Node currentNode = openSet.remove();

            closedSet.add(currentNode);

            if (solver.isEnd(currentNode)) {
                return retracePath(start, currentNode);
            }

            List<Node> neighbours = solver.getNeighbours(grid, currentNode);

            for(Node neighbour: neighbours) {
                if (!neighbour.isWalkable || closedSet.contains(neighbour)) {
                    continue;
                }

                int newCost = currentNode.g + solver.getDistance(currentNode, neighbour) * neighbour.price;
                if (newCost < neighbour.g || !openSet.contains(neighbour)) {
                    neighbour.g = newCost;
                    neighbour.h = solver.getDistance(neighbour, solver.getNearestEndNode(grid, neighbour));
                    neighbour.cost = neighbour.g + neighbour.h;
                    neighbour.parent = currentNode;

                    if (!openSet.contains(neighbour)) {
                        openSet.add(neighbour);
                    }
                }
            }
        }

        return null;
    }

    public Result search(Point startPosition) {
        return search(startPosition.x, startPosition.y);
    }

    protected Result retracePath(Node startNode, Node endNode) {
        List<Node> path = new ArrayList<>();

        Node currentNode = endNode;

        while(currentNode != startNode) {
            path.add(currentNode);
            currentNode = currentNode.parent;
        }

        path.add(startNode);

        Collections.reverse(path);

        return new Result(endNode.cost, path, path.size() - 1);
    }
}