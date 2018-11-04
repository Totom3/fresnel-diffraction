package us.phaseshifters;

/**
 *
 * @author Totom3
 */
public class ComplexNumber {

	public static ComplexNumber exp(double x) {
		return new ComplexNumber(Math.cos(x), Math.sin(x));
	}

	public double real, imaginary;

	public ComplexNumber() {
	}

	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	public double norm() {
		return Math.sqrt(normSquared());
	}

	public double normSquared() {
		return (real * real) + (imaginary * imaginary);
	}

	public double phase() {
		return Math.atan2(imaginary, real);
	}

	public ComplexNumber plus(ComplexNumber other) {
		return new ComplexNumber(this.real + other.real, this.imaginary + other.imaginary);
	}

	public ComplexNumber minus(ComplexNumber other) {
		return new ComplexNumber(this.real - other.real, this.imaginary - other.imaginary);
	}

	public ComplexNumber scale(double k) {
		return new ComplexNumber(k * real, k * imaginary);
	}

	public ComplexNumber scaleInverse(double k) {
		return new ComplexNumber(real / k, imaginary / k);

	}

	@Override
	public String toString() {
		return String.format("%.2f %.2fi", real, imaginary);
	}
}
