package SiftAndBow;


import Jama.Matrix;

import java.util.HashMap;

/**
 * Created by QQQ on 2017/4/16.
 */
public class SpectralClustering implements ClusterInterface {
	private MyMatrix data;
	private int K;
	private double sigma = 1;
	private MyMatrix Wmatrix;
	private MyMatrix Dmatrix;
	private MyMatrix Lmatrix;
//	前K个特征向量组成的特征矩阵
	private Matrix EigMatrix;

	public SpectralClustering(double[][] data,int k,double sigma){
		this.data = new MyMatrix(data.clone());
		this.K = k;
		this.sigma = sigma;
		this.Wmatrix = new MyMatrix(data.length,data.length);
		this.Dmatrix = new MyMatrix(data.length,data.length);
		this.Lmatrix = new MyMatrix(data.length,data.length);
		this.EigMatrix = new Matrix(data.length,K);
	}

	public void setData(double[][] data){
		this.data = new MyMatrix(data.clone());
		this.Wmatrix = new MyMatrix(data.length,data.length);
		this.Dmatrix = new MyMatrix(data.length,data.length);
		this.Lmatrix = new MyMatrix(data.length,data.length);
	}

	private double culSimilarity(double[] arr1,double[] arr2){
		double result = 0;
		for(int i=0;i<arr1.length;i++){
			result += Math.pow((arr1[i]-arr2[i]),2);
		}
		result = Math.exp(-(result)/(2*sigma));
		return result;
	}


	private void culWmatrix(){
		for (int i =0;i<data.rows();i++){
			for(int j=0;j<data.rows();j++){
//TODO
// 	Wmatrix.set(i,j);
				double similar = this.culSimilarity(data.getRow(i),data.getRow(j));
				Wmatrix.set(i,j,similar);
				Wmatrix.set(j,i,similar);

			}

		}


	}

	private void culDmatrix(){

		for (int i=0;i<data.rows();i++){
			Dmatrix.set(i,i,Wmatrix.getRowSums()[i]);
		}

	}

	private void setDiagZero(MyMatrix m){

		for (int i=0;i<m.rows();i++){
			m.set(i,i,0);
		}

	}
//TODO
	private void culLmatrix(){

		MyMatrix sqrtDM = this.Dmatrix.clone();
		for (int i=0;i<Dmatrix.rows();i++){
			sqrtDM.set(i,i,Math.sqrt(Dmatrix.get(i,i)));
		}

		Lmatrix = (MyMatrix) sqrtDM.times(Wmatrix);
		Lmatrix = (MyMatrix) Lmatrix.times(sqrtDM);

	}

	private void getKEIG(){
//		特征值对角阵
		MyMatrix eigD = (MyMatrix) Lmatrix.eig().getD();
//		特征向量矩阵，按列排序
		MyMatrix eigV = (MyMatrix) Lmatrix.eig().getV();




	}

	@Override
	public double[][] cluster(double[][] data) {
		this.setData(data);

		return new double[0][];
	}
}
