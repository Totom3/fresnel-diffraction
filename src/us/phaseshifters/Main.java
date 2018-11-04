package us.phaseshifters;

import java.util.List;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import us.phaseshifters.renderers.DiffractionParameters;
import us.phaseshifters.renderers.DiffractionRenderer;
import us.phaseshifters.renderers.DoubleSlitRenderer;
import us.phaseshifters.renderers.PoissonSpotRenderer;
import us.phaseshifters.renderers.PolygonRenderer;
import us.phaseshifters.renderers.SingleSlitRenderer;
import us.phaseshifters.renderers.SquareRenderer;

/**
 *
 * @author Totom3
 */
public class Main extends Application {

	public static final DoubleProperty DISTANCE_AW_PROP = new SimpleDoubleProperty(750);
	public static DiffractionParameters params;

	int height;
	int width;

	public Label wavelengthValueLabel;
	public Label distanceASValueLabel;
	public Label sourceIntensityValueLabel;

	public Slider wavelengthSlider;
	public Slider distanceASSlider;
	public Slider sourceIntensity;
	public CheckBox reversedCheckBox;
	public AperturePane aperture;

	public boolean displaySetup = false;
	public static boolean doneDrawing;

	@Override
	public void start(Stage primaryStage) {

		//Getting the screens size
		Rectangle2D screenSize = Screen.getPrimary().getBounds();
		height = (int) screenSize.getHeight();
		width = (int) screenSize.getWidth();

		//Setting up the VBox on the left side of the screen
		GridPane leftBox = new GridPane();

		int leftWidth = width / 4;
		leftBox.setAlignment(Pos.CENTER_LEFT);
		leftBox.setPrefSize(leftWidth, height);
		leftBox.setHgap(0);
		leftBox.setVgap((height / 2) / 8);

		//Creating all the sliders
		wavelengthSlider = new Slider();
		wavelengthSlider.setMin(380);
		wavelengthSlider.setMax(780);
		distanceASSlider = new Slider();
		distanceASSlider.setMin(200);
		distanceASSlider.setMax(700);
		sourceIntensity = new Slider();
		sourceIntensity.setMin(0.5);
		sourceIntensity.setMax(1.5);
		sourceIntensity.setValue(1);

		//Creating a Stack Pane and a Grid Pane for real-time slider value update
		StackPane stackpane = new StackPane();
		GridPane labelGrid = new GridPane();
		labelGrid.setAlignment(Pos.TOP_LEFT);
		labelGrid.setPrefSize(leftWidth, height);
		labelGrid.setHgap(0);
		labelGrid.setVgap((height / 2) / 8);

		// Create check boxes, combo box and button
		CheckBox setupCheckBox = new CheckBox("Display Setup");
		setupCheckBox.fire();
		reversedCheckBox = new CheckBox("Reversed");

		params = new DiffractionParameters(wavelengthSlider.valueProperty(), distanceASSlider.valueProperty(), DISTANCE_AW_PROP, sourceIntensity.valueProperty(), reversedCheckBox.selectedProperty());

		wavelengthValueLabel = new Label(String.format("%.2f", params.getWavelength()) + " nm");
		distanceASValueLabel = new Label(String.format("%.2f", params.getDistanceSA()) + " units");
		sourceIntensityValueLabel = new Label(String.format("%.2f", params.getIntensity()) + " units");

		GridPane.setConstraints(wavelengthValueLabel, 1, 1);
		GridPane.setConstraints(distanceASValueLabel, 1, 2);
		GridPane.setConstraints(sourceIntensityValueLabel, 1, 3);

		GridPane.setMargin(wavelengthValueLabel, new Insets(height / 40 + height / 35, 0, 0, leftWidth / 40 + leftWidth / 1.5));
		GridPane.setMargin(distanceASValueLabel, new Insets(height / 40, 0, 0, leftWidth / 40 + leftWidth / 1.5));
		GridPane.setMargin(sourceIntensityValueLabel, new Insets(height / 40, 0, 0, leftWidth / 40 + leftWidth / 1.5));

		labelGrid.getChildren().addAll(wavelengthValueLabel,
				distanceASValueLabel, sourceIntensityValueLabel);

		// Setting the size of the sliders'
		wavelengthSlider.setPrefSize(2 * leftWidth / 3, height / 2 / 4);
		distanceASSlider.setPrefSize(2 * leftWidth / 3, height / 2 / 4);
		sourceIntensity.setPrefSize(2 * leftWidth / 3, height / 2 / 4);

		//Creating all the labels
		Label wavelengthLabel = new Label("Wavelength");
		wavelengthLabel.setMinWidth(leftWidth / 2);
		Label distanceASLabel = new Label("Distance Aperture - Source");
		distanceASLabel.setMinWidth(leftWidth / 2);
		Label sourceIntensityLabel = new Label("Source Intensity");
		sourceIntensityLabel.setMinWidth(leftWidth / 2);

		String[] stringArray = {
			"Single-Slit", "Double-Slit", "Circular Aperture", "Square Aperture", "Custom Aperture"
		};

		ComboBox modeComboBox = new ComboBox();
		modeComboBox.getItems().addAll((Object[]) stringArray);
		modeComboBox.setValue(stringArray[0]);
		Button generateImageButton = new Button("Generate Image");

		modeComboBox.setPlaceholder(new Label("Aperture Type"));
		setupCheckBox.setPrefSize(leftWidth, height / 2 / 4);
		reversedCheckBox.setPrefSize(leftWidth, height / 2 / 4);
		modeComboBox.setPrefSize(leftWidth, height / 2 / 4);
		modeComboBox.setMinHeight(height / 16);
		generateImageButton.setPrefSize(leftWidth, height / 2 / 4);
		generateImageButton.setMinHeight(height / 8);

		//Setting the width of the first column to 1/3 of the VBox size and the second column to be 2/3
		GridPane.setConstraints(wavelengthSlider, 1, 1);
		GridPane.setConstraints(distanceASSlider, 1, 2);
		GridPane.setConstraints(sourceIntensity, 1, 3);

		GridPane.setConstraints(wavelengthLabel, 0, 1);
		GridPane.setConstraints(distanceASLabel, 0, 2);
		GridPane.setConstraints(sourceIntensityLabel, 0, 3);

		GridPane.setConstraints(setupCheckBox, 0, 4, 2, 1);
		GridPane.setConstraints(reversedCheckBox, 0, 5, 2, 1);
		GridPane.setConstraints(modeComboBox, 0, 6, 2, 1);
		GridPane.setConstraints(generateImageButton, 0, 7, 2, 1);

		GridPane.setMargin(distanceASLabel, new Insets(height / 40, 0, 0, leftWidth / 40));
		GridPane.setMargin(wavelengthLabel, new Insets(height / 40, 0, 0, leftWidth / 40));
		GridPane.setMargin(sourceIntensityLabel, new Insets(height / 40, 0, 0, leftWidth / 40));

		GridPane.setMargin(wavelengthSlider, new Insets(height / 40, leftWidth / 40, 0, 0));
		GridPane.setMargin(distanceASSlider, new Insets(height / 40, leftWidth / 40, 0, 0));
		GridPane.setMargin(sourceIntensity, new Insets(height / 40, leftWidth / 40, 0, 0));

		GridPane.setMargin(setupCheckBox, new Insets(0, leftWidth / 3 - leftWidth / 40, 0, leftWidth / 4 - leftWidth / 40));
		GridPane.setMargin(reversedCheckBox, new Insets(0, leftWidth / 3 - leftWidth / 40, 0, leftWidth / 4 - leftWidth / 40));
		GridPane.setMargin(modeComboBox, new Insets(0, leftWidth / 40, height / 16, leftWidth / 40));
		GridPane.setMargin(generateImageButton, new Insets(0, leftWidth / 40, height / 16, leftWidth / 40));

		//Adding all the elements to the silder grid
		leftBox.getChildren().addAll(wavelengthSlider, distanceASSlider,
				sourceIntensity, wavelengthLabel,
				distanceASLabel, sourceIntensityLabel, setupCheckBox,
				reversedCheckBox, modeComboBox, generateImageButton);

		stackpane.getChildren().addAll(labelGrid, leftBox);
		//Setting up the VBox on the right side of the screen
		int rightWidth = 3 * width / 4;
		BorderPane mainPane = new BorderPane();

		VBox centerBox = new VBox();
		centerBox.setPrefSize(rightWidth, height);
		centerBox.setAlignment(Pos.TOP_CENTER);
		DiagramPane diaPane = new DiagramPane();
		aperture = new AperturePane(rightWidth, height / 2);
		centerBox.getChildren().add(0, diaPane);
		centerBox.getChildren().add(1, aperture);

		//Setting up CheckBox listeners
		setupCheckBox.selectedProperty().addListener((arg0, arg1, arg2) -> {
			displaySetup = setupCheckBox.isSelected();
			System.out.println("changing to " + displaySetup);
			if (!displaySetup) {
				centerBox.getChildren().remove(diaPane);
			} else {
				centerBox.getChildren().add(0, diaPane);
				diaPane.drawCanvas(params.getWavelength(), params.isReversed(),
						params.getDistanceSA(), ExperimentType.DOUBLE_SLIT, 0,
						(int) centerBox.getHeight() / 2, (int) centerBox.getWidth());
			}
		});

		reversedCheckBox.selectedProperty().addListener((arg0, arg1, arg2) -> {
			aperture.pointList.clear();
			aperture.drawCanvas();
			updateDiaPane(setupCheckBox, centerBox, diaPane, getExperimentType(modeComboBox));
		});

		// Top box
		VBox topBox = new VBox();
		topBox.setMinHeight(height / 15);
		Font subtitleFont = new Font("Impact", 40);
		Text subtitle = new Text("Fresnel Diffraction Simulator");
		topBox.setAlignment(Pos.CENTER);
		subtitle.setFont(subtitleFont);
		subtitle.setTextAlignment(TextAlignment.CENTER);
		topBox.getChildren().add(subtitle);

		//Adding all the layotus to the VBox on the right
		mainPane.setTop(topBox);
		mainPane.setLeft(stackpane);
		mainPane.setCenter(centerBox);

		Scene scene = new Scene(mainPane, width, height);

		scene.setOnKeyReleased(e -> {
			List<Vec2D> points = aperture.getPoints();
			GraphicsContext gc = aperture.getCanvas().getGraphicsContext2D();
			if ((e.getCode() == KeyCode.ENTER) && (points.size() > 2)) {
				Vec2D p1 = points.get(0);
				Vec2D p2 = aperture.getPoints().get(aperture.getPoints().size() - 1);
				gc.setLineWidth(5);

			}
			if (e.getCode() == KeyCode.ESCAPE) {
				System.exit(0);
			}
		});

		scene.setOnKeyPressed((KeyEvent event) -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				System.exit(0);
			}
			if (event.getCode() == KeyCode.ENTER) {
				doneDrawing = true;
			}
			if (event.getCode() == KeyCode.Z) {
				doneDrawing = false;
				aperture.pointList.clear();
				aperture.drawCanvas();
			}
		});

		//Adding listeners on change to the sliders
		wavelengthSlider.valueProperty().addListener((arg0, arg1, arg2) -> {
			wavelengthValueLabel.setText(String.format("%.2f", params.getWavelength()) + " nm");
			updateDiaPane(setupCheckBox, centerBox, diaPane, getExperimentType(modeComboBox));
		});

		//Adding listener to ComboBox
		modeComboBox.valueProperty().addListener((arg0, arg1, arg2) -> {
			updateDiaPane(setupCheckBox, centerBox, diaPane, getExperimentType(modeComboBox));
		});

		distanceASSlider.valueProperty().addListener((arg0, arg1, arg2) -> {
			distanceASValueLabel.setText(String.format("%.2f", params.getDistanceSA()) + " units");
			updateDiaPane(setupCheckBox, centerBox, diaPane, getExperimentType(modeComboBox));
		});

		sourceIntensity.valueProperty().addListener((arg0, arg1, arg2) -> {
			sourceIntensityValueLabel.setText(String.format("%.2f", params.getIntensity()) + " units");
			updateDiaPane(setupCheckBox, centerBox, diaPane, getExperimentType(modeComboBox));
		});

		//Adding action to button
		generateImageButton.setOnAction(e -> {
			final DiffractionRenderer renderer = getRenderer(modeComboBox);
			if (renderer instanceof PolygonRenderer) {
				System.out.println("Points: "+aperture.pointList);
			}
			
			ImageDisplay image = new ImageDisplay(renderer);
			Stage secondaryStage = new Stage();
			image.start(secondaryStage);

		});

		primaryStage.setTitle("Hackathon 2018");
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
		primaryStage.show();

		updateDiaPane(setupCheckBox, centerBox, diaPane, getExperimentType(modeComboBox));
	}

	//temporary method to start AperturePane
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	public void updateDiaPane(CheckBox setupCheckBox, VBox centerBox, DiagramPane diaPane, ExperimentType expr) {
		if (setupCheckBox.isSelected()) {
			centerBox.getChildren().add(0, new DiagramPane());
			diaPane.drawCanvas(params.getWavelength(), params.isReversed(),
					params.getDistanceSA(), expr, 0,
					(int) centerBox.getHeight() / 2, (int) centerBox.getWidth());
		}
	}

	public ExperimentType getExperimentType(ComboBox<String> modeComboBox) {
		switch (modeComboBox.getValue()) {
			case "Single-Slit":
				return ExperimentType.SINGLE_SLIT;
			case "Double-Slit":
				return ExperimentType.DOUBLE_SLIT;
			case "Circular Aperture":
				return ExperimentType.CIRCULAR_APERTURE;
			case "Square Aperture":
				return ExperimentType.OBJECT;
			case "Custom Aperture":
				return ExperimentType.OBJECT;
			default:
				return ExperimentType.OBJECT;
		}
	}

	public DiffractionRenderer getRenderer(ComboBox<String> modeComboBox) {
		switch (modeComboBox.getValue()) {
			case "Single-Slit":
				return new SingleSlitRenderer();
			case "Double-Slit":
				return new DoubleSlitRenderer();
			case "Circular Aperture":
				return new PoissonSpotRenderer();
			case "Square Aperture":
				return new SquareRenderer();
			case "Custom Aperture":
				return new PolygonRenderer(aperture.pointList);
			default:
				return new PoissonSpotRenderer();
		}
	}
}
