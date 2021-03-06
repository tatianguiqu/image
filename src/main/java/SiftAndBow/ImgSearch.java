package SiftAndBow;

import data.DirServiceImp;
import data.WordServiceImp;
import dataService.Dirservice;
import dataService.WordService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by QQQ on 2017/4/28.
 */
public class ImgSearch {
	HashMap<String,double[]> allImg = new HashMap<>();
	ClusterPic cluster = new ClusterPic();
	double[][] dic ;


	private HashMap<String,double[]> getImgMap(){
		WordService ws = new WordServiceImp();
		allImg = ws.getWords();
		return  allImg;
	}

	public double[][] getDic(){
		Dirservice ds = new DirServiceImp();
		dic = ds.getDir();
		return dic;
	}

	public void setDic(){
		cluster.setDictionary(dic);
	}

	public ArrayList<String> getTopFByWords(double[] word){

//		System.out.println(picPath+" word:");
//		for (int i=0;i<picWord.length;i++){
//
//			System.out.print(picWord[i]+";");
//
//		}
//		System.out.println();

		double minest = 200000000;
		ArrayList<Double> minDis = new ArrayList<>();
		for (int i = 0;i<6;i++){
			minDis.add(i, (double) 200000000);
		}
		ArrayList<String> topFive = new ArrayList<String>();
		for(Map.Entry<String,double[]> pic:allImg.entrySet()
				){
			double temp = cluster.calDis(pic.getValue().clone(),word);

//			if (pic.getKey().equals("D:\\GraduationProject\\data_x\\TestBow\\test_1\\13_47.jpg")){
//				System.out.println(temp);
//			}

			if (temp<= minest){
				minest = temp;
//				System.out.println(temp+":"+pic.getKey());
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

//		for (int i=0;i<35;i++){
//			System.out.println("Match "+i+":"+topFive.get(i)+"Dis:"+minDis.get(i));
//
//		}

		return topFive;

	}

	public ArrayList<String> getTopF(String picPath) throws IOException {

		HashMap<String,double[]> toFind = cluster.getPicWord(picPath);
		double[] picWord = toFind.get(picPath);

		System.out.println(picPath+" word:");
		for (int i=0;i<picWord.length;i++){

			System.out.print(picWord[i]+";");

		}
		System.out.println();

		double minest = 200000000;
		ArrayList<Double> minDis = new ArrayList<>();
		for (int i = 0;i<6;i++){
			minDis.add(i, (double) 200000000);
		}
		ArrayList<String> topFive = new ArrayList<String>();
		for(Map.Entry<String,double[]> pic:allImg.entrySet()
				){
			double temp = cluster.calDis(pic.getValue().clone(),picWord);

//			if (pic.getKey().equals("D:\\GraduationProject\\data_x\\TestBow\\test_1\\13_47.jpg")){
//				System.out.println(temp);
//			}

			if (temp<= minest){
				minest = temp;
//				System.out.println(temp+":"+pic.getKey());
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

		for (int i=0;i<20;i++){
			System.out.println("Match "+i+":"+topFive.get(i));

		}

		return topFive;

	}


	public static void main(String[] args) throws IOException {
		ImgSearch test = new ImgSearch();
		HashMap<String,double[]> result = test.getImgMap();
		test.getDic();
		test.setDic();
		double[] input=test.cluster.getPicWord("D:\\data\\data_x\\data_Fdress\\130_37.jpg").get("D:\\data\\data_x\\data_Fdress\\130_37.jpg");
//		 System.out.println("本次测试图片1万张");
//		 System.out.println("测试查询1张图片");
//		 System.out.println("重复查询100万次");
//		 System.out.println("花费查询时间");
		long startP = System.nanoTime();
		for(int i=0;i<1000000;i++){
			ArrayList<String>l =test.getTopFByWords(input);
			
			
		}
		long endP = System.nanoTime();
		double msP = (endP - startP) / 1000000d;
		System.out.println(msP+" ms");
		
	
	}


}
