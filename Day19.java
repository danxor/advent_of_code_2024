import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Day19 extends DayRunner {
	private List<String> patterns;
	private List<String> designs;
	private Long second;
	private Long first;

	Day19(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		boolean isPattern = true;

		this.patterns = new ArrayList<>();
		this.designs = new ArrayList<>();

		for(String line: getLines(fileName)) {
			if (line.length() == 0) {
				isPattern = false;
				continue;
			}

			if (isPattern) {
				for(String pattern: line.trim().split(",")) {
					this.patterns.add(pattern.trim());
				}
			} else {
				this.designs.add(line.trim());
			}
		}
	}

	protected Integer getDayNumber() {
		return 19;
	}

	protected String getExpectedFirstResult() {
		return "6";
	}

	protected String getExpectedSecondResult() {
		return "16";
	}

	protected String getFirstResult() {
		first = 0L;
		second = 0L;

		for(String design: this.designs) {
			long count = getDesignCount(new HashMap<String, Long>(), design, this.patterns);
			if (count > 0) {
				first++;
				second += count;
			}
		}
		
		return first.toString();
	}

	protected String getSecondResult() {
		return second.toString();
	}

	protected long getDesignCount(Map<String, Long> cache, String design, List<String> patterns) {
		long count = 0;

		if (design.length() == 0) {
			return 1;
		} else if (cache.containsKey(design)) {
			return cache.get(design);
		}

		for(String pattern: patterns) {
			if (design.startsWith(pattern)) {
				count += getDesignCount(cache, design.substring(pattern.length()), patterns);
			}
		}

		cache.put(design, count);

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

		new Day19(debug, runTests, runActual).Run();
	}
}
