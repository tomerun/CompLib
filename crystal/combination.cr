MOD      = 1_000_000_007i64
FACT     = [1i64]
FACT_INV = [1i64]
2001.times do |i|
  FACT << FACT[i] * (i + 1) % MOD
  FACT_INV << inv(FACT[i + 1])
end

def binom(n, k)
  FACT[n] * FACT_INV[k] % MOD * FACT_INV[n - k] % MOD
end

def inv(v)
  pow(v, MOD - 2)
end

def pow(v, p)
  ret = 1i64
  while p > 0
    if (p & 1i64) != 0
      ret *= v
      ret %= MOD
    end
    v *= v
    v %= MOD
    p >>= 1
  end
  ret
end
