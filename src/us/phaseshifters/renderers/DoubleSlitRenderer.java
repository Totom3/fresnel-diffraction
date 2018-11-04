package us.phaseshifters.renderers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author Administrator
 */
public class DoubleSlitRenderer implements DiffractionRenderer {

	private static final double WIDTH = 0.00001;
	private static final double DISTANCE = 0.0001;

	@Override
	public void render(Canvas canvas, DiffractionParameters params, int size, int resolution) {
		final int samplesCount = size / resolution / 2;
		double probabilities[] = new double[samplesCount];

		int shift = size / 2;
		double max = 0;
		for (int i = 0; i < samplesCount; ++i) {
			double x = ((i * resolution) - shift) / 300.0;
			double intensity = getIntensity(x, params);

			probabilities[i] = intensity;
			if (intensity > max) {
				max = intensity;
			}
		}

		double median = median(probabilities);

		GraphicsContext gc = canvas.getGraphicsContext2D();
		Color color = wavelengthToColor(params.getWavelength());

		double correctionFactor = (255.0 - 40.0) / (max - median);
		for (int i = 0; i < samplesCount; ++i) {
			double intensity = probabilities[i];

			intensity = correctionFactor * (intensity - median) + 40;
			intensity = Math.pow(intensity, params.getIntensity());

			intensity = Math.max(0, Math.min(255, intensity));

			gc.setFill(Color.grayRgb((int) intensity));
			gc.fillRect(i * resolution, 0, resolution, size);
			gc.fillRect(size - 1 - i * resolution - resolution, 0, resolution, size);

		}
	}

	private double median(double[] array) {
		List<Double> list = new ArrayList<>();
		for (double d : array) {
			list.add(d);
		}

		Collections.sort(list);
		return list.get(list.size() / 2);
	}

	public double getIntensity(double x, DiffractionParameters params) {

		double sinAngle = x / (Math.sqrt((x * x) + (params.getDistanceAW() * params.getDistanceAW())));
		double temp = (1_000_000_000 * (Math.PI * DISTANCE * sinAngle) / params.getWavelength());
		double denominator = (1_000_000_000 * (Math.PI * WIDTH * sinAngle) / params.getWavelength());
		double numerator = Math.sin(denominator);
		
		double cos = Math.cos(temp);

		return params.getIntensity() * (cos * cos) * (numerator * numerator) / (denominator * denominator);

	}

}
