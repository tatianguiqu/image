package src.main.java.SiftAndBow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by QQQ on 2017/4/18.
 */
public class OutInterface {

	public ArrayList<String> getTopF(String picPath,String trainPath,String testPath,int K) throws IOException {

		ClusterPic cluster = new ClusterPic();
		cluster.setDictionary(trainPath,K);
		double[][] testDic = cluster.getDictionary();
		for (int i = 0;i<testDic.length;i++){
			for (int j=0;j<testDic[0].length;j++){
				System.out.print(testDic[i][j]);
			}
			System.out.println();
		}


		HashMap<String,double[]> result = cluster.getPicWord(testPath);

		for(Map.Entry<String,double[]> pic:result.entrySet()
				){
			System.out.println(pic.getKey()+":");
			double[] word = pic.getValue().clone();
			for (double d:word){
				System.out.print(d+";");
			}
			System.out.println();
		}

		HashMap<String,double[]> toFind = cluster.getPicWord(picPath);
		double[] picWord = toFind.get(picPath);
		double minest = 200000000;
		ArrayList<Double> minDis = new ArrayList<>();
		for (int i = 0;i<6;i++){
			minDis.add(i, (double) 200000000);
		}
		ArrayList<String> topFive = new ArrayList<String>();
		for(Map.Entry<String,double[]> pic:result.entrySet()
				){
			double temp = cluster.calDis(pic.getValue().clone(),picWord);
			if (temp< minest){
				minest = temp;
				topFive.add(0,pic.getKey());
				minDis.add(0,temp);
			}else{
				for(int i =0;i<5;i++){
					if(temp>minDis.get(i)&&temp<minDis.get(i+1)){
						minDis.add(i+1,temp);
						topFive.add(i+1,pic.getKey());
					}

				}
			}
		}

		for (int i=0;i<5;i++){
			System.out.println("Match "+i+":"+topFive.get(i));
		}

		return topFive;

	}

	public static void main(String[] args) throws IOException {
		OutInterface test = new OutInterface();
		ArrayList<String> tpf = test.getTopF("D:\\GraduationProject\\data_x\\TestBow\\159_7.jpg","D:\\GraduationProject\\data_x\\TestBow\\train","D:\\GraduationProject\\data_x\\TestBow\\test_1",30);

	}

}