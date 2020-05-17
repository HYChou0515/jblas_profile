package io.hychou;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.jblas.DoubleMatrix;
import org.jblas.Eigen;
import org.jblas.Solve;

public class Others {

    public static void apacheCov(double[][] x) {
        double st = System.currentTimeMillis();
        RealMatrix X = new Array2DRowRealMatrix(x);
        RealMatrix C = new Covariance(X).getCovarianceMatrix();
        System.out.println( System.currentTimeMillis() - st);
//		System.out.println(C);
    }
    public static void jblasCov2(double[][] x) {
        double st = System.currentTimeMillis();
        DoubleMatrix X = new DoubleMatrix(x);
        DoubleMatrix oneX = DoubleMatrix.ones(X.rows).transpose().mmul(X);
        DoubleMatrix XX = oneX.transpose().mmul(oneX).mul(1.0/(X.rows-1)/X.rows);
        DoubleMatrix C = X.transpose().mmul(X).mul(1.0/(X.rows-1)).sub(XX);
        System.out.println( System.currentTimeMillis() - st);
//		System.out.println(C);
    }
    public static void jblasCov(double[][] x) {
        double st = System.currentTimeMillis();
        DoubleMatrix X = new DoubleMatrix(x);
        int n = X.columns;
        int l = X.rows;
        DoubleMatrix a = X.sub((DoubleMatrix.ones(l, l).mmul(X).mul(1.0/l)));
        DoubleMatrix C = a.transpose().mmul(a).mul(1.0/(l-1));
        System.out.println( System.currentTimeMillis() - st);
//		System.out.println(C);
    }
    public static void apacheSquare(double[][] x) {
        double st = System.currentTimeMillis();
        RealMatrix X = new Array2DRowRealMatrix(x);
        RealMatrix X2 = X.transpose().multiply(X);
        System.out.println( System.currentTimeMillis() - st);
//		System.out.println(X2);
    }
    public static void jblasSquare(double[][] x) {
        double st = System.currentTimeMillis();
        DoubleMatrix X = new DoubleMatrix(x);
        DoubleMatrix X2 = X.transpose().mmul(X);
        System.out.println( System.currentTimeMillis() - st);
//		System.out.println(X2);
    }
    public static void apacheInverse(double[][] x) {
        double st = System.currentTimeMillis();
        RealMatrix X = new Array2DRowRealMatrix(x);
        RealMatrix Xinv = new LUDecomposition(X).getSolver().getInverse();
        System.out.println( System.currentTimeMillis() - st);
        System.out.println(Xinv);
    }
    public static void jblasInverse2(double[][] x) {
        double st = System.currentTimeMillis();
        DoubleMatrix X = new DoubleMatrix(x);
        DoubleMatrix Xinv = new Solve().pinv(X);
        System.out.println( System.currentTimeMillis() - st);
        System.out.println(Xinv);
    }
    public static void jblasInverse(double[][] x) {
        double st = System.currentTimeMillis();
        DoubleMatrix X = new DoubleMatrix(x);
        DoubleMatrix[] E = Eigen.symmetricEigenvectors(X);
        DoubleMatrix ev = DoubleMatrix.zeros(X.columns, X.columns);
        for(int i=0; i<X.columns; i++)
            ev.put(i, i, 1.0/E[1].get(i, i));
        DoubleMatrix Xinv = E[0].mmul(ev).mmul(E[0].transpose());
        System.out.println( System.currentTimeMillis() - st);
        System.out.println(Xinv);
    }
}
