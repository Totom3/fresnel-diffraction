/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.phaseshifters;


import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class AperturePane extends Pane{

    public static Canvas canvas;
    public static ArrayList<Point> pointList;
    public static boolean connected = false;
    
    public AperturePane(){
        this.canvas = new Canvas();
        pointList = new ArrayList<>();
        canvas.setHeight(400);
        canvas.setWidth(400);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if(!Main.reversed){
            gc.setFill(Color.WHITE);
        }else{
            gc.setFill(Color.BLACK);
        }
        gc.fillRect(0, 0, 400, 400);
        getChildren().add(canvas);
        
        this.setOnMouseReleased(e -> {
            pointList.add(new Point((int)e.getX(), (int)e.getY()));
            drawCanvas();
        });
        
    }
    
    public Canvas getCanvas(){
        return canvas;
    }
    
    public void drawCanvas(){
        int x;
        int y;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for(Point p : pointList){
            x = (int) p.getX();
            y = (int) p.getY();
            if(!Main.reversed){
                gc.setFill(Color.BLACK);
            }else{
                gc.setFill(Color.WHITE);
            }
            gc.fillOval(x, y, 5, 5);
        }
        if(pointList.size() > 1){
            for (int i = 0; i < pointList.size() - 1; i++) {
                Point p1 = pointList.get(i);
                Point p2 = pointList.get(i + 1);
                gc.setLineWidth(1);
                gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                
            }
        }
    }


}
