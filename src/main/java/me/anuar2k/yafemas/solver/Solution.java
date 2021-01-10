package me.anuar2k.yafemas.solver;

public class Solution {
    public final double domLeft;
    public final double domRight;
    public final double[] coefficients;

    public Solution(double domLeft, double domRight, double[] coefficients) {
        this.domLeft = domLeft;
        this.domRight = domRight;
        this.coefficients = coefficients;
    }
}
