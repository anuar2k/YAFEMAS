package me.anuar2k.yafemas.solver;

import java.util.Arrays;
import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.analysis.integration.UnivariateIntegrator;
import org.apache.commons.math3.linear.*;

public class Solver {
  public final int integrationPointCount;
  private final UnivariateIntegrator integrator;

  public Solver(int integrationPointCount) {
    if (integrationPointCount < 10) {
      throw new IllegalArgumentException("integrationPointCount must be > 10");
    }

    this.integrationPointCount = integrationPointCount;
    this.integrator =
        new IterativeLegendreGaussIntegrator(integrationPointCount, 1e-6, 1e-6);
  }

  public Solution solve(int discretization) {
    if (discretization < 2) {
      throw new IllegalArgumentException("n must be > 2");
    }

    double dom = 2;

    // build B matrix
    RealMatrix bMatrix =
        new Array2DRowRealMatrix(discretization, discretization);

    for (int n = 0; n < discretization; n++) {
      for (int m = 0; m < discretization; m++) {
        double integral1 = 0;

        if (Math.abs(m - n) <= 1) {
          int finalN = n;
          int finalM = m;

          double integrateFrom =
              Math.max(0, dom * (Math.min(n, m) - 1) / discretization);
          double integrateTo =
              Math.min(dom, dom * (Math.max(n, m) + 1) / discretization);

          integral1 = this.integrator.integrate(
              Integer.MAX_VALUE,
              x
              -> dBasis_dx(discretization, dom, finalN, x) *
                     dBasis_dx(discretization, dom, finalM, x),
              integrateFrom, integrateTo);
        }

        double integral2 = 0;

        if (Math.abs(m - n) <= 1) {
          int finalN = n;
          int finalM = m;

          double integrateFrom =
              Math.max(0, dom * (Math.min(n, m) - 1) / discretization);
          double integrateTo =
              Math.min(dom, dom * (Math.max(n, m) + 1) / discretization);

          integral2 = this.integrator.integrate(
              Integer.MAX_VALUE,
              x
              -> basis(discretization, dom, finalN, x) *
                     basis(discretization, dom, finalM, x),
              integrateFrom, integrateTo);
        }
        bMatrix.setEntry(n, m,
                         basis(discretization, dom, n, 2) *
                                 basis(discretization, dom, m, 2) -
                             integral1 + integral2);
        ;
      }
    }

    // build L vector
    RealVector lVector = new ArrayRealVector(discretization, 0);
    for (int n = 0; n < discretization; n++) {
      double integral = 0;

      if (n > 0) {
        int finalN = n;

        double integrateFrom = 0;
        double integrateTo = 2;

        integral = this.integrator.integrate(
            Integer.MAX_VALUE,
            x
            -> - basis(discretization, dom, finalN, x) * Math.sin(x),
            integrateFrom, integrateTo);
      }
      lVector.setEntry(n, integral);
    }

    // calculate coefficients
    RealVector coefficients =
        new LUDecomposition(bMatrix).getSolver().solve(lVector);

    double[] result = Arrays.copyOf(coefficients.toArray(), discretization + 1);
    result[discretization] = 0;

    System.out.print("bMatrix: ");
    System.out.println(bMatrix);
    System.out.print("lVector: ");
    System.out.println(lVector);
    System.out.print("result: ");
    System.out.println(Arrays.toString(result));
    System.out.println("---------------------------");

    return new Solution(0, dom, result);
  }

  private static double E(double x) { return x <= 1.0 ? 3.0 : 5.0; }

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

  private static double dBasis_dx(int discretization, double dom, int i,
                                  double x) {
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
