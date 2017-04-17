package image.image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import org.nd4j.linalg.api.ndarray.INDArray;

public class Main {
	private static ArrayList<ImageKey> ikList;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String basePath = "D:/data/";

		AutoCodeModel.loadNetWork(basePath);

		DatabaseController.loadDatabase();
		ikList = DatabaseController.getImageKey("imagekey");
		DatabaseController.close();
		File testFile = new File(basePath + "test/");
		String[] listOftest = testFile.list();
		for (String testimage : listOftest) {
			String path = basePath + "test/" + testimage;
			INDArray descriptor = AutoCodeModel.getDescriptor(AutoCodeModel.getImage1d(path, 100, 100, 1), 4);
			searchImage(descriptor);
		}

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
	for(int i=0;i<5;i++){
		System.out.println(listOfImage[i].getPath()+listOfImage[i].getC()+listOfImage[i].getDistance());
	}
	System.out.println();
		return result;
	}
}
