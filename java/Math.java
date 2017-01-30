  static class Kitamasa {
    final int N;
    final long[] base;
    final long[][] coef;

    Kitamasa(long[] base, long upper) {
      this.base = base;
      this.N = base.length;
      this.coef = new long[64 - Long.numberOfLeadingZeros(upper)][N];
      build();
    }

    private void build() {
      for (int i = 0; i < coef.length; i++) {
        if ((1L << i) < N) {
          coef[i][1 << i] = 1;
        } else if (i == 0 && N == 1) {
          coef[0][0] = base[0];
        } else {
          long[] buf = new long[2 * N - 1];
          for (int j = 0; j < N; j++) {
            for (int k = 0; k < N; k++) {
              buf[j + k] += coef[i - 1][j] * coef[i - 1][k];
              buf[j + k] %= MOD;
            }
          }
          for (int j = 2 * N - 2; j >= N; j--) {
            for (int k = 0; k < N; k++) {
              buf[j - N + k] += buf[j] * base[k];
              buf[j - N + k] %= MOD;
            }
          }
          for (int j = 0; j < N; j++) {
            coef[i][j] = buf[j];
          }
        }
      }
    }

    long[] calc(long v) {
      long[] ret = new long[N];
      long[] buf = new long[N * 2];
      ret[0] = 1;
      for (int i = 0; (1L << i) <= v; i++) {
        if ((v & (1L << i)) != 0) {
          for (int j = 0; j < N; j++) {
            for (int k = 0; k < N; k++) {
              buf[j + k] += ret[j] * coef[i][k];
              buf[j + k] %= MOD;
            }
          }
          for (int j = N * 2 - 2; j >= N; j--) {
            for (int k = 0; k < N; k++) {
              buf[j - N + k] += buf[j] * base[k];
              buf[j - N + k] %= MOD;
            }
          }
          for (int j = 0; j < N; j++) {
            ret[j] = buf[j];
          }
          Arrays.fill(buf, 0);
        }
      }
      return ret;
    }

  }
