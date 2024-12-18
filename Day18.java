import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.lang.Math;
import astar.AStar;
import astar.Point;
import astar.Node;
import astar.Result;
import astar.Grid;
import astar.IAStarSolver;
import map.Map;

public class Day18 extends DayRunner {
	private List<Point> errors;
	private PathSolver solver;
	private Map map;

	Day18(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.errors = new ArrayList<>();

		for(String line: getLines(fileName)) {
			String[] parts = line.split(",");
			Point p = new Point(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]));
			this.errors.add(p);
		}

		int width = 71;
		int height = 71;
		int count = 1024;

		if (this.errors.size() < 100) {
			width = 7;
			height = 7;
			count = 12;
		}

		Set<Point> errorPoints = new HashSet<>();
		for(int i = 0; i < count; i++) {
			Point p = this.errors.remove(0);
			errorPoints.add(p);
		}

		List<String> lines = new ArrayList<>();

		for(int y = 0; y < height; y++) {
			StringBuilder line = new StringBuilder();

			for(int x = 0; x < width; x++) {
				Point p = new Point(x, y);
				if (errorPoints.contains(p)) {
					line.append('#');
				} else {
					line.append('.');
				}
			}

			lines.add(line.toString());
		}

		this.map = Map.createFromLines(lines);

		this.solver = new PathSolver(new Point(width - 1, height - 1));
	}

	protected Integer getDayNumber() {
		return 18;
	}

	protected String getExpectedFirstResult() {
		return "22";
	}

	protected String getExpectedSecondResult() {
		return "6,1";
	}

	protected String getFirstResult() {
		AStar pathFinder = new AStar(this.map, this.solver);

		Result result = pathFinder.search(0, 0);

		if (this.debug) {
			Map clone = this.map.clone();

			for(Node node: result.path) {
				Character c = clone.get(node.x, node.y);
				if (c == '.') {
					clone.set(node.x, node.y, 'O');
				} else {
					System.out.println("WTF?!");
				}
			}

			clone.print();
		}

		return Integer.toString(result.steps);
	}

	protected String getSecondResult() {
		Point error = findBreakingPoint();
		if (error != null) {
			return Integer.toString(error.x) + "," + Integer.toString(error.y);
		} else {
			return "X";
		}
	}

	protected Point findBreakingPoint() {
		for(Point error: this.errors) {
			this.map.set(error.x, error.y, '#');
			AStar pathFinder = new AStar(this.map, this.solver);
			Result result = pathFinder.search(0, 0);
			if (result == null) {
				return error;
			} else {
				if (this.debug) {
					Map clone = this.map.clone();

					for(Node node: result.path) {
						Character c = clone.get(node.x, node.y);
						if (c == '.') {
							clone.set(node.x, node.y, 'O');
						} else {
							System.out.println("WTF?!");
						}
					}

					clone.print();
				}
			}
		}

		return null;
	}

	public static void main(String[] args) {
		Boolean debug = false;
		Boolean runTests = true;
		Boolean runActual = true;

		for(String arg: args) {
			if (arg.equals("--skip-tests")) {
				runTests = false;
			} else if (arg.equals("--only-tests"))  {
				runActual = false;
			} else if (arg.equals("--debug")) {
				debug = true;
			}
		}

		new Day18(debug, runTests, runActual).Run();
	}

	public class PathSolver implements IAStarSolver {
		public final Point end;

		public PathSolver(Point end) {
			this.end = end;
		}

		@Override
		public Node getNearestEndNode(Grid grid, Node current) {
			return grid.get(this.end.x, this.end.y);
		}

		@Override
		public int getDistance(Node a, Node b) {
			int distX = Math.abs(a.x - b.x);
			int distY = Math.abs(a.y - b.y);

			if (distX > distY) {
				return 28 * distY + 20 * (distX - distY);
			} else {
				return 28 * distX + 20 * (distY - distX);
			}
		}

		@Override
		public boolean isEnd(Node current) {
			if (current.x == this.end.x && current.y == this.end.y) {
				return true;
			}

			return false;
		}

		@Override
		public List<Node> getNeighbours(Grid grid, Node current) {
			List<Node> neighbours = new ArrayList<>();

			for(Point delta: AStar.STRAIGHT_DELTA) {
				Node node = grid.get(current.x + delta.x, current.y + delta.y);
				if (node == null) {
					continue;
				}

				neighbours.add(node);
			}

			return neighbours;
		}
	}
}
