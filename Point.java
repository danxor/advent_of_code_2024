import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Point {
	public final Integer X;
	public final Integer Y;

	Point(Integer x, Integer y) {
		this.X = x;
		this.Y = y;
	}

	public Point add(Point delta) {
		return new Point(this.X + delta.X, this.Y + delta.Y);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof Point)) return false;

		Point o = (Point)other;
		return (this.X == o.X && this.Y == o.Y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime * this.X.hashCode() * this.Y.hashCode();
	}

	@Override
	public String toString() {
		return "(" + this.X.toString() + "," + this.Y.toString() + ")";
	}
}
