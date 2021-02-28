class Matrix(T)
  @a : Array(Array(T))
  getter :a, :d1, :d2

  def initialize(n : Int32, m : Int32)
    @a = Array.new(n) { Array.new(m, T.zero) }
    @d1 = n
    @d2 = m
  end

  def clone
    ret = Matrix(T).new(@d1, @d2)
    @d1.times do |i|
      @d2.times do |j|
        ret.a[i][j] = @a[i][j]
      end
    end
    return ret
  end

  def [](idx : Int32)
    return @a[idx]
  end

  def mul(other : Matrix(T))
    if @d2 != other.d1
      raise ArgumentError.new
    end
    d3 = other.d2
    tmp = Array.new(d3) { Array.new(@d2, T.zero) }
    d3.times do |i|
      tr1 = tmp[i]
      @d2.times do |j|
        tr1[j] = other[j][i]
      end
    end
    ret = Array.new(@d1) { Array.new(d3, T.zero) }
    @d1.times do |i|
      row1 = @a[i]
      ret1 = ret[i]
      d3.times do |j|
        sum = T.zero
        row2 = tmp[j]
        @d2.times do |k|
          sum += row1[k] * row2[k]
        end
        ret1[j] = sum
      end
    end
    @a = ret
  end

  def pow(p : Int64, m : Int64)
    b = Matrix(T).new(@d1, @d2)
    ret = Matrix(T).new(@d1, @d2)
    @d1.times do |i|
      @d2.times do |j|
        b.a[i][j] = @a[i][j] % m
      end
      ret.a[i][i] = 1
    end
    while p > 0
      if (p & 1) != 0
        ret.mul(b, m)
      end
      b.mul(b, m)
      p >>= 1
    end
    return ret
  end
end

class AndXor
  property :v

  def initialize(v : UInt32)
    @v = v
  end

  def self.zero
    return self.new(0u32)
  end

  def +(other : AndXor)
    return self.class.new(@v ^ other.v)
  end

  def *(other : AndXor)
    return self.class.new(@v & other.v)
  end
end
