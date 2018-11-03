package us.phaseshifters;

import java.awt.Dimension;
import java.awt.Toolkit;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
        Slider distanceAW = new Slider();
        Slider distanceAS = new Slider();
        Slider sourceIntensity = new Slider();
        
        //Creating a Stack Pane and a Grid Pane for real-time slider value update
        
        StackPane stackpane = new StackPane();
        GridPane labelGrid = new GridPane();
        labelGrid.setVgap((height/2)/4);
        
        Label wavelengthValueLabel = new Label();
        wavelengthValueLabel.setText(String.valueOf(wavelength.getValue()));
        Label distanceAWValueLabel = new Label(String.valueOf(distanceAW.getValue()));
        Label distanceASValueLabel = new Label(String.valueOf(distanceAS.getValue()));
        Label sourceIntensityValueLabel = new Label(String.valueOf(sourceIntensity.getValue()));
        
        GridPane.setConstraints(wavelengthValueLabel, 1, 0);
        GridPane.setConstraints(distanceAWValueLabel, 1, 1);
        GridPane.setConstraints(distanceASValueLabel, 1, 2);
        GridPane.setConstraints(sourceIntensityValueLabel, 1, 3);
        
        GridPane.setMargin(wavelengthValueLabel, new Insets(height/40, 0, 0, leftWidth/40));
        GridPane.setMargin(distanceAWValueLabel, new Insets(height/40, 0, 0, leftWidth/40));
        GridPane.setMargin(distanceASValueLabel, new Insets(height/40, 0, 0, leftWidth/40));
        GridPane.setMargin(sourceIntensityValueLabel, new Insets(height/40, 0, 0, leftWidth/40));
        
        labelGrid.getChildren().addAll(wavelengthValueLabel, distanceAWValueLabel,
                distanceASValueLabel, sourceIntensityValueLabel);
        
        
        
        

        // Setting the size of the sliders'
        wavelength.setPrefSize(2 * leftWidth / 3, height / 2 / 4);
        distanceAW.setPrefSize(2 * leftWidth / 3, height / 2 / 4);
        distanceAS.setPrefSize(2 * leftWidth / 3, height / 2 / 4);
        sourceIntensity.setPrefSize(2 * leftWidth / 3, height / 2 / 4);

        //Creating all the labels
        Label wavelengthLabel = new Label("Wavelength");
        wavelengthLabel.setMinWidth(leftWidth / 2);
        Label distanceAWLabel = new Label("Distance Aperture - Wall");
        distanceAWLabel.setMinWidth(leftWidth / 2);
        Label distanceASLabel = new Label("Distance Aperture - Source");
        distanceASLabel.setMinWidth(leftWidth / 2);
        Label sourceIntensityLabel = new Label("Source Intensity");
        sourceIntensityLabel.setMinWidth(leftWidth / 2);

        // Create check boxes, combo box and button
        CheckBox setupCheckBox = new CheckBox("Display Setup");
        CheckBox reversedCheckBox = new CheckBox("Reversed");
        ComboBox modeComboBox = new ComboBox(); 
        Button generateImageButton = new Button("Generate Image");

        modeComboBox.setPlaceholder(new Label("Aperture Type"));
        setupCheckBox.setPrefSize(leftWidth, height / 2 / 4);
        reversedCheckBox.setPrefSize(leftWidth, height / 2 / 4);
        modeComboBox.setPrefSize(leftWidth, height / 2 / 4);
        modeComboBox.setMinHeight(height/16);
        generateImageButton.setPrefSize(leftWidth, height / 2 / 4);
        generateImageButton.setMinHeight(height/8);

        //Setting the width of the first column to 1/3 of the VBox size and the second column to be 2/3

        GridPane.setConstraints(wavelength, 1, 0);
        GridPane.setConstraints(distanceAW, 1, 1);
        GridPane.setConstraints(distanceAS, 1, 2);
        GridPane.setConstraints(sourceIntensity, 1, 3);
        
        GridPane.setConstraints(distanceAWLabel, 0, 0);
        GridPane.setConstraints(distanceASLabel, 0, 1);
        GridPane.setConstraints(wavelengthLabel, 0, 2);
        GridPane.setConstraints(sourceIntensityLabel, 0, 3);
        
        GridPane.setConstraints(setupCheckBox, 0, 4, 2, 1);
        GridPane.setConstraints(reversedCheckBox, 0, 5, 2, 1);
        GridPane.setConstraints(modeComboBox, 0, 6, 2, 1);
        GridPane.setConstraints(generateImageButton, 0, 7, 2, 1);
        
        GridPane.setMargin(wavelengthLabel, new Insets(height/40, 0, 0, leftWidth/40));
        GridPane.setMargin(distanceAWLabel, new Insets(height/40, 0, 0, leftWidth/40));
        GridPane.setMargin(distanceASLabel, new Insets(height/40, 0, 0, leftWidth/40));
        GridPane.setMargin(sourceIntensityLabel, new Insets(height/40, 0, 0, leftWidth/40));
        
        GridPane.setMargin(wavelength, new Insets(height/40, leftWidth/40, 0, 0));
        GridPane.setMargin(distanceAW, new Insets(height/40, leftWidth/40, 0, 0));
        GridPane.setMargin(distanceAS, new Insets(height/40, leftWidth/40, 0, 0));
        GridPane.setMargin(sourceIntensity, new Insets(height/40, leftWidth/40, 0,0));
        
        GridPane.setMargin(setupCheckBox, new Insets(0, leftWidth/3 - leftWidth/40, 0, leftWidth/4 - leftWidth/40));
        GridPane.setMargin(reversedCheckBox, new Insets(0, leftWidth/3 - leftWidth/40, 0, leftWidth/4 - leftWidth/40));
        GridPane.setMargin(modeComboBox, new Insets(0, leftWidth/40, height/16, leftWidth/40));
        GridPane.setMargin(generateImageButton, new Insets(0, leftWidth/40, height/16, leftWidth/40));
        
        
        

        //Adding all the elements to the silder grid
        leftBox.getChildren().addAll(wavelength, distanceAW, distanceAS,
                sourceIntensity, wavelengthLabel, distanceAWLabel,
                distanceASLabel, sourceIntensityLabel, setupCheckBox, 
                reversedCheckBox, modeComboBox, generateImageButton);
        
        stackpane.getChildren().addAll(labelGrid, leftBox);
        //Setting up the VBox on the right side of the screen
        int rightWidth = 3 * width / 4;
        BorderPane mainPane = new BorderPane();
        
        VBox centerBox = new VBox();
        centerBox.setPrefSize(rightWidth, height);
        centerBox.setAlignment(Pos.BOTTOM_CENTER);
        centerBox.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

        // Top box
        VBox topBox = new VBox();
        Font subtitleFont = new Font("Arial",24);
        Text subtitle = new Text("Fresnel Diffraction Simulator"); //Temporarily Hard-Coded
        subtitle.setFont(subtitleFont);
        subtitle.setTextAlignment(TextAlignment.CENTER);
        topBox.getChildren().add(subtitle);
        
        //Adding all the layotus to the VBox on the right
        mainPane.setTop(topBox);
        mainPane.setLeft(stackpane);
        mainPane.setCenter(centerBox);

        Scene scene = new Scene(mainPane, width, height);

        primaryStage.setTitle("Hackathon 2018");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
