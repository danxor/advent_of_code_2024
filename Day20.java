import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;

import astar.AStar;
import astar.Point;
import astar.Vector;
import map.Map;

public class Day20 extends DayRunner {
	private int[][] start_grid;
	private int[][] end_grid;
	private Point start;
	private Point end;
	private Map map;

	Day20(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.map = Map.createFromFile(fileName);
		this.map.outOfBoundsChar = '#';

		this.start = getPosition('S');
		this.end = getPosition('E');

		this.start_grid = bfs(start);
		this.end_grid = bfs(end);
	}

	protected Integer getDayNumber() {
		return 20;
	}

	protected String getExpectedFirstResult() {
		return "0";
	}

	protected String getExpectedSecondResult() {
		return "0";
	}

	protected String getFirstResult() {
		int picoseconds = start_grid[end.y][end.x];

		Set<Vector> cheats = new HashSet<>();

		for(int y = 0; y < this.map.height; y++) {
			for(int x = 0; x < this.map.width; x++) {
				int before_cheat = start_grid[y][x];
				if (before_cheat == -1) {
					continue;
				}

				Character c = this.map.get(x, y);
				if (c == '#') {
					continue;
				}

				Point p = new Point(x, y);

				for(Point d: AStar.STRAIGHT_DELTA) {
					Point n = p.add(d);
					for(Point d2: AStar.STRAIGHT_DELTA) {
						Point n2 = n.add(d2);

						Character nc = this.map.get(n2.x, n2.y);
						if (nc == '#') {
							continue;
						}

						int after_cheat = end_grid[n2.y][n2.x];
						if (after_cheat == -1) {
							continue;
						}

						int picosecondsWithCheat = before_cheat + after_cheat + 2;
						int savedPicoseconds = picoseconds - picosecondsWithCheat;
						if (savedPicoseconds >= 100) {
							cheats.add(new Vector(p, n2));
						}
					}
				}
			}
		}

		return Integer.toString(cheats.size());
	}

	protected String getSecondResult() {
		int picoseconds = start_grid[end.y][end.x];

		Set<Vector> cheats = new HashSet<>();

		Set<Point> possible = new HashSet<>();
		for(int y = 0; y < this.map.height; y++) {
			for(int x = 0; x < this.map.width; x++) {
				if (start_grid[y][x] == -1) {
					continue;
				}

				Character c = this.map.get(x, y);
				if (c == '#') {
					continue;
				}

				possible.add(new Point(x, y));
			}
		}

		for(Point p: possible) {
			int[][] grid = new int[this.map.height][this.map.width];
			for(int j = 0; j < this.map.height; j++) {
				for(int i = 0; i < this.map.width; i++) {
					grid[j][i] = -1;
				}
			}

			grid[p.y][p.x] = 0;

			Deque<Point> queue = new ArrayDeque<>();
			queue.add(p);

			while(!queue.isEmpty()) {
				Point x = queue.removeFirst();
				int dist = grid[x.y][x.x];
				if (dist == 20) {
					continue;
				}

				dist++;

				for(Point d: AStar.STRAIGHT_DELTA) {
					Point n = x.add(d);
					if (!this.map.isInside(n.x, n.y)) {
						continue;
					}

					if (grid[n.y][n.x] == -1 || grid[n.y][n.x] > dist) {
						grid[n.y][n.x] = dist;
						queue.add(n);
					}
				}
			}
				
			int before_cheat = start_grid[p.y][p.x];

			for(int y = 0; y < this.map.height; y++) {
				for(int x = 0; x < this.map.width; x++) {
					int d = grid[y][x];
					if (d < 1) {
						continue;
					}

					Character c = this.map.get(x, y);
					if (c == '#') {
						continue;
					}

					int after_cheat = end_grid[y][x];
					if (after_cheat == -1) {
						continue;
					}

					int picosecondsWithCheat = before_cheat + after_cheat + d;
					int savedPicoseconds = picoseconds - picosecondsWithCheat;
					if (savedPicoseconds >= 100) {
						Point n = new Point(x, y);
						cheats.add(new Vector(p, n));
					}
				}
			}
		}

		return Integer.toString(cheats.size());
	}

	protected Point getPosition(Character c) {
		map.Point pos = map.find(c);
		return new Point((int)pos.X, (int)pos.Y);
	}

	protected HashMap<Point, Integer> wallHack(Point start, int[][] grid) {
		HashMap<Point, Integer> possible = new HashMap<>();

		for(int dy = 1; dy <= 20; dy++) {
			for(int dx = 1; dx <= 20; dx++) {
				int d = dx + dy;
				if (d > 20) {
					continue;
				} else if (!map.isInside(start.x + dx, start.y + dy)) {
					continue;
				}

				Point n = start.add(dx, dy);
				if (grid[n.y][n.x] == -1) {
					continue;
				}

				possible.put(n, d);
			}
		}

		return possible;
	}

	protected int[][] bfs(Point start) {
		int[][] grid = new int[this.map.height][this.map.width];

		for(int j = 0; j < this.map.height; j++) {
			for(int i = 0; i < this.map.width; i++) {
				grid[j][i] = -1;
			}
		}

		Deque<Point> queue = new ArrayDeque<>();
		queue.addLast(start);
		grid[start.y][start.x] = 0;

		while(!queue.isEmpty()) {
			Point p = queue.removeFirst();
			for(Point d: AStar.STRAIGHT_DELTA) {
				Point n = p.add(d);
				Character c = map.get(n.x, n.y);
				
				if (c == '#') {
					continue;
				}

				int dist = grid[p.y][p.x] + 1;
				if (grid[n.y][n.x] == -1 || grid[n.y][n.x] > dist) {
					grid[n.y][n.x] = dist;
					queue.addLast(n);
				}
			}
		}

		return grid;
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

		new Day20(debug, runTests, runActual).Run();
	}
}
