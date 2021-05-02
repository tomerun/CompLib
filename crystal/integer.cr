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
