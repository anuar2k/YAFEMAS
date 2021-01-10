package me.anuar2k.yafemas.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Spinner;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import me.anuar2k.yafemas.solver.Solution;
import me.anuar2k.yafemas.solver.Solver;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TextFlow yafemasLabel;

    @FXML
    private LineChart<Number, Number> plot;

    @FXML
    private Spinner<Integer> feCount;

    @FXML
    private Spinner<Integer> ipCount;

    private Solver solver = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.buildYafemasLabel();

        this.feCount.getValueFactory().valueProperty().addListener(value -> this.recalculate());
        this.ipCount.getValueFactory().valueProperty().addListener(value -> this.recalculate());

        this.recalculate();
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

    private void recalculate() {
        if (this.solver == null || this.solver.integrationPointCount != this.ipCount.getValue()) {
            this.solver = new Solver(this.ipCount.getValue());
        }

        Solution solution = this.solver.solve(this.feCount.getValue());
        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for (int i = 0; i < solution.coefficients.length; i++) {
            double x = (solution.domRight - solution.domLeft) * i / (solution.coefficients.length - 1);
            series.getData().add(new XYChart.Data<>(x, solution.coefficients[i]));
        }

        this.plot.getData().clear();
        this.plot.getData().add(series);
    }
}
