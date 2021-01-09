package me.anuar2k.yafemas.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    TextFlow yafemasLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.buildYafemasLabel();
    }

    private void buildYafemasLabel() {
        String[] words = "Yet Another Finite Element Method Assignment Solution".split(" ");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            Text firstLetter = new Text(word.substring(0, 1));
            Text rest = new Text(word.substring(1));

            firstLetter.setStyle("-fx-font-weight: bold");
            rest.setStyle("-fx-font-weight: normal");

            this.yafemasLabel.getChildren().addAll(firstLetter, rest);
            if (i != words.length - 1) {
                this.yafemasLabel.getChildren().add(new Text("\n"));
            }
        }
    }
}
