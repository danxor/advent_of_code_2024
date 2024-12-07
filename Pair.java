public class Pair<X, Y> {
	private X a;
	private Y b;

	public Pair(X first, Y second) {
		this.a = first;
		this.b = second;
	}

	public X getA() {
		return this.a;
	}

	public Y getB() {
		return this.b;
	}

	public void setA(X value) {
		this.a = value;
	}

	public void setB(Y value) {
		this.b = value;
	}

	@Override
	public String toString() {
		return "Pair(A = " + this.a.toString() + ", B=" + this.b.toString() + ")";
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof Pair)) return false;

		Pair o = (Pair)other;
		return (this.a == o.getA() && this.b == o.getB());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime * this.a.hashCode() * this.b.hashCode();
	}
}
