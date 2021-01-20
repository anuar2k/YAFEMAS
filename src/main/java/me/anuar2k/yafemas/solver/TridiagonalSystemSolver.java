package me.anuar2k.yafemas.solver;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public interface TridiagonalSystemSolver {
    RealVector solve(RealMatrix aMatrix, RealVector bVector);
}
