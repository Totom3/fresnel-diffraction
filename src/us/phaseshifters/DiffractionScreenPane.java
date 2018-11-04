package us.phaseshifters;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import us.phaseshifters.renderers.DiffractionParameters;
import us.phaseshifters.renderers.DiffractionRenderer;
import us.phaseshifters.renderers.PolygonRenderer;

public class DiffractionScreenPane extends Pane {

	private final Canvas canvas;

	public DiffractionScreenPane() {
		this.canvas = new Canvas();
		getChildren().add(canvas);
	}

	public Canvas getCanvas() {
		return canvas;
	}
	
	public void drawCanvas(DiffractionParameters params, DiffractionRenderer renderer) {
		final int resolution = (renderer instanceof PolygonRenderer) ? 3 : 1;

		int size = (int) Math.min(getWidth(), getHeight());
		canvas.setWidth(size);
		canvas.setHeight(size);
		
		renderer.render(canvas, params, size, resolution);
	}
}
