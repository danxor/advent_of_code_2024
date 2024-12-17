import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Day17 extends DayRunner {
	private Map<Integer, Machine.Operation> instructionSet;
	private List<String> lines;

	Day17(Boolean debug, Boolean runTests, Boolean runActual) {
		super(debug, runTests, runActual);

		this.instructionSet = new HashMap<>();
		this.instructionSet.put(0, new ComboRegisterDivide("A", "A"));
		this.instructionSet.put(1, new XorLiteral("B", "B"));
		this.instructionSet.put(2, new ComboModulo("B"));
		this.instructionSet.put(3, new JumpNotZero("A"));
		this.instructionSet.put(4, new XorRegister("B", "C", "B"));
		this.instructionSet.put(5, new Output());
		this.instructionSet.put(6, new ComboRegisterDivide("A", "B"));
		this.instructionSet.put(7, new ComboRegisterDivide("A", "C"));
	}

	protected void parse(String fileName) {
		this.lines = getLines(fileName);
	}

	protected Integer getDayNumber() {
		return 17;
	}

	protected String getExpectedFirstResult() {
		return "4,6,3,5,6,3,5,2,1,0";
	}

	protected String getExpectedSecondResult() {
		return "29328";
	}

	protected String getFirstResult() {
		Machine m = this.createMachine(this.lines);

		m.run(this.instructionSet);

		ArrayList<String> numbers = new ArrayList<>();
		for(long v: m.getOutputs()) {
			numbers.add(Long.toString(v));
		}

		return String.join(",", numbers);
	}

	protected String getSecondResult() {
		Machine m = this.createMachine(lines);

		int[] program = m.getProgram();
		List<Long> possible = new ArrayList<>();
		possible.add(0L);

		for(int i = program.length - 1; i >= 0; i--) {
			int pi = program[i];

			List<Long> nextPossible = new ArrayList<>();

			for(int a = 0; a < 8; a++) {
				for(long last_a: possible) {
					long na = a + (last_a << 3);
					long res = runSingleRound(m, program, na);
					if ((res & 7) == pi) {
						nextPossible.add(na);
					}
				}
			}

			if (this.debug) {
				System.out.print(Integer.toString(pi) +" [");

				String[] parts = new String[nextPossible.size()];
				for(int k = 0; k < nextPossible.size(); k++) {
					parts[k] = Long.toString(nextPossible.get(k));
				}

				System.out.println(String.join(", ", parts) + "]");
			}

			possible = nextPossible;
		}

		long min = possible.get(0);
		for(long v: possible) {
			if (v < min) {
				min = v;
			}
		}

		return Long.toString(min);
	}

	protected long runSingleRound(Machine m, int[] program, long na) {
		m.reset();
		m.set("A", na);

		for(int ip = 0; ip < program.length; ip += 2) {
			int instruction = program[ip];
			if (instruction == 3) {
				continue;
			} else {
				m.singleStep(this.instructionSet, ip);
				if (instruction == 5) {
					long[] output = m.getOutputs();
					return output[0];
				}
			}
		}

		return -1;
	}

	public Machine createMachine(List<String> lines) {
		Map<String, Long> registers = new HashMap<>();
		List<Integer> program = new ArrayList<>();

		boolean isRegisterParse = true;

		program = new ArrayList<>();

		for(String line: lines) {
			if (line.length() == 0) {
				isRegisterParse = false;
				continue;
			}

			if (isRegisterParse) {
				String register = line.substring(9, 10);
				long value = Long.valueOf(line.substring(12));
				registers.put(register, value);
			} else {
				for(String num: line.substring(9).split(",")) {
					if(num.length() == 0) {
						continue;
					}

					program.add(Integer.valueOf(num));
				}
			}
		}

		int[] p = new int[program.size()];
		for(int i = 0; i < program.size(); i++) {
			p[i] = program.get(i);
		}

		return new Machine(registers, p);
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

		new Day17(debug, runTests, runActual).Run();
	}

	// adv, bdv, cdv
	public class ComboRegisterDivide implements Machine.Operation {
		public String src;
		public String dst;

		public ComboRegisterDivide(String src, String dst) {
			this.src = src;
			this.dst = dst;
		}

		@Override
		public int getNumberOfOperands() {
			return 1;
		}

		@Override
		public int perform(int[] operands, Machine m, int ip) {
			long numerator = m.get(this.src);
			long denominator = m.getComboValue(operands[0]);
			m.set(this.dst, numerator >> denominator);
			return ip + 2;
		}
	}

	// bxl
	public class XorLiteral implements Machine.Operation {
		public String src;
		public String dst;

		public XorLiteral(String src, String dst) {
			this.src = src;
			this.dst = dst;
		}

		@Override
		public int getNumberOfOperands() {
			return 1;
		}

		@Override
		public int perform(int[] operands, Machine m, int ip) {
			long value = m.get(this.src);
			m.set(this.dst, value ^ operands[0]);
			return ip + 2;
		}
	}

	// bxc
	public class XorRegister implements Machine.Operation {
		public String a;
		public String b;
		public String dst;

		public XorRegister(String a, String b, String dst) {
			this.a = a;
			this.b = b;
			this.dst = dst;
		}

		@Override
		public int getNumberOfOperands() {
			return 1;
		}

		@Override
		public int perform(int[] operands, Machine m, int ip) {
			long a = m.get(this.a);
			long b = m.get(this.b);
			m.set(this.dst, a ^ b);
			return ip + 2;
		}
	}


	// bst
	public class ComboModulo implements Machine.Operation {
		public String dst;

		public ComboModulo(String dst) {
			this.dst = dst;
		}

		@Override
		public int getNumberOfOperands() {
			return 1;
		}

		@Override
		public int perform(int[] operands, Machine m, int ip) {
			long operand = m.getComboValue(operands[0]) & 7;
			m.set(this.dst, operand);
			return ip + 2;
		}
	}

	// jnz
	public class JumpNotZero implements Machine.Operation {
		public String src;

		public JumpNotZero(String src) {
			this.src = src;
		}

		@Override
		public int getNumberOfOperands() {
			return 1;
		}

		@Override
		public int perform(int[] operands, Machine m, int ip) {
			if (m.get(this.src) == 0) {
				return ip + 2;
			}

			return operands[0];
		}
	}

	// out
	public class Output implements Machine.Operation {
		@Override
		public int getNumberOfOperands() {
			return 1;
		}

		@Override
		public int perform(int[] operands, Machine m, int ip) {
			long value = m.getComboValue(operands[0]) & 7;
			m.output(value);
			return ip + 2;
		}
	}
}
