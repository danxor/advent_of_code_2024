import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;

public class Day05 extends DayRunner {
	private Boolean parsingRules;
	private List<List<Integer>> updates;
	private Hashtable<Integer, Day05Rules> rules;

	Day05(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.parsingRules = true;
		this.updates = new ArrayList<>();
		this.rules = new Hashtable<>();

		for(String line: DayRunner.getLines(fileName)) {
			if (null == line || line.length() == 0) {
				this.parsingRules = false;
				continue;
			} else if (this.parsingRules) {
				Integer idx = line.indexOf("|");

				String[] parts = new String[] { line.substring(0, idx), line.substring(idx + 1) };

				Integer a = Integer.valueOf(parts[1]);
				Integer b = Integer.valueOf(parts[0]);

				if (!this.rules.containsKey(a)) {
					this.rules.put(a, new Day05Rules());
				}

				if (!this.rules.containsKey(b)) {
					this.rules.put(b, new Day05Rules());
				}

				Day05Rules ruleA = this.rules.get(a);
				ruleA.before.add(b);

				Day05Rules ruleB = this.rules.get(b);
				ruleB.after.add(a);
			} else {
				List<Integer> update = new ArrayList<>();

				for(String item: line.split(",")) {
					Integer x = Integer.valueOf(item);
					update.add(x);
				}

				this.updates.add(update);
			}
		}
	}

	protected Integer getDayNumber() {
		return 5;
	}

	protected String getExpectedFirstResult() {
		return "143";
	}

	protected String getExpectedSecondResult() {
		return "123";
	}

	protected Boolean isCorrectlyOrdered(List<Integer> update) {
		for(Integer i = 1; i < update.size(); i++) {
			Integer prev_page = update.get(i -1);
			Integer page = update.get(i);

			if (!this.rules.containsKey(page)) {
				return false;
			}

			Day05Rules rule = this.rules.get(page);
			if (!rule.before.contains(prev_page)) {
				return false;
			}
		}

		return true;
	}

	protected String getFirstResult() {
		Integer sum = 0;

		for(List<Integer> update: this.updates) {
			if (isCorrectlyOrdered(update)) {
				Integer mid = update.size() / 2;
				sum += update.get(mid);
			}
		}
			
		return sum.toString();
	}

	protected String getSecondResult() {
		List<List<Integer>> incorrect = new ArrayList<>();

		for(List<Integer> update: this.updates) {
			if (!isCorrectlyOrdered(update)) {
				incorrect.add(update);
			}
		}

		Boolean updating = true;
		while(updating) {
			updating = false;
			for(List<Integer> update: incorrect) {
				for(Integer i = 1; i < update.size(); i++) {
					Integer prev_page = update.get(i - 1);
					Integer page = update.get(i);

					Day05Rules rule = this.rules.get(page);
					if (!rule.before.contains(prev_page)) {
						updating = true;
						update.set(i - 1, page);
						update.set(i, prev_page);
					}
				}
			}
		}

		Integer sum = 0;

		for(List<Integer> update: incorrect) {
			Integer mid = update.size() / 2;
			sum += update.get(mid);
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

		new Day05(debug, runTests, runActual).Run();
	}
}
