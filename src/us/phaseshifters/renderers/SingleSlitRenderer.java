package us.phaseshifters.renderers;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import us.phaseshifters.ComplexNumber;

/**
 *
 * @author Totom3
 */
public class SingleSlitRenderer implements DiffractionRenderer {

	public static final int WIDTH = 20;
	public static final int HEIGHT = 600;
	public static final int HALF_WIDTH = WIDTH / 2;
	public static final int HALF_HEIGHT = HEIGHT / 2;

	@Override
	public void render(Canvas canvas, DiffractionParameters params, int size, int resolution) {
		final int thetaStepCount = 128;
		final double deltaTheta = 2 * Math.PI / thetaStepCount;
		final double phasorMultiplier = 1_000_000_000 * (1 / params.getDistanceSA() + 1 / params.getDistanceAW()) * Math.PI / params.getWavelength();

		double min = 0;
		double max = 0;
		final int shift = size / 2;
		final int binCount = size / resolution / 2;
		double[][] probabilities = new double[binCount][binCount];

		// Compute probabilities for each bin
		for (int i = 0; i < binCount; ++i) {
			for (int j = 0; j < binCount; ++j) {

				int x = shift - (i * resolution);
				int y = shift - (j * resolution);

				ComplexNumber totalPhasor = outerIntegral(x, y, thetaStepCount, deltaTheta, phasorMultiplier, params);

				double prob = totalPhasor.normSquared();
				probabilities[i][j] = prob;
				if (prob > max) {
					max = prob;
				} else if (prob < min) {
					min = prob;
				}
			}
		}

		// Render each pixel
		double lightShift = 255 - max;
		double lightScale = -(1+(255/(min-max)));
		GraphicsContext graphics = canvas.getGraphicsContext2D();
		for (int i = 0; i < binCount; ++i) {
			for (int j = 0; j < binCount; ++j) {
				int x1 = i * resolution;
				int y1 = j * resolution;

				int x2 = size - 1 - x1 - resolution;
				int y2 = size - 1 - y1 - resolution;

				double intensity = probabilities[i][j];
				intensity += lightShift;
				intensity *= lightScale;
				graphics.setFill(Color.grayRgb((int) intensity));
				graphics.fillRect(x1, y1, resolution, resolution);
				graphics.fillRect(x1, y2, resolution, resolution);
				graphics.fillRect(x2, y1, resolution, resolution);
				graphics.fillRect(x2, y2, resolution, resolution);
			}
		}

		int i = binCount / 2;
		for (int j = 0; j < binCount; ++j) {
			System.out.println(j+"\t"+probabilities[i][j]);
		}
	}

	private ComplexNumber outerIntegral(int x, int y, int thetaStepCount, double deltaTheta, double phasorMultiplier, DiffractionParameters params) {
		ComplexNumber totalPhasor = new ComplexNumber();

		for (int t = 0; t < thetaStepCount; ++t) {
			// Compute theta & friends
			double theta = t * deltaTheta;
			double cos = Math.cos(theta);
			double sin = Math.sin(theta);

			List<Double> r = new ArrayList<>(4);

			// Find intersections with vertical edges
			if (cos != 0) {
				// Compute radii
				double r1 = (-HALF_WIDTH - x) / cos;
				double r2 = (+HALF_WIDTH - x) / cos;

				// Verify validity and add contributions
				if (isRadiusValid(r2, sin, y, HALF_HEIGHT)) {
					r.add(r2);
				}

				if (isRadiusValid(r1, sin, y, HALF_HEIGHT)) {
					r.add(r1);
				}
			}

			// Find intersections with horizontal edges
			if (sin != 0) {
				// Compute radii
				double r3 = (-HALF_HEIGHT - y) / sin;
				double r4 = (+HALF_HEIGHT - y) / sin;

				// Verify validity and add contributions
				if (isRadiusValid(r4, cos, x, HALF_WIDTH)) {
					r.add(r4);
				}

				if (isRadiusValid(r3, cos, x, HALF_WIDTH)) {
					r.add(r3);
				}
			}

			// Add center contribution
			if (Math.abs(x) < HALF_WIDTH && Math.abs(y) < HALF_HEIGHT) {
				totalPhasor.real -= 1;
			} else if (r.size() == 2) {
				double x1 = r.get(0);
				double x2 = r.get(1);

				// Make sure that x1 is smaller than x2
				if (x1 > x2) {
					double temp = x1;
					x1 = x2;
					x2 = temp;
				}

				totalPhasor = totalPhasor.plus(ComplexNumber.exp(x2 * phasorMultiplier))
						.minus(ComplexNumber.exp(x1 * phasorMultiplier));
			}
		}

		return totalPhasor;
	}

	private boolean isRadiusValid(double radius, double trig, int shift, int lim) {
		return radius >= 0 && Math.abs(radius * trig + shift) < lim;
	}
}
