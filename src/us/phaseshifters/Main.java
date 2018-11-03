package us.phaseshifters;

import java.awt.Dimension;
import java.awt.Toolkit;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            height = screenSize.height;
            width = screenSize.width;
	
            //Setting up the VBox on the left side of the screen
            VBox vbox = new VBox();
            int leftWidth = width/4;
            vbox.setAlignment(Pos.CENTER_LEFT);
            vbox.setPrefSize(height, leftWidth);

            //Creating a grid pane for the sliders
            GridPane sliderGrid = new GridPane();
            sliderGrid.setAlignment(Pos.TOP_CENTER);
            sliderGrid.setHgap(leftWidth/4);
            sliderGrid.setVgap((height/2)/8);
            
            //Creating all the sliders
            Slider wavelength = new Slider();
            wavelength.setPrefSize(2*leftWidth/3, height/2/4);
            
            Slider distanceAW = new Slider();
            distanceAW.setPrefSize(2*leftWidth/3, height/2/4);
            
            Slider distanceAS = new Slider();
            distanceAS.setPrefSize(2*leftWidth/3, height/2/4);
            
            Slider sourceIntensity = new Slider();
            sourceIntensity.setPrefSize(2*leftWidth/3, height/2/4);
            
            //Creating all the labels
            Label wavelengthLabel = new Label("Wavelength");
            Label distanceAWLabel = new Label("Distance Aperture - Wall");
            Label distanceASLabel = new Label("Distance Aperture - Source");
            Label sourceIntensityLabel = new Label("Source Intensity");
            
            //Setting the width of the first column to 1/3 of the VBox size and the second column to be 2/3
           
           
            
            //1st row
            sliderGrid.setConstraints(wavelengthLabel, 0, 0);
            sliderGrid.setConstraints(wavelength, 1, 0);
            //2nd row
            sliderGrid.setConstraints(distanceAWLabel, 0, 1);
            sliderGrid.setConstraints(distanceAW, 1, 1);
            //3rd row
            sliderGrid.setConstraints(distanceASLabel, 0, 2);
            sliderGrid.setConstraints(distanceAS, 1, 2);
            //4th row
            sliderGrid.setConstraints(wavelengthLabel, 0, 3);
            sliderGrid.setConstraints(sourceIntensity, 1, 3);

            //Adding all the elements to the silder grid
            sliderGrid.getChildren().addAll(wavelength, distanceAW, distanceAS,
                    sourceIntensity,wavelengthLabel, distanceAWLabel,
                    distanceASLabel, sourceIntensityLabel);
            //Adding the slider grid to the left VBox
            vbox.getChildren().add(sliderGrid);
            
            //Creating the layout for the buttons (goes under the slider grid)
            VBox buttonBox = new VBox(height/2/4/6);
            
            //Creating the buttons and the check box and setting their size
            CheckBox setupCheckBox = new CheckBox("Display the setup?");
            setupCheckBox.setPrefSize(leftWidth, height/2/4);
            
            Button apertureButton = new Button("Aperture");
            apertureButton.setPrefSize(leftWidth, height/2/4);
            
            Button modeButton = new Button("Mode");
            modeButton.setPrefSize(leftWidth, height/2/4);
            
            Button generateImageButton = new Button("Generate Image");
            generateImageButton.setPrefSize(leftWidth, height/2/4);
            
            buttonBox.getChildren().addAll(setupCheckBox, 
                    apertureButton, modeButton, generateImageButton);
            
            //Adding the button box to the left VBox
            vbox.getChildren().add(buttonBox);
            
            
            //Setting up the VBox on the right side of the screen
            int rightWidth = width - width/3;
            VBox displayBox = new VBox(height/3/4);
            displayBox.setAlignment(Pos.CENTER_RIGHT);
            displayBox.setPrefSize(height, rightWidth);
            
            //Creating two Vbox inside the Vbox on the right side of the screen
            //and a StackPane for the sub title
            VBox rightUpBox = new VBox();
            rightUpBox.setAlignment(Pos.CENTER);
            VBox rightDownBox = new VBox();
            rightDownBox.setAlignment(Pos.BOTTOM_CENTER);
            StackPane subtitlePane = new StackPane();
            subtitlePane.setAlignment(Pos.TOP_CENTER);
            
            
            //Adding the sub-title to the subtitle stack pane
            Font subtitleFont = new Font("Arial", 24);    
            Text subtitle = new Text("Sub-Title"); //Temporarly Hard-Coded
            subtitle.setFont(subtitleFont);
            subtitlePane.getChildren().add(subtitle);
            
            //Temporarly adding the subtitle to the display boxes(up and down) to visualize
            rightUpBox.getChildren().add(subtitle);
            rightDownBox.getChildren().add(subtitle);
            
            //Adding all the layotus to the VBox on the right
            displayBox.getChildren().addAll(rightUpBox, rightDownBox, subtitlePane);
            
            //Creating the horizontal box (main layout) it will contain both the 
            //left and the right Vbox
            HBox layout = new HBox();
            layout.getChildren().addAll(vbox, displayBox);
            
            Scene scene = new Scene(layout, width, height);
            
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
