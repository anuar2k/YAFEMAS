package me.anuar2k.yafemas.solver;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class ThomasSolver implements TridiagonalSystemSolver {
    @Override
    public RealVector solve(RealMatrix aMatrix, RealVector bVector) {
        int n = aMatrix.getRowDimension();

        RealVector cPrim = new ArrayRealVector(n - 1);

        cPrim.setEntry(0, aMatrix.getEntry(0, 1)
                          / aMatrix.getEntry(0, 0));
        for (int i = 1; i < n - 1; i++) {
            cPrim.setEntry(i, aMatrix.getEntry(i, i + 1)
                              / (aMatrix.getEntry(i, i) - aMatrix.getEntry(i, i - 1) * cPrim.getEntry(i - 1)));
        }

        RealVector dPrim = new ArrayRealVector(n);

        dPrim.setEntry(0, bVector.getEntry(0)
                          / aMatrix.getEntry(0, 0));
        for (int i = 1; i < n; i++) {
            dPrim.setEntry(i, (bVector.getEntry(i) - aMatrix.getEntry(i, i - 1) * dPrim.getEntry(i - 1))
                              / (aMatrix.getEntry(i, i) - aMatrix.getEntry(i, i - 1) * cPrim.getEntry(i - 1)));
        }

        RealVector solution = new ArrayRealVector(n);
        solution.setEntry(n - 1, dPrim.getEntry(n - 1));
        for (int i = n - 2; i >= 0; i--) {
            solution.setEntry(i, dPrim.getEntry(i) - cPrim.getEntry(i) * solution.getEntry(i + 1));
        }

        return solution;
    }
}
