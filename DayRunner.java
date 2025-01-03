import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.lang.Class;
import java.time.LocalDateTime;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.reflect.*;

public abstract class DayRunner {
	public Boolean debug = false;
	public Boolean runTests = true;
	public Boolean runActual = true;

	public DayRunner(Boolean debug, Boolean runTests, Boolean runActual) {
		this.debug = debug;
		this.runTests = runTests;
		this.runActual = runActual;
	}

	protected static List<String> getLines(String fileName) {
		try {
			return Files.readAllLines(Paths.get(fileName));
		} catch (IOException e) {
			System.out.println(String.format("Failed to read content from %s", fileName));
			e.printStackTrace();
			System.exit(1);
		}

		return null;
	}

	protected void Run() {
		Integer day = getDayNumber();

		if (this.runTests) {
			String exampleFileName = String.format("data/example%02d.txt", day);

			this.parse(exampleFileName);

			String expectedFirstResult = this.getExpectedFirstResult();
			String actualFirstResult = this.getFirstResult();

			Assert(expectedFirstResult, actualFirstResult, 1);

			if (!runActual) {
				System.out.println(String.format("Day #%02d, part 1: %s", day, actualFirstResult));
			}

			String expectedSecondResult = this.getExpectedSecondResult();
			String actualSecondResult = this.getSecondResult();

			Assert(expectedSecondResult, actualSecondResult, 2);

			if (!runActual) {
				System.out.println(String.format("Day #%02d, part 2: %s", day, actualSecondResult));
			}
		}

		if (this.runActual) {
			String inputFileName = String.format("data/input%02d.txt", day);
			LocalDateTime now = LocalDateTime.now();
			
			InputDownloader.ensureInputExists(now.getYear(), day, inputFileName);

			this.parse(inputFileName);

			String firstResult = this.getFirstResult();
			System.out.println(String.format("Day #%02d, part 1: %s", day, firstResult));

			String secondResult = this.getSecondResult();
			System.out.println(String.format("Day #%02d, part 2: %s", day, secondResult));
		}
	}

	public static void main(String[] args) {
		List<Integer> runDays = new ArrayList<>();
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
			} else if (arg.equals("--today")) {
				LocalDateTime now = LocalDateTime.now();
				Integer today = now.getDayOfMonth();
				if (now.getYear() == 2024 && now.getMonthValue() == 12 && today <= 25) {
					runDays.add(today);
				}
			} else if (isInteger(arg)) {
				runDays.add(Integer.valueOf(arg));
			}
		}

		if (runDays.size() == 0) {
			LocalDateTime now = LocalDateTime.now();
			Integer maxDays = 25;

			if (now.getYear() < 2024 || now.getMonthValue() < 12) {
				maxDays = 0;
			} else if (now.getDayOfMonth() < 25) {
				maxDays = now.getDayOfMonth();
			}

			for(Integer day = 1; day <= maxDays; day++) {
				runDays.add(day);
			}

			Integer hour = now.getHour();
			if (hour >= 6) {
				runDays.add(now.getDayOfMonth());
			}
		}

		for(Integer day: runDays) {
			createInstance(day, debug, runTests, runActual).Run();
		}
	}

	private void Assert(String expected, String actual, int part) {
		if (!expected.equals(actual)) {
			System.out.println("Test for day " + this.getDayNumber().toString() + " part " + Integer.toString(part) + " failed:");
			System.out.println("Expected: " + expected);
			System.out.println("Actual:   " + actual);
			System.exit(1);
		}
	}

	private static DayRunner createInstance(Integer day, Boolean debug, Boolean runTests, Boolean runActual) {
		String className = String.format("Day%02d", day);

		try {
			@SuppressWarnings("rawtypes")
			Class[] constructorDef = new Class[] { Boolean.class, Boolean.class, Boolean.class };
			Object[] constructorArguments = new Object[] { debug, runTests, runActual };

			Class<?> clazz = Class.forName(className);

			@SuppressWarnings("rawtypes")
			Constructor constructor = clazz.getDeclaredConstructor(constructorDef);
			Object obj = constructor.newInstance(constructorArguments);

			return (DayRunner)obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
		}

		return false;
	}

	protected abstract void parse(String fileName);
	protected abstract Integer getDayNumber();
	protected abstract String getExpectedFirstResult();
	protected abstract String getExpectedSecondResult();
	protected abstract String getFirstResult();
	protected abstract String getSecondResult();
}
