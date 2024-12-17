import java.util.HashSet;

public class Day06 extends DayRunner {
	private HashSet<Point> visited;
	private Point start;
	private Map map;

	Day06(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		map = Map.createFromFile(fileName);
		this.start = this.map.find('^');
	}

	protected Integer getDayNumber() {
		return 6;
	}

	protected String getExpectedFirstResult() {
		return "41";
	}

	protected String getExpectedSecondResult() {
		return "6";
	}

	protected String getFirstResult() {
		if (this.start == null) {
			return "Not found";
		}

		Point dir = new Point(0, -1);

		this.visited = new HashSet<>();

		Point pos = new Point(this.start.X, this.start.Y);

		while(map.isInside(pos)) {
			this.visited.add(pos);

			Point new_pos = pos.add(dir);
			if (!map.isInside(new_pos)) {
				break;
			}

			Character c = map.get(new_pos);
			if (c == '#') {
				dir = rotateRight(dir);
			} else {
				pos = new_pos;
			}
		}

		Integer len = this.visited.size();
		return len.toString();
	}

	protected String getSecondResult() {
		Integer[] size = this.map.size();
		Point dir = new Point(0, -1);
		Integer count = 0;

		for(Integer y = 0; y < size[1]; y++) {
			for(Integer x = 0; x < size[0]; x++) {
				Point pos = new Point(x, y);

				if (this.visited.contains(pos)) {
					Character c = this.map.get(pos);
					if (c == '.') {
						this.map.set(pos, '#');

						Vector v = new Vector(this.start, dir);
						if (isLoop(this.map, v)) {
							count++;
						}

						this.map.set(pos, '.');
					}
				}
			}
		}

		return count.toString();
	}

	protected Boolean isLoop(Map map, Vector v) {
		HashSet<Vector> movements = new HashSet<>();

		while(map.isInside(v.pos)) {
			if (this.debug) {
				System.out.println(v.toString());
			}

			Point new_pos = v.pos.add(v.dir);
			Vector new_v = new Vector(new_pos, v.dir);

			if (!map.isInside(new_pos)) {
				return false;
			} else if (movements.contains(new_v)) {
				return true;
			}

			movements.add(v);

			Character c = map.get(new_pos);
			if (c == '#') {
				v = new Vector(v.pos, rotateRight(v.dir));
			} else {
				v = new_v;
			}
		}

		return false;
	}

	protected Point rotateRight(Point p) {
		if (p.Y == 0) return new Point(0, p.X);

		return new Point(-p.Y, 0);
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

		new Day06(debug, runTests, runActual).Run();
	}
}
