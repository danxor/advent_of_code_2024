import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InputDownloader {
	private static String[] getCurlCommand(String url, String outputFileName) {
		String fileName = "cookie.txt";

		Boolean fileExists = Files.exists(Paths.get(fileName));
		if (!fileExists) {
			System.out.println("You are missing the " + fileName + " file");
			System.exit(1);
		}

		List<String> parameters = new ArrayList<>();
		parameters.add("/usr/bin/curl");

		try {
			for(String line : Files.readAllLines(Paths.get(fileName))) {
				parameters.add("-H");
				parameters.add("Cookie: " + line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		parameters.add(url);
		parameters.add("-o");
		parameters.add(outputFileName);

		return parameters.stream().toArray(String[]::new);
	}

	public static void ensureInputExists(Integer year, Integer day, String fileName) {
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
}
