package me.anuar2k.yafemas.solver;

import java.util.List;
import java.util.function.Function;

public class Solution {
    public final double domLeft;
    public final double domRight;
    public final List<Function<Double, Double>> elements;

    public Solution(double domLeft, double domRight, List<Function<Double, Double>> elements) {
        this.domLeft = domLeft;
        this.domRight = domRight;
        this.elements = elements;
    }

    public double value(double x) {
        return this.elements.stream().mapToDouble(el -> el.apply(x)).sum();
    }
}
