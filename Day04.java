import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Day04 extends DayRunner {
	private List<String> lines;
	private Integer maxY;
	private Integer maxX;

	private final String wordOne = "XMAS";
	private final String wordTwo = "MAS";

	Day04(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		lines = new ArrayList<String>();

		getLines(fileName)
			.stream()
			.forEach(line -> {
				lines.add(line);
			});

		this.maxY = lines.size();
		if (this.maxY > 0) {
			this.maxX = lines.get(0).length();
		} else {
			this.maxX = 0;
		}

		if (this.debug) {
			System.out.println("Size: " + maxX.toString() + "x" + maxY.toString());
		}
	}

	protected Integer getDayNumber() {
		return 4;
	}

	protected String getExpectedFirstResult() {
		return "18";
	}

	protected String getExpectedSecondResult() {
		return "9";
	}

	protected String getFirstResult() {
		Integer sum = 0;

		for(Integer y = 0; y < maxY; y++) {
			for(Integer x = 0; x < maxX; x++) {
				sum += countXmas(x, y);
			}
		}

		return sum.toString();
	}

	protected String getSecondResult() {
		Integer sum = 0;

		for(Integer y = 0; y < maxY; y++) {
			for(Integer x = 0; x < maxX; x++) {
				if (isMasX(x, y)) {
					sum++;
				}
			}
		}

		return sum.toString();
	}

	protected Character charAt(Integer x, Integer y) {
		if (x >= 0 && x < this.maxX && y >= 0 && y < this.maxY) {
			return this.lines.get(y).charAt(x);
		}

		return '.';
	}

	protected Boolean isMasX(Integer x, Integer y) {
		Boolean found = false;

		if (charAt(x, y) == 'A' && charAt(x - 1, y - 1) == 'M' && charAt(x - 1, y + 1) == 'M' && charAt(x + 1, y - 1) == 'S' && charAt(x + 1, y + 1) == 'S')  {
			found = true;
		} else if (charAt(x, y) == 'A' && charAt(x - 1, y - 1) == 'S' && charAt(x - 1, y + 1) == 'M' && charAt(x + 1, y - 1) == 'S' && charAt(x + 1, y + 1) == 'M')  {
			found = true;
		} else if (charAt(x, y) == 'A' && charAt(x - 1, y - 1) == 'S' && charAt(x - 1, y + 1) == 'S' && charAt(x + 1, y - 1) == 'M' && charAt(x + 1, y + 1) == 'M')  {
			found = true;
		} else if (charAt(x, y) == 'A' && charAt(x - 1, y - 1) == 'M' && charAt(x - 1, y + 1) == 'S' && charAt(x + 1, y - 1) == 'M' && charAt(x + 1, y + 1) == 'S')  {
			found = true;
		}

		if (found && this.debug) {
			System.out.println(y.toString() + "," + x.toString());
		}

		return found;
	}

	protected Boolean isXmas(Integer x, Integer y, Integer dx, Integer dy) {
		if (charAt(x + dx, y + dy) == 'M' && charAt(x + dx * 2, y + dy * 2) == 'A' && charAt(x + dx * 3, y + dy * 3) == 'S') {
			if (this.debug) {
				System.out.println(y.toString() + "," + x.toString() + " [" + dx.toString() + "," + dy.toString() + "]");
			}

			return true;
		}

		return false;
	}

	protected Integer countXmas(Integer x, Integer y) {
		Integer count = 0;

		if (charAt(x, y) == 'X') {
			for(Integer dy = -1; dy < 2; dy++) {
				for(Integer dx = -1; dx < 2; dx++) {
					if (isXmas(x, y, dx, dy)) {
						count++;
					}
				}
			}
		}

		return count;
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

		new Day04(debug, runTests, runActual).Run();
	}
}
