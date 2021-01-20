package me.anuar2k.yafemas.solver;

import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.linear.*;

import java.util.Arrays;

public class Solver {
    public final int integrationPointCount;
    private final UnivariateIntegrator integrator;

    public Solver(int integrationPointCount) {
        if (integrationPointCount < 10) {
            throw new IllegalArgumentException("integrationPointCount must be > 10");
        }

        this.integrationPointCount = integrationPointCount;
        this.integrator = new IterativeLegendreGaussIntegrator(
                integrationPointCount,
                1e-6,
                1e-6);
    }

    public Solution solve(int discretization) {
        if (discretization < 2) {
            throw new IllegalArgumentException("n must be > 2");
        }

        double dom = 2;

        //build B matrix
        RealMatrix bMatrix = new Array2DRowRealMatrix(discretization, discretization);

        for (int n = 0; n < discretization; n++) {
            for (int m = 0; m < discretization; m++) {
                double integral = 0;

                if (Math.abs(m - n) <= 1) {
                    int finalN = n;
                    int finalM = m;

                    //find the intersection of basis function domains and the global domain [0, dom]
                    double integrateFrom = dom * Math.max(Math.max(n, m) - 1, 0             ) / discretization;
                    double integrateTo =   dom * Math.min(Math.min(n, m) + 1, discretization) / discretization;

                    integral = this.integrator.integrate(
                            Integer.MAX_VALUE,
                            x -> E(x) * dBasis_dx(discretization, dom, finalN, x) * dBasis_dx(discretization, dom, finalM, x),
                            integrateFrom,
                            integrateTo);
                }

                bMatrix.setEntry(n, m, -E(0) * basis(discretization, dom, n, 0) * basis(discretization, dom, m, 0) + integral);
            }
        }

        //build L vector
        RealVector lVector = new ArrayRealVector(discretization, 0);
        lVector.setEntry(0, -10 * E(0) * basis(discretization, dom, 0, 0));

        //calculate coefficients
//        RealVector coefficients = new LUDecomposition(bMatrix).getSolver().solve(lVector);
        RealVector coefficients = new ThomasSolver().solve(bMatrix, lVector);
        System.out.println(coefficients);

        double[] result = Arrays.copyOf(coefficients.toArray(), discretization + 1);
        result[discretization] = 0;

        return new Solution(0, dom, result);
    }

    private static double E(double x) {
        return x <= 1.0 ? 3.0 : 5.0;
    }
    
    private static double basis(int discretization, double dom, int i, double x) {
        double h = dom / discretization;
        double hInv = discretization / dom;
        
        double center = dom * i / discretization;
        double left = center - h;
        double right = center + h;

        if (x < left || x > right) {
            return 0.0;
        }
        if (x <= center) {
            return (x - left) * hInv;
        }
        return (right - x) * hInv;
    }
    
    private static double dBasis_dx(int discretization, double dom, int i, double x) {
        double h = dom / discretization;
        double hInv = discretization / dom;

        double center = dom * i / discretization;
        double left = center - h;
        double right = center + h;
        
        if (x < left || x > right) {
            return 0.0;
        }
        if (x <= center) {
            return hInv;
        }
        return -hInv;
    }
}
