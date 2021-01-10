package me.anuar2k.yafemas.solver;

import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.linear.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Solver {
    private Solver() {

    }

    public static Solution solve(int discretization, int integrationPointCount) {
        if (discretization < 2) {
            throw new IllegalArgumentException("n must be > 2");
        }
        if (integrationPointCount < 2) {
            throw new IllegalArgumentException("integrationPointCount must be > 2");
        }

        double dom = 2;
        double h = dom / discretization;
        double hInv = discretization / dom;

        UnivariateIntegrator integrator = new IterativeLegendreGaussIntegrator(
                integrationPointCount,
                IterativeLegendreGaussIntegrator.DEFAULT_RELATIVE_ACCURACY,
                IterativeLegendreGaussIntegrator.DEFAULT_ABSOLUTE_ACCURACY);

        int basisCount = discretization + 1;
        List<Function<Double, Double>> basis = new ArrayList<>(basisCount);
        List<Function<Double, Double>> basisDerivatives = new ArrayList<>(basisCount);

        //build basis functions and their derivatives
        basis.add(x -> {
            if (x < 0 || x > h) {
                return 0.0;
            }
            return 1.0 - x * hInv;
        });

        basisDerivatives.add(x -> {
            if (x < 0 || x > h) {
                return 0.0;
            }
            return -hInv;
        });

        for (int i = 1; i < basisCount - 1; i++) {
            double center = dom * i / discretization;
            double left = center - h;
            double right = center + h;

            basis.add(x -> {
                if (x < left || x > right) {
                    return 0.0;
                }
                if (x <= center) {
                    return (x - left) * hInv;
                }
                return (right - x) * hInv;
            });

            basisDerivatives.add(x -> {
                if (x < left || x > right) {
                    return 0.0;
                }
                if (x <= center) {
                    return hInv;
                }
                return -hInv;
            });
        }

        basis.add(x -> {
            if (x < dom - h || x > dom) {
                return 0.0;
            }

            return (x - dom) * hInv + 1;
        });

        basisDerivatives.add(x -> {
            if (x < dom - h || x > dom) {
                return 0.0;
            }
            
            return hInv;
        });

        //build B matrix
        RealMatrix bMatrix = new Array2DRowRealMatrix(basisCount, basisCount);

        for (int n = 0; n < basisCount; n++) {
            for (int m = 0; m < basisCount; m++) {
                Function<Double, Double> u = basis.get(n);
                Function<Double, Double> uDerivative = basisDerivatives.get(n);
                Function<Double, Double> v = basis.get(m);
                Function<Double, Double> vDerivative = basisDerivatives.get(m);

                double integral = integrator.integrate(
                        Integer.MAX_VALUE,
                        x -> E(x) * uDerivative.apply(x) * vDerivative.apply(x),
                        0,
                        dom);

                bMatrix.setEntry(n, m, -E(0) * u.apply(0.0) * v.apply(0.0) + integral);
            }
        }

        //build L vector
        RealVector lVector = new ArrayRealVector(basisCount);

        for (int m = 0; m < basisCount; m++) {
            Function<Double, Double> v = basis.get(m);
            lVector.setEntry(m, -10 * E(0) * v.apply(0.0));
        }

        //calculate coefficients
        RealVector coefficients = new LUDecomposition(bMatrix).getSolver().solve(lVector);

        System.out.println(bMatrix);
        System.out.println(lVector);
        System.out.println(coefficients);

        List<Function<Double, Double>> elements = new ArrayList<>(basisCount);
        for (int i = 0; i < basisCount; i++) {
            Function<Double, Double> base = basis.get(i);
            double coefficient = coefficients.getEntry(i);

            elements.add(x -> coefficient * base.apply(x));
        }

        return new Solution(0, dom, elements);
    }

    private static double E(double x) {
        return x <= 1.0 ? 3.0 : 5.0;
    }
}
