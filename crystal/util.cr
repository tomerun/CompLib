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
end

class BIT
  def initialize(size)
    len = 1
    while len < size
      len *= 2
    end
    @v = Array(Int64).new(len + 1, 0)
  end

  def cumulativeSum(index)
    ret = 0i64
    while index > 0
      ret += @v[index]
      # ret %= MOD
      index &= index - 1
    end
    return ret
  end

  # inclusive, 1-indexed
  def sum(l, r)
    ret = cumulativeSum(r) - cumulativeSum(l - 1)
    # ret += MOD if ret < 0
    return ret
  end

  def add(index, val)
    while index < @v.size
      @v[index] += val
      # @v[index] %= MOD
      # @v[index] += MOD if (@v[index] < 0)
      index += (index & -index)
    end
  end

  def set(index, val)
    old = sum(index, index)
    add(index, val - old)
  end
end
