package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class myToolMenu extends HBox {

    private static ColorPicker colorPicker;
    private static boolean autoSave = false;
    private static Canvas myCanvas;
    // private String myLineType = "Free Hand";
    //private String myLineType;
    public boolean mychanges;
    public GraphicsContext myGC;
    //final ColorPicker colorPicker = new ColorPicker();

    public Color color1;

    /**
     * Tool Menu
     * Contains settings for color 1 and 2, line width, tool being used, and autosave
     * @param canvas    main canvas
     * @param gc        graphics context of canvas
     * @param color     color being used
     * @param changes   changes made true or false
     */
    myToolMenu(Canvas canvas, GraphicsContext gc, Color color, boolean changes){

        myCanvas = canvas;
        mychanges = changes;
        myGC = gc;
        color1 = color;

        ToggleButton TS = new ToggleButton("AS");
        final ToggleGroup group = new ToggleGroup();
        group.getToggles().add(TS);

        colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLACK);
        final ColorPicker colorPicker2 = new ColorPicker();
        colorPicker2.setValue(Color.WHITE);

        final ComboBox lineSizeBox = new ComboBox();
        lineSizeBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        final ComboBox lineTypeBox = new ComboBox();
        lineTypeBox.getItems().addAll("Straight Line", "Free Hand", "Rectangle", "Square", "Circle", "Ellipse", "Color Dropper", "Text",
                "Eraser", "Crazy Circles","Triangle","Polygon","Grab", "Move"
                );
        lineSizeBox.setMaxWidth(30);

        this.getChildren().addAll(colorPicker,colorPicker2, lineTypeBox, lineSizeBox,TS);

        lineSizeBox.setEditable(true);
        lineSizeBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                myGC.setLineWidth(Double.parseDouble(t1));                      //set line width to chosen number

            }
        });

        lineTypeBox.setEditable(true);
        lineTypeBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                //myLineType = t1;
                Main.setLineType(t1);                                           //send linetype string to Main
                //myLineType = "Free Hand";
            }
        });

        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                myGC.setStroke(colorPicker.getValue());     //set border color
            }
        });

        colorPicker2.setOnAction(new EventHandler() {
            public void handle(Event t) {
                myGC.setFill(colorPicker2.getValue());      //set filler color
            }
        });

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle toggle, Toggle new_toggle) {
                if (toggle == null){ autoSave = true; }                 //toggling will turn autosave timer on

                else { autoSave = false; }                              //turns autosave timer off
            }
        });
    }

    /**
     * setColor of drawing
     * @param C
     */
    public static void setColor(Color C){
        colorPicker.setValue(C);                //set color value from dropper
    }

    public static void myAutoSave(Stage stage) {    //autosave function
        if (getAutoSave()) {

            WritableImage image2 = new WritableImage(myHBox.getMyWidth(), myHBox.getMyHeight());
            myCanvas.snapshot(null, image2);
            Main.saveToFile(image2, 0);
            stage.setTitle("Matt's Paint");
        }
    }

    public static boolean getAutoSave(){        //returns if autosave is on or off
            return autoSave;
    }


}
