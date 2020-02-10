package sample;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class myLabel extends Label {
    myLabel(String text){
        this.setTextFill(Color.WHITE);
        this.setText(text);
    }

}
