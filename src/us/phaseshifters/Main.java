package us.phaseshifters;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author Totom3
 */
public class Main extends Application {

    int height;
    int width;

    public static int wavelengthValue;
    public static double distanceASValue;
    public static double sourceIntensityValue;

    public Label wavelengthValueLabel;
    public Label distanceASValueLabel;
    public Label sourceIntensityValueLabel;

    public static boolean reversed;
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
        Slider wavelength = new Slider();
        wavelength.setMin(380);
        wavelength.setMax(780);
        Slider distanceAS = new Slider();
        distanceAS.setMin(200);
        distanceAS.setMax(700);
        Slider sourceIntensity = new Slider();

        //Creating a Stack Pane and a Grid Pane for real-time slider value update
        StackPane stackpane = new StackPane();
        GridPane labelGrid = new GridPane();
        labelGrid.setAlignment(Pos.TOP_LEFT);
        labelGrid.setPrefSize(leftWidth, height);
        labelGrid.setHgap(0);
        labelGrid.setVgap((height / 2) / 8);

        wavelengthValue = (int) wavelength.getMin();
        distanceASValue = distanceAS.getMin();
        sourceIntensityValue = sourceIntensity.getMin();

        wavelengthValueLabel = new Label(String.valueOf(wavelengthValue) + " nm");
        distanceASValueLabel = new Label(String.valueOf(distanceASValue) + " units");
        sourceIntensityValueLabel = new Label(String.valueOf(sourceIntensityValue) + " units");

        GridPane.setConstraints(wavelengthValueLabel, 1, 1);
        GridPane.setConstraints(distanceASValueLabel, 1, 2);
        GridPane.setConstraints(sourceIntensityValueLabel, 1, 3);

        GridPane.setMargin(wavelengthValueLabel, new Insets(height / 40 + height / 35, 0, 0, leftWidth / 40 + leftWidth / 1.5));
        GridPane.setMargin(distanceASValueLabel, new Insets(height / 40, 0, 0, leftWidth / 40 + leftWidth / 1.5));
        GridPane.setMargin(sourceIntensityValueLabel, new Insets(height / 40, 0, 0, leftWidth / 40 + leftWidth / 1.5));

        labelGrid.getChildren().addAll(wavelengthValueLabel,
                distanceASValueLabel, sourceIntensityValueLabel);

        // Setting the size of the sliders'
        wavelength.setPrefSize(2 * leftWidth / 3, height / 2 / 4);
        distanceAS.setPrefSize(2 * leftWidth / 3, height / 2 / 4);
        sourceIntensity.setPrefSize(2 * leftWidth / 3, height / 2 / 4);

        //Creating all the labels
        Label wavelengthLabel = new Label("Wavelength");
        wavelengthLabel.setMinWidth(leftWidth / 2);
        Label distanceASLabel = new Label("Distance Aperture - Source");
        distanceASLabel.setMinWidth(leftWidth / 2);
        Label sourceIntensityLabel = new Label("Source Intensity");
        sourceIntensityLabel.setMinWidth(leftWidth / 2);

        // Create check boxes, combo box and button
        CheckBox setupCheckBox = new CheckBox("Display Setup");
        setupCheckBox.fire();
        CheckBox reversedCheckBox = new CheckBox("Reversed");
        String[] stringArray = {
            "Custom Aperture", "Single-Slit", "Double-Slit", "Circular Aperture"
        };

        ComboBox modeComboBox = new ComboBox();
        modeComboBox.getItems().addAll(stringArray);
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
        GridPane.setConstraints(wavelength, 1, 1);
        GridPane.setConstraints(distanceAS, 1, 2);
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

        GridPane.setMargin(wavelength, new Insets(height / 40, leftWidth / 40, 0, 0));
        GridPane.setMargin(distanceAS, new Insets(height / 40, leftWidth / 40, 0, 0));
        GridPane.setMargin(sourceIntensity, new Insets(height / 40, leftWidth / 40, 0, 0));

        GridPane.setMargin(setupCheckBox, new Insets(0, leftWidth / 3 - leftWidth / 40, 0, leftWidth / 4 - leftWidth / 40));
        GridPane.setMargin(reversedCheckBox, new Insets(0, leftWidth / 3 - leftWidth / 40, 0, leftWidth / 4 - leftWidth / 40));
        GridPane.setMargin(modeComboBox, new Insets(0, leftWidth / 40, height / 16, leftWidth / 40));
        GridPane.setMargin(generateImageButton, new Insets(0, leftWidth / 40, height / 16, leftWidth / 40));

        //Adding all the elements to the silder grid
        leftBox.getChildren().addAll(wavelength, distanceAS,
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
        AperturePane aperture = new AperturePane(rightWidth, height / 2);
        centerBox.getChildren().add(0, diaPane);
        centerBox.getChildren().add(1, aperture);

        //Setting up CheckBox listeners
        setupCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue ov, Boolean old_val, Boolean new_val) {

                displaySetup = setupCheckBox.isSelected();
                System.out.println("changing to " + displaySetup);
                if (!displaySetup) {
                    centerBox.getChildren().remove(diaPane);
                } else {
                    centerBox.getChildren().add(0, diaPane);
                    diaPane.drawCanvas(wavelengthValue, reversed,
                            distanceASValue, ExperimentType.DOUBLE_SLIT, 0,
                            (int) centerBox.getHeight() / 2, (int) centerBox.getWidth());
                }
            }
        });

        reversedCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue ov, Boolean old_val, Boolean new_val) {               
                reversed = reversedCheckBox.isSelected();
                aperture.pointList.clear();
                aperture.drawCanvas();
                updateDiaPane(setupCheckBox, centerBox, diaPane, getComboBoxExperiment(modeComboBox));
            }
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

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    System.exit(0);
                }
                if (event.getCode() == KeyCode.ENTER) {
                    doneDrawing = true;
                }
                if (event.getCode() == KeyCode.Z) {
                    doneDrawing = false;
                    System.out.println("aaa");
                    aperture.pointList.clear();
                    aperture.drawCanvas();
                }
            }
        });

        //Adding listeners on change to the sliders
        wavelength.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                wavelengthValue = (int) wavelength.getValue();
                wavelengthValueLabel.setText(String.valueOf(wavelengthValue) + " nm");
                updateDiaPane(setupCheckBox, centerBox, diaPane, getComboBoxExperiment(modeComboBox));

            }
        });

        //Adding listener to ComboBox
        modeComboBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                updateDiaPane(setupCheckBox, centerBox, diaPane, getComboBoxExperiment(modeComboBox));
            }
        });

        distanceAS.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {

                distanceASValue = distanceAS.getValue();
                distanceASValueLabel.setText(String.format("%.2f", distanceASValue) + " units");
                updateDiaPane(setupCheckBox, centerBox, diaPane, getComboBoxExperiment(modeComboBox));

            }
        });

        sourceIntensity.valueProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {

                sourceIntensityValue = sourceIntensity.getValue();
                sourceIntensityValueLabel.setText(String.format("%.2f", sourceIntensityValue) + " units");
                updateDiaPane(setupCheckBox, centerBox, diaPane, getComboBoxExperiment(modeComboBox));

            }
        });
        
        //Adding action to button
        generateImageButton.setOnAction(e ->{
            ImageDisplay image = new ImageDisplay();
            Stage secondaryStage = new Stage();
            image.start(secondaryStage);
            
        });

        primaryStage.setTitle("Hackathon 2018");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        updateDiaPane(setupCheckBox, centerBox, diaPane, getComboBoxExperiment(modeComboBox));

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
            System.out.println("nyeee");
            diaPane.drawCanvas(wavelengthValue, reversed,
                    distanceASValue, expr, 0,
                    (int) centerBox.getHeight() / 2, (int) centerBox.getWidth());
        }
    }

    public ExperimentType getComboBoxExperiment(ComboBox modeComboBox) {
        
        ExperimentType experiment = ExperimentType.DOUBLE_SLIT;

        if (modeComboBox.getValue().equals("Double-Slit")) {
            experiment = ExperimentType.DOUBLE_SLIT;
        } else if (modeComboBox.getValue().equals("Single-Slit")) {
            experiment = ExperimentType.SINGLE_SLIT;
        } else if (modeComboBox.getValue().equals("Circular Aperture")) {
            experiment = ExperimentType.CIRCULAR_APERTURE;
        } else if (modeComboBox.getValue().equals("Custom Aperture")) {
            experiment = ExperimentType.OBJECT;
        }
        
        return experiment;
    }

}
