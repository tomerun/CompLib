import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ImageExamine {
	public static void main(String[] args) throws Exception {
		String seed = args[0];
		String fileName = "testdata/300px/" + seed + ".png";
		BufferedImage src = ImageIO.read(new File(fileName));
		int W = src.getWidth();
		int H = src.getHeight();
		int[] raster = src.getData().getPixels(0, 0, W, H, new int[H * W]);
		double[][] pixels = new double[H][W];
		for (int i = 0; i < H; ++i) {
			for (int j = 0; j < W; ++j) {
				pixels[i][j] = raster[i * W + j];
			}
		}
		int DEG = 100;
		double[][] coef = new double[DEG][DEG];
		for (int i = 0; i <= DEG * 2; ++i) {
			for (int r = 0; r <= Math.min(i, DEG - 1); ++r) {
				int c = i - r;
				if (c >= DEG) continue;
				double bh = 1. * (r + 1) / H;
				double bw = 1. * (c + 1) / W;
				//				double bh = r * Math.PI / H;
				//				double bw = c * Math.PI / W;
				double[] vh = new double[H];
				double[] vw = new double[W];
				for (int j = 0; j < H; ++j) {
					vh[j] = (int) ((j + 0.5) * bh) % 2 == 0 ? 1 : -1;
					//					vh[j] = bh == 0 ? 1 : Math.cos((j + 0.5) * bh);
				}
				for (int j = 0; j < W; ++j) {
					vw[j] = (int) ((j + 0.5) * bw) % 2 == 0 ? 1 : -1;
					//					vw[j] = bw == 0 ? 1 : Math.cos((j + 0.5) * bw);
				}
				double sum = 0;
				for (int y = 0; y < H; ++y) {
					for (int x = 0; x < W; ++x) {
						sum += pixels[y][x] * vh[y] * vw[x];
					}
				}
				coef[r][c] = sum / (H * W);
				for (int y = 0; y < H; ++y) {
					for (int x = 0; x < W; ++x) {
						pixels[y][x] -= coef[r][c] * vh[y] * vw[x];
					}
				}
			}
		}
//		for (int i = 0; i < DEG; ++i) {
//			for (int j = 0; j < DEG; ++j) {
//				System.out.printf("%.5f ", coef[i][j]);
//			}
//			System.out.println();
//		}

		double[][] dstP = new double[H][W];
		for (int r = 0; r < DEG; ++r) {
			for (int c = 0; c < DEG; ++c) {
				double bh = 1. * (r + 1) / H;
				double bw = 1. * (c + 1) / W;
				//				double bh = r * Math.PI / H;
				//				double bw = c * Math.PI / W;
				double[] vh = new double[H];
				double[] vw = new double[W];
				for (int j = 0; j < H; ++j) {
					vh[j] = (int) ((j + 0.5) * bh) % 2 == 0 ? 1 : -1;
					//					vh[j] = bh == 0 ? 1 : Math.cos((j + 0.5) * bh);
				}
				for (int j = 0; j < W; ++j) {
					vw[j] = (int) ((j + 0.5) * bw) % 2 == 0 ? 1 : -1;
					//					vw[j] = bw == 0 ? 1 : Math.cos((j + 0.5) * bw);
				}
				for (int y = 0; y < H; ++y) {
					for (int x = 0; x < W; ++x) {
						dstP[y][x] += coef[r][c] * vh[y] * vw[x];
					}
				}
			}
		}

		BufferedImage dst = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < H; ++i) {
			for (int j = 0; j < W; ++j) {
				int p = (int) (dstP[i][j] + 0.5);
				if (p < 0) p = 0;
				if (p > 255) p = 255;
				p |= (p << 8) | (p << 16);
				dst.setRGB(j, i, p);
			}
		}
		ImageIO.write(dst, "png", new File(seed + "_out.png"));
	}
}
