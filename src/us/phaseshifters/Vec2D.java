package us.phaseshifters;

/**
 *
 * @author Totom3
 */
public class Vec2D {

	public double x, y;

	public Vec2D() {
	}

	public Vec2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vec2D add(Vec2D other) {
		return new Vec2D(this.x + other.x, this.y + other.y);
	}

	public Vec2D subtract(Vec2D other) {
		return new Vec2D(this.x - other.x, this.y - other.y);
	}

	public double dot(Vec2D other) {
		return (this.x * other.x) + (this.y * other.y);
	}

	public double cross(Vec2D other) {
		return (this.x * other.y) - (this.y * other.x);
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}
