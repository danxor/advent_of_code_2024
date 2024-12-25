import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Day25 extends DayRunner {
	private List<List<Integer>> keys;
	private List<List<Integer>> locks;

	Day25(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		String data = "";

		try {
			byte[] bytes = Files.readAllBytes(Paths.get(fileName));
			data = new String(bytes);
		} catch (IOException e) {
			System.out.println(String.format("Failed to read content from %s", fileName));
			e.printStackTrace();
			System.exit(1);
		}

		String[] parts = data.split("\r?\n\r?\n");

		this.locks = new ArrayList<>();
		this.keys = new ArrayList<>();

		for(String part: parts) {
			String[] lines = part.split("\r?\n");

			ArrayList<String> schematic = new ArrayList<>();
			for(String s: lines) schematic.add(s);

			if ("#####".equals(schematic.get(0))) {
				List<Integer> heights = getHeights(schematic);
				locks.add(heights);
			} else {
				Collections.reverse(schematic);
				List<Integer> heights = getHeights(schematic);

				keys.add(heights);
			}
		}

		if (this.debug) {
			System.out.println("Keys:");
			for(List<Integer> key: this.keys) {
				System.out.println("[" + getListString(key) + "]");
			}

			System.out.println("Locks:");
			for(List<Integer> lock: this.locks) {
				System.out.println("[" + getListString(lock) + "]");
			}
		}
	}

	protected Integer getDayNumber() {
		return 25;
	}

	protected String getExpectedFirstResult() {
		return "3";
	}

	protected String getExpectedSecondResult() {
		return "X-MAS";
	}

	protected String getFirstResult() {
		Integer sum = 0;

		for(List<Integer> lock: locks) {
			for(List<Integer> key: keys) {
				String l = getListString(lock);
				String k = getListString(key);

				if (fits(key, lock)) {
					if (this.debug) {
						System.out.println("[" + l + "] [" + k + "] fits");
					}
					sum++;
				} else {
					if (this.debug) {
						System.out.println("[" + l + "] [" + k + "] overlaps");
					}
				}
			}

		}

		return sum.toString();
	}

	protected String getSecondResult() {
		return "X-MAS";
	}

	protected boolean fits(List<Integer> key, List<Integer> lock) {
		for(int i = 0; i < key.size(); i++) {
			int sum = key.get(i) + lock.get(i);
			if (sum > 5) {
				return false;
			}
		}

		return true;
	}

	protected List<Integer> getHeights(List<String> lines) {
		List<Integer> heights = new ArrayList<>();
		
		for(int i = 0; i < lines.get(0).length(); i++) heights.add(lines.size() - 1);

		for(int y = 1; y < lines.size(); y++) {
			String line = lines.get(y);
			int x = -1;

			while(x < line.length()) {
				x = line.indexOf(".", x + 1);
				if (x < 0) x = line.length();
				else if (y - 1 < heights.get(x)) {
					heights.set(x, y - 1);
				}
			}
		}

		return heights;
	}

	protected String getListString(List<Integer> list) {
		List<String> converted = new ArrayList<>();

		for(Integer x: list) {
			converted.add(Integer.toString(x));
		}

		return String.join(", ", converted);
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

		new Day25(debug, runTests, runActual).Run();
	}
}
