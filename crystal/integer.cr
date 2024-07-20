def normalized_linear_diophantine(a, b, c)
  # solve ax + by = c
  # return {x, y} with minimum nonnegative x
  if a < 0
    a *= -1
    b *= -1
    c *= -1
  end
  rev_b = b < 0 ? -1 : 1
  b *= rev_b
  gcd = a.gcd(b)
  return nil if c % gcd != 0
  a //= gcd
  b //= gcd
  c //= gcd
  if a == 0
    if c % b == 0
      return {a, c // b}
    else
      return nil
    end
  end
  if b == 0
    if c % a == 0
      return {c // a, b}
    else
      return nil
    end
  end
  x, y = extgcd(a, b)
  x *= c
  y *= c
  if x < 0
    cnt = -x // b
    cnt += 1 if x % b != 0
    x += cnt * b
    y -= cnt * a
  else
    cnt = x // b
    x -= cnt * b
    y += cnt * a
  end
  return {x, y * rev_b}
end

def extgcd(a, b)
  return {1i64, 0i64} if b == 0
  x, y = extgcd(b, a % b)
  return {y, x - (a // b) * y}
end

def primitive_root(p : Int64)
  d = 2i64
  fs = [] of Int64
  pt = p - 1
  while d * d <= pt
    if pt % d == 0
      fs << d
      pt //= d
      while pt % d == 0
        pt //= d
      end
    end
    d += 1
  end
  if pt != 1
    fs << pt
  end
  r = 2
  while true
    if fs.all? { |f| pow(r, (p - 1) // f, p) != 1 }
      return r
    end
    r += 1
  end
end

def pow(v, p, mod)
  ret = 1i64
  while p > 0
    if (p & 1i64) != 0
      ret *= v
      ret %= mod
    end
    v *= v
    v %= mod
    p >>= 1
  end
  ret
end

def totient(v)
  c2 = v.trailing_zeros_count
  v >>= c2
  ret = c2 == 0 ? 1i64 : 1i64 << (c2 - 1)
  div = 3i64
  while div * div <= v
    if v % div == 0
      c = 1
      v //= div
      while v % div == 0
        c += 1
        v //= div
      end
      ret *= (div ** (c - 1)) * (div - 1)
    end
    div += 2
  end
  if v != 1
    ret *= v - 1
  end
  return ret
end

def chinese_remainder(mod : Array(Int), rem : Array(Int))
  m1 = mod[0]
  r1 = rem[0]
  1.upto(mod.size - 1) do |i|
    m2 = mod[i]
    r2 = rem[i]
    r1 = chinese_remainder(m1, r1, m2, r2)
    m1 *= m2
  end
  return r1
end

def chinese_remainder(m1, r1, m2, r2)
  a = ((r2 - r1) % m2 + m2) * inv(m1, m2) % m2
  return (a * m1 + r1) % (m1 * m2)
end
