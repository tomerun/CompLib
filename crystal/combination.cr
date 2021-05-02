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

COMBINATION = Array.new(1001) { Array.new(1001, 0i64) }
COMBINATION.size.times do |i|
  COMBINATION[i][0] = COMBINATION[i][i] = 1i64
end
COMBINATION.size.times do |i|
  1.upto(i - 1) do |j|
    COMBINATION[i][j] = COMBINATION[i - 1][j] + COMBINATION[i - 1][j - 1]
    COMBINATION[i][j] -= MOD if COMBINATION[i][j] >= MOD
  end
end

def lucas_theorum(n, k)
  # C(n, k) % MOD
  ret = 1
  while n > 0
    ret *= COMBINATION[n % MOD][k % MOD]
    ret %= MOD
    n //= MOD
    k //= MOD
  end
  return ret
end
