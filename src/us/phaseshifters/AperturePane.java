package us.phaseshifters;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import static us.phaseshifters.Main.reversed;

/**
 *
 * @author Administrator
 */
public class AperturePane extends Pane {

    private final Canvas canvas;
    public List<Vec2D> pointList;
    private boolean connected;
    int width;
    int height;

    public AperturePane(int width, int height) {
        this.width = width;
        this.height = height;
        this.canvas = new Canvas(width, height);
        this.pointList = new ArrayList<>();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Main.reversed ? Color.BLACK : Color.WHITE);

        gc.fillRect(0, 0, width, height);
        getChildren().add(canvas);

        setOnMouseReleased(e -> {
            pointList.add(new Vec2D((int) e.getX(), (int) e.getY()));
            drawCanvas();
        });

    }

    public Canvas getCanvas() {
        return canvas;
    }

    public List<Vec2D> getPoints() {
        return pointList;
    }

    public void drawCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw the circles for each point
        double circleSize = 4;
        double offset = circleSize / 2;
        

       

        if (!reversed) {
            gc.setFill(Color.WHITE);
        } else {
            gc.setFill(Color.BLACK);
        }
        gc.fillRect(0, 0, width, height);
        if (!reversed) {
            gc.setFill(Color.BLACK);
        } else {
            gc.setFill(Color.WHITE);
        }
        
        gc.setFill(Main.reversed ? Color.WHITE : Color.BLACK);
        for (Vec2D p : pointList) {
            gc.fillOval(p.x - offset, p.y - offset, circleSize, circleSize);
        }
        
         // Draw polygon
        double[] xPoints = new double[pointList.size()];
        double[] yPoints = new double[pointList.size()];
        for (int i = 0; i < pointList.size(); i++) {
            xPoints[i] = pointList.get(i).x;
            yPoints[i] = pointList.get(i).y;
        }
        
        gc.fillPolygon(xPoints, yPoints, xPoints.length);
    }

}
