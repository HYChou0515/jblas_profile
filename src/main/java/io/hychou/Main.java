package io.hychou;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.jblas.DoubleMatrix;
import org.jblas.Eigen;
import org.jblas.MatrixFunctions;
import org.jblas.Solve;

import java.util.Arrays;
import java.util.Random;

public class Main {
	public static double[] apacheOriginal(double[][] x) {
		double st = System.currentTimeMillis();
		RealMatrix X = new Array2DRowRealMatrix(x);
		RealMatrix C = new Covariance(X).getCovarianceMatrix();
		int l = X.getRowDimension();
		int n = X.getColumnDimension();
		RealMatrix ones_div_l = new Array2DRowRealMatrix(new double[l][1]);
		for(int i=0; i < l; i++)
			ones_div_l.setEntry(i, 0, 1.0/l);
		RealMatrix mu = X.transpose().multiply(ones_div_l);
		for(int i=0; i<n; i++)
			X.setColumnVector(i, X.getColumnVector(i).mapSubtract(mu.getEntry(i, 0)));
		C = new LUDecomposition(C).getSolver().getInverse();
		double[] diag = new double[l];
		RealMatrix ret = X.multiply(C).multiply(X.transpose());
		for(int i=0; i<l; i++)
			diag[i] = ret.getEntry(i, i);
		System.out.println( System.currentTimeMillis() - st);
		return diag;
	}
	public static double[] apacheDiag(double[][] x) {
		double st = System.currentTimeMillis();
		RealMatrix X = new Array2DRowRealMatrix(x);
		RealMatrix C = new Covariance(X).getCovarianceMatrix();
		int l = X.getRowDimension();
		int n = X.getColumnDimension();
		RealMatrix ones = new Array2DRowRealMatrix(new double[l][1]);
		for(int i=0; i < l; i++)
			ones.setEntry(i, 0, 1.0/l);
		RealMatrix mu = X.transpose().multiply(ones);
		for(int i=0; i<n; i++)
			X.setColumnVector(i, X.getColumnVector(i).mapSubtract(mu.getEntry(i, 0)));
		C = new LUDecomposition(C).getSolver().getInverse();
		double[] diag = new double[l];
		for(int i=0; i<l; i++)
			diag[i] = X.getRowMatrix(i).multiply(C).multiply(X.getRowMatrix(i).transpose()).getEntry(0, 0);
		System.out.println( System.currentTimeMillis() - st);
		return diag;
	}
	public static double[] jblasDiag(double[][] x) {
		double st = System.currentTimeMillis();
		DoubleMatrix X = new DoubleMatrix(x);
		int n = X.columns;
		int l = X.rows;
		DoubleMatrix oneX = DoubleMatrix.ones(X.rows).transpose().mmul(X); // M: 1*n
		DoubleMatrix Xmu =  X.subRowVector(oneX.div(X.rows));// M: l*n
		DoubleMatrix mu = oneX.transpose().mmul(oneX).mul(1.0/X.rows); // M: n*n
		DoubleMatrix C = X.transpose().mmul(X).sub(mu).mul(1.0/(X.rows-1));  // M: n*n
		C = Solve.solvePositive(C, DoubleMatrix.eye(n));
		double[] diag = new double[l];
		for(int i=0; i < l; i++)
			diag[i] = Xmu.getRow(i).mmul(C).dot(Xmu.getRow(i));
		System.out.println( System.currentTimeMillis() - st);
		return diag;
	}
	public static double[] jblasEigen(double[][] x) {
		double st = System.currentTimeMillis();
		DoubleMatrix X = new DoubleMatrix(x);
		int n = X.columns;
		int l = X.rows;
		DoubleMatrix oneX = DoubleMatrix.ones(X.rows).transpose().mmul(X); // M: 1*n
		DoubleMatrix mu = oneX.transpose().mmul(oneX).mul(1.0/X.rows); // M: n*n
		DoubleMatrix C = X.transpose().mmul(X).sub(mu).mul(1.0/(X.rows-1));  // M: n*n
		DoubleMatrix[] E = Eigen.symmetricEigenvectors(C);
		DoubleMatrix XE = X.subRowVector(oneX.div(X.rows)).mmul(E[0]);
		DoubleMatrix L = new DoubleMatrix(n);
		for(int i=0; i<n; i++)
			L.put(i, 1.0/E[1].get(i, i));
		double[] diag = new double[l];
		for(int i=0; i < l; i++)
			diag[i] = MatrixFunctions.pow(XE.getRow(i), 2).dot(L);
		System.out.println( System.currentTimeMillis() - st);
		return diag;
	}
	public static void profiling(int l, int n, int algo, boolean printing){
		profiling(l, n, algo, printing, 0);
	}
	public static void profiling(int l, int n, int algo, boolean printing, int seed){
		double[][] x = new double[l][n];
		Random r = new Random(seed);
		for(int i=0; i < l; i++)
			for(int j=0; j < n; j++)
				x[i][j] = r.nextDouble();
		double[] diag;
		try {
			if (algo == 0)
				diag = apacheOriginal(x);
			else if (algo == 1)
				diag = apacheDiag(x);
			else if (algo == 2)
				diag = jblasDiag(x);
			else if (algo == 3)
				diag = jblasEigen(x);
			else
				return;
			if(printing)
				System.out.println(Arrays.toString(diag).replace("], ", "]\n"));
		} catch (Exception e) {
			System.out.println("nan");
		}
	}
	public static void main(String[] args) {
		int l = Integer.parseInt(args[0]);
		int n = Integer.parseInt(args[1]);
		int algo = Integer.parseInt(args[2]);
		boolean printing;
		if(args.length < 4)
			printing = false;
		else
			printing = Integer.parseInt(args[3]) == 1;
		String algoStr;
		if(algo == 0)
			algoStr = "apache-original";
		else if(algo == 1)
			algoStr = "apache-diag";
		else if(algo == 2)
			algoStr = "jblas-diag";
		else if(algo == 3)
			algoStr = "jblas-eigen";
		else
			return;
		System.out.print(String.format("%d %d %s ", l, n, algoStr));
		profiling(l, n, algo, printing);
	}
}
