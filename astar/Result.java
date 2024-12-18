package astar;

import java.util.List;

public class Result {
    public final int minScore;
    public final List<Node> path;
    public final int steps;

    public Result(int minScore, List<Node> path, int steps) {
        this.minScore = minScore;
        this.path = path;
        this.steps = steps;
    }
}
