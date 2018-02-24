  static double[] solveLinearEquations(Matrix a, double[] b) {
    final int N = a.dim1();
    assert (a.dim2() == N);
    assert (b.length == N);
    int[] pi = new int[N];
    lupDecomposition(a, pi);
    double[] x = new double[N];
    double[] y = new double[N];
    for (int i = 0; i < N; i++) {
      y[i] = b[pi[i]];
      for (int j = 0; j < i; j++) {
        y[i] -= a.v[i][j] * y[j];
      }
    }
    for (int i = N - 1; i >= 0; i--) {
      x[i] = y[i];
      for (int j = i + 1; j < N; j++) {
        x[i] -= a.v[i][j] * x[j];
      }
      x[i] /= a.v[i][i];
    }
    return x;
  }

  static void lupDecomposition(Matrix a, int[] pi) {
    final int N = a.dim1();
    for (int i = 0; i < N; i++) {
      pi[i] = i;
    }
    for (int i = 0; i < N; i++) {
      double p = 0;
      int ni = -1;
      for (int j = i; j < N; j++) {
        if (Math.abs(a.v[j][i]) > p) {
          p = Math.abs(a.v[j][i]);
          ni = j;
        }
      }
      if (ni == -1) throw new RuntimeException("not full rank");
      int tmp = pi[i];
      pi[i] = pi[ni];
      pi[ni] = tmp;
      double[] tmpa = a.v[i];
      a.v[i] = a.v[ni];
      a.v[ni] = tmpa;
      for (int j = i + 1; j < N; j++) {
        a.v[j][i] /= a.v[i][i];
        for (int k = i + 1; k < N; k++) {
          a.v[j][k] -= a.v[j][i] * a.v[i][k];
        }
      }
    }
  }

  static class Matrix {

    double[][] v;

    Matrix(int dh, int dw) {
      this.v = new double[dh][dw];
    }

    Matrix mul(Matrix o) {
      Matrix ret = new Matrix(this.dim1(), o.dim2());
      for (int i = 0; i < this.dim1(); ++i) {
        for (int j = 0; j < o.dim2(); ++j) {
          for (int k = 0; k < this.dim2(); ++k) {
            ret.v[i][j] += this.v[i][k] * o.v[k][j];
          }
        }
      }
      return ret;
    }

    Matrix pow(long p) {
      Matrix ret = new Matrix(this.dim1(), this.dim1());
      for (int i = 0; i < this.dim1(); ++i) {
        ret.v[i][i] = 1;
      }
      Matrix base = this.clone();
      while (p > 0) {
        if (p % 2 != 0) {
          ret = ret.mul(base);
        }
        p /= 2;
        base = base.mul(base);
      }
      return ret;
    }

    int dim1() {
      return this.v.length;
    }

    int dim2() {
      return this.v[0].length;
    }

    public Matrix clone() {
      Matrix ret = new Matrix(this.dim1(), this.dim2());
      for (int i = 0; i < this.dim1(); ++i) {
        System.arraycopy(this.v[i], 0, ret.v[i], 0, this.dim2());
      }
      return ret;
    }

    @Override
    public String toString() {
      return Arrays.deepToString(v);
    }
  }

	static class Matrix {

		long[][] v;

		Matrix(int dh, int dw) {
			this.v = new long[dh][dw];
		}

		Matrix mul(Matrix o) {
			Matrix ret = new Matrix(this.dim1(), o.dim2());
			for (int i = 0; i < this.dim1(); ++i) {
				for (int j = 0; j < o.dim2(); ++j) {
					for (int k = 0; k < this.dim2(); ++k) {
						ret.v[i][j] += this.v[i][k] * o.v[k][j];
						ret.v[i][j] %= MOD;
					}
				}
			}
			return ret;
		}

		Matrix pow(long p) {
			Matrix ret = new Matrix(this.dim1(), this.dim1());
			for (int i = 0; i < this.dim1(); ++i) {
				ret.v[i][i] = 1;
			}
			Matrix base = this.clone();
			while (p > 0) {
				if (p % 2 != 0) {
					ret = ret.mul(base);
				}
				p /= 2;
				base = base.mul(base);
			}
			return ret;
		}

		int dim1() {
			return this.v.length;
		}

		int dim2() {
			return this.v[0].length;
		}

		public Matrix clone() {
			Matrix ret = new Matrix(this.dim1(), this.dim2());
			for (int i = 0; i < this.dim1(); ++i) {
				for (int j = 0; j < this.dim2(); ++j) {
					ret.v[i][j] = this.v[i][j];
				}
			}
			return ret;
		}

	}

	// BigInteger
	static class Matrix {

		BigInteger[][] v;

		Matrix(int dh, int dw) {
			this.v = new BigInteger[dh][dw];
			for (int i = 0; i < dh; ++i) {
				for (int j = 0; j < dw; ++j) {
					v[i][j] = BigInteger.ZERO;
				}
			}
		}

		Matrix mul(Matrix o) {
			Matrix ret = new Matrix(this.dim1(), o.dim2());
			for (int i = 0; i < this.dim1(); ++i) {
				for (int j = 0; j < o.dim2(); ++j) {
					for (int k = 0; k < this.dim2(); ++k) {
						ret.v[i][j] = ret.v[i][j].add(this.v[i][k].multiply(o.v[k][j]));
						ret.v[i][j] = ret.v[i][j].remainder(MOD_BI);
					}
				}
			}
			return ret;
		}

		Matrix pow(long p) {
			Matrix ret = new Matrix(this.dim1(), this.dim1());
			for (int i = 0; i < this.dim1(); ++i) {
				ret.v[i][i] = BigInteger.ONE;
			}
			Matrix base = this.clone();
			while (p > 0) {
				if (p % 2 != 0) {
					ret = ret.mul(base);
				}
				p /= 2;
				base = base.mul(base);
			}
			return ret;
		}

		int dim1() {
			return this.v.length;
		}

		int dim2() {
			return this.v[0].length;
		}

		public Matrix clone() {
			Matrix ret = new Matrix(this.dim1(), this.dim2());
			for (int i = 0; i < this.dim1(); ++i) {
				for (int j = 0; j < this.dim2(); ++j) {
					ret.v[i][j] = this.v[i][j];
				}
			}
			return ret;
		}

	}

