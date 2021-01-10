package me.anuar2k.yafemas.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(this.getClass().getResource("main.fxml"));

        primaryStage.setTitle("YAFEMAS - anuar2k 2020");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("main.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
