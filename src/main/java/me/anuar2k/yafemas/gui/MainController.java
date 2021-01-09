package me.anuar2k.yafemas.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Label debugLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.debugLabel.setText("SIEMARA GITA");
    }
}
