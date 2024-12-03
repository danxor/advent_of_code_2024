import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 extends DayRunner {
	private List<String> lines;
	private final Pattern pattern;

	Day03(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);

		pattern = Pattern.compile("(mul[(]([0-9]{1,3}),([0-9]{1,3})[)]|do[(][)]|don\'t[(][)])");
	}

	protected void parse(String fileName) {
		lines = new ArrayList<>();

		getLines(fileName)
			.stream()
			.forEach(line -> {
				lines.add(line);
			});
	}

	protected Integer getDayNumber() {
		return 3;
	}

	protected String getExpectedFirstResult() {
		return "161";
	}

	protected String getExpectedSecondResult() {
		return "48";
	}

	protected String getFirstResult() {
		Integer sum = 0;

		for(String line: lines) {
			Matcher matcher = this.pattern.matcher(line);
			while(matcher.find()) {
				String name = matcher.group(0);
				if (name.startsWith("mul")) {
					Integer a = Integer.parseInt(matcher.group(2));
					Integer b = Integer.parseInt(matcher.group(3));

					sum += a * b;

					if (this.debug) {
						System.out.print(a.toString() + " * " + b.toString() + " + ");
					}
				}
			}
		}

		if (this.debug) {
			System.out.println("\b\b\b = " + sum.toString());
		}

		return sum.toString();
	}

	protected String getSecondResult() {
		Integer sum = 0;
		Boolean active = true;

		for(String line: lines) {
			Matcher matcher = this.pattern.matcher(line);
			while(matcher.find()) {
				String name = matcher.group(0);
				if (name.equals("do()")) {
					active = true;
				} else if (name.equals("don\'t()")) {
					active = false;
				} else if (active) {
					Integer a = Integer.parseInt(matcher.group(2));
					Integer b = Integer.parseInt(matcher.group(3));

					sum += a * b;

					if (this.debug) {
						System.out.print(a.toString() + " * " + b.toString() + " + ");
					}
				}
			}
		}

		if (this.debug) {
			System.out.println("\b\b\b = " + sum.toString());
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

		new Day03(debug, runTests, runActual).Run();
	}
}
