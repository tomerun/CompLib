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
