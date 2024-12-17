import java.util.List;

public class Day12 extends DayRunner {
	private List<Region> regions;
	private Map map;

	Day12(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.map = Map.createFromFile(fileName);
		this.regions = map.getRegions();
	}


	protected Integer getDayNumber() {
		return 12;
	}

	protected String getExpectedFirstResult() {
		return "1930";
	}

	protected String getExpectedSecondResult() {
		return "1206";
	}

	protected String getFirstResult() {
		Integer sum = 0;

		for(Region reg: this.regions) {
			int perim = reg.getPerimeter();
			if (this.debug) {
				int s = reg.area * perim;
				System.out.println(reg.letter.toString() + ": " + Integer.toString(reg.area) + " * " + Integer.toString(perim) + " = " + Integer.toString(s));
			}
			sum += reg.area * perim;
		}

		return sum.toString();
	}

	protected String getSecondResult() {
		Integer sum = 0;

		for(Region reg: this.regions) {
			int sides = reg.getSides();
			if (this.debug) {
				int s = reg.area * sides;
				System.out.println(reg.letter.toString() + ": " + Integer.toString(reg.area) + " * " + Integer.toString(sides) + " = " + Integer.toString(s));
			}
			sum += reg.area * sides;
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

		new Day12(debug, runTests, runActual).Run();
	}
}
