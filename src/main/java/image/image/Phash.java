package image.image;

import java.io.File;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import data.HashCodeServiceImp;
import dataService.HashCodeService;

public class Phash {
	static long m1 = 0x5555555555555555l;
	static long m2 = 0x3333333333333333l;
	static long m4 = 0x0f0f0f0f0f0f0f0fl;
	static long m8 = 0x00ff00ff00ff00ffl;
	static long m16 = 0x0000ffff0000ffffl;
	static long m32 = 0x00000000ffffffffl;
	static long[] mask = { 0x5555555555555555l, 0x3333333333333333l, 0x0f0f0f0f0f0f0f0fl, 0x00ff00ff00ff00ffl,
			0x0000ffff0000ffffl, 0x00000000ffffffffl };

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashCodeService hcs = new HashCodeServiceImp();
		String basePath = "D:\\data\\data_x\\data_shoes";
		File folder = new File(basePath);
		String[] listOfImageName = folder.list();
		ArrayList<ImageHash> hashCodeList = new ArrayList<ImageHash>(listOfImageName.length);
		for (String imageFileName : listOfImageName) {
			ImageHash ih = new ImageHash();
			String pathOfImageFile = basePath + "\\" + imageFileName;
			ih.setPath(pathOfImageFile);
			ih.setC("shoes"+imageFileName.split("_")[0]);
			ih.setHash(pHash(pathOfImageFile));
			hashCodeList.add(ih);
		}
		hcs.saveHashCodeToSQL(hashCodeList);
	}

	public static long pHash(String path) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat image = Highgui.imread(path);
		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
		Mat newImage = new Mat();
		Size size = new Size(8, 8);
		double fx = (double) size.width / image.cols();
		double fy = (double) size.height / image.rows();
		Imgproc.resize(image, newImage, size, fx, fy, Imgproc.INTER_AREA);
		double mean = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				mean = mean + newImage.get(i, j)[0];
			}
		}
		mean = mean / 64;
		long l = 0;
		long x = 1;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (newImage.get(i, j)[0] >= mean) {
					l |= x << (63 - i * 8 - j);
				}
			}
		}
		return l;
	}

	public static long pHashl(String path) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat image = Highgui.imread(path);
		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
		Mat newImage = new Mat();
		Mat dst = new Mat();
		Size size = new Size(32, 32);
		double fx = (double) size.width / image.cols();
		double fy = (double) size.height / image.rows();
		Imgproc.resize(image, newImage, size, fx, fy, Imgproc.INTER_AREA);
		// Imgproc.resize(image, newImage, size);
		newImage.convertTo(newImage, CvType.CV_32FC1);
		Core.dct(newImage, dst);
		double mean = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				mean = mean + dst.get(i, j)[0];
			}
		}
		mean = mean / 64;
		long l = 0;
		long x = 1;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (dst.get(i, j)[0] >= mean) {
					l |= x << (63 - i * 8 - j);
				}
			}
		}
		return l;
	}

	public static int getWeight(long input) {
		input = (input & m1) + ((input >>> 1) & m1);
		input = (input & m2) + ((input >>> 2) & m2);
		input = (input & m4) + ((input >>> 4) & m4);
		input = (input & m8) + ((input >>> 8) & m8);
		input = (input & m16) + ((input >>> 16) & m16);
		input = (input & m32) + ((input >>> 32) & m32);
//		for (int i = 0; i < 6; i++) {
//			input = (input & mask[i]) + ((input >>> (int) Math.pow(2, i)) & mask[i]);
//		}
		return (int) input;

	}
}
