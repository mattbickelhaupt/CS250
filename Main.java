package sample;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main extends Application {
    FileChooser fileChooser = new FileChooser();

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("File Chooser Sample");

        Button btnImg = new Button("Open Image");
        Button btnPaint = new Button("Paint from Scratch");
        Button btnClose = new Button("Close");

        btnImg.setOnAction(e -> {

            try {
                ImagePage(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

        btnPaint.setOnAction(e -> {

            try {
                PaintPage(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

        btnClose.setOnAction(i -> {
            System.exit(0);
        });


        HBox hboxMain = new HBox();
        hboxMain.setPadding(new Insets(15, 12, 15, 12));
        hboxMain.setSpacing(10);
        hboxMain.setStyle("-fx-background-color: #336699;");
        hboxMain.getChildren().addAll(btnImg, btnPaint, btnClose);

        Scene scene = new Scene(hboxMain, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public void ImagePage(Stage primaryStage) throws Exception {

        //File file = fileChooser.showOpenDialog(primaryStage);
        BorderPane root = new BorderPane();

        Image selectedImage = new Image(fileChooser.showOpenDialog(primaryStage).toURI().toString());
        ImageView iv = new ImageView(selectedImage);
        iv.setFitHeight(300);
        iv.setFitWidth(300);
        root.setCenter(iv);

        Button btnFile = new Button("File");
        Button btnEdit = new Button("Edit");
        Button btnView = new Button("View");

        btnFile.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        btnEdit.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        btnView.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        Menu fileMenu = new Menu("File");
        MenuItem propBtn = new MenuItem("Properties");
        MenuItem saveBtn = new MenuItem("Save");
        MenuItem backBtn = new MenuItem("Go Back");
        fileMenu.getItems().addAll(propBtn,saveBtn,backBtn);

        Menu editMenu = new Menu("Edit");

        MenuBar topMenu = new MenuBar();
        topMenu.getMenus().add(fileMenu);
        topMenu.getMenus().add(editMenu);

        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent t) {
                            saveToFile(selectedImage);
                            }
                        });
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        HBox hboxTop = new HBox();
        hboxTop.setPadding(new Insets(15, 12, 15, 12));
        hboxTop.setSpacing(10);
        hboxTop.setStyle("-fx-background-color: #336699;");
        //hboxTop.getChildren().addAll(btnFile, btnEdit, btnView);
        hboxTop.getChildren().addAll(topMenu);

        Button btnInc = new Button("Inc Size");
        Button btnDec = new Button("Dec Size");
        Button btnClose = new Button("Close");

        btnInc.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        btnDec.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        btnClose.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        btnDec.setOnAction(i -> {
            iv.setFitWidth(iv.getFitWidth() - 10);
            iv.setFitHeight(iv.getFitHeight() - 10);
        });

        btnInc.setOnAction(i -> {
            iv.setFitWidth(iv.getFitWidth() + 10);
            iv.setFitHeight(iv.getFitHeight() + 10);
        });

        btnClose.setOnAction(i -> {
            System.exit(0);
        });


        HBox hboxBot = new HBox();
        hboxBot.setPadding(new Insets(15, 12, 15, 12));
        hboxBot.setSpacing(10);
        hboxBot.setStyle("-fx-background-color: #336699;");
        hboxBot.getChildren().addAll(btnInc, btnDec, btnClose);

        root.setTop(hboxTop);
        root.setBottom(hboxBot);

        Scene scene = new Scene(root, 400, 400);

        primaryStage.setScene(scene);
        primaryStage.show();


    }


    public void PaintPage(Stage primaryStage) throws Exception {

        //File file = fileChooser.showOpenDialog(primaryStage);
        BorderPane root = new BorderPane();

        Image selectedImage = new Image(new FileInputStream("C:\\users\\matth\\pictures\\blankpaint.png"));
        ImageView iv = new ImageView(selectedImage);
        iv.setFitHeight(300);
        iv.setFitWidth(300);
        root.setCenter(iv);

        Button btnFile = new Button("File");
        Button btnEdit = new Button("Edit");
        Button btnView = new Button("View");

        btnFile.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        btnEdit.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        btnView.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        Menu fileMenu = new Menu("File");
        MenuItem propBtn = new MenuItem("Properties");

        MenuItem saveBtn = new MenuItem("Save");
        MenuItem backBtn = new MenuItem("Go Back");
        MenuItem openBtn = new MenuItem("Open Image");

        fileMenu.getItems().addAll(propBtn,openBtn,saveBtn,backBtn);

        Menu editMenu = new Menu("Edit");

        MenuBar topMenu = new MenuBar();
        topMenu.getMenus().add(fileMenu);
        topMenu.getMenus().add(editMenu);

        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                saveToFile(selectedImage);
            }
        });

        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        openBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Image selectedImage = new Image(fileChooser.showOpenDialog(primaryStage).toURI().toString());
                ImageView iv = new ImageView(selectedImage);
                iv.setFitHeight(300);
                iv.setFitWidth(300);
                root.setCenter(iv);
            }

        });

        HBox hboxTop = new HBox();
        hboxTop.setPadding(new Insets(15, 12, 15, 12));
        hboxTop.setSpacing(10);
        hboxTop.setStyle("-fx-background-color: #336699;");
        //hboxTop.getChildren().addAll(btnFile, btnEdit, btnView);
        hboxTop.getChildren().addAll(topMenu);

        Button btnInc = new Button("Inc Size");
        Button btnDec = new Button("Dec Size");
        Button btnClose = new Button("Close");

        btnInc.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        btnDec.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
        btnClose.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

        btnDec.setOnAction(i -> {
            iv.setFitWidth(iv.getFitWidth() - 10);
            iv.setFitHeight(iv.getFitHeight() - 10);
        });

        btnInc.setOnAction(i -> {
            iv.setFitWidth(iv.getFitWidth() + 10);
            iv.setFitHeight(iv.getFitHeight() + 10);
        });

        btnClose.setOnAction(i -> {
            System.exit(0);
        });

        HBox hboxBot = new HBox();
        hboxBot.setPadding(new Insets(15, 12, 15, 12));
        hboxBot.setSpacing(10);
        hboxBot.setStyle("-fx-background-color: #336699;");
        hboxBot.getChildren().addAll(btnInc, btnDec, btnClose);

        root.setTop(hboxTop);
        root.setBottom(hboxBot);

        Scene scene = new Scene(root, 400, 400);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void saveToFile(Image image) {
        File outputFile = new File("C:\\users\\matth\\pictures\\paint.png");
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
}

