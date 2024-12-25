import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day24 extends DayRunner {
	private HashMap<String, Integer> values;
	private HashMap<String, Operation> instructions;
	private final Pattern pattern;

	Day24(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);

		pattern = Pattern.compile("([^ ]+) ([^ ]+) ([^ ]+) -> ([^ ]+)");
	}

	protected void parse(String fileName) {
		this.values = new HashMap<>();
		this.instructions = new HashMap<>();

		boolean isValues = true;
		for(String s: getLines(fileName)) {
			if  (s.length() == 0) {
				isValues = false;
			} else if (isValues) {
				String[] parts = s.split(":");
				this.values.put(parts[0].trim(), Integer.valueOf(parts[1].trim()));
			} else {
				Matcher matcher = this.pattern.matcher(s.trim());
				if (matcher.find()) {
					Operation x = new Operation(matcher.group(1), matcher.group(3), matcher.group(2), matcher.group(4));
					this.instructions.put(x.o, x);
				}
			}
		}
	}

	protected Integer getDayNumber() {
		return 24;
	}

	protected String getExpectedFirstResult() {
		return "2024";
	}

	protected String getExpectedSecondResult() {
		return "";
	}

	protected String getFirstResult() {
		Long res = 0L;

		HashMap<String, Operation> instructions = new HashMap<>(this.instructions);
		HashMap<String, Integer> values = deepCopy(this.values);

		simulate(instructions, values);
		res = getBinaryNumber("z", values);

		return res.toString();
	}

	protected String getSecondResult() {
		List<String> sortedOutputs = new ArrayList<>();

		for(String key: this.instructions.keySet()) {
			if (!key.startsWith("z")) {
				continue;
			}

			sortedOutputs.add(key);
		}

		Collections.sort(sortedOutputs);

		HashMap<String, String> expected = new HashMap<>();

		String old = "";

		for(String key: sortedOutputs) {
			String expr = "";

			int bit = Integer.valueOf(key.substring(1));
			if (bit == 0) {
				expr = "(x00 XOR y00)";
			} else {
				String left;

				if (bit == 1) {
					left = String.format("(x%02d AND y%02d)", bit - 1, bit - 1);
				} else {
					left = String.format("((%s AND (x%02d XOR y%02d)) OR (x%02d AND y%02d))", old, bit - 1, bit - 1, bit - 1, bit - 1);
				}

				old = left;

				String k = String.format("z%02d", bit + 1);
				if (this.instructions.containsKey(k)) {
					expr = String.format("(%s XOR (x%02d XOR y%02d))", left, bit, bit);
				} else {
					expr = left;
				}
			}

			expected.put(key, expr);
		}

		List<String> swapped = new ArrayList<>();
		Set<String> swappable = new HashSet<>(this.instructions.keySet());

		for(String key: sortedOutputs) {
			String expectedExpression = expected.get(key);

			WireExpression currentExpresssion = createWireExpression(key);
			if (!expectedExpression.equals(currentExpresssion.expr)) {
				Set<Combo> combinations = getCombinations(swappable);
				for(Combo p: combinations) {
					swap(p.a, p.b);

					WireExpression newCurrentExpression = createWireExpression(key);
					if (expectedExpression.equals(newCurrentExpression.expr)) {
						if (this.debug) {
							System.out.println("Wire " + key + " was fixed by swapping " + p.a + " with " + p.b);
						}

						swapped.add(p.a);
						swapped.add(p.b);

						for(String k: newCurrentExpression.wires) {
							swappable.remove(k);
						}

						break;
					} else {
						swap(p.a, p.b);
					}
				}
			}
		}

		Collections.sort(swapped);

		return String.join(",", swapped);
	}

	protected void swap(String a, String b) {
		Operation x = this.instructions.get(a);
		Operation y = this.instructions.get(b);
		this.instructions.put(a, y);
		this.instructions.put(b, x);
	}

	protected Set<Combo> getCombinations(Set<String> items) {
		Set<Combo> result = new HashSet<>();

		for(String a: items) {
			for(String b: items) {
				if (a.equals(b)) {
					continue;
				}

				result.add(new Combo(a, b));
			}
		}

		return result;
	}

	protected WireExpression createWireExpression(String wire) {
		Set<String> wires = new HashSet<>();

		String expr = generateExpression(wire, wires);

		return new WireExpression(expr, wires);
	}

	protected String generateExpression(String wire, Set<String> wires) {
		if (!instructions.containsKey(wire)) {
			return wire;
		}

		if (wires.contains(wire)) {
			return "WTF";
		}

		wires.add(wire);

		Operation op = instructions.get(wire);
		String expr_left = generateExpression(op.a, wires);
		String expr_right = generateExpression(op.b, wires);

		List<String> exprs = new ArrayList<>();
		exprs.add(expr_left);
		exprs.add(expr_right);

		Collections.sort(exprs);

		expr_left = exprs.get(0);
		expr_right = exprs.get(1);

		return String.format("(%s %s %s)", expr_left, op.op, expr_right);
	}

	protected boolean simulate(HashMap<String, Operation> instructions, HashMap<String, Integer> values) {
		while(!instructions.isEmpty()) {
			ArrayList<String> remove = new ArrayList<>();

			for(String key: instructions.keySet()) {
				Operation inst = instructions.get(key);

				if (!values.containsKey(inst.a)) {
					continue;
				}

				if (!values.containsKey(inst.b)) {
					continue;
				}

				remove.add(key);

				int a = values.get(inst.a);
				int b = values.get(inst.b);
				int r = -1;

				switch(inst.op) {
					case "AND":
						r = a & b;
						break;
					case "XOR":
						r = a ^ b;
						break;
					case "OR":
						r = a | b;
						break;
					default:
						continue;
				}

				values.put(inst.o, r);
			}

			for(String k: remove) {
				instructions.remove(k);
			}
		}

		return true;
	}

	protected long getBinaryNumber(String prefix, HashMap<String, Integer> values) {
		Long res = 0L;

		ArrayList<String> keys = new ArrayList<>();
		for(String k: values.keySet()) {
			if (k.startsWith(prefix)) {
				keys.add(k);
			}
		}

		Collections.sort(keys);
		Collections.reverse(keys);

		for(String k: keys) {
			int z = values.get(k);
			if (this.debug) {
				System.out.println(k + ": " + Integer.toString(z));
			}
			res = (res << 1) + z;
		}

		return res;
	}

	protected HashMap<String, Integer> deepCopy(HashMap<String, Integer> s) {
		HashMap<String, Integer> d = new HashMap<>();
		
		for(String k: s.keySet()) {
			int v = s.get(k);
			d.put(k, v);
		}

		return d;
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

		new Day24(debug, runTests, runActual).Run();
	}

	public class WireExpression {
		public final String expr;
		public final Set<String> wires;

		WireExpression(String expr, Set<String> wires) {
			this.expr = expr;
			this.wires = wires;
		}
	}

	public class Combo {
		public final String a;
		public final String b;

		public Combo(String a, String b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public String toString() {
			return "(" + this.a  + "," + this.b + ")";
		}
	
		@Override
		public boolean equals(Object other) {
			if (other == null) return false;
			if (other == this) return true;
			if (!(other instanceof Combo)) return false;
	
			Combo o = (Combo)other;
			return (this.a == o.a && this.b == o.b);
		}
	
		@Override
		public int hashCode() {
			final int prime = 31;
			return prime * this.a.hashCode() * this.b.hashCode();
		}
	}

	public class Operation {
		public final String a;
		public final String b;
		public final String op;
		public final String o;

		public Operation(String a, String b, String op, String o) {
			this.a = a;
			this.b = b;
			this.op = op;
			this.o = o;
		}

		@Override
		public String toString() {
			return "Operation(A = " + this.a.toString() + ", B=" + this.b.toString() + ", Operation=" + this.op.toString() + ", O=" + this.o.toString() + ")";
		}
	
		@Override
		public boolean equals(Object other) {
			if (other == null) return false;
			if (other == this) return true;
			if (!(other instanceof Operation)) return false;
	
			Operation o = (Operation)other;
			return (this.a == o.a && this.b == o.b && this.op == o.op && this.o == o.o);
		}
	
		@Override
		public int hashCode() {
			final int prime = 31;
			return prime * this.a.hashCode() * this.b.hashCode() * this.op.hashCode() * this.o.hashCode();
		}
	}
}