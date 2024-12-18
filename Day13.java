import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import map.Point;

public class Day13 extends DayRunner {
	private List<ClawMachine> machines;
	private final Pattern button;
	private final Pattern prize;
	private List<String> lines;

	Day13(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);

		this.button = Pattern.compile("Button [AB]: X[+]([0-9]+), Y[+]([0-9]+)");
		this.prize = Pattern.compile("Prize: X=([0-9]+), Y=([0-9]+)");
	}

	protected void parse(String fileName) {
		Point a = null;
		Point b = null;
		Point t = null;

		this.lines = getLines(fileName);

		int lineNum = 0;

		this.machines = new ArrayList<>();

		for(String line: this.lines) {
			lineNum++;

			switch(lineNum % 4) {
				case 1:
					a = getRegexPoint(this.button, line);
					break;
				case 2:
					b = getRegexPoint(this.button, line);
					break;
				case 3:
					t = getRegexPoint(this.prize, line);
					this.machines.add(new ClawMachine(a, b, t));
					break;
				default:
					break;
			}
		}
	}

	protected Point getRegexPoint(Pattern pattern, String line) {
		Matcher matcher = pattern.matcher(line);
		while(matcher.find()) {
			int x = Integer.valueOf(matcher.group(1));
			int y = Integer.valueOf(matcher.group(2));
			return new Point(x, y);
		}

		return null;
	}

	protected Integer getDayNumber() {
		return 13;
	}

	protected String getExpectedFirstResult() {

		return "480";
	}

	protected String getExpectedSecondResult() {
		return "875318608908";
	}

	protected String getFirstResult() {
		Long sum = 0L;

		for(ClawMachine m: this.machines) {
			long cost = m.getLowestCost();
			if (cost >= 0L) {
				sum += cost;
			}
		}

		return sum.toString();
	}

	protected String getSecondResult() {
		Long sum = 0L;

		for(ClawMachine m: this.machines) {
			ClawMachine other = new ClawMachine(m.buttonA, m.buttonB, new Point(m.target.X + 10000000000000L, m.target.Y + 10000000000000L));

			long cost = other.getLowestCost();
			if (cost >= 0L) {
				sum += cost;
			}
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

		new Day13(debug, runTests, runActual).Run();
	}

	public class ClawMachine {
		public final Point buttonA;
		public final Point buttonB;
		public final Point target;

		public ClawMachine(Point a, Point b, Point t) {
			this.buttonA = a;
			this.buttonB = b;
			this.target = t;
		}

		public long getLowestCost() {
			long am = (target.X * buttonB.Y - target.Y * buttonB.X) / (buttonB.Y * buttonA.X - buttonB.X * buttonA.Y);
			long bm = (target.X * buttonA.Y - target.Y * buttonA.X) / (buttonB.X * buttonA.Y - buttonB.Y * buttonA.X);
			if (0L < am && 0L < bm) {
				Point p = new Point(buttonA.X * am + buttonB.X * bm, buttonA.Y * am + buttonB.Y * bm);
				if (p.equals(target)) {
					return (3 * am) + bm;
				}
			}

			return -1L;
		}
	}
}
