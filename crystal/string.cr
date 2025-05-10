def z_algo(p : Array(T), t : Array(T)) forall T
  # find p in t
  # z[i] := prefix match length starting at (p+(null)+t)[i]
  ret = [] of Int32
  return ret if p.size > t.size
  s = p + [T.zero] + t
  z = Array.new(s.size, 0)
  left = 0
  right = 0
  1.upto(s.size - 1) do |i|
    if i >= right
      left = i
      right = i
      while right < s.size && s[right] == s[right - left]
        right += 1
      end
      z[i] = right - left
    else
      k = i - left
      if z[k] < right - i
        z[i] = z[k]
      else
        left = i
        while right < s.size && s[right] == s[right - left]
          right += 1
        end
        z[i] = right - left
      end
    end
    if p.size < i && z[i] == p.size
      ret << i - p.size - 1
    end
  end
  return ret
end

class RollingHash(T)
  MUL = 31i64
  MOD = [1_000_000_007i64, 1_000_000_009i64]

  def initialize(s : Array(T))
    @a = Array(Array(Int64)).new(MOD.size) { [0i64] }
    @pow = Array(Array(Int64)).new(MOD.size) { [1i64] }
    s.each do |v|
      MOD.size.times do |i|
        @a[i] << (@a[i][-1] + @pow[i][-1] * v) % MOD[i]
        @pow[i] << @pow[i][-1] * MUL % MOD[i]
      end
    end
    @pow_rev = Array(Array(Int64)).new(MOD.size) { Array.new(s.size + 1, 0i64) }
    MOD.size.times do |i|
      @pow_rev[i][-1] = inv(@pow[i][-1], MOD[i])
      s.size.downto(1) do |j|
        @pow_rev[i][j - 1] = @pow_rev[i][j] * MUL % MOD[i]
      end
    end
  end

  def get(l, r)
    # [l, r)
    MOD.size.times.map do |i|
      v = @a[i][r] - @a[i][l]
      v += MOD[i] if v < 0
      v *= @pow_rev[i][l]
      v % MOD[i]
    end.to_a
  end

  def inv(v : Int64, mod : Int64)
    pow(v, mod - 2, mod)
  end

  def pow(v : Int64, p, mod : Int64)
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
end

# MOD1つだけ版
class RollingHash(T)
  MUL =                31i64
  MOD = 10000000000000061i64

  def initialize(s : Array(T))
    @a = [0i64]
    @pow = [1i64]
    s.each do |v|
      @a << (@a[-1] + @pow[-1] * v) % MOD
      @pow << @pow[-1] * MUL % MOD
    end
    @pow_rev = Array(Int64).new(s.size + 1, 0i64)
    @pow_rev[-1] = inv(@pow[-1].to_i128)
    s.size.downto(1) do |i|
      @pow_rev[i - 1] = @pow_rev[i] * MUL % MOD
    end
  end

  def get(l, r)
    # [l, r)
    v = @a[r] - @a[l]
    v += MOD if v < 0
    v = (v.to_i128 * @pow_rev[l]) % MOD
    v.to_i64
  end

  def inv(v)
    pow(v, MOD - 2)
  end

  def pow(v, p)
    ret = 1.to_i128
    while p > 0
      if (p & 1i64) != 0
        ret *= v
        ret %= MOD
      end
      v *= v
      v %= MOD
      p >>= 1
    end
    ret.to_i64
  end
end

def manacher(s)
  str = ['$']
  s.each do |c|
    str << c << '$'
  end
  i = 0
  j = 0
  rad = Array.new(str.size, 0)
  while i < str.size
    while i - j >= 0 && i + j < str.size && str[i - j] == str[i + j]
      j += 1
    end
    rad[i] = j
    k = 1
    while k <= i && k + rad[i - k] < j
      rad[i + k] = rad[i - k]
      k += 1
    end
    i += k
    j -= k
  end
  rad

  # TODO

  # pos = 1.step(to: str.size - 1, by: 2).max_by { |i| rad[i] }
  # len = (rad[pos] + 1) // 2
  # odd_max = {(pos - rad[pos] + 1) // 2, len * 2 - 1}

  # pos = 0.step(to: str.size - 1, by: 2).max_by { |i| rad[i] }
  # len = rad[pos] // 2
  # even_max = {(pos - rad[pos] + 1) // 2, len * 2}
end

class SuffixArray
  @suffix_array : Array(Int32)

  def initialize(str : String)
    s = str.bytes.map(&.to_i) + [0] # 文字列を整数配列に変換し、番兵を追加
    max_char = s.max
    @suffix_array = rec(s, max_char)
  end

  def rec(s : Array(Int32), max_char : Int32) : Array(Int32)
    n = s.size
    is_stype = Array(Bool).new(n, false)
    is_stype[n - 1] = true

    (n - 2).downto(0) do |i|
      is_stype[i] = if s[i] == s[i + 1]
                      is_stype[i + 1]
                    else
                      s[i] < s[i + 1]
                    end
    end

    lms_index = [] of Int32
    (0...n - 1).each { |i| lms_index << (i + 1) if !is_stype[i] && is_stype[i + 1] }

    sa = Array(Int32).new(n, -1)
    bucket_tail = get_bucket_tail(s, max_char)
    bucket_pos = bucket_tail.dup

    lms_index.each do |p|
      first_char = s[p]
      bucket_pos[first_char] -= 1
      sa[bucket_pos[first_char]] = p
    end

    induced_sort(s, is_stype, sa, bucket_tail.dup)

    s1 = Array.new(lms_index.size, 0)
    n1 = 1
    1.upto(n - 1) do |i|
      if is_lms(is_stype, sa[i])
        sa[n1] = sa[i]
        n1 += 1
      end
    end
    sa.fill(-1, n1...n)
    name = 0
    n1.times do |i|
      if i != 0 && !is_equal_lms(s, is_stype, sa[i - 1], sa[i])
        name += 1
      end
      sa[n1 + sa[i] // 2] = name
    end
    i = n - 1
    j = n1
    while j > 0
      if sa[i] != -1
        j -= 1
        s1[j] = sa[i]
      end
      i -= 1
    end
    sa1 = [] of Int32
    if name + 1 == n1
      sa1 = Array.new(n1, 0)
      n1.times do |i|
        sa1[s1[i]] = i
      end
    else
      sa1 = rec(s1, name)
    end

    sa.fill(-1)
    bucket_pos = bucket_tail.dup
    (sa1.size - 1).downto(0) do |i|
      p = lms_index[sa1[i]]
      first_char = s[p]
      bucket_pos[first_char] -= 1
      sa[bucket_pos[first_char]] = p
    end
    induced_sort(s, is_stype, sa, bucket_tail.dup)
    sa
  end

  def induced_sort(s, is_stype, sa, bucket_tail)
    n = s.size
    bucket_head = Array(Int32).new(bucket_tail.size, 0)
    (1...bucket_head.size).each { |i| bucket_head[i] = bucket_tail[i - 1] }

    n.times do |i|
      next if sa[i] <= 0
      if !is_stype[sa[i] - 1]
        first_char = s[sa[i] - 1]
        sa[bucket_head[first_char]] = sa[i] - 1
        bucket_head[first_char] += 1
      end
    end

    (n - 1).downto(0) do |i|
      next if sa[i] <= 0
      if is_stype[sa[i] - 1]
        first_char = s[sa[i] - 1]
        bucket_tail[first_char] -= 1
        sa[bucket_tail[first_char]] = sa[i] - 1
      end
    end
  end

  def is_equal_lms(s, is_stype, p1, p2)
    i = 0
    while true
      return false if (s[p1 + i] != s[p2 + i])
      return false if is_stype[p1 + i] != is_stype[p2 + i]
      if i > 0
        return is_lms(is_stype, p2 + i) if is_lms(is_stype, p1 + i)
        return false if is_lms(is_stype, p2 + i)
      end
      i += 1
    end
  end

  def get_bucket_tail(s, max_char)
    bucket_tail = Array(Int32).new(max_char + 1, 0)
    s.each { |v| bucket_tail[v] += 1 }
    (1..max_char).each { |i| bucket_tail[i] += bucket_tail[i - 1] }
    bucket_tail
  end

  def is_lms(is_stype, p)
    return false if p == 0
    return is_stype[p] && !is_stype[p - 1]
  end

  def self.build(str : String) : SuffixArray
    new(str)
  end

  def suffix_array
    @suffix_array
  end
end

# sa = SuffixArray.new("...")
# sa.suffix_array.each do |i|
#   puts "#{sprintf("%2d", i)} #{s[i..].join}"
# end
