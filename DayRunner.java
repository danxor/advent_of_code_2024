import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.lang.Class;
import java.time.LocalDateTime;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class DayRunner {
	public Boolean debug = false;

	public DayRunner() {
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

	public static void main(String[] args) {
		Boolean runTests = true;
		Boolean runActual = true;
		Boolean runDebug = false;
		List<Integer> runDays = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();

		for(String arg: args) {
			if (arg.equals("--skip-tests")) {
				runTests = false;
			} else if (arg.equals("--only-tests"))  {
				runActual = false;
			} else if (arg.equals("--debug")) {
				runDebug = true;
			} else if (isInteger(arg)) {
				runDays.add(Integer.parseInt(arg));
			}
		}

		if (runDays.size() == 0) {
			for(Integer day = 1; day < now.getDayOfMonth(); day++) {
				runDays.add(day);
			}

			Integer hour = now.getHour();
			if (hour >= 6) {
				runDays.add(now.getDayOfMonth());
			}
		}

		for(Integer day: runDays) {
			String className = String.format("Day%02d", day);
			String exampleFileName = String.format("data/example%02d.txt", day);
			String inputFileName = String.format("data/input%02d.txt", day);

			DayRunner instance = createInstance(className);
			instance.debug = runDebug;

			if (runTests) {
				instance.parse(exampleFileName);

				String expectedFirstResult = instance.getExpectedFirstResult();
				String actualFirstResult = instance.getFirstResult();

				Assert(expectedFirstResult, actualFirstResult);

				if (!runActual) {
					System.out.println(String.format("Day #%02d, part 1: %s", day, actualFirstResult));
				}

				String expectedSecondResult = instance.getExpectedSecondResult();
				String actualSecondResult = instance.getSecondResult();

				Assert(expectedSecondResult, actualSecondResult);

				if (!runActual) {
					System.out.println(String.format("Day #%02d, part 2: %s", day, actualSecondResult));
				}
			}

			if (runActual) {
				ensureInputExists(now.getYear(), day, inputFileName);

				instance.parse(inputFileName);

				String firstResult = instance.getFirstResult();
				System.out.println(String.format("Day #%02d, part 1: %s", day, firstResult));

				String secondResult = instance.getSecondResult();
				System.out.println(String.format("Day #%02d, part 2: %s", day, secondResult));
			}
		}
	}

	private static String[] getCurlCommand(String url, String outputFileName) {
		String fileName = "cookie.txt";

		Boolean fileExists = Files.exists(Paths.get(fileName));
		if (!fileExists) {
			System.out.println("You are missing the " + fileName + " file");
			System.exit(1);
		}

		List<String> parameters = new ArrayList<>();
		parameters.add("/usr/bin/curl");

		for(String line : getLines(fileName)) {
			parameters.add("-H");
			parameters.add("Cookie: " + line);
		}

		parameters.add(url);
		parameters.add("-o");
		parameters.add(outputFileName);

		return parameters.stream().toArray(String[]::new);
	}

	private static void ensureInputExists(Integer year, Integer day, String fileName) {
		Boolean fileExists = Files.exists(Paths.get(fileName));
		if (!fileExists) {
			String downloadUrl = String.format("https://adventofcode.com/%d/day/%d/input", year, day);

			String[] parameters = getCurlCommand(downloadUrl, fileName);

			ProcessBuilder pb = new ProcessBuilder(parameters);

			try {
				Process process = pb.start();

				Integer exitCode = process.waitFor();
				if (exitCode != 0) {
					System.out.println(String.format("curl exited with an error: %d", exitCode));
					System.exit(1);
				}
			} catch (InterruptedException | IOException e) {
				System.out.println("Failed to run curl");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	private static void Assert(String expected, String actual) {
		if (!expected.equals(actual)) {
			System.out.println("Expected: " + expected);
			System.out.println("Actual:   " + actual);
			System.exit(1);
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

	private static DayRunner createInstance(String className) {
		try {
			Class<?> clazz = Class.forName(className);

			DayRunner instance = (DayRunner)clazz.getDeclaredConstructor().newInstance();

			return instance;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	protected abstract void parse(String fileName);
	protected abstract String getExpectedFirstResult();
	protected abstract String getExpectedSecondResult();
	protected abstract String getFirstResult();
	protected abstract String getSecondResult();
}
