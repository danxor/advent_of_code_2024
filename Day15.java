import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import map.Point;
import map.Map;

public class Day15 extends DayRunner {
	private HashMap<Character, Point> deltas;
	private String movement;
	private Map secondMap;
	private Map firstMap;

	Day15(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);

		this.deltas = new HashMap<>();
		this.deltas.put('^', new Point(0, -1));
		this.deltas.put('>', new Point(1, 0));
		this.deltas.put('v', new Point(0, 1));
		this.deltas.put('<', new Point(-1, 0));
	}

	protected void parse(String fileName) {
		List<String> lines = getLines(fileName);

		StringBuilder movement = new StringBuilder();
		List<String> map = new ArrayList<>();
		boolean isMap = true;

		for(String line: lines) {
			if (line.length() == 0) {
				isMap = false;
			} else if (isMap) {
				map.add(line);
			} else {
				movement.append(line);
			}
		}

		this.firstMap = Map.createFromLines(map);

		this.movement = movement.toString();

		ArrayList<String> secondMapLines = new ArrayList<>();
		for(int y = 0; y < this.firstMap.height; y++) {
			StringBuilder line = new StringBuilder();

			for(int x = 0; x < this.firstMap.width; x++) {
				Character c = this.firstMap.get(x, y);
				switch(c) {
					case '@':
						line.append('@');
						line.append('.');
						break;
					case '#':
						line.append('#');
						line.append('#');
						break;
					case 'O':
						line.append('[');
						line.append(']');
						break;
					default:
						line.append('.');
						line.append('.');
						break;
				}
			}

			secondMapLines.add(line.toString());
		}

		this.secondMap = Map.createFromLines(secondMapLines);
	}

	protected Integer getDayNumber() {
		return 15;
	}

	protected String getExpectedFirstResult() {
		return "10092";
	}

	protected String getExpectedSecondResult() {
		return "9021";
	}

	protected String getFirstResult() {
		Point cur = this.firstMap.find('@');

		this.firstMap.set(cur, '.');

		if (this.debug) {
			System.out.println("Intial");
			printMap(cur, this.firstMap);
		}

		for(int i = 0; i < this.movement.length(); i++) {
			Character c = this.movement.charAt(i);
			Point d = this.deltas.get(c);

			if (cur.X < 1 || cur.Y < 1) {
				System.out.println("WTF?!");
			}

			if (canMove(this.firstMap, d, cur.add(d), new HashMap<Point, Boolean>())) {
				commitMove(this.firstMap, d, cur.add(d), new HashSet<Point>());
				cur = cur.add(d);
			}

			if (this.debug) {
				System.out.println("After " + c.toString());
				printMap(cur, this.firstMap);
			}
		}

		if (this.debug) {
			System.out.println("Final");
			printMap(cur, this.firstMap);
		}

		return Long.toString(getScore(this.firstMap));
	}

	protected String getSecondResult() {
		Point cur = this.secondMap.find('@');

		this.secondMap.set(cur, '.');

		if (this.debug) {
			System.out.println("Intial");
			printMap(cur, this.secondMap);
		}

		for(int i = 0; i < this.movement.length(); i++) {
			Character c = this.movement.charAt(i);
			Point d = this.deltas.get(c);

			if (canMove(this.secondMap, d, cur.add(d), new HashMap<Point, Boolean>())) {
				commitMove(this.secondMap, d, cur.add(d), new HashSet<Point>());
				cur = cur.add(d);
			}

			if (this.debug) {
				System.out.println("After " + c.toString());
				printMap(cur, this.secondMap);
			}
		}


		if (this.debug) {
			System.out.println("Final");
			printMap(cur, this.secondMap);
		}

		return Long.toString(getScore(this.secondMap));
	}

	protected boolean canMove(Map map, Point d, Point r, HashMap<Point, Boolean> checked) {
		if (checked.containsKey(r)) {
			return checked.get(r);
		}

		checked.put(r, true);

		Character c = map.get(r);
		if (c == '#') {
			checked.put(r, false);
			return false;
		} else if (c == '.') {
			checked.put(r, true);
			return true;
		} else if (c == 'O') {
			Point np = r.add(d);
			if (canMove(map, d, np, checked)) {
				return true;
			}

			return false;
		} else if (c == '[') {
			Point np = new Point(1, 0);
			boolean res = canMove(map, d, r.add(d), checked) && canMove(map, d, r.add(np), checked);
			checked.put(r, res);
			return res;
		} else if (c == ']') {
			Point np = new Point(-1, 0);
			boolean res = canMove(map, d, r.add(d), checked) && canMove(map, d, r.add(np), checked);
			checked.put(r, res);
			return res;
		}

		return true;
	}

	protected void commitMove(Map map, Point d, Point r, Set<Point> committed) {
		if (committed.contains(r)) {
			return;
		}

		committed.add(r);

		Character c = map.get(r);
		if (c == '#') {
			return;
		} else if (c == '.') {
			return;
		} else if (c == 'O') {
			Point np = r.add(d);
			commitMove(map, d, np, committed);
			map.set(np, map.get(r));
			map.set(r, '.');
		} else if (c == '[') {
			Point np = new Point(1, 0);
			commitMove(map, d, r.add(d), committed);
			commitMove(map, d, r.add(np), committed);
			map.set(r.add(d), map.get(r));
			map.set(r, '.');
		} else if (c == ']') {
			Point np = new Point(-1, 0);
			commitMove(map, d, r.add(d), committed);
			commitMove(map, d, r.add(np), committed);
			map.set(r.add(d), map.get(r));
			map.set(r, '.');
		}
	}

	protected long getScore(Map map) {
		long sum = 0L;

		for(int y = 0; y < map.height; y++) {
			for(int x = 0; x < map.width; x++) {
				Character c = map.get(x, y);
				if (c == 'O') {
					sum += 100 * y + x;
				} else if (c == '[') {
					sum += 100 * y + x;
				}
			}
		}

		return sum;
	}

	protected void printMap(Point robot, Map map) {
		for(int y = 0; y < map.height; y++) {
			if (y == 0) {
				System.out.print(" ");
				for(int i = 0; i < map.width; i++) {
					System.out.print(Integer.toString(i % 10));
				}
				System.out.println("");
			}

			for(int x = 0; x < map.width; x++) {
				if (x == 0) {
					System.out.print(Integer.toString(y % 10));
				}

				if (x == robot.X && y == robot.Y) {
					System.out.print('@');
				} else {
					Character c = map.get(x, y);
					System.out.print(c.toString());
				}
			}

			System.out.println("");
		}
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

		new Day15(debug, runTests, runActual).Run();
	}
}
