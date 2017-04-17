package SiftAndBow;



import java.util.*;

/**
 * Created by QQQ on 2017/4/9.
 */
public class MyKmeans implements ClusterInterface {
//	K
	private int K = 0;
//记录数据属性
	private double[][] data ;
	private int rows;
	private int cols;


//记录数据对应的簇
	private HashMap<Integer, List<List<Double>>> map = new HashMap<>();

//聚类中心
	private double[][] centres;

	public MyKmeans(){

	}

	public MyKmeans(int K,double[][] data){
		if(data.length ==0)
			throw new java.lang.IllegalArgumentException("Data shouldn't be empty");
		if(data[0].length == 0){
			throw new java.lang.IllegalArgumentException("Data shouldn't be empty");
		}
		if(K<=0)
			throw new java.lang.IllegalArgumentException("Wrong input of K");

		this.data = data;

		rows = data.length;
		cols = data[0].length;
//
		centres = new double[K][cols];
		this.K = K;

	}

	private void iniData(double[][] data){
		if(data.length ==0)
			throw new java.lang.IllegalArgumentException("Data shouldn't be empty");
		if(data[0].length == 0){
			throw new java.lang.IllegalArgumentException("Data shouldn't be empty");
		}


		this.data = data;

		rows = data.length;
		cols = data[0].length;
//
		centres = new double[K][cols];
//		this.K = k;

	}
/**
 * @param K 聚成K类
 *
 */
	public void setK(int K){
		if(K<0)
			throw new java.lang.IllegalArgumentException("K should >0");
		this.K = K;
	}

	/**
	 * 计算欧式距离
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	private double calDis(double[] arr1,double[] arr2){
		double result = 0;
		for(int i=0;i<cols;i++){
			result += Math.pow((arr1[i]-arr2[i]),2);
		}

		return Math.sqrt(result);

	}

	/**
	 * 初始化聚类中心,采用随机中心的算法
	 */
	private void iniRanCentre(){
		Random ran = new Random();
//		int[] label = new int[K];
		String ifContain = "";

		for(int i = 0;i<K;i++){
			int temp = ran.nextInt(data.length);
//		N个不同随机数
				if(ifContain.contains(Integer.toString(temp))){
				i--;
			}else {
//				label[i] = ran.nextInt(data.length);
				centres[i] = data[temp].clone();
//				map.put(i,data[temp]);
				ifContain += temp+"";
			}

		}
	}

	/**
	 * 将数据聚类，返回属于的类
	 * @param arr
	 * @return
	 */
	private int runCluster(double[] arr){
		double min = this.calDis(arr,centres[0]);
		double temp;
		int label = 0;
		for(int i=1;i<K;i++){
			temp = this.calDis(arr,centres[i]);
			if(temp<min){
				min = temp;
				label = i;
			}
		}

//		System.out.println(this.printFormat(convertDArray(arr))+"?");

		if(!map.containsKey(label)){
			List<List<Double>> getL = new ArrayList<>();
			getL.add(convertDArray(arr));
			map.put(label,getL);
		}else{
			List<List<Double>> getL = map.get(label);
			getL.add(convertDArray(arr));
			map.put(label,getL);
		}


		return label;

	}

	/**
	 * 计算中心

	 */
	private void calMeans(){

		for(int m=0;m<K;m++) {
			int nums = map.get(m).size();
			double[] sum = new double[cols];
			for(int i=0;i<nums;i++){
				List<Double> temp = map.get(m).get(i);
				for(int j=0;j<temp.size();j++) {

					sum[j] += temp.get(j).doubleValue();
	//				double sum = centres[label][i] * nums;
	//			System.out.println(arr.get(i).doubleValue()+"?");
	//				sum += arr.get(i);
	//				double mean = sum / (nums + 1);
				}

			}

			for (int i=0;i<cols;i++) {
				centres[m][i] = sum[i]/nums;
			}
		}
	}

	/**
	 * 将double数组转换成List，便于map存取
	 * @param arry
	 * @return
	 */
	private List<Double> convertDArray(double[] arry){
		List<Double> list = new ArrayList<Double>();
		for(int i=0;i<arry.length;i++){
			list.add(arry[i]);
		}
		return list;
	}

	private double[] convertDTd(Double[] d){
		double[] result = new double[d.length];
		for(int i=0;i<d.length;i++){
			result[i] = d[i].doubleValue();
		}

		return result;
	}

//	计算属于集合i的元素到集合中心的总距离
	private double calTotalDis(){
		double result = 0;
		for(int i = 0;i<K;i++){

			int temp = map.get(i).size();
			for(int j=0;j<temp;j++){
				Double[] d = new Double[map.get(i).get(j).size()];
				map.get(i).get(j).toArray(d);

				result += calDis(this.convertDTd(d),centres[i]);
			}
		}
		return result;
	}

	/**
	 * 主要算法
	 * @param sdata 需要聚类的数据，
	 * @return 聚类后的中心
	 */
	@Override
	public double[][] cluster(double[][] sdata) {
		if(K<=0)
			throw new java.lang.IllegalArgumentException("Should Set(K) first");

		this.iniData(sdata);

		this.iniRanCentre();


		double old = -1;
		double newD = 1;


//若偏移量小于1，则不进行迭代
		while(Math.abs(newD-old)>=1) {


//			保留中心位置，清空记录，进行下次迭代
			map.clear();
//			double[][] tempCen = centres;

			int count = 0;
			for (double[] arr : this.data) {
//				System.out.println(printFormat(convertDArray(arr))+"????");
//				将数据聚类
				int label = this.runCluster(arr);
//				System.out.println(convertDArray(arr).get(0));
			}
//				重新计算中心
//				System.out.println("!");
			this.calMeans();
//			System.out.println(data.length+":"+count++);
//				System.out.println("?");
//			计算每次迭代之间欧式距离综合的偏移量
			old = newD;
			newD = this.calTotalDis();

//			for(int i=0;i<centres.length;i++){
//				for (int j=0;j<centres[0].length;j++){
//					System.out.print(centres[i][j]+";");
//				}
//				System.out.println();
//			}
			System.out.println("K-means:caltulate dis of iterator"+Math.abs(newD-old));

		}

//		for(int i=0;i<data.length;i++){
//			for(int j=0;j<data[0].length;j++){
//				System.out.print(data[i][j]+";");
//			}
//			System.out.println();
//		}

//		this.print();
		return this.centres;
	}

	private String printFormat(double[] input){

		String result = "";
		for (int i=0;i<input.length;i++){
			result += input[i]+"/";
		}
		return result;
	}

	private String printFormat(List<Double> input){

		String result = "";
		for (int i=0;i<input.size();i++){
			result += input.get(i)+"/";
		}
		return result;
	}

	private void print(){

		for(int i=0;i<K;i++){
			System.out.println(this.printFormat(centres[i])+":");
			if(map.isEmpty())
				break;
			for(int j=0;j<map.get(i).size();j++){
				System.out.println(this.printFormat(map.get(i).get(j)));
			}
			System.out.println();
		}


	}

	public static void main(String[] args){
		double[][] test = new double[][]{{0,0},{1,1},{20,20},{21,21},{70,70},{80,80}};
		MyKmeans testKm = new MyKmeans();
		testKm.setK(3);
		testKm.cluster(test);
//		for(int i=0;i<data.length;i++){
//			for(int j=0;j<data[0].length;j++){
//				System.out.print(data[i][j]+";");
//			}
//			System.out.println();
//		}

	}



}
