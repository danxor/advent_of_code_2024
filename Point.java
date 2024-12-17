public class Point {
	public final long X;
	public final long Y;

	Point(long x, long y) {
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
		return prime * Long.hashCode(this.X) * Long.hashCode(this.Y);
	}

	@Override
	public String toString() {
		return "(" + Long.toString(this.X) + "," + Long.toString(this.Y) + ")";
	}
}
