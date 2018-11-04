package us.phaseshifters;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import us.phaseshifters.renderers.DiffractionParameters;
import us.phaseshifters.renderers.DoubleSlitRenderer;
import us.phaseshifters.renderers.PoissonSpotRenderer;
import us.phaseshifters.renderers.SingleSlitRenderer;


/**
 *
 * @author Totom3
 */
public class ImageDisplay extends Application {

	@Override
	public void start(Stage stage) {
                     
		DiffractionScreenPane pane = new DiffractionScreenPane();
		DiffractionParameters params = new DiffractionParameters(500.0, 1, 2, 1.2, true);

		Scene scene = new Scene(pane, 600, 600);
                scene.setOnKeyPressed(e -> {
                    if(e.getCode() == KeyCode.Q){
                        stage.close();
                    }
                });
		stage.setTitle("Diffraction Renderer");
		stage.setScene(scene);
		stage.show();

		System.out.println("Rendering...");
		pane.drawCanvas(params,new PoissonSpotRenderer());
		System.out.println("Finished rendering!");
	}

}
