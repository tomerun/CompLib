
    static void nthElement(int[] a, int n) {
        int l = 0;
        int r = a.length;
        while (true) {
            int lo = l + 1;
            int hi = r - 1;
            int len = r - l;
            if (len > 10) {
                if ((n - l) * 2 < len ^ a[l] < a[l + 1]) {
                    swap(a, l, l + 1);
                }
            }
            while (lo <= hi) {
                if (a[l] < a[lo]) {
                    swap(a, lo, hi);
                    --hi;
                } else {
                    ++lo;
                }
            }
            swap(a, l, hi);
            if (hi == n || hi == n - 1) break;
            if (hi < n) {
                l = hi + 1;
            } else {
                r = hi;
            }
        }
    }

    static void swap(int[] a, int p1, int p2) {
        int tmp = a[p1];
        a[p1] = a[p2];
        a[p2] = tmp;
    }

    static <T extends Comparable<T>> void nthElement(ArrayList<T> a, int n) {
        int l = 0;
        int r = a.size();
        while (true) {
            int lo = l + 1;
            int hi = r - 1;
            int len = r - l;
            if (len > 10) {
                if ((n - l) * 2 < len ^ a.get(l).compareTo(a.get(l + 1)) < 0) {
                    swap(a, l, l + 1);
                }
            }
            while (lo <= hi) {
                if (a.get(l).compareTo(a.get(lo)) < 0) {
                    swap(a, lo, hi);
                    --hi;
                } else {
                    ++lo;
                }
            }
            swap(a, l, hi);
            if (hi == n || hi == n - 1) break;
            if (hi < n) {
                l = hi + 1;
            } else {
                r = hi;
            }
        }
    }

    static <T extends Comparable<T>> void swap(ArrayList<T> a, int p1, int p2) {
        T tmp = a.get(p1);
        a.set(p1, a.get(p2));
        a.set(p2, tmp);
    }

	static void debug(String str) {
		if (DEBUG) System.err.println(str);
	}

	static void debug(Object... obj) {
		if (DEBUG) System.err.println(Arrays.deepToString(obj));
	}

	static final class Timer {
		ArrayList<Long> sum = new ArrayList<>();
		ArrayList<Long> start = new ArrayList<>();

		void start(int i) {
			if (MEASURE_TIME) {
				while (sum.size() <= i) {
					sum.add(0L);
					start.add(0L);
				}
				start.set(i, System.currentTimeMillis());
			}
		}

		void stop(int i) {
			if (MEASURE_TIME) {
				sum.set(i, sum.get(i) + System.currentTimeMillis() - start.get(i));
			}
		}

		void print() {
			if (MEASURE_TIME && !sum.isEmpty()) {
				System.err.print("[");
				for (int i = 0; i < sum.size(); ++i) {
					System.err.print(sum.get(i) + ", ");
				}
				System.err.println("]");
			}
		}

	}

	static final class Counter {
		ArrayList<Integer> count = new ArrayList<>();

		void add(int i) {
			if (DEBUG) {
				while (count.size() <= i) {
					count.add(0);
				}
				count.set(i, count.get(i) + 1);
			}
		}

		void print() {
			if (DEBUG) {
				System.err.print("[");
				for (int i = 0; i < count.size(); ++i) {
					System.err.print(count.get(i) + ", ");
				}
				System.err.println("]");
			}
		}

	}

	static final class XorShift {
		int x = 123456789;
		int y = 362436069;
		int z = 521288629;
		int w = 88675123;
		static final double TO_DOUBLE = 1.0 / (1L << 31);

		int nextInt(int n) {
			final int t = x ^ (x << 11);
			x = y;
			y = z;
			z = w;
			w = (w ^ (w >>> 19)) ^ (t ^ (t >>> 8));
			final int r = w % n;
			return r < 0 ? r + n : r;
		}

		int nextInt() {
			final int t = x ^ (x << 11);
			x = y;
			y = z;
			z = w;
			w = (w ^ (w >>> 19)) ^ (t ^ (t >>> 8));
			return w;
		}

		boolean nextBoolean() {
			final int t = x ^ (x << 11);
			x = y;
			y = z;
			z = w;
			w = (w ^ (w >>> 19)) ^ (t ^ (t >>> 8));
			return (w & 8) == 0;
		}

		double nextDouble() {
			final int t = x ^ (x << 11);
			x = y;
			y = z;
			z = w;
			w = (w ^ (w >>> 19)) ^ (t ^ (t >>> 8));
			return Math.abs(w * TO_DOUBLE);
		}

		double nextSDouble() {
			final int t = x ^ (x << 11);
			x = y;
			y = z;
			z = w;
			w = (w ^ (w >>> 19)) ^ (t ^ (t >>> 8));
			return w * TO_DOUBLE;
		}
	}

	private static final int THREAD_COUNT = 4;

	public static void main(String[] args) throws Exception {
		long seed = 1;
		long begin = -1;
		long end = -1;
		vis = false;
		manual = false;
		debug = false;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-seed")) seed = Long.parseLong(args[++i]);
			if (args[i].equals("-begin")) begin = Long.parseLong(args[++i]);
			if (args[i].equals("-end")) end = Long.parseLong(args[++i]);
			if (args[i].equals("-vis")) vis = true;
			if (args[i].equals("-manual")) manual = true;
			if (args[i].equals("-info")) debug = true;
			if (args[i].equals("-delay")) del = Integer.parseInt(args[++i]);
			if (args[i].equals("-side")) side = Integer.parseInt(args[++i]);
		}
		if (manual) vis = true;
		if (begin != -1 && end != -1) {
			vis = false;
			manual = false;
			ArrayList<Long> seeds = new ArrayList<Long>();
			for (long i = begin; i <= end; ++i) {
				seeds.add(i);
			}
			int len = seeds.size();
			Result[] results = new Result[len];
			TestThread[] threads = new TestThread[THREAD_COUNT];
			int prev = 0;
			for (int i = 0; i < THREAD_COUNT; ++i) {
				int next = len * (i + 1) / THREAD_COUNT;
				threads[i] = new TestThread(prev, next - 1, seeds, results);
				prev = next;
			}
			for (int i = 0; i < THREAD_COUNT; ++i) {
				threads[i].start();
			}
			for (int i = 0; i < THREAD_COUNT; ++i) {
				threads[i].join();
			}
			double sum = 0;
			for (int i = 0; i < results.length; ++i) {
				System.out.println(results[i]);
				System.out.println();
				sum += results[i].score;
			}
			System.out.println("ave:" + (sum / results.length));
		} else {
			Visualizer tester = new Visualizer();
			System.out.println(tester.runTest(seed));
		}
	}

	static class TestThread extends Thread {
		int begin, end;
		ArrayList<Long> seeds;
		Result[] results;

		TestThread(int begin, int end, ArrayList<Long> seeds, Result[] results) {
			this.begin = begin;
			this.end = end;
			this.seeds = seeds;
			this.results = results;
		}

		public void run() {
			for (int i = begin; i <= end; ++i) {
				Visualizer f = new Visualizer();
				try {
					Result res = f.runTest(seeds.get(i));
					results[i] = res;
				} catch (Exception e) {
					e.printStackTrace();
					results[i] = new Result();
					results[i].seed = seeds.get(i);
				}
			}
		}
	}

	static class Result {
		static final Result NULL_RESULT = new Result();
		long seed;
		int SZ;
		int whiteCount;
		double score;
		long elapsed;
		boolean isStripe;

		public String toString() {
			return "seed " + this.seed + "\nSZ " + this.SZ + "\nelapsed " + this.elapsed / 1000.0 + "\nscore " + this.score;
		}

	}