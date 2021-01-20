package me.anuar2k.yafemas.solver;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class LUSolver implements TridiagonalSystemSolver {
    @Override
    public RealVector solve(RealMatrix aMatrix, RealVector bVector) {
        return new LUDecomposition(aMatrix).getSolver().solve(bVector);
    }
}
