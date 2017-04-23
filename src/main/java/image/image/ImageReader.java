package image.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class ImageReader {
	public static void main(String[] args) throws IOException {
		readImage("D:\\data\\data_x\\data_baby\\165_0.jpg");
	}

	public static double[][] readImage(String path) throws IOException {
		BufferedImage img = ImageIO.read(new File(path));
		int w = img.getWidth();
		int h = img.getHeight();
		int startX = 0;
		int startY = 0;
		int offset = 0;
		int scansize = w;
		int dd = w - startX;
		int hh = h - startY;
		int x0 = w / 2;
		int y0 = h / 2;
		int[] rgbArray = new int[offset + hh * scansize + dd];
		int[][] newArray = new int[h][w];
		img.getRGB(startX, startY, w, h, rgbArray, offset, scansize);
		Color c;
		double sum = 0;
		double sum2 = 0;
		for (int i = 0; i < h - startY; i++) {
			for (int j = 0; j < w - startX; j++) {
				c = new Color(rgbArray[i * dd + j]);
				// 彩色图像转换成无彩色的灰度图像Y=0.299*R + 0.578*G + 0.114*B
				newArray[i][j] = (int) (0.299 * c.getRed() + 0.578 * c.getGreen() + 0.114 * c.getBlue());
				sum = sum + newArray[i][j];
				sum2 = sum2 + newArray[i][j] * newArray[i][j];
			}
		}
		double mean = sum / (w * h);
		double S = Math.pow(sum2 / (w * h) - mean * mean + 10, 0.5);
		double result[][] = new double[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				result[i][j] = (newArray[i][j] - mean) / S;
			}
		}
		return result;
	}

}
