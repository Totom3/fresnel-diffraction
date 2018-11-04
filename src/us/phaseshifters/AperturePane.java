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
	private List<Vec2D> pointList;
	private boolean connected;

	public AperturePane(int width, int height) {
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
		gc.setFill(Main.reversed ? Color.WHITE : Color.BLACK);
		for (Vec2D p : pointList) {
			gc.fillOval(p.x - offset, p.y - offset, circleSize, circleSize);
		}

		// Draw lines between points
		if (pointList.size() > 1) {
			for (int i = 0; i < pointList.size() - 1; i++) {
				Vec2D p1 = pointList.get(i);
				Vec2D p2 = pointList.get(i + 1);
				gc.setLineWidth(1);
				gc.strokeLine(p1.x, p1.y, p2.x, p2.y);
			}
		}
	}

}
