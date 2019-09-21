import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

	public static int pow(int a, int p) {
		if (p <= 0)
			return 1;
		int r = p % 2 == 0 ? 1 : a;
		int s = pow(a, p / 2);
		r *= s;
		r *= s;
		return r;
	}

	public static int gcd(int a, int b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	public static void pascal() {
		int size = 202;
		int[][] comb = new int[size][size];
		for (int i = 0; i < size; i++) {
			comb[i][0] = 1;
		}
		for (int i = 1; i < size; ++i) {
			for (int j = 1; j <= i; ++j) {
				comb[i][j] = comb[i - 1][j] + comb[i - 1][j - 1];
				if (comb[i][j] < 0) {
					comb[i][j] = Integer.MAX_VALUE;
				}
			}
		}
	}

	public static boolean nextPermutation(int[] a) {
		for (int i = a.length - 1; i > 0; --i) {
			if (a[i - 1] < a[i]) {
				int swapIndex = find(a[i - 1], a, i, a.length - 1);
				int temp = a[swapIndex];
				a[swapIndex] = a[i - 1];
				a[i - 1] = temp;
				Arrays.sort(a, i, a.length);
				return true;
			}
		}
		return false;
	}

	private static int find(int dest, int[] a, int s, int e) {
		if (s == e) {
			return s;
		}
		int m = (s + e + 1) / 2;
		return a[m] <= dest ? find(dest, a, s, m - 1) : find(dest, a, m, e);
	}

	public static int[][] permutation(int size) {
		ArrayList<int[]> list = new ArrayList<int[]>(fact(size));
		int[] array = new int[size];
		Arrays.fill(array, 0);
		collectArrays(list, array, 1);
		return list.toArray(new int[0][0]);
	}

	private static void collectArrays(List<int[]> list, int[] array, int level) {
		if (level > array.length) {
			list.add(Arrays.copyOf(array, array.length));
			return;
		}
		for (int i = 0; i < array.length; i++) {
			if (array[i] == 0) {
				array[i] = level;
				collectArrays(list, array, level + 1);
				array[i] = 0;
			}
		}
	}

	private static int fact(int num) {
		if (num <= 1) {
			return 1;
		}
		return fact(num - 1) * num;
	}

	public static long combination(int n, int m) {
		long ret = 1;
		for (int i = 0; i < m; i++) {
			ret *= (n - i);
		}
		for (int i = 1; i <= m; i++) {
			ret /= i;
		}
		return ret;
	}

	static class Rational {
		final long num;
		final long den;

		Rational(long n, long d) {
			if (n == 0) {
				num = 0;
				den = 1;
			} else {
				long gcd = gcd(Math.abs(n), Math.abs(d));
				num = n / gcd;
				den = d / gcd;
			}
		}

		Rational mul(long n, long d) {
			return new Rational(this.num * n, this.den * d);
		}

		Rational add(long n, long d) {
			return new Rational(this.num * d + n * this.den, this.den * d);
		}

		Rational add(Rational r) {
			return add(r.num, r.den);
		}

		Rational subtract(Rational r) {
			return add(-r.num, r.den);
		}

		public int compareTo(Rational r) {
			long n1 = this.num * r.den;
			long n2 = this.den * r.num;
			if (n1 > n2) {
				return 1;
			} else if (n1 == n2) {
				return 0;
			} else {
				return -1;
			}
		}

		public String toString() {
			return this.num + "/" + this.den;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (den ^ (den >>> 32));
			result = prime * result + (int) (num ^ (num >>> 32));
			return result;
		}

		public boolean equals(Object obj) {
			Rational other = (Rational) obj;
			if (den != other.den) return false;
			if (num != other.num) return false;
			return true;
		}

	}

	static class Rational implements Comparable<Rational> {
		final BigInteger num;
		final BigInteger den;

		Rational(BigInteger n, BigInteger d) {
			if (n.equals(BigInteger.ZERO)) {
				num = BigInteger.ZERO;
				den = BigInteger.ONE;
			} else {
				BigInteger gcd = n.gcd(d);
				num = n.divide(gcd);
				den = d.divide(gcd);
			}
		}

		Rational mul(Rational v) {
			return new Rational(this.num.multiply(v.num), this.den.multiply(v.den));
		}

		Rational div(Rational v) {
			return new Rational(this.num.multiply(v.den), this.den.multiply(v.num));
		}

		Rational add(Rational v) {
			return new Rational(this.num.multiply(v.den).add(v.num.multiply(this.den)), this.den.multiply(v.den));
		}

		public int compareTo(Rational r) {
			BigInteger n1 = this.num.multiply(r.den);
			BigInteger n2 = this.den.multiply(r.num);
			return n1.compareTo(n2);
		}

		public String toString() {
			return this.num + "/" + this.den;
		}

		public int hashCode() {
			return this.num.hashCode() ^ this.den.hashCode();
		}

		public boolean equals(Object obj) {
			Rational other = (Rational) obj;
			if (!den.equals(other.den)) return false;
			if (!num.equals(other.num)) return false;
			return true;
		}

	}

	static class UnionFind {
		int[] set;

		UnionFind(int n) {
			set = new int[n];
			Arrays.fill(set, -1);
		}

		void union(int a, int b) {
			int rtA = root(a);
			int rtB = root(b);
			if (rtA == rtB) {
				return;
			}
			set[rtA] += set[rtB];
			set[rtB] = rtA;
		}

		boolean find(int a, int b) {
			return root(a) == root(b);
		}

		int root(int a) {
			if (set[a] < 0) {
				return a;
			} else {
				set[a] = root(set[a]);
				return set[a];
			}
		}

		int size(int a) {
			return -set[root(a)];
		}
	}


	static class Bits {
		long[] bits;

		Bits(int size) {
			bits = new long[(size + 63) / 64];
		}

		void shiftLOr(int shift) {
			int m1 = shift >> 6;
			int m2 = shift & 63;
			if (m2 == 0) {
				for (int i = bits.length - 1 - m1; i >= 0; i--) {
					bits[i + m1] |= bits[i];
				}
			} else {
				for (int i = bits.length - 1 - m1; i >= 0; i--) {
					int p1 = i + m1 + 1;
					int p2 = i + m1;
					long v1 = bits[i] >>> (64 - m2);
					long v2 = bits[i] << m2;
					if (p1 < bits.length) {
						bits[p1] |= v1;
					}
					bits[p2] |= v2;
				}
			}
		}

		void copy(long[] buf, int from) {
			int start = from >> 6;
			int rem = from & 63;
			if (rem == 0) {
				for (int i = start; i < bits.length; i++) {
					buf[i - start] = bits[i];
				}
			} else {
				for (int i = start; i < bits.length; i++) {
					buf[i - start] = bits[i] >>> rem;
					if (i < bits.length - 1) {
						buf[i - start] |= bits[i + 1] << (64 - rem);
					}
				}
			}
			if (start > 0) buf[bits.length - start] = 0;
		}

		boolean get(int pos) {
			return ((bits[pos >> 6] >>> (pos & 63)) & 1) != 0;
		}

		void set(int pos) {
			bits[pos >> 6] |= (1L << (pos & 63));
		}
	}

}
