import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Day14 extends DayRunner {
	private List<Robot> robots;
	private List<String> lines;
	private int width = 101;
	private int height = 103;

	Day14(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.robots = new ArrayList<>();
		this.lines = getLines(fileName);
		for(String line: lines) {
			String[] parts = line.split(" ");
			String[] p = parts[0].substring(2).split(",");
			String[] v = parts[1].substring(2).split(",");
			Point rp = new Point(Integer.valueOf(p[0]), Integer.valueOf(p[1]));
			Point rv = new Point(Integer.valueOf(v[0]), Integer.valueOf(v[1]));
			Robot r = new Robot(rp, rv);
			this.robots.add(r);
		}

		// This is a bit hacky to disguish the test set from the real set
		if (this.robots.size() <= 12) {
			this.width = 11;
			this.height = 7;
		} else {
			this.width =  101;
			this.height = 103;
		}
	}

	protected Integer getDayNumber() {
		return 14;
	}

	protected String getExpectedFirstResult() {
		return "12";
	}

	protected String getExpectedSecondResult() {
		return "1";
	}

	protected String getFirstResult() {
		List<Robot> cur = this.robots;

		if (this.debug) {
			System.out.println("Initial");
			printMap(cur);
		}

		for(int s = 0; s < 100; s++) {
			cur = moveRobots(cur);
			if (this.debug) {
				System.out.println("After " + Integer.toString(1 + s) + " seconds");
				printMap(cur);
			}
		}

		if (this.debug) {
			System.out.println("Final");
			printMap(cur);
		}

		return getSafetyFactor(cur).toString();
	}

	protected String getSecondResult() {
		boolean overlaps = true;

		List<Robot> cur = this.robots;

		Long count = 0L;

		while(overlaps) {
			HashSet<Point> set = new HashSet<>();

			cur = moveRobots(cur);
			count++;

			overlaps = false;
			for(Robot r: cur) {
				if (set.contains(r.p)) {
					overlaps = true;
					break;
				}
				set.add(r.p);
			}
		}

		return count.toString();
	}

	protected List<Robot> moveRobots(List<Robot> robots) {
		List<Robot> new_robots = new ArrayList<Robot>();

		for(Robot r: robots) {
			long x = r.p.X + r.v.X;
			while(x >= width) x -= this.width;
			while(x < 0) x += width;

			long y = r.p.Y + r.v.Y;
			while(y >= height) y -= this.height;
			while(y < 0) y += height;

			Point p = new Point(x, y);
			new_robots.add(new Robot(p, r.v));
		}

		return new_robots;
	}

	protected Long getSafetyFactor(List<Robot> robots) {
		int mw = this.width / 2;
		int mh = this.height / 2;

		long[] counts = new long[] { 0, 0, 0, 0 };

		for(Robot r: robots) {
			if (this.debug) {
				System.out.println(r.p.toString());
			}

			if (r.p.X >= 0 && r.p.X < mw && r.p.Y >= 0 && r.p.Y < mh) counts[0]++;
			if (r.p.X >= 1 + mw && r.p.X < this.width && r.p.Y >= 0 && r.p.Y < mh) counts[1]++;
			if (r.p.X >= 0 && r.p.X < mw && r.p.Y >= 1 + mh && r.p.Y < this.height) counts[2]++;
			if (r.p.X >= 1 + mw && r.p.X < this.width && r.p.Y >= 1 + mh && r.p.Y < this.height) counts[3]++;
		}

		if (this.debug) {
			for(int i = 0; i < counts.length; i++) {
				System.out.println(Integer.toString(i) + "=" + Long.toString(counts[i]));
			}
		}

		return counts[0] * counts[1] * counts[2] * counts[3];
	}

	protected void printMap(List<Robot> robots) {
		HashMap<Point, Integer> counts = new HashMap<>();

		for(Robot r: robots) {
			if (counts.containsKey(r.p)) {
				counts.put(r.p, counts.get(r.p) + 1);
			} else {
				counts.put(r.p, 1);
			}
		}

		for(int y = 0; y < this.height; y++) {
			for(int x = 0; x < this.width; x++) {
				Point p = new Point(x, y);
				if (counts.containsKey(p)) {
					String c = Integer.toString(counts.get(p));
					System.out.print(c);
				} else {
					System.out.print(".");
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

		new Day14(debug, runTests, runActual).Run();
	}

	public class Robot {
		public final Point p;
		public final Point v;

		public Robot(Point p, Point v) {
			this.p = p;
			this.v = v;
		}
	}
}
