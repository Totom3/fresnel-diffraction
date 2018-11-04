package us.phaseshifters;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author Administrator
 */
public class AperturePane extends Pane {

	private final Canvas canvas;
	public List<Vec2D> pointList;
	int width;
	int height;

	public AperturePane(int width, int height) {
		this.width = width;
		this.height = height;
		this.canvas = new Canvas(width, height);
		this.pointList = new ArrayList<>();

		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Main.params.isReversed() ? Color.BLACK : Color.WHITE);

		gc.fillRect(0, 0, width, height);
		getChildren().add(canvas);

		setOnMouseReleased(e -> {
			pointList.add(new Vec2D((int) e.getX() - getWidth()/2, (int) e.getY() - getHeight()/2));
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
		double shiftX = width / 2;
		double shiftY = height / 2;

		gc.setFill(Main.params.isReversed() ? Color.BLACK : Color.WHITE);
		gc.fillRect(0, 0, width, height);
		gc.setFill(Main.params.isReversed() ? Color.WHITE : Color.BLACK);

		// Draw the circles for each point
		double circleSize = 4;
		double offset = circleSize / 2;
		gc.setFill(Main.params.isReversed() ? Color.WHITE : Color.BLACK);
		for (Vec2D p : pointList) {
			gc.fillOval(p.x + shiftX - offset, p.y + shiftY - offset, circleSize, circleSize);
		}

		// Draw polygon
		double[] xPoints = new double[pointList.size()];
		double[] yPoints = new double[pointList.size()];
		for (int i = 0; i < pointList.size(); i++) {
			xPoints[i] = pointList.get(i).x + shiftX;
			yPoints[i] = pointList.get(i).y + shiftY;
		}

		gc.fillPolygon(xPoints, yPoints, xPoints.length);

		gc.setLineWidth(2);
		gc.setStroke(Color.RED);

		double w = canvas.getWidth();
		double h = canvas.getHeight();
		gc.strokeLine(w / 2, 0, w / 2, h);
		gc.strokeLine(0, h / 2, w, h / 2);
	}

}
