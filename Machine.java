import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Machine {
    private Map<String, Long> registers;
    private List<Long> outputs;
    private final int[] program;

    public Machine(Map<String, Long> registers, int[] program) {
        this.registers = registers;
        this.program = program;
        this.outputs = new ArrayList<>();
    }

    public boolean run(Map<Integer, Operation> instructions) {
        int ip = 0;

        while(ip >= 0 && ip < this.program.length) {
            ip = singleStep(instructions, ip);
        }

        return (ip > this.program.length);
    }

    public int singleStep(Map<Integer, Operation> instructions, int ip) {
        if (ip >= 0 && ip < this.program.length) {
            int instruction = this.program[ip];
            if (instructions.containsKey(instruction)) {
                Operation op = instructions.get(instruction);

                int num = op.getNumberOfOperands();
                int operands[] = new int[num];
                for(int i = 0; i < num; i++) {
                    operands[i] = this.program[1 + i + ip];
                }

                return op.perform(operands, this, ip);
            } else {
                throw new IndexOutOfBoundsException("Illegal instruction encountered: " + Integer.toString(instruction));
            }
        }

        return -1;
    }

    public void reset() {
        this.outputs = new ArrayList<>();
        for(String key: this.registers.keySet()) {
            this.registers.put(key, 0L);
        }
    }

    public void set(String register, long value) {
        this.registers.put(register, value);
    }

    public long get(String register) {
        if (this.registers.containsKey(register)) {
            return this.registers.get(register);
        }

        throw new IndexOutOfBoundsException("Cannot get value of register: " + register);
    }

    public void output(long value) {
        this.outputs.add(value);
    }

    public int[] getProgram() {
        return this.program;
    }

    public long[] getOutputs() {
        long[] outputs = new long[this.outputs.size()];
        for(int i = 0; i < this.outputs.size(); i++) {
            outputs[i] = this.outputs.get(i);
        }
        return outputs;
    }

    public long getComboValue(int combo) {
        switch(combo) {
            case 0:
            case 1:
            case 2:
            case 3:
                return combo;
            case 4:
                return this.get("A");
            case 5:
                return this.get("B");
            case 6:
                return this.get("C");
            default:
                throw new IndexOutOfBoundsException("The combo value if out of range: " + Integer.toString(combo));
        }
    }

    public interface Operation {
        public int getNumberOfOperands();
        public int perform(int[] operands, Machine m, int ip);
    }
}
