import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Day07 extends DayRunner {
	private List<Long[]> lines;

	Day07(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.lines = new ArrayList<>();

		for(String line: getLines(fileName)) {
			Long[] num = parseLine(line);
			this.lines.add(num);
		}
	}

	protected Long[] parseLine(String line) {
		List<Long> numbers = new ArrayList<>();

		Integer index = line.indexOf(":");
		numbers.add(Long.valueOf(line.substring(0, index)));

		line = line.substring(2 + index);
		while(line.length() > 0) {
			index = line.indexOf(" ");
			if (index < 0) {
				index = line.length();
			}

			numbers.add(Long.valueOf(line.substring(0, index)));
			if (1 + index > line.length()) {
				index = line.length() - 1;
			}

			line = line.substring(1 + index);
		}

		Long[] res = new Long[numbers.size()];
		return numbers.toArray(res);
	}

	protected Integer getDayNumber() {
		return 7;
	}

	protected String getExpectedFirstResult() {
		return "3749";
	}

	protected String getExpectedSecondResult() {
		return "11387";
	}

	protected String getFirstResult() {
		String[] operators = new String[] { "+", "*" };
		Long sum = 0L;

		for(Long[] numbers: this.lines) {
			sum += getResult(numbers, operators);
		}

		return sum.toString();
	}

	protected String getSecondResult() {
		String[] operators = new String[] { "+", "*", "||" };
		Long sum = 0L;

		for(Long[] numbers: this.lines) {
			sum += getResult(numbers, operators);
		}

		return sum.toString();
	}

	protected Long getResult(Long[] numbers, String[] operators) {
		List<Pair<Long, String>> possible = new ArrayList<>();

		if (this.debug) {
			System.out.print("[ ");
			for(Long v: numbers) {
				System.out.print(v.toString() + " ");
			}
			System.out.println("]");
		}

		Pair<Long, String> first = new Pair<>(numbers[1], numbers[1].toString());
		possible.add(first);

		for(Integer index = 2; index < numbers.length; index++) {
			List<Pair<Long, String>> next_possible = new ArrayList<>();

			for(Pair<Long, String> item: possible) {
				Long value = item.getA();
				String expr = item.getB();

				for(String operator: operators) {
					Long result = numbers[0] + 1;

					if (operator.equals("+")) {
						result = value + numbers[index];
					} else if (operator.equals("*")) {
						result = value * numbers[index];
					} else if (operator.equals("||")) {
						result = Long.valueOf(value.toString() + numbers[index].toString());
					}

					if (result <= numbers[0]) {
						Pair<Long, String> next = new Pair<>(result, expr + " " + operator.toString() +  " " + numbers[index].toString());
						if (this.debug) {
							System.out.println(next.toString());
						}
						next_possible.add(next);
					}
				}

			}

			possible = next_possible;
			if (possible.size() == 0) {
				break;
			}
		}

		for(Pair<Long, String> item: possible) {
			if (item.getA().equals(numbers[0])) {
				if (this.debug) {
					System.out.println(item.toString());
				}
				return numbers[0];
			}
		}

		return 0L;
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

		new Day07(debug, runTests, runActual).Run();
	}
}
