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

    private static final double RADIUS = 200;
    private static final double RADIUS_SQUARED = RADIUS * RADIUS;

    @Override
    public void render(Canvas canvas, DiffractionParameters params, int size, int resolution) {
        final int thetaStepCount = 200;
        final double deltaTheta = 2 * Math.PI / thetaStepCount;
        final double phasorMultiplier = 1_000_000_000 * (1 / params.getDistanceSA() + 1 / params.getDistanceAW()) * Math.PI / params.getWavelength();

        final int shift = size / 2;
        final double radiiStep = 1.0;
        final int maxRadius = (int) (Math.sqrt(2) * size / resolution);
        final int radiiCount = (int) (maxRadius / radiiStep);

        final double[] probabilities = new double[radiiCount];

        // Render each pixel on the screen
        double max = 0;
        for (int i = 0; i < radiiCount; ++i) {
            int x = (int) (i * radiiStep);

            // Outer integral
            ComplexNumber totalPhasor = outerIntegral(x, thetaStepCount, deltaTheta, phasorMultiplier, params);

            // Store result
            double prob = totalPhasor.normSquared();
            probabilities[i] = prob;
            if (prob > max) {
                max = prob;
            }
        }

        // Render each pixel
        // TODO: rescale values appropriately
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        for (int x = 0; x < size; x += resolution) {
            for (int y = 0; y < size; y += resolution) {
                int x1 = x - shift;
                int y1 = y - shift;
                int bin = (int) Math.round(Math.sqrt((double) ((x1 * x1) + (y1 * y1))) / radiiStep);

                graphics.setFill(Color.gray(probabilities[bin] / max));
                graphics.fillRect(x, y, resolution, resolution);
            }
        }
    }

    private ComplexNumber outerIntegral(int r, int thetaStepCount, double deltaTheta, double phasorMultiplier, DiffractionParameters params) {
        ComplexNumber totalPhasor = new ComplexNumber();

        for (int t = 0; t < thetaStepCount; ++t) {
            // Compute theta & friends
            double theta = t * deltaTheta;
            double cos = Math.cos(theta);

            // Intermediary constants
            double const1 = (r * cos);
            double const2 = (r * r) - (RADIUS_SQUARED);
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
            if (!params.isReversed()) {
                if (r2 > 0) {
                    totalPhasor = totalPhasor.minus(ComplexNumber.exp(r2 * phasorMultiplier));
                }

                if (r1 >= 0) {
                    totalPhasor = totalPhasor.plus(ComplexNumber.exp(r1 * phasorMultiplier));
                    totalPhasor.real -= 1;
                }
            } else {
                if (r2 >= 0) {
                    totalPhasor = totalPhasor.plus(ComplexNumber.exp(r2 * phasorMultiplier));
                    totalPhasor.real += 1;
                }

                if (r1 > 0) {
                    totalPhasor = totalPhasor.minus(ComplexNumber.exp(r1 * phasorMultiplier));
                    totalPhasor.real += 1;
                }
            }
        }

        return totalPhasor;
    }

}
