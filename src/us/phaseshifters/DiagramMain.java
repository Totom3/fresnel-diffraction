/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.phaseshifters;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Sam
 */
public class DiagramMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        DiagramPane diagram = new DiagramPane();
        Image lightBulb = new Image("assets/LightIcon.png");
        ImageView imageView = new ImageView(lightBulb);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);

        Scene scene = new Scene(diagram, 600, 450);

        primaryStage.setTitle("Diagram Pane");
        primaryStage.setScene(scene);
        primaryStage.show();

        diagram.drawCanvas(780, true, 300, ExperimentType.DOUBLE_SLIT, 0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
