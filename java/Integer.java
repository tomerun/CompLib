
	static int chineseRemainder(int[] mod, int[] rem) {
		int m1 = mod[0];
		int r1 = rem[0];
		for (int i = 1; i < mod.length; ++i) {
			int m2 = mod[i];
			int r2 = rem[i];
			r1 = chineseRemainder(m1, r1, m2, r2);
			m1 *= m2;
		}
		return r1;
	}

	static int chineseRemainder(int m1, int r1, int m2, int r2) {
		int A = ((r2 - r1) % m2 + m2) * inv(m1, m2) % m2;
		return (A * m1 + r1) % (m1 * m2);
	}

	static int totient(int v) {
		int ret = v;
		for (int i = 0; i < ps.length && v > 1; ++i) {
			if (v % ps[i] == 0) {
				v /= ps[i];
				while (v % ps[i] == 0) {
					v /= ps[i];
				}
				ret /= ps[i];
				ret *= (ps[i] - 1);
			}
		}
		return ret;
	}

	static int[] modFact(int n, int p, int base, int[] factTable) {
		if (n == 0) return new int[] { 1, 0 };
		int[] ret = modFact(n / base, p, base, factTable);
		ret[1] += n / base;
		if (n / p % 2 == 0) {
			ret[0] = ret[0] * factTable[n % p] % p;
		} else {
			ret[0] = ret[0] * (p - factTable[n % p]) % p;
		}
		return ret;
	}