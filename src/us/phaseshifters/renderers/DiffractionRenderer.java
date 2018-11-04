package us.phaseshifters.renderers;

import javafx.scene.canvas.Canvas;

/**
 *
 * @author Totom3
 */
public interface DiffractionRenderer {
	
	void render(Canvas canvas, DiffractionParameters params, int size, int resolution);

}
