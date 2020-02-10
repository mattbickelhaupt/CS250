package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class myBox extends HBox {
    myBox(){

        this.setPadding(new Insets(0, 15, 5, 0));
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #336699;");

    }
}
