package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class myVBox extends HBox {
    myVBox(Canvas canvas, GraphicsContext gc, Stage primaryStage,Stage secondaryStage, Color color, int realWidth, int realHeight,boolean changes,
            ScrollPane sp2
    ){
        this.setPadding(new Insets(0, 15, 5, 0));
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #336699;");

        myHBox HBoxTop = new myHBox(canvas,gc,primaryStage,secondaryStage, color, realWidth,realHeight,changes,sp2);

        myToolMenu ToolMenu = new myToolMenu(canvas,gc,color,changes);

        this.getChildren().addAll(HBoxTop, ToolMenu);

    }


}
