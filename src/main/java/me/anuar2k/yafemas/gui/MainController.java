package me.anuar2k.yafemas.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import me.anuar2k.yafemas.solver.Solution;
import me.anuar2k.yafemas.solver.Solver;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    TextFlow yafemasLabel;

    @FXML
    LineChart<Number, Number> plot;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.buildYafemasLabel();
        Solution s = Solver.solve(100, 10);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for (int i = 0; i < s.coefficients.length; i++) {
            double x = (s.domRight - s.domLeft) / (s.coefficients.length - 1) * i;
            series.getData().add(new XYChart.Data<>(x, s.coefficients[i]));
        }

        this.plot.getData().add(series);
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
