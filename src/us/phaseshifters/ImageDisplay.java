package us.phaseshifters;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import us.phaseshifters.renderers.DiffractionParameters;
import us.phaseshifters.renderers.DiffractionRenderer;
import us.phaseshifters.renderers.PolygonRenderer;

/**
 *
 * @author Totom3
 */
public class ImageDisplay extends Application {

	private final DiffractionRenderer renderer;

	public ImageDisplay(DiffractionRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public void start(Stage stage) {
		DiffractionScreenPane pane = new DiffractionScreenPane();

		int size = (renderer instanceof PolygonRenderer) ? 400 : 800;
		
		Scene scene = new Scene(pane, size, size);
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.Q) {
				stage.close();
			}
		});
		stage.setTitle("Diffraction Renderer");
		stage.setScene(scene);
		stage.show();

		System.out.println("Rendering...");
		pane.drawCanvas(Main.params, renderer);
		System.out.println("Finished rendering!");
	}

}
