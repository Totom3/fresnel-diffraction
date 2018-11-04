package us.phaseshifters.renderers;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

/**
 *
 * @author Totom3
 */
public interface DiffractionRenderer {
	
	void render(Canvas canvas, DiffractionParameters params, int size, int resolution);

	default Color getColor(double wavelength, double brightness) {

        double intensityMax = 255;
        double gamma = 0.8;
        double red, green, blue;
        double factor;

        if ((wavelength >= 380) && (wavelength < 440)) {
            red = -(wavelength - 440) / (440 - 380);
            green = 0.0;
            blue = 1.0;
        } else if ((wavelength >= 440) && (wavelength < 490)) {
            red = 0.0;
            green = (wavelength - 440) / (490 - 440);
            blue = 1.0;
        } else if ((wavelength >= 490) && (wavelength < 510)) {
            red = 0.0;
            green = 1.0;
            blue = -(wavelength - 510) / (510 - 490);
        } else if ((wavelength >= 510) && (wavelength < 580)) {
            red = (wavelength - 510) / (580 - 510);
            green = 1.0;
            blue = 0.0;
        } else if ((wavelength >= 580) && (wavelength < 645)) {
            red = 1.0;
            green = -(wavelength - 645) / (645 - 580);
            blue = 0.0;
        } else if ((wavelength >= 645) && (wavelength < 781)) {
            red = 1.0;
            green = 0.0;
            blue = 0.0;
        } else {
            red = 0.0;
            green = 0.0;
            blue = 0.0;
        }

        // Let the intensity fall off near the vision limits
        if ((wavelength >= 380) && (wavelength < 420)) {
            factor = 0.3 + (0.7 * (wavelength - 380) / (420 - 380));
        } else if ((wavelength >= 420) && (wavelength < 701)) {
            factor = 1.0;
        } else if ((wavelength >= 701) && (wavelength < 781)) {
            factor = 0.3 + 0.7 * (780 - wavelength) / (780 - 700);
        } else {
            factor = 0.0;
        }

        int[] rgb = new int[3];

        // Don't want 0^x = 1 for x <> 0
        rgb[0] = red == 0.0 ? 0 : (int) Math.round(intensityMax * Math.pow(red * factor, gamma));
        rgb[1] = green == 0.0 ? 0 : (int) Math.round(intensityMax * Math.pow(green * factor, gamma));
        rgb[2] = blue == 0.0 ? 0 : (int) Math.round(intensityMax * Math.pow(blue * factor, gamma));

        return Color.rgb(rbgRange(brightness * rgb[0]), rbgRange(brightness * rgb[1]), rbgRange(brightness * rgb[2]));
    }
	
	default int rbgRange(double x) {
		return (int) Math.max(0, Math.min(255, x));
	}
}
