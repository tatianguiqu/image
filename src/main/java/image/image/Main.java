package image.image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import org.nd4j.linalg.api.ndarray.INDArray;

import SiftAndBow.OutInterface;

public class Main {
	private static ArrayList<ImageKey> ikList;
	private static String CForTest="";
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String basePath = "D:\\data\\";
		OutInterface oi=new OutInterface();
		oi.trainATest("D:\\data\\oitrain", "D:\\data\\data_x\\data_Fdress", 30);
		AutoCodeModel.loadNetWork(basePath);

		DatabaseController.loadDatabase();
		ikList = DatabaseController.getImageKey("imagekeyT");
		DatabaseController.close();
		File testFile = new File(basePath + "test\\");
		String[] listOftest = testFile.list();
		double count=0.0;
		for (String testimage : listOftest) {
			CForTest="";
			String path = basePath + "test\\" + testimage;
			INDArray descriptor = AutoCodeModel.getDescriptor(AutoCodeModel.getImage1d(path, 100, 100, 1), 4);
			if(!searchImage(descriptor)){
				ArrayList<String> result=oi.getTopF(path);
			if(result.get(0).indexOf(testimage.split("_")[0])>-1){
				count++;
			}
			}
			System.out.println(testimage.split("_")[0]);
			if(testimage.split("_")[0].equals(CForTest)){
				count++;
			}
			System.out.println();
		}
    System.out.println(count/listOftest.length);
	}

	public static boolean searchImage(INDArray descriptor) {
		boolean result=true;
		ImageForSort[] listOfImage = new ImageForSort[ikList.size()];
		int number=0;
		for(ImageKey ik:ikList){
			ImageForSort ifs=new ImageForSort();
			ifs.setC(ik.getC());
			ifs.setPath(ik.getPath());
			ifs.setDistance(AutoCodeModel.getDistance(descriptor, ik.getImageKey()));
			listOfImage[number]=ifs;
			number++;
		}
		  Comparator<ImageForSort> myCom=new Comparator<ImageForSort>(){

				@Override
				public int compare(ImageForSort arg0, ImageForSort arg1) {
					// TODO Auto-generated method stub
					if(arg0.getDistance()==arg1.getDistance())
					return 0;
					else if(arg0.getDistance()>arg1.getDistance()){
						return 1;
					}
					else return -1;
				}
		    	 
		     };
		     Arrays.sort(listOfImage,myCom );
		result =judge(listOfImage);
		return result;
	}
	public static boolean judge(ImageForSort[] listOfImage){
		boolean result=true;
		ImageForSort top1=listOfImage[0];
		ImageForSort top2=listOfImage[1];
		ImageForSort top3=listOfImage[2];
		ImageForSort top4=listOfImage[3];
		ImageForSort top5=listOfImage[4];
		if(top1.getDistance()==0.0){
			CForTest=top1.getC();
		}else if((top1.getC().equals(top2.getC()))&&(top2.getC().equals(top3.getC()))&&(top3.getC().equals(top4.getC()))&&(top4.getC().equals(top5.getC()))){
			CForTest=top1.getC();
		}
		else{
			result=false;
		}
		return result;
	}
}
