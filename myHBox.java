package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class myHBox extends HBox {

    private static Canvas myCanvas;
    private static int realHeight;
    private static int realWidth;
    private static Stage PStage;
    private static Stage SStage;
    private static BorderPane myRoot;

    private static boolean mychanges;
    private static boolean dataLoss;
    private static FileChooser.ExtensionFilter selectedFilter;
    private static FileChooser fileChooser = new FileChooser();
    private static ScrollPane mysp2;
    private static GraphicsContext myGC;
    private static Image image1;
    private static Image image2;

    //public boolean mychanges;
    //public Canvas myCanvas;
    //public GraphicsContext myGC;
    //public int realWidth;
    //public int realHeight;
    //public ScrollPane mysp2;

    //public Stage PStage;

    public Color color1;
    public String myLineType;

    //FileChooser fileChooser = new FileChooser();

    /**
     * File/Edit Menu
     * This menu contains settings for open, save, new, resize, properties, undo, and redo
     * @param canvas    main canvas
     * @param gc        graphics context of canvas
     * @param primaryStage main Paint application page
     * @param secondaryStage    secondary stage for saveCheck, properties, and resize
     * @param color     current color being used
     * @param Width     width of canvas
     * @param Height    height of canvas
     * @param changes   changes made or saved
     * @param sp2       scrollpane for canvas
     * @param root      borderpane holding all of the components
     */
    myHBox(Canvas canvas, GraphicsContext gc, Stage primaryStage, Stage secondaryStage, Color color, int Width, int Height, boolean changes,
           ScrollPane sp2, BorderPane root
    ) {

        PStage = primaryStage;
        SStage = secondaryStage;
        myCanvas = canvas;
        mychanges = changes;
        myGC = gc;
        myRoot = root;
        realHeight = Height;
        realWidth = Width;
        mysp2 = sp2;
        color1 = color;

        this.setPadding(new Insets(0, 15, 5, 0));
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #336699;");

        javafx.scene.control.Menu fileMenu = new javafx.scene.control.Menu("File");
        javafx.scene.control.Menu editMenu = new javafx.scene.control.Menu("Edit");

        javafx.scene.control.MenuItem btnProp = new javafx.scene.control.MenuItem("Properties");
        javafx.scene.control.MenuItem btnSave = new javafx.scene.control.MenuItem("Save");
        javafx.scene.control.MenuItem btnSaveAs = new javafx.scene.control.MenuItem("Save As");
        javafx.scene.control.MenuItem btnNew = new javafx.scene.control.MenuItem("New");
        javafx.scene.control.MenuItem btnOpen = new javafx.scene.control.MenuItem("Open Image");
        javafx.scene.control.MenuItem btnOpen2 = new javafx.scene.control.MenuItem("Open Squad Image");     //Not being used
        javafx.scene.control.MenuItem btnResize = new javafx.scene.control.MenuItem("Resize Canvas");

        javafx.scene.control.MenuItem btnUndo = new javafx.scene.control.MenuItem("Undo");
        javafx.scene.control.MenuItem btnRedo = new javafx.scene.control.MenuItem("Redo");

        fileMenu.getItems().addAll(btnProp, btnOpen, btnSave, btnSaveAs, btnNew);
        editMenu.getItems().addAll(btnUndo, btnRedo, btnResize);
                //add menu items to main menu
        javafx.scene.control.MenuBar topMenu = new MenuBar();
        topMenu.setPadding(new Insets(0, 0, 0, 0));
        topMenu.getMenus().addAll(fileMenu, editMenu);

        this.getChildren().add(topMenu);

        btnUndo.setOnAction(i -> {
            undo();     //call undo function
        });
        btnRedo.setOnAction(i -> {
            redo();     //call redo function
        });


        btnProp.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {

                try {
                    Properties(secondaryStage); //open properties on second stage
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnNew.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    secondaryStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                saveCheck(secondaryStage,0,null);

            }
        });

        btnOpen.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                open(secondaryStage);
            }   //open file chooser
        });

        btnOpen2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Image selectedImage = null;
                try {
                    selectedImage = new Image(new FileInputStream("C:\\users\\matth\\pictures\\paintpic.jpg"));
                                //open specific file, used for quick open access and testing
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                selectedFilter = fileChooser.getSelectedExtensionFilter();

                setSize((int) selectedImage.getWidth(), (int) selectedImage.getHeight());

                if (getMyWidth() > 1500) {
                    setSize(getMyWidth() / 4, getMyHeight() / 4);
                    setDataLoss(true);

                }

                resize();
                gc.drawImage(selectedImage, 0, 0, getMyWidth(), getMyHeight());

                setChanges(true);
                primaryStage.setTitle("Matt's Paint *not saved");


            }

        });

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {     //Save button
                saveCheck(secondaryStage,0,null);
                primaryStage.setTitle("Matt's Paint");
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

                if(selectedFilter != fileChooser.getSelectedExtensionFilter()){
                    setDataLoss(true);
                    saveCheck(secondaryStage,1,file);
                    selectedFilter = fileChooser.getSelectedExtensionFilter();

                }

                setChanges(false);
                primaryStage.setTitle("Matt's Paint");

            }
        });
        btnResize.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                resizeCanvas();
            }
        });
    }

    public static void open(Stage secondaryStage) {

        saveCheck(secondaryStage,0,null);
            //ask to save if changes are made

        Image selectedImage = new Image(fileChooser.showOpenDialog(PStage).toURI().toString());
        setSize((int) selectedImage.getWidth(), (int) selectedImage.getHeight());

        selectedFilter = fileChooser.getSelectedExtensionFilter();

        if (getMyWidth() > 1500) {
            setSize(getMyWidth() / 5, getMyWidth() / 5);
            setDataLoss(true);

        }

        resize();
        myGC.drawImage(selectedImage, 0, 0, getMyWidth(), getMyHeight());
        setChanges(false);

        PStage.setTitle("Matt's Paint *not saved");

    }

    public static boolean getChanges() {
        return mychanges;       //return boolean if changes are made
    }

    /**
     * set changes
     * @param c
     */
    public static void setChanges(boolean c) {
        mychanges = c;
    }   //set boolean if changes are made

    public static int getMyWidth() {
        return realWidth;
    }   //get width of canvas

    public static int getMyHeight() {
        return realHeight;
    }   //get height of canvas

    public static boolean getDataLoss() {
        return dataLoss;
    }   //get height of canvas

    public static void setDataLoss(boolean d) {
        dataLoss = d;
    }   //set boolean if changes are made


    /**
     * sets size of canvas
     * @param w
     * @param h
     */
    public static void setSize(int w, int h) {
        realWidth = w;              //set width and height of canvas
        realHeight = h;
    }

    /**
     * Popup window that asks if you want to save changes made
     * @param secondaryStage
     */
    public static void saveCheck(Stage secondaryStage,int i, File file) {
                            //popup to check if you want to save changes

        if (getChanges()) {     //if changes are made

            secondaryStage.setTitle("Save Changes?");

            if(getDataLoss()){
                secondaryStage.setTitle("Image Scaling may cause Data Loss. Okay?");

            }
            secondaryStage.setWidth(300);

            myBox saveBox = new myBox();
            myButton btnYes = new myButton("Save");
            myButton btnCancel = new myButton("Cancel");

            BorderPane savePane = new BorderPane(saveBox);
            saveBox.getChildren().addAll(btnYes, btnCancel);

            btnYes.setOnAction(new EventHandler<ActionEvent>() {

                public void handle(ActionEvent t) {

                    if(i == 1){
                            if (file != null) {
                                try {
                                    WritableImage writableImage = new WritableImage(getMyWidth(), getMyHeight());
                                    myCanvas.snapshot(null, writableImage);
                                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                                    ImageIO.write(renderedImage, "png", file);

                                    setChanges(false);
                                    setDataLoss(false);
                                    secondaryStage.close();

                                } catch (IOException ex) {
                                    // Logger.getLogger(JavaFX_DrawOnCanvas.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }


                    else {
                        WritableImage writableImage = new WritableImage(getMyWidth(), getMyHeight());
                        myCanvas.snapshot(null, writableImage);
                        saveToFile(writableImage, 0);

                        setChanges(false);
                        setDataLoss(false);
                        secondaryStage.close();
                    }
                }
            });

            btnCancel.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    secondaryStage.close();
                }
            });


            Scene scene = new Scene(savePane, 300, 300);
            secondaryStage.setScene(scene);
            secondaryStage.show();
        }

    }


    /**
     * Popup window that displays properties of current version
     * @param secondaryStage
     * @throws Exception
     */
    public void Properties(Stage secondaryStage) throws Exception {
        secondaryStage.setTitle("Properties");
        ScrollPane propSP = new ScrollPane();
        Text text = new Text();
        String info = "20 April 2020\n" +
                "Final Pain(t) Submission\n" +
                "Matthew Bickelhaupt \n" +
                "CS250\n" +
                "\n" +
                "New Paint Updates v7.0 \t\t(4/20/2020)\n" +
                "\tScrollPane was added to Properties tab to view all of README file\n" +
                "\tPolygon coloring was fixed \n" +
                "\tAutosave Saves every 5 seconds rather than 10\n" +
                "\tWindow/Menu spacing and zoom is more comfortable\n" +
                "\t\"Open Squad Image\" for testing was removed\n" +
                "\t\"*not Saved\" Window Title was fixed for Autosave, Save As, and Ctrl+S\n" +
                "\t\n" +
                "The program is compiled of one main function with new classes for different nodes:\n" +
                "\tmyBox\n" +
                "\tmyButton\n" +
                "\tmyCanvas\n" +
                "\tmyBorderPane\n" +
                "\tmyVBox\n" +
                "\tmyHBox\n" +
                "\tmyToolMenu\n" +
                "\tTriangle\n" +
                "\n" +
                "In addition, there are new functions to make the main function cleaner and easier to read and sort:\n" +
                "\tsaveCheck();\n" +
                "\topen();\n" +
                "\tundo();\n" +
                "\tredo();\n" +
                "\tresizeCanvas();\n" +
                "\tresize();\n" +
                "\tsaveToFile();\n" +
                "\tsetLineType();\n" +
                "\tsetSPWidth();\n" +
                "\tgetChanges();\n" +
                "\tsetChanges();\n" +
                "\tgetMyWidth();\n" +
                "\tgetMyHeight();\n" +
                "\tsetSize();\n" +
                "\tautoSave();\n" +
                "\tgetDataLoss();\n" +
                "\tsetDataLoss();\n" +
                "\n" +
                "Components Include:\n" +
                "-Open Image from file\n" +
                "-Save Image\n" +
                "-Close program\n" +
                "-Menu Bar: File, Save, Go back, Properties, Edit\n" +
                "-Save, Save As, Open, New\n" +
                "-saveCheck() will provide a double check window to ensure you want to save the changes made\n" +
                "-Stage Title will display if project is saved or not\n" +
                "-resize() will open a window with width and height inputs for canvas\n" +
                "-colorPicker1 will set color for lines/borders\n" +
                "-colorPicker2 will set color for shape fill\n" +
                "-shapeType drop down menu will provide options for drawing:\n" +
                "\tFree Hand\n" +
                "\tStraight Line\n" +
                "\tSquare\n" +
                "\tRectangle\n" +
                "\tCircle\n" +
                "\tEllipse\n" +
                "\tColor Grabber\n" +
                "-lineSize drop down menu will provide numbers 1-10 for line width/border width\n" +
                "-resize() will increase and decrease Image view\n" +
                "-Undo() will remove the most recent change made by placing an old saved image on top \n" +
                "-Redo() will paste the most recent undo made by placing an old saved image on top \n" +
                "-New button will open a blank canvas, and double check using saveCheck()\n" +
                "-Control+S will bring up saveCheck() and save\n" +
                "-Control+O will open a new image file\n" +
                "-Now supports save/open from .jpg, .png, and .gif files\n" +
                "-Triangles, polygons can be drawn\n" +
                "-Text can be added to the canvas\n" +
                "-Eraser will draw white\n" +
                "-Timer will count to 5 seconds and autosave if turned on\n" +
                "-Stage title displays if the canvas has saved the recent changes\n" +
                "-After opening an image, that file type is saved\n" +
                "\t+if saved as a smaller file type, a popup, similar to savecheck() \n" +
                "\tasks if possible data loss is okay\n" +
                "-JavaDoc comments were added to be viewed in JavaDoc form on a browser\n" +
                "\n" +
                "Tool Instructions:\n" +
                "Choose Tool from Drop Down Menu\n" +
                "-For Shapes, start at top left of shape and drag to bottom right\n" +
                "-Grab tool, start at top left of shape and drag to bottom right\n" +
                "-Move tool, drag and drop selected rectangle to place\n" +
                "-For text tool, input your text in bottom textField, then click on canvas for location\n" +
                "-For polygon, input number of edges in bottom textField, then click around that number\n" +
                "\tto add each vertex\n" +
                "-For triangle, click once for each vertex and after third vertext will draw your triangle \n" +
                "-Button will enable/diable autosave.\n" +
                "https://github.com/mattbickelhaupt/CS250\n" ;
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
        propSP.setContent(flowPane);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.BLUEVIOLET);
        graphicsContext.fillRect(0, 0, 200, 200);
        Scene scene = new Scene(propSP, 375, 320);

        secondaryStage.setScene(scene);
        secondaryStage.show();
    }

    /**
     * saves given image to file, i is used for undo and redo parameters
     * @param image
     * @param i
     */
    public static void saveToFile(Image image, int i) {

        File outputFile;
        if (i == 1) {
            outputFile = new File("C:\\users\\matth\\documents\\paintwork1.png");

        }
        else if (i == 2) {
            outputFile = new File("C:\\users\\matth\\documents\\paintwork2.png");

        }else {
            outputFile = new File("C:\\users\\matth\\documents\\college\\CS250\\paintwork.png");

        }
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void undo() {
                //replaces canvas with previous edit
        image2 = null;
        try {
            image2 = new Image(new FileInputStream("C:\\users\\matth\\documents\\paintwork2.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        myGC.drawImage(image2, 0, 0, realWidth, realHeight);

    }

    public static void redo() {         //replaces canvas with what was undone

        image1 = null;
        try {
            image1 = new Image(new FileInputStream("C:\\users\\matth\\documents\\paintwork1.png"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        myGC.drawImage(image1, 0, 0, realWidth, realHeight);

    }

    public static void resize() {    //popup window that takes parameters for new size of window

        myRoot.setPrefWidth(getMyWidth());
        PStage.setWidth(getMyWidth());
        PStage.setHeight(getMyHeight() + 150);
        myCanvas.setWidth(getMyWidth());
        myCanvas.setHeight(getMyHeight());
        mysp2.setPrefWidth(getMyWidth());


    }

        public void resizeCanvas() {    //popup window that takes parameters for new size of window

        saveCheck(SStage,0, null);

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
                setSize((int) Double.parseDouble(widthInput.getText()), (int) Double.parseDouble(heightInput.getText()));

                resize();
                //myGC.drawImage(selectedImage, 0, 0, realWidth, realHeight);
                myGC.setFill(Color.WHITE);
                myGC.fillRect(0, 0, getMyWidth() + 50, getMyHeight());
                Main.setSPWidth();
                resizeStage.close();


            }
        });


    }
}


