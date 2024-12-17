import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Region {
	public final int area;
	public final Character letter;
	public final Set<Point> region;

	public Region(Character letter, Set<Point> region) {
		this.letter = letter;
		this.region = region;
		this.area = region.size();
	}

	public int getPerimeter() {
		int perim = 0;

		for(Point p: this.region) {
			for(Point d: Map.STRAIGHT_DELTA) {
				Point n = p.add(d);
				if (!this.region.contains(n)) {
					perim++;
				}
			}
		}

		return perim;
	}

	public int getSides() {
		Set<Vector> perim = new HashSet<>();

		for(Point p: this.region) {
			for(Point d: Map.STRAIGHT_DELTA) {
				Point n = p.add(d);
				if (!this.region.contains(n)) {
					Vector v = new Vector(p, d);
					perim.add(v);
				}
			}
		}

		Set<Vector> covered = new HashSet<>();
		int sides = 0;

		for(Vector v: perim) {
			if (covered.contains(v)) {
				continue;
			}

			List<Point> queue = new ArrayList<>();
			queue.add(v.pos);
			covered.add(v);
			sides++;

			while(queue.size() > 0) {
				Point p = queue.remove(0);

				for(Point d: Map.STRAIGHT_DELTA) {
					Point p2 = p.add(d);
					Vector v2 = new Vector(p2, v.dir);
					if (perim.contains(v2) && !covered.contains(v2)) {
						covered.add(v2);
						queue.add(p2);
					}
				}
			}
		}

		return sides;
	}
}
