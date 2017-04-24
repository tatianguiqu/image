package SiftAndBow;

import data.DirServiceImp;
import data.WordServiceImp;
import dataService.Dirservice;
import dataService.WordService;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by QQQ on 2017/4/24.
 */
public class DataSaving {

	private ClusterPic cluster = new ClusterPic();
	public void saveDir(String dirPath,int K) throws IOException {
		cluster.setDictionary(dirPath,K);
		double[][] dictionary = cluster.getDictionary();

//		for (int i = 0;i<dictionary.length;i++){
//			for (int j=0;j<dictionary[0].length;j++){
//				System.out.print(dictionary[i][j]);
//			}
//			System.out.println();
//		}

		Dirservice ds = new DirServiceImp();
		ds.saveDirToSQL(dictionary);
//		for (int i = 0;i<testDic.length;i++){
//			for (int j=0;j<testDic[0].length;j++){
//				System.out.print(testDic[i][j]);
//			}
//			System.out.println();
//		}
	}

	public void saveWords(String picPath) throws IOException {
		HashMap<String,double[]> words = new HashMap<>();
		WordService ws = new WordServiceImp();
		File dir = new File(picPath);
		File[] fileList = dir.listFiles();

		int count = 0;
		for (int i=0;i<fileList.length;i++,count++){
			if (count<200){
				String key = fileList[i].getPath();
				double[] word = cluster.getPicWord(key).get(key);
				words.put(key,word);
			}else{

				ws.saveWordToSQL(words);
				words.clear();
				count =0;

			}


		}



//		words = cluster.getPicWord(picPath);
//		WordService ws = new WordServiceImp();

	}

	public static void main(String[] args) throws IOException {
		DataSaving dt = new DataSaving();
		dt.saveDir("D:\\GraduationProject\\data_x\\TestBow\\train",30);
		dt.saveWords("D:\\GraduationProject\\data_x\\TestBow\\test_1");

	}



}
