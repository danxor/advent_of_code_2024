import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.Math;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Day11 extends DayRunner {
	private HashMap<String, Long> histo;
	private List<String> lines;

	Day11(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.lines = getLines(fileName);

		this.histo = new HashMap<>();
		String[] parts = this.lines.get(0).split(" ");
		for(String stone: parts) {
			if (this.histo.containsKey(stone)) {
				this.histo.put(stone, this.histo.get(stone) + 1L);
			} else {
				this.histo.put(stone, 1L);
			}
		}
	}

	protected Integer getDayNumber() {
		return 11;
	}

	protected String getExpectedFirstResult() {
		return "55312";
	}

	protected String getExpectedSecondResult() {
		return "65601038650482";
	}

	protected HashMap<String, Long> simulate(HashMap<String, Long> data) {
		HashMap<String, Long> new_h = new HashMap<>();

		for(String stone: data.keySet()) {
			String[] newStones;

			Long count = data.get(stone);

			if ("0".equals(stone)) {
				newStones = new String[] { "1" };
			} else if (stone.length() % 2 == 0) {
				int mid = stone.length() / 2;
				String left = Long.valueOf(stone.substring(0, mid)).toString();
				String right = Long.valueOf(stone.substring(mid, stone.length())).toString();
				newStones = new String[] { left, right };
			} else {
				Long value = Long.valueOf(stone);
				value *= 2024;
				newStones = new String[] { value.toString() };
			}

			for(String s: newStones) {
				if (new_h.containsKey(s)) {
					new_h.put(s, new_h.get(s) + count);
				} else {
					new_h.put(s, count);
				}
			}
		}

		return new_h;
	}

	protected String getFirstResult() {
		for(int i = 0; i < 25; i++) {
			this.histo = simulate(this.histo);
		}

		Long sum = 0L;
		for(Long count: this.histo.values()) {
			sum += count;
		}

		return sum.toString();
	}

	protected String getSecondResult() {
		for(int i = 0; i < 50; i++) {
			this.histo = simulate(this.histo);
		}

		Long sum = 0L;
		for(Long count: this.histo.values()) {
			sum += count;
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

		new Day11(debug, runTests, runActual).Run();
	}
}
