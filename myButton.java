package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Button;

public class myButton extends Button {

    myButton(String text){
        this.setStyle("-fx-background-color: #84868b; -fx-text-fill: white;");
        this.setPadding(new Insets(10,10,10,10));
        this.setHeight(30);
        this.setWidth(100);
        this.setText(text);
    }
}
