package image.image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import org.nd4j.linalg.api.ndarray.INDArray;

import SiftAndBow.ClusterPic;
import SiftAndBow.DataSaving;
import SiftAndBow.ImgSearch;
import SiftAndBow.OutInterface;
import data.DatabaseController;
import data.DirServiceImp;
import data.HashCodeServiceImp;
import data.WordServiceImp;
import dataService.Dirservice;
import dataService.HashCodeService;
import dataService.WordService;

public class MainSearch {
	private static ArrayList<ImageKey> ikList;
	private static String CForTest = "";
	private static HashMap<Long, ArrayList<ImageHash>> pHash1 = new HashMap<Long, ArrayList<ImageHash>>();
	private static HashMap<Long, ArrayList<ImageHash>> pHash2 = new HashMap<Long, ArrayList<ImageHash>>();
	private static HashMap<Long, ArrayList<ImageHash>> pHash3 = new HashMap<Long, ArrayList<ImageHash>>();
	private static HashMap<Long, ArrayList<ImageHash>> pHash4 = new HashMap<Long, ArrayList<ImageHash>>();
	private static HashMap<String,ArrayList<String>> pHash=new HashMap<String,ArrayList<String>>();
	private static ArrayList<ImageHash> imageHashList;
	private static KDTree kt;
	private static double[][] dic;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		// DatabaseController dc=new DatabaseController();
		// dc.setConn();
		// ikList = dc.getImageKey("imagekey");
		// dc.close();
		// System.out.println(1);
		// KDTree kt=KDTree.build(ikList);
		// System.out.println(1);
		// ImageKey ik=ikList.get(111);
		// System.out.println(ik.getPath());
		// System.out.println(kt.queryP(ik.getImageKey(),10));
		//

//		 DataSaving dt = new DataSaving();
//		 dt.saveDir("D:\\data\\oitrain",50);
//		 long startP = System.nanoTime();
//		 dt.saveWords("D:\\data\\data_x\\data_baby");
//		 dt.saveWords("D:\\data\\data_x\\data_Beauty");
//		 dt.saveWords("D:\\data\\data_x\\data_Fdress");
//		 dt.saveWords("D:\\data\\data_x\\data_Mdress");
//		 dt.saveWords("D:\\data\\data_x\\data_package");
//		 dt.saveWords("D:\\data\\data_x\\data_shoes");
//		 long endP = System.nanoTime();
//		 double msP = (endP - startP) / 1000000d;
//		 System.out.printf("Saving all picture words cost %,.3f ms%n",msP);
		
		loadSift();
		loadKDTree();
		double[] input = getSift("D:\\data\\data_x\\data_Fdress\\130_37.jpg");	
		kt.setCount(200);
//		 System.out.println("本次测试图片1065张");
//		 System.out.println("测试查询1张图片");
//		 System.out.println("重复查询100万次");
//		 System.out.println("花费查询时间");
		System.out.println("相似度最高Top10：");
		 long startP = System.nanoTime();
		 for(int i=0;i<1;i++){
			 ArrayList<String> result = kt.queryP(input,10);
			 for(int j=0;j<result.size();j++){
				 System.out.println(result.get(result.size()-1-j));
			 }
		 }
		 long endP = System.nanoTime();
		 double msP = (endP - startP) / 1000000d;
		// System.out.println(msP+" ms");
		
//		 int c=0;
//		 for(int i=0;i<1;i++){
//			 ArrayList<String> result = kt.queryP(input,10);
//			for(int j=0;j<result.size();j++){
//				if(result.get(j).indexOf("140_")>0){
//					c++;
//					System.out.println(result.get(j));
//				}
//			}
//			System.out.println(result);
//			System.out.println(c);
//		 }
		
		
		

//		 loadHash();
//		 String basePath = "D:\\data\\data_x\\data_shoes";
//			File folder = new File(basePath);
//			int n1=0;
//			int n2=0;
//			String[] listOfImageName = folder.list();
//			for (String imageFileName : listOfImageName) {
//				String pathOfImageFile = basePath + "\\" + imageFileName;
//				String c="shoes"+imageFileName.split("_")[0];
//				long hashCode=Phash.pHashl(pathOfImageFile);
//				String c2=fastHash1(hashCode);
//				n1++;
//				if(c2.equals(c)){
//					n2++;
//				}else{
//					//System.out.println(imageFileName+"  "+c2);
//				}
//			}
//			System.out.println(n1+" "+n2);
			
			
//		loadHash();
//		 long hashCode=Phash.pHashl("D:\\data\\data_x\\data_Fdress\\130_37.jpg");
//		 System.out.println("本次测试图片10384张");
//		 System.out.println("测试查询1张图片");
//		 System.out.println("重复查询100万次");
//		 System.out.println("花费查询时间");
//		 long startP = System.nanoTime();
//		 for(int i=0;i<1000000;i++){
//			fastHash(hashCode);
//		 }
//		 long endP = System.nanoTime();
//		 double msP = (endP - startP) / 1000000d;
//		 System.out.println(msP+" ms");
//		 System.out.println();
//		 System.out.println();
//		 System.out.println();
	}
public static ArrayList<String> search(String path){
	long hashCode=Phash.pHashl(path);
	String c=fastHash1(hashCode);

	if(c!=null){
		return pHash.get(c);
		
	}else{
		double[] input=getSift(path);
		return kt.queryP(input, 10);
	}
}
	public static void loadSift() {

		Dirservice ds = new DirServiceImp();
		dic = ds.getDir();
		WordService ws = new WordServiceImp();
		ikList = ws.getWordsArray();

	}

	public static void loadKDTree() {
		kt = KDTree.build(ikList);
	}

	public static double[] getSift(String path) {
		ClusterPic cp = new ClusterPic();
		cp.setDictionary(dic);
		try {
			return cp.getPicWord(path).get(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void loadHash() {
		HashCodeService hcs = new HashCodeServiceImp();
		imageHashList = hcs.getHashCode();
		for (int i = 0; i < imageHashList.size(); i++) {
			ImageHash ih = imageHashList.get(i);
			long h1 = ih.getHash() & 0xffff000000000000l;
			long h2 = ih.getHash() & 0x0000ffff00000000l;
			long h3 = ih.getHash() & 0x00000000ffff0000l;
			long h4 = ih.getHash() & 0x000000000000ffffl;
			if(pHash.get(ih.getC())!=null){
				ArrayList<String> pathList=pHash.get(ih.getC());
				pathList.add(ih.getPath());
				pHash.put(ih.getC(), pathList);
			}else{
				ArrayList<String> pathList=new ArrayList<String>();
				pathList.add(ih.getPath());
				pHash.put(ih.getC(), pathList);
			}
			
			loadOne(h1, pHash1, ih);
			loadOne(h2, pHash2, ih);
			loadOne(h3, pHash3, ih);
			loadOne(h4, pHash4, ih);
		}
	}

	public static String fastHash(long hashCode) {
		HashMap<String, ImageForSort> ifsHash = new HashMap<String, ImageForSort>();
		long h1 = hashCode & 0xffff000000000000l;
		long h2 = hashCode & 0x0000ffff00000000l;
		long h3 = hashCode & 0x00000000ffff0000l;
		long h4 = hashCode & 0x000000000000ffffl;
		if (pHash1.get(h1) != null) {
			ArrayList<ImageHash> ihl = pHash1.get(h1);
			makeSort(ihl, hashCode, ifsHash);
		}
		if (pHash2.get(h2) != null) {
			ArrayList<ImageHash> ihl = pHash2.get(h2);
			makeSort(ihl, hashCode, ifsHash);
		}
		if (pHash3.get(h3) != null) {
			ArrayList<ImageHash> ihl = pHash3.get(h3);
			makeSort(ihl, hashCode, ifsHash);
		}
		if (pHash4.get(h4) != null) {
			ArrayList<ImageHash> ihl = pHash4.get(h4);
			makeSort(ihl, hashCode, ifsHash);
		}
		Iterator<?> iter = ifsHash.entrySet().iterator();
		double distance=4;
		String tmp="";
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			ImageForSort ifs=(ImageForSort) entry.getValue();
			if(ifs.getDistance()<distance){
				tmp=ifs.getC();
				distance=ifs.getDistance();
			}
		}
		return tmp;
		
	}
	public static String fastHash1(long hashCode) {
		String result=null;
		long h1 = hashCode & 0xffff000000000000l;
		long h2 = hashCode & 0x0000ffff00000000l;
		long h3 = hashCode & 0x00000000ffff0000l;
		long h4 = hashCode & 0x000000000000ffffl;
		if((result=find(pHash1,h1,hashCode))!=null){
			return result;
		}
		if((result=find(pHash2,h2,hashCode))!=null){
			return result;
		}
		if((result=find(pHash3,h3,hashCode))!=null){
			return result;
		}
		if((result=find(pHash4,h4,hashCode))!=null){
			return result;
		}
		return result;
	}
	public static String find(HashMap<Long, ArrayList<ImageHash>> pHash,long h,long hashCode){
		String result =null;
		if (pHash.get(h) != null) {
			ArrayList<ImageHash> ihl = pHash.get(h);
			for(int i=0;i<ihl.size();i++){
				int distance=Phash.getWeight(ihl.get(i).getHash()^hashCode);
				if(distance<=3){
					result=ihl.get(i).getC();
				}
			}
		}
		return result;
	}
	public static void makeSort(ArrayList<ImageHash> ihl, long hashCode, HashMap<String, ImageForSort> ifsHash) {
		for (int i = 0; i < ihl.size(); i++) {
			ImageHash ih = ihl.get(i);
			ImageForSort ims = new ImageForSort();
			ims.setC(ih.getC());
			ims.setPath(ih.getPath());
			ims.setDistance(Phash.getWeight(hashCode ^ ih.getHash()));
			ifsHash.put(ims.getPath(), ims);
		}
	}

	public static void loadOne(long h, HashMap<Long, ArrayList<ImageHash>> pHash, ImageHash ih) {
		if (pHash.get(h) == null) {
			ArrayList<ImageHash> ihl = new ArrayList<ImageHash>();
			ihl.add(ih);
			pHash.put(h, ihl);
		} else {
			ArrayList<ImageHash> ihl = pHash.get(h);
			ihl.add(ih);
			pHash.put(h, ihl);
		}
	}

	public static boolean searchImage(INDArray descriptor) {
		boolean result = true;
		ImageForSort[] listOfImage = new ImageForSort[ikList.size()];
		int number = 0;
		for (ImageKey ik : ikList) {
			ImageForSort ifs = new ImageForSort();
			ifs.setC(ik.getC());
			ifs.setPath(ik.getPath());
			ifs.setDistance(AutoCodeModel.getDistance(descriptor, ik.getImageKey()));
			listOfImage[number] = ifs;
			number++;
		}
		Comparator<ImageForSort> myCom = new Comparator<ImageForSort>() {

			@Override
			public int compare(ImageForSort arg0, ImageForSort arg1) {
				// TODO Auto-generated method stub
				if (arg0.getDistance() == arg1.getDistance())
					return 0;
				else if (arg0.getDistance() > arg1.getDistance()) {
					return 1;
				} else
					return -1;
			}

		};
		Arrays.sort(listOfImage, myCom);
		result = judge(listOfImage);
		return result;
	}

	public static boolean judge(ImageForSort[] listOfImage) {
		boolean result = true;
		ImageForSort top1 = listOfImage[0];
		ImageForSort top2 = listOfImage[1];
		ImageForSort top3 = listOfImage[2];
		ImageForSort top4 = listOfImage[3];
		ImageForSort top5 = listOfImage[4];
		if (top1.getDistance() == 0.0) {
			CForTest = top1.getC();
		} else if ((top1.getC().equals(top2.getC())) && (top2.getC().equals(top3.getC()))
				&& (top3.getC().equals(top4.getC())) && (top4.getC().equals(top5.getC()))) {
			CForTest = top1.getC();
		} else {
			result = false;
		}
		return result;
	}
}
