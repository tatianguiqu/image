package SiftAndBow;  /**
 * Created by QQQ on 2017/4/10.
 */

/**
 * 词袋模型训练器，输入所有图片的特征点组成的二维矩阵（N*128），利用KMEANS进行聚类，返回码本
 */
public class BowTrainer {
	private int K = 0;
	private double[][] data;
	private double[][] dictionary;

	public BowTrainer(int K,double[][] data){
		this.K = K;
		this.data = data;
	}

	public double[][] getDictionary(){
		if(data.length ==0)
			throw new java.lang.IllegalArgumentException("Data shouldn't be empty");
		if(data[0].length == 0)
			throw new java.lang.IllegalArgumentException("Data shouldn't be empty");
		if(K<=0)
			throw new java.lang.IllegalArgumentException("Wrong input of K");

		MyKmeans kmeans = new MyKmeans();
		kmeans.setK(K);
		dictionary = kmeans.cluster(data);
		return dictionary;


	}


}
