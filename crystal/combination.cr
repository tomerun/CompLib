MOD  = 1_000_000_007i64
FACT = [1i64]
100000.times do |i|
  FACT << FACT[i] * (i + 1) % MOD
end
FACT_INV = Array.new(FACT.size, 0i64)
FACT_INV[-1] = inv(FACT[-1])
(FACT_INV.size - 2).downto(0) do |i|
  FACT_INV[i] = FACT_INV[i + 1] * (i + 1) % MOD
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
