package us.phaseshifters;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import us.phaseshifters.renderers.DiffractionParameters;
import us.phaseshifters.renderers.PoissonSpotRenderer;
import us.phaseshifters.renderers.SingleSlitRenderer;

/**
 *
 * @author Totom3
 */
public class RendererMain extends Application {

	@Override
	public void start(Stage stage) {
		DiffractionScreenPane pane = new DiffractionScreenPane();
		DiffractionParameters params = new DiffractionParameters(0.5, 1, 1, 1);

		Scene scene = new Scene(pane, 600, 600);
		stage.setTitle("Diffraction Renderer");
		stage.setScene(scene);
		stage.show();

		System.out.println("Rendering...");
		pane.drawCanvas(params, new SingleSlitRenderer());
		System.out.println("Finished rendering!");
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
