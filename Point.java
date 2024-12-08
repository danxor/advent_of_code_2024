import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Point {
	public final int X;
	public final int Y;

	Point(int x, int y) {
		this.X = x;
		this.Y = y;
	}

	public Point add(Point delta) {
		return new Point(this.X + delta.X, this.Y + delta.Y);
	}

	public Point mirror(Point other) {
		return new Point(this.X - (other.X - this.X), this.Y - (other.Y - this.Y));
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
		return prime * Integer.hashCode(this.X) * Integer.hashCode(this.Y);
	}

	@Override
	public String toString() {
		return "(" + Integer.toString(this.X) + "," + Integer.toString(this.Y) + ")";
	}
}
