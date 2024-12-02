import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Day02 extends DayRunner {
	List<List<Integer>> reports = null;

	Day02(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.reports = new ArrayList<>();

		getLines(fileName)
			.stream()
			.map(line -> line.split(" "))
			.forEach(line -> {
				List<Integer> res = new ArrayList<>();

				for(Integer i = 0; i < line.length; i++) {
					res.add(Integer.valueOf(line[i]));
				}

				this.reports.add(res);
			});
	}

	protected Integer getDayNumber() {
		return 2;
	}

	protected String getExpectedFirstResult() {
		return "2";
	}

	protected String getExpectedSecondResult() {
		return "4";
	}

	protected Boolean isAscending(List<Integer> numbers) {
		Integer prev = numbers.get(0);

		for(Integer i = 1; i < numbers.size(); i++) {
			Integer cur = numbers.get(i);
			Integer delta = cur - prev;

			if (prev <= cur && delta > 0 && delta <= 3) {
				prev = cur;
			} else {
				return false;
			}
		}

		return true;
	}

	protected Boolean isDescending(List<Integer> numbers) {
		Integer prev = numbers.get(0);

		for(Integer i = 1; i < numbers.size(); i++) {
			Integer cur = numbers.get(i);
			Integer delta = prev - cur;

			if (prev >= cur && delta > 0 && delta <= 3) {
				prev = cur;
			} else {
				return false;
			}
		}

		return true;
	}

	protected String getFirstResult() {
		Integer count = 0;

		for(List<Integer> report: reports) {
			if (this.debug) {
				for(Integer num: report) {
					System.out.print(num.toString() + " ");
				}
			}

			if (isAscending(report)) {
				count++;
				if (this.debug) {
					System.out.println("Safe");
				}
			} else if (isDescending(report)) {
				count++;
				if (this.debug) {
					System.out.println("Safe");
				}
			} else if (this.debug) {
				System.out.println("Unsafe");
			}
		}

		return count.toString();
	}

	protected String getSecondResult() {
		Integer count = 0;

		for(List<Integer> report: reports) {
			if (this.debug) {
				for(Integer num: report) {
					System.out.print(num.toString() + " ");
				}
			}

			if (isAscending(report)) {
				count++;
				if (this.debug) {
					System.out.println("Safe");
				}
			} else if (isDescending(report)) {
				count++;
				if (this.debug) {
					System.out.println("Safe");
				}
			} else {
				Boolean isSafe = false;

				for(Integer i = 0; i < report.size(); i++) {
					List<Integer> clone = new ArrayList<>();
					for(Integer j = 0; j < report.size(); j++) {
						if (i != j) {
							clone.add(report.get(j));
						}
					}

					if (isAscending(clone)) {
						isSafe = true;
						break;
					} else if (isDescending(clone)) {
						isSafe = true;
						break;
					}
				}

				if (isSafe) {
					count++;
					if (this.debug) {
						System.out.println("Safe");
					}
				} else if (this.debug) {
					System.out.println("Unsafe");
				}
			}
		}

		return count.toString();
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

		new Day02(debug, runTests, runActual).Run();
	}
}
