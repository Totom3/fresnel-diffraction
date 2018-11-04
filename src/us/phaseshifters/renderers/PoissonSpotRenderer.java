package us.phaseshifters.renderers;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import us.phaseshifters.ComplexNumber;

/**
 *
 * @author Totom3
 */
public class PoissonSpotRenderer implements DiffractionRenderer {

	private static final double RADIUS = 50;
	private static final double RADIUS_SQUARED = RADIUS * RADIUS;

	@Override
	public void render(Canvas canvas, DiffractionParameters params, int size, int resolution) {
		final int thetaStepCount = 200;
		final double deltaTheta = 2 * Math.PI / thetaStepCount;
		final double phasorMultiplier = 1_000_000_000 * (1 / params.getDistanceSA() + 1 / params.getDistanceAW()) * Math.PI / params.getWavelength();

		final int bounds = size / 2;
		final int effectiveSize = size / resolution;
		final double[][] probabilities = new double[effectiveSize][effectiveSize];

		// Render each pixel on the screen
		//double sum = 0;
		double max = 0;
		for (int i = 0; i < effectiveSize; ++i) {
			for (int j = 0; j < effectiveSize; ++j) {

				int x = (i * resolution) - bounds;
				int y = (j * resolution) - bounds;

				// Outer integral
				ComplexNumber totalPhasor = outerIntegral(x, y, thetaStepCount, deltaTheta, phasorMultiplier, params);

				// Store result
				double prob = totalPhasor.normSquared();
				probabilities[i][j] = prob;
				//sum += prob;
				if (prob > max) {
					max = prob;
				}
			}
		}

		// TODO: rescale values appropriately
		// Paint values
		GraphicsContext graphics = canvas.getGraphicsContext2D();
		for (int i = 0; i < effectiveSize; ++i) {
			for (int j = 0; j < effectiveSize; ++j) {
				int x = (i * resolution);
				int y = (j * resolution);

				graphics.setFill(Color.gray(probabilities[i][j] / max));
				graphics.fillRect(x, y, resolution, resolution);
			}
		}
	}

	private ComplexNumber outerIntegral(int x, int y, int thetaStepCount, double deltaTheta, double phasorMultiplier, DiffractionParameters params) {
		ComplexNumber totalPhasor = new ComplexNumber();

		for (int t = 0; t < thetaStepCount; ++t) {
			// Compute theta & friends
			double theta = t * deltaTheta;
			double cos = Math.cos(theta);
			double sin = Math.sin(theta);

			// Intermediary constants
			double const1 = (x * cos) + (y * sin);
			double const2 = (x * x) + (y * y) - (RADIUS_SQUARED);
			double discriminant = (const1 * const1) - const2;

			// Check discriminant sign
			if (discriminant <= 0) {
				// If the ray never crosses the circle,
				// we only add the contribution from the origin.
				totalPhasor.real -= 1;
				continue;
			}

			// Compute intersection radii
			double const3 = Math.sqrt(discriminant);
			double r1 = const1 - const3;
			double r2 = const1 + const3;

			// Add contribution from the obstacle
			if (r2 > 0) {
				totalPhasor = totalPhasor.minus(ComplexNumber.exp(r2 * phasorMultiplier));
			}

			if (r1 >= 0) {
				totalPhasor = totalPhasor.plus(ComplexNumber.exp(r1 * phasorMultiplier));
				totalPhasor.real -= 1;
			}
		}

		return totalPhasor;
	}

}
