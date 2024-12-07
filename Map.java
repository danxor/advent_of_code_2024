import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.Math;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Map {
	private final Character[][] map;
	private final Integer maxX;
	private final Integer maxY;

	Map(List<String> lines) {
		this.maxY = lines.size();
		if (this.maxY > 0) {
			this.maxX = lines.get(0).length();
		} else {
			this.maxX = 0;
		}

		this.map = new Character[this.maxY][this.maxX];

		for(Integer y = 0; y < this.maxY; y++) {
			String line = lines.get(y);

			for(Integer x = 0; x < this.maxX; x++) {
				this.map[y][x] = line.charAt(x);
			}
		}


	}

	public static Map createFromFile(String fileName) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(fileName));

			return new Map(lines);
		} catch (IOException e) {
			System.out.println(String.format("Failed to read content from %s", fileName));
			e.printStackTrace();
			System.exit(1);
		}

		return null;
	}

	public Integer[] size() {
		return new Integer[] { this.maxX, this.maxY };
	}

	public Boolean isInside(Point pos) {
		if (pos.X >= 0 && pos.X < this.maxX && pos.Y >= 0 && pos.Y < this.maxY) {
			return true;
		}

		return false;
	}

	public Point find(Character c) {
		for(Integer y = 0; y < this.maxY; y++) {
			for(Integer x = 0; x < this.maxX; x++) {
				if (this.map[y][x] == c) {
					return new Point(x, y);
				}
			}
		}

		return null;
	}

	public Character get(Point pos) {
		if (isInside(pos)) {
			return this.map[pos.Y][pos.X];
		}

		return '.';
	}

	public void set(Point pos, Character c) {
		if (isInside(pos)) {
			this.map[pos.Y][pos.X] = c;
		}
	}
}
