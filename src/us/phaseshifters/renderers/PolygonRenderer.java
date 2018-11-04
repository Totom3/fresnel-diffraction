package us.phaseshifters.renderers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import us.phaseshifters.ComplexNumber;
import us.phaseshifters.Vec2D;

/**
 *
 * @author Totom3
 */
public class PolygonRenderer implements DiffractionRenderer {

	public static final List<Vec2D> SAMPLE_POINTS;
	
	static {
		SAMPLE_POINTS = new ArrayList<>();
		SAMPLE_POINTS.add(new Vec2D(25, 25));
		SAMPLE_POINTS.add(new Vec2D(25, -25));
		SAMPLE_POINTS.add(new Vec2D(-25, -25));
		SAMPLE_POINTS.add(new Vec2D(-25, 25));
	}
	
	private final List<Vec2D> points;

	public PolygonRenderer(List<Vec2D> points) {
		this.points = points;
	}

	@Override
	public void render(Canvas canvas, DiffractionParameters params, int size, int resolution) {
		final int thetaStepCount = 256;
		final double deltaTheta = 2 * Math.PI / thetaStepCount;
		final double phasorMultiplier = 1_000_000_000 * (1 / params.getDistanceSA() + 1 / params.getDistanceAW()) * Math.PI / params.getWavelength();

		double probabilities[][] = new double[size / resolution][size / resolution];

		for (int x = 0; x < size; x += resolution) {
			for (int y = 0; y < size; y += resolution) {

				ComplexNumber totalPhasor = outerIntegral(x, y, thetaStepCount, deltaTheta, phasorMultiplier);
				probabilities[x][y] = totalPhasor.normSquared();
			}
		}
		
		GraphicsContext graphics = canvas.getGraphicsContext2D();
		for (int x = 0; x < size; x += resolution) {
			for (int y = 0; y < size; y += resolution) {
				
				double intensity = probabilities[x][y];
				intensity = Math.max(0, Math.min(255, intensity));
				
				graphics.setFill(Color.grayRgb((int)intensity));
				graphics.fillRect(x, y, resolution, resolution);
			}
		}
	}

	private static final double EPSILON = 0.0001;

	private ComplexNumber outerIntegral(int x, int y, double thetaStepCount, double deltaTheta, double phasorMultiplier) {
		ComplexNumber totalPhasor = new ComplexNumber();

		boolean insidePolygon = false;

		for (int t = 0; t < thetaStepCount; ++t) {
			double theta = t * deltaTheta;
			double cos = Math.cos(theta);
			double sin = Math.sin(theta);
			Vec2D n = new Vec2D(cos, sin);

			List<Double> radii = new ArrayList<>();

			// Case #1: vertices
			for (int i = 0; i < points.size(); ++i) {
				Vec2D point = points.get(i);
				double arg = Math.atan2(point.y - y, point.x - x);
				if (Math.abs(arg - theta) >= EPSILON) {
					continue;
				}

				Vec2D diff1 = next(i).subtract(point);
				Vec2D diff2 = previous(i).subtract(point);
				if (!isInSector(diff1, diff2, n)) {
					continue;
				}

				// Add point
				if (cos != 0) {
					radii.add(point.x / cos);
				}
			}

			// Case #2: points
			for (int i = 0; i < points.size(); ++i) {
				Vec2D point = points.get(i);
				Vec2D next = next(i);
				if (!isInSector(point, next, n)) {
					continue;
				}

				Vec2D diff = point.subtract(next);
				double numerator = diff.cross(point);
				double denominator = diff.cross(n);
				if (denominator == 0) {
					continue;
				}

				radii.add(numerator / denominator);
			}

			Collections.sort(radii);

			if (t == 0) {
				insidePolygon = radii.size() % 2 == 1;
				totalPhasor.real -= 1;
			}

			double sgn = insidePolygon ? 1 : -1;
			for (double radius : radii) {
				totalPhasor = totalPhasor.plus(ComplexNumber.exp(radius * phasorMultiplier).scale(sgn));
			}
		}

		return totalPhasor;
	}

	private Vec2D next(int i) {
		return (i == points.size() - 1) ? points.get(0) : points.get(i + 1);
	}

	private Vec2D previous(int i) {
		return (i == 0) ? points.get(points.size() - 1) : points.get(i - 1);
	}

	private boolean isInSector(Vec2D lim1, Vec2D lim2, Vec2D vec) {
		final double c1 = lim1.cross(lim2);
		return (lim1.cross(vec) * c1) >= 0 && (lim2.cross(vec) * c1) <= 0;
	}
}
