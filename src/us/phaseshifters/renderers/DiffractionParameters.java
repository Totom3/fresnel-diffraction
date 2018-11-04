package us.phaseshifters.renderers;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author Totom3
 */
public class DiffractionParameters {

	private final DoubleProperty wavelength;
	private final DoubleProperty distanceSA;
	private final DoubleProperty distanceAW;
	private final DoubleProperty intensity;

	public DiffractionParameters(double wavelength, double distanceSA, double distanceAW, double intensity) {
		this(new SimpleDoubleProperty(wavelength),
				new SimpleDoubleProperty(distanceSA),
				new SimpleDoubleProperty(distanceAW),
				new SimpleDoubleProperty(intensity));
	}
	
	public DiffractionParameters(SimpleDoubleProperty wavelength, DoubleProperty distanceSA, DoubleProperty distanceAW, DoubleProperty intensity) {
		this.wavelength = wavelength;
		this.distanceSA = distanceSA;
		this.distanceAW = distanceAW;
		this.intensity = intensity;
	}

	public double getWavelength() {
		return wavelength.get();
	}

	public double getIntensity() {
		return intensity.get();
	}

	public double getDistanceSA() {
		return distanceSA.get();
	}

	public double getDistanceAW() {
		return distanceAW.get();
	}

}
