    static final class IntAry {
        int[] a;
        int size;

        IntAry() {
            a = new int[16];
        }

        IntAry(int cap) {
            a = new int[cap];
        }

        IntAry(int[] a) {
            this.a = a;
            this.size = a.length;
        }

        IntAry(IntAry that) {
            a = that.a.clone();
            size = that.size;
        }

        @Override
        public String toString() {
            return Arrays.toString(Arrays.copyOf(a, size));
        }

        IntAry copyOf(int from, int to) {
            return new IntAry(Arrays.copyOfRange(this.a, from, to));
        }

        void add(int v) {
            if (size == a.length) {
                int[] na = new int[a.length * 2];
                System.arraycopy(a, 0, na, 0, size);
                a = na;
            }
            a[size++] = v;
        }

        void addAll(IntAry ia) {
            if (this.size + ia.size < a.length) {
                int[] na = new int[this.size + ia.size];
                System.arraycopy(a, 0, na, 0, size);
                a = na;
            }
            System.arraycopy(ia.a, 0, a, size, ia.size);
            size += ia.size;
        }

        void clear() {
            size = 0;
        }

        int pop() {
            size--;
            return a[size];
        }

        int back() {
            return a[size - 1];
        }

        void remove(int pos) {
            System.arraycopy(a, pos + 1, a, pos, size - pos - 1);
            size--;
        }

        void swapRemove(int pos) {
            swap(a, pos, size - 1);
            size--;
        }

        void sort() {
            Arrays.sort(a, 0, size);
        }
    }

    static void swap(int[] a, int p1, int p2) {
        int tmp = a[p1];
        a[p1] = a[p2];
        a[p2] = tmp;
    }

  static class CountBuf {
      int[][] count;
      int turn;

      CountBuf(int size) {
          count = new int[size][size];
      }

      void clear() {
          turn++;
      }

      boolean get(int r, int c) {
          return count[r][c] == turn;
      }

      void set(int r, int c) {
          count[r][c] = turn;
      }
  }

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

    static void debugf(String fmt, Object... obj) {
        if (DEBUG) System.err.printf(fmt, obj);
    }

    static void debugfln(String fmt, Object... obj) {
        if (DEBUG) System.err.printf(fmt + "\n", obj);
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