import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.lang.Math;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Day23 extends DayRunner {
	private HashMap<String, HashSet<String>> connections;

	Day23(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.connections = new HashMap<>();

		for(String line: getLines(fileName)) {
			String[] computers = line.split("-");

			if (!connections.containsKey(computers[0])) {
				connections.put(computers[0], new HashSet<>());
			}

			if (!connections.containsKey(computers[1])) {
				connections.put(computers[1], new HashSet<>());
			}

			connections.get(computers[0]).add(computers[1]);
			connections.get(computers[1]).add(computers[0]);
		}
	}

	protected Integer getDayNumber() {
		return 23;
	}

	protected String getExpectedFirstResult() {
		return "7";
	}

	protected String getExpectedSecondResult() {
		return "co,de,ka,ta";
	}

	protected String getFirstResult() {
		HashSet<String> combinations = new HashSet<>();

		for(String computer: connections.keySet()) {
			HashSet<String> computerSet = connections.get(computer);

			for(String neighbor: connections.get(computer)) {
				HashSet<String> neightborSet = connections.get(neighbor);

				HashSet<String> intersection = new HashSet<>(computerSet);
				intersection.retainAll(neightborSet);

				for(String common: intersection) {
					if (computer.startsWith("t") || neighbor.startsWith("t") || common.startsWith("t")) {
						ArrayList<String> list = new ArrayList<>();

						list.add(computer);
						list.add(neighbor);
						list.add(common);

						String k = getSortedString(list);

						combinations.add(k);
					}
				}
			}
		}

		return Integer.toString(combinations.size());
	}

	protected String getSecondResult() {
		HashMap<String, Integer> combinationCount = new HashMap<>();

		for(String computer: connections.keySet()) {
			HashSet<String> others = connections.get(computer);
			for(String other: others) {
				HashSet<String> rest = new HashSet<>(others);
				rest.retainAll(connections.get(other));

				HashSet<String> group = new HashSet<>();
				group.add(computer);
				group.add(other);
				group.addAll(rest);

				String k = getSortedString(group);
				if (combinationCount.containsKey(k)) {
					combinationCount.put(k, combinationCount.get(k) + 1);
				} else {
					combinationCount.put(k, 1);
				}
			}
		}

		String maxGroup = "";
		int max = 0;

		for(String k: combinationCount.keySet()) {
			int len = combinationCount.get(k);
			if (len > max) {
				maxGroup = k;
				max = len;
			}
		}

		return maxGroup;
	}

	protected String getSortedString(Iterable<String> values) {
		ArrayList<String> list = new ArrayList<>();

		for(String s: values) {
			list.add(s);
		}

		Collections.sort(list);

		return String.join(",", list);
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

		new Day23(debug, runTests, runActual).Run();
	}
}
