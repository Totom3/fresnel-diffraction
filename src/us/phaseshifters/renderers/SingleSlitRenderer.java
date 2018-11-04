/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.phaseshifters.renderers;

import static java.lang.Math.*;
import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author Administrator
 */
public class SingleSlitRenderer implements DiffractionRenderer{

    
    private final double PI = Math.PI;
    private final int wavelength = 500;
    private final double intensity = 250;
    private final double distanceAW = 1;
    private double slitWidth = 1;
    @Override
    public void render(Canvas canvas, DiffractionParameters params, int size, int resolution) {
        
        //Will contain intensity 
        ArrayList<Double> doubleList = new ArrayList();
        
        //Finding the intensity for half the x
        for (int i = 0; i < size/2; i += resolution) {
            doubleList.add(getIntensity(i));
        }
        for (int j = size/2; j < 0; j--) {
            doubleList.add(doubleList.get(j));
        }
        
        for (int i = 0; i < 10; i++) {
            
        }
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Color color = wavelengthToColor(params.getWavelength());
        
        for (int i = 0; i < size; i += resolution) {
            
            double calculatedIntensity = getIntensity(i);
            calculatedIntensity = Math.max(0, Math.min(255, calculatedIntensity));
            
            for (int j = 0; j < size; j += resolution) {
                double brightness = Color.grayRgb((int) calculatedIntensity).getBrightness();
                gc.setFill(Color.hsb(color.getHue(), color.getSaturation(), brightness));
                gc.fillOval(i, j, resolution, resolution);
            }
            
        }  
    }
    
    public double getIntensity(double x){
        
        double sinAngle = x/(sqrt(x * x + distanceAW * distanceAW));
        double denominator = ((PI*slitWidth * sinAngle) / wavelength);
        double numerator = Math.sin(denominator);
        
        System.out.println(intensity * Math.pow((numerator/denominator), 2));
        return intensity * Math.pow((numerator/denominator), 2);
        
    }
    
}