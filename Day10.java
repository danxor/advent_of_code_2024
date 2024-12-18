import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import map.Point;
import map.Map;

public class Day10 extends DayRunner {
	private final Point[] deltas = new Point[] { new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(0, -1) };
	private List<Point> starts;
	private Map map;

	Day10(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.map = Map.createFromFile(fileName);
		this.starts = new ArrayList<>();
		Integer[] size = map.size();

		for(int y = 0; y < size[1]; y++) {
			for(int x = 0; x < size[0]; x++) {
				Character c = map.get(x, y);
				if (c == '0') {
					Point p = new Point(x, y);
					starts.add(p);
				}
			}
		}

	}

	protected Integer getDayNumber() {
		return 10;
	}

	protected String getExpectedFirstResult() {
		return "36";
	}

	protected String getExpectedSecondResult() {
		return "81";
	}

	protected String getFirstResult() {
		Integer sum = 0;

		for(Point start: this.starts) {
			HashSet<Point> ends = new HashSet<>();
			List<Point> current = new ArrayList<>();

			current.add(start);

			while(current.size() > 0) {
				List<Point> next_current = new ArrayList<>();

				for(Point p: current) {
					Integer v = Integer.valueOf("" + this.map.get(p));

					for(Point d: this.deltas) {
						Point np = p.add(d);
						Character c = this.map.get(np);
						if (c == '.') {
							continue;
						}

						int nv = Integer.valueOf("" + this.map.get(np));
						int dv = nv - v;

						if (dv == 1 && nv < 9) {
							if (this.debug) {
								System.out.println(p.toString() + " -> " + np.toString() + ": " + Integer.valueOf(dv) + " " + Integer.valueOf(nv));
							}

							next_current.add(np);
						} else if (dv == 1 && nv == 9) {
							ends.add(np);
						}
					}
				}

				current = next_current;
			}

			sum += ends.size();
		}

		return sum.toString();
	}

	protected String getSecondResult() {
		Integer sum = 0;

		for(Point start: this.starts) {
			List<Point> current = new ArrayList<>();

			current.add(start);

			while(current.size() > 0) {
				List<Point> next_current = new ArrayList<>();

				for(Point p: current) {
					Integer v = Integer.valueOf("" + this.map.get(p));

					if (v == 9) {
						next_current.add(p);
						continue;
					}

					for(Point d: this.deltas) {
						Point np = p.add(d);
						Character c = this.map.get(np);
						if (c == '.') {
							continue;
						}

						int nv = Integer.valueOf("" + this.map.get(np));
						int dv = nv - v;

						if (dv == 1) {
							if (this.debug) {
								System.out.println(p.toString() + " -> " + np.toString() + ": " + Integer.valueOf(dv) + " " + Integer.valueOf(nv));
							}

							next_current.add(np);
						}
					}
				}

				current = next_current;

				boolean all = true;
				for(Point p: current) {
					Integer v = Integer.valueOf("" + this.map.get(p));
					if (v < 9) {
						all = false;
						break;
					}
				}

				if (all) {
					break;
				}
			}

			sum += current.size();
		}

		return sum.toString();
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

		new Day10(debug, runTests, runActual).Run();
	}
}
