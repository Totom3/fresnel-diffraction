/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.phaseshifters;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author Administrator
 */
public class DiagramPane extends Pane {

    private Canvas diagram;

    public int height;
    public int width;
    public DiagramPane() {
        this.diagram = new Canvas();
        getChildren().add(diagram);
    }

    public void drawCanvas(double wavelength, boolean reverse, double SA, 
            ExperimentType preset, double sourceXStart, int height, int width) {

        this.width = width;
        this.height = height;
        System.out.println(width + " " + height);
        diagram.setWidth(width);
        diagram.setHeight(height);

        //Setting Background
        GraphicsContext gc = diagram.getGraphicsContext2D();
        gc.setFill(wavelenghtToColor(wavelength));
        gc.fillRect(0, 0, width, height);

        //Drawing Lightbulb
        Image lightBulb = new Image("assets/LightIcon.png");
        gc.drawImage(lightBulb, sourceXStart, height / 2.8);

        //Drawing Wall 
        gc.setFill(Color.BLACK);
        gc.fillRect(width - width / 100 - 10, 0, width / 100, height);

        if (preset.equals(ExperimentType.DOUBLE_SLIT)) {
            drawDoubleSlit(sourceXStart, SA, gc);
            //Aperture Text
            gc.setFont(new Font("Impact", 16));
            gc.fillText("Aperture", SA - width / 12, height - height / 15);
        } else if (preset.equals(ExperimentType.SINGLE_SLIT) || reverse && preset.equals(ExperimentType.OBJECT) || reverse && preset.equals(ExperimentType.CIRCULAR_APERTURE)) {
            drawHoleObject(sourceXStart, SA, gc);
            //Aperture Text
            gc.setFont(new Font("Impact", 16));
            gc.fillText("Aperture", SA - width / 12, height - height / 15);
        } else {
            drawObject(sourceXStart, SA, gc);
            //Aperture Text
            gc.setFont(new Font("Impact", 16));
            gc.fillText("Aperture", SA - width / 12, height/2);
        }

        //Source Text
        gc.setFont(new Font("Impact", 16));
        gc.fillText("Source", lightBulb.getWidth() / 3.5, height / 2.8 + lightBulb.getHeight() + 5);

        //Wall Text
        gc.setFont(new Font("Impact", 16));
        gc.fillText("Wall", width - width / 10, height - height / 15);

    }

    public void drawObject(double sourceXStart, double SA, GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(sourceXStart + SA, height / 4.0, width / 100, height / 2.5);
    }

    public void drawHoleObject(double sourceXStart, double SA, GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(sourceXStart + SA, 0, width / 100, height / 2.1);
        gc.setFill(Color.BLACK);
        gc.fillRect(sourceXStart + SA, (height - (height / 2.1)), width / 100, height / 2.1);
    }

    public void drawDoubleSlit(double sourceXStart, double SA, GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(sourceXStart + SA, 0, width / 100, height / 2.5);
        gc.setFill(Color.BLACK);
        gc.fillRect(sourceXStart + SA, height / 2 - height / 20, width / 100, height / 10);
        gc.setFill(Color.BLACK);
        gc.fillRect(sourceXStart + SA, (height - (height / 2.5)), width / 100, height / 2.5);
    }

    public Color wavelenghtToColor(double wavelength) {

        double intensityMax = 255;
        double gamma = 0.8;
        double Red, Green, Blue;
        double factor;

        if ((wavelength >= 380) && (wavelength < 440)) {
            Red = -(wavelength - 440) / (440 - 380);
            Green = 0.0;
            Blue = 1.0;
        } else if ((wavelength >= 440) && (wavelength < 490)) {
            Red = 0.0;
            Green = (wavelength - 440) / (490 - 440);
            Blue = 1.0;
        } else if ((wavelength >= 490) && (wavelength < 510)) {
            Red = 0.0;
            Green = 1.0;
            Blue = -(wavelength - 510) / (510 - 490);
        } else if ((wavelength >= 510) && (wavelength < 580)) {
            Red = (wavelength - 510) / (580 - 510);
            Green = 1.0;
            Blue = 0.0;
        } else if ((wavelength >= 580) && (wavelength < 645)) {
            Red = 1.0;
            Green = -(wavelength - 645) / (645 - 580);
            Blue = 0.0;
        } else if ((wavelength >= 645) && (wavelength < 781)) {
            Red = 1.0;
            Green = 0.0;
            Blue = 0.0;
        } else {
            Red = 0.0;
            Green = 0.0;
            Blue = 0.0;
        }

        // Let the intensity fall off near the vision limits
        if ((wavelength >= 380) && (wavelength < 420)) {
            factor = 0.3 + (0.7 * (wavelength - 380) / (420 - 380));
        } else if ((wavelength >= 420) && (wavelength < 701)) {
            factor = 1.0;
        } else if ((wavelength >= 701) && (wavelength < 781)) {
            factor = 0.3 + 0.7 * (780 - wavelength) / (780 - 700);
        } else {
            factor = 0.0;
        }

        int[] rgb = new int[3];

        // Don't want 0^x = 1 for x <> 0
        rgb[0] = Red == 0.0 ? 0 : (int) Math.round(intensityMax * Math.pow(Red * factor, gamma));
        rgb[1] = Green == 0.0 ? 0 : (int) Math.round(intensityMax * Math.pow(Green * factor, gamma));
        rgb[2] = Blue == 0.0 ? 0 : (int) Math.round(intensityMax * Math.pow(Blue * factor, gamma));

        return Color.rgb(rgb[0], rgb[1], rgb[2]);
    }
}
