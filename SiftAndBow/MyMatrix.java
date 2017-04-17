package SiftAndBow;

/**
 * Created by QQQ on 2017/4/16.
 */
public class MyMatrix {
	private double[][] data;
	private int rows;
	private int cols;

	public MyMatrix(double[][] data){
		this.data = data.clone();
		this.rows = data.length;
		this.cols = data[0].length;

	}

	public MyMatrix(int rows,int cols){
		this.data = new double[rows][cols];
		this.rows = rows;
		this.cols = cols;
	}



	public double[][] getData(){
		return this.data;
	}

	public double get(int i,int j){
		return data[i][j];
	}

	public void set(int i,int j,double num){
		data[i][j] = num;
	}

	public int rows(){
		return this.rows;

	}

	public int col(){

		return this.cols;

	}

	public double[] getRowSums(){
		double[] sums = new double[rows];
		for (int i=0;i<rows;i++){
			for (int j=0;j<cols;j++){
				sums[i]+= data[i][j];
			}
		}
		return sums;
	}

	public MyMatrix clone(){

		MyMatrix copyM = new MyMatrix(this.data);
		return copyM;


	}
//TODO
	public MyMatrix multiply(MyMatrix m){
		int rowN = rows;
		int colN = m.col();
		MyMatrix result = new MyMatrix(rows,m.col());
		for (int i=0;i<rowN;i++){
			for (int j=0;j<colN;j++){

			}
		}
		return null;

	}

	public double dArrayMultiply(double[] arr1,double[] arr2){

		double result = 0;
		for (int i=0;i<arr1.length;i++){
			result += arr1[i]*arr2[i];
		}
		return result;

	}

	public void setRows(int i,double[] row){

		this.data[i] = row.clone();

	}

	public double[] getRow(int i){
		return data[i];
	}

	public double[] getCol(int j){
		double[] col = new double[rows];
		for (int i=0;i<rows;i++
			 ) {

			col[i] = data[i][j];
		}
		return col;
	}

}
