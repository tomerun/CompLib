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

