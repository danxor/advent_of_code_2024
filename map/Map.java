package map;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Map {
    /**
     * Delta points for moving North, South, East and West.
     */
    public static final Point[] STRAIGHT_DELTA = new Point[] {
        new Point(-1, 0),
        new Point(0, -1),
        new Point(1, 0),
        new Point(0, 1)
    };

    /**
     * Delta points for moving North, South, East and West as well as moving
     * diagonally; i.e. North-West, South-East and so forth.
     */
    public static final Point[] DIAGONAL_DELTA = new Point[] {
        new Point(-1, -1),
        new Point(0, -1),
        new Point(1, -1),
        new Point(-1, 0),
        new Point(1, 0),
        new Point(-1, 1),
        new Point(0, 1),
        new Point(1, 1)
    };

    /**
     * Delta points for only moving diagonally; i.e. only North-West,
     * North-East, South-West and South-East.
     */
    public static final Point[] CROSS_DELTA = new Point[] {
        new Point(-1, -1),
        new Point(1, -1),
        new Point(-1, 1),
        new Point(1, -1)
    };

	private final Character[][] map;
	
	public final int width;
	public final int height;

	Map(int width, int height) {
		this.width = width;
		this.height = height;
		this.map = new Character[height][width];
	}

	Map(List<String> lines) {
		this.height = lines.size();
		if (this.height > 0) {
			this.width = lines.get(0).length();
		} else {
			this.width = 0;
		}

		this.map = new Character[this.height][this.width];

		for(int y = 0; y < this.height; y++) {
			String line = lines.get(y);

			for(int x = 0; x < this.width; x++) {
				this.map[y][x] = line.charAt(x);
			}
		}
	}

	public static Map createFromLines(List<String> lines) {
		return new Map(lines);
	}

	public static Map createFromFile(String fileName) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(fileName));

			return Map.createFromLines(lines);
		} catch (IOException e) {
			System.out.println(String.format("Failed to read content from %s", fileName));
			e.printStackTrace();
			System.exit(1);
		}

		return null;
	}

	public Map clone() {
		Map m = new Map(this.width, this.height);
	
		for(int y = 0; y < this.height; y++) {
			for(int x = 0; x < this.width; x++) {
				m.set(x, y, this.get(x, y));
			}
		}
	
		return m;
	}

	public Integer[] size() {
		return new Integer[] { this.width, this.height };
	}

	public Boolean isInside(Point pos) {
		return this.isInside((int)pos.X, (int)pos.Y);
	}

	public Boolean isInside(int x, int y) {
		if (x >= 0 && x < this.width && y >= 0 && y < this.height) {
			return true;
		}

		return false;
	}

	public Point find(Character c) {
		for(Integer y = 0; y < this.height; y++) {
			for(Integer x = 0; x < this.width; x++) {
				if (this.map[y][x] == c) {
					return new Point(x, y);
				}
			}
		}

		return null;
	}

	public Character get(Point pos) {
		return this.get((int)pos.X, (int)pos.Y);
	}

	public Character get(int x, int y) {
		if (isInside(x, y)) {
			return this.map[y][x];
		}

		return '.';
	}

	public void set(Point pos, Character c) {
		this.set((int)pos.X, (int)pos.Y, c);
	}

	public void set(int x, int y, Character c) {
		if (isInside(x, y)) {
			this.map[y][x] = c;
		}
	}

	public List<Region> getRegions() {
		Set<Point> visited = new HashSet<>();
		List<Region> regions = new ArrayList<>();

		for(int y = 0; y < this.height; y++) {
			for(int x = 0; x < this.width; x++) {
				Point p = new Point(x, y);
				if (visited.contains(p)) continue;
				Region reg = getRegion(visited, p);
				regions.add(reg);
			}
		}

		return regions;
	}

	public void print() {
		System.out.print(" ");
		for(int i = 0; i < this.width; i++) {
			System.out.print(Integer.toString(i % 10));
		}

		System.out.println("");

		for(int y = 0; y < this.width; y++) {
			System.out.print(Integer.toString(y % 10));

			for(int x = 0; x < this.width; x++) {
				Character c = this.get(x, y);
				System.out.print(Character.toString(c));
			}
			System.out.println("");
		}
	}

	public Region getRegion(Set<Point> visited, Point p) {
		Set<Point> region = new HashSet<>();
		List<Point> queue = new ArrayList<>();

		Character letter = this.get(p);
		region.add(p);
		visited.add(p);
		queue.add(p);

		while(queue.size() > 0) {
			p = queue.remove(0);

			for(Point d: Map.STRAIGHT_DELTA) {
				Point n = p.add(d);

				if (visited.contains(n)) continue;

				Character c = this.get(n);
				if (letter == c) {
					visited.add(n);
					region.add(n);
					queue.add(n);
				}
			}
		}

		return new Region(letter, region);
	}
}
