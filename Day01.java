import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Collections;
import java.lang.Math;

public class Day01 extends DayRunner {
	List<Integer> left = null;
	List<Integer> right = null;

	Day01(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.left = new ArrayList<>();
		this.right = new ArrayList<>();

		getLines(fileName)
			.stream()
			.map(line -> line.split(" "))
			.map(parts -> new Integer[] { Integer.valueOf(parts[0]), Integer.valueOf(parts[3]) })
			.forEach(parts -> {
				left.add(parts[0]);
				right.add(parts[1]);
			});

		Collections.sort(this.left);
		Collections.sort(this.right);
	}

	protected Integer getDayNumber() {
		return 1;
	}

	protected String getExpectedFirstResult() {
		return "11";
	}

	protected String getExpectedSecondResult() {
		return "31";
	}

	protected String getFirstResult() {
		Integer sum = 0;

		for(Integer i = 0; i < this.left.size(); i++) {
			Integer delta = Math.abs(this.left.get(i) - this.right.get(i));
			if (this.debug) {
				System.out.print(delta.toString() + " + ");
			}

			sum += delta;
		}

		if (this.debug) {
			System.out.println("\b\b\b = " + sum.toString());
		}

		return sum.toString();
	}

	protected String getSecondResult() {
		Hashtable<Integer, Integer> rightValueCounts = new Hashtable<>();
		Integer sum = 0;

		for(Integer i = 0; i < this.right.size(); i++) {
			Integer value = this.right.get(i);

			if (!rightValueCounts.containsKey(value)) {
				Integer count = 1;

				for(Integer j = i + 1; j < this.right.size(); j++) {
					if (value.equals(this.right.get(j))) {
						count++;
					}
				}

				rightValueCounts.put(value, count);					
			}
		}

		for(Integer value : this.left) {
			Integer count = 0;

			if (rightValueCounts.containsKey(value)) {
				count = rightValueCounts.get(value);
			}

			if (this.debug) {
				System.out.print(value.toString() + " * " + count.toString() + " + ");
			}

			sum += value * count;
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

		new Day01(debug, runTests, runActual).Run();
	}
}
