import java.util.Random;

public class FFT {

	static int N = 50000;

	static void fftLoop(Complex[] a, boolean forward) {
		int N = a.length;
		int[] ord = new int[N];
		for (int i = 2; i <= N; i *= 2) {
			for (int j = 0; j < i / 2; ++j) {
				ord[j] *= 2;
				ord[i / 2 + j] = ord[j] + 1;
			}
		}
		Complex[] tmp = a.clone();
		for (int i = 0; i < N; ++i) {
			a[i] = tmp[ord[i]];
		}
		for (int len = 2; len <= N; len *= 2) {
			double theta = Math.PI * 2 / len;
			if (!forward) theta *= -1;
			for (int i = 0; i < len / 2; ++i) {
				Complex w = new Complex(Math.cos(theta * i), Math.sin(theta * i));
				for (int j = 0; j < N; j += len) {
					Complex c1 = a[j + i];
					Complex c2 = a[j + i + len / 2].mul(w);
					double re1 = c1.re;
					double im1 = c1.im;
					double re2 = c2.re;
					double im2 = c2.im;
					a[j + i].re = re1 + re2;
					a[j + i].im = im1 + im2;
					a[j + i + len / 2].re = re1 - re2;
					a[j + i + len / 2].im = im1 - im2;
				}
			}
		}
	}

	static void fftRec(Complex[] a, boolean forward) {
		if (a.length == 1) return;
		Complex[] b = new Complex[a.length / 2];
		Complex[] c = new Complex[a.length / 2];
		for (int i = 0; i < a.length / 2; ++i) {
			b[i] = a[i * 2];
			c[i] = a[i * 2 + 1];
		}
		fftRec(b, forward);
		fftRec(c, forward);
		double theta = Math.PI * 2 / a.length;
		if (!forward) theta *= -1;
		Complex w = new Complex(1, 0);
		Complex base = new Complex(Math.cos(theta), Math.sin(theta));
		for (int i = 0; i < a.length / 2; ++i) {
			Complex wc = c[i].mul(w);
			a[i] = new Complex(b[i].re + wc.re, b[i].im + wc.im);
			a[a.length / 2 + i] = new Complex(b[i].re - wc.re, b[i].im - wc.im);
			w = w.mul(base);
		}
	}

	static double[] convolutionFft(double[] a, double[] b, boolean recursive) {
		int N = 1;
		while (N < 2 * a.length) {
			N *= 2;
		}
		Complex[] aa = new Complex[N];
		Complex[] ba = new Complex[N];
		for (int i = 0; i < a.length; ++i) {
			aa[i] = new Complex(a[i], 0);
			ba[i] = new Complex(b[i], 0);
		}
		for (int i = a.length; i < N; ++i) {
			aa[i] = new Complex(0, 0);
			ba[i] = new Complex(0, 0);
		}
		if (recursive) {
			fftRec(aa, true);
			fftRec(ba, true);
		} else {
			fftLoop(aa, true);
			fftLoop(ba, true);
		}
		Complex[] mul = new Complex[N];
		for (int i = 0; i < N; ++i) {
			mul[i] = aa[i].mul(ba[i]);
		}
		if (recursive) {
			fftRec(mul, false);
		} else {
			fftLoop(mul, false);
		}
		for (int i = 0; i < N; ++i) {
			mul[i].re /= N;
			mul[i].im /= N;
		}
		double[] ret = new double[2 * a.length];
		for (int i = 0; i < ret.length; ++i) {
			ret[i] = mul[i].re;
		}

		return ret;
	}

	static class Complex {
		double re, im;

		Complex(double re, double im) {
			this.re = re;
			this.im = im;
		}

		Complex mul(Complex o) {
			return new Complex(this.re * o.re - this.im * o.im, this.re * o.im + this.im * o.re);
		}

		public String toString() {
			return "(" + re + "," + im + ")";
		}
	}

	static double[] convolutionNaive(double[] a, double[] b) {
		int N = a.length;
		double[] ret = new double[2 * N];
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < N; ++j) {
				ret[i + j] += a[i] * b[j];
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		Random rnd = new Random(42);
		double[] a = new double[N];
		double[] b = new double[N];
		for (int i = 0; i < N; ++i) {
			a[i] = rnd.nextInt(100);
			b[i] = rnd.nextInt(100);
		}
		double[] ans;
		{
			long before = System.currentTimeMillis();
			ans = convolutionNaive(a, b);
			System.out.println("naive:" + (System.currentTimeMillis() - before) + "ms");
		}
		{
			long before = System.currentTimeMillis();
			double[] ansFft = convolutionFft(a, b, true);
			System.out.println("fft by recursion:" + (System.currentTimeMillis() - before) + "ms");
			double error = 0;
			for (int i = 0; i < N; ++i) {
				error = Math.max(error, Math.abs(ansFft[i] - ans[i]));
			}
			System.out.println("error:" + error);
		}
		{
			long before = System.currentTimeMillis();
			double[] ansFft = convolutionFft(a, b, false);
			System.out.println("fft by loop:" + (System.currentTimeMillis() - before) + "ms");
			double error = 0;
			for (int i = 0; i < N; ++i) {
				error = Math.max(error, Math.abs(ansFft[i] - ans[i]));
			}
			System.out.println("error:" + error);
		}

	}
}
