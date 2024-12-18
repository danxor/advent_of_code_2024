package astar;

import java.util.List;

public interface IAStarSolver {
    public Node getNearestEndNode(Grid grid, Node current);
    public int getDistance(Node a, Node b);
    public boolean isEnd(Node current);
    public List<Node> getNeighbours(Grid grid, Node current);
}
