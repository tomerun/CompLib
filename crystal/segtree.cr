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

  def least(lo : Int32, hi : Int32, &block : T -> Bool)
    ret = @zero
    bit = 1
    base = @size
    while lo < hi
      if (lo & ((1 << bit) - 1)) != 0
        ret = @op.call(ret, @ar[base + (lo >> (bit - 1))])
        lo += 1 << (bit - 1)
      end
      if (hi & ((1 << bit) - 1)) != 0
        ret = @op.call(ret, @ar[base + (hi >> (bit - 1)) - 1])
        hi -= 1 << (bit - 1)
      end
      bit += 1
      base >>= 1
    end
    return ret
  end

  def get(lo : Int32, hi : Int32)
    ret = @zero
    bit = 1
    base = @size
    while lo < hi
      if (lo & ((1 << bit) - 1)) != 0
        ret = @op.call(ret, @ar[base + (lo >> (bit - 1))])
        lo += 1 << (bit - 1)
      end
      if (hi & ((1 << bit) - 1)) != 0
        ret = @op.call(ret, @ar[base + (hi >> (bit - 1)) - 1])
        hi -= 1 << (bit - 1)
      end
      bit += 1
      base >>= 1
    end
    return ret
  end

  def set(pos : Int32, v : T)
    base = @size
    @ar[base + pos] = v
    pos >>= 1
    base >>= 1
    while base > 0
      @ar[base + pos] = @op.call(@ar[base * 2 + pos * 2], @ar[base * 2 + pos * 2 + 1])
      pos >>= 1
      base >>= 1
    end
  end
end

class RangeSetSegTree(T)
  @ar : Array(T)
  @size : Int32
  @op : Proc(T, T, T)
  @mul : Proc(Int32, T, T)
  @zero : T

  def initialize(s : Int32, @op : Proc(T, T, T), @mul : Proc(Int32, T, T), @zero : T)
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

  def least(lo : Int32, hi : Int32, &block : T -> Bool)
    ret = @zero
    bit = 1
    base = @size
    while lo < hi
      if (lo & ((1 << bit) - 1)) != 0
        ret = @op.call(ret, @ar[base + (lo >> (bit - 1))])
        lo += 1 << (bit - 1)
      end
      if (hi & ((1 << bit) - 1)) != 0
        ret = @op.call(ret, @ar[base + (hi >> (bit - 1)) - 1])
        hi -= 1 << (bit - 1)
      end
      bit += 1
      base >>= 1
    end
    return ret
  end

  def get(lo : Int32, hi : Int32)
    ret = @zero
    bit = 1
    base = @size
    while lo < hi
      if (lo & ((1 << bit) - 1)) != 0
        ret = @op.call(ret, @ar[base + (lo >> (bit - 1))])
        lo += 1 << (bit - 1)
      end
      if (hi & ((1 << bit) - 1)) != 0
        ret = @op.call(ret, @ar[base + (hi >> (bit - 1)) - 1])
        hi -= 1 << (bit - 1)
      end
      bit += 1
      base >>= 1
    end
    return ret
  end

  def set(pos : Int32, v : T)
    base = @size
    @ar[base + pos] = v
    pos >>= 1
    base >>= 1
    while base > 0
      @ar[base + pos] = @op.call(@ar[base * 2 + pos * 2], @ar[base * 2 + pos * 2 + 1])
      pos >>= 1
      base >>= 1
    end
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
