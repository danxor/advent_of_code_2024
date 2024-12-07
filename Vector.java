import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

public class Vector {
	public final Point pos;
	public final Point dir;

	Vector(Point pos, Point dir) {
		this.pos = pos;
		this.dir = dir;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof Vector)) return false;

		Vector o = (Vector)other;
		return (this.pos.equals(o.pos) && this.dir.equals(o.dir));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime * this.pos.hashCode() * this.dir.hashCode();
	}

	@Override
	public String toString() {
		return "Vector(pos=" + this.pos.toString() + ", dir=" + this.dir.toString() + ")";
	}
}
