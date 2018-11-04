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

	public static final double WIDTH = 10;
	public static final double HALF_WIDTH = WIDTH / 2;

	private static final int MAX_RADIUS = 1_000_000;

	@Override
	public void render(Canvas canvas, DiffractionParameters params, int size, int resolution) {
		final int thetaStepCount = 256;
		final double deltaTheta = 2 * Math.PI / thetaStepCount;
		final double phasorMultiplier = 1_000_000_000 * (1 / params.getDistanceSA() + 1 / params.getDistanceAW()) * Math.PI / params.getWavelength();

		double min = 0;
		double max = 0;
		final int shift = size / 2;
		final int binCount = size / resolution / 2;
		double[] probabilities = new double[binCount];

		// Compute probabilities for each bin
		for (int i = 0; i < binCount; ++i) {

			int x = 10 * (shift - (i * resolution));
			ComplexNumber totalPhasor = outerIntegral(x, thetaStepCount, deltaTheta, phasorMultiplier, params);
			totalPhasor = totalPhasor.scale(deltaTheta);
			
			double prob = totalPhasor.normSquared();
			probabilities[i] = prob;
			if (prob > max) {
				max = prob;
			} else if (prob < min) {
				min = prob;
			}
		}

		double lightShift = 255 - max;
		double lightScale = -(1 + 255 / (min - max));

		// Render each pixel
		GraphicsContext graphics = canvas.getGraphicsContext2D();
		for (int i = 0; i < binCount; ++i) {
			for (int j = 0; j < binCount; ++j) {
				int x1 = i * resolution;
				int y1 = j * resolution;

				int x2 = size - 1 - x1 - resolution;
				int y2 = size - 1 - y1 - resolution;

				double intensity = probabilities[i];
				intensity *= 2;
				
				intensity += lightShift;
				intensity = lightScale * (intensity - 255) + intensity;
				
				intensity = Math.min(255, intensity);

				graphics.setFill(Color.grayRgb((int) intensity));
				graphics.fillRect(x1, y1, resolution, resolution);
				graphics.fillRect(x1, y2, resolution, resolution);
				graphics.fillRect(x2, y1, resolution, resolution);
				graphics.fillRect(x2, y2, resolution, resolution);
			}
		}

		for (int i = 0; i < binCount; ++i) {
			System.out.println(i + "\t" + probabilities[i]);
		}
	}

	private ComplexNumber outerIntegral(int x, int thetaStepCount, double deltaTheta, double phasorMultiplier, DiffractionParameters params) {
		ComplexNumber totalPhasor = new ComplexNumber();

		for (int t = 0; t < thetaStepCount; ++t) {
			// Compute theta & friends
			double theta = t * deltaTheta;
			double cos = Math.cos(theta);

			List<Double> r = new ArrayList<>(4);

			// Find intersections with vertical edges
			if (cos != 0) {
				// Compute radii
				double r1 = (-HALF_WIDTH - x) / cos;
				double r2 = (+HALF_WIDTH - x) / cos;

				// Verify validity and add contributions
				if (r2 >= 0 && r2 <= MAX_RADIUS) {
					r.add(r2);
				}

				if (r1 >= 0 && r2 <= MAX_RADIUS) {
					r.add(r1);
				}
			}

			// Add center contribution
			if (Math.abs(x) < HALF_WIDTH) {
				// If the point is inside the slit
				totalPhasor.real -= 1;
				if (r.size() == 1) {
					totalPhasor = totalPhasor.plus(ComplexNumber.exp(r.get(0) * phasorMultiplier));
				}
			} else if (r.size() == 2) {
				// If the point is outside the slit
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
