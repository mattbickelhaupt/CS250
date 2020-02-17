package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import jdk.nashorn.internal.ir.PropertyNode;

public class myToolMenu extends HBox {

    private static ColorPicker colorPicker;
    // private String myLineType = "Free Hand";
    //private String myLineType;
    public boolean mychanges;
    public Canvas myCanvas;
    public GraphicsContext myGC;
    //final ColorPicker colorPicker = new ColorPicker();

    public Color color1;

    myToolMenu(Canvas canvas, GraphicsContext gc, Color color, boolean changes){

        myCanvas = canvas;
        mychanges = changes;
        myGC = gc;
        color1 = color;

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

        this.getChildren().addAll(colorPicker,colorPicker2, lineTypeBox, lineSizeBox);

        lineSizeBox.setEditable(true);
        lineSizeBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                myGC.setLineWidth(Double.parseDouble(t1));

            }
        });

        lineTypeBox.setEditable(true);
        lineTypeBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                //myLineType = t1;
                Main.setLineType(t1);
                //myLineType = "Free Hand";
            }
        });

        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                myGC.setStroke(colorPicker.getValue());
                myGC.closePath();
                myGC.beginPath();
            }
        });

        colorPicker2.setOnAction(new EventHandler() {
            public void handle(Event t) {
                myGC.setFill(colorPicker2.getValue());
                myGC.closePath();
                myGC.beginPath();
            }
        });

    }

    public static void setColor(Color C){
        colorPicker.setValue(C);

    }




}
