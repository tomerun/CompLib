MOD      = 1_000_000_007i64
FACT     = [1i64]
FACT_INV = [1i64]
2001.times do |i|
  FACT << FACT[i] * (i + 1)
  FACT_INV << inv(FACT[i + 1])
end

def binom(n, k)
  FACT[n] * FACT_INV[k] % MOD * FACT_INV[n - k] % MOD
end

def inv(v)
  pow(v, MOD - 2)
end

def pow(v, p)
  return 1 if p == 0
  return v if p == 1
  ret = pow(v, p >> 1)
  ret *= ret
  ret %= MOD
  if p % 2 == 1
    ret *= v
    ret %= MOD
  end
  ret
end
