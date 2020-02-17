package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


public class Triangle extends Polygon {

    private static int numPoints = 3;

    Triangle(Canvas canvas, GraphicsContext gc, double x[], double y[], Color color,int points){
        numPoints = points;
        gc.setStroke(color);
        //double points[] = { x[0], x[1], x[3], y[0], y[1],y[2] };
        //Polygon polygon = new Polygon(points);
        gc.fillPolygon(x,y,numPoints);
        gc.strokePolygon(x,y,numPoints);
        gc.strokePolyline(x,y,numPoints);

    }
    public static void setPoints(int p){
        numPoints = p;
    }
}
