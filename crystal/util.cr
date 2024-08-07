class UnionFind
  def initialize(size : Int32)
    @root = Array(Int32).new(size, -1)
  end

  def union(i, j)
    a = root(i)
    b = root(j)
    if a != b
      @root[a] += @root[b]
      @root[b] = a
    end
  end

  def find(i, j)
    root(i) == root(j)
  end

  def root(i)
    return i if @root[i] < 0
    @root[i] = root(@root[i])
  end

  def size(i)
    -@root[root(i)]
  end
end

class BIT(T)
  # 1-indexed

  def initialize(size : Int)
    len = 1
    while len < size
      len *= 2
    end
    @v = Array(T).new(len + 1, T.zero)
  end

  def cumulative_sum(index : Int)
    ret = T.zero
    while index > 0
      ret += @v[index]
      index &= index - 1
    end
    ret
  end

  def sum(l : Int, r : Int)
    cumulative_sum(r) - cumulative_sum(l - 1)
  end

  def add(index : Int, val : T)
    while index < @v.size
      @v[index] += val
      index += (index & -index)
    end
  end

  def set(index : Int, val : T)
    old = sum(index, index)
    add(index, val - old)
  end
end

class BitSet
  getter :size

  def initialize(size : Int32)
    @ar = Array(UInt64).new((size + 63) >> 6, 0u64)
    @size = size
  end

  def get(i)
    return (@ar[i >> 6] & (1u64 << (i & 63))) != 0u64
  end

  def set(i)
    return @ar[i >> 6] |= (1u64 << (i & 63))
  end

  def next(i)
    pos = i >> 6
    idx = i & 63
    cur = @ar[pos] & ~((1u64 << idx) - 1)
    if cur != 0u64
      return pos * 64 + (cur ^ (cur - 1)).popcount - 1
      # return pos * 64 + cur.trailing_zeros_count
    end
    pos += 1
    while pos < @ar.size
      if @ar[pos] != 0u64
        return pos * 64 + (@ar[pos] ^ (@ar[pos] - 1)).popcount - 1
        # return pos * 64 + @ar[pos].trailing_zeros_count
      end
      pos += 1
    end
    return -1
  end

  def shift(v)
    n = v >> 6
    p = v & 63
    ret = BitSet.new(@size)
  end
end

def floor_sum(n, m, a, b)
  # sum{i=0->n-1}(floor((a * i + b) // m))
  ans = 0i64
  while true
    if a >= m
      ans += n * (n - 1) // 2 * (a // m)
      a %= m
    end
    if b >= m
      ans += n * (b // m)
      b %= m
    end
    y_max = a * n + b
    break if y_max < m
    n = y_max // m
    b = y_max % m
    m, a = a, m
  end
  return ans
end

def uniq_permutations(a : Array(T)) forall T
  n = a.size
  while true
    yield a
    found = false
    (n - 2).downto(0) do |i|
      if a[i] < a[i + 1]
        j = n - 1
        while a[j] <= a[i]
          j -= 1
        end
        a.swap(i, j)
        s = Slice.new(a.to_unsafe + i + 1, n - i - 1)
        s.unstable_sort!
        found = true
        break
      end
    end
    break if !found
  end
end
