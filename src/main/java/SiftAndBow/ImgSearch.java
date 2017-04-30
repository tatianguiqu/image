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

		for (int i=0;i<5;i++){
			System.out.println("Match "+i+":"+topFive.get(i));

		}

		return topFive;

	}


	public static void main(String[] args) throws IOException {
		ImgSearch test = new ImgSearch();
		HashMap<String,double[]> result = test.getImgMap();
		test.getDic();
		test.setDic();
		test.getTopF("D:\\data\\data_x\\data_Fdress\\130_37.jpg");
		System.out.println(result.size());
	}


}
