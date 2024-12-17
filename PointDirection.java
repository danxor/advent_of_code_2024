public class PointDirection {
    public final int x;
    public final int y;
    public final int d;

    public PointDirection(int x, int y, int d) {
        this.x = x;
        this.y = y;
        this.d = d;
    }

    public PointDirection(Point point, int d) {
        this.x = (int)point.X;
        this.y = (int)point.Y;
        this.d = d;
    }

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof PointDirection)) return false;

		PointDirection o = (PointDirection)other;
		return (this.x == o.x && this.y == o.y && this.d == o.d);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime * Integer.hashCode(this.x) * Integer.hashCode(this.y) * Integer.hashCode(this.d);
	}

	@Override
	public String toString() {
		return "( X=" + Integer.toString(this.x) + ", Y=" + Integer.toString(this.y) + ", D=" + Integer.toString(this.d) + " )";
	}
}
