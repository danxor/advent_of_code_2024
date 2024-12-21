import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.lang.Math;
import astar.Point;

public class Day21 extends DayRunner {
	private final String[] numericKeypad = new String[] { "789", "456", "123", " 0A" };
	private final String[] directionalKeypad = new String[] { " ^A", "<v>" };
	private HashMap<String, Long> minDirectionalCache;
	private HashMap<Character, Point> directionalDict;
	private HashMap<Character, Point> numericDict;
	private List<String> lines;

	Day21(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);
	}

	protected void parse(String fileName) {
		this.lines = getLines(fileName);

		this.numericDict = new HashMap<>();
		for(int j = 0; j < numericKeypad.length; j++) {
			String line = numericKeypad[j];
			for(int i = 0; i < line.length(); i++) {
				numericDict.put(line.charAt(i), new Point(i, j));
			}
		}

		this.directionalDict = new HashMap<>();
		for(int j = 0; j < directionalKeypad.length; j++) {
			String line = directionalKeypad[j];
			for(int i = 0; i < line.length(); i++) {
				directionalDict.put(line.charAt(i), new Point(i, j));
			}
		}

		this.minDirectionalCache = new HashMap<>();
	}

	protected Integer getDayNumber() {
		return 21;
	}

	protected String getExpectedFirstResult() {
		return "126384";
	}

	protected String getExpectedSecondResult() {
		return "154115708116294";
	}

	protected String getFirstResult() {
		Long sum = 0L;

		for(String code: this.lines) {
			long min = minNumericKeypad(code, 1);

			String numeric = code.substring(0, code.length() - 1);
			while(numeric.charAt(0) == '0') {
				numeric = numeric.substring(1);
			}

			long value = Long.valueOf(numeric);

			if (this.debug) {
				System.out.println(code + ": " + Long.toString(min) + " * " + Long.toString(value) + " = " + Long.valueOf(min * value));
			}

			sum += min * value;
		}

		return sum.toString();
	}

	protected String getSecondResult() {
		Long sum = 0L;

		for(String code: this.lines) {
			long min = minNumericKeypad(code, 24);

			String numeric = code.substring(0, code.length() - 1);
			while(numeric.charAt(0) == '0') {
				numeric = numeric.substring(1);
			}

			long value = Integer.valueOf(numeric);

			if (this.debug) {
				System.out.println(code + ": " + Long.toString(min) + " * " + Long.toString(value) + " = " + Long.valueOf(min * value));
			}

			sum += min * value;
		}

		return sum.toString();
	}

	protected long minNumericKeypad(String code, int remain) {
		Point cur = this.numericDict.get('A');
		long total = 0;

		for(int i = 0; i < code.length(); i++) {
			char c = code.charAt(i);

			Point cc = this.numericDict.get(c);
			int dx = cur.x - cc.x;
			int dy = cur.y - cc.y;

			long minStrokes = -1;
			String m = deltaChars(dx, dy);

			List<String> permutations = permutations(m);
			List<String> possiblePermutations = new ArrayList<>();

			for(String possible: permutations) {
				if (isPossible(this.numericKeypad, cur, possible)) {
					possiblePermutations.add(possible);
				}
			}

			if (this.debug) {
				System.out.println(Character.toString(c) + ": [" + getStringArray(possiblePermutations) + "]");
			}

			for(String possible: possiblePermutations) {
				String next_code = possible + 'A';

				long strokes = minDirectionalKeypad(next_code, remain);
				if (minStrokes == -1 || strokes < minStrokes) {
					minStrokes = strokes;
				}
			}

			cur = cc;
			total += minStrokes;
		}

		return total;
	}

	protected long minDirectionalKeypad(String code, int remain) {
		String k = code + ":" + Integer.toString(remain);
		if (this.minDirectionalCache.containsKey(k)) {
			long found = this.minDirectionalCache.get(k);
			return found;
		}

		if (remain == 0) {
			long len = getDistance(code);
			this.minDirectionalCache.put(k, len);
			return len;
		}

		Point cur = this.directionalDict.get('A');
		long total = 0;

		for(int i = 0; i < code.length(); i++) {
			char c = code.charAt(i);

			Point cc = this.directionalDict.get(c);
			int dx = cur.x - cc.x;
			int dy = cur.y - cc.y;

			long minStrokes = -1;
			String m = deltaChars(dx, dy);
			List<String> permutations = permutations(m);

			for(String possible: permutations) {
				if (isPossible(this.directionalKeypad, cur, possible)) {
					String next_code = possible + 'A';

					long strokes = minDirectionalKeypad(next_code, remain - 1);
					if (minStrokes == -1 || strokes < minStrokes) {
						minStrokes = strokes;
					}
				}
			}

			cur = cc;
			total += minStrokes;
		}

		this.minDirectionalCache.put(k, total);

		return total;
	}

	private static String getStringArray(List<String> items) {
		ArrayList<String> parts = new ArrayList<>();
		for(String q: items) {
			parts.add("'" + q + "'");
		}

		return String.join(",", parts);
	}

	protected int getDistance(String code) {
		Point cur = this.directionalDict.get('A');
		int total = 0;

		for(int i = 0; i < code.length(); i++) {
			char c = code.charAt(i);
			Point cc = this.directionalDict.get(c);
			total += 1 + Math.abs(cur.x - cc.x) + Math.abs(cur.y - cc.y);
			cur = cc;
		}

		return total;
	}

	private static boolean isPossible(String[] keypad, Point cur, String code) {
		Point n = cur;

		for(int j = 0; j < code.length(); j++) {
			Character kc = keypad[n.y].charAt(n.x);
			if (kc == ' ') {
				return false;
			}

			char ch = code.charAt(j);
			switch(ch) {
				case '^': n = new Point(n.x, n.y - 1); break;
				case 'v': n = new Point(n.x, n.y + 1); break;
				case '<': n = new Point(n.x - 1, n.y); break;
				case '>': n = new Point(n.x + 1, n.y); break;
				default:
					break;
			}
		}

		return true;
	}

	private static String deltaChars(int dx, int dy) {
		String sy;
		String sx;

		if (dy < 0) sy = appendTimes('v', -dy);
		else sy = appendTimes('^', dy);

		if (dx < 0) sx = appendTimes('>', -dx);
		else sx = appendTimes('<', dx);

		return sy + sx;
	}

	private static String appendTimes(char c, int times) {
		StringBuilder builder = new StringBuilder(times);

		for(int i = 0; i < times; i++) builder.append(c);

		return builder.toString();
	}

	private static List<String> permutations(String s) {
		Set<String> set = new HashSet<>();

		char[] chars = s.toCharArray();

		backtrack(chars, 0, set);

		List<String> result = new ArrayList<>();

		for(String x: set) {
			result.add(x);
		}

		Collections.sort(result);

		return result;
	}

	private static void backtrack(char[] chars, int index, Set<String> result) {
		if (index == chars.length) {
			result.add(new String(chars));
		} else {
			for (int i = index; i < chars.length; i++) {
				swap(chars, index, i);
				backtrack(chars, index + 1, result);
				swap(chars, index, i);
			}
		}
	}

	private static void swap(char[] chars, int i, int j) {
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
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

		new Day21(debug, runTests, runActual).Run();
	}
}
