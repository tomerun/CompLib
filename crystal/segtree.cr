class SegTree(T)
  @ar : Array(T)
  @size : Int32
  @op : Proc(T, T, T)
  @zero : T

  def initialize(s : Int32, @op : Proc(T, T, T), @zero : T)
    @size = 1
    while @size < s
      @size *= 2
    end
    @ar = Array.new(@size * 2, @zero)
  end

  def initialize(init : Array(T), @op : Proc(T, T, T), @zero : T)
    @size = 1
    while @size < init.size
      @size *= 2
    end
    @ar = Array.new(@size, @zero)
    @ar.concat(init)
    @ar.concat([@zero] * (@size - init.size))
    (@size - 1).downto(1) do |i|
      @ar[i] = @op.call(@ar[i * 2], @ar[i * 2 + 1])
    end
  end

  def get(lo : Int32, hi : Int32)
    return get(lo, hi, 1, 0, @size)
  end

  private def get(lo : Int32, hi : Int32, node : Int32, left : Int32, right : Int32)
    if lo <= left && right <= hi
      return @ar[node]
    end
    mid = (left + right) // 2
    ret = @zero
    if lo < mid
      ret = get(lo, hi, node * 2, left, mid)
    end
    if hi > mid
      ret = @op.call(ret, get(lo, hi, node * 2 + 1, mid, right))
    end
    return ret
  end

  def set(pos : Int32, v : T)
    return set(pos, 1, 0, @size, v)
  end

  private def set(pos : Int32, node : Int32, left : Int32, right : Int32, v : T)
    if pos == left && pos + 1 == right
      @ar[node] = v
      return
    end
    mid = (left + right) // 2
    if pos < mid
      set(pos, node * 2, left, mid, v)
    else
      set(pos, node * 2 + 1, mid, right, v)
    end
    @ar[node] = @op.call(@ar[node * 2], @ar[node * 2 + 1])
  end
end

data = [1, 2, 3, 4, 5, 6, 1, 2]
segtree = SegTree(Int32).new(data, ->(x : Int32, y : Int32) { {x, y}.max }, 0)
2.upto(data.size) do |i|
  puts segtree.get(1, i)
end
segtree.set(3, 7)
2.upto(data.size) do |i|
  puts segtree.get(1, i)
end
