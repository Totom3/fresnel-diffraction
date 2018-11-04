package us.phaseshifters.renderers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private final BooleanProperty reversed;

    public DiffractionParameters(double wavelength, double distanceSA, double distanceAW, double intensity, boolean reversed) {
        this(new SimpleDoubleProperty(wavelength),
                new SimpleDoubleProperty(distanceSA),
                new SimpleDoubleProperty(distanceAW),
                new SimpleDoubleProperty(intensity),
                new SimpleBooleanProperty(reversed));
    }

    public boolean isReversed() {
        return reversed.get();
    }

    public DiffractionParameters(DoubleProperty wavelength, DoubleProperty distanceSA, DoubleProperty distanceAW, DoubleProperty intensity, BooleanProperty reversed) {
        this.wavelength = wavelength;
        this.distanceSA = distanceSA;
        this.distanceAW = distanceAW;
        this.intensity = intensity;
        this.reversed = reversed;
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
