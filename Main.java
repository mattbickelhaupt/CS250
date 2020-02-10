package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    FileChooser fileChooser = new FileChooser();

    public double realWidth = 500;
    public double realHeight = 300;
    public int sizeOfCanvas = 350;
    public String lineType = "None";
    public String shapeType = "None";

    public boolean changes = false;
    public List<Shape> shapes = new ArrayList<>();


    final Canvas canvas = new Canvas(realWidth + 50, realHeight);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    Image selectedImage;
    ScrollPane sp2 = new ScrollPane(canvas);

    Stage primaryStage = new Stage();
    Stage secondaryStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("File Chooser Sample");

        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");

        MenuItem btnProp = new MenuItem("Properties");
        MenuItem btnSave = new MenuItem("Save");
        MenuItem btnSaveAs = new MenuItem("Save As");
        MenuItem btnNew = new MenuItem("New");
        MenuItem btnOpen = new MenuItem("Open Image");
        MenuItem btnOpen2 = new MenuItem("Open Squad Image");
        MenuItem btnResize = new MenuItem("Resize Canvas");

        MenuItem btnUndo = new MenuItem("Undo");
        MenuItem btnRedo = new MenuItem("Redo");

        fileMenu.getItems().addAll(btnProp, btnOpen, btnOpen2, btnSave, btnSaveAs, btnNew);
        editMenu.getItems().addAll(btnUndo, btnRedo, btnResize);

        MenuBar topMenu = new MenuBar();
        topMenu.setPadding(new Insets(0, 0, 0, 0));
        topMenu.getMenus().add(fileMenu);
        topMenu.getMenus().add(editMenu);

        final ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLACK);
        final ColorPicker colorPicker2 = new ColorPicker();
        colorPicker2.setValue(Color.WHITE);

        final ComboBox lineSizeBox = new ComboBox();
        lineSizeBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        final ComboBox lineTypeBox = new ComboBox();
        lineTypeBox.getItems().addAll("Straight Line", "Free Hand", "Rectangle", "Square", "Circle", "Ellipse", "Color Dropper");
        lineSizeBox.setMaxWidth(30);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, realWidth + 50, realHeight);
        gc.setLineWidth(1);

        Rectangle rect = new Rectangle();
        Circle circ = new Circle();
        Ellipse elip = new Ellipse();

        sp2.setPrefWidth(0);
        sp2.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        final StackPane zoomPane = new StackPane();
        sp2.setContent(zoomPane);

        myButton btnClose = new myButton("Close");

        myBox hboxBot = new myBox();
        hboxBot.getChildren().addAll(btnClose);

        BorderPane root = new BorderPane();
        ScrollPane sp = new ScrollPane(root);
        sp.setPrefWidth(0);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp.setContent(root);
        VBox fullMenu = new VBox();
        myBox hboxTop = new myBox();
        hboxTop.getChildren().addAll(topMenu, colorPicker,colorPicker2, lineTypeBox, lineSizeBox);
        fullMenu.getChildren().addAll(hboxTop);

        root.setCenter(sp2);
        root.setTop(fullMenu);
        root.setBottom(hboxBot);

        final double SCALE_DELTA = 1.1;

        zoomPane.getChildren().add(canvas);
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

        Scene scene = new Scene(sp, realWidth + 120, realHeight + 150);
        primaryStage.setScene(scene);
        primaryStage.show();

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        if (lineType == "Free Hand") {

                            gc.lineTo(event.getX(), event.getY());
                            gc.stroke();
                            gc.closePath();
                            gc.beginPath();
                            gc.moveTo(event.getX(), event.getY());

                        }
                        changes = true;
                        primaryStage.setTitle("File Chooser Sample *not saved");

                    }
                });

        canvas.setOnMouseReleased(e-> {

            if(lineType == "Rectangle") {
                rect.setWidth(Math.abs((e.getX() - rect.getX())));
                rect.setHeight(Math.abs((e.getY() - rect.getY())));
                gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            }
            if(lineType == "Square") {
                rect.setWidth(Math.abs((e.getX() - rect.getX())));
                rect.setHeight(rect.getWidth());
                gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
            }
            else if(lineType == "Circle"){
                circ.setRadius((Math.abs(e.getX() - circ.getCenterX()) + Math.abs(e.getY() - circ.getCenterY())) / 2);
                gc.fillOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());
                gc.strokeOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());

            }
            else if(lineType == "Ellipse"){
                elip.setRadiusX(Math.abs(e.getX() - elip.getCenterX()) + Math.abs(e.getY() - elip.getCenterY()));
                elip.setRadiusY(elip.getRadiusX()/2);

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

            changes = true;
            primaryStage.setTitle("File Chooser Sample *not saved");

        });

        canvas.setOnMousePressed(e->{
            WritableImage writableImage = new WritableImage((int) sizeOfCanvas, (int) sizeOfCanvas);
            canvas.snapshot(null, writableImage);
            saveToFile(writableImage, 1);

            gc.setStroke(colorPicker.getValue());
            gc.setFill(colorPicker2.getValue());

            if(lineType == "Color Dropper"){
                WritableImage image = new WritableImage((int) realWidth, (int) realHeight);
                canvas.snapshot(null, image);
                PixelReader r = image.getPixelReader();
                Color color = r.getColor((int) e.getX(), (int) e.getY());
                colorPicker.setValue(color);

            }
            if(lineType == "Rectangle") {
                rect.setX(e.getX());
                rect.setY(e.getY());
            }

            else if(lineType == "Square") {
                rect.setX(e.getX());
                rect.setY(e.getY());
            }

            else if(lineType == "Circle"){
                circ.setCenterX(e.getX());
                circ.setCenterY(e.getY());
            }

            else if(lineType == "Ellipse"){
                elip.setCenterX(e.getX());
                elip.setCenterY(e.getY());
            }

            else if (lineType == "Straight Line") {
                gc.stroke();
                //graphicsContext.closePath();
                gc.beginPath();
                gc.moveTo(e.getX(), e.getY());

            }

        });

        lineSizeBox.setEditable(true);
        lineSizeBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                gc.setLineWidth(Double.parseDouble(t1));
            }
        });

        lineTypeBox.setEditable(true);
        lineTypeBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                lineType = t1;
            }
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
            final KeyCombination keyComb2 = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
            final KeyCombination keyComb3 = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
            final KeyCombination keyComb4 = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.CONTROL_DOWN);

            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    saveCheck();
                    if (changes == false) {
                        primaryStage.setTitle("File Chooser Sample");
                    }
                    ke.consume(); // <-- stops passing the event to next node
                }
                if (keyComb2.match(ke)) {
                    open();
                    ke.consume(); // <-- stops passing the event to next node
                }
                if (keyComb3.match(ke)) {
                    zoomIn();
                    ke.consume(); // <-- stops passing the event to next node
                }
            }

        });

        btnUndo.setOnAction(i -> {
            undo();
        });

        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                gc.setStroke(colorPicker.getValue());
                gc.closePath();
                gc.beginPath();
            }
        });


        btnProp.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {

                try {
                    Properties(secondaryStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnNew.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                saveCheck();

            }
        });

        btnClose.setOnAction(i -> {
            System.exit(0);
        });

        btnOpen.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                open();
            }
        });

        btnOpen2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Image selectedImage = null;
                try {
                    selectedImage = new Image(new FileInputStream("C:\\users\\matth\\pictures\\paintpic.jpg"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                realWidth = selectedImage.getWidth();
                realHeight = selectedImage.getHeight();
                if (realWidth > 1500) {
                    realWidth = realWidth / 5;
                    realHeight = realHeight / 5;

                }

                primaryStage.setWidth(realWidth);
                primaryStage.setHeight(realHeight + 150);
                canvas.setWidth(realWidth);
                canvas.setHeight(realHeight);
                sp2.setPrefWidth(realWidth);
                gc.drawImage(selectedImage, 0, 0, realWidth, realHeight);

                changes = true;
                primaryStage.setTitle("File Chooser Sample *not saved");


            }

        });

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                saveCheck();
                if (changes == false) {
                    primaryStage.setTitle("File Chooser Sample");
                }
            }
        });


        btnSaveAs.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
                FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
                FileChooser.ExtensionFilter extFilter3 = new FileChooser.ExtensionFilter("GIF files (*.gif)", "*.gif");

                fileChooser.getExtensionFilters().addAll(extFilter, extFilter2, extFilter3);

                //Show save file dialog
                File file = fileChooser.showSaveDialog(primaryStage);

                if (file != null) {
                    try {
                        WritableImage writableImage = new WritableImage((int) sizeOfCanvas, (int) sizeOfCanvas);
                        canvas.snapshot(null, writableImage);
                        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                        ImageIO.write(renderedImage, "png", file);
                    } catch (IOException ex) {
                        // Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                changes = false;
                primaryStage.setTitle("File Chooser Sample");

            }
        });

        btnResize.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                resize();
            }
        });

    }

    public void Properties(Stage secondaryStage) throws Exception {
        showProperties();
    }

    public static void saveToFile(Image image, int i) {
        File outputFile;
        if (i == 1) {
            outputFile = new File("C:\\users\\matth\\documents\\paintwork.png");

        } else {
            outputFile = new File("C:\\users\\matth\\documents\\college\\CS250\\paintwork.png");

        }
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ImagePage img = new ImagePage();
        //PaintGui b = new PaintGui();
        //Multiply m = new Multiply();
        launch(args);

    }

    public void showProperties() {
        secondaryStage.setTitle("Properties");

        Text text = new Text();
        String info = "" +
                "27 January 2020\n" +
                "Pain(t) Submission 1\n" +
                "Matthew Bickelhaupt \n" +
                "CS250\n" +
                "\n" +
                "The program is compiled of one main function with three pages. \n" +
                "Opening page, image viewer, and blank paint viewer.\n" +
                "\n" +
                "Components Include:\n" +
                "-Open Image from file\n" +
                "-Save Image\n" +
                "-Close program\n" +
                "-Menu Bar: File, Save, Go back, Properties, Edit\n" +
                "-Increase and Decrease Image view\n" +
                "\n" +
                "https://github.com/mattbickelhaupt/CS250\n";

        text.setText(info);

        Button btnClose = new Button("Close");
        btnClose.setOnAction(i -> {
            secondaryStage.close();
        });

        Canvas canvas = new Canvas();
        HBox hboxMain = new HBox();

        hboxMain.setPadding(new Insets(15, 12, 15, 12));
        hboxMain.setSpacing(50);
        hboxMain.getChildren().addAll(btnClose);

        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().addAll(text, hboxMain);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.BLUEVIOLET);
        graphicsContext.fillRect(0, 0, 200, 200);
        Scene scene = new Scene(flowPane, 375, 320);

        secondaryStage.setScene(scene);
        secondaryStage.show();

    }

    private void saveCheck() {
        if (changes == true) {
            Stage saveCheck = new Stage();
            saveCheck.setTitle("Save Changes?");
            saveCheck.setWidth(300);
            myBox saveBox = new myBox();
            myButton btnYes = new myButton("Save");
            myButton btnCancel = new myButton("Cancel");

            BorderPane savePane = new BorderPane(saveBox);
            saveBox.getChildren().addAll(btnYes, btnCancel);
            btnYes.setOnAction(new EventHandler<ActionEvent>() {

                public void handle(ActionEvent t) {


                    WritableImage writableImage = new WritableImage((int) sizeOfCanvas, (int) sizeOfCanvas);
                    canvas.snapshot(null, writableImage);
                    saveToFile(writableImage, 0);

                    changes = false;

                    saveCheck.close();


                }
            });

            btnCancel.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    saveCheck.close();
                }
            });
            Scene saveScene = new Scene(savePane);
            saveCheck.setScene(saveScene);
            saveCheck.show();
        }

    }

    public void open() {

        saveCheck();
        Image selectedImage = new Image(fileChooser.showOpenDialog(primaryStage).toURI().toString());
        realWidth = selectedImage.getWidth();
        realHeight = selectedImage.getHeight();

        if (realWidth > 1500) {
            realWidth = realWidth / 5;
            realHeight = realHeight / 5;

        }

        primaryStage.setWidth(realWidth);
        primaryStage.setHeight(realHeight + 150);
        canvas.setWidth(realWidth);
        canvas.setHeight(realHeight);
        sp2.setPrefWidth(realWidth);
        gc.drawImage(selectedImage, 0, 0, realWidth, realHeight);

        changes = true;
        primaryStage.setTitle("File Chooser Sample *not saved");

    }

    public void undo() {

        Image selectedImage = null;
        try {
            selectedImage = new Image(new FileInputStream("C:\\users\\matth\\documents\\paintwork.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        realWidth = selectedImage.getWidth();
        realHeight = selectedImage.getHeight();
        if (realWidth > 1500) {
            realWidth = realWidth / 5;
            realHeight = realHeight / 5;

        }

        primaryStage.setWidth(realWidth + 50);
        primaryStage.setHeight(realHeight + 150);
        canvas.setWidth(realWidth);
        canvas.setHeight(realHeight);
        sp2.setPrefWidth(realWidth);
        gc.drawImage(selectedImage, 0, 0, realWidth, realHeight);

    }

    public void resize() {
        Stage resizeStage = new Stage();
        resizeStage.setTitle("Resize Canvas");
        BorderPane resizePane = new BorderPane();
        Scene resizeScene = new Scene(resizePane);
        TextField widthInput = new TextField();
        TextField heightInput = new TextField();
        Label widthText = new Label("New Width:   ");
        Label heightText = new Label("New Height:  ");
        HBox widthFields = new HBox();
        HBox heightFields = new HBox();
        HBox buttons = new HBox();
        buttons.setSpacing(20);
        myButton btnApply = new myButton("Apply");
        myButton btnCancel = new myButton("Cancel");
        buttons.getChildren().addAll(btnApply, btnCancel);
        widthFields.getChildren().addAll(widthText, widthInput, buttons);
        heightFields.getChildren().addAll(heightText, heightInput);
        VBox resizeBox = new VBox();
        resizeBox.setSpacing(10);
        resizeBox.setPadding(new Insets(10, 10, 10, 10));
        resizeBox.getChildren().addAll(widthFields, heightFields, buttons);
        resizePane.setCenter(resizeBox);
        resizeStage.setScene(resizeScene);
        resizeStage.show();

        btnApply.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                realWidth = Double.parseDouble(widthInput.getText());
                realHeight = Double.parseDouble(heightInput.getText());
                canvas.setWidth(realWidth);
                canvas.setHeight(realHeight);
                gc.drawImage(selectedImage, 0, 0, realWidth, realHeight);
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, realWidth + 50, realHeight);
                sp2.setPrefWidth(realWidth);

                resizeStage.close();


            }
        });
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                resizeStage.close();
            }
        });
    }

    public void zoomIn() {

        canvas.setScaleX(12);
        canvas.setScaleY(12);

        changes = true;
        primaryStage.setTitle("File Chooser Sample *not saved");

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {


                    }
                });


    }

}