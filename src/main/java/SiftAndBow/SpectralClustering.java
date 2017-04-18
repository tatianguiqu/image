package SiftAndBow;


import Jama.Matrix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private MyMatrix EigMatrix;
	private MyMatrix NEigMatrix;
	private HashMap<Integer,List<Integer>> indexMap = new HashMap<>();

	private double[][] centers;


	public SpectralClustering(double[][] data,int k,double sigma){
		this.data = new MyMatrix(data.clone());
		this.K = k;
		this.sigma = sigma;
		this.Wmatrix = new MyMatrix(data.length,data.length);
		this.Dmatrix = new MyMatrix(data.length,data.length);
		this.Lmatrix = new MyMatrix(data.length,data.length);
		this.EigMatrix = new MyMatrix(data.length,K);
		this.NEigMatrix = new MyMatrix(data.length,K);
		this.centers = new double[K][data[0].length];

	}

	public void setData(double[][] data){
		this.data = new MyMatrix(data.clone());
		this.Wmatrix = new MyMatrix(data.length,data.length);
		this.Dmatrix = new MyMatrix(data.length,data.length);
		this.Lmatrix = new MyMatrix(data.length,data.length);
		this.centers = new double[K][data[0].length];
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

//	获取特征值
		double[] eigDd = new double[eigD.rows()];
		for (int i = 0;i<eigD.rows();i++){
			eigDd[i] = eigD.get(i,i);
		}

//	排序
		for (int i=0;i<K;i++){
			double tempM = eigDd[0];
			int index = 0;
			for (int j=0;j<eigDd.length;j++){
				if(eigDd[j]>tempM){
					tempM = eigDd[j];
					EigMatrix.setCols(i,eigV.getCol(j));
					index = j;
				}
			}
			eigDd[index] = eigDd[0]-1;
		}



	}

//	归一化X特征向量矩阵
	private void normalizeEig(){
		MyMatrix dmMatrix =EigMatrix.dotMultiply(EigMatrix);
		double[] colSum = dmMatrix.colSum();
		for (int i=0;i<EigMatrix.rows();i++){
			for (int j=0;j<EigMatrix.cols();j++){
				NEigMatrix.set(i,j,EigMatrix.get(i,j)/Math.sqrt(colSum[j]));
			}
		}


	}


	private HashMap<Integer,List<Integer>> getKmeans(){

		MyKmeans kmeans = new MyKmeans(K,NEigMatrix.getData());
		double[][] center = kmeans.cluster(NEigMatrix.getData());
		HashMap<Integer,List<Integer>> result = kmeans.getIndexMap();
		this.indexMap = result;
		return result;

	}

	private void culCenters(){

		for(int i=0;i<indexMap.size();i++){

			centers[i] = this.getCenters(indexMap.get(i));

		}

	}

	private double[] getCenters(List<Integer> list){
		double[] result = new double[data.cols()];
		double[] sum = new double[data.cols()];
		for (int i=0;i<data.cols();i++){
			for (int j =0;j<list.size();j++){
				sum[i] += data.get(list.get(j),i);

			}
		}

		for (int i = 0;i<data.cols();i++){
			result[i] = sum[i]/list.size();
		}

		return result;

	}


	@Override
	public double[][] cluster(double[][] data) {
		this.setData(data);
		this.culWmatrix();
		this.setDiagZero(Wmatrix);
		this.culDmatrix();
		this.culLmatrix();
		this.getKEIG();
		this.normalizeEig();
		this.getKmeans();
		this.culCenters();

		return this.centers;
	}
}
