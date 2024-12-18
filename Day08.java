import java.util.HashMap;
import java.util.HashSet;
import map.Point;
import map.Map;

public class Day08 extends DayRunner {
	private HashMap<Character, HashSet<Point>> antennas;
	private Map map;

	Day08(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.map = Map.createFromFile(fileName);

		this.antennas = new HashMap<>();
		Integer[] size = this.map.size();

		for(int y = 0; y < size[1]; y++) {
			for(int x = 0; x < size[0]; x++) {
				Character c = this.map.get(x, y);
				if (c == '.') {
					continue;
				}

				if (!this.antennas.containsKey(c)) {
					this.antennas.put(c, new HashSet<Point>());
				}

				HashSet<Point> set = antennas.get(c);
				set.add(new Point(x, y));
			}
		}
	}

	protected Integer getDayNumber() {
		return 8;
	}

	protected String getExpectedFirstResult() {
		return "14";
	}

	protected String getExpectedSecondResult() {
		return "34";
	}

	protected String getFirstResult() {
		HashSet<Point> locations = new HashSet<>();

		for(HashSet<Point> set: this.antennas.values()) {
			Point[] coords = set.toArray(new Point[set.size()]);

			for(int i = 0; i < coords.length; i++) {
				Point first = coords[i];

				for(int j = i + 1; j < coords.length; j++) {
					Point second = coords[j];

					if (this.debug) {
						System.out.println(first.toString() + " " + second.toString());
					}

					Point mirrorOne = first.mirror(second);
					if (this.map.isInside(mirrorOne)) {
						locations.add(mirrorOne);
					}

					Point mirrorTwo = second.mirror(first);
					if (this.map.isInside(mirrorTwo)) {
						locations.add(mirrorTwo);
					}
				}
			}
		}

		return Integer.toString(locations.size());
	}

	protected String getSecondResult() {
		HashSet<Point> locations = new HashSet<>();

		for(HashSet<Point> set: this.antennas.values()) {
			Point[] coords = set.toArray(new Point[set.size()]);

			for(int i = 0; i < coords.length; i++) {
				Point first = coords[i];

				for(int j = i + 1; j < coords.length; j++) {
					Point second = coords[j];

					if (this.debug) {
						System.out.println(first.toString() + " " + second.toString());
					}

					Point mirrorOne = first.mirror(second);
					Point mirrorTwo = second.mirror(first);

					locations.add(first);
					locations.add(second);

					Point mirrorRefOne = first;
					Point mirrorRefTwo = second;

					while(this.map.isInside(mirrorOne)) {
						locations.add(mirrorOne);

						Point save = mirrorOne;
						mirrorOne = mirrorOne.mirror(mirrorRefOne);
						mirrorRefOne = save;
					}

					while(this.map.isInside(mirrorTwo)) {
						locations.add(mirrorTwo);

						Point save = mirrorTwo;
						mirrorTwo = mirrorTwo.mirror(mirrorRefTwo);
						mirrorRefTwo = save;
					}
				}
			}
		}

		return Integer.toString(locations.size());
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

		new Day08(debug, runTests, runActual).Run();
	}
}
