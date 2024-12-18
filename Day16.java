import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayDeque;
import java.util.Deque;
import map.Point;
import map.Map;
import map.PointDirection;

public class Day16 extends DayRunner {
	private static final Point[] DELTA = new Point[] {
		new Point(1, 0),	// East
		new Point(0, 1),	// South
		new Point(-1, 0),		// West
		new Point(0, -1)		// North
	};

	private float[][][] dist;
	private Point start;
	private Point end;
	private Map map;

	Day16(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.map = Map.createFromFile(fileName);
		this.start = this.map.find('S');
		this.end = this.map.find('E');

		this.dist = new float[this.map.height][this.map.width][4];
		for(int y = 0; y < this.map.height; y++) {
			for(int x = 0; x < this.map.width; x++) {
				for(int d = 0; d < 4; d++) {
					this.dist[y][x][d] = Float.POSITIVE_INFINITY;
				}
			}
		}
	}

	protected Integer getDayNumber() {
		return 16;
	}

	protected String getExpectedFirstResult() {
		return "7036";
	}

	protected String getExpectedSecondResult() {
		return "45";
	}

	protected String getFirstResult() {
		int minCost = getLowestCost(this.map, this.dist, this.start, this.end, 0);
		return Integer.toString(minCost);
	}

	protected String getSecondResult() {
		int numTiles = getBestPathTiles(this.map, this.dist, this.start, this.end);
		return Integer.toString(numTiles);
	}

	protected int getLowestCost(Map map, float[][][] dist, Point start, Point end, int startDir) {
		dist[(int)start.Y][(int)start.X][startDir] = 0f;

		PriorityQueue<WalkItem> queue = new PriorityQueue<>();
		queue.add(new WalkItem(0, new PointDirection(start, startDir)));
		Set<PointDirection> visited = new HashSet<>();

		while(!queue.isEmpty()) {
			WalkItem cur = queue.remove();

			if (visited.contains(cur.pd)) {
				continue;
			}

			visited.add(cur.pd);

			float currentDistance = dist[cur.pd.y][cur.pd.x][cur.pd.d];
			if (cur.cost > currentDistance) {
				continue;
			}

			int dx = (int)Day16.DELTA[cur.pd.d].X;
			int dy = (int)Day16.DELTA[cur.pd.d].Y;
			int nx = cur.pd.x + dx;
			int ny = cur.pd.y + dy;
			float newCost = 0f;

			Character c = map.get((int)nx, (int)ny);
			if (map.isInside(cur.pd.x, cur.pd.y) && c != '#') {
				newCost = cur.cost + 1f;
				if (newCost < dist[ny][nx][cur.pd.d]) {
					dist[ny][nx][cur.pd.d] = newCost;
					queue.add(new WalkItem(newCost, new PointDirection(nx, ny, cur.pd.d)));
				}
			}

			int leftDir = wrapAround(cur.pd.d - 1, 4);
			newCost = cur.cost + 1000f;
			if (newCost < dist[cur.pd.y][cur.pd.x][leftDir]) {
				dist[cur.pd.y][cur.pd.x][leftDir] = newCost;
				queue.add(new WalkItem(newCost, new PointDirection(cur.pd.x, cur.pd.y, leftDir)));
			}

			int rightDir = wrapAround(cur.pd.d + 1, 4);
			newCost = cur.cost + 1000f;
			if (newCost < dist[cur.pd.y][cur.pd.x][rightDir]) {
				dist[cur.pd.y][cur.pd.x][rightDir] = newCost;
				queue.add(new WalkItem(newCost, new PointDirection(cur.pd.x, cur.pd.y, rightDir)));
			}
		}

		int minCost = Integer.MAX_VALUE;
		for(int d = 0; d < 4; d++) {
			int cost = (int)dist[(int)end.Y][(int)end.X][d];
			if (cost < minCost) {
				minCost = cost;
			}
		}

		return minCost;
	}

	protected int getBestPathTiles(Map map, float[][][] dist, Point start, Point end) {
		float minCost = Float.POSITIVE_INFINITY;
		for(int d = 0; d < 4; d++) {
			int cost = (int)dist[(int)end.Y][(int)end.X][d];
			if (cost < minCost) {
				minCost = cost;
			}
		}

		boolean bestPath[][] = new boolean[map.height][map.width];
		for(int c = 0; c < map.height; c++) {
			for(int r = 0; r < map.width; r++) {
				bestPath[c][r] = false;
			}
		}

		Deque<PointDirection> deque = new ArrayDeque<>();
		for(int d = 0; d < 4; d++) {
			if (dist[(int)end.Y][(int)end.X][d] == minCost) {
				deque.addLast(new PointDirection((int)end.X, (int)end.Y, d));
			}
		}

		Set<PointDirection> visited = new HashSet<>();

		int count = 0;
		
		while(!deque.isEmpty()) {
			PointDirection pd = deque.removeFirst();
			if (false == bestPath[pd.y][pd.x]) {
				bestPath[pd.y][pd.x] = true;
				count++;
			}

			Float costHere = dist[pd.y][pd.x][pd.d];

			int dx = (int)Day16.DELTA[pd.d].X;
			int dy = (int)Day16.DELTA[pd.d].Y;

			int px = pd.x - dx;
			int py = pd.y - dy;

			Character c = map.get(px, py);
			if (map.isInside(px, py) &&  c != '#') {
				if (dist[py][px][pd.d] == costHere - 1f) {
					PointDirection npd = new PointDirection(px, py, pd.d);
					if (!visited.contains(npd)) {
						visited.add(npd);
						deque.addLast(npd);
					}
				}
			}

			int leftDir = wrapAround(pd.d - 1, 4);
			if (dist[pd.y][pd.x][leftDir] == costHere - 1000f) {
				PointDirection npd = new PointDirection(pd.x, pd.y, leftDir);
				if (!visited.contains(npd)) {
					visited.add(npd);
					deque.addLast(npd);
				}
			}

			int rightDir = wrapAround(pd.d + 1, 4);
			if (dist[pd.y][pd.x][rightDir] == costHere - 1000f) {
				PointDirection npd = new PointDirection(pd.x, pd.y, rightDir);
				if (!visited.contains(npd)) {
					visited.add(npd);
					deque.addLast(npd);
				}
			}
		}

		return count;
	}

    public static int wrapAround(int v, int d) {
        while(v < 0) { v += d; }
        return v % d;
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

		new Day16(debug, runTests, runActual).Run();
	}

	public class WalkItem implements Comparable<WalkItem> {
		public final float cost;
		public final PointDirection pd;

		public WalkItem(float cost, PointDirection pd) {
			this.cost = cost;
			this.pd = pd;
		}

		@Override
		public boolean equals(Object other) {
			if (other == null) return false;
			if (other == this) return true;
			if (!(other instanceof WalkItem)) return false;
	
			WalkItem o = (WalkItem)other;
			return (this.cost == o.cost && this.pd.equals(o.pd));
		}

		@Override
		public int compareTo(WalkItem other) {
			if (other == null) return -1;
			if (other == this) return 0;

			return (int)(this.cost - other.cost);
		}
	
		@Override
		public int hashCode() {
			final int prime = 31;
			return prime * Float.hashCode(this.cost) * this.pd.hashCode();
		}
	
		@Override
		public String toString() {
			return "( score = " + Float.toString(this.cost) + ", pos = " + this.pd.toString() + " )";
		}
		}
}
