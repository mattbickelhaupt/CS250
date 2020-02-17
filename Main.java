package sample;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.w3c.dom.css.Rect;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;


public class Main extends Application {

    private static String lineType;
    private static ScrollPane sp2 = new ScrollPane();
    private boolean changes = true;
    public boolean cut = false;
    public Image image3;
    public double realWidth = 500;
    public double realHeight = 300;
    double x[] = new double[3];
    double y[] = new double[3];
    double xp[] = new double[20];
    double yp[] = new double[20];

    public int point = 0;
    public Canvas canvas = new Canvas();

    public GraphicsContext gc = canvas.getGraphicsContext2D();
    private WritableImage grabImage;
    private WritableImage moveImage;
    private PixelReader pixelReader;

    Stage primaryStage = new Stage();
    Stage secondaryStage = new Stage();
    public Color color;

    double prevX, prevY;

    @Override
    public void start(Stage primaryStage) throws Exception {

        canvas.setWidth(realWidth);
        canvas.setHeight(realHeight);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, realWidth + 50, realHeight);
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        Rectangle rect = new Rectangle();
        Rectangle grabRect = new Rectangle();
        Rectangle hole = new Rectangle();

        Circle circ = new Circle();
        Ellipse elip = new Ellipse();

        final StackPane zoomPane = new StackPane();

        BorderPane root = new BorderPane();

        //ScrollPane sp2 = new ScrollPane();

        ScrollPane sp = new ScrollPane();

        myVBox VBox = new myVBox(canvas, gc, primaryStage, secondaryStage, color, (int) realWidth, (int) realHeight, changes, sp2);

        root.setTop(VBox);

        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp.setContent(root);

        sp2.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp2.setContent(zoomPane);

        //drawText myText = new drawText(canvas,gc,x,y);
        Label lblText = new Label("Text:   ");
        TextField myText = new TextField();
        HBox textBox = new HBox();
        textBox.getChildren().addAll(lblText, myText);

        String str = myText.getText();
        //int num = (int)Double.parseDouble(str);


        root.setBottom(textBox);
        zoomPane.getChildren().add(canvas);
        root.setCenter(sp2);
        root.setPrefWidth(myHBox.getMyWidth() + 100);

        Scene scene = new Scene(sp, myHBox.getMyWidth() + 120, myHBox.getMyHeight() + 150);
        primaryStage.setScene(scene);
        primaryStage.show();

        canvas.setOnMousePressed(e -> {

            WritableImage image2 = new WritableImage(myHBox.getMyWidth(), myHBox.getMyHeight());
            canvas.snapshot(null, image2);
            saveToFile(image2, 2);

            if (lineType == "Color Dropper") {
                WritableImage image = new WritableImage(myHBox.getMyWidth(), myHBox.getMyHeight());
                canvas.snapshot(null, image);
                PixelReader r = image.getPixelReader();
                Color color2 = r.getColor((int) e.getX(), (int) e.getY());
                color = color2;
                myToolMenu.setColor(color);

            }
            if (lineType == "Rectangle") {
                rect.setX(e.getX());
                rect.setY(e.getY());

            }

            if(lineType == "Grab"){
                grabImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                canvas.snapshot(null, grabImage);

                hole.setX(e.getX());
                hole.setY(e.getY());

                rect.setX(e.getX());
                rect.setY(e.getY());

                grabRect.setX(e.getX());
                grabRect.setY(e.getY());
            }

            else if (lineType == "Square") {
                rect.setX(e.getX());
                rect.setY(e.getY());

            }
            else if (lineType == "Triangle"){

                x[point] = e.getX();
                y[point] = e.getY();

                gc.setStroke(color);
                gc.lineTo(e.getX(), e.getY());
                gc.setStroke(color);
                gc.stroke();
                gc.moveTo(e.getX(), e.getY());

                point++;
                if(point == 3){
                    gc.setStroke(color);
                    gc.fillPolygon(x,y,3);
                    gc.strokePolygon(x,y,3);
                    gc.strokePolyline(x,y,3);
                    gc.closePath();
                    gc.beginPath();
                    point = 0;

                }
            }

            else if (lineType == "Polygon"){
                int num = (int)Double.parseDouble(myText.getText());
                Triangle.setPoints(num);
                xp[point] = e.getX();
                yp[point] = e.getY();
                gc.lineTo(e.getX(), e.getY());
                gc.setStroke(color);
                gc.stroke();
                gc.moveTo(e.getX(), e.getY());

                point++;
                if(point == num){
                    gc.fillPolygon(xp,yp,num);
                    gc.strokePolygon(xp,yp,num);
                    gc.strokePolyline(xp,yp,num);
                    gc.closePath();
                    gc.beginPath();
                    point = 0;

                }
            }
            else if (lineType == "Circle" || lineType == "Crazy Circles") {
                circ.setCenterX(e.getX());
                circ.setCenterY(e.getY());
            }
            else if (lineType == "Ellipse") {
                elip.setCenterX(e.getX());
                elip.setCenterY(e.getY());
            }
            else if (lineType == "Straight Line") {
                gc.stroke();
                //graphicsContext.closePath();
                gc.beginPath();
                gc.moveTo(e.getX(), e.getY());

            } else if (lineType == "Text") {

                gc.moveTo(e.getX(), e.getY());
                gc.strokeText(myText.getText(),e.getX(),e.getY());

            }
        });

        canvas.setOnMouseDragged(e -> {

            if (lineType == "Free Hand") {

                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                gc.closePath();
                gc.beginPath();
                gc.moveTo(e.getX(), e.getY());

                myHBox.setChanges(true);
                primaryStage.setTitle("File Chooser Sample *not saved");
            }

            else if(lineType == "Move"){

                gc.fillRect(hole.getX(), hole.getY(), hole.getWidth(), hole.getHeight());
                gc.strokeRect(hole.getX(), hole.getY(), hole.getWidth(), hole.getHeight());

                gc.drawImage(grabImage, 0, 0);
                gc.drawImage(moveImage, e.getX(), e.getY());

            }

            else if(lineType == "Rectangle"){
                rect.setWidth(Math.abs((e.getX() - rect.getX())));
                rect.setHeight(Math.abs((e.getY() - rect.getY())));
                gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);

            }
            else if(lineType == "Square"){
                rect.setWidth(Math.abs((e.getX() - rect.getX())));
                rect.setHeight(rect.getWidth());
                gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);

            }

            else if(lineType == "Crazy Circles"){
                circ.setRadius((Math.abs(e.getX() - circ.getCenterX()) + Math.abs(e.getY() - circ.getCenterY())) / 2);

                gc.fillOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());
                gc.strokeOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());

            }

            else if(lineType == "Eraser"){
                gc.setStroke(Color.WHITE);
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                gc.closePath();
                gc.beginPath();
                gc.moveTo(e.getX(), e.getY());

            }


        });

        canvas.setOnMouseReleased(e -> {

            WritableImage image1 = new WritableImage(myHBox.getMyWidth(), myHBox.getMyHeight());
            canvas.snapshot(null, image1);
            saveToFile(image1, 1);

            myHBox.setChanges(true);
            if(lineType == "Grab"){
                gc.setStroke(Color.WHITE);
                gc.setFill(Color.WHITE);

                grabRect.setWidth(Math.abs(e.getX()-grabRect.getX()));
                grabRect.setHeight(Math.abs(e.getY()-grabRect.getY()));

                if(grabRect.getX() > e.getX()) {
                    grabRect.setX(e.getX());
                }
                if(grabRect.getY() > e.getY()) {
                    grabRect.setY(e.getY());
                }

                WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                canvas.snapshot(null, writableImage);
                gc.fillRect(grabRect.getX(), grabRect.getY(), grabRect.getWidth(), grabRect.getHeight());

                pixelReader = writableImage.getPixelReader();
                moveImage = new WritableImage(pixelReader, (int)grabRect.getX(), (int)grabRect.getY(), (int)grabRect.getWidth(), (int)grabRect.getHeight());

                hole.setWidth(Math.abs((e.getX() - hole.getX())));
                hole.setHeight(Math.abs((e.getY() - hole.getY())));
            }

            else if(lineType == "Move"){

                gc.fillRect(hole.getX(), hole.getY(), hole.getWidth(), hole.getHeight());
                gc.strokeRect(hole.getX(), hole.getY(), hole.getWidth(), hole.getHeight());
                gc.drawImage(moveImage, e.getX(), e.getY());

            }
            else if (lineType == "Rectangle") {

                rect.setWidth(Math.abs((e.getX() - rect.getX())));
                rect.setHeight(Math.abs((e.getY() - rect.getY())));
                //rect.setX((rect.getX() > e.getX()) ? e.getX(): rect.getX());
                if(rect.getX() > e.getX()) {
                    rect.setX(e.getX());
                }
                //rect.setY((rect.getY() > e.getY()) ? e.getY(): rect.getY());
                if(rect.getY() > e.getY()) {
                    rect.setY(e.getY());
                }
                gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            }
            if (lineType == "Square") {
                rect.setWidth(Math.abs((e.getX() - rect.getX())));
                rect.setHeight(rect.getWidth());
                gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            }
            else if (lineType == "Circle") {
                circ.setRadius((Math.abs(e.getX() - circ.getCenterX()) + Math.abs(e.getY() - circ.getCenterY())) / 2);
                if(circ.getCenterX() > e.getX()) {
                    circ.setCenterX(e.getX());
                }
                if(circ.getCenterY() > e.getY()) {
                    circ.setCenterY(e.getY());
                }
                gc.fillOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());
                gc.strokeOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());

            }
            else if (lineType == "Ellipse") {
                elip.setRadiusX(Math.abs(e.getX() - elip.getCenterX()) + Math.abs(e.getY() - elip.getCenterY()));
                elip.setRadiusY(elip.getRadiusX() / 2);

                gc.fillOval(elip.getCenterX(), elip.getCenterY(), elip.getRadiusX(), elip.getRadiusY());
                gc.strokeOval(elip.getCenterX(), elip.getCenterY(), elip.getRadiusX(), elip.getRadiusY());

            }
            else if (lineType == "Free Hand") {

                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                gc.closePath();
                gc.beginPath();
            }
            else if (lineType == "Straight Line") {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                gc.closePath();
            }
            myHBox.setChanges(true);
            primaryStage.setTitle("File Chooser Sample *not saved");

        });

        final double SCALE_DELTA = 1.1;

        zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor =
                        (event.getDeltaY() > 0)
                                ? SCALE_DELTA
                                : 1 / SCALE_DELTA;

                canvas.setScaleX(canvas.getScaleX() * scaleFactor);
                canvas.setScaleY(canvas.getScaleY() * scaleFactor);
            }
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
            final KeyCombination keyComb2 = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
            final KeyCombination keyComb3 = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
            final KeyCombination keyComb4 = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.CONTROL_DOWN);

            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    myHBox.saveCheck(secondaryStage, myHBox.getChanges());
                    if (myHBox.getChanges()) {
                        primaryStage.setTitle("File Chooser Sample");
                    }
                    ke.consume(); // <-- stops passing the event to next node
                }
                if (keyComb2.match(ke)) {
                    myHBox.open(secondaryStage);
                    ke.consume(); // <-- stops passing the event to next node
                }

            }

        });

    }

    public static void setLineType(String line) {
        lineType = line;
    }


    public static void setSPWidth(){
            sp2.setPrefWidth(myHBox.getMyWidth());

    }

    public static void saveToFile(Image image, int i) {
        File outputFile;
        if (i == 1) {
            outputFile = new File("C:\\users\\matth\\documents\\paintwork1.png");

        }
        else if (i == 2) {
            outputFile = new File("C:\\users\\matth\\documents\\paintwork2.png");

        }
        else {
            outputFile = new File("C:\\users\\matth\\documents\\college\\CS250\\paintwork.png");

        }
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void moveImg(Image image, int x, int y, int width, int height){


    }


    public static void main(String[] args) {
        launch(args);
    }
}
