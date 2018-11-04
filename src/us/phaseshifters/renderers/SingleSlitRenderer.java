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
import us.phaseshifters.Main;

/**
 *
 * @author Administrator
 */
public class SingleSlitRenderer implements DiffractionRenderer{

    
    private final double PI = Math.PI;
    private final int wavelength = 500;
    private final double intensity = 1;
    private final double distanceAW = 1;
    private double slitWidth = 30;
    @Override
    public void render(Canvas canvas, DiffractionParameters params, int size, int resolution) {
        ArrayList<Double> doubleList = new ArrayList();
        
        for (int i = 0; i < size/2; i += resolution) {
            System.out.println(i);
            doubleList.add(getIntensity(i));
        }
        for (int j = size/2; j < 0; j--) {
            doubleList.add(doubleList.get(j));
        }
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int i = 0; i < doubleList.size(); i++) {
            
            double calculatedIntensity = doubleList.get(i);
            double brightness = Color.grayRgb((int) calculatedIntensity).getBrightness();
            Color color = wavelengthToColor(wavelength);
            gc.setFill(Color.hsb(color.getHue(), color.getSaturation(), brightness));
            
            for (int j = 0; j < doubleList.size(); j++) {
                gc.fillRect(i, j, resolution, resolution);
            }
        }
        
    }
    
    public double getIntensity(double x){
        
        double sinAngle = x/sqrt(x * x + distanceAW * distanceAW);
        double denominator = ((PI*slitWidth * sinAngle) / wavelength);
        double numerator = 1;
        
        System.out.println(intensity * Math.pow((numerator/denominator), 2));
        return intensity * Math.pow((numerator/denominator), 2);
        
    }
    
}